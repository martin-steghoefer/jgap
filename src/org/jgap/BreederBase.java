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

/**
 * Abstract base class for breeders.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public abstract class BreederBase
    implements IBreeder {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.4 $";

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
    try {
      // Process all natural selectors applicable before executing the
      // genetic operators (reproduction, crossing over, mutation...).
      // -------------------------------------------------------------
      int selectorSize = a_config.getNaturalSelectorsSize(
          a_processBeforeGeneticOperators);
      if (selectorSize > 0) {
        int population_size = a_config.getPopulationSize();
        // Only select part of the previous population into this generation.
        // -----------------------------------------------------------------
        population_size = (int) Math.round(population_size *
            a_config.getSelectFromPrevGen());
        int single_selection_size;
        Population new_population = new Population(a_config,
            population_size);
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
            single_selection_size = population_size - a_pop.size();
          }
          else {
            single_selection_size = population_size / selectorSize;
          }
          // Do selection of chromosomes.
          // ----------------------------
          /**@todo utilize jobs: integrate job into NaturalSelector!*/
          selector.select(single_selection_size, a_pop, new_population);
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
   * @param a_config the configuration to use
   * @param a_pop the population to use as input
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

  /**
   * @return deep clone of this instance
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public abstract Object clone();

  /**
   * @param a_other sic
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
