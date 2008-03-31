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
 * Implementation of a NaturalSelector that ensures a certain threshold of the
 * best chromosomes are taken to the next generation.
 *
 * @author Klaus Meffert
 * @since 2.0
 */
public class ThresholdSelector
    extends NaturalSelectorExt {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.19 $";

  /**
   * Stores the chromosomes to be taken into account for selection
   */
  private List m_chromosomes;

  /**
   * Indicated whether the list of added chromosomes needs sorting
   */
  private boolean m_needsSorting;

  /**
   * Comparator that is only concerned about fitness values
   */
  private FitnessValueComparator m_fitnessValueComparator;

  private ThresholdSelectorConfigurable m_config
      = new ThresholdSelectorConfigurable();

  /**
   * Default constructor. Uses threshold of 30 percent.<p>
   * Attention: The configuration used is the one set with the static method
   * Genotype.setConfiguration.
   *
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public ThresholdSelector()
      throws InvalidConfigurationException {
    this(Genotype.getStaticConfiguration(), 0.3d);
  }

  /**
   * @param a_config the configuration to use
   * @param a_bestChromosomes_Percentage indicates the number of best
   * chromosomes from the population to be selected for granted. All other
   * chromosomes will be selected in a random fashion. The value must be in
   * the range from 0.0 to 1.0.
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public ThresholdSelector(final Configuration a_config,
                           final double a_bestChromosomes_Percentage)
      throws InvalidConfigurationException {
    super(a_config);
    if (a_bestChromosomes_Percentage < 0.0000000d
        || a_bestChromosomes_Percentage > 1.0000000d) {
      throw new IllegalArgumentException("Percentage must be between 0.0"
          + " and 1.0 !");
    }
    m_config.m_bestChroms_Percentage = a_bestChromosomes_Percentage;
    m_chromosomes = new Vector();
    m_needsSorting = false;
    m_fitnessValueComparator = new FitnessValueComparator();
  }

  /**
   * Select a given number of Chromosomes from the pool that will move on
   * to the next generation population. This selection will be guided by the
   * fitness values. The chromosomes with the best fitness value win.

   * @param a_howManyToSelect the number of Chromosomes to select
   * @param a_to_pop the population the Chromosomes will be added to
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void selectChromosomes(final int a_howManyToSelect,
                                Population a_to_pop) {
    int canBeSelected;
    if (a_howManyToSelect > m_chromosomes.size()) {
      canBeSelected = m_chromosomes.size();
    }
    else {
      canBeSelected = a_howManyToSelect;
    }
    // Sort the collection of chromosomes previously added for evaluation.
    // Only do this if necessary.
    // -------------------------------------------------------------------
    if (m_needsSorting) {
      Collections.sort(m_chromosomes, m_fitnessValueComparator);
      m_needsSorting = false;
    }
    // Select the best chromosomes for granted
    int bestToBeSelected = (int) Math.round(canBeSelected
        * m_config.m_bestChroms_Percentage);
    for (int i = 0; i < bestToBeSelected; i++) {
      a_to_pop.addChromosome( (IChromosome) m_chromosomes.get(i));
    }
    // Fill up the rest by randomly selecting chromosomes.
    // ---------------------------------------------------
    /**@todo replace this step by adding newly to create chromosomes*/
    int missing = a_howManyToSelect - bestToBeSelected;
    RandomGenerator rn = getConfiguration().getRandomGenerator();
    int index;
    int size = m_chromosomes.size();
    for (int i = 0; i < missing; i++) {
      index = rn.nextInt(size);
      IChromosome chrom = (IChromosome) m_chromosomes.get(index);
      a_to_pop.addChromosome(chrom);
    }
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
    m_needsSorting = false;
  }

  /**
   *
   * @param a_chromosomeToAdd Chromosome
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  protected void add(final IChromosome a_chromosomeToAdd) {
    m_chromosomes.add(a_chromosomeToAdd);
    m_needsSorting = true;
  }

  /**
   * Comparator regarding only the fitness value. Best fitness value will
   * be on first position of resulting sorted list.
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  private class FitnessValueComparator
      implements Comparator {
    public FitnessValueComparator() {

    }
    public int compare(final Object a_first, final Object a_second) {
      IChromosome chrom1 = (IChromosome) a_first;
      IChromosome chrom2 = (IChromosome) a_second;
      if (getConfiguration().getFitnessEvaluator().isFitter(chrom2.
          getFitnessValue(), chrom1.getFitnessValue())) {
        return 1;
      }
      else if (getConfiguration().getFitnessEvaluator().isFitter(
          chrom1.getFitnessValue(), chrom2.getFitnessValue())) {
        return -1;
      }
      else {
        return 0;
      }
    }
  }
  class ThresholdSelectorConfigurable {
    /**
     * This percentage indicates the number of best chromosomes from the
     * population to be selected for granted. All other chromosomes will
     * be selected in a random fashion.
     */
    public double m_bestChroms_Percentage;
  }
}
