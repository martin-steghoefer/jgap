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
import org.jgap.eval.PopulationHistoryIndexed;

/**
 * A meta monitor that chains together given monitors and executes them
 * subsequently.
 *
 * @author Klaus Meffert
 * @since 3.4.4
 */
public class ChainedMonitors
    implements IEvolutionMonitor {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.4 $";

  private List<IEvolutionMonitor> m_monitors;

  private int m_positiveMonitorsRequired;

  /**
   *
   * @param a_monitors sequence of monitors to use
   * @param a_positiveMonitorsRequired number of monitors that must return true
   * in nextCycle in order to let the evolution continue. If this parameter
   * equals one, it means we have an or-operation, if it equals the number of
   * monitors, we have an and-operation.
   *
   * @author Klaus Meffert
   * @since 3.4.4
   */
  public ChainedMonitors(List<IEvolutionMonitor> a_monitors,
      int a_positiveMonitorsRequired) {
    if (a_monitors == null || a_monitors.size() < 1) {
      throw new IllegalArgumentException(
          "Number of monitors must be one or greater!");
    }
    if (a_positiveMonitorsRequired < 1) {
      throw new IllegalArgumentException(
          "Number of positive monitors must be one or greater!");
    }
    if (a_positiveMonitorsRequired > a_monitors.size()) {
      throw new IllegalArgumentException("Number of positive monitors must not"
          + " be bigger than number of available monitors!");
    }
    m_monitors = a_monitors;
    m_positiveMonitorsRequired = a_positiveMonitorsRequired;
  }

  /**
   * Called after another evolution cycle has been executed.
   *
   * @param a_pop the currently evolved population
   * @param a_messages the monitor can append messages here to indicate why
   * it asks evolution to stop
   *
   * @return true: continue with the evolution; false: stop evolution
   *
   * @author Klaus Meffert
   * @since 3.4.4
   */
  public boolean nextCycle(Population a_pop, List<String> a_messages) {
    int size = m_monitors.size();
    int positive = 0;
    for (IEvolutionMonitor monitor : m_monitors) {
      if (monitor.nextCycle(a_pop, a_messages)) {
        positive++;
        if (positive >= m_positiveMonitorsRequired) {
          return true;
        }
      }
      else {
        // Check for early fail.
        // ---------------------
        size--;
        if (size + positive < m_positiveMonitorsRequired) {
          return false;
        }
      }
    }
    return false;
  }

  /**
   * Called just before the evolution starts.
   *
   * @param a_config the configuration used
   */
  public void start(Configuration a_config) {
    for (IEvolutionMonitor monitor : m_monitors) {
      monitor.start(a_config);
    }
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
    for (IEvolutionMonitor monitor : m_monitors) {
      monitor.event(a_monitorEvent, a_evolutionNo, a_information);
    }
  }

  /**
   * @return null, use getMonitors() and then call each monitor's method
   *
   * @author Klaus Meffert
   * @since 3.5
   */
  public PopulationHistoryIndexed getPopulations() {
    return null;
  }

  /**
   * @return List of monitors registered
   *
   * @author Klaus Meffert
   * @since 3.5
   */
  public List<IEvolutionMonitor> getMonitors() {
    return m_monitors;
  }
}
