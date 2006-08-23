/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.gp;

import java.util.*;
import org.jgap.*;
import org.jgap.event.*;
import org.jgap.gp.*;

/**
 * Example demonstrating Genetic Programming (GP) capabilities of JGAP.<p>
 * Here, the Fibonacci sequence is calculated (only integers are used).<p>
 * Please note: We try to find an approximation formula, not a program that
 * computes Fibonacci iteratively or recursively (as in method Fib(int), see
 * below). Of course this is an oversimplification to show the principle of GP
 * only.<p>
 *
 * @todo advance this example to full-blown functionality
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class Fibonacci {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.8 $";

  static Variable vx;

  static Variable va;

  private final static int NUMFIB = 10;

  static Integer[] x = new Integer[NUMFIB];

  static int[] y = new int[NUMFIB];

  public static GPGenotype create(GPConfiguration a_conf)
      throws InvalidConfigurationException {
    Class[] types = {
        CommandGene.VoidClass, CommandGene.IntegerClass};
    Class[][] argTypes = {
        {}, {}, //CommandGene.IntegerClass},
    };
    int[] maxDepths = new int[]{3, 12};
    /**@todo allow to optionally preset a static program in each chromosome*/
    CommandGene[][] nodeSets = {
        {
          new SubProgramCommand(a_conf, new Class[] {CommandGene.VoidClass,
                                CommandGene.VoidClass, CommandGene.VoidClass,}, 3),
        new Constant(a_conf, CommandGene.IntegerClass, new Integer(1)),
        new Constant(a_conf, CommandGene.IntegerClass, new Integer(0)),
        new StoreTerminalCommand(a_conf, "mem0", CommandGene.IntegerClass),
        new StoreTerminalCommand(a_conf, "mem1", CommandGene.IntegerClass),
        new StoreTerminalCommand(a_conf, "mem2", CommandGene.IntegerClass),
        new IncrementCommand(a_conf, CommandGene.IntegerClass, 1),
        new PushCommand(a_conf, CommandGene.IntegerClass),
        new NOP(a_conf),
//        new ADF(a_conf, 1),
    }, {
        vx = Variable.create(a_conf, "X", CommandGene.IntegerClass),
        new AddCommand(a_conf, CommandGene.IntegerClass),
        new ForXCommand(a_conf, CommandGene.IntegerClass),
//        new SubProgramCommand(a_conf,
//                              new Class[] {CommandGene.IntegerClass,
//                              CommandGene.IntegerClass,
//                              CommandGene.IntegerClass}, 3),
//        new IncrementCommand(a_conf, CommandGene.IntegerClass, -1),
//        new ReadTerminalCommand(a_conf, CommandGene.IntegerClass,
//                                "thruput0"),
//        new ReadTerminalCommand(a_conf, CommandGene.IntegerClass,
//                                "thruput1"),
        //        new Terminal(a_conf, 0,100, CommandGene.IntegerClass),
        //        new ModCommand(a_conf, CommandGene.IntegerClass),
        //        new MultiplyCommand(a_conf, CommandGene.IntegerClass),
    }
    };
    // Add commands working with internal memory.
    // ------------------------------------------
    nodeSets[1] = CommandFactory.createStoreCommands(nodeSets[1], a_conf,
        CommandGene.IntegerClass, "mem", 3);
//    nodeSets[1] = CommandFactory.createStackCommands(nodeSets[1], a_conf,
//        CommandGene.IntegerClass);

    // Randomly initialize function data (X-Y table) for Fib(x).
    // ---------------------------------------------------------
    for (int i = 0; i < NUMFIB; i++) {
      int index = i;
      x[i] = new Integer(index);
      y[i] = fib_iter(index); //fib_array(index);
      System.out.println(i + ") " + x[i] + "   " + y[i]);
    }
    // Create genotype with initial population.
    // ----------------------------------------
    return GPGenotype.randomInitialGenotype(a_conf, types, argTypes, nodeSets,
                                            maxDepths);
  }
  //(Sort of) This is what we would like to (but cannot) find via GP:
  private static int fib(int a_index) {
    if (a_index == 0 || a_index == 1) {
      return 1;
    }
    return fib(a_index - 1) + fib(a_index - 2);
  }

  //(Sort of) This is what we would like to (and can) find via GP:
  private static int fib_iter(int a_index) {
    // 1
    if (a_index == 0 || a_index == 1) {
      return 1;
    }
    // 2
    int a = 1;
    int b = 1;
    int x = 0;
    // 3
    for (int i = 2; i <= a_index; i++) {
      x = a + b;
      a = b;
      b = x;
    }
    return x;
  }

  //(Sort of) This is what we would like to find via GP:
  private int fib_array(int a_index) {
    // 1
    if (a_index == 0 || a_index == 1) {
      return 1;
    }
    // 2
    int[] numbers = new int[a_index + 1];
    numbers[0] = numbers[1] = 1;
    // 3
    for (int i = 2; i <= a_index; i++) {
      numbers[i] = numbers[i - 1] + numbers[i - 2];
    }
    return numbers[a_index];
  }

  /**
   * Starts the example.
   *
   * @param args ignored
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public static void main(String[] args) {
    try {
      System.out.println("Program to discover: Fibonacci(x)");
      GPConfiguration config = new GPConfiguration();
      config.setMaxInitDepth(9);
      config.setPopulationSize(1200);
      config.setFitnessFunction(new Fibonacci.FormulaFitnessFunction());
      GPGenotype gp = create(config);
      final Thread t = new Thread(gp);
      // Simple implementation of running evolution in a thread.
      // -------------------------------------------------------
      config.getEventManager().addEventListener(GeneticEvent.
                                                GPGENOTYPE_EVOLVED_EVENT,
                                                new GeneticEventListener() {
        public void geneticEventFired(GeneticEvent a_firedEvent) {
          GPGenotype genotype = (GPGenotype) a_firedEvent.getSource();
          int evno = genotype.getConfiguration().getGenerationNr();
          double freeMem = GPGenotype.getFreeMemoryMB();
          if (evno % 50 == 0) {
            double bestFitness = genotype.getFittestProgram().
                getFitnessValue();
            System.out.println("Evolving generation " + evno
                               + ", best fitness: " + bestFitness
                               + ", memory free: " + freeMem + " MB");
          }
          if (evno > 300000) {
            t.stop();
          }
          else {
            // Collect garbage if memory low.
            // ------------------------------
            if (freeMem < 50) {
              System.gc();
            }
            try {
              // Avoid 100% CPU load
              t.sleep(30);
            }
            catch (InterruptedException iex) {
              iex.printStackTrace();
              System.exit(1);
            }
          }
        }
      });
      t.start();
//      gp.evolve(1200);
//      gp.outputSolution(gp.getAllTimeBest());
    }
    catch (Exception ex) {
      ex.printStackTrace();
      System.exit(1);
    }
  }

  public static class FormulaFitnessFunction
      extends GPFitnessFunction {
    protected double evaluate(GPProgram a_subject) {
      return computeRawFitness(a_subject);
    }

    public double computeRawFitness(GPProgram a_program) {
      double error = 0.0f;
      Object[] noargs = new Object[0];
      // Initialize local stores.
      // ------------------------
      GPGenotype.getGPConfiguration().clearStack();
      GPGenotype.getGPConfiguration().clearMemory();
      // Compute fitness for each program.
      // ---------------------------------
      for (int j = 0; j < a_program.size(); j++) {
        /**@todo check if program valid, i.e. worth evaluating*/
        for (int i = 2; i < NUMFIB; i++) {
          vx.set(x[i]);
          try {
            try {
//            double result = a_program.getChromosome(j).execute_int(noargs);
              // Only evaluate after whole GP program was run.
              // ---------------------------------------------
              if (j == a_program.size() - 1) {
                double result = a_program.execute_int(j, noargs);
                error += Math.abs(result - y[i]);
              }
              else {
                /**@todo use init. params to distinguish program flow*/
                a_program.execute_void(j, noargs);
              }
            }
            catch (IllegalStateException iex) {
              error = Double.MAX_VALUE / 2; /**@todo use constant*/
              break;
            }
          }
          catch (ArithmeticException ex) {
            System.out.println("x = " + x[i].intValue());
            System.out.println(a_program.getChromosome(j));
            throw ex;
          }
        }
      }
      if (GPGenotype.getGPConfiguration().stackSize() > 0) {
        error = Double.MAX_VALUE / 2; /**@todo use constant*/
      }
      if (error < 0.000001) {
        error = 0.0d;
      }
      else if (error < Double.MAX_VALUE / 2) { /**@todo use constant*/
        /**@todo add penalty for longer solutions*/
      }
      return error;
    }
  }
}
