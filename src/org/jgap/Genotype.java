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

package org.jgap;

import java.io.*;
import java.util.*;
import org.jgap.event.*;

/**
 * Genotypes are fixed-length populations of chromosomes. As an instance of
 * a Genotype is evolved, all of its Chromosomes are also evolved. A Genotype
 * may be constructed normally, whereby an array of Chromosomes must be
 * provided, or the static randomInitialGenotype() method can be used to
 * generate a Genotype with a randomized Chromosome population.
 *
 * @author Neil Rotstan, Klaus Meffert
 * @since 1.0
 */

public class Genotype
    implements Serializable {

  /**
   * The current active Configuration instance.
   */
  transient protected Configuration m_activeConfiguration;

  /**
   * The array of Chromosomes that makeup the Genotype's population.
   */
  protected Chromosome[] m_chromosomes;

  /**
   * The working pool of Chromosomes, which is where Chromosomes that are
   * to be candidates for the next natural selection process are deposited.
   * This list is passed to each of the genetic operators as they are
   * invoked during each phase of evolution so that they can add the
   * Chromosomes they operated upon to it, and then it is eventually passed
   * to the NaturalSelector so that it can choose which Chromosomes will
   * go on to the next generation and which will be discarded. It is wiped
   * clean after each cycle of evolution.
   */
  transient protected List m_workingPool;

  /**
   * The fitness evaluator. See interface class FitnessEvaluator for details
   * @since 1.1
   */
  private FitnessEvaluator m_fitnessEvaluator;

  /**
   * Constructs a new Genotype instance with the given array of
   * Chromosomes and the given active Configuration instance. Note
   * that the Configuration object must be in a valid state
   * when this method is invoked, or a InvalidconfigurationException
   * will be thrown.
   *
   * @param a_activeConfiguration: The current active Configuration object.
   * @param a_initialChromosomes: The Chromosome population to be
   *                              managed by this Genotype instance.
   * @throws IllegalArgumentException if either the given Configuration object
   *         or the array of Chromosomes is null, or if any of the Genes
   *         in the array of Chromosomes is null.
   * @throws InvalidConfigurationException if the given Configuration object
   *         is in an invalid state.
   */
  public Genotype(Configuration a_activeConfiguration,
                  Chromosome[] a_initialChromosomes) throws
      InvalidConfigurationException {
    this(a_activeConfiguration, a_initialChromosomes,
         new DefaultFitnessEvaluator());
  }

  /**
   * Same as constructor without parameter of type FitnessEvaluator.
   * Additionally a specific fitnessEvaluator can be specified here. See
   * interface class FitnessEvaluator for details.
   * @param a_activeConfiguration The current active Configuration object.
   * @param a_initialChromosomes The Chromosome population to be
   *                             managed by this Genotype instance.
   * @param a_fitnessEvaluator a specific fitness value evaluator
   * @throws InvalidConfigurationException
   * @since 1.1
   */
  public Genotype(Configuration a_activeConfiguration,
                  Chromosome[] a_initialChromosomes,
                  FitnessEvaluator a_fitnessEvaluator) throws
      InvalidConfigurationException {
    // Sanity checks: Make sure neither the Configuration, the array

    // of Chromosomes, nor any of the Genes inside the array are null.

    // ---------------------------------------------------------------
    if (a_activeConfiguration == null) {
      throw new IllegalArgumentException(
          "The Configuration instance may not be null.");
    }
    if (a_initialChromosomes == null) {
      throw new IllegalArgumentException(
          "The array of Chromosomes may not be null.");
    }
    if (a_fitnessEvaluator == null) {
      throw new IllegalArgumentException(
          "The fitness evaluator may not be null.");
    }
    for (int i = 0; i < a_initialChromosomes.length; i++) {
      if (a_initialChromosomes[i] == null) {
        throw new IllegalArgumentException(
            "The Gene instance at index " + i + " of the array of " +
            "Chromosomes is null. No Gene instance in this array " +
            "may be null.");
      }
    }
    // Lock the settings of the Configuration object so that the cannot

    // be altered.

    // ----------------------------------------------------------------
    a_activeConfiguration.lockSettings();
    m_chromosomes = a_initialChromosomes;
    m_activeConfiguration = a_activeConfiguration;
    m_fitnessEvaluator = a_fitnessEvaluator;
    m_workingPool = new ArrayList();

  }

  /**
   * Sets the active Configuration object on this Genotype and its
   * member Chromosomes. This method should be invoked immediately following
   * deserialization of this Genotype. If an active Configuration has already
   * been set on this Genotype, then this method will do nothing.
   *
   * @param a_activeConfiguration The current active Configuration object
   *                              that is to be referenced internally by
   *                              this Genotype and its member Chromosome
   *                              instances.
   *
   * @throws InvalidConfigurationException if the Configuration object is
   *         null or cannot be locked because it is in an invalid or
   *         incomplete state.
   */
  public void setActiveConfiguration(Configuration a_activeConfiguration) throws
      InvalidConfigurationException {
    // Only assign the given Configuration object if we don't already

    // have one.

    // --------------------------------------------------------------
    if (m_activeConfiguration == null) {
      if (a_activeConfiguration == null) {
        throw new InvalidConfigurationException(
            "The given Configuration object may not be null.");
      }
      else {
        // Make sure the Configuration object is locked and cannot be
        // changed.

        // ----------------------------------------------------------
        a_activeConfiguration.lockSettings();
        m_activeConfiguration = a_activeConfiguration;
        // Since this method is invoked following deserialization of
        // this Genotype, the constructor hasn't been invoked. So make
        // sure any other transient fields are initialized properly.
        // -----------------------------------------------------------
        m_workingPool = new ArrayList();
        // Now set this Configuration on each of the member
        // Chromosome instances.
        // ------------------------------------------------
        for (int i = 0; i < m_chromosomes.length; i++) {
          m_chromosomes[i].setActiveConfiguration(
              m_activeConfiguration);
        }
      }
    }

  }

  /**
   * Retrieves the array of Chromosomes that make up the population of this
   * Genotype instance.
   *
   * @return The population of Chromosomes.
   */
  public synchronized Chromosome[] getChromosomes() {
    return m_chromosomes;

  }

  /**
   * Retrieves the Chromosome in the population with the highest fitness
   * value.
   *
   * @return The Chromosome with the highest fitness value, or null if
   *         there are no chromosomes in this Genotype.
   */
  public synchronized Chromosome getFittestChromosome() {
    if (m_chromosomes.length == 0) {
      return null;
    }
    // Set the best fitness value to that of the first chromosome.

    // Then loop over the rest of the chromosomes and see if any has

    // a better fitness value.

    // The decision whether a fitness value if better than another is

    // delegated to a FitnessEvaluator

    // --------------------------------------------------------------
    Chromosome fittestChromosome = m_chromosomes[0];
    int fittestValue = fittestChromosome.getFitnessValue();
    for (int i = 1; i < m_chromosomes.length; i++) {
      if (m_fitnessEvaluator.isFitter(m_chromosomes[i].getFitnessValue(),
                                      fittestValue)) {
        fittestChromosome = m_chromosomes[i];
        fittestValue = fittestChromosome.getFitnessValue();
      }
    }
    return fittestChromosome;

  }

  /**
   * Evolve the population of Chromosomes within this Genotype. This will
   * execute all of the genetic operators added to the present active
   * Configuration and then invoke the natural selector to choose which
   * chromosomes will be included in the next generation population. Note
   * that the population size always remains constant.
   */
  public synchronized void evolve() {
    verifyConfigurationAvailable();
    // Process all natural selectors applicable before executing the
    // Genetic Operators.
    // -------------------------------------------------------------
    // Add the chromosomes pool to the natural selector.
    // ----------------------------------------------------------------
    Iterator iterator1 = Arrays.asList(m_chromosomes).iterator();
    while (iterator1.hasNext()) {
      Chromosome currentChromosome = (Chromosome) iterator1.next();
      m_activeConfiguration.getNaturalSelector().add(
          m_activeConfiguration,
          currentChromosome);
    }
    // Repopulate the population of chromosomes with those selected
    // by the natural selector.
    // ------------------------------------------------------------
//TODO: Interate over all natural selectors!
    if (m_activeConfiguration.getNaturalSelectors(true).size() > 0) {
      m_chromosomes = m_activeConfiguration.getNaturalSelectors(true).get(0).
          select(m_activeConfiguration,
                 m_activeConfiguration.getPopulationSize());
      // Fire an event to indicate we've performed an evolution.
      // -------------------------------------------------------
      m_activeConfiguration.getEventManager().fireGeneticEvent(
          new GeneticEvent(GeneticEvent.GENOTYPE_EVOLVED_EVENT, this));
      // Clean up the natural selector.
      // ------------------------------
      m_activeConfiguration.getNaturalSelector().empty();
    }
    // Execute all of the Genetic Operators.
    // -------------------------------------
    List geneticOperators = m_activeConfiguration.getGeneticOperators();
    Iterator operatorIterator = geneticOperators.iterator();
    while (operatorIterator.hasNext()) {
      ( (GeneticOperator) operatorIterator.next()).operate(
          m_activeConfiguration, m_chromosomes, m_workingPool);
    }
    // If a bulk fitness function has been provided, then convert the
    // working pool to an array and pass it to the bulk fitness
    // function so that it can evaluate and assign fitness values to
    // each of the Chromosomes.
    // --------------------------------------------------------------
    BulkFitnessFunction bulkFunction =
        m_activeConfiguration.getBulkFitnessFunction();
    if (bulkFunction != null) {
      Chromosome[] candidateChromosomes = (Chromosome[])
          m_workingPool.toArray(new Chromosome[m_workingPool.size()]);
      bulkFunction.evaluate(candidateChromosomes);
    }
    // Add the chromosomes in the working pool to the natural selector.
    // ----------------------------------------------------------------
    Iterator iterator = m_workingPool.iterator();
    NaturalSelector selector;
    while (iterator.hasNext()) {
      Chromosome currentChromosome = (Chromosome) iterator.next();
      selector = m_activeConfiguration.getNaturalSelector();
      selector.add(m_activeConfiguration,
                   currentChromosome);
    }
    // Repopulate the population of chromosomes with those selected
    // by the natural selector.
    // ------------------------------------------------------------
    m_chromosomes = m_activeConfiguration.getNaturalSelector().select(
        m_activeConfiguration,
        m_chromosomes.length);
    // Fire an event to indicate we've performed an evolution.
    // -------------------------------------------------------
    m_activeConfiguration.getEventManager().fireGeneticEvent(
        new GeneticEvent(GeneticEvent.GENOTYPE_EVOLVED_EVENT, this));
    // Iterate over the Chromosomes in the working pool. Clean up any that
    // haven't been selected to go on to the next generation.
    // -------------------------------------------------------------------
    Iterator workingPoolIterator = m_workingPool.iterator();
    Chromosome currentChromosome;
    while (workingPoolIterator.hasNext()) {
      currentChromosome = (Chromosome) workingPoolIterator.next();
      if (!currentChromosome.isSelectedForNextGeneration()) {
        currentChromosome.cleanup();
      }
    }
    // Clear out the working pool in preparation for the next evolution
    // cycle.
    // ----------------------------------------------------------------
    m_workingPool.clear();
  }

  /**
   * Evolves this Genotype the specified number of times. This is
   * equivalent to invoking the standard evolve() method the given number
   * of times in a row.
   *
   * @param a_numberOfEvolutions The number of times to evolve this Genotype
   *                             before returning.
   */
  public void evolve(int a_numberOfEvolutions) {
    for (int i = 0; i < a_numberOfEvolutions; i++) {
      evolve();
    }
  }

  /**
   * Return a string representation of this Genotype instance,
   * useful for dispaly purposes.
   *
   * @return A string representation of this Genotype instance.
   */

  public String toString() {
    StringBuffer buffer = new StringBuffer();
    for (int i = 0; i < m_chromosomes.length; i++) {
      buffer.append(m_chromosomes[i].toString());
      buffer.append(" [");
      buffer.append(m_chromosomes[i].getFitnessValue());
      buffer.append(']');
      buffer.append('\n');
    }
    return buffer.toString();

  }

  /**
   * Convenience method that returns a newly constructed Genotype
   * instance configured according to the given Configuration instance.
   * The population of Chromosomes will created according to the setup of
   * the sample Chromosome in the Configuration object, but the gene values
   * (alleles) will be set to random legal values.
   * <p>
   * Note that the given Configuration instance must be in a valid state
   * at the time this method is invoked, or an InvalidConfigurationException
   * will be thrown.
   *
   * @return A newly constructed Genotype instance.
   *
   * @throws IllegalArgumentException if the given Configuration object is
   *         null.
   * @throws InvalidConfigurationException if the given Configuration
   *         instance not in a valid state.
   */
  public static Genotype randomInitialGenotype(
      Configuration a_activeConfiguration) throws InvalidConfigurationException {
    if (a_activeConfiguration == null) {
      throw new IllegalArgumentException(
          "The Configuration instance may not be null.");
    }
    a_activeConfiguration.lockSettings();
    // Create an array of chromosomes equal to the desired size in the

    // active Configuration and then populate that array with Chromosome

    // instances constructed according to the setup in the sample

    // Chromosome, but with random gene values (alleles). The Chromosome

    // class' randomInitialChromosome() method will take care of that for

    // us.

    // ------------------------------------------------------------------
    int populationSize = a_activeConfiguration.getPopulationSize();
    Chromosome[] chromosomes = new Chromosome[populationSize];
    for (int i = 0; i < populationSize; i++) {
      chromosomes[i] =
          Chromosome.randomInitialChromosome(a_activeConfiguration);
    }
    return new Genotype(a_activeConfiguration, chromosomes);

  }

  /**
   * Compares this Genotype against the specified object. The result is true
   * if the argument is an instance of the Genotype class, has exactly the
   * same number of chromosomes as the given Genotype, and, for each
   * Chromosome in this Genotype, there is an equal chromosome in the
   * given Genotype. The chromosomes do not need to appear in the same order
   * within the populations.
   *
   * @param other The object to compare against.
   * @return true if the objects are the same, false otherwise.
   */
  public boolean equals(Object other) {
    try {
      // First, if the other Genotype is null, then they're not equal.

      // -------------------------------------------------------------
      if (other == null) {
        return false;
      }
      Genotype otherGenotype = (Genotype) other;
      // First, make sure the other Genotype has the same number of
      // chromosomes as this one.

      // ----------------------------------------------------------
      if (m_chromosomes.length != otherGenotype.m_chromosomes.length) {
        return false;
      }
      // Next, prepare to compare the chromosomes of the other Genotype
      // against the chromosomes of this Genotype. To make this a lot
      // simpler, we first sort the chromosomes in both this Genotype
      // and the one we're comparing against. This won't affect the
      // genetic algorithm (it doesn't care about the order), but makes
      // it much easier to perform the comparison here.
      // --------------------------------------------------------------
      Arrays.sort(m_chromosomes);
      Arrays.sort(otherGenotype.m_chromosomes);
      for (int i = 0; i < m_chromosomes.length; i++) {
        if (! (m_chromosomes[i].equals(
            otherGenotype.m_chromosomes[i]))) {
          return false;
        }
      }
      return true;
    }
    catch (ClassCastException e) {
      return false;
    }

  }

  /**
   * Verifies that a Configuration object has been properly set on this
   * Genotype instance. If not, then an IllegalStateException is thrown.
   * In general, this method should be invoked by any operation on this
   * Genotype that makes use of the Configuration instance.
   */
  private void verifyConfigurationAvailable() {
    if (m_activeConfiguration == null) {
      throw new IllegalStateException(
          "The active Configuration object must be set on this " +
          "Genotype prior to invocation of other operations.");
    }

  }

  public FitnessEvaluator getFitnessEvaluator() {
    return m_fitnessEvaluator;
  }

}
