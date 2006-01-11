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
 * Default implementation of a mutation rate calculcator
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class DefaultMutationRateCalculator
    implements IUniversalRateCalculator {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.12 $";

  /**
   * Calculates the mutation rate
   * @return calculated divisor of mutation rate probability (dividend is 1)
   *
   * @author Klaus Meffert
   * @since 1.1 (same functionality since earlier, but not encapsulated)
   */
  public int calculateCurrentRate() {
    int size = Genotype.getConfiguration().getChromosomeSize();
    if (size < 1) {
      size = 1;
    }
    return size;
  }

  /**
   * Determines whether mutation is to be carried out. In this case
   * the rate is the size of the chromosome. There is therefore a
   * probability of 1/totalgenes that a particular gene mutates.
   * @return true if gene should be mutated
   *
   * @author Chris Knowles
   * @since 2.0
   */
  public boolean toBePermutated() {
    RandomGenerator generator
        = Genotype.getConfiguration().getRandomGenerator();
    return (generator.nextInt(calculateCurrentRate()) == 0);
  }
}
