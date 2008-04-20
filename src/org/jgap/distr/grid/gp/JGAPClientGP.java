/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.distr.grid.gp;

import org.apache.log4j.*;
import org.homedns.dade.jcgrid.client.*;
import org.jgap.*;
import org.jgap.gp.*;
import org.jgap.gp.impl.*;
import org.homedns.dade.jcgrid.message.*;
import org.homedns.dade.jcgrid.cmd.*;
import org.apache.commons.cli.*;
import org.jgap.distr.grid.*;
import org.jgap.distr.grid.common.*;
import rww.google.docs.UploadFailedException;

/**
 * A client defines work for the grid and sends it to the JGAPServer.
 * Use this class as base class for your grid client implementations.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public class JGAPClientGP
    extends Thread {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.9 $";

  public static final String MODULE_CS = "CS";/**@todo das ist nicht module, sondern sender-receiver*/

  public static final String MODULE_SC = "SC";

  public static final String MODULE_SW = "SW";

  public static final String MODULE_WS = "WS";

  public static final String MODULE_ANY = "*";

  public static final String CONTEXT_WORK_REQUEST = "WREQ";

  public static final String CONTEXT_WORK_RESULT = "WRES";

  public static final String CONTEXT_ANY = "W*";

  public static final String CONTEXT_ID_EMPTY = "0";

  public static final String CONTEXT_ID_ANY = "*";

  public static final int TIMEOUT_SECONDS = 20;

  public static final int WAITTIME_SECONDS = 5;

  private transient Logger log = Logger.getLogger(getClass());

  protected GridNodeClientConfig m_gridconfig;

  protected JGAPRequestGP m_workReq;

  private IGridClientMediator m_gcmed;

  private IGridConfigurationGP m_gridConfig;

  /**
   * Is the client operating in a WAN or in a LAN?
   * TRUE:  WAN --> Do not use JCGrid architecture
   * FALSE: LAN --> Do use JCGrid architecture
   */
  private boolean m_WANMode;

  /**
   * Only received results or send requests beforehand?
   */
  private boolean m_receiveOnly;

  public JGAPClientGP(GridNodeClientConfig a_gridconfig,
                      String a_clientClassName,
                      boolean a_WANMode,
                      boolean a_receiveOnly)
      throws Exception {
    this(null, a_gridconfig, a_clientClassName, a_WANMode, a_receiveOnly);
    m_gcmed = new DummyGridClientMediator(m_gridconfig);
  }

  public JGAPClientGP(IGridClientMediator a_gcmed,
                      GridNodeClientConfig a_gridconfig,
                      String a_clientClassName,
                      boolean a_WANMode,
                      boolean a_receiveOnly)
      throws Exception {
    m_WANMode = a_WANMode;
    m_receiveOnly = a_receiveOnly;
    m_gridconfig = a_gridconfig;
    Class client = Class.forName(a_clientClassName);
    m_gridConfig = (IGridConfigurationGP) client.getConstructor(new
        Class[] {}).newInstance(new Object[] {});
    m_gridConfig.initialize(m_gridconfig);
    if (m_gridConfig.getClientFeedback() == null) {
      m_gridConfig.setClientFeedback(new NullClientFeedbackGP());
    }
    // Setup work request.
    // -------------------
    JGAPRequestGP req = new JGAPRequestGP(m_gridconfig.getSessionName(), 0,
        m_gridConfig);
    req.setWorkerReturnStrategy(m_gridConfig.getWorkerReturnStrategy());
    req.setGenotypeInitializer(m_gridConfig.getGenotypeInitializer());
    req.setEvolveStrategy(m_gridConfig.getWorkerEvolveStrategy());
    // Evolution takes place on client only!
    // -------------------------------------
    req.setEvolveStrategy(null);
    setWorkRequest(req);
    m_gcmed = a_gcmed;
  }

  public void setWorkRequest(JGAPRequestGP a_request) {
    m_workReq = a_request;
  }

  /**
   * Called at start of run().
   * Override in sub classes if needed.
   *
   * @throws Exception
   */
  protected void onBeginOfRunning()
      throws Exception {
  }

  /**
   * Called in run() before sending work requests.
   * Override in sub classes if needed.
   *
   * @return true: do send work requests, false: don't send any work request
   * @throws Exception
   *
   * @throws Exception
   */
  protected boolean beforeSendWorkRequests() throws Exception {
    return true;
  }

  /**
   * Called in run() before one evolution step is executed.
   * Override in sub classes if needed.
   *
   * @throws Exception
   */
  protected void beforeEvolve()
      throws Exception {
  }

  /**
   * Called in run() after one evolution step is executed.
   * Override in sub classes if needed.
   *
   * @throws Exception
   */
  protected void afterEvolve()
      throws Exception {
  }

  /**
   * Called after stopping the client in run().
   * Override in sub classes if needed.
   *
   * @param a_t null if no error occured on stopping, otherwise exception object
   *
   * @throws Exception
   */
  protected void afterStopped(Throwable a_t)
      throws Exception {
  }

  /**
   * Called in run() in case of any unhandled error.
   * Override in sub classes if needed.
   *
   * @param a_ex exception object expressing the error
   */
  protected void onError(Exception a_ex) {
  }

  /**
   * Threaded: Splits work, sends it to workers and receives computed solutions.
   *
   * @author Klaus Meffert
   * @since 3.01
   */
  public void run() {
    try {
      onBeginOfRunning();
      // Start client.
      // -------------
      try {
        if (!m_receiveOnly) {
          // Initialize evolution.
          // ---------------------
          IClientEvolveStrategyGP clientEvolver = m_gridConfig.
              getClientEvolveStrategy();
          if (clientEvolver != null) {
            clientEvolver.initialize(m_gcmed, getConfiguration(),
                                     m_gridConfig.getClientFeedback());
          }
        }
        // Do the evolution.
        // -----------------
        beforeEvolve();
        evolve(m_gcmed, m_receiveOnly);
        afterEvolve();
      } finally {
        Throwable t = null;
        try {
          try {
            m_gcmed.stop();
          } catch (Throwable t1) {
            t = t1;
          }
        } finally {
          afterStopped(t);
        }
      }
    } catch (Exception ex) {
      m_gridConfig.getClientFeedback().error("Error while doing the work", ex);
      onError(ex);
    }
  }

  protected JGAPRequestGP[] sendWorkRequests(int evolutionIndex,
      IClientEvolveStrategyGP evolver, IRequestSplitStrategyGP splitter,
      IClientFeedbackGP feedback)
      throws Exception {
    if (beforeSendWorkRequests()) {
      JGAPRequestGP[] workRequests = null;
      log.warn("Beginning evolution cycle " + evolutionIndex);
//      m_clientEvolveStrategy.beforeGenerateWorkResults();
      workRequests = evolver.generateWorkRequests(
          m_workReq, splitter, null);
      feedback.setProgressMaximum(0);
      feedback.setProgressMaximum(workRequests.length - 1);
      sendWorkRequests(workRequests);
      return workRequests;
    }
      else {
        return null;
      }
  }

  protected void sendWorkRequests(JGAPRequestGP[] a_workList)
      throws Exception {
    // Send work requests.
    // -------------------
    for (int i = 0; i < a_workList.length; i++) {
      JGAPRequestGP req = a_workList[i];
      GPPopulation pop = req.getPopulation();
      if (pop == null || pop.isFirstEmpty()) {
        log.warn("Initial population to send to worker is empty!");
      }
      m_gridConfig.getClientFeedback().sendingFragmentRequest(req);
      MessageContext context = new MessageContext(MODULE_CS,
          CONTEXT_WORK_REQUEST, CONTEXT_ID_EMPTY);
      m_gcmed.send(new GridMessageWorkRequest(req), context,
                   null);
      if (isInterrupted()) {
        break;
      }
    }
  }

  protected void receiveWorkResults(JGAPRequestGP[] workList)
      throws Exception {
    log.info("Receiving work results...");
    IClientFeedbackGP feedback = m_gridConfig.getClientFeedback();
    // Receive work results.
    // ---------------------
    int size;
    if (workList == null) {
      size = -1;
    }
    else {
      size = workList.length;
    }
    if (m_WANMode && size < 1) {
      int i = 0;
      do {
        feedback.setProgressValue(i + workList.length);
        i++;
        JGAPResultGP result = receiveWorkResult(workList, feedback);
        if (result == null) {
          break;
        }
      } while (true);
    }
    else {
      for (int i = 0; i < size; i++) {
        feedback.setProgressValue(i + workList.length);
        receiveWorkResult(workList, feedback);
        if (this.isInterrupted()) {
          break;
        }
      }
    }
  }

  private JGAPResultGP receiveWorkResult(JGAPRequestGP[] workList,
      IClientFeedbackGP feedback)
      throws Exception {
    /**@todo make this asynchronous with fall-back and reconnect!*/
    int idx = -1;
    MessageContext context = new MessageContext(MODULE_WS /**@todo later: SC*/,
        CONTEXT_WORK_RESULT,
        CONTEXT_ID_EMPTY);
    GridMessageWorkResult gmwr = (GridMessageWorkResult) m_gcmed.
        getGridMessage(context, null, TIMEOUT_SECONDS, WAITTIME_SECONDS, true);
    if (gmwr == null) {
      throw new NoWorkResultsFoundException();
    }
    else {
      log.info("Work result received!");
    }
    JGAPResultGP workResult = (JGAPResultGP) gmwr.getWorkResult();
    m_gridConfig.getClientEvolveStrategy().resultReceived(workResult);
    idx = workResult.getRID();
    // Fire listener.
    // --------------
    feedback.receivedFragmentResult(workList[idx], workResult,
                                    idx);
    return workResult;
  }

  /**
   * If necessary: override to implement your evolution cycle individually.
   *
   * @param a_gcmed the GridClient mediator
   * @param a_receiveOnly false: Don't send any work requests, just receive
   * results from former evolutions
   *
   * @throws Exception
   */
  protected void evolve(IGridClientMediator a_gcmed, boolean a_receiveOnly)
      throws Exception {
    // Do the complete evolution cycle until end.
    // ------------------------------------------
    IClientFeedbackGP feedback = m_gridConfig.getClientFeedback();
    feedback.beginWork();
    IClientEvolveStrategyGP evolver = m_gridConfig.getClientEvolveStrategy();
    IRequestSplitStrategyGP splitter = m_gridConfig.getRequestSplitStrategy();
    int evolutionIndex = 0;
    do {
      JGAPRequestGP[] workRequests = null;
      if (!a_receiveOnly) {
        // Check if work requests already sent!
        // ------------------------------------
        try {
          workRequests = sendWorkRequests(evolutionIndex, evolver, splitter,
              feedback);
        } catch (UploadFailedException uex) {
          log.warn("Work request could no be sent, will be deferred.",uex);
          // Save work request for later resend.
          // -----------------------------------
          /**@todo impl.*/
        }
      }
      if (this.isInterrupted()) {
        break;
      }
      if (!a_receiveOnly) {
        evolver.afterWorkRequestsSent();
      }
      try {
        receiveWorkResults(workRequests);
      } catch (NoWorkResultsFoundException nwr) {
        log.info("No work results found.");
      }
      if (!a_receiveOnly) {
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

  public GPConfiguration getConfiguration() {
    return m_gridConfig.getConfiguration();
  }

  public IGridClientMediator getGridClientMediator() {
    return m_gcmed;
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
    if (args.length < 2) {
      System.out.println(
          "Please provide an application name of the grid (textual identifier");
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
      options.addOption("l", true, "LAN or WAN");
      options.addOption("receive_only", false,
                        "Only receive results, don't send requests");
      CommandLine cmd = MainCmd.parseCommonOptions(options, config, args);
      String networkMode = cmd.getOptionValue("l", "LAN");
      boolean inWAN;
      if (networkMode != null && networkMode.equals("LAN")) {
        inWAN = false;
      }
      else {
        inWAN = true;
      }
      boolean receiveOnly = cmd.hasOption("receive_only");
      // Setup and start client.
      // -----------------------
      JGAPClientGP client = new JGAPClientGP(config, args[0], inWAN,
          receiveOnly);
      // Start the threaded process.
      // ---------------------------
      client.start();
      client.join();
    } catch (Exception ex) {
      ex.printStackTrace();
      System.exit(1);
    }
  }

  protected IGridConfigurationGP getGridConfigurationGP() {
    return m_gridConfig;
  }
}
