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
 * Monitors the evolution and stops it after a given number of seconds.
 *
 * @author Klaus Meffert
 * @since 3.4.4
 */
public class TimedMonitor
    implements IEvolutionMonitor {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.5 $";

  private long m_startMillis;

  private int m_seconds;

  /**
   * Constructor.
   *
   * @param a_seconds number of seconds to let the evolution run
   *
   * @author Klaus Meffert
   * @since 3.4.4
   */
  public TimedMonitor(int a_seconds) {
    m_seconds = a_seconds;
  }

  /**
   * Called after another evolution cycle has been executed.
   *
   * @param a_pop the currently evolved population
   * @param a_messages the monitor can append messages here to indicate why
   * it asks evolution to stop
   * @return true: continue with the evolution; false: stop evolution
   *
   * @author Klaus Meffert
   * @since 3.4.4
   */
  public boolean nextCycle(Population a_pop, List<String> a_messages) {
    long currentMillis = System.currentTimeMillis();
    if (currentMillis - m_startMillis >= m_seconds * 1000) {
      a_messages.add(m_seconds + " seconds maximum runtime were reached.");
      return false;
    }
    else {
      return true;
    }
  }

  /**
   * Called just before the evolution starts.
   *
   * @param a_config the configuration used
   *
   * @author Klaus Meffert
   * @since 3.4.4
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
    // Not needed here.
    // ----------------
  }

  /**
   * @return null as no data is gathered by this monitor
   *
   * @author Klaus Meffert
   * @since 3.5
   */
  public PopulationHistoryIndexed getPopulations() {
    return null;
  }
}
