/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.distr.grid;

import org.apache.log4j.*;
import org.homedns.dade.jcgrid.client.*;
import org.jgap.*;
import org.homedns.dade.jcgrid.message.GridMessageWorkResult;
import org.homedns.dade.jcgrid.message.GridMessageWorkRequest;

/**
 * A client defines work for the grid and sends it to the JGAPServer.
 *
 * @author Klaus Meffert
 * @since 3.01
 */
public abstract class JGAPClient
    extends Thread {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.5 $";

  private final static String className = JGAPClient.class.getName();

  private static Logger log = Logger.getLogger(className);

  protected GridNodeClientConfig m_gridconfig;

  protected IClientFeedback m_clientFeedback;

  protected JGAPRequest m_workReq;

  protected RequestSplitStrategy m_splitStrategy;

  public JGAPClient(GridNodeClientConfig a_gridconfig, //Configuration cfg,
                    IClientFeedback feedback, JGAPRequest req) {
    m_gridconfig = a_gridconfig;
//    clientCfg = cfg;
    m_clientFeedback = feedback;
    m_workReq = req;
  }

  public JGAPClient(GridNodeClientConfig a_gridconfig) {
    m_gridconfig = a_gridconfig;
  }

  public void setClientFeedback(IClientFeedback a_feedbackObject) {
    m_clientFeedback = a_feedbackObject;
  }

  public void setWorkRequest(JGAPRequest a_request) {
    m_workReq = a_request;
  }

  /**
   * Threaded: Splits work, sends it to workers and receives computed solutions.
   *
   * @author Klaus Meffert
   * @since 3.01
   */
  public void run() {
    try {
      // Start Client.
      // -------------
      GridClient gc = new GridClient();
      gc.setNodeConfig(m_gridconfig);
      gc.start();
      try {
        /**@todo here we can care about distributed fitness computation
         * For that let compute the fitness of each individual by the workers.
         * Afterwards do the evolution and continue with previous step until
         * target reached*/
        // Split the work. This is done by the work request.
        // -------------------------------------------------
        JGAPRequest[] workList;
        workList = m_splitStrategy.split(m_workReq);
        m_clientFeedback.setProgressMaximum(0);
        m_clientFeedback.setProgressMaximum(workList.length - 1);
        m_clientFeedback.beginWork();
        // Send work requests.
        // -------------------
        for (int i = 0; i < workList.length; i++) {
          JGAPRequest req = workList[i];
          m_clientFeedback.sendingFragmentRequest(req);
          gc.send(new GridMessageWorkRequest(req));
          if (this.isInterrupted())
            break;
        }
        // Receive work results.
        // ---------------------
        for (int i = 0; i < workList.length; i++) {
          m_clientFeedback.setProgressValue(i + workList.length);
          GridMessageWorkResult gmwr = (GridMessageWorkResult) gc.recv();
          JGAPResult workResult = (JGAPResult) gmwr.getWorkResult();
          int idx = workResult.getRID();
          m_clientFeedback.receivedFragmentResult(workList[idx], workResult,
              idx);
          m_clientFeedback.completeFrame(idx);
          if (this.isInterrupted()) {
            break;
          }
        }
      } finally {
        try {
          gc.stop();
        } catch (Exception ex) {}
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      m_clientFeedback.error("Error while doing the work", ex);
    }
    m_clientFeedback.endWork();
  }

  public void setRequestSplitStrategy(RequestSplitStrategy a_splitStrategy) {
    m_splitStrategy = a_splitStrategy;
  }

  public void start() {
    if (m_splitStrategy == null) {
      throw new RuntimeException("Please set the request split strategy first"
                                 +" with JGAPClient before starting it!");
    }
    super.start();
  }
}
