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

/**
 * Gaussian deviation serving as basis for randomly finding a number.
 * @see http://tracer.lcc.uma.es/tws/cEA/GMut.htm
 * @see http://hyperphysics.phy-astr.gsu.edu/hbase/math/gaufcn.html
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class GaussianRandomGenerator
    implements RandomGenerator {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.6 $";

  //delta for distinguishing whether a value is to be interpreted as zero
  private static final double DELTA = 0.0000001;

  private Random rn;

  /**
   * Mean of the gaussian deviation
   */
  private double m_mean;

  /**
   * Standard deviation of the gaussian deviation
   */
  private double m_standardDeviation;

  public GaussianRandomGenerator() {
    this(1.0d);
  }

  /**
   * Constructor speicifying the (obliagtory) standard deviation
   * @param a_standardDeviation the standard deviation to use
   */
  public GaussianRandomGenerator(double a_standardDeviation) {
    super();
    init();
    setGaussianStdDeviation(a_standardDeviation);
  }

  /**
   * Initializations on construction
   */
  private void init() {
    rn = new Random();
  }

  public void setGaussianMean(double a_mean) {
    m_mean = a_mean;
  }

  public void setGaussianStdDeviation(double a_standardDeviation) {
    if (a_standardDeviation <= 0.00000d) {
      throw new IllegalArgumentException(
          "Standard deviation must be greater 0!");
    }
    m_standardDeviation = a_standardDeviation;
  }

  public double getGaussianStdDeviation() {
    return m_standardDeviation;
  }

  /**
   * @return positive integer
   */
  public int nextInt() {
    return Math.abs(Math.min(Integer.MAX_VALUE - 1,
                    (int) Math.round(nextGaussian() * Integer.MAX_VALUE)));
  }

  /**
   * @return positive integer between 0 and ceiling
   */
  public int nextInt(int ceiling) {
    return Math.min(ceiling - 1,
                    (int) Math.round(nextGaussian() * ceiling));
  }

  public long nextLong() {
    return Math.min(Long.MAX_VALUE - 1,
                    Math.round(nextGaussian() * Long.MAX_VALUE));
  }

  public double nextDouble() {
    return nextGaussian();
  }

  public float nextFloat() {
    return Math.min(Float.MAX_VALUE - 1,
                    (float) (nextGaussian() * Float.MAX_VALUE));
  }

  public boolean nextBoolean() {
    return nextGaussian() >= 0.5d;
  }

  /**
   * @return the next randomly distributed gaussian with current standard
   * deviation, will be greater/equal zero
   */
  private double nextGaussian() {
    /**@todo this is not satisfying! We need an algorithm where boundaries
     * are known to then scale to 0..1*/
    double r = (rn.nextGaussian() + 2.8284271247d ) / 2;
    if (r < DELTA) {
      r = 0.0000001d;
    }
    return r;

  }
}
