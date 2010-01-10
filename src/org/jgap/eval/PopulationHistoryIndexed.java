/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.eval;

import java.io.*;
import java.util.*;
import org.jgap.*;

/**
 * Manages populations. See class EvolutionMonitor for a description of events
 * and how populations are tracked.
 *
 * @author Klaus Meffert
 * @since 3.5
 */
public class PopulationHistoryIndexed
    implements Serializable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  private Map<Integer, Map> m_evolutions;

  /**
   *
   * @author Klaus Meffert
   * @since 3.5
   */
  public PopulationHistoryIndexed() {
    m_evolutions = new HashMap();
  }

  /**
   *
   * @param a_index evolution number or any other appropriate index
   * @param a_event unique identifier of an event (if needed). See class
   * EvolutionMonitor for event constants
   * @param a_pop the population to add
   *
   * @author Klaus Meffert
   * @since 3.5
   */
  public void addPopulation(int a_index, int a_event, PopulationContext a_pop) {
    Map entry = m_evolutions.get(a_index);
    if (entry == null) {
      entry = new HashMap();
    }
    entry.put(a_event, a_pop);
    m_evolutions.put(a_index, entry);
  }

  /**
   *
   * @param a_index evolution number or any other appropriate index
   * @return Map with tuples <event, Population>
   *
   * @author Klaus Meffert
   * @since 3.5
   */
  public Map<Integer, PopulationContext> getPopulations(int a_index) {
    return m_evolutions.get(a_index);
  }

  /**
   * @return complete population history
   *
   * @author Klaus Meffert
   * @since 3.5
   */
  public Map<Integer, Map> getPopulationsHistory() {
    return m_evolutions;
  }

  /**
   *
   * @param a_index evolution number or any other appropriate index
   * @param a_event unique identifier of an event (if needed). See class
   * EvolutionMonitor for event constants
   * @return the population together with its context
   *
   * @author Klaus Meffert
   * @since 3.5
   */
  public PopulationContext getPopulation(int a_index, int a_event) {
    Map<Integer, PopulationContext> entry = getPopulations(a_index);
    if (entry != null) {
      Iterator<Integer> it = entry.keySet().iterator();
      while(it.hasNext()) {
        Integer key = it.next();
        if(key == a_event) {
          return entry.get(key);
        }
      }
      return null;
    }
    else {
      return null;
    }
  }
}
