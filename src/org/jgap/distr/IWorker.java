/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.distr;

/**
 * Interface for workers processing requests from an IMaster instance.
 *
 * @author Klaus Meffert
 * @since 2.4
 */
public interface IWorker {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.5 $";

  /**
   * @return display name of the worker
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  String getDisplayName();

  /**
   * @return current status of the entity
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  Object getStatus();

  /**
   * Forces the worker to pause its work (can be resumed)
   * @return status message
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  Object pause();

  /**
   * Forces the worker to stop its work (cannot be resumed)
   * @return status message
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  Object stop();

  /**
   * Forces the worker to resume a paused work
   * @return status message
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  Object resume();

  /**
   * Lets a server send a command to process to the worker
   * @param a_command the command to process
   * @return status message
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  Object sendCommand(WorkerCommand a_command);
}
