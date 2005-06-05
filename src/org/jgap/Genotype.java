/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap;

import java.io.*;
import java.util.*;
import java.lang.reflect.*;
import org.jgap.event.*;

/**
 * Genotypes are fixed-length populations of chromosomes. As an instance of
 * a Genotype is evolved, all of its Chromosomes are also evolved. A Genotype
 * may be constructed normally via constructor, or the static
 * randomInitialGenotype() method can be used to generate a Genotype with a
 * randomized Chromosome population.
 *
 * @author Neil Rotstan
 * @author Klaus Meffert
 * @since 1.0
 */
public class Genotype
    implements Serializable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.50 $";

  /**
   * The current active Configuration instance.
   * @since 1.0
   */
  transient protected static Configuration m_activeConfiguration;

  /**
   * The array of Chromosomes that makeup the Genotype's population.
   * @since 2.0
   */
  protected Population m_population;

  /**
   * Constructs a new Genotype instance with the given array of
   * Chromosomes and the given active Configuration instance. Note
   * that the Configuration object must be in a valid state
   * when this method is invoked, or a InvalidconfigurationException
   * will be thrown.
   *
   * @param a_activeConfiguration the current active Configuration object
   * @param a_initialChromosomes the Chromosome population to be managed by
   * this Genotype instance
   * @throws IllegalArgumentException if either the given Configuration object
   * or the array of Chromosomes is null, or if any of the Genes in the array
   * of Chromosomes is null
   * @throws InvalidConfigurationException if the given Configuration object is
   * in an invalid state
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 1.0
   * @deprecated use Genotype(Configuration, Population) instead
   */
  public Genotype(Configuration a_activeConfiguration,
                  Chromosome[] a_initialChromosomes)
      throws InvalidConfigurationException {
    this(a_activeConfiguration, new Population(a_initialChromosomes));
  }

  /**
   * Constructs a new Genotype instance with the given array of
   * Chromosomes and the given active Configuration instance. Note
   * that the Configuration object must be in a valid state
   * when this method is invoked, or a InvalidconfigurationException
   * will be thrown.
   *
   * @param a_activeConfiguration the current active Configuration object
   * @param a_population The Chromosome population to be managed by this
   * Genotype instance
   * @throws InvalidConfigurationException
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 2.0
   */
  public Genotype(Configuration a_activeConfiguration, Population a_population)
      throws InvalidConfigurationException {
    // Sanity checks: Make sure neither the Configuration, the array
    // of Chromosomes, nor any of the Genes inside the array are null.
    // ---------------------------------------------------------------
    if (a_activeConfiguration == null) {
      throw new IllegalArgumentException(
          "The Configuration instance may not be null.");
    }
    if (a_population == null) {
      throw new IllegalArgumentException(
          "The Population may not be null.");
    }
    for (int i = 0; i < a_population.size(); i++) {
      if (a_population.getChromosome(i) == null) {
        throw new IllegalArgumentException(
            "The Chromosome instance at index " + i + " of the array of " +
            "Chromosomes is null. No Chromosomes instance in this array " +
            "may be null.");
      }
    }
    // Lock the settings of the Configuration object so that the cannot
    // be altered.
    // ----------------------------------------------------------------
    a_activeConfiguration.lockSettings();
    m_population = a_population;
    m_activeConfiguration = a_activeConfiguration;
  }

  /**
   * Sets the active Configuration object on this Genotype and its
   * member Chromosomes. This method should be invoked immediately following
   * deserialization of this Genotype. If an active Configuration has already
   * been set on this Genotype, then this method will do nothing.
   *
   * @param a_activeConfiguration the current active Configuration object that
   * is to be referenced internally by this Genotype and its member Chromosome
   * instances
   *
   * @throws InvalidConfigurationException if the Configuration object is null
   * or cannot be locked because it is in an invalid or incomplete state
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 1.0
   */
  public void setActiveConfiguration(Configuration a_activeConfiguration)
      throws InvalidConfigurationException {
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
      }
    }
  }

  /**
   * Retrieves the array of Chromosomes that make up the population of this
   * Genotype instance.
   *
   * @return The population of Chromosomes.
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 1.0
   * @deprecated uses getPopulation() instead
   */
  public synchronized Chromosome[] getChromosomes() {
    Iterator it = getPopulation().iterator();
    Chromosome[] result = new Chromosome[getPopulation().size()];
    int i = 0;
    while (it.hasNext()) {
      result[i++] = (Chromosome) it.next();
    }
    return result;
  }

  /**
   * @return the current population of chromosomes
   *
   * @author Klaus Meffert
   * @since 2.1 (?)
   */
  public Population getPopulation() {
    return m_population;
  }

  /**
   * Retrieves the Chromosome in the population with the highest fitness
   * value.
   *
   * @return the Chromosome with the highest fitness value, or null if there
   * are no chromosomes in this Genotype
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 1.0
   */
  public synchronized Chromosome getFittestChromosome() {
    return getPopulation().determineFittestChromosome();
  }

  /**
   * Evolve the population of Chromosomes within this Genotype. This will
   * execute all of the genetic operators added to the present active
   * configuration and then invoke the natural selector to choose which
   * chromosomes will be included in the next generation population. Note
   * that the population size not always remains constant (dependent on the
   * NaturalSelectors used!).
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 1.0
   */
  public synchronized void evolve() {
    verifyConfigurationAvailable();
    //determine the fittest chromosome in the population
    Chromosome fittest = null;
    if (getConfiguration().isPreserveFittestIndividual()) {
      fittest = getPopulation().determineFittestChromosome();
    }
    // Apply NaturalSelectors before GeneticOperators will be applied.
    // ---------------------------------------------------------------
    applyNaturalSelectors(true);
    // Execute all of the Genetic Operators.
    // -------------------------------------
    List geneticOperators = m_activeConfiguration.getGeneticOperators();
    Iterator operatorIterator = geneticOperators.iterator();
    while (operatorIterator.hasNext()) {
      ( (GeneticOperator) operatorIterator.next()).operate(getPopulation(),
          getPopulation().getChromosomes());
    }
    // Apply NaturalSelectors after GeneticOperators have been applied.
    // ----------------------------------------------------------------
    applyNaturalSelectors(false);
    // If a bulk fitness function has been provided, call it.
    // ------------------------------------------------------
    BulkFitnessFunction bulkFunction =
        m_activeConfiguration.getBulkFitnessFunction();
    if (bulkFunction != null) {
      bulkFunction.evaluate(getPopulation());
    }
    int sizeWanted = m_activeConfiguration.getPopulationSize();
    int popSize;
    // Fill up population randomly if size dropped below 10% of original size.
    // -----------------------------------------------------------------------
    if (m_activeConfiguration.getMinimumPopSizePercent() > 0) {
      int minSize = (int) (sizeWanted *
                           m_activeConfiguration.getMinimumPopSizePercent() /
                           100);
      popSize = getPopulation().size();
      if (popSize < minSize) {
        Chromosome newChrom;
        try {
          while (getPopulation().size() < minSize) {
            newChrom = Chromosome.randomInitialChromosome();
            getPopulation().addChromosome(newChrom);
          }
        }
        catch (InvalidConfigurationException invex) {
          invex.printStackTrace();
        }
      }
    }
    if (getConfiguration().isPreserveFittestIndividual()) {
      if (!getPopulation().contains(fittest)) {
        // Re-add fittest chromosome to current population.
        // ------------------------------------------------
        getPopulation().addChromosome(fittest);
      }
    }
    // Adjust population size to configured size (if wanted).
    // ------------------------------------------------------
    if (m_activeConfiguration.isKeepPopulationSizeConstant()) {
      popSize = getPopulation().size();
      if (popSize > sizeWanted) {
        // See request  1213752.
        // ---------------------
        while (popSize > sizeWanted) {
          getPopulation().removeChromosome(--popSize);
        }
      }
    }
    // Increase number of generation.
    // ------------------------------
    m_activeConfiguration.incrementGenerationNr();
    // Fire an event to indicate we've performed an evolution.
    // -------------------------------------------------------
    m_activeConfiguration.getEventManager().fireGeneticEvent(
        new GeneticEvent(GeneticEvent.GENOTYPE_EVOLVED_EVENT, this));
  }

  /**
   * Evolves this Genotype the specified number of times. This is
   * equivalent to invoking the standard evolve() method the given number
   * of times in a row.
   *
   * @param a_numberOfEvolutions The number of times to evolve this Genotype
   *                             before returning.
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void evolve(int a_numberOfEvolutions) {
    for (int i = 0; i < a_numberOfEvolutions; i++) {
      evolve();
    }
  }

  /**
   * Return a string representation of this Genotype instance,
   * useful for display purposes.
   *
   * @return string representation of this Genotype instance
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    for (int i = 0; i < getPopulation().size(); i++) {
      buffer.append(getPopulation().getChromosome(i).toString());
      buffer.append(" [");
      buffer.append(getPopulation().getChromosome(i).getFitnessValue());
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
   * @param a_activeConfiguration the current active Configuration object
   * @return Aanewly constructed Genotype instance
   *
   * @throws IllegalArgumentException if the given Configuration object is
   * null
   * @throws InvalidConfigurationException if the given Configuration instance
   * is not in a valid state
   *
   * @author Klaus Meffert
   * @since 1.0
   */
  public static Genotype randomInitialGenotype(Configuration
                                               a_activeConfiguration)
      throws InvalidConfigurationException {
    return randomInitialGenotype(a_activeConfiguration, Chromosome.class);
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
   * @param a_activeConfiguration the current active Configuration object
   * @param a_chromosome class of Chromosome offspring (or null to use
   * Chromosome class itself) to use for calling randomInitialChromosome
   * @return a newly constructed Genotype instance
   *
   * @throws IllegalArgumentException if the given Configuration object is
   * null
   * @throws InvalidConfigurationException if the given Configuration
   * instance is not in a valid state
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 2.3
   */
  public static Genotype randomInitialGenotype(Configuration
                                               a_activeConfiguration,
                                               Class a_chromosome)
      throws InvalidConfigurationException {
    if (a_activeConfiguration == null) {
      throw new IllegalArgumentException(
          "The Configuration instance may not be null.");
    }
    Genotype.setConfiguration(a_activeConfiguration);
    a_activeConfiguration.lockSettings();
    // Create an array of chromosomes equal to the desired size in the
    // active Configuration and then populate that array with Chromosome
    // instances constructed according to the setup in the sample
    // Chromosome, but with random gene values (alleles). The Chromosome
    // class' randomInitialChromosome() method will take care of that for
    // us.
    // ------------------------------------------------------------------
    int populationSize = a_activeConfiguration.getPopulationSize();
    Population pop = new Population(populationSize);
    if (a_chromosome == null ||
        a_chromosome.getName().equals(Chromosome.class.getName())) {
      // do default randomized initialization
      for (int i = 0; i < populationSize; i++) {
        pop.addChromosome(Chromosome.randomInitialChromosome());
      }
    }
    else {
      // use user-provided randomized initialization
      for (int i = 0; i < populationSize; i++) {
        try {
          Method m = a_chromosome.getDeclaredMethod("randomInitialChromosome",
              new Class[] {});
          m.invoke(new Object[] {}
                   , new Object[] {});
        }
        catch (NoSuchMethodException nom) {
          throw new InvalidConfigurationException("NoSuchMethodException: " +
                                                  nom.getMessage());
        }
        catch (IllegalAccessException iex) {
          throw new InvalidConfigurationException("IllegalAccessException: " +
                                                  iex.getMessage());
        }
        catch (InvocationTargetException ite) {
          throw new InvalidConfigurationException("InvocationTargetException: " +
                                                  ite.getMessage());
        }
      }
    }
    return new Genotype(a_activeConfiguration, pop);
  }

  /**
   * Compares this Genotype against the specified object. The result is true
   * if the argument is an instance of the Genotype class, has exactly the
   * same number of chromosomes as the given Genotype, and, for each
   * Chromosome in this Genotype, there is an equal chromosome in the
   * given Genotype. The chromosomes do not need to appear in the same order
   * within the populations.
   *
   * @param a_other the object to compare against
   * @return true if the objects are the same, false otherwise
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 1.0
   */
  public boolean equals(Object a_other) {
    try {
      // First, if the other Genotype is null, then they're not equal.
      // -------------------------------------------------------------
      if (a_other == null) {
        return false;
      }
      Genotype otherGenotype = (Genotype) a_other;
      // First, make sure the other Genotype has the same number of
      // chromosomes as this one.
      // ----------------------------------------------------------
      if (getPopulation().size() != otherGenotype.getPopulation().size()) {
        return false;
      }
      // Next, prepare to compare the chromosomes of the other Genotype
      // against the chromosomes of this Genotype. To make this a lot
      // simpler, we first sort the chromosomes in both this Genotype
      // and the one we're comparing against. This won't affect the
      // genetic algorithm (it doesn't care about the order), but makes
      // it much easier to perform the comparison here.
      // --------------------------------------------------------------
      Collections.sort(getPopulation().getChromosomes());
      Collections.sort(otherGenotype.getPopulation().getChromosomes());
      for (int i = 0; i < getPopulation().size(); i++) {
        if (! (getPopulation().getChromosome(i).equals(
            otherGenotype.getPopulation().getChromosome(i)))) {
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
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  private void verifyConfigurationAvailable() {
    if (m_activeConfiguration == null) {
      throw new IllegalStateException(
          "The active Configuration object must be set on this " +
          "Genotype prior to invocation of other operations.");
    }
  }

  /**
   * Applies all NaturalSelectors registered with the Configuration
   * @param a_processBeforeGeneticOperators true apply NaturalSelectors
   * applicable before GeneticOperators, false: apply the ones applicable
   * after GeneticOperators
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  protected void applyNaturalSelectors(boolean a_processBeforeGeneticOperators) {
    // Process all natural selectors applicable before executing the
    // genetic operators (reproduction, crossing over, mutation...).
    // -------------------------------------------------------------
    int selectorSize = m_activeConfiguration.getNaturalSelectorsSize(
        a_processBeforeGeneticOperators);
    if (selectorSize > 0) {
      int m_population_size = m_activeConfiguration.getPopulationSize();
      int m_single_selection_size = m_population_size / selectorSize;
      Population m_new_population;
      m_new_population = new Population(m_population_size);
      NaturalSelector selector;
      // Repopulate the population of chromosomes with those selected
      // by the natural selector. Iterate over all natural selectors.
      // ------------------------------------------------------------
      for (int i = 0; i < selectorSize; i++) {
        selector = m_activeConfiguration.getNaturalSelector(
            a_processBeforeGeneticOperators, i);
        if (i == selectorSize - 1 && i > 0) {
          // Ensure the last NaturalSelector adds the remaining Chromosomes.
          // ---------------------------------------------------------------
          m_single_selection_size = m_population_size - getPopulation().size();
        }
        else {
          m_single_selection_size = m_population_size;
        }
        // Do selection of Chromosomes.
        // ----------------------------
        selector.select(m_single_selection_size, getPopulation(),
                        m_new_population);
        // Clean up the natural selector.
        // ------------------------------
        selector.empty();
      }
      setPopulation(new Population());
      getPopulation().addChromosomes(m_new_population);
    }
  }

  /**
   * @return the configuration to use with the Genetic Algorithm
   *
   * @author Klaus Meffert
   * @since 2.0 (?)
   */
  public static Configuration getConfiguration() {
    return m_activeConfiguration;
  }

  /**
   * Sets the configuration to use with the Genetic Algorithm
   * @param a_configuration the configuration to use
   *
   * @author Klaus Meffert
   * @since 2.0 (?)
   */
  public static void setConfiguration(Configuration a_configuration) {
    m_activeConfiguration = a_configuration;
  }

  /***
   * Hashcode function for the genotype, tries to create a unique hashcode for
   * the chromosomes within the population. The logic for the hashcode is
   *
   * Step  Result
   * ----  ------
   *    1  31*0      + hashcode_0 = y(1)
   *    2  31*y(1)   + hashcode_1 = y(2)
   *    3  31*y(2)   + hashcode_2 = y(3)
   *    n  31*y(n-1) + hashcode_n-1 = y(n)
   *
   * Each hashcode is like a digit and the binary equivalent is computed and
   * reported.
   * @return the computed hashcode
   *
   * @author vamsi
   * @since 2.1
   */
  public int hashCode() {
    int i, size = getPopulation().size();
    Chromosome s;
    int twopower = 1;
    // For empty genotype we want a special value different from other hashcode
    // implementations.
    int localHashCode = -573;
    for (i = 0; i < size; i++, twopower = 2 * twopower) {
      s = getPopulation().getChromosome(i);
      localHashCode = 31 * localHashCode + s.hashCode();
    }
    return localHashCode;
  }

  protected void setPopulation(Population a_pop) {
    m_population = a_pop;
  }
}
