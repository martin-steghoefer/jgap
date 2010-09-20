/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.gp;

import org.jgap.*;
import org.jgap.event.*;
import org.jgap.gp.*;
import org.jgap.gp.function.*;
import org.jgap.gp.impl.*;
import org.jgap.gp.terminal.*;
import org.jgap.util.*;

/**
 * Simple example of Genetic Programming to discover the formula
 * (X > 0) OR (X == -8) OR (X == - 5)
 * given a set of inputs from -10 to 10 and expected outputs (true or false).
 *
 * The fitness function used in this example cares about deviation from the
 * expected result as well as the complexity of the solution (the easier the
 * solution, the better it is, mutatis mutandis).
 *
 * @author Klaus Meffert
 * @since 3.5
 */
public class SimpleExample
    extends GPProblem {

  protected static Variable vx;

  public GPGenotype create()
      throws InvalidConfigurationException {
    GPConfiguration conf = getGPConfiguration();
    // The resulting GP program returns a boolean.
    // -------------------------------------------
    Class[] types = {CommandGene.BooleanClass};
    Class[][] argTypes = { {}
    };
    // The commands and terminals allowed to find a solution.
    // ------------------------------------------------------
    CommandGene[][] nodeSets = { {
        // We need a variable to feed in data (see fitness function).
        // ----------------------------------------------------------
        vx = Variable.create(conf, "X", CommandGene.IntegerClass),
        // Define the terminals (here: numbers) to try:
        // Easiest: Define two constants
        // More challenging: Define a terminal that can be within -10 and 10
//        new Constant(conf, CommandGene.IntegerClass, 0),
//        new Constant(conf, CommandGene.IntegerClass, -8),
        new Terminal(conf, CommandGene.IntegerClass,-10,10,true),
        // Boolean operators to use.
        // _------------------------
        new GreaterThan(conf, CommandGene.IntegerClass),
        new Or(conf),
        new Equals(conf, CommandGene.IntegerClass),
        // Define complex operator.
        // ------------------------
        new If(conf, CommandGene.BooleanClass),
        // Boolean terminals to use (they do not appear in an optimal solution
        // and make the task more challenging) --> Leave away if needed
        // -------------------------------------------------------------------
//        new True(conf, CommandGene.BooleanClass),
        new False(conf, CommandGene.BooleanClass)
    }
    };
    // Initialize the GPGenotype.
    // --------------------------
    return GPGenotype.randomInitialGenotype(conf, types, argTypes, nodeSets,
        100, true);
  }

  public void start()
      throws Exception {
    GPConfiguration config = new GPConfiguration();
    config.setMaxInitDepth(5);
    config.setPopulationSize(80);
    config.setFitnessFunction(new SimpleFitnessFunction());
    config.setStrictProgramCreation(false);
    config.setProgramCreationMaxTries(5);
    config.setMaxCrossoverDepth(5);
    // Lower fitness value is better as fitness value indicates error rate.
    // --------------------------------------------------------------------
    config.setGPFitnessEvaluator(new DeltaGPFitnessEvaluator());
    super.setGPConfiguration(config);
    GPGenotype geno = create();
    // Simple implementation of running evolution in a thread.
    // -------------------------------------------------------
    config.getEventManager().addEventListener(GeneticEvent.
        GPGENOTYPE_EVOLVED_EVENT, new GeneticEventListener() {
      public void geneticEventFired(GeneticEvent a_firedEvent) {
        GPGenotype genotype = (GPGenotype) a_firedEvent.getSource();
        int evno = genotype.getGPConfiguration().getGenerationNr();
        double freeMem = SystemKit.getFreeMemoryMB();
        if (evno % 100 == 0) {
          IGPProgram best = genotype.getAllTimeBest();
          System.out.println("Evolving generation " + evno);
          genotype.outputSolution(best);
        }
        if (evno > 3000) {
          System.exit(1);
        }
      }
    });
    config.getEventManager().addEventListener(GeneticEvent.
        GPGENOTYPE_NEW_BEST_SOLUTION, new GeneticEventListener() {
      /**
       * New best solution found.
       *
       * @param a_firedEvent GeneticEvent
       */
      public void geneticEventFired(GeneticEvent a_firedEvent) {
        GPGenotype genotype = (GPGenotype) a_firedEvent.getSource();
        IGPProgram best = genotype.getAllTimeBest();
        double bestFitness = genotype.getFittestProgram().
            getFitnessValue();
        if (bestFitness < 0.1) {
          // Quit, when the solutions seems perfect.
          // ---------------------------------------
          genotype.outputSolution(best);
          System.exit(0);
        }
      }
    });
    geno.evolve(10000);
  }

  public static void main(String[] args)
      throws Exception {
    SimpleExample example = new SimpleExample();
    example.start();
  }

  class SimpleFitnessFunction
      extends GPFitnessFunction {
    protected double evaluate(final IGPProgram ind) {
      int error = 0;
      Object[] noargs = new Object[0];
      int maxDepth = ind.getChromosome(0).getDepth(0);
      if (maxDepth > 2) {
        error += maxDepth - 2;
      }
      for (int i = -10; i < 10; i++) {
        vx.set(new Integer(i));
        boolean y;
        if (i > 0) {
          y = true;
        }
        else {
          if (i != -8 && i != - 5) {
            y = false;
          }
          else {
            y = true;
          }
        }
        try {
          boolean result = ind.execute_boolean(0, noargs);
          if (result != y) {
            error += 10;
          }
        } catch (ArithmeticException ex) { // some illegal operation was executed.
          System.out.println("x = " + i);
          System.out.println(ind);
          throw ex;
        }
      }
      return error;
    }
  }
}
