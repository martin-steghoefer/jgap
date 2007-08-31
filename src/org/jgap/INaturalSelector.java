/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap;

import java.io.*;

/**
 * Natural selectors are responsible for actually selecting a specified number
 * of Chromosome specimens from a population, using the fitness values as a
 * guide. Usually fitness is treated as a statistic probability of survival,
 * not as the sole determining factor. Therefore, Chromosomes with higher
 * fitness values are more likely to survive than those with lesser fitness
 * values, but it's not guaranteed.
 *
 * @author Neil Rotstan
 * @author Klaus Meffert
 * @since 2.0
 */
public interface INaturalSelector
    extends Serializable {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.13 $";

  /**
   * Select a given number of Chromosomes from the pool that will move on
   * to the next generation population. This selection should be guided by
   * the fitness values, but fitness should be treated as a statistical
   * probability of survival, not as the sole determining factor. In other
   * words, Chromosomes with higher fitness values should be more likely to
   * be selected than those with lower fitness values, but it should not be
   * guaranteed.
   *
   * @param a_howManyToSelect the number of Chromosomes to select
   * @param a_from_population the population the Chromosomes will be
   * selected from
   * @param a_to_population the population the Chromosomes will be added to
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 2.0 (since 1.0 with different return type)
   */
  void select(int a_howManyToSelect, Population a_from_population,
              Population a_to_population);

  /**
   * Empty out the working pool of Chromosomes. This will be invoked after
   * each evolution cycle so that the natural selector can be reused for
   * the next one.
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  void empty();

  /**
   * @return true: The implementation of the NaturalSelector only returns
   * unique Chromosome's (example: BestChromosomesSelector). false: Also
   * doublettes could be returned (example: WeightedRouletteSelector).
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  boolean returnsUniqueChromosomes();
}
