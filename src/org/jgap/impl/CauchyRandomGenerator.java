/*
 * This file is part of JGAP.
 *
 * JGAP is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * JGAP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser Public License
 * along with JGAP; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package org.jgap.impl;

import java.util.Random;
import org.jgap.RandomGenerator;

/**@todo not ready yet*/


/**
 * Cauchy probability density function
 * @see http://www.itl.nist.gov/div898/handbook/eda/section3/eda3663.htm
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class CauchyRandomGenerator
    implements RandomGenerator {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";
  private static final double DELTA = 0.0000001;
  private double m_standardDistribution;
  private Random rn = new Random();
  public CauchyRandomGenerator() {
    this(1);
  }

  public CauchyRandomGenerator(double a_standardDistribution) {
    m_standardDistribution = a_standardDistribution;
  }

  /**
   * Calculates a random density of the cauchy distribution
   * @return calculated density
   *
   * @since 1.1
   */
  private int calculateDensity() {
    //compute (standard) cauchy distribution:
    //f(x) = (1/[pi(1+x²)])
    //----------------------------
    int result;
    double rate;
    double v1 = 10 * nextDouble() - 5; // between -5 and 5
    //invert the result as higher values indicate less probable mutation ???
    //------------------------------------------------------------------
    rate = /*1 / */ (Math.PI * (1 + v1 * v1));
    result = (int) Math.round(rate);
    return result;
  }

  public int nextInt() {
    return Math.min(Integer.MAX_VALUE - 1,
                    (int) Math.round(nextCauchy() * Integer.MAX_VALUE));
  }

  public int nextInt(int ceiling) {
    return Math.min(ceiling - 1,
                    (int) Math.round(nextCauchy() * ceiling));
  }

  public long nextLong() {
    return Math.min(Long.MAX_VALUE - 1,
                    Math.round(nextCauchy() * Long.MAX_VALUE));
  }

  public double nextDouble() {
    return nextCauchy();
  }

  public float nextFloat() {
    return Math.min(Float.MAX_VALUE - 1,
                    (float) (nextCauchy() * Float.MAX_VALUE));
  }

  public boolean nextBoolean() {
    return nextCauchy() >= 0.5d;
  }

  public double nextCauchy() {
    return calculateDensity(); // * getGaussianStdDeviation ();
  }

  public void setCauchyStandardDeviation(double a_standardDeviation) {
    m_standardDistribution = a_standardDeviation;
  }

  public double getCauchyStandardDeviation() {
    return m_standardDistribution;
  }
}
