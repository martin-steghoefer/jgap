/*
 * This file is part of JGAP.
 *
 * JGAP is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * JGAP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser Public License
 * along with JGAP; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package org.jgap.impl;

import java.util.*;

import org.jgap.*;

/**
 * Implementation of a NaturalSelector that takes the top n chromosomes into
 * the next generation. n can be specified. Which chromosomes are the best is
 * decided by evaluating their fitness value
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class BestChromosomesSelector
    implements NaturalSelector {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.7 $";

  /**
   * Stores the chromosomes to be taken into account for selection
   */
  private List chromosomes;

  /**
   * Indicated whether the list of added chromosomes needs sorting
   */
  private boolean needsSorting;

  /**
   * Comparator that is only concerned about fitness values
   */
  private FitnessValueComparator fitnessValueComparator;

  public BestChromosomesSelector() {
    chromosomes = new Vector();
    needsSorting = false;
    fitnessValueComparator = new FitnessValueComparator();
  }

  /**
   * Add a Chromosome instance to this selector's working pool of Chromosomes.
   * @param a_activeConfigurator: The current active Configuration to be used
   *                              during the add process.
   * @param a_chromosomeToAdd: The specimen to add to the pool.
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public synchronized void add(Configuration a_activeConfigurator,
                               Chromosome a_chromosomeToAdd) {
    // Check if chromosome already added
    if (chromosomes.contains(a_chromosomeToAdd)) {
      return;
    }
    // New chromosome, insert it into the sorted collection of chromosomes
    a_chromosomeToAdd.setIsSelectedForNextGeneration(false);
    chromosomes.add(a_chromosomeToAdd);
    // Indicate that the list of chromosomes to add needs sorting
    // ----------------------------------------------------------
    needsSorting = true;
  }

  /**
   * Select a given number of Chromosomes from the pool that will move on
   * to the next generation population. This selection will be guided by the
   * fitness values. The chromosomes with the best fitness value win.
   * @param a_activeConfiguration: The current active Configuration that is
   *                               to be used during the selection process.
   * @param a_howManyToSelect: The number of Chromosomes to select.
   *
   * @return An array of the selected Chromosomes.
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public synchronized Chromosome[] select(Configuration a_activeConfiguration,
                                          int a_howManyToSelect) {
    if (a_howManyToSelect > chromosomes.size()) {
      a_howManyToSelect = chromosomes.size();
    }
    // Sort the collection of chromosomes previously added for evaluation.
    // Only do this if necessary.
    // -------------------------------------------------------------------
    if (needsSorting) {
      Collections.sort(chromosomes, fitnessValueComparator);
      needsSorting = false;
    }
    Chromosome[] selections = new Chromosome[a_howManyToSelect];
    // To select a chromosome, we just go thru the sorted list.
    // --------------------------------------------------------
    Chromosome selectedChromosome;
    for (int i = 0; i < a_howManyToSelect; i++) {
      selectedChromosome = (Chromosome) chromosomes.get(i);
      selectedChromosome.setIsSelectedForNextGeneration(true);
      selections[i] = selectedChromosome;
    }
    return selections;
  }

  /**
   * Empty out the working pool of Chromosomes.
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public synchronized void empty() {
    // clear the list of chromosomes
    // -----------------------------
    chromosomes.clear();
    needsSorting = false;
  }

  /**
   * Comparator regarding only the fitness value. Best fitness value will
   * be on first position of resulted sorted list
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  private class FitnessValueComparator
      implements Comparator {
    public int compare(Object first, Object second) {
      Chromosome chrom1 = (Chromosome) first;
      Chromosome chrom2 = (Chromosome) second;
      if (chrom1.getFitnessValue() < chrom2.getFitnessValue()) {
        return 1;
      }
      else if (chrom1.getFitnessValue() > chrom2.getFitnessValue()) {
        return -1;
      }
      else {
        return 0;
      }
    }
  }
}
