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
 * Fitness function always returning the same value.
 * <p>
 * Only for testing purpose!
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class StaticFitnessFunction
    extends FitnessFunction {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.5 $";

  /**
   * @since 2.0 (until 1.1: type int)
   */
  private double staticFitnessValue;

  /**
   * @param staticFitnessValue double
   *
   * @author Klaus Meffert
   * @since 2.0 (until 1.1: input type int)
   */
  public StaticFitnessFunction(double staticFitnessValue) {
    this.staticFitnessValue = staticFitnessValue;
  }

  /**
   * @param chrom Chromosome
   * @return double
   *
   * @author Klaus  Meffert
   * @since 2.0 (until 1.1: return type int)
   */
  public double evaluate(Chromosome chrom) {
    double result = staticFitnessValue;
    return result;
  }

  /**
   * @return double typed fitness value
   *
   * @author Klaus Meffert
   * @since 2.0 (until 1.1: return type int)
   */
  public double getStaticFitnessValue() {
    return staticFitnessValue;
  }

  /**
   * @param staticFitnessValue double
   *
   * @author Klaus Meffert
   * @since 2.0 (until 1.1: type int)
   */
  public void setStaticFitnessValue(double staticFitnessValue) {
    this.staticFitnessValue = staticFitnessValue;
  }
}
