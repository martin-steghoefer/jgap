/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.audit;

import java.util.*;
import org.jgap.*;
import org.jgap.eval.*;

/**
 * Monitors the evolution progress extensively.
 * Keeps track of the population development for each single generation during
 * the whole evolution process.
 * Monitors chromosomes as well as genes. For each chromosome the originating
 * chromosome is monitored (applies for natural selectors as well as for genetic
 * operators that transform a single chromosome, like MutationOperator).
 * For more complex genetic operators, like CrossoverOperator, all originating
 * chromosomes are monitored (in crossover there are two).
 * For genes an analogue monitoring is supported as for chromosomes.
 *
 * TO ACTIVATE MONITORING:
 * Configuration.setMonitor(new EvolutionMonitor());
 *
 * Each chromosome and gene itself has a unique ID. This ID is set by method
 * setUniqueID. A unique ID is a String value that, in best case, is unique
 * worldwide.
 *
 * A chromosome's or gene's originator is set by method setUniqueIDTemplate.
 * This method accepts an index, as there could be more than one originator
 * (see below).
 *
 * --------------------------------------------------------------------------
 *
 * To give a better view, here are the population snapshots that are taken
 * during a single evolution generation (chromosomes are given by *,+,-,.):
 *
 *     |-------|      |-------|      |-------|      |-------|      |-------|
 *     | ** *  |      | **    |      |  *    |      |  +    |      |  +    |
 *     |* *  * |  =>  |* *  * |  =>  |*    * |  =>  |*    - |  =>  |*    . |
 *     | * **  |      |   **  |      |   **  |      |   **  |      |   -*  |
 *     |-------|      |-------|      |-------|      |-------|      |-------|
 *       Update        Natural        Natural        Genetic        Genetic
 *    Chromosomes     Selector 1     Selector i     Operator 1     Operator j
 *   (Fitness calc.)
 *
 *     |-------|      |-------|      |-------|      |-------|      |-------|
 *     |  +    |      |  +    |      |  +**  |      |  +**  |      |  + *  |
 * =>  |*    . |  =>  |*    . |  =>  |*    . |  =>  |* *  . |  =>  |* *  . |
 *     |   -*  |      |   -*  |      |*  -*  |      |*  -*  |      |*  -   |
 *     |-------|      |-------|      |-------|      |-------|      |-------|
 *    Bulk Fitness      Update        Add new         Re-add        Natural
 *      Function      Chromosomes    Chromosomes      Fittest      Selector k
 *     (optional)                                   Chromosome     (Post-Sel.)
 *
 *     |-------|
 *     |  + *  |
 * =>  |* *  . |
 *     |*  -   |
 *     |-------|
 *    End of cycle
 *
 * For each single evolution cycle, these population states are kept separately.
 *
 * --------------------------------------------------------------------------
 *
 * For each chromosome the originating chromosomes are tracked. Here are examples:
 *
 * Natural Selection, e.g. WeightedRouletteSelector:
 *   Chromosome 4715 has originator Chromosome 4711 (different ID because of
 *   cloning)
 *
 * Genetic Operation, e.g. MutationOperator:
 *   Chromosome 5512 has originator Chromosome 1139
 *
 *  Genetic Operation, e.g. CrossoverOperator:
 *   Chromosome 7119 has originator Chromosomes 807 and 5703
 *   Chromosome 7120 has originator Chromosomes 807 and 5703
 *   => two resulting Chromosomes have the same originators!
 *
 * Genetic, Operation, e.g. YourOperator:
 *   There can be as many originating Chromosomes as your operator regards
 *
 * --------------------------------------------------------------------------
 *
 * For each gene the originating genes are tracked. Examples:
 *
 * Natural Selection, e.g. WeightedRouletteSelector:
 *   All Genes in Chromosome 4711 have the same originating genes as
 *   Chromosome 4711's originating Chromosome's genes.
 *   => Chromosome is selected only, not modified, thus the Genes stick to the
 *      Chromosome they belong to. Genes unique ID of template is not set.
 *
 * Genetic Operation, e.g. MutationOperator:
 *   Each mutated Gene originates from exactly one other Gene
 *
 * Genetic Operation, e.g. CrossoverOperator:
 *   Each crossed over Gene A has one originating Gene B.
 *   In turn, Gene B has one originating Gene A, as their alleles are swapped.
 *   All uncrossed Genes have the same originator as the Chromosome they
 *   belong to, thus their unique ID of template is not set.
 *
 * Genetic, Operation, e.g. YourOperator:
 *   A Gene could virtually have any number of originators.
 *
 * @author Klaus Meffert
 * @since 3.5
 */
public class EvolutionMonitor
    implements IEvolutionMonitor {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  public static final int CONTEXT_UPDATE_CHROMOSOMES1 = 0;

  public static final int CONTEXT_OFFSET_NATURAL_SELECTOR1 = 1;

  public static final int CONTEXT_OFFSET_AFTER_OPERATE = 10;

  public static final int CONTEXT_AFTER_BULK_EVALUATION = 20;

  public static final int CONTEXT_UPDATE_CHROMOSOMES2 = 22;

  public static final int CONTEXT_NEW_CHROMOSOME = 22;

  public static final int CONTEXT_READD_FITTEST = 23;

  public static final int CONTEXT_OFFSET_NATURAL_SELECTOR2 = 30;

  public static final int CONTEXT_END_OF_CYCLE = 999;

  private int m_checks;

  private PopulationHistoryIndexed m_popHist;

  private int m_selectorIndex;

  private int m_operatorIndex;

  public EvolutionMonitor() {
    init();
  }

  protected void init() {
    m_popHist = new PopulationHistoryIndexed();
  }

  /**
   * Called after another evolution cycle has been executed.
   *
   * @param a_pop the currently evolved population
   * @param a_messages the monitor can append messages to indicate why it asks
   * evolution to stop
   * @return true: continue with the evolution; false: stop evolution
   *
   * @author Klaus Meffert
   * @since 3.4.4
   */
  public boolean nextCycle(Population a_pop, List<String> a_messages) {
    // Let's verify the progress since our last check.
    // -----------------------------------------------
    IChromosome best = a_pop.determineFittestChromosome();
    if (best != null) {
      // A best solution exists.
      // -----------------------
    }
    else {
      // No solution exists at all.
      // --------------------------
    }
    // Store the population
    Population popClone = (Population) a_pop.clone();
//    m_evaluator.storePopulation(CONTEXT_END_OF_CYCLE, m_checks, popClone);
    PopulationContext context = new PopulationContext(popClone);
    context.setChromosome(best);
    m_popHist.addPopulation(m_checks, CONTEXT_END_OF_CYCLE, context);
    m_checks++;
    initCounters();
    // No continouos processing as in this example there is an outer main loop.
    // ------------------------------------------------------------------------
    return false;
  }

  /**
   * Called just before the evolution starts.
   *
   * @param a_config the configuration used
   *
   * @author Klaus Meffert
   * @since 3.5
   */
  public void start(Configuration a_config) {
    initCounters();
  }

  private void initCounters() {
    m_selectorIndex = 0;
    m_operatorIndex = 0;
  }

  /**
   * Called whenever it's worth monitoring.
   *
   * @param a_monitorEvent see constants at top of class IEvolutionMonitor
   * @param a_evolutionNo the index of the evolution round (1, 2, ...)
   * @param a_information event-specific information
   *
   * @author Klaus Meffert
   * @since 3.5
   */
  public void event(String a_monitorEvent, int a_evolutionNo,
                    Object[] a_information) {
    if (a_monitorEvent == null) {
      // Should never happen.
      // --------------------
      return;
    }
    if (a_information == null) {
      return;
    }
    PopulationContext context;
    // The events are queried in the chronological order they do appear.
    // -----------------------------------------------------------------
    if (a_monitorEvent.equals(IEvolutionMonitor.MONITOR_EVENT_REMOVE_CHROMOSOME)) {
      // Initiated by Population.keepPopSizeConstant()
      Population pop = (Population) a_information[0];
      Integer chromosomeIndex = (Integer) a_information[1];
      IChromosome chrom = pop.getChromosome(chromosomeIndex);
    }
    if (a_monitorEvent.equals(IEvolutionMonitor.
                              MONITOR_EVENT_BEFORE_UPDATE_CHROMOSOMES1)) {
      // Initiated by GABreeder.evolve before calling updateChromosomes(..)
      Population pop = (Population) a_information[0];
    }
    if (a_monitorEvent.equals(IEvolutionMonitor.
                              MONITOR_EVENT_AFTER_UPDATE_CHROMOSOMES1)) {
      // Initiated by GABreeder.evolve after calling updateChromosomes(..)
      Population pop = (Population) ( (Population) a_information[0]).clone();
      // Record population as now each chromosome has a fitness value assigned.
      // ----------------------------------------------------------------------
//      m_evaluator.storePopulation(CONTEXT_UPDATE_CHROMOSOMES1, m_checks, pop);
      context = new PopulationContext(pop);
      m_popHist.addPopulation(m_checks, CONTEXT_UPDATE_CHROMOSOMES1, context);
    }
    if (a_monitorEvent.equals(IEvolutionMonitor.MONITOR_EVENT_BEFORE_SELECT)) {
      NaturalSelector selector = (NaturalSelector) a_information[0];
      Population pop = (Population) a_information[1];
      int selectionSize = (Integer) a_information[2];
      boolean a_processBeforeGeneticOperators = (Boolean) a_information[3];
    }
    if (a_monitorEvent.equals(IEvolutionMonitor.MONITOR_EVENT_AFTER_SELECT)) {
      NaturalSelector selector = (NaturalSelector) a_information[0];
      Population pop = (Population) ( (Population) a_information[1]).clone();
      Population newPop = (Population) ( (Population) a_information[2]).clone();
      int selectionSize = (Integer) a_information[3];
      boolean a_processBeforeGeneticOperators = (Boolean) a_information[4];
      if (a_processBeforeGeneticOperators) {
        // Selection executed before genetic operators.
        // --------------------------------------------
        // Store data for each selector subsequently.
        // ------------------------------------------
        context = new PopulationContext(pop);
        context.setSelector(selector);
        m_popHist.addPopulation(m_checks, CONTEXT_OFFSET_NATURAL_SELECTOR1 +
                                m_selectorIndex * 2 + 0, context);
        context = new PopulationContext(newPop);
        context.setSelector(selector);
        m_popHist.addPopulation(m_checks, CONTEXT_OFFSET_NATURAL_SELECTOR1 +
                                m_selectorIndex * 2 + 1, context);
        m_selectorIndex++;
      }
      else {
        // Selection executed after genetic operators.
        // -------------------------------------------
        context = new PopulationContext(pop);
        context.setSelector(selector);
        // Store data for each selector subsequently.
        // ------------------------------------------
        m_popHist.addPopulation(m_checks, CONTEXT_OFFSET_NATURAL_SELECTOR2 +
                                m_selectorIndex * 2 + 0, context);
        context = new PopulationContext(newPop);
        context.setSelector(selector);
        m_popHist.addPopulation(m_checks, CONTEXT_OFFSET_NATURAL_SELECTOR2 +
                                m_selectorIndex * 2 + 1, context);
      }
    }
    if (a_monitorEvent.equals(IEvolutionMonitor.MONITOR_EVENT_BEFORE_OPERATE)) {
      GeneticOperator operator = (GeneticOperator) a_information[0];
      Population pop = (Population) a_information[1];
      List<IChromosome> chromosomes = (List<IChromosome>) a_information[2];
    }
    if (a_monitorEvent.equals(IEvolutionMonitor.MONITOR_EVENT_AFTER_OPERATE)) {
      GeneticOperator operator = (GeneticOperator) a_information[0];
      Population pop = (Population) a_information[1];
      List<IChromosome> chromosomes = (List<IChromosome>) a_information[2];
      // Store data for each selector subsequently.
      // ------------------------------------------
      context = new PopulationContext(pop);
      context.setOperator(operator);
      m_popHist.addPopulation(m_checks, CONTEXT_OFFSET_AFTER_OPERATE +
                              m_operatorIndex, context);
      m_operatorIndex++;
      m_selectorIndex = 0;
    }
    if (a_monitorEvent.equals(IEvolutionMonitor.MONITOR_EVENT_BEFORE_BULK_EVAL)) {
      BulkFitnessFunction bulkFitnessFunction = (BulkFitnessFunction)
          a_information[0];
      Population pop = (Population) a_information[1];
    }
    if (a_monitorEvent.equals(IEvolutionMonitor.MONITOR_EVENT_AFTER_BULK_EVAL)) {
      BulkFitnessFunction bulkFitnessFunction = (BulkFitnessFunction)
          a_information[0];
      Population pop = (Population) ( (Population) a_information[1]).clone();
      context = new PopulationContext(pop);
      context.setBulkFitnessFunction(bulkFitnessFunction);
      m_popHist.addPopulation(m_checks, CONTEXT_AFTER_BULK_EVALUATION, context);
    }
    if (a_monitorEvent.equals(IEvolutionMonitor.
                              MONITOR_EVENT_BEFORE_UPDATE_CHROMOSOMES2)) {
      Population pop = (Population) a_information[0];
    }
    if (a_monitorEvent.equals(IEvolutionMonitor.
                              MONITOR_EVENT_AFTER_UPDATE_CHROMOSOMES2)) {
      Population pop = (Population) a_information[0];
      context = new PopulationContext(pop);
      m_popHist.addPopulation(m_checks, CONTEXT_UPDATE_CHROMOSOMES2, context);
    }
    if (a_monitorEvent.equals(IEvolutionMonitor.
                              MONITOR_EVENT_BEFORE_ADD_CHROMOSOME)) {
      Population pop = (Population) a_information[0];
      IChromosome newChromosome = (IChromosome) ( (IChromosome) a_information[1]).
          clone();
      context = new PopulationContext(pop);
      context.setChromosome(newChromosome);
      m_popHist.addPopulation(m_checks, CONTEXT_NEW_CHROMOSOME, context);
    }
    if (a_monitorEvent.equals(IEvolutionMonitor.MONITOR_EVENT_READD_FITTEST)) {
      Population pop = (Population) a_information[0];
      IChromosome fittest = (IChromosome) ( (IChromosome) a_information[1]).
          clone();
      context = new PopulationContext(pop);
      context.setChromosome(fittest);
      m_popHist.addPopulation(m_checks, CONTEXT_READD_FITTEST, context);
    }
  }

  public PopulationHistoryIndexed getPopulations() {
    return m_popHist;
  }
}
