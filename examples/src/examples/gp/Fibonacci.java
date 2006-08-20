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
public class Fibonacci
    extends GPGenotype {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  static Variable vx;

  static Variable va;

  private final static int NUMFIB = 10;

  static Integer[] x = new Integer[NUMFIB];

  static int[] y = new int[NUMFIB];

  public Fibonacci(Population a_pop)
      throws InvalidConfigurationException {
    super(getGPConfiguration(), a_pop);
  }

  public static GPGenotype create(GPConfiguration a_conf)
      throws InvalidConfigurationException {
    Class[] types = {
        CommandGene.IntegerClass};
    Class[][] argTypes = {
        {}
    };
    CommandGene[][] nodeSets = {
        {
        vx = Variable.create(a_conf, "X", CommandGene.IntegerClass),
//        new FreeVariable(a_conf, CommandGene.IntegerClass,"a"),
//        new FreeVariable(a_conf, CommandGene.IntegerClass,"b"),
//        new Terminal(a_conf, 0,100),
//        new Terminal(a_conf, 0,100),
        new AddCommand(a_conf, CommandGene.IntegerClass),
        new IncrementCommand(a_conf, CommandGene.IntegerClass),
//        new MultiplyCommand(a_conf, CommandGene.IntegerClass),
        new PushCommand(a_conf, CommandGene.IntegerClass),
        new ForCommand(a_conf, CommandGene.IntegerClass),
        new PopCommand(a_conf, CommandGene.IntegerClass),
    }
    };
    Random random = new Random();
    // randomly initialize function data (X-Y table) for Fib(x)
    for (int i = 0; i < NUMFIB; i++) {
      int index = random.nextInt(NUMFIB * 2);
      x[i] = new Integer(index);
      y[i] = Fib(index);
      System.out.println(i + ") " + x[i] + "   " + y[i]);
    }
    // Create genotype with initial population
    return randomInitialGenotype(a_conf, types, argTypes, nodeSets);
  }

  private static int Fib(int a_index) {
    if (a_index == 0 || a_index == 1) {
      return 1;
    }
    return Fib(a_index - 1) + Fib(a_index - 2);
  }

  //(Sort of) This is what we would like to find via GP:
  private static int Fib_iter(int a_index) {
    if (a_index == 0 || a_index == 1) {
      return 1;
    }
    int a = 0;
    int b = 0;
    int c = 0;
    for (int i = 2; i <= a_index; i++) {
      c = a + b;
      a = b;
      b = c;
    }
    return c;
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
      System.out.println("Formula to discover: Fibonacci(x)");
      GPConfiguration config = new GPConfiguration();
      config.setMaxInitDepth(8);
      config.setPopulationSize(800);
      config.setFitnessFunction(new Fibonacci.FormulaFitnessFunction());
      GPGenotype gp = create(config);
      gp.evolve(1200);
      gp.outputSolution(gp.getAllTimeBest());
    }
    catch (Exception ex) {
      System.exit(1);
    }
  }

  public static class FormulaFitnessFunction
      extends FitnessFunction {
    protected double evaluate(IChromosome a_subject) {
      return computeRawFitness( (ProgramChromosome) a_subject);
    }

    public double computeRawFitness(ProgramChromosome ind) {
      double error = 0.0f;
      Object[] noargs = new Object[0];
      for (int i = 0; i < NUMFIB; i++) {
        vx.set(x[i]);
        try {
          try {
            double result = ind.execute_int(noargs);
            error += Math.abs(result - y[i]);
          }
          catch (IllegalStateException iex) {
            error = Double.MAX_VALUE / 2;
            break;
          }
        }
        catch (ArithmeticException ex) {
          System.out.println("x = " + x[i].intValue());
          System.out.println(ind);
          throw ex;
        }
      }
      if (getGPConfiguration().stackSize() > 0) {
        error = Double.MAX_VALUE / 2;
      }
      if (error < 0.000001) {
        error = 0.0d;
      }
      return error;
    }
  }
}
