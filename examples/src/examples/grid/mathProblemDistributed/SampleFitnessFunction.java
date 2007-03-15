/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.grid.mathProblemDistributed;

import org.jgap.*;
import org.jgap.impl.*;
import examples.gp.MathProblem;
import org.jgap.gp.IGPProgram;
import org.jgap.gp.GPFitnessFunction;
import org.jgap.gp.terminal.Variable;

/**
 * Fitness function for our example.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public class SampleFitnessFunction
        extends GPFitnessFunction {
      /** String containing the CVS revision. Read out via reflection!*/
      private final static String CVS_REVISION = "$Revision: 1.1 $";

      static Variable vx;

      static Float[] x = new Float[20];/**@todo initialize*/

      static float[] y = new float[20];/**@todo initialize*/

      protected double evaluate(final IGPProgram a_subject) {
        return computeRawFitness(a_subject);
      }

      public double computeRawFitness(final IGPProgram ind) {
        double error = 0.0f;
        Object[] noargs = new Object[0];
        Variable vx = ind.getGPConfiguration().getVariable("X");
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
