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
 * Implementation of a NaturalSelector that is suited for being processed after
 * genetic operators have been executed. It takes all chromosomes with no
 * fitness value computed to the next generation. Then it takes the top n
 * chromosomes from the remaining input into the next generation.
 * Which chromosomes are the best is decided by evaluating their fitness value.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public class StandardPostSelector
    extends NaturalSelector implements ICloneable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.4 $";

  /**
   * Stores the chromosomes to be taken into account for selection
   */
  private Population m_chromosomes;

  /**
   * Indicated whether the list of added chromosomes needs sorting
   */
  private boolean m_needsSorting;

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
   * @author Klaus Meffert
   * @since 3.2
   */
  public StandardPostSelector()
      throws InvalidConfigurationException {
    this(Genotype.getStaticConfiguration());
  }

  /**
   * Constructor.
   *
   * @param a_config the configuration to use
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public StandardPostSelector(final Configuration a_config)
      throws InvalidConfigurationException {
    super(a_config);
    m_chromosomes = new Population(a_config);
    m_needsSorting = false;
    m_fitnessValueComparator = new FitnessValueComparator();
  }

  /**
   * Add a Chromosome instance to this selector's working pool of Chromosomes.
   *
   * @param a_chromosomeToAdd the specimen to add to the pool
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  protected void add(final IChromosome a_chromosomeToAdd) {
    // New chromosome, insert it into the sorted collection of chromosomes.
    // --------------------------------------------------------------------
    a_chromosomeToAdd.setIsSelectedForNextGeneration(false);
    m_chromosomes.addChromosome(a_chromosomeToAdd);
    // Indicate that the list of chromosomes to add needs sorting.
    // -----------------------------------------------------------
    m_needsSorting = true;
  }

  /**
   * Selects a given number of Chromosomes from the pool that will move on
   * to the next generation population. This selection will be guided by the
   * fitness values. The chromosomes with the best fitness value win.
   *
   * @param a_from_pop the population the Chromosomes will be selected from
   * @param a_to_pop the population the Chromosomes will be added to
   * @param a_howManyToSelect the number of Chromosomes to select
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void select(final int a_howManyToSelect,
                     final Population a_from_pop,
                     final Population a_to_pop) {
    if (a_from_pop != null) {
      int popSize = a_from_pop.size();
      if (popSize < 1) {
        throw new IllegalStateException("Population size must be greater 0");
      }
      for (int i = 0; i < popSize; i++) {
        add(a_from_pop.getChromosome(i));
      }
    }
    int canBeSelected;
    int chromsSize = m_chromosomes.size();
    if (chromsSize < 1) {
      throw new IllegalStateException(
          "Number of chromosomes must be greater 0");
    }
    if (a_howManyToSelect > chromsSize) {
      canBeSelected = chromsSize;
    }
    else {
      canBeSelected = a_howManyToSelect;
    }
    int neededSize = a_howManyToSelect;
    // First select all chromosomes with no fitness value computed.
    // ------------------------------------------------------------
    Iterator it = m_chromosomes.iterator();
    while (it.hasNext()) {
      IChromosome c = (IChromosome)it.next();
      if (Math.abs(c.getFitnessValueDirectly() -
                   FitnessFunction.NO_FITNESS_VALUE)
          < FitnessFunction.DELTA) {
        a_to_pop.addChromosome(c);
        it.remove();
        canBeSelected--;
        if (canBeSelected < 1) {
          break;
        }
      }
    }

    // Sort the collection of chromosomes previously added for evaluation.
    // Only do this if necessary.
    // -------------------------------------------------------------------
    if (m_needsSorting && canBeSelected > 0) {
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
    int toAdd;
    toAdd = neededSize - a_to_pop.size();
    // Add existing chromosomes to fill up the return
    // result to contain the desired number of chromosomes.
    // ----------------------------------------------------
    for (int i = 0; i < toAdd; i++) {
      selectedChromosome = m_chromosomes.getChromosome(i % chromsSize);
      selectedChromosome.setIsSelectedForNextGeneration(true);
      a_to_pop.addChromosome(selectedChromosome);
    }
  }

  /**
   * Empties out the working pool of chromosomes.
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void empty() {
    // Clear the list of chromosomes.
    // ------------------------------
    m_chromosomes.getChromosomes().clear();
    m_needsSorting = false;
  }

  /**
   * @return always true as no chromosome can be returnd multiple times
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public boolean returnsUniqueChromosomes() {
    return true;
  }

  public boolean equals(Object a_o) {
    if (a_o == null) {
      return false;
    }
    StandardPostSelector other = (StandardPostSelector) a_o;
    if (!m_fitnessValueComparator.getClass().getName().equals(
        other.m_fitnessValueComparator.getClass().getName())) {
      return false;
    }
    if (!m_chromosomes.equals(other.m_chromosomes)) {
      return false;
    }
    return true;
  }

  public Object clone() {
    try {
      StandardPostSelector sel = new StandardPostSelector(getConfiguration());
      sel.m_needsSorting = m_needsSorting;
      return sel;
    } catch (Throwable t) {
      throw new CloneException(t);
    }
  }
}
