/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.impl.job;

import org.jgap.*;
import org.jgap.*;
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
public class EvolveJob
    extends JobBase implements IEvolveJob {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.12 $";

  public EvolveJob(JobData a_data) {
    super(a_data);
  }

  /**
   * Execute the evolution via JGAP.
   *
   * @param a_data input parameter of type EvolveData
   * @return result of the evolution
   *
   * @throws Exception in case of any error
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public JobResult execute(JobData a_data)
      throws Exception {
    EvolveData data = (EvolveData) a_data;
    return evolve(data);
  }

  /**
   * Does the genetic evolution.
   *
   * @param a_evolveData parameters for the evolution
   * @return result of the evolution
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public EvolveResult evolve(EvolveData a_evolveData) {
    EvolveResult result = new EvolveResult();
    Configuration config = a_evolveData.getConfiguration();
    result.setConfiguration(config);
    Population pop = a_evolveData.getPopulation();
    // Breed a new population by performing evolution.
    // -----------------------------------------------
    IBreeder breeder = a_evolveData.getBreeder();
    pop = breeder.evolve(pop, config);
    //
    result.setPopulation(pop);
    return result;
  }

  /**
   * Applies all NaturalSelectors registered with the Configuration.
   *
   * @param a_processBeforeGeneticOperators true apply NaturalSelectors
   * applicable before GeneticOperators, false: apply the ones applicable
   * after GeneticOperators
   *
   * @author Klaus Meffert
   * @since 3.2
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
          // Do selection of chromosomes.
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
      else {
        return a_pop;
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
   * @since 3.2
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
