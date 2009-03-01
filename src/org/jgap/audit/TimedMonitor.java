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

import org.jgap.*;

/**
 * Monitors the evolution and stops it after a given number of seconds.
 *
 * @author Klaus Meffert
 * @since 3.4.4
 */
public abstract class TimedMonitor
    implements IEvolutionMonitor {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

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
   * @return true: continue with the evolution; false: stop evolution
   *
   * @author Klaus Meffert
   * @since 3.4.4
   */
  public boolean nextCycle(Population a_pop) {
    long currentMillis = System.currentTimeMillis();
    if (currentMillis - m_startMillis >= m_seconds * 1000) {
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
}
