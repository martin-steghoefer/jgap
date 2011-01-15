/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap;

import java.io.*;
import java.util.*;

import org.apache.commons.lang.builder.*;
import org.jgap.audit.*;
import org.jgap.data.config.*;
import org.jgap.event.*;
import org.jgap.impl.*;
import org.jgap.util.*;

/**
 * The Configuration class represents the current configuration of
 * plugins and parameters necessary to execute the genetic algorithm (such
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
public class Configuration
    implements Configurable, Serializable, ICloneable, Comparable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.102 $";

  /**
   * Constant for class name of JGAP Factory to use. Use as:
   * System.setProperty(PROPERTY_JGAPFACTORY_CLASS, "myJGAPFactory");
   * If none such property set, class JGAPFactory will be used.
   */
  public static final String PROPERTY_JGAPFACTORY_CLASS = "JGAPFACTORYCLASS";

  public static final String PROPERTY_FITFUNC_INST = "JGAPFITFUNCINST";

  public static final String PROPERTY_BFITFNC_INST = "JGAPBFITFNCINST";

  public static final String PROPERTY_FITEVAL_INST = "JGAPFITEVALINST";

  public static final String PROPERTY_SAMPLE_CHROM_INST = "JGAPSAMPLECHRMINST";

  public static final String PROPERTY_EVENT_MGR_INST = "JGAPEVNTMGRINST";

  /**
   * Constants for toString()
   */
  public static final String S_CONFIGURATION = "Configuration";

  public static final String S_CONFIGURATION_NAME = "Configuration name";

  public static final String S_POPULATION_SIZE = "Population size";

  public static final String S_MINPOPSIZE = "Minimum pop. size [%]";

  public static final String S_CHROMOSOME_SIZE = "Chromosome size";

  public static final String S_SAMPLE_CHROM = "Sample Chromosome";

  public static final String S_SIZE = "Size";

  public static final String S_TOSTRING = "toString";

  public static final String S_RANDOM_GENERATOR = "Random generator";

  public static final String S_EVENT_MANAGER = "Event manager";

  public static final String S_NONE = "none";

  public static final String S_CONFIGURATION_HANDLER = "Configuration handler";

  public static final String S_FITNESS_FUNCTION = "Fitness function";

  public static final String S_FITNESS_EVALUATOR = "Fitness evaluator";

//  public static final String S_POPCONSTANT_SELECTOR = "Constant Population Selector";

  public static final String S_GENETIC_OPERATORS = "Genetic operators";

  public static final String S_NATURAL_SELECTORS = "Natural Selectors";

  public static final String S_PRE = "pre";

  public static final String S_POST = "post";

  /**
   * Contains a bag of custom properties. Can be empty.
   * A custom property is such that is not included in the standard JGAP
   * configuration.
   *
   * @since 3.3.1
   */
//  private Map m_propertyBag;

  private ConfigurationConfigurable m_config = new ConfigurationConfigurable();

  /**
   * References the current fitness function that will be used to evaluate
   * chromosomes during the natural selection process. Note that only this
   * or the bulk fitness function may be set - the two are mutually exclusive.
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  private FitnessFunction m_objectiveFunction;

  /**
   * The fitness evaluator. See interface class FitnessEvaluator for details.
   *
   * @since 2.0 (since 1.1 in class Genotype)
   */
  private FitnessEvaluator m_fitnessEvaluator;

  /**
   * Performs the evolution.
   */
  private IBreeder m_breeder;

  /**
   * Minimum size guaranteed for population. If zero or below then no ensurance.
   *
   * @author Klaus Meffert
   */
  private int m_minPercentageSizePopulation;

  /**
   * References the current bulk fitness function that will be used to
   * evaluate chromosomes (in bulk) during the natural selection
   * process. Note that only this or the normal fitness function
   * may be set - the two are mutually exclusive.
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  private BulkFitnessFunction m_bulkObjectiveFunction;

//  /**
//   * If population size should be kept constant then this selector determines
//   * which of the chromosomes to select into the next generation.
//   *
//   * @author Klaus Meffert
//   * @since 3.2.2
//   */
//  private INaturalSelector m_popConstantSelector;

  /**
   * References a Chromosome that serves as a sample of the Gene setup
   * that is to be used. Each gene in the Chromosome should be represented
   * with the desired Gene type.
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  private IChromosome m_sampleChromosome;

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
   * References the event manager that is to be used for the notification
   * of genetic events and the management of event subscribers.
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  private IEventManager m_eventManager;

  /**
   * References the chromosome pool, if any, that is to be used to pool
   * discarded Chromosome instances so that they may be recycled later,
   * thereby saving memory and the time to construct them from scratch.
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  private transient IChromosomePool m_chromosomePool;

  /**
   * Stores all of the GeneticOperator implementations that are to be used
   * to operate upon the chromosomes of a population prior to natural
   * selection. Operators will be executed in the order that they are
   * added to this list.
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  private List m_geneticOperators;

  /**
   * The number of genes that will be stored in each chromosome in the
   * population.
   */
  private int m_chromosomeSize;

  /**
   * Indicates whether the settings of this Configuration instance have
   * been locked. Prior to locking, the settings may be set and reset
   * as desired. Once this flag is set to true, no settings may be
   * altered.
   */
  private boolean m_settingsLocked;

  /**
   * Ordered chain of NaturalSelector's which will be executed before applying
   * Genetic Operators.
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  private ChainOfSelectors m_preSelectors;

  /**
   * Ordered chain of NaturalSelector's which will be executed after applying
   * Genetic Operators.
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  private ChainOfSelectors m_postSelectors;

  /**
   * Should the fittest chromosome in the population be preserved to the next
   * generation when evolving (in Genotype.evolve()) ?
   */
  private boolean m_preserveFittestIndividual;

  /**
   * How many chromosomes should be selected from previous generation?
   * The missing chromosomes will be filled up with randomly created new
   * ones.
   * 1 = all
   */
  private double m_selectFromPrevGen;

  /**
   * Indicates how many times the evolve()-method in class Genotype has been
   * called. Represents the number of the current population.
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  private int m_generationNr;

  /**
   * The Configuration handler for this Configurable.
   *
   * @author Siddhartha Azad
   * @since 2.3
   */
  private transient RootConfigurationHandler m_conHandler;

  /**
   * Informative name for output.
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  private String m_name;

  /**
   * True: population size will be kept constant at specified size in
   * configuration. False: population size will grow dependently on used
   * NaturalSelector's and GeneticOperator's.
   * Default is TRUE
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  private boolean m_keepPopulationSizeConstant;

  /**
   * Holds the central configurable factory for creating default objects.
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  private IJGAPFactory m_factory;

  /**
   * See Chromosome class, field m_alwaysCalculate, for description
   *
   * @author Klaus Meffert
   * @since 3.2.2
   */
  private boolean m_alwaysCalculateFitness;

  private transient String threadKey;

  /**
   * True: Use unique keys, especially in Chromosomes, to allow tracking and
   * monitoring of evolution progress.
   *
   * @author Klaus Meffert
   * @since 3.5
   */
  private boolean m_uniqueKeysActive;

  /**
   * Unique ID for a configuration to distinguish it from other configurations
   * instantiated within the same thread.
   *
   * @author Klaus Meffert
   * @since 3.01
   */
  private String m_id;

  /**
   *
   * @author Klaus Meffert
   * @since 3.5
   */
  private IEvolutionMonitor m_monitor;

  public Configuration() {
    this("", null);
  }

  /**
   * Initialize with default values.
   *
   * @param a_id unique id for the configuration within the current thread
   * @param a_name informative name of the configuration, may be null
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 1.0
   */
  public Configuration(String a_id, String a_name) {
//    m_propertyBag = new Hashtable();
    m_id = a_id;
    setName(a_name);
    makeThreadKey();
    m_preSelectors = new ChainOfSelectors(this);
    m_postSelectors = new ChainOfSelectors(this);
    m_selectFromPrevGen = 1.0d;
    // Use synchronized list for distributed computing.
    // ------------------------------------------------
    m_geneticOperators = new Vector();
    m_conHandler = new RootConfigurationHandler();
    m_conHandler.setConfigurable(this);
    m_keepPopulationSizeConstant = true;
    m_alwaysCalculateFitness = false;
    // Create factory for being able to configure the used default objects,
    // like random generators or fitness evaluators.
    // --------------------------------------------------------------------
    String clazz = System.getProperty(PROPERTY_JGAPFACTORY_CLASS);
    if (clazz != null && clazz.length() > 0) {
      try {
        m_factory = (IJGAPFactory) Class.forName(clazz).newInstance();
      } catch (Throwable ex) {
        throw new RuntimeException("Class " + clazz
                                   + " could not be instantiated"
                                   + " as type IJGAPFactory", ex);
      }
    }
    else {
      m_factory = new JGAPFactory(false);
    }
  }

  /**
   * Constructs a configuration with an informative name but without a unique
   * ID. This practically prevents more than one configurations to be
   * instantiated within the same thread.
   *
   * @param a_name informative name of the configuration, may be null
   *
   * @author Klaus Meffert
   */
  public Configuration(final String a_name) {
    this();
    setName(a_name);
  }

  /**
   * Reads in the configuration from the given file.
   *
   * @param a_configFileName the config file from which to load the
   * configuration
   * @param a_ignore just there to create distinct signatures :-(
   *
   * @throws ConfigException
   * @throws InvalidConfigurationException
   *
   * @author Siddhartha Azad
   * @since 2.3
   */
  public Configuration(final String a_configFileName, boolean a_ignore)
      throws ConfigException, InvalidConfigurationException {
    this();
    ConfigFileReader.instance().setFileName(a_configFileName);
    // Set the configuration statically for constructing classes by the
    // default constructor.
    // ----------------------------------------------------------------
    Genotype.setStaticConfiguration(this);
    // Read in the config, thus creating instances of configurable classes
    // by invoking their default constructor.
    // -------------------------------------------------------------------
    getConfigurationHandler().readConfig();
  }

  /**
   * SHOULD NOT BE NECESSARY TO CALL UNDER NORMAL CIRCUMSTANCES. May be useful
   * for unit tests.<p>
   * Reset the configuration so that re-setting parameters such as fitness
   * function is possible (without calling this method, an overwriting of a
   * previously set fitness function results in a RuntimeException).
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public static void reset() {
    reset("");
  }

  /**
   * Reset the configuration so that re-setting parameters such as fitness
   * function is possible (without calling this method, an overwriting of a
   * previously set fitness function results in a RuntimeException).
   *
   * @param a_id a hopefully unique id of the configuration
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public static void reset(final String a_id) {
    String threadKey = getThreadKey(Thread.currentThread(), a_id);
    System.setProperty(threadKey + Configuration.PROPERTY_FITFUNC_INST, "");
    System.setProperty(threadKey + Configuration.PROPERTY_BFITFNC_INST, "");
    System.setProperty(threadKey + Configuration.PROPERTY_FITEVAL_INST, "");
    System.setProperty(threadKey + Configuration.PROPERTY_SAMPLE_CHROM_INST, "");
    System.setProperty(threadKey + Configuration.PROPERTY_EVENT_MGR_INST, "");
  }

  /**
   * See Configuration.reset().
   * @param a_propName the property to reset
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public static void resetProperty(final String a_propName) {
    resetProperty(a_propName, "");
  }

  public static void resetProperty(final String a_propName, final String a_id) {
    String threadKey = getThreadKey(Thread.currentThread(), a_id);
    System.setProperty(threadKey + a_propName, "");
  }

  /**
   * @param a_name informative name of the configuration
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public void setName(final String a_name) {
    m_name = a_name;
  }

  /**
   * @return informative name of the configuration
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public String getName() {
    return m_name;
  }

  /**
   * Sets the fitness function to be used for this genetic algorithm.
   * The fitness function is responsible for evaluating a given Chromosome and
   * returning a positive integer that represents its worth as a candidate
   * solution. These values are used as a guide by the natural to determine
   * which Chromosome instances will be allowed to move on to the next round of
   * evolution, and which will instead be eliminated.
   *
   * Note that it is illegal to set both this fitness function and a bulk
   * fitness function. Although one or the other must be set, the two are
   * mutually exclusive.
   *
   * @param a_functionToSet fitness function to be used
   *
   * @throws InvalidConfigurationException if the fitness function is null, a
   * bulk fitness function has already been set, or if this Configuration
   * object is locked.
   *
   * @author Neil Rotstan
   * @since 1.1
   */
  public synchronized void setFitnessFunction(final FitnessFunction
      a_functionToSet)
      throws InvalidConfigurationException {
    verifyChangesAllowed();
    // Sanity check: Make sure that the given fitness function isn't null.
    // -------------------------------------------------------------------
    if (a_functionToSet == null) {
      throw new InvalidConfigurationException(
          "The FitnessFunction instance must not be null.");
    }
    // Make sure the bulk fitness function hasn't already been set.
    // ------------------------------------------------------------
    if (m_bulkObjectiveFunction != null) {
      throw new InvalidConfigurationException(
          "The bulk fitness function and normal fitness function " +
          "may not both be set.");
    }
    // Ensure that no other fitness function has been set in a different
    // configuration object within the same thread!
    // -----------------------------------------------------------------
    checkProperty(PROPERTY_FITFUNC_INST, a_functionToSet, m_objectiveFunction,
                  "Fitness function has already been set differently.");
    m_objectiveFunction = a_functionToSet;
  }

  /**
   * Verifies that a property is not set. If not set, set it, otherwise throw
   * a RuntimeException with a_errmsg as text.
   * @param a_propname the property to check (the current thread will be
   * considered as a part of the property's name, too)
   * @param a_obj the object that should be set in charge of the property
   * @param a_oldObj the old object that is set until now. Not used yet
   * @param a_errmsg the error message to throw in case the property is already
   * set for the current thread
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  protected void checkProperty(final String a_propname, final Object a_obj,
                               final Object a_oldObj, final String a_errmsg) {
    String instanceHash = System.getProperty(threadKey + a_propname, null);
    String key = makeKey(a_obj);
    if (instanceHash == null || instanceHash.length() < 1) {
      System.setProperty(threadKey + a_propname, key);
    }
    else if (!instanceHash.equals(key)) {
      throw new RuntimeException(a_errmsg + "\nIf you want to set or construct"
                                 +
                                 " a configuration multiple times, please call"
                                 +
                                 " static method Configuration.reset() before"
                                 + " each setting!");
    }
  }

  /**
   * @param a_obj the object to make a key for, must not be null
   * @return key produced for the object (hashCode() is used, so it should be
   * implemented properly!)
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  protected String makeKey(final Object a_obj) {
    String key = String.valueOf(a_obj.hashCode())
        + a_obj.getClass().getName();
    return key;
  }

  /**
   * Retrieves the fitness function previously setup in this Configuration
   * object.
   *
   * @return the fitness function
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public synchronized FitnessFunction getFitnessFunction() {
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
   * @param a_functionToSet bulk fitness function to be used
   *
   * @throws InvalidConfigurationException if the bulk fitness function is
   * null, the normal fitness function has already been set, or if this
   * Configuration object is locked
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 1.0
   */
  public synchronized void setBulkFitnessFunction(
      final BulkFitnessFunction a_functionToSet)
      throws InvalidConfigurationException {
    verifyChangesAllowed();
    // Sanity check: Make sure that the given bulk fitness function
    // isn't null.
    // ------------------------------------------------------------
    if (a_functionToSet == null) {
      throw new InvalidConfigurationException(
          "The BulkFitnessFunction instance must not be null.");
    }
    // Make sure a normal fitness function hasn't already been set.
    // ------------------------------------------------------------
    if (m_objectiveFunction != null) {
      throw new InvalidConfigurationException(
          "The bulk fitness function and normal fitness function " +
          "must not both be set.");
    }
    // Ensure that no other bulk fitness function has been set in a
    // different configuration object within the same thread!
    // ------------------------------------------------------------
    checkProperty(PROPERTY_BFITFNC_INST, a_functionToSet,m_bulkObjectiveFunction,
                  "Bulk fitness function has already been set differently.");
    m_bulkObjectiveFunction = a_functionToSet;
  }

  /**
   * Retrieves the bulk fitness function previously setup in this
   * Configuration object.
   *
   * @return the bulk fitness function
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public synchronized BulkFitnessFunction getBulkFitnessFunction() {
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
   * @param a_sampleChromosomeToSet Chromosome to be used as the sample
   * @throws InvalidConfigurationException if the given Chromosome is null or
   * this Configuration object is locked
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public void setSampleChromosome(final IChromosome a_sampleChromosomeToSet)
      throws InvalidConfigurationException {
    verifyChangesAllowed();
    // Sanity check: Make sure that the given chromosome isn't null.

    // -----------------------------------------------------------
    if (a_sampleChromosomeToSet == null) {
      throw new InvalidConfigurationException(
          "The sample chromosome instance must not be null.");
    }
    if (a_sampleChromosomeToSet.getConfiguration() == null) {
      throw new InvalidConfigurationException(
          "The sample chromosome's configuration must not be null.");
    }
    // Ensure that no other sample chromosome has been set in a
    // different configuration object within the same thread!
    // --------------------------------------------------------
    checkProperty(PROPERTY_SAMPLE_CHROM_INST, a_sampleChromosomeToSet,
                  m_sampleChromosome,                  "Sample chromosome has already been set differently.");
    m_sampleChromosome = a_sampleChromosomeToSet;
    m_chromosomeSize = m_sampleChromosome.size();
  }

  /**
   * Retrieves the sample Chromosome that contains the desired Gene setup
   * for each respective gene position (locus).
   *
   * @return sample Chromosome instance
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public IChromosome getSampleChromosome() {
    return m_sampleChromosome;
  }

  /**
   * Retrieves the chromosome size being used by this genetic
   * algorithm. This value is set automatically when the sample chromosome
   * is provided.
   *
   * @return chromosome size used in this configuration
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
   * @param a_selectorToSet the natural selector to be used
   *
   * @throws InvalidConfigurationException if the natural selector is null or
   * this Configuration object is locked
   *
   * @author Neil Rotstan
   * @since 1.0
   * @deprecated use addNaturalSelector(false) instead
   */
  public synchronized void setNaturalSelector(final NaturalSelector
      a_selectorToSet)
      throws InvalidConfigurationException {
    addNaturalSelector(a_selectorToSet, false);
  }

  /**
   * Retrieves the natural selector setup in this Configuration instance.
   *
   * @return the natural selector
   *
   * @author Neil Rotstan
   * @since 1.0
   * @deprecated use getNaturalSelectors(true) or getNaturalSelectors(false)
   * to obtain the relevant chain of NaturalSelector's and then call the
   * chain's get(index) method
   */
  public synchronized NaturalSelector getNaturalSelector() {
    if (getNaturalSelectors(false).size() < 1) {
      return null;
    }
    return getNaturalSelectors(false).get(0);
  }

  /**
   * @param a_processBeforeGeneticOperators true: retrieve selector that is
   * registered to be executed before genetic operators, false: get the one
   * that is registered to be executed after genetic operators
   * @param a_index index of the selector to get
   * @return NaturalSelector
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public synchronized NaturalSelector getNaturalSelector(final boolean
      a_processBeforeGeneticOperators, final int a_index) {
    if (a_processBeforeGeneticOperators) {
        return m_preSelectors.get(a_index);
    }
    else {
        return m_postSelectors.get(a_index);
    }
  }

  /**
   * Only use for read-only access! Especially don't call clear() for the
   * returned ChainOfSelectors object!
   *
   * @param a_processBeforeGeneticOperators true: retrieve selector that is
   * registered to be executed before genetic operators, false: get the one
   * that is registered to be executed after genetic operators
   * @return ChainOfSelectors
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public ChainOfSelectors getNaturalSelectors(final boolean
      a_processBeforeGeneticOperators) {
    if (a_processBeforeGeneticOperators) {
      return m_preSelectors;
    }
    else {
      return m_postSelectors;
    }
  }

  /**
   * @param a_processBeforeGeneticOperators true: retrieve selector that is
   * registered to be executed before genetic operators, false: get the one
   * that is registered to be executed after genetic operators
   * @return size of selector list (distinct by a_processBeforeGeneticOperators)
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public int getNaturalSelectorsSize(final boolean
                                     a_processBeforeGeneticOperators) {
    if (a_processBeforeGeneticOperators) {
      return m_preSelectors.size();
    }
    else {
      return m_postSelectors.size();
    }
  }

  /**
   * Removes all natural selectors (either pre or post ones).
   *
   * @param a_processBeforeGeneticOperators true: remove all selectors
   * processed before genetic operators, false: remove the ones processed
   * afterwards
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public void removeNaturalSelectors(final boolean
                                     a_processBeforeGeneticOperators) {
    if (a_processBeforeGeneticOperators) {
      getNaturalSelectors(true).clear();
    }
    else {
      getNaturalSelectors(false).clear();
    }
  }

  /**
   * Sets the random generator to be used for this genetic algorithm.
   * The random generator is responsible for generating random numbers,
   * which are used throughout the process of genetic evolution and
   * selection. This setting is required.
   *
   * @param a_generatorToSet random generator to be used
   *
   * @throws InvalidConfigurationException if the random generator is null or
   * this object is locked
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public synchronized void setRandomGenerator(final RandomGenerator
      a_generatorToSet)
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
   * @return the random generator
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public synchronized RandomGenerator getRandomGenerator() {
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
   * @param a_operatorToAdd the genetic operator to add.
   *
   * @throws InvalidConfigurationException if the genetic operator is null or
   * if this Configuration object is locked
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public synchronized void addGeneticOperator(final GeneticOperator
      a_operatorToAdd)
      throws InvalidConfigurationException {
    verifyChangesAllowed();
    // Sanity check: Make sure that the given genetic operator isn't null.
    // -------------------------------------------------------------------
    if (a_operatorToAdd == null) {
      throw new InvalidConfigurationException(
          "The GeneticOperator instance must not be null.");
    }
    m_geneticOperators.add(a_operatorToAdd);
  }

  /**
   * Retrieves the genetic operators setup in this Configuration instance.
   * Note that once this Configuration instance is locked, a new, immutable list
   * of operators is used and any lists previously retrieved with this method
   * will no longer reflect the actual list in use.
   *
   * @return the list of genetic operators
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
   * @param a_sizeOfPopulation population size to be used
   *
   * @throws InvalidConfigurationException if the population size is not
   * positive or this object is locked
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public synchronized void setPopulationSize(final int a_sizeOfPopulation)
      throws InvalidConfigurationException {
    verifyChangesAllowed();
    // Sanity check: Make sure the population size is positive.
    // --------------------------------------------------------
    if (a_sizeOfPopulation < 1) {
      throw new InvalidConfigurationException(
          "The population size must be positive.");
    }
    m_config.m_populationSize = a_sizeOfPopulation;
  }

  /**
   * Retrieves the population size setup in this Configuration instance.
   *
   * @return population size
   */
  public synchronized int getPopulationSize() {
    return m_config.m_populationSize;
  }

  /**
   * Sets the event manager that is to be associated with this configuration.
   * The event manager is responsible for the management of event subscribers
   * and event notifications.
   *
   * @param a_eventManagerToSet the EventManager instance to use in this
   * configuration
   *
   * @throws InvalidConfigurationException if the event manager is null
   * or this Configuration object is locked
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public void setEventManager(final IEventManager a_eventManagerToSet)
      throws InvalidConfigurationException {
    verifyChangesAllowed();
    // Sanity check: Make sure that the given event manager isn't null.
    // ----------------------------------------------------------------
    if (a_eventManagerToSet == null) {
      throw new InvalidConfigurationException(
          "The event manager instance must not be null.");
    }
    // Ensure that no other event manager has been set in a different
    // configuration object within the same thread!
    // --------------------------------------------------------------
    checkProperty(PROPERTY_EVENT_MGR_INST, a_eventManagerToSet,m_eventManager,
                  "Event manager has already been set differently.");
    m_eventManager = a_eventManagerToSet;
  }

  /**
   * Retrieves the event manager associated with this configuration. The event
   * manager is responsible for the management of event subscribers
   * and event notifications.
   *
   * @return the actively configured event manager instance
   *
   * @since 1.0
   */
  public IEventManager getEventManager() {
    return m_eventManager;
  }

  /**
   * Sets the ChromosomePool that is to be associated with this
   * configuration. The ChromosomePool is used to pool discarded Chromosome
   * instances so that they may be recycled later, thereby saving memory and
   * the time to construct them from scratch. The presence of a ChromosomePool
   * is optional. If none exists, then a new Chromosome will be constructed
   * each time one is needed.
   *
   * @param a_chromosomePoolToSet the ChromosomePool instance to use
   * @throws InvalidConfigurationException if this object is locked
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public void setChromosomePool(final IChromosomePool a_chromosomePoolToSet)
      throws InvalidConfigurationException {
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
   * @return the ChromosomePool instance associated this configuration, or
   * null if none has been provided
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public IChromosomePool getChromosomePool() {
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
   * It's possible to test whether this object is locked through the
   * isLocked() method.
   * <p>
   * It is ok to lock an object more than once. In that case, this method
   * does nothing and simply returns.
   *
   * @throws InvalidConfigurationException if this Configuration object is
   * in an invalid state at the time of invocation
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public synchronized void lockSettings()
      throws InvalidConfigurationException {
    if (!m_settingsLocked) {
      verifyStateIsValid();
//      // Make genetic operators list immutable.
//      // --------------------------------------
//      m_geneticOperators = Collections.unmodifiableList(m_geneticOperators);
      m_settingsLocked = true;
    }
  }

  /**
   * Retrieves the lock status of this object.
   *
   * @return true if this object has been locked by a previous successful
   * call to the lockSettings() method, false otherwise
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
   * is not valid. The error message in the exception will detail the reason
   * for invalidity
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
    if (m_config.m_populationSize <= 0) {
      throw new InvalidConfigurationException(
          "A population size greater than zero must be specified in " +
          "the active configuration.");
    }
    if (m_fitnessEvaluator == null) {
      throw new IllegalArgumentException(
          "The fitness evaluator must not be null.");
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
            "The sample Gene at gene position (locus) "
            + i
            + " does not appear to have a working equals() or compareTo()"
            + " method.\n"
            + "It could also be that you forgot to implement method"
            + " newGene() in your Gene implementation.\n"
            + "When tested, the method returned false when comparing "
            + "the sample gene with a gene of the same type and "
            + "possessing the same value (allele).");
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
   * locked
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
   * Normally, you would add a selector that is applied after the genetic
   * operators are processed (a_processBeforeGeneticOperators = false).
   *
   * @param a_selector the selector to be added to the chain
   * @param a_processBeforeGeneticOperators true: execute NaturalSelector
   * before any genetic operator will be applied, false: .. after..
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void addNaturalSelector(NaturalSelector a_selector,
                                 boolean a_processBeforeGeneticOperators)
      throws InvalidConfigurationException {
    verifyChangesAllowed();
    if (a_processBeforeGeneticOperators) {
      m_preSelectors.addNaturalSelector(a_selector);
    }
    else {
      m_postSelectors.addNaturalSelector(a_selector);
    }
  }

  /**
   * Minimum size guaranteed for population. This is significant during
   * evolution as natural selectors could select fewer chromosomes for the next
   * generation than the initial population size was.
   * @param a_minimumSizeGuaranteedPercent if zero or below then no ensurance
   * for size given, see Genotype.evolve()
   *
   * @author Klaus Meffert
   */
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
   * Set the fitness evaluator (deciding if a given fitness value is better
   * when it's higher or better when it's lower).
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
    // Ensure that no other fitness evaluator has been set in a
    // different configuration object within the same thread!
    // --------------------------------------------------------
    checkProperty(PROPERTY_FITEVAL_INST, a_fitnessEvaluator,m_fitnessEvaluator,
                  "Fitness evaluator has already been set differently.");
    m_fitnessEvaluator = a_fitnessEvaluator;
  }

  /**
   * @return true: fittest chromosome should always be transferred to next
   * generation
   *
   * @author Klaus Meffert
   * @since 2.1
   */
  public boolean isPreserveFittestIndividual() {
    return m_preserveFittestIndividual;
  }

  /**
   * Determines whether to save (keep) the fittest individual.
   * @param a_preserveFittest true: always transfer fittest chromosome to next
   * generation
   *
   * @author Klaus Meffert
   * @since 2.1
   */
  public void setPreservFittestIndividual(boolean a_preserveFittest) {
    m_preserveFittestIndividual = a_preserveFittest;
  }

  public void incrementGenerationNr() {
    m_generationNr++;
  }

  public int getGenerationNr() {
    return m_generationNr;
  }

  /**
   * Implementation of the Configurable interface.
   * @return ConfigurationHandler
   * @throws ConfigException
   *
   * @author Siddhartha Azad
   */
  public ConfigurationHandler getConfigurationHandler() {
    return m_conHandler;
  }

  /**
   * @return string representation of the configuration containing all
   * configurable elements
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public String toString() {
    String result = S_CONFIGURATION + ":";
    // Basic parameters.
    // -----------------
    result += "\n " + S_CONFIGURATION_NAME + ": " + getName();
    result += "\n " + S_POPULATION_SIZE + ": " + getPopulationSize();
    result += "\n " + S_MINPOPSIZE + ": " + getMinimumPopSizePercent();
    result += "\n " + S_CHROMOSOME_SIZE + ": " + getChromosomeSize();
    // Sample chromosome.
    // ------------------
    result += "\n " + S_SAMPLE_CHROM + ":\n";
    if (getSampleChromosome() == null) {
      result += "\n null";
    }
    else {
      result += "\n    " + S_SIZE + ": " + getSampleChromosome().size();
      result += "\n    " + S_TOSTRING + ": " + getSampleChromosome().toString();
    }
    // Random generator.
    // -----------------
    result += "\n  " + S_RANDOM_GENERATOR + ": ";
    if (getRandomGenerator() != null) {
      result += getRandomGenerator().getClass().getName();
    }
    else {
      result += S_NONE;
    }
    result += "\n  " + S_EVENT_MANAGER + ": ";
    // Event manager.
    // --------------
    if (getEventManager() == null) {
      result += S_NONE;
    }
    else {
      result += getEventManager().getClass().getName();
    }
    // Configuration handler.
    // ----------------------
    result += "\n " + S_CONFIGURATION_HANDLER + ": ";
    if (getConfigurationHandler() == null) {
      result += "null";
    }
    else {
      result += getConfigurationHandler().getName();
    }
    // Fitness function.
    // -----------------
    result += "\n " + S_FITNESS_FUNCTION + ": ";
    if (getFitnessFunction() == null) {
      result += "null";
    }
    else {
      result += getFitnessFunction().getClass().getName();
    }
    // Fitness evaluator.
    // ------------------
    result += "\n " + S_FITNESS_EVALUATOR + ": ";
    if (getFitnessEvaluator() == null) {
      result += "null";
    }
    else {
      result += getFitnessEvaluator().getClass().getName();
    }
    // Genetic operators.
    // ------------------
    result += "\n  " + S_GENETIC_OPERATORS + ": ";
    if (getGeneticOperators() == null) {
      result += "null";
    }
    else {
      int gensize = getGeneticOperators().size();
      if (gensize < 1) {
        result += S_NONE;
      }
      else {
        for (int i = 0; i < gensize; i++) {
          if (i > 0) {
            result += "; ";
          }
          result += " " + getGeneticOperators().get(i).getClass().getName();
        }
      }
    }
    // Natural selectors (pre).
    // ------------------------
    int natsize = getNaturalSelectors(true).size();
    result += "\n  " + S_NATURAL_SELECTORS + "(" + S_PRE + "): ";
    if (natsize < 1) {
      result += S_NONE;
    }
    else {
      for (int i = 0; i < natsize; i++) {
        if (i > 0) {
          result += "; ";
        }
        result += " " + getNaturalSelectors(true).get(i).getClass().getName();
      }
    }
    // Natural selectors (post).
    // -------------------------
    natsize = getNaturalSelectors(false).size();
    result += "\n  " + S_NATURAL_SELECTORS + "(" + S_POST + "): ";
    if (natsize < 1) {
      result += "none";
    }
    else {
      for (int i = 0; i < natsize; i++) {
        if (i > 0) {
          result += "; ";
        }
        result += " " + getNaturalSelectors(false).get(i).getClass().getName();
      }
    }
//    String popSelector;
//    if (m_popConstantSelector == null) {
//      popSelector = "null";
//    }
//    else {
//      popSelector = m_popConstantSelector.getClass().getName();
//    }
//    result += "\n " + S_POPCONSTANT_SELECTOR + ": "
//        + popSelector;
    return result;
  }

  /**
   * See setKeepPopulationSizeConstant and
   * GABreeder#evolve(Population, Configuration) for detailled explanation.
   * @return true: population size will always be the same size
   * (as given with Configuration.setPopulationSize(int)
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public boolean isKeepPopulationSizeConstant() {
    return m_keepPopulationSizeConstant;
  }


  /**
   * Allows to keep the population size constant during one evolution, even if
   * there is no appropriate instance of NaturalSelector (such as
   * WeightedRouletteSelector) registered with the Configuration.<p>
   * See setKeepPopulationSizeConstant and
   * GABreeder#evolve(Population, Configuration) for detailled explanation.
   *
   * @param a_keepPopSizeConstant true: population size will always be
   * the same size (as given with Configuration.setPopulationSize(int)
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void setKeepPopulationSizeConstant(boolean a_keepPopSizeConstant) {
    m_keepPopulationSizeConstant = a_keepPopSizeConstant;
  }

  public void setSelectFromPrevGen(double a_percentage) {
    if (a_percentage < 0 || a_percentage > 1.00) {
      throw new IllegalArgumentException("Argument must be between 0 and 1");
    }
    m_selectFromPrevGen = a_percentage;
  }

  public double getSelectFromPrevGen() {
    return m_selectFromPrevGen;
  }

  /**
   * @return the JGAP factory registered
   *
   * @author Klaus Meffert
   */
  public IJGAPFactory getJGAPFactory() {
    return m_factory;
  }

  public class ConfigurationConfigurable
      implements Serializable {
    /**
     * The number of chromosomes that will be stored in the Genotype.
     */
    int m_populationSize;
  }

  /**
   * Builds a string considering the current thread and the given id.
   *
   * @param current the current Thread
   * @param a_id a hopefully unique id of the configuration
   *
   * @return string built up
   *
   * @author Klaus Meffert
   * @since 3.01
   */
  protected static String getThreadKey(Thread current, String a_id) {
    return current.toString() + "|" + a_id + "|";
  }

  /**
   * @param a_factory the IJGAPFactory to use
   *
   * @author Klaus Meffert
   * @since 3.01
   */
  public void setJGAPFactory(IJGAPFactory a_factory) {
    m_factory = a_factory;
  }

  /**
   * @param a_breeder the breeder to use
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void setBreeder(IBreeder a_breeder) {
    assert(a_breeder != null);
    m_breeder = a_breeder;
  }

  /**
   * @return the breeder set or new standard breeder instance in case no breeder
   * was set before.
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public IBreeder getBreeder() {
    if (m_breeder == null) {
      m_breeder = new GABreeder();
    }
    return m_breeder;
  }

//  /**
//   * If population size should be kept constant then this selector determines
//   * which of the chromosomes to select into the next generation.
//   *
//   * @param a_popConstantSelector the selector to use
//   * @throws InvalidConfigurationException
//   *
//   * @author Klaus Meffert
//   * @since 3.2.2
//   */
//  public void setKeepPopConstantSelector(INaturalSelector a_popConstantSelector)
//      throws InvalidConfigurationException {
//    verifyChangesAllowed();
//    m_popConstantSelector = a_popConstantSelector;
//  }

//  /**
//   * @return the registered selector to keep population size constant. If none
//   * is set, the NewestChromosomesSelector is instantiated, assigned and
//   * returned
//   *
//   * @throws InvalidConfigurationException
//   *
//   * @author Klaus Meffert
//   * @since 3.2.2
//   */
//  public INaturalSelector getKeepPopConstantSelector()
//  throws InvalidConfigurationException{
//    if (m_popConstantSelector == null) {
//      m_popConstantSelector = new NewestChromosomesSelector(this);
//    }
//    return m_popConstantSelector;
//  }

  /**
   * @param a_alwaysCalculate true: Chromosome.getFitnessValue() will always
   * (re-)calculate the fitness value. This may be necessary in case of
   * environments where the state changes without the chromosome to notice.
   *
   * @author Klaus Meffert
   * @since 3.2.2
   */
  public void setAlwaysCaculateFitness(boolean a_alwaysCalculate) {
    m_alwaysCalculateFitness = a_alwaysCalculate;
  }

  /**
   * @return true: Chromosome.getFitnessValue() will always (re-)calculate the
   * fitness value. This may be necessary in case of environments where the
   * state changes without the chromosome to notice.
   *
   * @author Klaus Meffert
   * @since 3.2.2
   */
  public boolean isAlwaysCalculateFitness() {
    return m_alwaysCalculateFitness;
  }


  protected String makeThreadKey() {
    Thread current = Thread.currentThread();
    threadKey = getThreadKey(current, m_id);
    return threadKey;
  }

  /**
   * Sets a property in the bag.
   *
   * Be aware that setting in unserializable value here leads to problems with
   * distributed computing or other scenarios based on serialization!
   *
   * Be aware that cloning may also lead to conflicts
   * in case you use unsupported object types!
   *
   * @param a_propName name of the property to se the value for
   * @param a_value value of the property
   *
   * @author Klaus Meffert
   * @since 3.3.1
   */
//  public void setPropertyInBag(String a_propName, Object a_value) {
//    m_propertyBag.put(a_propName, a_value);
//  }

  /**
   *
   * @param a_propName name of the property to retrieve the value from
   * @return value of the property
   *
   * @author Klaus Meffert
   * @since 3.3.1
   */
//  public Object getPropertyFromBag(String a_propName) {
//   return m_propertyBag.get(a_propName);
//  }

  /**
   * Deserialize the object. Needed to provide a unique ID for each thread the
   * object is used in.
   *
   * @param a_inputStream the ObjectInputStream provided for deserialzation
   * @throws ClassNotFoundException
   * @throws IOException
   *
   * @author Klaus Meffert
   * @since 3.01
   */
  private void readObject(ObjectInputStream a_inputStream)
      throws ClassNotFoundException, IOException {
    // Always perform the default de-serialization first.
    // --------------------------------------------------
    a_inputStream.defaultReadObject();
    makeThreadKey();
  }

  /**
   * @return the id of the configuration set
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public String getId() {
    return m_id;
  }

  /**
   * Only to be called by sub classes, such as GPConfiguration.
   *
   * @param a_id the id to set
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  protected void setId(String a_id) {
    m_id = a_id;
  }

  /**
   * @return deep clone of this instance
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public Object clone() {
    return newInstance(m_id, m_name);
  }

  /**
   * Creates a new Configuration instance by cloning. Allows to preset the
   * ID and the name.
   *
   * @param a_id new ID for clone
   * @param a_name new name for clone
   * @return deep clone of this instance
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public Configuration newInstance(String a_id, String a_name) {
    try {
      Configuration result = new Configuration(m_name);
      // Clone JGAPFactory first because it helps in cloning other objects.
      // ------------------------------------------------------------------
      if (m_factory instanceof ICloneable) {
        result.m_factory = (IJGAPFactory)((ICloneable)m_factory).clone();
      }
      else {
        // We must fallback to a standardized solution.
        // --------------------------------------------
        m_factory = new JGAPFactory(false);
        result.m_factory = (IJGAPFactory)((JGAPFactory)m_factory).clone();
      }
      if (m_breeder != null) {
        result.m_breeder = (IBreeder)doClone(m_breeder);
      }
      if (m_bulkObjectiveFunction != null) {
        result.m_bulkObjectiveFunction = m_bulkObjectiveFunction;
      }
      result.m_chromosomeSize = m_chromosomeSize;
//    result.m_chromosomePool = m_chromosomePool.clone();
//    result.m_conHandler = m_conHandler.clone();
      result.m_eventManager = (IEventManager)doClone(m_eventManager);
      result.m_fitnessEvaluator = (FitnessEvaluator)doClone(m_fitnessEvaluator);
      result.m_generationNr = 0;
      result.m_geneticOperators = (List)doClone(m_geneticOperators);
      result.m_keepPopulationSizeConstant = m_keepPopulationSizeConstant;
      result.m_minPercentageSizePopulation = m_minPercentageSizePopulation;
      result.m_selectFromPrevGen = m_selectFromPrevGen;
      result.m_objectiveFunction = (FitnessFunction)doClone(m_objectiveFunction);
//      result.m_popConstantSelector = (INaturalSelector)doClone(m_popConstantSelector);
      result.m_postSelectors = (ChainOfSelectors)doClone(m_postSelectors);
      result.m_preSelectors = (ChainOfSelectors)doClone(m_preSelectors);
      result.m_preserveFittestIndividual = m_preserveFittestIndividual;
      result.m_randomGenerator = (RandomGenerator)doClone(m_randomGenerator);
      if (m_sampleChromosome != null) {
        result.m_sampleChromosome = (IChromosome) m_sampleChromosome.clone();
      }
      result.m_alwaysCalculateFitness = m_alwaysCalculateFitness;
      result.m_settingsLocked = m_settingsLocked;
//      result.m_propertyBag = (Map)doClone(m_propertyBag);
      // Configurable data.
      // ------------------
      result.m_config = new ConfigurationConfigurable();
      result.m_config.m_populationSize = m_config.m_populationSize;
      // Identificative data.
      // --------------------
      result.m_name = a_name;
      result.m_id = a_id;
      result.makeThreadKey();// Must be called after m_id is set
      return result;
    } catch (Throwable t) {
      throw new CloneException(t);
    }
  }

  /**
   * Helper called from clone.
   *
   * @param a_objToClone the object to clone
   * @return cloned object or null, if cloning not possible
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  protected Object doClone(Object a_objToClone) throws Exception {
    if (a_objToClone != null) {
      ICloneHandler handler = getJGAPFactory().getCloneHandlerFor(
          a_objToClone, null);
      if (handler != null) {
        return handler.perform(a_objToClone, null, null);
      }
      else {
        /**@todo try cloning in a standard way*/
      }
    }
    return null;
  }

  /**
   * The equals-method.
   *
   * @param a_other the other object to compare
   * @return true if seen as equal
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public boolean equals(Object a_other) {
    return compareTo(a_other) == 0;
  }

  /**
   * The compareTo-method.
   *
   * @param a_other the other object to compare
   * @return -1, 0, 1
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public int compareTo(Object a_other) {
    if (a_other == null) {
      return 1;
    }
    else {
      Configuration other = (Configuration) a_other;
      try {
        return new CompareToBuilder()
            .append(m_config.m_populationSize, other.m_config.m_populationSize)
            .append(m_factory, other.m_factory)
            .append(m_breeder, other.m_breeder)
            .append(m_objectiveFunction, other.m_objectiveFunction)
            .append(m_fitnessEvaluator, other.m_fitnessEvaluator)
            .append(m_bulkObjectiveFunction, other.m_bulkObjectiveFunction)
            .append(m_sampleChromosome, other.m_sampleChromosome)
            .append(m_randomGenerator, other.m_randomGenerator)
//            .append(m_eventManager, other.m_eventManager)
//            .append(m_chromosomePool, other.m_chromosomePool)
            .append(m_geneticOperators.toArray(), other.m_geneticOperators.toArray())
            .append(m_chromosomeSize, other.m_chromosomeSize)
            .append(m_preSelectors, other.m_preSelectors)
            .append(m_postSelectors, other.m_postSelectors)
//            .append(m_popConstantSelector, other.m_popConstantSelector)
            .append(m_preserveFittestIndividual,
                    other.m_preserveFittestIndividual)
//            .append(m_conHandler, other.m_conHandler)
            .append(threadKey, other.threadKey)
            .append(m_keepPopulationSizeConstant,
                    other.m_keepPopulationSizeConstant)
            .append(m_alwaysCalculateFitness, other.m_alwaysCalculateFitness)
            .append(m_minPercentageSizePopulation,
                    other.m_minPercentageSizePopulation)
            .append(m_selectFromPrevGen,other.m_selectFromPrevGen)
            .append(m_generationNr, other.m_generationNr)
            .append(m_name, other.m_name)
//            .append(m_propertyBag, other.m_propertyBag)
            .append(m_settingsLocked, other.m_settingsLocked)
            .toComparison();
      } catch (ClassCastException cex) {
        throw new RuntimeException("Cannot compare all objects within"
                                   + " org.jgap.Configuration, because at"
                                   + " least one does not implement interface"
                                   + " java.lang.Comparable!");
      }
    }
  }

  /**
   * Sets a monitor for auditing evolution progress.
   *
   * @param a_monitor the monitor to use
   *
   * @author Klaus Meffert
   * @since 3.5
   */
  public void setMonitor(IEvolutionMonitor a_monitor) {
    m_monitor = a_monitor;
    if(m_monitor != null) {
      setUniqueKeysActive(true);
    }
  }

  /**
   * @return the monitor uses for auditing the evolution progress.
   *
   * @author Klaus Meffert
   * @since 3.5
   */
  public IEvolutionMonitor getMonitor() {
    return m_monitor;
  }

  /**
   * @param a_active true: use unique keys to allow tracking and monitoring
   *
   * @author Klaus Meffert
   * @since 3.5
   */
  public void setUniqueKeysActive(boolean a_active) {
    m_uniqueKeysActive = a_active;
  }

  /**
   * @return true: use unique keys to allow tracking and monitoring
   *
   * @author Klaus Meffert
   * @since 3.5
   */
  public boolean isUniqueKeysActive() {
    return m_uniqueKeysActive;
  }
}
