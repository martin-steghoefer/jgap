/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.impl;

import org.jgap.*;

/**
 * Fitness function always returning the same value.<p>
 * Only for testing purpose!
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class StaticFitnessFunction
    extends FitnessFunction {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.9 $";

  /**
   * @since 2.0 (until 1.1: type int)
   */
  private double m_staticFitnessValue;

  /**
   * @param a_staticFitnessValue double
   *
   * @author Klaus Meffert
   * @since 2.0 (until 1.1: input type int)
   */
  public StaticFitnessFunction(double a_staticFitnessValue) {
    m_staticFitnessValue = a_staticFitnessValue;
  }

  /**
   * @param a_chrom ignored: the Chromosome to evaluate
   * @return static fitness value
   *
   * @author Klaus  Meffert
   * @since 2.0 (until 1.1: return type int)
   */
  public double evaluate(IChromosome a_chrom) {
    double result = m_staticFitnessValue;
    return result;
  }

  /**
   * @return double typed fitness value
   *
   * @author Klaus Meffert
   * @since 2.0 (until 1.1: return type int)
   */
  public double getStaticFitnessValue() {
    return m_staticFitnessValue;
  }

  /**
   * @param a_staticFitnessValue the value to return as fitness value when
   * calling evaluate()
   *
   * @author Klaus Meffert
   * @since 2.0 (until 1.1: type int)
   */
  public void setStaticFitnessValue(double a_staticFitnessValue) {
    m_staticFitnessValue = a_staticFitnessValue;
  }

  public int hashCode() {
    int result = new Double(m_staticFitnessValue).hashCode();
    return result;
  }
}
