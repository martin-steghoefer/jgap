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

import java.util.*;

import org.jgap.audit.*;
import org.jgap.event.*;

/**
 * Abstract base class for breeders.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public abstract class BreederBase
    implements IBreeder {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.10 $";

  public BreederBase() {
  }

  /**
   * Applies all NaturalSelectors registered with the Configuration.
   *
   * @param a_config the configuration to use
   * @param a_pop the population to use as input
   * @param a_processBeforeGeneticOperators true apply NaturalSelectors
   * applicable before GeneticOperators, false: apply the ones applicable
   * after GeneticOperators
   * @return selected part of input population
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  protected Population applyNaturalSelectors(Configuration a_config,
      Population a_pop, boolean a_processBeforeGeneticOperators) {
    /**@todo optionally use working pool*/
    boolean monitorActive = a_config.getMonitor() != null;
    try {
      int selectorSize = a_config.getNaturalSelectorsSize(
          a_processBeforeGeneticOperators);
      if (selectorSize > 0) {
        int population_size = a_config.getPopulationSize();
        // Only select part of the previous population into this generation.
        // -----------------------------------------------------------------
        population_size = (int) Math.round(population_size *
            a_config.getSelectFromPrevGen());
        int single_selection_size;
        Population new_population = new Population(a_config, population_size);
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
            single_selection_size = population_size - new_population.size();
          }
          else {
            single_selection_size = population_size / selectorSize;
          }
          if (monitorActive) {
            // Monitor that selection is going to be performed.
            // ------------------------------------------------
            a_config.getMonitor().event(
                IEvolutionMonitor.MONITOR_EVENT_BEFORE_SELECT,
                a_config.getGenerationNr(),
                new Object[] {selector, a_pop, single_selection_size,
                a_processBeforeGeneticOperators});
          }
          // Do selection of chromosomes.
          // ----------------------------
          /**@todo utilize jobs: integrate job into NaturalSelector!*/
          selector.select(single_selection_size, a_pop, new_population);
          if (monitorActive) {
            // Monitor population after selection took place.
            // ----------------------------------------------
            a_config.getMonitor().event(
                IEvolutionMonitor.MONITOR_EVENT_AFTER_SELECT,
                a_config.getGenerationNr(),
                new Object[] {selector, a_pop, new_population,
                single_selection_size, a_processBeforeGeneticOperators});
          }
          // Clean up the natural selector.
          // ------------------------------
          selector.empty();
        }
        return new_population;
      }
      else {
        return a_pop;
      }
    } catch (InvalidConfigurationException iex) {
      // This exception should never be reached.
      // ---------------------------------------
      throw new IllegalStateException(iex);
    }
  }

  /**
   * Applies all GeneticOperators registered with the Configuration.
   *
   * @param a_config the configuration to use
   * @param a_pop the population to use as input
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  protected void applyGeneticOperators(Configuration a_config, Population a_pop) {
    List geneticOperators = a_config.getGeneticOperators();
    Iterator operatorIterator = geneticOperators.iterator();
    boolean monitorActive = a_config.getMonitor() != null;
    while (operatorIterator.hasNext()) {
      GeneticOperator operator = (GeneticOperator) operatorIterator.next();
      /**@todo utilize jobs: integrate job into GeneticOperator*/
      // Fire listener before genetic operator will be executed.
      // -------------------------------------------------------
      a_config.getEventManager().fireGeneticEvent(
          new GeneticEvent(GeneticEvent.BEFORE_GENETIC_OPERATOR, new Object[] {
                           this, operator}));
      if (monitorActive) {
        // Monitor that operator will be performed.
        // ----------------------------------------
        a_config.getMonitor().event(
            IEvolutionMonitor.MONITOR_EVENT_BEFORE_OPERATE,
            a_config.getGenerationNr(),
            new Object[] {operator, a_pop, a_pop.getChromosomes()});
      }
      operator.operate(a_pop, a_pop.getChromosomes());
      if (monitorActive) {
        // Monitor that operator has been performed.
        // -----------------------------------------
        a_config.getMonitor().event(
            IEvolutionMonitor.MONITOR_EVENT_AFTER_OPERATE,
            a_config.getGenerationNr(),
            new Object[] {operator, a_pop, a_pop.getChromosomes()});
      }
      // Fire listener after genetic operator has been executed.
      // -------------------------------------------------------
      a_config.getEventManager().fireGeneticEvent(
          new GeneticEvent(GeneticEvent.AFTER_GENETIC_OPERATOR, new Object[] {
                           this, operator}));
    }
  }

  /**
   * @return deep clone of this instance
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public abstract Object clone();

  /**
   * @param a_other object to compare to
   * @return as always
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public int compareTo(Object a_other) {
    if (a_other == null) {
      return 1;
    }
    return getClass().getName().compareTo(a_other.getClass().getName());
  }
}
