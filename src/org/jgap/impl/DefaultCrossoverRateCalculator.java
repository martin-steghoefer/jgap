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
 * Default implementation of a dynamic CrossoverRateCalculator
 *
 * @author Chris Knowles
 * @since 2.0
 */
public class DefaultCrossoverRateCalculator
    implements IUniversalRateCalculator {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.6 $";

  /**
   * Calculates the dynamic crossover rate. This is chosen to be the chromosome
   * size. As the chromosome gets larger we assume that it is less likely to
   * reproduce.
   *
   * @return calculated divisor of crossover rate
   *
   * @author Chris Knowles
   * @since 2.0
   */
  public int calculateCurrentRate() {
    int size = Genotype.getConfiguration().getChromosomeSize();
    if (size < 1) {
      size = 1;
    }
    return size;
  }

  /**
   * Determines whether crossover is to be carried out for a given population.
   * @return true the DefaultCrossoverRateCalculator always returns a finite
   * rate
   *
   * @author Chris Knowles
   * @since 2.0
   */
  public boolean toBePermutated(IChromosome a_chrom, int a_geneIndex) {
    return true;
  }
}
