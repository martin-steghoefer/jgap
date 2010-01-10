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

import java.io.*;
import java.util.*;

import org.jgap.*;
import org.jgap.eval.*;

/**
 * Monitors evolution and decides when to stop the evolution cycle.
 *
 * @author Klaus Meffert
 * @since 3.4.4
 */
public interface IEvolutionMonitor extends Serializable {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.5 $";

  public final static String MONITOR_EVENT_REMOVE_CHROMOSOME =
      "remove_chromosome";

  public final static String MONITOR_EVENT_BEFORE_UPDATE_CHROMOSOMES1 =
      "before_update_chromosomes(1)";

  public final static String MONITOR_EVENT_BEFORE_UPDATE_CHROMOSOMES2 =
      "before_update_chromosomes(2)";

  public final static String MONITOR_EVENT_AFTER_UPDATE_CHROMOSOMES1 =
      "after_update_chromosomes(1)";

  public final static String MONITOR_EVENT_AFTER_UPDATE_CHROMOSOMES2 =
      "after_update_chromosomes(2)";

  public final static String MONITOR_EVENT_BEFORE_SELECT = "before_select";

  public final static String MONITOR_EVENT_AFTER_SELECT = "after_select";

  public final static String MONITOR_EVENT_BEFORE_OPERATE = "before_operate";

  public final static String MONITOR_EVENT_AFTER_OPERATE = "after_operate";

  public final static String MONITOR_EVENT_BEFORE_BULK_EVAL =
      "before_bulk_eval";

  public final static String MONITOR_EVENT_AFTER_BULK_EVAL = "after_bulk_eval";

  public final static String MONITOR_EVENT_BEFORE_ADD_CHROMOSOME =
      "before_add_chromosome";

  public final static String MONITOR_EVENT_READD_FITTEST =
      "readd_fittest_chromosome";

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

  /**
   * Called whenever it's worth monitoring.
   *
   * @param a_monitorEvent see constants at top of class
   * @param a_evolutionNo the index of the evolution round (1, 2, ...)
   * @param a_information event-specific information
   *
   * @author Klaus Meffert
   * @since 3.5
   */
  void event(String a_monitorEvent, int a_evolutionNo, Object[] a_information);

  /**
   * @return the gathered monitoring data
   *
   * @author Klaus Meffert
   * @since 3.5
   */
  PopulationHistoryIndexed getPopulations();
}
