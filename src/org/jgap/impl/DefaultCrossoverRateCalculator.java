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

import org.jgap.*;

/**
 * Default implementation of a dynamic CrossoverRateCalculator.
 *
 * @author Chris Knowles
 * @since 2.0
 */
public class DefaultCrossoverRateCalculator
    extends BaseRateCalculator {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.9 $";

  /**
   *
   * @param a_config the configuration to use
   * @throws InvalidConfigurationException
   */
  public DefaultCrossoverRateCalculator(Configuration a_config)
      throws InvalidConfigurationException {
    super(a_config);
  }

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
    int size = getConfiguration().getChromosomeSize();
    if (size < 1) {
      size = 1;
    }
    return size;
  }

  /**
   * Determines whether crossover is to be carried out for a given population.
   *
   * @param a_chrom ignored
   * @param a_geneIndex ignored
   *
   * @return true: the DefaultCrossoverRateCalculator always returns a finite
   * rate
   *
   * @author Chris Knowles
   * @since 2.0
   */
  public boolean toBePermutated(IChromosome a_chrom, int a_geneIndex) {
    return true;
  }
}
