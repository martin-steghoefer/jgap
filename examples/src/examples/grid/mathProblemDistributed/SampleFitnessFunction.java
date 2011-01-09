/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.grid.mathProblemDistributed;

import java.util.*;
import org.jgap.gp.*;
import org.jgap.gp.terminal.*;
import org.apache.log4j.*;

/**
 * Fitness function for our example.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public class SampleFitnessFunction
    extends GPFitnessFunction {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.8 $";

  private static Logger log = Logger.getLogger(SampleFitnessFunction.class);

  static Variable vx;

  static Float[] x = new Float[20];

  static float[] y = new float[20];

  public SampleFitnessFunction() {
    init();
  }

  public void init() {
    Random random = new Random();
    // Randomly initialize function data (X-Y table) for x^4+x^3+x^2-x
    // ---------------------------------------------------------------
    for (int i = 0; i < 20; i++) {
      float f = 8.0f * (random.nextFloat() - 0.3f);
      x[i] = new Float(f);
      y[i] = f * f * f * f + f * f * f + f * f - f;
      log.debug(i + ") " + x[i] + "   " + y[i]);
    }
  }

  protected double evaluate(final IGPProgram a_subject) {
    return computeRawFitness(a_subject);
  }

  public double computeRawFitness(final IGPProgram ind) {
    double error = 0.0f;
    Object[] noargs = new Object[0];
    Variable vx = ind.getGPConfiguration().getVariable("X");
    if (vx == null) {
      log.error("Variable X not initialized correctly!");
      return GPFitnessFunction.MAX_FITNESS_VALUE;
    }
    if(x[0] == null) {
      init();
    }
    for (int i = 0; i < 20; i++) {
      vx.set(x[i]);
      try {
        double result = ind.execute_float(0, noargs);
        error += Math.abs(result - y[i]);
      } catch (ArithmeticException ex) {
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
