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
 * Worker implementation. A worker receives commands from an IMaster instance
 * and returns results to the master. A worker can receive commands even when
 * it is working but it can only work on one task at a time.
 *
 * @author Klaus Meffert
 * @since 2.4
 */
public class Worker
    implements IWorker {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.8 $";

  /**
   * Display name, only textual information.
   */
  private String m_displayName;

  /**
   * Listener to requests from the (one and only) master of this worker
   * (=KKMultiServerThread resp. KKMultiServer (to make it more sophisticated
   * and allow multiple requests from the master at once))
   */
  private MasterListener m_masterListener;

  /**
   * Reference to master for calling him back.
   */
  private MasterInfo m_master;

  /**
   * Construct the worker and tell him who his master is.
   * @param a_displayName name for imformative purpose
   * @param a_master the master of this worker
   * @param a_masterListener listener for requests from master
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public Worker(final String a_displayName, final MasterInfo a_master,
                final MasterListener a_masterListener) {
    m_displayName = a_displayName;
    m_master = a_master;
    m_masterListener = a_masterListener;
  }

  /**
   * @return display name of the worker
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public String getDisplayName() {
    return m_displayName;
  }

  /**
   * Lets a server send a command to process to the worker
   * @param a_command the command to process
   * @return status message
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public Object sendCommand(final WorkerCommand a_command) {
    /**@todo this should be moved to a thread*/
    /**@todo implement:
     * currently working? if yes, add to queue (if queue not full)
     * if no: start work*/
    return null;
  }

  /**
   * @return current status of the entity
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public Object getStatus() {
    /**@todo implement:
     * idle
     * starting
     * receiving task
     * working
     * sending result
     * stopping
     * stopped*/
    return null;
  }

  /**
   * Forces the worker to pause its work (can be resumed)
   * @return status message
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public Object pause() {
    /**@todo implement:
     * able to pause resp. in work?*/
    return null;
  }

  /**
   * Forces the worker to stop its work (cannot be resumed)
   * @return status message
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public Object stop() {
    /**@todo implement:
     * able to stop resp. in work?*/
    return null;
  }

  /**
   * Forces the worker to resume a paused work
   * @return status message
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public Object resume() {
    /**@todo implement:
     * able to resum resp. paused?*/
    return null;
  }
}
