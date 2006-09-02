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

import java.io.*;
import org.jgap.*;
import org.jgap.event.*;
import org.jgap.gp.*;
import org.jgap.gp.impl.*;
import org.jgap.gp.function.*;
import org.jgap.gp.terminal.*;

/**
 * Example demonstrating Genetic Programming (GP) capabilities of JGAP.<p>
 * Here, the Fibonacci sequence is calculated (only integers are used).<p>
 * Please note: We try to find an approximation formula, not a program that
 * computes Fibonacci iteratively or recursively (as in method Fib(int), see
 * below). Of course this is an oversimplification to show the principle of GP
 * only.<p>
 * This example utilizes a INodeValidator (see FibonacciNodeValidator).<p>
 * Each new best solution found will we shown as a tree representing the GP.
 * The tree is written to a PNG-imagefile onto harddisk.<p>
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class Fibonacci
    extends GPProblem {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.22 $";

  static Variable vx;

  static Variable va;

  private final static int NUMFIB = 10;

  static Integer[] x = new Integer[NUMFIB];

  static int[] y = new int[NUMFIB];

  public Fibonacci(GPConfiguration a_conf)
      throws InvalidConfigurationException {
    super(a_conf);
  }

  /**
   * Sets up the functions to use and other parameters. Then creates the
   * initial genotype.
   *
   * @return the genotype created
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public GPGenotype create()
      throws InvalidConfigurationException {
    Class[] types = {
        CommandGene.VoidClass, CommandGene.VoidClass, CommandGene.IntegerClass};
    Class[][] argTypes = { {}, {}, {}
    };
    int[] minDepths = new int[] {2, 3, 1};
    int[] maxDepths = new int[] {3, 9, 1};
    GPConfiguration conf = getGPConfiguration();
    /**@todo allow to optionally preset a static program in each chromosome*/
    CommandGene[][] nodeSets = { {
        new SubProgram(conf, new Class[] {CommandGene.VoidClass,
                       CommandGene.VoidClass}),
        new Constant(conf, CommandGene.IntegerClass, new Integer(1)),
        new StoreTerminal(conf, "mem0", CommandGene.IntegerClass),
        new StoreTerminal(conf, "mem1", CommandGene.IntegerClass),
        new Increment(conf, CommandGene.IntegerClass),
        new NOP(conf),
    }, {
        vx = Variable.create(conf, "X", CommandGene.IntegerClass),
        new AddAndStore(conf, CommandGene.IntegerClass, "mem2"),
        new ForLoop(conf, CommandGene.IntegerClass, 1, NUMFIB),
        new Increment(conf, CommandGene.IntegerClass, -1),
        new TransferMemory(conf, "mem2", "mem1"),
        new TransferMemory(conf, "mem1", "mem0"),
        new ReadTerminal(conf, CommandGene.IntegerClass, "mem0"),
        new ReadTerminal(conf, CommandGene.IntegerClass, "mem1"),
        new SubProgram(conf, new Class[] {CommandGene.VoidClass,
                       CommandGene.VoidClass, CommandGene.VoidClass}),
    }, {
        // Commands will be added programmatically, see below.
        // ---------------------------------------------------
    }
    };
    // Add commands working with internal memory.
    // ------------------------------------------
    nodeSets[2] = CommandFactory.createReadOnlyCommands(nodeSets[2], conf,
        CommandGene.IntegerClass, "mem", 1, 2, !true);
    // Randomly initialize function data (X-Y table) for Fib(x).
    // ---------------------------------------------------------
    for (int i = 0; i < NUMFIB; i++) {
      int index = i;
      x[i] = new Integer(index);
      y[i] = fib_iter(index);
      System.out.println(i + ") " + x[i] + "   " + y[i]);
    }
    // Create genotype with initial population.
    // ----------------------------------------
    return GPGenotype.randomInitialGenotype(conf, types, argTypes, nodeSets,
        minDepths, maxDepths, 400, new boolean[] {!true, !true, false}, true);
  }

  //(Sort of) This is what we would like to (and can) find via GP:
  private static int fib_iter(int a_index) {
    // 1
    if (a_index == 0 || a_index == 1) {
      return 1;
    }
    // 2
    int a = 1; //Store("mem0", Constant(1))
    int b = 1; //Store("mem1", Constant(1))
    int x = 0; //Store("mem2", Constant(0))
    // 3
    for (int i = 2; i <= a_index; i++) { //FORX (Subprogram(A;B;C))
      x = a + b; // A: AddAndStore(Read("mem0"),Read("mem1"),"mem2")
      a = b; //B: TransferMemory("mem1","mem0")
      b = x; //C: TransferMemory("mem2","mem1")
    }
    return x; //Read("mem2")
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

  //(Sort of) This is what we would like to (but cannot) find via GP:
  private static int fib(int a_index) {
    if (a_index == 0 || a_index == 1) {
      return 1;
    }
    return fib(a_index - 1) + fib(a_index - 2);
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
      int popSize;
      if (args.length == 1) {
        popSize = Integer.parseInt(args[0]);
      }
      else {
        popSize = 1200;
      }
      System.out.println("Using population size of " + popSize);
      config.setMaxInitDepth(6);
      config.setPopulationSize(popSize);
      config.setFitnessFunction(new Fibonacci.FormulaFitnessFunction());
      config.setStrictProgramCreation(false);
      config.setProgramCreationMaxTries(5);
      // Set a node validator to demonstrate speedup when something is known
      // about the solution (see FibonacciNodeValidator).
      // -------------------------------------------------------------------
      config.setNodeValidator(new FibonacciNodeValidator());
      final GPProblem problem = new Fibonacci(config);
      GPGenotype gp = problem.create();
      gp.setVerboseOutput(true);
      final Thread t = new Thread(gp);
      // Simple implementation of running evolution in a thread.
      // -------------------------------------------------------
      config.getEventManager().addEventListener(GeneticEvent.
          GPGENOTYPE_EVOLVED_EVENT, new GeneticEventListener() {
        public void geneticEventFired(GeneticEvent a_firedEvent) {
          GPGenotype genotype = (GPGenotype) a_firedEvent.getSource();
          int evno = genotype.getGPConfiguration().getGenerationNr();
          double freeMem = GPGenotype.getFreeMemoryMB();
          if (evno % 50 == 0) {
            double bestFitness = genotype.getFittestProgram().
                getFitnessValue();
            System.out.println("Evolving generation " + evno
                               + ", best fitness: " + bestFitness
                               + ", memory free: " + freeMem + " MB");
          }
          if (evno > 3000) {
            t.stop();
          }
          else {
            try {
              // Collect garbage if memory low.
              // ------------------------------
              if (freeMem < 50) {
                System.gc();
                t.sleep(500);
              }
              else {
                // Avoid 100% CPU load.
                // --------------------
                t.sleep(30);
              }
            } catch (InterruptedException iex) {
              iex.printStackTrace();
              System.exit(1);
            }
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
          int evno = genotype.getGPConfiguration().getGenerationNr();
          String indexString = "" + evno;
          while (indexString.length() < 5) {
            indexString = "0" + indexString;
          }
          String filename = "fibonacci_best" + indexString + ".png";
          IGPProgram best = genotype.getAllTimeBest();
          try {
            problem.showTree(best, filename);
          } catch (InvalidConfigurationException iex) {
            iex.printStackTrace();
          }
          double bestFitness = genotype.getFittestProgram().
              getFitnessValue();
          if (bestFitness < 0.001) {
            genotype.outputSolution(best);
            t.stop();
            System.exit(0);
          }
        }
      });
      t.start();
    } catch (Exception ex) {
      ex.printStackTrace();
      System.exit(1);
    }
  }

  public static class FormulaFitnessFunction
      extends GPFitnessFunction {
    protected double evaluate(final IGPProgram a_subject) {
      return computeRawFitness(a_subject);
    }

    public double computeRawFitness(final IGPProgram a_program) {
      double error = 0.0f;
      Object[] noargs = new Object[0];
      // Initialize local stores.
      // ------------------------
      GPGenotype.getGPConfiguration().clearStack();
      GPGenotype.getGPConfiguration().clearMemory();
      // Compute fitness for each program.
      // ---------------------------------
      /**@todo check if program valid, i.e. worth evaluating*/
      for (int i = 2; i < NUMFIB; i++) {
        for (int j = 0; j < a_program.size(); j++) {
          vx.set(x[i]);
          try {
            try {
              // Init. params (a_program.getTypes()) distinguish program flow.
              // This could be coded dynamically but that would slow down
              // things a lot.
              // -------------------------------------------------------------
              if (j == a_program.size() - 1) {
                // Only evaluate after whole GP program was run.
                // ---------------------------------------------
                double result = a_program.execute_int(j, noargs);
                error += Math.abs(result - y[i]);
              }
              else {
                // Execute memory manipulating subprograms.
                // ----------------------------------------
                a_program.execute_void(j, noargs);
              }
            } catch (IllegalStateException iex) {
              error = GPFitnessFunction.MAX_FITNESS_VALUE;
              break;
            }
          } catch (ArithmeticException ex) {
            System.out.println("x = " + x[i].intValue());
            System.out.println(a_program.getChromosome(j));
            throw ex;
          }
        }
      }
      if (GPGenotype.getGPConfiguration().stackSize() > 0) {
        error = GPFitnessFunction.MAX_FITNESS_VALUE;
      }
      if (error < 0.000001) {
        error = 0.0d;
      }
      else if (error < GPFitnessFunction.MAX_FITNESS_VALUE) {
        /**@todo add penalty for longer solutions*/
      }
      return error;
    }
  }
}
