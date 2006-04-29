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

import org.jgap.gp.*;
import org.jgap.*;
import java.util.*;

/**
 * Example demonstrating Genetic Programming (GP) capabilities of JGAP.<p>
 * The problem is to find a formula for a given truth table (X/Y-pairs).
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class MathProblem
    extends GPGenotype {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.5 $";

  static Variable vx;

  static Float[] x = new Float[20];

  static float[] y = new float[20];

  public MathProblem(Population a_pop)
      throws InvalidConfigurationException {
    super(a_pop);
  }

  public static GPGenotype create(GPConfiguration a_conf)
      throws InvalidConfigurationException {
    /**@todo this should be randomInitializeGenotype*/
    Class[] types = {
        CommandGene.floatClass};
    Class[][] argTypes = {
        {}
    };
    CommandGene[][] nodeSets = {
        {
        vx = Variable.create(a_conf, "X", CommandGene.floatClass),
        new AddCommand(a_conf, CommandGene.floatClass),
        new SubtractCommand(a_conf, CommandGene.floatClass),
        new MultiplyCommand(a_conf, CommandGene.floatClass),
//            new DivideCommand(a_conf, CommandGene.floatClass),
//        new SinCommand(a_conf, CommandGene.floatClass),
//        new CosCommand(a_conf, CommandGene.floatClass),
        new ExpCommand(a_conf, CommandGene.floatClass),
//        new NaturalLogarithmCommand(a_conf, CommandGene.floatClass),
    }
    };
    Random random = new Random();
    // randomly initialize function data (X-Y table) for x^4+x^3+x^2+x
    for (int i = 0; i < 20; i++) {
      float f = 2.0f * (random.nextFloat() - 0.5f);
      x[i] = new Float(f);
      y[i] = f * f * f * f + f * f * f + f * f - f;
      System.out.println(i + ") " + x[i] + "   " + y[i]);
    }
    // Create genotype with initial population
    return randomInitialGenotype(a_conf, types, argTypes, nodeSets);
  }

  // Method for testing purpose only during development phase
  public static void main(String[] args)
      throws Exception {
    System.out.println("Formula to discover: x^4+x^3+x^2-x");
    GPConfiguration config = new GPConfiguration();
    config.setMaxInitDepth(8);
    config.setPopulationSize(800);
    config.setFitnessFunction(new MathProblem.FormulaFitnessFunction());
    GPGenotype gp = create(config);
    gp.evolve(800);
    gp.outputSolution(gp.getAllTimeBest());
  }

  public static class FormulaFitnessFunction
      extends FitnessFunction {
    protected double evaluate(IChromosome a_subject) {
//      return 1.0f / (1.0f + computeRawFitness( (ProgramChromosome) a_subject));
      return computeRawFitness( (ProgramChromosome) a_subject);
    }

    public double computeRawFitness(ProgramChromosome ind) {
      double error = 0.0f;
      Object[] noargs = new Object[0];
      for (int i = 0; i < 20; i++) {
        vx.set(x[i]);
        try {
          double result = ind.execute_float(noargs);
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
