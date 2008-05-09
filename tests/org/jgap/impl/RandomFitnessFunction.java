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

import org.jgap.*;

/**
 * Fitness function returning random values.
 * Only for testing purposes!
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class RandomFitnessFunction
    extends FitnessFunction {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.7 $";

  private Random m_rand;

  public RandomFitnessFunction() {
    m_rand = new Random();
  }

  /**
   * @param a_chrom ignored: the Chromosome to evaluate
   * @return randomized fitness value
   * @since 2.0 (until 1.1: return type int)
   */
  public double evaluate(IChromosome a_chrom) {
    double result;
    result = m_rand.nextDouble();
    return result;
  }
}
