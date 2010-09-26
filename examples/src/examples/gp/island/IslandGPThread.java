/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.gp.island;

import org.jgap.*;
import org.jgap.gp.*;
import org.jgap.gp.function.*;
import org.jgap.gp.impl.*;
import org.jgap.gp.terminal.*;

import examples.*;

/**
 * Simple example of an island thread. It utilizes the former example class
 * MinimizingMakeChangeFitnessFunction.
 *
 * @author Klaus Meffert
 * @since 3.6
 */
public class IslandGPThread
    extends GPProblem implements Runnable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  private GPGenotype gen = null;

  private int m_nextNumber;

  private IGPProgram m_best;

  private boolean m_finished;

  protected static Variable vx;

  public IslandGPThread(int nextNumber)
      throws Exception {
    m_nextNumber = nextNumber;
    String threadKey = Thread.currentThread().getId() + "/" + m_nextNumber;
    System.out.println("Starting thread: " + nextNumber);
    GPConfiguration config = new GPConfiguration(threadKey, threadKey);
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
    gen = create();
  }

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

  static boolean locked = false;

  public void run() {
    try {
      for (int i = 1; i <= 500; i++) {
        gen.evolve(5);
        Thread.currentThread().sleep( (int) Math.random() *
                                     (20 + m_nextNumber * 5));
      }
      m_finished = true;
      m_best = gen.getFittestProgram();;
      // Use a lock to avoid cluttered output of best solution.
      // ------------------------------------------------------
      while (locked) {
        Thread.currentThread().sleep(1);
      }
      try {
        locked = true;
        System.out.println("Thread " + m_nextNumber + ": Best solution:");
        if(m_best == null) {
          System.out.println(" None yet.");
        }
        else {
            System.out.println("  Fitness value: " +
                               m_best.getFitnessValue());
            System.out.print("  Solution: " + m_best.toStringNorm(0));
        }
        System.out.println("----");
      } finally {
        locked = false;
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public boolean isFinished() {
    return m_finished;
  }

  public IGPProgram getBestSolution() {
    if (!m_finished) {
      throw new RuntimeException("Thread not finished yet!");
    }
    return m_best;
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
