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
 * Implementation of a NaturalSelector that plays tournaments to determine
 * the chromosomes to be taken to the next generation.
 * <p>
 * The tournament size can be adjusted as well as the probability for selecting
 * an individual.
 *
 * @author Klaus Meffert
 * @since 2.0
 */
public class ThresholdSelector
    extends NaturalSelector {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  /**
   * Stores the chromosomes to be taken into account for selection
   */
  private List m_chromosomes;

  /**
   * Indicated whether the list of added chromosomes needs sorting
   */
  private boolean m_needsSorting;

  /**
   * This percentage indicates the number of best chromosomes from the
   * population to be selected for granted. All other chromosomes will
   * be selected in a random fashion.
   */
  private double m_bestChroms_Percentage;

  /**
   * Standard constructor
   *
   * @param a_bestChromosomes_Percentage indicates the number of best
   * chromosomes from the population to be selected for granted. All other
   * chromosomes will be selected in a random fashion.

   * @author Klaus Meffert
   * @since 2.0
   */
  public ThresholdSelector(double a_bestChromosomes_Percentage) {
    super();
    m_bestChroms_Percentage = a_bestChromosomes_Percentage;
    m_chromosomes = new Vector();
  }

  /**
   * Comparator that is only concerned about fitness values
   */
  private FitnessValueComparator m_fitnessValueComparator;

  /**
   *
   * @param a_howManyToSelect The number of Chromosomes to select.
   * @return The selected Chromosomes.
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public Population select(int a_howManyToSelect) {
    // Sort the collection of chromosomes previously added for evaluation.
    // Only do this if necessary.
    // -------------------------------------------------------------------
    if (m_needsSorting) {
      Collections.sort(m_chromosomes, m_fitnessValueComparator);
      m_needsSorting = false;
    }

    Population pop = new Population(a_howManyToSelect);
    // Select the best chromosomes for granted
    int bestToBeSelected = (int) Math.round(a_howManyToSelect *
                                            m_bestChroms_Percentage);
    for (int i = 0; i < bestToBeSelected; i++) {
      pop.addChromosome((Chromosome)m_chromosomes.get(i));
    }

    // Fill up the rest by randomly selecting chromosomes
    int missing = a_howManyToSelect - bestToBeSelected;
    RandomGenerator rn = Genotype.getConfiguration().getRandomGenerator();
    int index;
    int size = m_chromosomes.size();
    for (int i = 0; i < missing; i++) {
      index = rn.nextInt(size);
      pop.addChromosome((Chromosome)m_chromosomes.get(index));
    }
    return pop;
  }

  /**
   * @return false as we allow to return the same chromosome multiple times
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public boolean returnsUniqueChromosomes() {
    return false;
  }

  public void empty() {
    m_chromosomes.clear();
  }

  /**
   *
   * @param a_chromosomeToAdd Chromosome
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void add(Chromosome a_chromosomeToAdd) {
    m_chromosomes.add(a_chromosomeToAdd);
  }

  /**
   * Comparator regarding only the fitness value. Best fitness value will
   * be on first position of resulting sorted list
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  private class FitnessValueComparator
      implements Comparator {
    public int compare(Object first, Object second) {
      Chromosome chrom1 = (Chromosome) first;
      Chromosome chrom2 = (Chromosome) second;

      if (Genotype.getConfiguration().getFitnessEvaluator().isFitter(chrom2.
          getFitnessValue(), chrom1.getFitnessValue())) {
        return 1;
      }
      else if (Genotype.getConfiguration().getFitnessEvaluator().isFitter(
          chrom1.getFitnessValue(), chrom2.getFitnessValue())) {
        return -1;
      }
      else {
        return 0;
      }
    }
  }
}
