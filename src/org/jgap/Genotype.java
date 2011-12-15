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

import org.jgap.audit.*;
import org.jgap.distr.*;
import org.jgap.impl.job.*;

/**
 * Genotypes are fixed-length populations of chromosomes. As an instance of
 * a Genotype is evolved, all of its Chromosomes are also evolved. A Genotype
 * may be constructed normally via constructor, or the static
 * randomInitialGenotype() method can be used to generate a Genotype with a
 * randomized Chromosome population.
 * <p>
 * Please note that among all created Genotype instances there may only be one
 * configuration, used by all Genotype instances.
 *
 * @author Neil Rotstan
 * @author Klaus Meffert
 * @since 1.0
 */
public class Genotype
    implements Serializable, Runnable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.109 $";

  /**
   * The current Configuration instance.
   * @since 1.0
   */
  private Configuration m_activeConfiguration;

  private transient static Configuration m_staticConfiguration;

  /**
   * The array of Chromosomes that make-up the Genotype's population.
   * @since 2.0
   */
  private Population m_population;

  /** Use an enolution monitor
   * @since 3.6
   */
  private boolean m_useMonitor;

  private IEvolutionMonitor m_monitor;

  /**
   * Constructs a new Genotype instance with the given array of Chromosomes and
   * the given active Configuration instance. Note that the Configuration object
   * must be in a valid state when this method is invoked, or a
   * InvalidConfigurationException will be thrown.
   *
   * @param a_configuration the Configuration object to use
   * @param a_initialChromosomes the Chromosome population to be managed by
   * this Genotype instance
   * @throws InvalidConfigurationException if the given Configuration object is
   * in an invalid state
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 1.0
   * @deprecated use Genotype(Configuration, Population) instead
   */
  public Genotype(Configuration a_configuration,
                  IChromosome[] a_initialChromosomes)
      throws InvalidConfigurationException {
    this(a_configuration, new Population(a_configuration, a_initialChromosomes));
  }

  /**
   * Constructs a new Genotype instance with the given array of
   * Chromosomes and the given active Configuration instance. Note
   * that the Configuration object must be in a valid state
   * when this method is invoked, or a InvalidconfigurationException
   * will be thrown.
   *
   * @param a_configuration the Configuration object to use
   * @param a_population the Chromosome population to be managed by this
   * Genotype instance
   * @throws InvalidConfigurationException
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 2.0
   */
  public Genotype(Configuration a_configuration, Population a_population)
      throws InvalidConfigurationException {
    // Sanity checks: Make sure neither the Configuration, nor the array
    // of Chromosomes, nor any of the Genes inside the array is null.
    // -----------------------------------------------------------------
    if (a_configuration == null) {
      throw new IllegalArgumentException(
          "The Configuration instance must not be null.");
    }
    if (a_population == null) {
      throw new IllegalArgumentException(
          "The Population must not be null.");
    }
    for (int i = 0; i < a_population.size(); i++) {
      if (a_population.getChromosome(i) == null) {
        throw new IllegalArgumentException(
            "The Chromosome instance at index " + i + " of the array of " +
            "Chromosomes is null. No Chromosomes instance in this array " +
            "must not be null.");
      }
    }
    m_population = a_population;
    // Lock the settings of the configuration object so that it cannot
    // be altered.
    // ---------------------------------------------------------------
    a_configuration.lockSettings();
    m_activeConfiguration = a_configuration;
  }

  /**
   * Don't use this constructor, it's only for internal use.
   *
   * @param a_configuration not used here!
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public Genotype(Configuration a_configuration)
      throws InvalidConfigurationException {
  }

  /**
   * Retrieves the array of Chromosomes that make up the population of this
   * Genotype instance.
   *
   * @return the Population of Chromosomes
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 1.0
   * @deprecated uses getPopulation() instead
   */
  public synchronized IChromosome[] getChromosomes() {
    Iterator it = getPopulation().iterator();
    IChromosome[] result = new Chromosome[getPopulation().size()];
    int i = 0;
    while (it.hasNext()) {
      result[i++] = (IChromosome) it.next();
    }
    return result;
  }

  /**
   * @return the current population of chromosomes
   *
   * @author Klaus Meffert
   * @since 2.1
   */
  public Population getPopulation() {
    return m_population;
  }

  /**
   * Retrieves the Chromosome in the Population with the highest fitness
   * value.
   *
   * @return the Chromosome with the highest fitness value, or null if there
   * are no chromosomes in this Genotype
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 1.0
   */
  public synchronized IChromosome getFittestChromosome() {
    return getPopulation().determineFittestChromosome();
  }

  /**
   * Retrieves the Chromosome in the Population with the highest fitness
   * value within the given indices.
   *
   * @param a_startIndex the index to start the determination with
   * @param a_endIndex the index to end the determination with
   * @return the Chromosome with the highest fitness value within the given
   * indices, or null if there are no chromosomes in this Genotype
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public synchronized IChromosome getFittestChromosome(int a_startIndex,
      int a_endIndex) {
    return getPopulation().determineFittestChromosome(a_startIndex, a_endIndex);
  }

  /**
   * Retrieves the top n Chromsomes in the population (the ones with the best
   * fitness values).
   *
   * @param a_numberOfChromosomes the number of chromosomes desired
   * @return the list of Chromosomes with the highest fitness values, or null
   * if there are no chromosomes in this Genotype
   *
   * @author Charles Kevin Hill
   * @since 2.4
   */
  public synchronized List getFittestChromosomes(int a_numberOfChromosomes) {
    return getPopulation().determineFittestChromosomes(a_numberOfChromosomes);
  }

  /**
   * Evolves the population of Chromosomes within this Genotype. This will
   * execute all of the genetic operators added to the present active
   * configuration and then invoke the natural selector to choose which
   * chromosomes will be included in the next generation population. Note
   * that the population size not always remains constant (dependent on the
   * NaturalSelectors used!).
   * To consecutively call this method, use evolve(int)!!!
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 1.0
   */
  public synchronized void evolve() {
    IBreeder breeder = getConfiguration().getBreeder();
    Population newPop = breeder.evolve(getPopulation(), getConfiguration());
    setPopulation(newPop);
  }

  /**
   * Evolves this Genotype the specified number of times. This is
   * equivalent to invoking the standard evolve() method the given number
   * of times in a row.
   *
   * @param a_numberOfEvolutions the number of times to evolve this Genotype
   * before returning
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void evolve(int a_numberOfEvolutions) {
    for (int i = 0; i < a_numberOfEvolutions; i++) {
      evolve();
    }
    if (m_activeConfiguration.isKeepPopulationSizeConstant()) {
      keepPopSizeConstant(getPopulation(),
                          m_activeConfiguration.getPopulationSize());
    }
  }

  /**
   * Evolves this genotype until the given monitor asks to quit the evolution
   * cycle.
   *
   * @param a_monitor the monitor used to decide when to stop evolution
   *
   * @return messages of the registered evolution monitor. May indicate why the
   * evolution was asked to be stopped. May be empty, depending on the
   * implementation of the used monitor
   *
   * @author Klaus Meffert
   * @since 3.4.4
   */
  public List<String> evolve(IEvolutionMonitor a_monitor) {
    a_monitor.start(getConfiguration());
    getConfiguration().setMonitor(a_monitor);
    List<String> messages = new Vector();
    do {
      evolve();
      boolean goon = a_monitor.nextCycle(getPopulation(), messages);
      if (!goon) {
        break;
      }
    } while (true);
    return messages;
  }

  /**
   * @return string representation of this Genotype instance, useful for display
   * or debug purposes
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    for (int i = 0; i < getPopulation().size(); i++) {
      buffer.append(getPopulation().getChromosome(i).toString());
      buffer.append(" [");
      buffer.append(getPopulation().getChromosome(i).getFitnessValueDirectly());
      buffer.append("]\n");
    }
    return buffer.toString();
  }

  /**
   * Convenience method that returns a newly constructed Genotype
   * instance configured according to the given Configuration instance.
   * The population of Chromosomes will be created according to the setup of
   * the sample Chromosome in the Configuration object, but the gene values
   * (alleles) will be set to random legal values.
   *
   * @param a_configuration the current active Configuration object
   * @return a newly constructed Genotype instance
   *
   * @throws IllegalArgumentException if the given Configuration object is null
   * @throws InvalidConfigurationException if the given Configuration
   * instance is not in a valid state
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 2.3
   */
  public static Genotype randomInitialGenotype(Configuration
      a_configuration)
      throws InvalidConfigurationException {
    if (a_configuration == null) {
      throw new IllegalArgumentException(
          "The Configuration instance may not be null.");
    }
    a_configuration.lockSettings();
    // Create an array of chromosomes equal to the desired size in the
    // active Configuration and then populate that array with Chromosome
    // instances constructed according to the setup in the sample
    // Chromosome, but with random gene values (alleles). The Chromosome
    // class randomInitialChromosome() method will take care of that for
    // us.
    // ------------------------------------------------------------------
    int populationSize = a_configuration.getPopulationSize();
    Population pop = new Population(a_configuration, populationSize);
    // Do randomized initialization.
    // -----------------------------
    Genotype result = new Genotype(a_configuration, pop);
    result.fillPopulation(populationSize);
    return result;
  }

  /**
   * Fills up the population with random chromosomes if necessary.
   *
   * @param a_num the number of chromosomes to add
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void fillPopulation(final int a_num)
      throws InvalidConfigurationException {
    IChromosome sampleChrom = getConfiguration().getSampleChromosome();
    Class sampleClass = sampleChrom.getClass();
    IInitializer chromIniter = getConfiguration().getJGAPFactory().
        getInitializerFor(sampleChrom, sampleClass);
    if (chromIniter == null) {
      throw new InvalidConfigurationException("No initializer found for class "
          + sampleClass);
    }
    try {
      for (int i = 0; i < a_num; i++) {
        getPopulation().addChromosome( (IChromosome) chromIniter.perform(
            sampleChrom,
            sampleClass, null));
      }
    } catch (Exception ex) {
      // Try to propagate exception, see "bug" 1661635.
      // ----------------------------------------------
      if (ex.getCause() != null) {
        throw new IllegalStateException(ex.getCause().toString());
      }
      else {
        throw new IllegalStateException(ex.getMessage());
      }
    }
  }

  /**
   * Compares this Genotype against the specified object. The result is true
   * if the argument is an instance of the Genotype class, has exactly the
   * same number of chromosomes as the given Genotype, and, for each
   * Chromosome in this Genotype, there is an equal chromosome in the
   * given Genotype. The chromosomes do not need to appear in the same order
   * within the population.
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
    } catch (ClassCastException e) {
      return false;
    }
  }

  /**
   * Applies all NaturalSelectors registered with the Configuration.
   *
   * @param a_processBeforeGeneticOperators true apply NaturalSelectors
   * applicable before GeneticOperators, false: apply the ones applicable
   * after GeneticOperators
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  protected void applyNaturalSelectors(
      boolean a_processBeforeGeneticOperators) {
    /**@todo optionally use working pool*/
    try {
      // Process all natural selectors applicable before executing the
      // genetic operators (reproduction, crossing over, mutation...).
      // -------------------------------------------------------------
      int selectorSize = m_activeConfiguration.getNaturalSelectorsSize(
          a_processBeforeGeneticOperators);
      if (selectorSize > 0) {
        int population_size = m_activeConfiguration.getPopulationSize();
        if (a_processBeforeGeneticOperators) {
          // Only select part of the previous population into this generation.
          // -----------------------------------------------------------------
          population_size = (int) Math.round(population_size *
              getConfiguration().getSelectFromPrevGen());
        }
        int single_selection_size;
        Population new_population;
        new_population = new Population(m_activeConfiguration,
                                        population_size);
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
            single_selection_size = population_size - getPopulation().size();
          }
          else {
            single_selection_size = population_size / selectorSize;
          }
          // Do selection of Chromosomes.
          // ----------------------------
          selector.select(single_selection_size, getPopulation(),
                          new_population);
          // Clean up the natural selector.
          // ------------------------------
          selector.empty();
        }
        setPopulation(new Population(m_activeConfiguration));
        getPopulation().addChromosomes(new_population);
      }
    } catch (InvalidConfigurationException iex) {
      // This exception should never be reached
      throw new IllegalStateException(iex.getMessage());
    }
  }

  /**
   * Applies all GeneticOperators registered with the Configuration.
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void applyGeneticOperators() {
    List geneticOperators = m_activeConfiguration.getGeneticOperators();
    Iterator operatorIterator = geneticOperators.iterator();
    while (operatorIterator.hasNext()) {
      GeneticOperator operator = (GeneticOperator) operatorIterator.next();
      applyGeneticOperator(operator, getPopulation(),
                           getPopulation().getChromosomes());
//      List workingPool = new Vector();
//      applyGeneticOperator(operator, getPopulation(),
//                           workingPool);
    }
  }

  /**
   * @return the configuration to use with the Genetic Algorithm
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public static Configuration getStaticConfiguration() {
    return m_staticConfiguration;
  }

  /**
   * Sets the configuration to use with the Genetic Algorithm.
   * @param a_configuration the configuration to use
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public static void setStaticConfiguration(Configuration a_configuration) {
    m_staticConfiguration = a_configuration;
  }

  public Configuration getConfiguration() {
    return m_activeConfiguration;
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
   * @return the computed hashcode
   *
   * @author vamsi
   * @since 2.1
   */
  public int hashCode() {
    int i, size = getPopulation().size();
    IChromosome s;
    int twopower = 1;
    // For empty genotype we want a special value different from other hashcode
    // implementations.
    // ------------------------------------------------------------------------
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

  /**
   * Overwritable method that calls a GeneticOperator to operate on a given
   * population and asks him to store the result in the list of chromosomes.
   * Override this method if you want to ensure that a_chromosomes is not
   * part of a_population resp. if you want to use a different list.
   *
   * @param a_operator the GeneticOperator to call
   * @param a_population the population to use
   * @param a_chromosomes the list of Chromosome objects to return
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  protected void applyGeneticOperator(GeneticOperator a_operator,
                                      Population a_population,
                                      List a_chromosomes) {
    a_operator.operate(a_population, a_chromosomes);
  }

  /**
   * Cares that the population size does not exceed the given maximum size.
   *
   * @param a_pop the population to keep constant in size
   * @param a_maxSize the maximum size allowed for the population
   *
   * @author Klaus Meffert
   * @since 2.5
   */
  protected void keepPopSizeConstant(Population a_pop, int a_maxSize) {
    /**@todo use StandardPostSelector instead?*/
    int popSize = a_pop.size();
    // See request  1213752.
    // ---------------------
    while (popSize > a_maxSize) {
      // Remove a chromosome.
      // --------------------
      a_pop.removeChromosome(0);
      popSize--;
    }
  }

  /**
   * If used in a Thread: runs the evolution forever.
   * You have to implement a listener to stop computation sometime. See
   * examples.simpleBooleanThreaded for a possible implementation of such a
   * listener.
   *
   * @author Klaus Meffert
   * @since 3.01
   */
  public void run() {
    while (!Thread.currentThread().interrupted()) {
      if(m_useMonitor) {
       evolve(m_monitor);
      }
      else {
        evolve();
      }
    }
  }

  /**
   * Splits a population into pieces that can be evolved independently.
   *
   * @param a_splitter splits the population
   * @return list of IEvolveJob objects
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public List<IEvolveJob> getEvolves(IPopulationSplitter a_splitter)
      throws Exception {
    // We return a list of IEvolveJob instances.
    // -----------------------------------------
    List result = new Vector();
    Population[] pops = a_splitter.split(getPopulation());
    // Feed the population chunks into different evolve jobs.
    // ------------------------------------------------------
    for (int i = 0; i < pops.length; i++) {
      Configuration newConf = (Configuration) getConfiguration().clone();
      // Adjust population size.
      // -----------------------
      /*@todo impl.*/
      EvolveData data = new EvolveData(newConf);
      if (pops[i] == null) {
        throw new IllegalStateException("Population must no be null"
                                        + " (Index: " + i
                                        + ", Splitter: "
                                        + a_splitter.getClass().getName() + ")");
      }
      data.setPopulation(pops[i]);
      data.setBreeder(newConf.getBreeder());
      IEvolveJob evolver = new EvolveJob(data);
      result.add(evolver);
    }
    getPopulation().clear();
    return result;
  }

  public void mergeResults(IPopulationMerger a_merger, EvolveResult[] a_results)
      throws Exception {
    int size = a_results.length;
    Population target = new Population(getConfiguration());
    for (int i = 0; i < size; i++) {
      EvolveResult singleResult = a_results[i];
      if (singleResult == null) {
        throw new IllegalStateException("Single result is null!");
      }
      Population pop = singleResult.getPopulation();
      /**@todo use/enhance IPopulationMerger*/
//      a_merger.mergePopulations()
      List goodOnes = pop.determineFittestChromosomes(3);
      for (int j = 0; j < goodOnes.size(); j++) {
        IChromosome goodOne = (IChromosome) goodOnes.get(j);
        target.addChromosome(goodOne);
      }
    }
    setPopulation(target);
  }

  /**
   * Use an evolution monitor, only to be used when running Genotype as a thread.
   * Otherwise use method evolve(IEvolutionMonitor)
   *
   * @param a_useMonitor true: use evolution monitor, set it via setMonitor
   *
   * @author Klaus Meffert
   * @since 3.6
   */
  public void setUseMonitor(boolean a_useMonitor) {
    m_useMonitor = a_useMonitor;
  }

  /**
   * Sets the evolution monitor to use, activate it via setUseMonitor(true).
   * Only to be used when running Genotype as a thread.
   *
   * @param a_monitor the IEvolutionMonitor to use
   *
   * @author Klaus Meffert
   * @since 3.6
   */
  public void setMonitor(IEvolutionMonitor a_monitor) {
    m_monitor = a_monitor;
  }
}
