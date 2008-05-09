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

import java.io.*;

import org.jgap.util.*;

/**
 * Represents an IMaster instance. Distributes work to IWorker instances.
 * Allows to receive new tasks and send them to the workers when applicable.
 *
 * @author Klaus Meffert
 * @since 2.3
 */
public abstract class Master {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.12 $";

  /**
   * Information needed by workers
   */
  private MasterInfo m_masterinfo;

  /**
   * Dispatcher of a request
   */
  private RequestDispatcher m_dispatcher;

  /**
   * Listener to requests and answers from workers (= KKMultiServer)
   */
  private WorkerListener m_workerListener;

  /**
   * Constructor.
   * @param a_dispatcher the dispatcher to use for requests to workers
   * @param a_workerListener the listener to use for listening to worker
   * messages
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public Master(final RequestDispatcher a_dispatcher,
                final WorkerListener a_workerListener)
      throws Exception {
    m_dispatcher = a_dispatcher;
    m_workerListener = a_workerListener;
    m_masterinfo = new MasterInfo();
    m_masterinfo.m_IPAddress = NetworkKit.getLocalIPAddress();
    m_masterinfo.m_name = NetworkKit.getLocalHostName();
  }

  /**
   * Starts the master listener. Implement in specific implementations of
   * Master.
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public abstract void start()
      throws Exception;

  /**
   * Stops the master from being working.
   */
  public void stop() {
    m_workerListener.stop();
    /**@todo notify all workers to stop working???
     * No, better would be: next time master is available it can receive
     * old results from workers. So, the workers need to store them for
     * some time, until the master is able to receive the worker results.*/
  }

  /**
   * @return information about this master
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public MasterInfo getMasterInfo() {
    return m_masterinfo;
  }

  /**
   * Sends a command to a worker.
   * @param a_worker the worker to send the command to
   * @param a_command the command to send
   * @throws IOException
   */
  public void sendToWorker(final IWorker a_worker,
                           final WorkerCommand a_command)
      throws IOException {
    /**@todo implement*/
    m_dispatcher.dispatch(a_worker, a_command);
  }

  /**
   *
   * @return the RequestDispatcher used
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public RequestDispatcher getDispatcher() {
    return m_dispatcher;
  }

  /**
   * @return the WorkerListener used
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public WorkerListener getWorkerListener() {
    return m_workerListener;
  }

}
