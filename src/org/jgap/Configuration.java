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

import java.util.*;

import org.jgap.event.*;
import org.jgap.impl.*;

/**
 * The Configuration class represents the current configuration of
 * plugins and flags necessary to execute the genetic algorithm (such
 * as fitness function, natural selector, genetic operators, and so on).
 * <p>
 * Note that, while during setup, the settings, flags, and other
 * values may be set multiple times. But once the lockSettings() method
 * is invoked, they cannot be changed. The default behavior of the
 * Genotype constructor is to invoke this method, meaning that
 * once a Configuration object is passed to a Genotype, it cannot
 * be subsequently modified. There is no mechanism for unlocking
 * the settings once they are locked.
 * <p>
 * Not all configuration options are required. See the documentation
 * for each of the respective mutator methods to determine whether
 * it is required to provide a value for that setting, and what the
 * setting will default to if not.
 *
 * @author Neil Rotstan
 * @author Klaus Meffert
 * @since 1.0
 */
public class Configuration {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.18 $";

  /**
   * References the current fitness function that will be used to evaluate
   * chromosomes during the natural selection process. Note that only this
   * or the bulk fitness function may be set--the two are mutually exclusive.
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  private FitnessFunction m_objectiveFunction;

  /**
   * The fitness evaluator. See interface class FitnessEvaluator for details
   * @since 2.0 (since 1.1 in class Genotype)
   */
  private FitnessEvaluator m_fitnessEvaluator;


  /**
   * Minimum size guaranteed for population. If zero or below then no ensurance
   */
  private int m_minPercentageSizePopulation;

  /**
   * References the current bulk fitness function that will be used to
   * evaluate chromosomes (in bulk) during the natural selection
   * process. Note that only this or the normal fitness function
   * may be set--the two are mutually exclusive.
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  private BulkFitnessFunction m_bulkObjectiveFunction;

  /**
   * References a Chromosome that serves as a sample of the Gene setup
   * that is to be used. Each gene in the Chromosome should be represented
   * with the desired Gene type.
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  private Chromosome m_sampleChromosome;

  /**
   * References the random number generator implementation that is to be
   * used for the generation of any random numbers during the various
   * genetic operations and processes.
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  private RandomGenerator m_randomGenerator;

  /**
   * References the EventManager that is to be used for the notification
   * of genetic events and the management of event subscribers.
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  private EventManager m_eventManager;

  /**
   * References the chromosome pool, if any, that is to be used to pool
   * discarded Chromosome instances so that they may be recycled later,
   * thereby saving memory and the time to construct them from scratch.
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  private ChromosomePool m_chromosomePool;

  /**
   * Stores all of the GeneticOperator implementations that are to be used
   * to operate upon the chromosomes of a population prior to natural
   * selection. Operators will be executed in the order that they are
   * added to this list.
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  private List m_geneticOperators = new ArrayList();

  /**
   * The number of genes that will be stored in each chromosome in the
   * population.
   */
  private int m_chromosomeSize;

  /**
   * The number of chromosomes that will be stored in the Genotype.
   */
  private int m_populationSize;

  /**
   * Indicates whether the settings of this Configuration instance have
   * been locked. Prior to locking, the settings may be set and reset
   * as desired. Once this flag is set to true, no settings may be
   * altered.
   */
  private boolean m_settingsLocked;

  /**
   * Ordered chain of NaturalSelector's which will be executed before applying
   * Genetic Operators
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  private ChainOfSelectors m_preSelectors;

  /**
   * Ordered chain of NaturalSelector's which will be executed after applying
   * Genetic Operators
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  private ChainOfSelectors m_postSelectors;

  private int m_sizeNaturalSelectorsPre;

  private int m_sizeNaturalSelectorsPost;

  /**
   * Should the fittest chromosome in the population be preserved to the next
   * generation when evolving (in Genotype.evolve()) ?
   */
  private boolean m_preserveFittestIndividual;

  /**
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 1.0
   */
  public Configuration() {
    m_preSelectors = new ChainOfSelectors();
    m_postSelectors = new ChainOfSelectors();
    m_sizeNaturalSelectorsPre = 0;
    m_sizeNaturalSelectorsPost = 0;
  }

  /**
   * Sets the fitness function to be used for this genetic algorithm.
   * The fitness function is responsible for evaluating a given
   * Chromosome and returning a positive integer that represents its
   * worth as a candidate solution. These values are used as a guide by the
   * natural to determine which Chromosome instances will be allowed to move
   * on to the next round of evolution, and which will instead be eliminated.
   * <p>
   * Note that it is illegal to set both this fitness function and a bulk
   * fitness function. Although one or the other must be set, the two are
   * mutually exclusive.
   *
   * @param a_functionToSet The fitness function to be used.
   *
   * @throws InvalidConfigurationException if the fitness function
   *         is null, a bulk fitness function has already been set,
   *         or if this Configuration object is locked.
   *
   * @author Neil Rotstan
   * @since 1.1
   */
  public synchronized void setFitnessFunction(
      FitnessFunction a_functionToSet)
      throws InvalidConfigurationException {
    verifyChangesAllowed();
    // Sanity check: Make sure that the given fitness function isn't null.
    // -------------------------------------------------------------------
    if (a_functionToSet == null) {
      throw new InvalidConfigurationException(
          "The FitnessFunction instance may not be null.");
    }
    // Make sure the bulk fitness function hasn't already been set.
    // ------------------------------------------------------------
    if (m_bulkObjectiveFunction != null) {
      throw new InvalidConfigurationException(
          "The bulk fitness function and normal fitness function " +
          "may not both be set.");
    }
    m_objectiveFunction = a_functionToSet;
  }

  /**
   * Retrieves the fitness function previously setup in this Configuration
   * object.
   *
   * @return The fitness function.
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public FitnessFunction getFitnessFunction() {
    return m_objectiveFunction;
  }

  /**
   * Sets the bulk fitness function to be used for this genetic algorithm.
   * The bulk fitness function may be used to evaluate and assign fitness
   * values to the entire group of candidate Chromosomes in a single batch.
   * This can be useful in cases where it's difficult to assign fitness
   * values to a Chromosome in isolation from the other candidate
   * Chromosomes.
   * <p>
   * Note that it is illegal to set both a bulk fitness function and a
   * normal fitness function. Although one or the other is required, the
   * two are mutually exclusive.
   *
   * @param a_functionToSet The bulk fitness function to be used.
   *
   * @throws InvalidConfigurationException if the bulk fitness function
   *         is null, the normal fitness function has already been set,
   *         or if this Configuration object is locked.
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public synchronized void setBulkFitnessFunction(
      BulkFitnessFunction a_functionToSet)
      throws InvalidConfigurationException {
    verifyChangesAllowed();
    // Sanity check: Make sure that the given bulk fitness function
    // isn't null.
    // ------------------------------------------------------------
    if (a_functionToSet == null) {
      throw new InvalidConfigurationException(
          "The BulkFitnessFunction instance may not be null.");
    }
    // Make sure a normal fitness function hasn't already been set.
    // ------------------------------------------------------------
    if (m_objectiveFunction != null) {
      throw new InvalidConfigurationException(
          "The bulk fitness function and normal fitness function " +
          "may not both be set.");
    }
    m_bulkObjectiveFunction = a_functionToSet;
  }

  /**
   * Retrieves the bulk fitness function previously setup in this
   * Configuration object.
   *
   * @return The bulk fitness function.
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public BulkFitnessFunction getBulkFitnessFunction() {
    return m_bulkObjectiveFunction;
  }

  /**
   * Sets the sample Chromosome that is to be used as a guide for the
   * construction of other Chromosomes. The Chromosome should be setup
   * with each gene represented by the desired concrete Gene implementation
   * for that gene position (locus). Anytime a new Chromosome is created,
   * it will be constructed with the same Gene setup as that provided in
   * this sample Chromosome.
   *
   * @param a_sampleChromosomeToSet The Chromosome to be used as the sample.
   * @throws InvalidConfigurationException if the given Chromosome is null
   *         or this Configuration object is locked.
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public void setSampleChromosome(Chromosome a_sampleChromosomeToSet)
      throws
      InvalidConfigurationException {
    verifyChangesAllowed();
    // Sanity check: Make sure that the given chromosome isn't null.
    // -----------------------------------------------------------
    if (a_sampleChromosomeToSet == null) {
      throw new InvalidConfigurationException(
          "The sample Chromosome instance may not be null.");
    }
    m_sampleChromosome = a_sampleChromosomeToSet;
    m_chromosomeSize = m_sampleChromosome.size();
  }

  /**
   * Retrieves the sample Chromosome that contains the desired Gene setup
   * for each respective gene position (locus).
   *
   * @return the sample Chromosome instance.
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public Chromosome getSampleChromosome() {
    return m_sampleChromosome;
  }

  /**
   * Retrieves the chromosome size being used by this genetic
   * algorithm. This value is set automatically when the sample Chromosome
   * is provided.
   *
   * @return The chromosome size used in this Configuration.
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public int getChromosomeSize() {
    return m_chromosomeSize;
  }

  /**
   * Sets the natural selector to be used for this genetic algorithm.
   * The natural selector is responsible for actually selecting
   * which Chromosome instances are allowed to move on to the next
   * round of evolution (usually guided by the fitness values
   * provided by the fitness function). This setting is required.
   *
   * @param a_selectorToSet The natural selector to be used.
   *
   * @throws InvalidConfigurationException if the natural selector
   *         is null or this Configuration object is locked.
   *
   * @author Neil Rotstan
   * @since 1.0
   * @deprecated use addNaturalSelector(false) instead
   */
  public synchronized void setNaturalSelector(
      NaturalSelector a_selectorToSet)
      throws InvalidConfigurationException {
    addNaturalSelector(a_selectorToSet, false);
  }

  /**
   * Retrieves the natural selector setup in this Configuration instance.
   *
   * @return The natural selector.
   *
   * @author Neil Rotstan
   * @since 1.0
   * @deprecated use getNaturalSelectors(true) or getNaturalSelectors(false)
   *             to obtain the relevant chain of NaturalSelector's and then
   *             call the chain's get(index) method
   */
  public NaturalSelector getNaturalSelector() {
    if (m_sizeNaturalSelectorsPre < 1) {
      return null;
    }
    return getNaturalSelectors(false).get(0);
  }

  /**
   *
   * @param processBeforeGeneticOperators boolean
   * @param index int
   * @return NaturalSelector
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public NaturalSelector getNaturalSelector(boolean processBeforeGeneticOperators, int index) {
    if (processBeforeGeneticOperators) {
      if (m_sizeNaturalSelectorsPre < index) {
        throw new IllegalArgumentException("Index of NaturalSelector out of bounds");
      }
      else {
        return m_preSelectors.get(index);
      }
    }
    else {
      if (m_sizeNaturalSelectorsPost < index) {
        throw new IllegalArgumentException("Index of NaturalSelector out of bounds");
      }
      else {
        return m_postSelectors.get(index);
      }
    }

  }

  /**
   *
   * @param processBeforeGeneticOperators boolean
   * @return ChainOfSelectors
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public ChainOfSelectors getNaturalSelectors(boolean
                                              processBeforeGeneticOperators) {
    if (processBeforeGeneticOperators) {
      return m_preSelectors;
    }
    else {
      return m_postSelectors;
    }
  }

  /**
   *
   * @param processBeforeGeneticOperators boolean
   * @return int
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public int getNaturalSelectorsSize(boolean processBeforeGeneticOperators) {
    if (processBeforeGeneticOperators) {
      return m_sizeNaturalSelectorsPre;
    }
    else {
      return m_sizeNaturalSelectorsPost;
    }
  }

  /**
   * Sets the random generator to be used for this genetic algorithm.
   * The random generator is responsible for generating random numbers,
   * which are used throughout the process of genetic evolution and
   * selection. This setting is required.
   *
   * @param a_generatorToSet The random generator to be used.
   *
   * @throws InvalidConfigurationException if the random generator
   *         is null or this object is locked.
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public synchronized void setRandomGenerator(
      RandomGenerator a_generatorToSet)
      throws InvalidConfigurationException {
    verifyChangesAllowed();
    // Sanity check: Make sure that the given random generator isn't null.
    // -------------------------------------------------------------------
    if (a_generatorToSet == null) {
      throw new InvalidConfigurationException(
          "The RandomGenerator instance may not be null.");
    }
    m_randomGenerator = a_generatorToSet;
  }

  /**
   * Retrieves the random generator setup in this Configuration instance.
   *
   * @return The random generator.
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public RandomGenerator getRandomGenerator() {
    return m_randomGenerator;
  }

  /**
   * Adds a genetic operator for use in this algorithm. Genetic operators
   * represent evolutionary steps that, when combined, make up the
   * evolutionary process. Examples of genetic operators are reproduction,
   * crossover, and mutation. During the evolution process, all of the
   * genetic operators added via this method are invoked in the order
   * they were added. At least one genetic operator must be provided.
   *
   * @param a_operatorToAdd The genetic operator to add.
   *
   * @throws InvalidConfigurationException if the genetic operator
   *         is null of if this Configuration object is locked.
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public synchronized void addGeneticOperator(GeneticOperator a_operatorToAdd)
      throws InvalidConfigurationException {
    verifyChangesAllowed();
    // Sanity check: Make sure that the given genetic operator isn't null.
    // -------------------------------------------------------------------
    if (a_operatorToAdd == null) {
      throw new InvalidConfigurationException(
          "The GeneticOperator instance may not be null.");
    }
    m_geneticOperators.add(a_operatorToAdd);
  }

  /**
   * Retrieves the genetic operators setup in this Configuration instance.
   * Note that once this Configuration instance is locked, a new,
   * immutable list of operators is used and any lists previously
   * retrieved with this method will no longer reflect the actual
   * list in use.
   *
   * @return The list of genetic operators.
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public List getGeneticOperators() {
    return m_geneticOperators;
  }

  /**
   * Sets the population size to be used for this genetic algorithm.
   * The population size is a fixed value that represents the
   * number of Chromosomes contained within the Genotype (population).
   * This setting is required.
   *
   * @param a_sizeOfPopulation The population size to be used.
   *
   * @throws InvalidConfigurationException if the population size
   *         is not positive or this object is locked.
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public synchronized void setPopulationSize(int a_sizeOfPopulation)
      throws
      InvalidConfigurationException {
    verifyChangesAllowed();
    // Sanity check: Make sure the population size is positive.
    // --------------------------------------------------------
    if (a_sizeOfPopulation < 1) {
      throw new InvalidConfigurationException(
          "The population size must be positive.");
    }
    m_populationSize = a_sizeOfPopulation;
  }

  /**
   * Retrieves the population size setup in this Configuration instance.
   *
   * @return The population size.
   */
  public int getPopulationSize() {
    return m_populationSize;
  }

  /**
   * Sets the EventManager that is to be associated with this configuration.
   * The EventManager is responsible for the management of event subscribers
   * and event notifications.
   *
   * @param a_eventManagerToSet the EventManager instance to use in this
   *                            configuration.
   *
   * @throws InvalidConfigurationException if the event manager is null
   *         or this Configuration object is locked.
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public void setEventManager(EventManager a_eventManagerToSet)
      throws
      InvalidConfigurationException {
    verifyChangesAllowed();
    // Sanity check: Make sure that the given event manager isn't null.
    // ----------------------------------------------------------------
    if (a_eventManagerToSet == null) {
      throw new InvalidConfigurationException(
          "The EventManager instance may not be null.");
    }
    m_eventManager = a_eventManagerToSet;
  }

  /**
   * Retrieves the EventManager associated with this configuration.
   * The EventManager is responsible for the management of event subscribers
   * and event notifications.
   *
   * @return the actively configured EventManager instance.
   */
  public EventManager getEventManager() {
    return m_eventManager;
  }

  /**
   * Sets the ChromosomePool that is to be associated with this
   * configuration. The ChromosomePool is used to pool discarded Chromosome
   * instances so that they may be recycled later, thereby memory and the
   * time to construct them from scratch. The presence of a ChromosomePool
   * is optional. If none exists, then a new Chromosome will be constructed
   * each time one is needed.
   *
   * @param a_chromosomePoolToSet The ChromosomePool instance to use.
   * @throws InvalidConfigurationException if this object is locked.
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public void setChromosomePool(ChromosomePool a_chromosomePoolToSet)
      throws
      InvalidConfigurationException {
    verifyChangesAllowed();
    m_chromosomePool = a_chromosomePoolToSet;
  }

  /**
   * Retrieves the ChromosomePool instance, if any, that is associated with
   * this configuration. The ChromosomePool is used to pool discarded
   * Chromosome instances so that they may be recycled later, thereby
   * saving memory and the time to construct them from scratch. The presence
   * of a ChromosomePool instance is optional. If none exists, then new
   * Chromosomes should be constructed each time one is needed.
   *
   * @return The ChromosomePool instance associated this configuration, or
   *         null if none has been provided.
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public ChromosomePool getChromosomePool() {
    return m_chromosomePool;
  }

  /**
   * Locks all of the settings in this configuration object. Once
   * this method is successfully invoked, none of the settings may
   * be changed. There is no way to unlock this object once it is locked.
   * <p>
   * Prior to returning successfully, this method will first invoke the
   * verifyStateIsValid() method to make sure that any required configuration
   * options have been properly set. If it detects a problem, it will
   * throw an InvalidConfigurationException and leave the object unlocked.
   * <p>
   * It's possible to test whether is object is locked through the
   * isLocked() method.
   * <p>
   * It is ok to lock an object more than once. In that case, this method
   * does nothing and simply returns.
   *
   * @throws InvalidConfigurationException if this Configuration object is
   *         in an invalid state at the time of invocation.
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public synchronized void lockSettings()
      throws InvalidConfigurationException {
    if (!m_settingsLocked) {
      verifyStateIsValid();
      // Make genetic operators list immutable.
      // --------------------------------------
      m_geneticOperators =
          Collections.unmodifiableList(m_geneticOperators);
      m_settingsLocked = true;
    }
  }

  /**
   * Retrieves the lock status of this object.
   *
   * @return true if this object has been locked by a previous successful
   *         call to the lockSettings() method, false otherwise.
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public boolean isLocked() {
    return m_settingsLocked;
  }

  /**
   * Tests the state of this Configuration object to make sure it's valid.
   * This generally consists of verifying that required settings have, in
   * fact, been set. If this object is not in a valid state, then an
   * exception will be thrown detailing the reason the state is not valid.
   *
   * @throws InvalidConfigurationException if the state of this Configuration
   *         is not valid. The error message in the exception will detail
   *         the reason for invalidity.
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public synchronized void verifyStateIsValid()
      throws InvalidConfigurationException {
    // First, make sure all of the required fields have been set to
    // appropriate values.
    // ------------------------------------------------------------
    if (m_objectiveFunction == null && m_bulkObjectiveFunction == null) {
      throw new InvalidConfigurationException(
          "A desired fitness function or bulk fitness function must " +
          "be specified in the active configuration.");
    }
    if (m_sampleChromosome == null) {
      throw new InvalidConfigurationException(
          "A sample instance of the desired Chromosome " +
          "setup must be specified in the active configuration.");
    }
    if (m_preSelectors.size() == 0 && m_postSelectors.size() == 0) {
      throw new InvalidConfigurationException(
          "At least one desired natural selector must be specified in the"
          + " active configuration.");
    }
    if (m_randomGenerator == null) {
      throw new InvalidConfigurationException(
          "A desired random number generator must be specified in the " +
          "active configuration.");
    }
    if (m_eventManager == null) {
      throw new InvalidConfigurationException(
          "A desired event manager must be specified in the active " +
          "configuration.");
    }
    if (m_geneticOperators.isEmpty()) {
      throw new InvalidConfigurationException(
          "At least one genetic operator must be specified in the " +
          "configuration.");
    }
    if (m_chromosomeSize <= 0) {
      throw new InvalidConfigurationException(
          "A chromosome size greater than zero must be specified in " +
          "the active configuration.");
    }
    if (m_populationSize <= 0) {
      throw new InvalidConfigurationException(
          "A genotype size greater than zero must be specified in " +
          "the active configuration.");
    }
    if (m_fitnessEvaluator == null) {
      throw new IllegalArgumentException(
          "The fitness evaluator may not be null.");
    }
    // Next, it's critical that each Gene implementation in the sample
    // Chromosome has a working equals() method, or else the genetic
    // engine will end up failing in mysterious and unpredictable ways.
    // We therefore verify right here that this method is working properly
    // in each of the Gene implementations used in the sample Chromosome.
    // -------------------------------------------------------------------
    Gene[] sampleGenes = m_sampleChromosome.getGenes();
    for (int i = 0; i < sampleGenes.length; i++) {
      Gene sampleCopy = sampleGenes[i].newGene();
      sampleCopy.setAllele(sampleGenes[i].getAllele());
      if (! (sampleCopy.equals(sampleGenes[i]))) {
        throw new InvalidConfigurationException(
            "The sample Gene at gene position (locus) " + i +
            " does not appear to have a working equals() method. " +
            "When tested, the method returned false when comparing " +
            "the sample gene with a gene of the same type and " +
            "possessing the same value (allele).");
      }
    }
  }

  /**
   * Makes sure that this Configuration object isn't locked. If it is, then
   * an exception is thrown with an appropriate message indicating
   * that settings in this object may not be altered. This method
   * should be invoked by any mutator method in this object prior
   * to making any state alterations.
   *
   * @throws InvalidConfigurationException if this Configuration object is
   *         locked.
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  protected void verifyChangesAllowed()
      throws InvalidConfigurationException {
    if (m_settingsLocked) {
      throw new InvalidConfigurationException(
          "This Configuration object is locked. Settings may not be " +
          "altered.");
    }
  }

  /**
   * Adds a NaturalSelector to the ordered chain of registered
   * NaturalSelector's. It's possible to execute the NaturalSelector before
   * or after (registered) genetic operations have been applied.
   * @param a_selector the selector to be added to the chain
   * @param processBeforeGeneticOperators true: execute NaturalSelector before
   *        any genetic operator will be applied, false: .. after..
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void addNaturalSelector(NaturalSelector a_selector,
                                 boolean processBeforeGeneticOperators)
      throws
      InvalidConfigurationException {
    verifyChangesAllowed();
    if (processBeforeGeneticOperators) {
      m_preSelectors.addNaturalSelector(a_selector);
      m_sizeNaturalSelectorsPre++;
    }
    else {
      m_postSelectors.addNaturalSelector(a_selector);
      m_sizeNaturalSelectorsPost++;
    }
  }

  public void setMinimumPopSizePercent(int a_minimumSizeGuaranteedPercent) {
    m_minPercentageSizePopulation = a_minimumSizeGuaranteedPercent;
  }

  public int getMinimumPopSizePercent() {
    return m_minPercentageSizePopulation;
  }

  /**
   * @return the assigned FitnessEvaluator
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public FitnessEvaluator getFitnessEvaluator() {
    return m_fitnessEvaluator;
  }

  /**
   * Set the fitness evaluator (deciding if a given fitness value is better when
   * it's higher or better when it's lower).
   * @param a_fitnessEvaluator the FitnessEvaluator to be used
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void setFitnessEvaluator(FitnessEvaluator a_fitnessEvaluator) {
    if (a_fitnessEvaluator == null) {
      throw new IllegalStateException(
          "The fitness evaluator object must not be null!");
    }
    m_fitnessEvaluator = a_fitnessEvaluator;
  }

  /**
   * @return true: fittest chromosome should always be transferred to next
   * generation.
   *
   * @author Klaus Meffert
   * @since 2.1
   */
  public boolean isPreserveFittestIndividual() {
    return m_preserveFittestIndividual;
  }

  /**
   * Determines whether to save (keep) the fittest individual
   * @param a_preserveFittest true: always transfer fittest chromosome to next gen.
   *
   * @author Klaus Meffert
   * @since 2.1
   */
  public void setPreservFittestIndividual(boolean a_preserveFittest) {
    m_preserveFittestIndividual = a_preserveFittest;
  }
}
