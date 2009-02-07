/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.math.ga;

import org.jgap.*;
import examples.math.*;

/**
 * The fitnessfunction of the example. Allows to specify a target number that
 * should be matched as best as possible with a formula to be evolved.
 *
 * @author Michael Grove
 * @since 3.4.2
 */
public class MathRunnerFitnessFunction
    extends FitnessFunction {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  private PhenotypeExpresser<Double> mExpresser;

  private double mTarget;

  public MathRunnerFitnessFunction(double theTarget, PhenotypeExpresser<Double>
      theExpr) {
    mTarget = theTarget;
    mExpresser = theExpr;
  }

  protected double evaluate(IChromosome theIChromosome) {
    double aValue = mExpresser.express(theIChromosome);
    return Math.abs(mTarget - aValue);
  }
}
