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

/**
 * Monitors the evolution progress extensively.
 *
 * @author Klaus Meffert
 * @since 3.5
 */
public class EvolutionMonitor
    implements IEvolutionMonitor {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  private long m_startMillis;

  private long m_lastCheckMillis;

  private int m_checks;

  private Evaluator m_evaluator;

  public EvolutionMonitor() {
    init();
  }

  protected void init() {
    PermutingConfiguration permConfig = new PermutingConfiguration();
    m_evaluator = new Evaluator(permConfig);
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
    }
    m_lastCheckMillis = System.currentTimeMillis();
    m_checks++;
    return true;
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
    m_startMillis = System.currentTimeMillis();
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
      Population pop = (Population) a_information[0];
      // Record population as now each chromosome has a fitness value assigned.
      // ----------------------------------------------------------------------
      m_evaluator.storePopulation(0, m_checks, pop);
    }
    if (a_monitorEvent.equals(IEvolutionMonitor.MONITOR_EVENT_BEFORE_SELECT)) {
      NaturalSelector selector = (NaturalSelector) a_information[0];
      Population pop = (Population) a_information[1];
      int selectionSize = (Integer) a_information[2];
      boolean a_processBeforeGeneticOperators = (Boolean) a_information[3];
    }
    if (a_monitorEvent.equals(IEvolutionMonitor.MONITOR_EVENT_AFTER_SELECT)) {
      NaturalSelector selector = (NaturalSelector) a_information[0];
      Population pop = (Population) a_information[1];
      Population newPop = (Population) a_information[2];
      int selectionSize = (Integer) a_information[3];
      boolean a_processBeforeGeneticOperators = (Boolean) a_information[4];
      if (a_processBeforeGeneticOperators) {
        // Selection executed before genetic operators.
        // --------------------------------------------
        /**@todo store data for each selector subsequently*/
        m_evaluator.storePopulation(1 + 0 * 1, m_checks, pop);
      }
      else {
        // Selection executed after genetic operators.
        // -------------------------------------------
        /**@todo store data for each selector subsequently*/
        m_evaluator.storePopulation(30 + 0 * 1, m_checks, pop);
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
      /**@todo store data for each operator subsequently*/
      m_evaluator.storePopulation(10 + 0 * 1, m_checks, pop);
    }
    if (a_monitorEvent.equals(IEvolutionMonitor.MONITOR_EVENT_BEFORE_BULK_EVAL)) {
      BulkFitnessFunction bulkFitnessFunction = (BulkFitnessFunction)
          a_information[0];
      Population pop = (Population) a_information[1];
    }
    if (a_monitorEvent.equals(IEvolutionMonitor.MONITOR_EVENT_AFTER_BULK_EVAL)) {
      BulkFitnessFunction bulkFitnessFunction = (BulkFitnessFunction)
          a_information[0];
      Population pop = (Population) a_information[1];
      m_evaluator.storePopulation(20, m_checks, pop);
    }
    if (a_monitorEvent.equals(IEvolutionMonitor.
                              MONITOR_EVENT_BEFORE_UPDATE_CHROMOSOMES2)) {
      Population pop = (Population) a_information[0];
    }
    if (a_monitorEvent.equals(IEvolutionMonitor.
                              MONITOR_EVENT_AFTER_UPDATE_CHROMOSOMES2)) {
      Population pop = (Population) a_information[0];
      m_evaluator.storePopulation(21, m_checks, pop);
    }
    if (a_monitorEvent.equals(IEvolutionMonitor.
                              MONITOR_EVENT_BEFORE_ADD_CHROMOSOME)) {
      Population pop = (Population) a_information[0];
      IChromosome newChromosome = (IChromosome) a_information[1];
      try {
        /**@todo eleganter machen*/
        m_evaluator.storePopulation(22, m_checks,
                                    new Population(pop.getConfiguration(),
            newChromosome));
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    if (a_monitorEvent.equals(IEvolutionMonitor.MONITOR_EVENT_READD_FITTEST)) {
      Population pop = (Population) a_information[0];
      IChromosome fittest = (IChromosome) a_information[1];
      try {
        /**@todo eleganter machen*/
        m_evaluator.storePopulation(23, m_checks,
                                    new Population(pop.getConfiguration(),
            fittest));
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }
}
