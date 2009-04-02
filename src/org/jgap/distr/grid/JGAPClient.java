/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.distr.grid;

import org.apache.log4j.*;
import org.homedns.dade.jcgrid.client.*;
import org.jgap.*;
import org.homedns.dade.jcgrid.message.*;
import org.homedns.dade.jcgrid.cmd.*;
import org.apache.commons.cli.*;

/**
 * A client defines work for the grid and sends it to the JGAPServer.
 * Use this class to get an idea for your grid client implementation
 * or see the samples under package examples.grid to have templates for
 * a) fitness computation and
 * b) evolution
 * on the worker side.
 *
 * @author Klaus Meffert
 * @since 3.01
 */
public class JGAPClient
    extends Thread {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.14 $";

  private final static String className = JGAPClient.class.getName();

  private static Logger log = Logger.getLogger(className);

  protected GridNodeClientConfig m_gridconfig;

  protected JGAPRequest m_workReq;

  private GridClient m_gc;

  private IGridConfiguration m_gridConfig;

  public JGAPClient(GridNodeClientConfig a_gridconfig, String a_clientClassName)
      throws Exception {
    m_gridconfig = a_gridconfig;
    Class client = Class.forName(a_clientClassName);
    m_gridConfig = (IGridConfiguration) client.getConstructor(new
        Class[] {}).newInstance(new Object[] {});
    m_gridConfig.initialize(m_gridconfig);
    // Setup work request.
    // -------------------
    JGAPRequest req = new JGAPRequest(m_gridconfig.getSessionName(), 0,
                                  m_gridConfig.getConfiguration());
    req.setWorkerReturnStrategy(m_gridConfig.getWorkerReturnStrategy());
    req.setGenotypeInitializer(m_gridConfig.getGenotypeInitializer());
    req.setEvolveStrategy(m_gridConfig.getWorkerEvolveStrategy());
    // Evolution takes place on client here!
    // -------------------------------------
    req.setEvolveStrategy(null);
    setWorkRequest(req);
    // Start the threaded process.
    // ---------------------------
    start();
    join();
  }

  public void setWorkRequest(JGAPRequest a_request) {
    m_workReq = a_request;
  }

  protected GridClient startClient()
      throws Exception {
    GridClient gc = new GridClient();
    gc.setNodeConfig(m_gridconfig);
    gc.start();
    return gc;
  }

  /**
   * Threaded: Splits work, sends it to workers and receives computed solutions.
   *
   * @author Klaus Meffert
   * @since 3.01
   */
  public void run() {
    try {
      if (m_gridConfig.getClientFeedback() == null) {
        // Use dummy feedback handler.
        // ---------------------------
        log.info("No feedback handler set, using DefaultClientFeedback");
        m_gridConfig.setClientFeedback(new DefaultClientFeedback());
      }
      // Start client.
      // -------------
      m_gc = startClient();
      try {
        // Initialize evolution.
        // ---------------------
        IClientEvolveStrategy clientEvolver = m_gridConfig.
            getClientEvolveStrategy();
        if (clientEvolver != null) {
          clientEvolver.initialize(m_gc, getConfiguration(),
                                    m_gridConfig.getClientFeedback());
        }
        // Do the evolution.
        // -----------------
        evolve(m_gc);
      } finally {
        try {
          m_gc.stop();
        } catch (Exception ex) {}
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      if (m_gridConfig.getClientFeedback() != null) {
        m_gridConfig.getClientFeedback().error("Error while doing the work", ex);
      }
    }
  }

  protected void sendWorkRequests(JGAPRequest[] a_workList)
      throws Exception {
    // Send work requests.
    // -------------------
    for (int i = 0; i < a_workList.length; i++) {
      JGAPRequest req = a_workList[i];
      m_gridConfig.getClientFeedback().sendingFragmentRequest(req);
      m_gc.send(new GridMessageWorkRequest(req));
      if (this.isInterrupted()) {
        break;
      }
    }
  }

  protected void receiveWorkResults(JGAPRequest[] workList)
      throws Exception {
    IClientFeedback feedback = m_gridConfig.getClientFeedback();
    // Receive work results.
    // ---------------------
    int idx = -1;
    for (int i = 0; i < workList.length; i++) {
      feedback.setProgressValue(i + workList.length);
      m_gc.getGridMessageChannel();
      GridMessageWorkResult gmwr = (GridMessageWorkResult) m_gc.recv(i);
      JGAPResult workResult = (JGAPResult) gmwr.getWorkResult();
      m_gridConfig.getClientEvolveStrategy().resultReceived(workResult);
      idx = workResult.getRID();
      // Fire listener.
      // --------------
      feedback.receivedFragmentResult(workList[idx], workResult,
                                      idx);
      if (this.isInterrupted()) {
        break;
      }
    }
  }

  /**
   * If necessary: override to implement your evolution cycle individually.
   *
   * @param gc GridClient
   * @throws Exception
   */
  protected void evolve(GridClient gc)
      throws Exception {
    // Do the complete evolution cycle until end.
    // ------------------------------------------
    IClientFeedback feedback = m_gridConfig.getClientFeedback();
    feedback.beginWork();
    IClientEvolveStrategy evolver = m_gridConfig.getClientEvolveStrategy();
    IRequestSplitStrategy splitter = m_gridConfig.getRequestSplitStrategy();
    int evolutionIndex = 0;
    do {
//      m_clientEvolveStrategy.beforeGenerateWorkResults();
      JGAPRequest[] workRequests = evolver.generateWorkRequests(
          m_workReq, splitter, null);
      feedback.setProgressMaximum(0);
      feedback.setProgressMaximum(workRequests.length - 1);
      sendWorkRequests(workRequests);
      if (this.isInterrupted()) {
        break;
      }
      evolver.afterWorkRequestsSent();
      receiveWorkResults(workRequests);
      evolver.evolve();
      // Fire listener that one evolution cycle is complete.
      // ---------------------------------------------------
      feedback.completeFrame(evolutionIndex);
      evolutionIndex++;
      // Check if evolution is finished.
      // -------------------------------
      if (evolver.isEvolutionFinished(evolutionIndex)) {
        evolver.onFinished();
        break;
      }
    } while (true);
    m_gridConfig.getClientFeedback().endWork();
  }

  public void start() {
    try {
      m_gridConfig.validate();
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
    super.start();
  }

  public Configuration getConfiguration() {
    return m_gridConfig.getConfiguration();
  }

  /**
   * Starts a client (first parameter: name of specific setup class).
   *
   * @param args String[]
   *
   * @author Klaus Meffert
   * @since 3.01
   */
  public static void main(String[] args) {
    if (args.length < 1) {
      System.out.println(
          "Please provide a name of the grid configuration class to use");
      System.out.println("An example class would be "
                         + "examples.grid.fitnessDistributed.GridConfiguration");
      System.exit(1);
    }
    try {
      // Setup logging.
      // --------------
      MainCmd.setUpLog4J("client", true);
      // Command line options.
      // ---------------------
      GridNodeClientConfig config = new GridNodeClientConfig();
      Options options = new Options();
      CommandLine cmd = MainCmd.parseCommonOptions(options, config, args);
      // Setup and start client.
      // -----------------------
      new JGAPClient(config, args[0]);
    } catch (Exception ex) {
      ex.printStackTrace();
      System.exit(1);
    }
  }
}
