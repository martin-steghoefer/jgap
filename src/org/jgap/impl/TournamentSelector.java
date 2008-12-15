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

import java.io.*;
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
public class TournamentSelector
    extends NaturalSelectorExt {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.24 $";

  private TournamentSelectorConfigurable m_config
      = new TournamentSelectorConfigurable();

  private List m_chromosomes;

  /**
   * Comparator that is only concerned about fitness values
   */
  private FitnessValueComparator m_fitnessValueComparator;

  /**
   * Default constructor.<p>
   * Attention: The configuration used is the one set with the static method
   * Genotype.setConfiguration.
   *
   * @throws InvalidConfigurationException
   *
   * @author Siddhartha Azad
   * @author Klaus Meffert
   */
  public TournamentSelector()
      throws InvalidConfigurationException {
    super(Genotype.getStaticConfiguration());
    init();
  }

  private void init() {
    m_chromosomes = new Vector();
    m_fitnessValueComparator = new FitnessValueComparator();
  }

  /**
   * @param a_config the configuration to use
   * @param a_tournament_size the size of each tournament to play
   * @param a_probability probability for selecting the best individuals
   *
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public TournamentSelector(final Configuration a_config,
                            final int a_tournament_size,
                            final double a_probability)
      throws InvalidConfigurationException {
    super(a_config);
    init();
    if (a_tournament_size < 1) {
      throw new IllegalArgumentException("Tournament size must be at least 1!");
    }
    if (a_probability <= 0.0d || a_probability > 1.0d) {
      throw new IllegalArgumentException("Probability must be greater 0.0 and"
          + " less or equal than 1.0!");
    }
    m_config.m_tournament_size = a_tournament_size;
    m_config.m_probability = a_probability;
  }

  public void setTournamentSize(final int a_tournament_size) {
    if (a_tournament_size < 1) {
      throw new IllegalArgumentException("Tournament size must be at least 1!");
    }
    m_config.m_tournament_size = a_tournament_size;
  }

  public int getTournamentSize() {
    return m_config.m_tournament_size;
  }

  public double getProbability() {
    return m_config.m_probability;
  }

  public void setProbability(final double a_probability) {
    if (a_probability <= 0.0d || a_probability > 1.0d) {
      throw new IllegalArgumentException("Probability must be greater 0.0 and"
          + " less or equal than 1.0!");
    }
    m_config.m_probability = a_probability;
  }

  /**
   * Select a given number of Chromosomes from the pool that will move on
   * to the next generation population. This selection will be guided by the
   * fitness values. The chromosomes with the best fitness value win.
   *
   * @param a_howManyToSelect int
   * @param a_to_pop the population the Chromosomes will be added to
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void selectChromosomes(final int a_howManyToSelect,
                                Population a_to_pop) {
    List tournament = new Vector();
    RandomGenerator rn = getConfiguration().getRandomGenerator();
    int size = m_chromosomes.size();
    if (size == 0) {
      return;
    }
    int k;
    for (int i = 0; i < a_howManyToSelect; i++) {
      // Choose [tournament size] individuals from the population at random.
      // -------------------------------------------------------------------
      tournament.clear();
      for (int j = 0; j < m_config.m_tournament_size; j++) {
        k = rn.nextInt(size);
        tournament.add(m_chromosomes.get(k));
      }
      Collections.sort(tournament, m_fitnessValueComparator);
      double prob = rn.nextDouble();
      double probAccumulated = m_config.m_probability;
      int index = 0;
      // Play the tournament.
      // --------------------
      if (m_config.m_tournament_size > 1) {
        do {
          if (prob <= probAccumulated) {
            break;
          }
          else {
            probAccumulated += probAccumulated * (1 - m_config.m_probability);
            index++;
          }
        } while (index < m_config.m_tournament_size - 1);
      }
      a_to_pop.addChromosome( (IChromosome) tournament.get(index));
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
  }

  /**
   *
   * @param a_chromosomeToAdd the chromosome to add
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  protected void add(final IChromosome a_chromosomeToAdd) {
    m_chromosomes.add(a_chromosomeToAdd);
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
  class TournamentSelectorConfigurable
      implements Serializable {
    /**
     * The probability for selecting the best chromosome in a tournament.
     * For the second-best chromosome it would be p * (1 - p).
     * For the third-best chromosome it would be p * (p * (1 - p)) and so forth.
     */
    public double m_probability;

    /**
     * Size of a tournament to be played, i.e. number of chromosomes taken into
     * account for one selection round
     */
    public int m_tournament_size;
  }
}
