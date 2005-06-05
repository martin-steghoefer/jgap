/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.distr;

/**
 * Worker implementation. A worker receives commands from an IMaster instance
 * and returns results to the master.
 *
 * @author Klaus Meffert
 * @since 2.4
 */
public abstract class Worker
    implements IWorker {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.5 $";

  /**@todo remove abstract, implement all methods*/
  private String m_displayName;

  /**
   * Listener to requests from the (one and only) master of this worker
   * (=KKMultiServerThread resp. KKMultiServer (to make it more sophisticated
   * and allow multiple requests from the master at once))
   */
  private MasterListener m_masterListener;

  // reference to master for calling him back
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
  public Worker(String a_displayName, MasterInfo a_master,
                MasterListener a_masterListener) {
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
   */
  public Object sendCommand(WorkerCommand a_command) {
    /**@todo implement*/
    return null;
  }
}
