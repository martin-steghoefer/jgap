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

import java.io.*;
import java.net.*;
import java.util.*;

import org.apache.commons.cli.*;
import org.homedns.dade.jcgrid.client.*;
import org.homedns.dade.jcgrid.cmd.*;
import org.homedns.dade.jcgrid.message.*;
import org.jgap.distr.*;
import org.jgap.distr.grid.*;
import org.jgap.distr.grid.common.*;
import org.jgap.gp.*;
import org.jgap.gp.impl.*;
import org.jgap.util.*;

import com.google.gdata.util.*;
import com.thoughtworks.xstream.*;
import rww.google.docs.*;

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
  private final static String CVS_REVISION = "$Revision: 1.10 $";

  public static final String MODULE_CS = "CS";

  public static final String CLIENT_DATABASE = "clientdb0.jgap";

  /**@todo das ist nicht module, sondern sender-receiver*/

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

//  private transient Logger log = Logger.getLogger(getClass());
  private static transient org.apache.log4j.Logger log
      = org.apache.log4j.Logger.getLogger(JGAPClientGP.class);

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

  private String m_workDir;

  private String m_runID;

  private boolean m_endless;

  private ClientStatus m_objects;

  private PersistableObject m_persister;

  private int m_requestIdx;

  public JGAPClientGP(GridNodeClientConfig a_gridconfig,
                      String a_clientClassName,
                      boolean a_WANMode,
                      boolean a_receiveOnly, boolean a_endless )
      throws Exception {
    this(null, a_gridconfig, a_clientClassName, a_WANMode, a_receiveOnly, a_endless);
    m_gcmed = new DummyGridClientMediator(m_gridconfig);
  }

  public JGAPClientGP(IGridClientMediator a_gcmed,
                      GridNodeClientConfig a_gridconfig,
                      String a_clientClassName, boolean a_WANMode,
                      boolean a_receiveOnly, boolean a_endless)
      throws Exception {
    m_runID = getRunID();
    log.info("ID of this run: " + m_runID);
    m_WANMode = a_WANMode;
    m_receiveOnly = a_receiveOnly;
    if (m_receiveOnly) {
      log.info("Only received results (as opted-in)");
    }
    m_endless = a_endless;
    if (m_endless) {
      log.info("Endless run (as opted-in).");
    }
    m_gridconfig = a_gridconfig;
    if (a_clientClassName == null || a_clientClassName.length() < 1) {
      throw new IllegalArgumentException(
          "Please specify a class name of the configuration!");
    }
    Class client = Class.forName(a_clientClassName);
    m_gridConfig = (IGridConfigurationGP) client.getConstructor(new
        Class[] {}).newInstance(new Object[] {});
    m_gridConfig.initialize(m_gridconfig);
    if (m_gridConfig.getClientFeedback() == null) {
      m_gridConfig.setClientFeedback(new NullClientFeedbackGP());
    }
    // Setup work request.
    // -------------------
    JGAPRequestGP req = new JGAPRequestGP(m_gridconfig.getSessionName(),
                                          m_runID + "_" + m_requestIdx,
                                          0, m_gridConfig);
    m_requestIdx++;
    req.setWorkerReturnStrategy(m_gridConfig.getWorkerReturnStrategy());
    req.setGenotypeInitializer(m_gridConfig.getGenotypeInitializer());
    req.setEvolveStrategy(m_gridConfig.getWorkerEvolveStrategy());
    MasterInfo requester = new MasterInfo(true);
    req.setRequesterInfo(requester);
    // Evolution takes place on client only!
    // -------------------------------------
    req.setEvolveStrategy(null);
    setWorkRequest(req);
    m_gcmed = a_gcmed;
    init();
  }

  private void init() throws Exception {
    String workDir = FileKit.getCurrentDir() + "/work/" + "storage";
    workDir = FileKit.getConformPath(workDir);
    // Try to load previous object information.
    // ----------------------------------------
    File f = new File(workDir, CLIENT_DATABASE);
    m_persister = new PersistableObject(f);
    m_objects = (ClientStatus) m_persister.load();
    if (m_objects == null) {
      m_objects = new ClientStatus();
      m_persister.setObject(m_objects);
    }
  }

  /**
   * @return the most possibly unique ID of a single program execution
   *
   * @author Klaus Meffert
   * @since 3.3.3
   */
  protected String getRunID() {
    if (m_runID == null) {
      return "RJGrid" + DateKit.getNowAsString();
    }
    else {
      return m_runID;
    }
  }

  public void setWorkRequest(JGAPRequestGP a_request) {
    m_workReq = a_request;
  }

  /**
   * Called at start of run().
   * Override in sub classes if needed.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.3.3
   */
  protected void onBeginOfRunning()
      throws Exception {
  }

  /**
   * Called in run() before sending work requests.
   * Override in sub classes if needed.
   *
   * @return true: do send work requests, false: don't send any work request
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.3.3
   */
  protected boolean beforeSendWorkRequests()
      throws Exception {
    return true;
  }

  /**
   * Called in run() after sending work requests successfully.
   * Override in sub classes if needed.
   *
   * @param a_workRequests the sent requests
   * @return true: process further, false: stop processing the rest
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.3.3
   */
  protected boolean afterSendWorkRequests(JGAPRequestGP[] a_workRequests)
      throws Exception {
    return true;
  }

  protected void errorOnSendWorkRequests(Throwable uex, JGAPRequestGP[] a_workRequests)
      throws Exception {
  }

  /**
   * Called in run() before one evolution step is executed.
   * Override in sub classes if needed.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.3.3
   */
  protected void beforeEvolve()
      throws Exception {
  }

  /**
   * Called in run() after one evolution step is executed.
   * Override in sub classes if needed.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.3.3
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
   *
   * @author Klaus Meffert
   * @since 3.3.3
   */
  protected void afterStopped(Throwable a_t)
      throws Exception {
  }

  /**
   * Called in run() in case of any unhandled error.
   * Override in sub classes if needed.
   *
   * @param a_ex exception object expressing the error
   *
   * @author Klaus Meffert
   * @since 3.3.3
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
    int errorConnect = 0;
    try {
      do {
        do {
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
                break;
              }
            }
          } catch (AuthenticationException aex) {
            // Could be because network cable plugged off during run...
            // --------------------------------------------------------
            errorConnect++;
            log.warn(
                "Authentication failed, possibly no connection? Retrying soon");
            sleep(10000);
          } catch (NoRouteToHostException nex) {
            errorConnect++;
            log.warn(
                "No route to host, possibly no connection? Retrying soon");
            sleep(10000);
          } catch (UnknownHostException aex) {
            // Could be because network cable was plugged off...
            // -------------------------------------------------
            errorConnect++;
            log.warn(
                "Unknown host, possibly no connection? Retrying soon");
            sleep(10000);
          } catch (ListingFailedException lex) {
            if (lex.getCause() != null &&
                lex.getCause().getClass().
                isAssignableFrom(AuthenticationException.class)) {
              errorConnect++;
              log.warn(
                  "Authentication failed, logging in again after a short break");
              sleep(10000);
              try {
                m_gcmed.connect();
              } catch (Exception ex) {
                log.error("Connect failed", ex);
                sleep(30000);
              }
            }
            else {
              log.info("Listing failed with cause "+lex.getCause(),lex);
              sleep(5000);
            }
          } catch (Exception ex) {
            log.fatal("Unpredicted error", ex);
            m_gridConfig.getClientFeedback().error("Error while doing the work",
                ex);
            onError(ex);
            try {
//              m_gcmed.disconnect();
            } catch (Exception ex1) {
              log.warn("Precautios disconnect failed.",ex1);
            }
            sleep(10000);
          }
        } while (true);
        if (!m_endless) {
            break;
        }
        else {
          log.info("Starting again after a short break...");
          sleep(15000);
        }
      } while (true);
    } catch (InterruptedException iex) {
      // Thread interrupted.
      // -------------------
      log.fatal("Thread was interrupted", iex);
    }
  }

  protected JGAPRequestGP[] sendWorkRequests(int a_evolutionIndex,
      IClientEvolveStrategyGP evolver, IRequestSplitStrategyGP splitter,
      IClientFeedbackGP feedback)
      throws Exception {
    if (beforeSendWorkRequests()) {
      JGAPRequestGP[] workRequests = null;
      log.info("Beginning evolution cycle " + a_evolutionIndex);
//      m_clientEvolveStrategy.beforeGenerateWorkResults();
      workRequests = evolver.generateWorkRequests(m_workReq, splitter, null);
      feedback.setProgressMaximum(0);
      feedback.setProgressMaximum(workRequests.length - 1);
      try {
        sendWorkRequests(workRequests);
        return workRequests;
      } catch (Exception ex) {
        throw new WorkRequestsSendException(ex, workRequests);
      }
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
        log.debug("Initial population to send to worker is empty!");
      }
      m_gridConfig.getClientFeedback().sendingFragmentRequest(req);
      MessageContext context = new MessageContext(MODULE_CS,
          CONTEXT_WORK_REQUEST, CONTEXT_ID_EMPTY);
      m_gcmed.send(new GridMessageWorkRequest(req), context, null);
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
      // First, get a list of all work results.
      // --------------------------------------
      MessageContext context = new MessageContext(MODULE_WS,
          /**@todo later: SC*/
          CONTEXT_WORK_RESULT, CONTEXT_ID_EMPTY);
      List requests = m_gcmed.listResults(context, null, null);
      // Then, iterate over them and receive one after another.
      // ------------------------------------------------------
      int i = 0;
      for (Object request : requests) {
        feedback.setProgressValue(i);
        i++;
        JGAPResultGP result = receiveWorkResult(request, feedback, true);
        if (result != null) {
          IGPProgram best = result.getPopulation().determineFittestProgram();
          log.info("Result received: " +
                   best.getFitnessValue());
          MasterInfo worker = result.getWorkerInfo();
          if (worker != null) {
            log.info(" Worker IP "+worker.m_IPAddress+", host "+worker.m_name);
          }
          // Store result to disk.
          // ---------------------
          String filename = "result_" + getRunID() + "_" + result.getID() +
              "_" +
              result.getSessionName() + "_" + result.getChunk();
          writeToFile(best, m_workDir, filename);
          // Now remove the result from the online store.
          // --------------------------------------------
          /**@todo do this here explicitely and not in receiveWorkResult*/
        }
      }
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

  private JGAPResultGP receiveWorkResult(Object a_result,
      IClientFeedbackGP feedback, boolean a_remove)
      throws Exception {
    MessageContext context = new MessageContext(MODULE_WS /**@todo later: SC*/,
        CONTEXT_WORK_RESULT, a_result);
    GridMessageWorkResult gmwr = (GridMessageWorkResult) m_gcmed.
        getGridMessage(context, null, TIMEOUT_SECONDS, WAITTIME_SECONDS,
                       a_remove);
    if (gmwr == null) {
      throw new WorkResultNotFoundException();
    }
    else {
      log.info("Work result received!");
    }
    JGAPResultGP workResult = (JGAPResultGP) gmwr.getWorkResult();
    m_gridConfig.getClientEvolveStrategy().resultReceived(workResult);
    int idx = workResult.getChunk();
    // Fire listener.
    // --------------
    feedback.receivedFragmentResult(null, workResult, idx);
    return workResult;
  }

  private JGAPResultGP receiveWorkResult(JGAPRequestGP[] workList,
      IClientFeedbackGP feedback)
      throws Exception {
    /**@todo make this asynchronous with fall-back and reconnect!*/
    MessageContext context = new MessageContext(MODULE_WS /**@todo later: SC*/,
        CONTEXT_WORK_RESULT, CONTEXT_ID_EMPTY);
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
    int idx = workResult.getChunk();
    // Fire listener.
    // --------------
    JGAPRequestGP req;
    if (workList == null || workList.length < 1) {
      req = null;
    }
    else {
      req = workList[idx];
    }
    feedback.receivedFragmentResult(req, workResult, idx);
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
      boolean deferRequests = false;
      if (!a_receiveOnly) {
        try {
          // Care that not too much work requests are online, do a listing
          // from time to time. If enough requests already there, don't create
          // them any more.
          long lastListing = m_objects.getLastListingRequestsMillis();
          long current = System.currentTimeMillis();
          if (current - lastListing > 60*60*2) {
            // Do a listing again after 2 hours or more break.
            // -----------------------------------------------
            MessageContext context = new MessageContext(MODULE_CS,
                CONTEXT_WORK_REQUEST, CONTEXT_ID_EMPTY);
            List requests = m_gcmed.listRequests(context, null, null);
            m_objects.setLastListingRequestsMillis(current);
            m_persister.save();
            if (requests != null && requests.size() > 100) {
              deferRequests = true;
              log.info("Deferring creating and sending further requests"
                       + ", maximum reached ("
                       + requests.size() + " found).");
            }
          }
          if (!deferRequests) {
            workRequests = sendWorkRequests(evolutionIndex, evolver, splitter,
                feedback);
          }
          else {
            // Defer creating and sending additional requests.
            // -----------------------------------------------
          }
        } catch (WorkRequestsSendException wex) {
          errorOnSendWorkRequests(wex.getCause(), wex.getWorkRequests());
//        } catch (UploadFailedException uex) {
//          errorOnSendWorkRequests(uex, null);
//          throw uex;
//        } catch (ListingFailedException lex) {
//          errorOnSendWorkRequests(lex, null);
//          throw lex;
        }
        if (!deferRequests && !afterSendWorkRequests(workRequests)) {
          break;
        }
      }
      if (this.isInterrupted()) {
        break;
      }
      if (!deferRequests && !a_receiveOnly) {
        evolver.afterWorkRequestsSent();
      }
      try {
        receiveWorkResults(workRequests);
      } catch (NoWorkResultsFoundException nwr) {
        log.info("No work results found.");
      } catch (ReadFailedException rfe) {
        log.warn("Reading work results failed.");
      } catch (DeleteFailedException dex) {
        log.warn("Deletion of received request failed.");
        /**@todo defer delete*/
      } catch (ListingFailedException lex) {
        log.warn("Listing failed.");
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
    try {
      m_gcmed.disconnect();
    } catch (Exception ex) {
      log.error("Disconnecting from server failed!", ex);
    }
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
          receiveOnly, false);
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

  /**
   * Writes an object to a local file.
   *
   * @param a_obj the object to persist
   * @param a_dir directory to write the file to
   * @param a_filename name of the file to create
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.3.3
   */
  public void writeToFile(Object a_obj, String a_dir, String a_filename)
      throws Exception {
    XStream xstream = new XStream();
    File f = new File(a_dir, a_filename);
    FileOutputStream fos = new FileOutputStream(f);
    xstream.toXML(a_obj, fos);
    fos.close();
  }

  public void setWorkDirectory(String a_workDir)
      throws IOException {
    m_workDir = a_workDir;
    FileKit.createDirectory(m_workDir);
    log.info("Work dir: " + m_workDir);
  }

  public String getWorkdirectory() {
    return m_workDir;
  }
}
