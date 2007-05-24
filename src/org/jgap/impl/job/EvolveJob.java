/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.impl.job;

import org.jgap.*;
import org.jgap.event.*;
import org.jgap.impl.*;
import java.util.*;

/**
 * A job that evolves a population. The evolution takes place as given by the
 * configuration. It operates on a population also provided.
 * This class utilizes sub jobs to perform its sub-tasks. Each sub job may
 * either be a local implementation satisfying the IJob interface. Or it may be
 * a job that eventually is put onto the grid and returns with a result!
 *
 * @author Klaus Meffert
 * @since 3.2
 */

public class EvolveJob extends JobBase
    implements IEvolveJob {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.5 $";

  public EvolveJob(Object a_data) {
    super(a_data);
  }

  /**
   * Execute the evolution via JGAP.
   *
   * @param a_data input parameter of type EvolveData
   * @throws Exception in case of any error
   */
  public void execute(Object a_data)
      throws Exception {
    EvolveData data = (EvolveData)a_data;
    evolve(data);
  }

  public void evolve(EvolveData a_evolveData) {
    Configuration config = a_evolveData.getConfiguration();
    Population pop = a_evolveData.getPopulation();
    if (config == null) {
      throw new IllegalStateException(
          "The Configuration object must be set prior to evolution.");
    }
    // Adjust population size to configured size (if wanted).
    // Theoretically, this should be done at the end of this method.
    // But for optimization issues it is not. If it is the last call to
    // evolve() then the resulting population possibly contains more
    // chromosomes than the wanted number. But this is no bad thing as
    // more alternatives mean better chances having a fit candidate.
    // If it is not the last call to evolve() then the next call will
    // ensure the correct population size by calling keepPopSizeConstant.
    // ------------------------------------------------------------------
    if (config.isKeepPopulationSizeConstant()) {
      pop.keepPopSizeConstant();
    }
    // Apply certain NaturalSelectors before GeneticOperators will be applied.
    // -----------------------------------------------------------------------
    pop = applyNaturalSelectors(config, pop, true);
    // Execute all of the Genetic Operators.
    // -------------------------------------
    applyGeneticOperators(config, pop);
    // Reset fitness value of genetically operated chromosomes.
    // Normally, this should not be necessary as the Chromosome
    // class initializes each newly created chromosome with
    // FitnessFunction.NO_FITNESS_VALUE. But who knows which
    // Chromosome implementation is used...
    // --------------------------------------------------------
    int originalPopSize = config.getPopulationSize();
    int currentPopSize = pop.size();
    for (int i = originalPopSize; i < currentPopSize; i++) {
      IChromosome chrom = pop.getChromosome(i);
      chrom.setFitnessValueDirectly(FitnessFunction.NO_FITNESS_VALUE);
    }
    // Apply certain NaturalSelectors after GeneticOperators have been applied.
    // ------------------------------------------------------------------------
    pop = applyNaturalSelectors(config, pop, false);
    // If a bulk fitness function has been provided, call it.
    // ------------------------------------------------------
    BulkFitnessFunction bulkFunction = config.getBulkFitnessFunction();
    if (bulkFunction != null) {
      /**@todo utilize jobs: bulk fitness function is not so important for a
       * prototype! */
      bulkFunction.evaluate(pop);
    }
    // Fill up population randomly if size dropped below specified percentage
    // of original size.
    // ----------------------------------------------------------------------
    if (config.getMinimumPopSizePercent() > 0) {
      int sizeWanted = config.getPopulationSize();
      int popSize;
      int minSize = (int) Math.round(sizeWanted *
                                     (double) config.getMinimumPopSizePercent()
                                     / 100);
      popSize = pop.size();
      if (popSize < minSize) {
        IChromosome newChrom;
        IChromosome sampleChrom = config.getSampleChromosome();
        Class sampleChromClass = sampleChrom.getClass();
        IInitializer chromIniter = config.getJGAPFactory().
            getInitializerFor(sampleChrom, sampleChromClass);
        while (pop.size() < minSize) {
          try {
            /**@todo utilize jobs: initialization may be time-consuming as
             * invalid combinations may have to be filtered out*/
            newChrom = (IChromosome) chromIniter.perform(sampleChrom,
                sampleChromClass, null);
            pop.addChromosome(newChrom);
          } catch (Exception ex) {
            throw new RuntimeException(ex);
          }
        }
      }
    }
    if (config.isPreserveFittestIndividual()) {
      /**@todo utilize jobs. In pop do also utilize jobs, especially for fitness
       * computation*/
      IChromosome fittest = pop.determineFittestChromosome(0,
          config.getPopulationSize() - 1);
      if (config.isKeepPopulationSizeConstant()) {
        pop.keepPopSizeConstant();
      }
      // Determine the fittest chromosome in the population.
      // ---------------------------------------------------
      if (!pop.contains(fittest)) {
        // Re-add fittest chromosome to current population.
        // ------------------------------------------------
        pop.addChromosome(fittest);
      }
    }
    // Increase number of generation.
    // ------------------------------
    config.incrementGenerationNr();
    // Fire an event to indicate we've performed an evolution.
    // -------------------------------------------------------
    config.getEventManager().fireGeneticEvent(
        new GeneticEvent(GeneticEvent.GENOTYPE_EVOLVED_EVENT, this));
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
  protected Population applyNaturalSelectors(Configuration a_config,
      Population a_pop, boolean a_processBeforeGeneticOperators) {
    /**@todo optionally use working pool*/
    try {
      // Process all natural selectors applicable before executing the
      // genetic operators (reproduction, crossing over, mutation...).
      // -------------------------------------------------------------
      int selectorSize = a_config.getNaturalSelectorsSize(
          a_processBeforeGeneticOperators);
      if (selectorSize > 0) {
        int m_population_size = a_config.getPopulationSize();
        int m_single_selection_size;
        Population new_population = new Population(a_config,
            m_population_size);
        NaturalSelector selector;
        // Repopulate the population of chromosomes with those selected
        // by the natural selector. Iterate over all natural selectors.
        // ------------------------------------------------------------
        for (int i = 0; i < selectorSize; i++) {
          selector = a_config.getNaturalSelector(
              a_processBeforeGeneticOperators, i);
          if (i == selectorSize - 1 && i > 0) {
            // Ensure the last NaturalSelector adds the remaining Chromosomes.
            // ---------------------------------------------------------------
            m_single_selection_size = m_population_size - a_pop.size();
          }
          else {
            m_single_selection_size = m_population_size / selectorSize;
          }
          // Do selection of Chromosomes.
          // ----------------------------
          /**@todo utilize jobs: integrate job into NaturalSelector!*/
          selector.select(m_single_selection_size, a_pop, new_population);
          // Clean up the natural selector.
          // ------------------------------
          selector.empty();
        }
//        Population result = new Population(a_config);
//        result.addChromosomes(new_population);
        return new_population;
      }
      return a_pop;
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
  protected void applyGeneticOperators(Configuration a_config, Population a_pop) {
    List geneticOperators = a_config.getGeneticOperators();
    Iterator operatorIterator = geneticOperators.iterator();
    while (operatorIterator.hasNext()) {
      GeneticOperator operator = (GeneticOperator) operatorIterator.next();
      /**@todo utilize jobs: integrate job into GeneticOperator*/
      operator.operate(a_pop, a_pop.getChromosomes());
    }
  }

}
