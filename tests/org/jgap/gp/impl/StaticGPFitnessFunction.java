/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp.impl;

import org.jgap.*;
import org.jgap.gp.*;

/**
 * GP Fitness function always returning the same value.
 * Only for testing purpose!
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public class StaticGPFitnessFunction
    extends GPFitnessFunction implements Comparable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  /**
   * @since 3.2
   */
  private double m_staticFitnessValue;

  /**
   * @param a_staticFitnessValue double
   *
   * @author Klaus Meffert
   * @since 2.0 (until 1.1: input type int)
   */
  public StaticGPFitnessFunction(double a_staticFitnessValue) {
    m_staticFitnessValue = a_staticFitnessValue;
  }

  /**
   * @param a_subject ignored: the GP program to evaluate
   * @return static fitness value
   *
   * @author Klaus  Meffert
   * @since 3.2
   */
  protected double evaluate(IGPProgram a_subject) {
    double result = m_staticFitnessValue;
    return result;
  }

  /**
   * @return double typed fitness value
   *
   * @author Klaus Meffert
   * @since 3.2
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

  /**
   * @param a_other sic
   * @return as always
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public int compareTo(Object a_other) {
    StaticGPFitnessFunction other = (StaticGPFitnessFunction) a_other;
    if (m_staticFitnessValue > other.m_staticFitnessValue) {
      return 1;
    }
    else if (m_staticFitnessValue < other.m_staticFitnessValue) {
      return -1;
    }
    else {
      return 0;
    }
  }
}
