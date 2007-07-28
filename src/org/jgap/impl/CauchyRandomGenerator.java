/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.impl;

import java.util.*;
import java.io.*;

import org.jgap.*;

/**
 * Cauchy probability density function (cumulative distribution function).
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class CauchyRandomGenerator
    implements RandomGenerator {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.15 $";

  private double m_scale;

  private double m_location;

  private Random m_rn;

  /**
   * Defaults to location = 0.0 and scale = 1.0.
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public CauchyRandomGenerator() {
    this(0.0d, 1.0d);
  }

  /**
   * @param a_location cauchy parameter, 0 is standard
   * @param a_scale cauchy parameter, 1 is standard
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public CauchyRandomGenerator(final double a_location, final double a_scale) {
    m_location = a_location;
    m_scale = a_scale;
    m_rn = new Random();
  }

  public int nextInt() {
    return Math.min(Integer.MAX_VALUE - 1,
                    (int) Math.round(nextCauchy() * Integer.MAX_VALUE));
  }

  public int nextInt(final int a_ceiling) {
    return Math.min(a_ceiling - 1,
                    (int) Math.round(nextCauchy() * a_ceiling));
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
   *
   * @return the probability that a stochastic variable x is less than X
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public double nextCauchy() {
    return 0.5
        + Math.atan( (m_rn.nextDouble() - m_location) / m_scale) / Math.PI;
  }

  /**
   * @return the cauchy standard deviation
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public double getCauchyStandardDeviation() {
    return m_scale;
  }

  /**
   * When deserializing, initialize the seed because otherwise we could get
   * duplicate evolution results when doing distributed computing!
   *
   * @param a_inputStream the ObjectInputStream provided for deserialzation
   *
   * @throws IOException
   * @throws ClassNotFoundException
   *
   * @author Klaus Meffert
   * @since 3.01
   */
  private void readObject(ObjectInputStream a_inputStream)
      throws IOException, ClassNotFoundException {
    //always perform the default de-serialization first
    a_inputStream.defaultReadObject();
    m_rn.setSeed(System.currentTimeMillis());
  }
}
