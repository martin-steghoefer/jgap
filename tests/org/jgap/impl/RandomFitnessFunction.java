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
import java.util.Random;

/**
 * Fitness function returning random values
 * Description: Only for testing purpose
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class RandomFitnessFunction
    extends FitnessFunction {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.4 $";

  private Random rand;

  public RandomFitnessFunction() {
    rand = new Random();
  }

  /**
   * @param chrom Chromosome
   * @return double
   * @since 2.0 (until 1.1: return type int)
   */
  public double evaluate(Chromosome chrom) {
    double result;
    result = rand.nextDouble();
    return result;
  }
}
