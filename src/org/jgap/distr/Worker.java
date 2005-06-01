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
 * and returns results
 *
 * @author Klaus Meffert
 * @since 2.4
 */
public abstract class Worker
    implements IWorker {
  /**@todo remove abstract, implement all methods*/

  /**
   * Listener to requests from the (one and only) master of this worker
   * (=KKMultiServerThread resp. KKMultiServer (to make it more sophisticated
   * and allow multiple requests from the master at once))
   */
  private MasterListener m_masterListener;

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  // reference to master for calling him back
  private MasterInfo m_master;

  /**
   * Construct worker and tell him who his master is
   * @param a_master
   */
  public Worker(MasterInfo a_master) {
    m_master = a_master;
  }

}
