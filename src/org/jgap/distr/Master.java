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

import java.io.*;

/**
 * Represents an IMaster instance. Distributes work to IWorker instances.
 *
 * @author Klaus Meffert
 * @since 2.3
 */
public class Master {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.5 $";

  //information needed by workers
  private MasterInfo m_masterinfo;

  /**
   * Dispatcher of a request
   */
  private RequestDispatcher m_dispatcher;

  /**
   * Listener to requests and answers from workers (= KKMultiServer)
   */
  private WorkerListener m_workerListener;

  public Master(RequestDispatcher a_dispatcher, WorkerListener a_workerListener) {
    m_dispatcher = a_dispatcher;
    m_workerListener = a_workerListener;
  }

  /**
   *
   * @return MasterInfo
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public MasterInfo getMasterInfo() {
    return m_masterinfo;
  }

  public void sendToWorker(IWorker a_worker, WorkerCommand a_command)
      throws IOException {
    /**@todo implement*/
    m_dispatcher.dispatch(a_worker, a_command);
  }
}
