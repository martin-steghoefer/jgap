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
 * Monitors the evolution and decides when to stop the evolution cycle.
 *
 * @author Klaus Meffert
 * @since 3.4.4
 */
public interface IEvolutionMonitor {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.2 $";

  /**
   * Called just before the evolution starts. Setup your monitor here.
   *
   * @param a_config the configuration used
   *
   * @author Klaus Meffert
   * @since 3.4.4
   */
  void start(Configuration a_config);

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
  boolean nextCycle(Population a_pop, List<String> a_messages);
}
