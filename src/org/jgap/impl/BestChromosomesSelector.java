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
    extends NaturalSelector {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.18 $";

  /**
   * Stores the chromosomes to be taken into account for selection
   */
  private Population m_chromosomes;

  /**
   * Allows or disallows doublette chromosomes to be added to the selector
   */
  private boolean m_doublettesAllowed;

  /**
   * Indicated whether the list of added chromosomes needs sorting
   */
  private boolean m_needsSorting;

  /**
   * Comparator that is only concerned about fitness values
   */
  private FitnessValueComparator m_fitnessValueComparator;

  /**
   * The rate of original Chromosomes selected. This is because we otherwise
   * would always return the original input as output
   */
  private double m_originalRate;

  /**
   * Internal: Indicated whether we pass the input unchanged to the output.
   * If so, then we must not empty the list of Chromosomes because we would
   * then empty the output list.
   */
  private boolean m_doNotEmpty;

  /**
   * Constructor
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public BestChromosomesSelector() {
    this(1.0d);
  }

  public BestChromosomesSelector(double a_originalRate) {
    super();
    m_chromosomes = new Population();
    m_needsSorting = false;
    setOriginalRate(a_originalRate);
    m_fitnessValueComparator = new FitnessValueComparator();
  }

  /**
   * Add a Chromosome instance to this selector's working pool of Chromosomes.
   * @param a_chromosomeToAdd The specimen to add to the pool.
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public synchronized void add(Chromosome a_chromosomeToAdd) {
    // If opted-in: Check if chromosome already added
    // This speeds up the process by orders of magnitude but could lower the
    // quality of evolved results because of fewer Chromosome's used!!!
    if (!m_doublettesAllowed &&
        m_chromosomes.getChromosomes().contains(a_chromosomeToAdd)) {
      return;
    }
    // New chromosome, insert it into the sorted collection of chromosomes
    a_chromosomeToAdd.setIsSelectedForNextGeneration(false);
    m_chromosomes.addChromosome(a_chromosomeToAdd);
    // Indicate that the list of chromosomes to add needs sorting
    // ----------------------------------------------------------
    m_needsSorting = true;
  }

  /**
   * Select a given number of Chromosomes from the pool that will move on
   * to the next generation population. This selection will be guided by the
   * fitness values. The chromosomes with the best fitness value win.
   * @param a_howManyToSelect The number of Chromosomes to select.
   *
   * @return An array of the selected Chromosomes.
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public synchronized Population select(int a_howManyToSelect) {
    int canBeSelected;
    if (a_howManyToSelect > m_chromosomes.size()) {
      canBeSelected = m_chromosomes.size();
    }
    else {
      canBeSelected = a_howManyToSelect;
    }

    int neededSize = a_howManyToSelect;
    if (m_originalRate < 1.0d) {
      int oldCanBeSelected = canBeSelected;
      canBeSelected = (int) (canBeSelected * m_originalRate);
      if (canBeSelected < 1) {
        canBeSelected = oldCanBeSelected;
      }
    }

    m_doNotEmpty = false;

    // Sort the collection of chromosomes previously added for evaluation.
    // Only do this if necessary.
    // -------------------------------------------------------------------
    if (m_needsSorting) {
      Collections.sort(m_chromosomes.getChromosomes(), m_fitnessValueComparator);
      m_needsSorting = false;
    }
    Population population = new Population(canBeSelected);
    // To select a chromosome, we just go thru the sorted list.
    // --------------------------------------------------------
    Chromosome selectedChromosome;
    for (int i = 0; i < canBeSelected; i++) {
      selectedChromosome = m_chromosomes.getChromosome(i);
      selectedChromosome.setIsSelectedForNextGeneration(true);
      population.addChromosome(selectedChromosome);
    }

    if (getDoubletteChromosomesAllowed()) {
      int toAdd;
      do {
        toAdd = neededSize - population.size();
        int loopCount;
        if (toAdd > a_howManyToSelect) {
          loopCount = a_howManyToSelect;
        }
        else {
          loopCount = toAdd;
        }
        // Add existing Chromosome's by cloning them to fill up the return result
        // to contain the desired number of Chromosome's
        for (int i = 0; i < loopCount; i++) {
          selectedChromosome = m_chromosomes.getChromosome(i);
          selectedChromosome.setIsSelectedForNextGeneration(true);
          population.addChromosome(selectedChromosome);
        }
      }
      while (toAdd > 0);
    }
    return population;
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
    if (!m_doNotEmpty) {
      m_chromosomes.getChromosomes().clear();
    }
    m_needsSorting = false;
  }

  /**
   * Comparator regarding only the fitness value. Best fitness value will
   * be on first position of resulting sorted list
   *
   * @author Klaus Meffert
   * @since 1.1
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

  /**
   * Determines whether doublette chromosomes may be added to the selector or
   * will be ignored.
   * @param a_doublettesAllowed true: doublette chromosomes allowed to be
   *       added to the selector. FALSE: doublettes will be ignored and not
   *       added
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void setDoubletteChromosomesAllowed(boolean a_doublettesAllowed) {
    m_doublettesAllowed = a_doublettesAllowed;
  }

  /**
   * @return TRUE: doublette chromosomes allowed to be added to the selector
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public boolean getDoubletteChromosomesAllowed() {
    return m_doublettesAllowed;
  }

  /**
   * @return always true as no Chromosome can be returnd multiple times
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public boolean returnsUniqueChromosomes() {
    return true;
  }

  /**
   *
   * @param a_originalRate the rate of how many of the original chromosomes
   * will be selected according to BestChromosomeSelector's strategy. The rest
   * (non-original) of the chromosomes is addest as duplicates
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void setOriginalRate(double a_originalRate) {
    if (a_originalRate < 0.0d || a_originalRate > 1.0d) {
      throw new IllegalArgumentException("Original rate must be greater than"
                                         + " zero and not greater than one!");
    }
    m_originalRate = a_originalRate;
  }

  /**
   *
   * @return see setOriginalRate
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public double getOriginalRate() {
    return m_originalRate;
  }
}
