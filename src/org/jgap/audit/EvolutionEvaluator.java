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

//import examples.*;

/**
 * Evaluates evolution progress thas has previously been tracked by
 * EvolutionMonitor (or a similar instance).
 * This class regards a complete evolution, not just a single round. This is an
 * optional way, another would be to just gather data per round and then display
 * or evaluate the round-specific data.
 * This class considers global (complete evolution) data because computations
 * are executed on this data, like tracking chromosomes, evaluating the progress
 * of selectors or genetic operators, etc.
 *
 * @author Klaus Meffert
 * @since 3.5
 */
public class EvolutionEvaluator {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  private PopulationHistoryIndexed m_monitorData;

  private List<Integer> evolutionIndexes;

  public EvolutionEvaluator(PopulationHistoryIndexed a_monitorData) {
    if (a_monitorData == null) {
      throw new IllegalArgumentException("Data must not be null!");
    }
    m_monitorData = a_monitorData;
    evolutionIndexes = new Vector();
  }

  public void evaluate() {
    Map<Integer, Map> data = m_monitorData.getPopulationsHistory();
    Iterator<Integer> evolutions = data.keySet().iterator();
    Population pop1, pop2;
    // Gather all evolution indexes.
    // -----------------------------
    int evNo;
    while (evolutions.hasNext()) {
      evNo = evolutions.next();
      evolutionIndexes.add(evNo);
    }
    // Gather a list of unique IDs for all chromosomes within all populations
    // of all recorded states in all generations.
    // ----------------------------------------------------------------------
    Map<String, Map<IChromosome, List<Integer>>> chromIDs =
        gatherChromosomesIDs(data);
    // Track all chromosomes.
    // ----------------------
    Map<IChromosome, Map<Integer, List<IChromosome>>> chromTrack =
        trackChromosomes(data);
    /**@todo use chromIDs and chromTrack*/
    int size = evolutionIndexes.size();
    for (int i = 0; i < size; i++) {
      evNo = evolutionIndexes.get(i);
      Map<Integer, PopulationContext>
          eventsPops = m_monitorData.getPopulations(evNo);
      // Care about all executed Selectors per generation.
      // -------------------------------------------------
      int index = 0;
      do {
        PopulationContext ctx1 = eventsPops.get(EvolutionMonitor.
            CONTEXT_OFFSET_NATURAL_SELECTOR1 + index * 2 + 0);
        if (ctx1 == null) {
          break;
        }
        pop1 = ctx1.getPopulation();
        NaturalSelector selector = ctx1.getSelector();
        PopulationContext ctx2 = eventsPops.get(EvolutionMonitor.
            CONTEXT_OFFSET_NATURAL_SELECTOR1 + index * 2 + 1);
        pop2 = ctx2.getPopulation();
        // Evaluate average fitness of selection

        // Track which individuals contributed to the top n solutions

        /**@todo */
      } while (true);
      // Executed Operators per generation.

      // Fitness progress intra-generation.

      // Fitness progress over generations.
    }
  }

  /**
   * Tracks the originator chain of each chromosome in the final population.
   * The originator chain potentially begins with the first generation.
   * A chromosome could virtually have as many originators as there are
   * evolutions (minus one), or it could have zero originators. The latter case
   * means the chromosome was just created in the final population.
   *
   * @param a_data population (evolution) data
   * @return the result of the investigation
   *
   * @author Klaus Meffert
   * @since 3.5
   */
  private Map<IChromosome, Map<Integer, List<IChromosome>>>
      trackChromosomes(Map<Integer, Map> a_data) {
    int size = evolutionIndexes.size() - 1;
    Map<Integer, PopulationContext> contextMap = a_data.get(size);
    PopulationContext context = contextMap.get(EvolutionMonitor.
        CONTEXT_END_OF_CYCLE);
    Population finalPop = context.getPopulation();
    return trackChromosomes(a_data, finalPop);
  }

  /**
   *
   * @param a_data Map
   * @param a_pop Population
   * @return the result of the investigation
   *
   * @author Klaus Meffert
   * @since 3.5
   */
  private Map<IChromosome, Map<Integer, List<IChromosome>>>
      trackChromosomes(Map<Integer, Map> a_data, Population a_pop) {
    // A track is a list of chromosomes, with each of them having a list
    // of originators:
    // A Chromosome has originators, possibly from several different evolutions.
    // -------------------------------------------------------------------------
    Map<IChromosome, Map<Integer, List<IChromosome>>> chromosomesTrack
        = new Hashtable();
    int evNo;
    int currentIndex = evolutionIndexes.size();
    if (currentIndex < 1) {
      return chromosomesTrack;
    }
    // Retrieve predecessing population.
    // ---------------------------------
    evNo = evolutionIndexes.get(currentIndex - 1);
    Map<Integer, PopulationContext> contextMap = a_data.get(evNo);
    PopulationContext context = contextMap.get(EvolutionMonitor.
        CONTEXT_END_OF_CYCLE);
    Population previousPop = context.getPopulation();
    Map<Integer, List<IChromosome>> evoMap = new Hashtable();
    // Find originators for all chromosomes in given population.
    // ---------------------------------------------------------
    for (int i = 0; i < a_pop.size(); i++) {
      IChromosome chrom = a_pop.getChromosome(i);
      int uindex = 1;
      List<IChromosome> uids = new Vector();
      // Gather originators for current chromosome in population.
      // --------------------------------------------------------
      do {
        String uid = chrom.getUniqueIDTemplate(uindex++);
        IChromosome chrom0 = null;
        if (uid != null || uindex == 2) {
          // Find chromosome instance for unique ID.
          // ---------------------------------------
          for (int k = 0; k < previousPop.size(); k++) {
            chrom0 = previousPop.getChromosome(k);
            if (chrom0.getUniqueID().equals(uid)) {
              break;
            }
          }
          uids.add(chrom0);
          evoMap.put(evNo, uids);
          chromosomesTrack.put(chrom, evoMap);
        }
        if (uid == null) {
          break;
        }
      } while (true);
    }
    return chromosomesTrack;
  }

  /**
   * For all chromosomes: Gather their IDs and the list of indexes of the
   * evolution rounds they appeared in. Normally, a chromosome with a specific
   * unique ID just appears within one single generation. The reason is, that,
   * normally, in selection and genetic operator, the chromosomes' IDs change,
   * because the chromosomes are cloned.
   *
   * @param a_data monitoring data
   * @return result of investigation
   */
  private Map<String, Map<IChromosome, List<Integer>>> gatherChromosomesIDs(
      Map<Integer, Map> a_data) {
    int size = evolutionIndexes.size() - 1;
    Map<Integer, PopulationContext> contextMap;
    // Gather all chromosomes' unique IDs
    // String = ID, IChromosome = Chromosome for ID, List = Index of Generation
    //   the chromosome appears in
    Map<String, Map<IChromosome, List<Integer>>> chromIDs = new Hashtable();
    // For each generator/evolution round...
    // -------------------------------------
    for (int i = 0; i <= size; i++) {
      int evNo = evolutionIndexes.get(i);
      contextMap = a_data.get(evNo);
      Iterator<PopulationContext> values = contextMap.values().iterator();
      // For each recorded Population state within a generation.
      // -------------------------------------------------------
      while (values.hasNext()) {
        PopulationContext ctx = values.next();
        Population pop = ctx.getPopulation();
        // For each chromosome...
        // ----------------------
        for (int j = 0; j < pop.size(); j++) {
          IChromosome chrom = pop.getChromosome(j);
          Map<IChromosome, List<Integer>> entry = chromIDs.get(chrom.getUniqueID());
          List valueList;
          if(entry == null) {
            // Add a new list with the first found evolution number.
            // -----------------------------------------------------
            valueList = new Vector();
            valueList.add(evNo);
            Map valueMap = new Hashtable();
            valueMap.put(chrom, valueList);
            chromIDs.put(chrom.getUniqueID(), valueMap);
          }
          else {
            // Already found in another evolution, just add the new number.
            // ------------------------------------------------------------
            valueList = entry.values().iterator().next();
            if(!valueList.contains(evNo)) {
              valueList.add(evNo);
            }
          }
        }
      }
    }
    return chromIDs;
  }

  /**
   * Sample usage of monitoring, together with an existing JGAP example.
   *
   * @param args String[]
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.5
   */
//  public static void main(String[] args)
//      throws Exception {
//    args = new String[2];
//    args[0] = "93";
//    args[1] = "MONITOR";
//    MinimizingMakeChange.main(args);
//    IEvolutionMonitor monitor = MinimizingMakeChange.m_monitor;
//    EvolutionEvaluator evaluator = new EvolutionEvaluator(
//        monitor.getPopulations());
//    evaluator.evaluate();
//  }
}
