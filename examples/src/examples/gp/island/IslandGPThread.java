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

/**
 * Simple GP example of an island thread. It utilizes the former example class
 * SimpleExample in the gp package for reasons of simplicity.
 *
 * @author Klaus Meffert
 * @since 3.6
 */
public class IslandGPThread
    extends GPProblem implements Runnable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.4 $";

  private GPGenotype gen;

  private int m_nextNumber;

  private IGPProgram m_best;

  private boolean m_finished;

  private static GPConfiguration config;

  protected static Variable vx;

  public GPConfiguration createConfiguration(String a_threadKey)
      throws Exception {
    if (config == null) {
      System.out.println("  Creating new configuration");
      config = new GPConfiguration(a_threadKey, a_threadKey);
      config.setMaxInitDepth(5);
      config.setPopulationSize(150);
      config.setFitnessFunction(new SimpleFitnessFunction());
      config.setStrictProgramCreation(false);
      config.setProgramCreationMaxTries(5);
      config.setPreservFittestIndividual(true);
      config.setMaxCrossoverDepth(5);
      // Lower fitness value is better as it indicates error rate.
      // ---------------------------------------------------------
      config.setGPFitnessEvaluator(new DeltaGPFitnessEvaluator());
      return config;
    }
    else {
      System.out.println("  Cloning configuration");
      GPConfiguration configClone = (GPConfiguration) config.newInstanceGP(
          a_threadKey, a_threadKey);
      return configClone;
    }
  }

  private IslandGPThread(int nextNumber)
      throws Exception {
  }

  public static IslandGPThread newInstance(int nextNumber)
      throws Exception {
    IslandGPThread inst = new IslandGPThread(nextNumber);
    inst.m_nextNumber = nextNumber;
    String threadKey = Thread.currentThread().getId() + "/" + inst.m_nextNumber;
    System.out.println("Starting thread: " + nextNumber);
    GPConfiguration config = inst.createConfiguration(threadKey);
    inst.setGPConfiguration(config);
    inst.gen = inst.create();
    return inst;
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
        // Easiest: Define four constants
        // More challenging: Define a terminal that can be within -10 and 10
//        new Constant(conf, CommandGene.IntegerClass, 0),
//        new Constant(conf, CommandGene.IntegerClass, 3),
//        new Constant(conf, CommandGene.IntegerClass, -5),
//        new Constant(conf, CommandGene.IntegerClass, -8),
        new Terminal(conf, CommandGene.IntegerClass, -10, 10, true),
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
        new True(conf, CommandGene.BooleanClass),
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
      for (int i = 1; i <= 100; i++) {
        gen.evolve(2);
        synchronized(this) {
            IGPProgram best = gen.getFittestProgram();
            if(best != null) {
              if(m_best != null) {
                if (best.getFitnessValue() < m_best.getFitnessValue()) {
                  m_best = best;
                }
              }
              else {
                m_best = best;
              }
            }

        }
        Thread.currentThread().sleep( (int) Math.random() *
                                     (20 + m_nextNumber * 5));
      }
      m_finished = true;
      // Use a lock to avoid cluttered output of best solution.
      // ------------------------------------------------------
      while (locked) {
        Thread.currentThread().sleep(5);
      }
      try {
        locked = true;
        outputBestSolution();
      } finally {
        locked = false;
      }
    } catch (InterruptedException iex) {
      // Do nothing, this is an intended exception.
      // ------------------------------------------
      ;
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Output the currently best solution for the current thread.
   */
  public void outputBestSolution() {
    System.out.println("Thread " + m_nextNumber + ": Best solution:");
    if (m_best == null) {
      System.out.println(" None yet.");
    }
    else {
      System.out.println("  Fitness value: " +
                         m_best.getFitnessValue());
      System.out.println("  Solution: " + m_best.toStringNorm(0));
    }
    System.out.println("----");
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

  public GPPopulation getPopulation() {
    return gen.getGPPopulation();
  }

  class SimpleFitnessFunction
      extends GPFitnessFunction {
    protected double evaluate(final IGPProgram ind) {
      int error = 0;
      Object[] noargs = new Object[0];
      int maxDepth = ind.getChromosome(0).getDepth(0);
      if (maxDepth > 4) {
        // penalty deeper  programs.
        // ------------------------
        error += maxDepth - 4;
      }
      int size = ind.getChromosome(0).getSize(0);
      if (size > 14) {
        // penalty longer programs (they seem to have redundant elements).
        // ---------------------------------------------------------------
        error += size - 14;
      }
      for (int i = -10; i < 10; i++) {
        vx.set(new Integer(i));
        boolean y;
        if (i > 0) {
          if (i == 3) {
            y = false;
          }
          else {
            y = true;
          }
        }
        else {
          if (i == -8 || i == -5) {
            y = true;
          }
          else {
            y = false;
          }
        }
        try {
          boolean result = ind.execute_boolean(0, noargs);
          if (result != y) {
            error += 10;
          }
        } catch (ArithmeticException ex) {
          // Some illegal operation was executed.
          // ------------------------------------
          System.out.println("x = " + i);
          System.out.println(ind);
          throw ex;
        }
      }
//      if (error < 1) {
//        System.out.println("BEST: "+ind.toStringNorm(0));
//        ind.execute_boolean(0, noargs);
//      }
      return error;
    }
  }
}
