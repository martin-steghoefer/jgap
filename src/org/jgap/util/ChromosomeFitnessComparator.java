/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.util;

import java.util.Comparator;
import org.jgap.*;

/**
 * Simple comparator to allow the sorting of Chromosome lists.
 * Usage example:
 *   Collections.sort(
 *     population.getPopulation().getChromosomes(),
 *     new ChromosomeFitnessComparator() );
 *
 * @author Charles Kevin Hill
 * @since 2.4
 */
public class ChromosomeFitnessComparator
    implements Comparator {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.4 $";

  /* (non-Javadoc)
   * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
   */
  public int compare(final Object a_chromosome1, final Object a_chromosome2) {
    if (a_chromosome1 == null) {
      return -1;
    }
    if (a_chromosome2 == null) {
      return 1;
    }
    IChromosome chromosomeOne = (IChromosome) a_chromosome1;
    IChromosome chromosomeTwo = (IChromosome) a_chromosome2;
    return (int) (chromosomeTwo.getFitnessValue()
                  - chromosomeOne.getFitnessValue());
  }
}
