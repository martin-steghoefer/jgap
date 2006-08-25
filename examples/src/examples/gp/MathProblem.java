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
import org.jgap.gp.terminal.*;
import org.jgap.gp.function.*;

/**
 * Example demonstrating Genetic Programming (GP) capabilities of JGAP.<p>
 * The problem is to find a formula for a given truth table (X/Y-pairs).
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class MathProblem {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.5 $";

  static Variable vx;

  static Float[] x = new Float[20];

  static float[] y = new float[20];

  public static GPGenotype create(GPConfiguration a_conf)
      throws InvalidConfigurationException {
    Class[] types = {
        CommandGene.FloatClass};
    Class[][] argTypes = {
        {}
    };
    CommandGene[][] nodeSets = {
        {
        vx = Variable.create(a_conf, "X", CommandGene.FloatClass),
        new Add(a_conf, CommandGene.FloatClass),
        new Add3(a_conf, CommandGene.FloatClass),
        new Subtract(a_conf, CommandGene.FloatClass),
        new Multiply(a_conf, CommandGene.FloatClass),
        new Multiply3(a_conf, CommandGene.FloatClass),
        new Divide(a_conf, CommandGene.FloatClass),
        new Sine(a_conf, CommandGene.FloatClass),
        new Exp(a_conf, CommandGene.FloatClass),
        new Pow(a_conf, CommandGene.FloatClass),
        new Terminal(a_conf, CommandGene.FloatClass,3.0d, 4.0d),
    }
    };
    Random random = new Random();
    // randomly initialize function data (X-Y table) for x^4+x^3+x^2-x
    for (int i = 0; i < 20; i++) {
      float f = 8.0f * (random.nextFloat() - 0.3f);
      x[i] = new Float(f);
      y[i] = f * f * f * f + f * f * f + f * f - f;
      System.out.println(i + ") " + x[i] + "   " + y[i]);
    }
    // Create genotype with initial population.
    // ----------------------------------------
    return GPGenotype.randomInitialGenotype(a_conf, types, argTypes, nodeSets,
        400);
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
  public static void main(String[] args)
      throws Exception {
    System.out.println("Formula to discover: X^4 + X^3 + X^2 - X");
    GPConfiguration config = new GPConfiguration();
    config.setMaxInitDepth(5);
    config.setPopulationSize(1000);
    config.setFitnessFunction(new MathProblem.FormulaFitnessFunction());
    GPGenotype gp = create(config);
    gp.evolve(800);
    gp.outputSolution(gp.getAllTimeBest());
  }

  public static class FormulaFitnessFunction
      extends GPFitnessFunction {
    protected double evaluate(GPProgram a_subject) {
      return computeRawFitness(a_subject);
    }

    public double computeRawFitness(GPProgram ind) {
      double error = 0.0f;
      Object[] noargs = new Object[0];
      for (int i = 0; i < 20; i++) {
        vx.set(x[i]);
        try {
          double result = ind.execute_float(0, noargs);
          error += Math.abs(result - y[i]);
        }
        catch (ArithmeticException ex) {
          System.out.println("x = " + x[i].floatValue());
          System.out.println(ind);
          throw ex;
        }
      }
      if (error < 0.000001) {
        error = 0.0d;
      }
      return error;
    }
  }
}
