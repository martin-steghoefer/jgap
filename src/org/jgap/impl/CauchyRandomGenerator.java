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

import java.util.*;

import org.jgap.*;

/**
 * Cauchy probability density function
 * @see http://www.itl.nist.gov/div898/handbook/eda/section3/eda3663.htm
 * (cumulative distribution function)
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class CauchyRandomGenerator
    implements RandomGenerator {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.5 $";

  //delta for distinguishing whether a value is to be interpreted as zero
  private static final double DELTA = 0.000001;

  private double m_scale;
  private double m_location;

  private Random rn;

  public CauchyRandomGenerator() {
    this(0.0d, 1.0d);
  }

  public CauchyRandomGenerator(double a_location, double a_scale) {
    m_location = a_location;
    m_scale = a_scale;

    rn = new Random();
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

  /**
  * Calculate Cumulative Cauchy distribution function.
  * @return the probability that a stochastic variable x is less than X
  */
  public double nextCauchy() {
     return 0.5 + Math.atan( (rn.nextDouble() - m_location) / m_scale) / Math.PI;
  }

  public double getCauchyStandardDeviation() {
     return m_scale;
  }


}
