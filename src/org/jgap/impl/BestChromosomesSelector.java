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
import org.jgap.util.*;

/**
 * Implementation of a NaturalSelector that takes the top n chromosomes into
 * the next generation. n can be specified. Which chromosomes are the best is
 * decided by evaluating their fitness value.
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class BestChromosomesSelector
    extends NaturalSelectorExt implements ICloneable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.54 $";

  /**
   * Stores the chromosomes to be taken into account for selection
   */
  private Population m_chromosomes;

  /**
   * Indicated whether the list of added chromosomes needs sorting
   */
  private boolean m_needsSorting;

  /**
   * Comparator that is concerned about both age and fitness values
   */
  private Comparator m_fitnessValueComparator;

  private BestChromosomesSelectorConfig m_config = new
      BestChromosomesSelectorConfig();

  /**
   * Default constructor.
   * Attention: The configuration used is the one set with the static method
   * Genotype.setConfiguration.
   *
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public BestChromosomesSelector()
      throws InvalidConfigurationException {
    this(Genotype.getStaticConfiguration());
  }

  /**
   * Using original rate of 1.0
   *
   * @param a_config the configuration to use
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public BestChromosomesSelector(final Configuration a_config)
      throws InvalidConfigurationException {
    this(a_config, 1.0d);
  }

  public BestChromosomesSelector(final Configuration a_config,
                                 final double a_originalRate)
      throws InvalidConfigurationException {
    super(a_config);
    m_chromosomes = new Population(a_config);
    m_needsSorting = false;
    setDoubletteChromosomesAllowed(true);
    setOriginalRate(a_originalRate);
    m_fitnessValueComparator = new FitnessAgeValueComparator();
  }

  /**
   * Add a Chromosome instance to this selector's working pool of Chromosomes.
   * @param a_chromosomeToAdd the specimen to add to the pool
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  protected void add(final IChromosome a_chromosomeToAdd) {
    // If opted-in: Check if chromosome already added
    // This speeds up the process by orders of magnitude but could lower the
    // quality of evolved results because of fewer Chromosome's used!!!
    if (!getDoubletteChromosomesAllowed()
        && m_chromosomes.getChromosomes().contains(a_chromosomeToAdd)) {
      return;
    }
    // New chromosome, insert it into the sorted collection of chromosomes
    a_chromosomeToAdd.setIsSelectedForNextGeneration(false);
    if (getDoubletteChromosomesAllowed()) {
      ICloneHandler cloner = getConfiguration().getJGAPFactory().
          getCloneHandlerFor(a_chromosomeToAdd, null);
      if (cloner != null) {
        try {
          IChromosome clone = (IChromosome) cloner.perform(
              a_chromosomeToAdd, null, null);
          clone.setAge(a_chromosomeToAdd.getAge() + 1);
          m_chromosomes.addChromosome(clone);
        } catch (Exception ex) {
          ex.printStackTrace();
          m_chromosomes.addChromosome(a_chromosomeToAdd);
        }
      }
      else {
        m_chromosomes.addChromosome(a_chromosomeToAdd);
      }
    }
    else {
      m_chromosomes.addChromosome(a_chromosomeToAdd);
    }
    // Indicate that the list of chromosomes to add needs sorting.
    // -----------------------------------------------------------
    m_needsSorting = true;
  }

  /**
   * Selects a given number of chromosomes from the pool that will move on
   * to the next generation population. This selection will be guided by the
   * fitness values. The chromosomes with the best fitness value win.
   *
   * @param a_to_pop the population the chromosomes will be added to
   * @param a_howManyToSelect the number of chromosomes to select
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void selectChromosomes(final int a_howManyToSelect,
                                Population a_to_pop) {
    int canBeSelected;
    int chromsSize = m_chromosomes.size();
    if (a_howManyToSelect > chromsSize) {
      canBeSelected = chromsSize;
    }
    else {
      canBeSelected = a_howManyToSelect;
    }
    int neededSize = a_howManyToSelect;
    double origRate;
    origRate = m_config.m_originalRate;
    if (origRate < 1.0d) {
      canBeSelected = (int) Math.round( (double) canBeSelected *
                                       origRate);
      if (canBeSelected < 1) {
        canBeSelected = 1;
      }
    }
    // Sort the collection of chromosomes previously added for evaluation.
    // Only do this if necessary.
    // -------------------------------------------------------------------
    if (m_needsSorting) {
      Collections.sort(m_chromosomes.getChromosomes(),
                       m_fitnessValueComparator);
      m_needsSorting = false;
    }
    // To select a chromosome, we just go thru the sorted list.
    // --------------------------------------------------------
    IChromosome selectedChromosome;
    for (int i = 0; i < canBeSelected; i++) {
      selectedChromosome = m_chromosomes.getChromosome(i);
      selectedChromosome.setIsSelectedForNextGeneration(true);
      a_to_pop.addChromosome(selectedChromosome);
    }
    if (getDoubletteChromosomesAllowed()) {
      int toAdd;
      toAdd = neededSize - a_to_pop.size();
      // Add existing Chromosome's to fill up the return
      // result to contain the desired number of Chromosome's.
      // -----------------------------------------------------
      for (int i = 0; i < toAdd; i++) {
        selectedChromosome = m_chromosomes.getChromosome(i % chromsSize);
        ICloneHandler cloner = getConfiguration().getJGAPFactory().
            getCloneHandlerFor(selectedChromosome, null);
        IChromosome cloned = null;
        if (cloner != null) {
          try {
            int age = selectedChromosome.getAge() + 1;
            cloned = (IChromosome) cloner.perform(
                selectedChromosome, null, null);
            cloned.setAge(age);
            cloned.setIsSelectedForNextGeneration(true);
            if(m_monitorActive) {
              cloned.setUniqueIDTemplate(selectedChromosome.getUniqueID(), 1);
            }
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        }
        if(cloned != null) {
          selectedChromosome = cloned;
        }
        a_to_pop.addChromosome(selectedChromosome);
      }
    }
  }

  /**
   * Empties out the working pool of Chromosomes.
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void empty() {
    // clear the list of chromosomes
    // -----------------------------
    m_chromosomes.getChromosomes().clear();
    m_needsSorting = false;
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
   * Setting this parameter controls how many chromosomes of the original
   * population will be considered for selection to the next population. If
   * the value is 1 then the whole original population is considered, if it is
   * 0.5 only half of the chromosomes are considered. If doublettes are allowed,
   * then a number of chromosomes missing (number of to be selected minus number
   * selected) will be added.
   * @param a_originalRate the rate of how many of the original chromosomes
   * will be selected according to BestChromosomeSelector's strategy. The rest
   * (non-original) of the chromosomes is added as duplicates
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void setOriginalRate(final double a_originalRate) {
    if (a_originalRate < 0.0d || a_originalRate > 1.0d) {
      throw new IllegalArgumentException("Original rate must be greater than"
          + " zero and not greater than one!");
    }
    m_config.m_originalRate = a_originalRate;
  }

  /**
   *
   * @return see setOriginalRate
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public double getOriginalRate() {
    return m_config.m_originalRate;
  }

  class BestChromosomesSelectorConfig
      implements java.io.Serializable {
    /**
     * The rate of original Chromosomes selected. This is because we otherwise
     * would always return the original input as output
     */
    public double m_originalRate;
  }

  public boolean equals(Object a_o) {
    if (a_o == null) {
      return false;
    }
    BestChromosomesSelector other = (BestChromosomesSelector) a_o;
    if (getDoubletteChromosomesAllowed() !=
        other.getDoubletteChromosomesAllowed()) {
      return false;
    }
    if (!m_fitnessValueComparator.getClass().getName().equals(
        other.m_fitnessValueComparator.getClass().getName())) {
      return false;
    }
    if (Math.abs(m_config.m_originalRate - other.m_config.m_originalRate) >
        0.001d) {
      return false;
    }
    if (!m_chromosomes.equals(other.m_chromosomes)) {
      return false;
    }
    return true;
  }

  public Object clone() {
    try {
      BestChromosomesSelector sel = new BestChromosomesSelector(
          getConfiguration(), m_config.m_originalRate);
      sel.m_needsSorting = m_needsSorting;
//      sel.m_chromosomes = (Population) m_chromosomes.clone();
      sel.setDoubletteChromosomesAllowed(getDoubletteChromosomesAllowed());
      return sel;
    } catch (Throwable t) {
      throw new CloneException(t);
    }
  }
}
