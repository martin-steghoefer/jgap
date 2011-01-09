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
import org.jgap.*;
import org.jgap.distr.*;
import org.jgap.distr.grid.*;
import org.jgap.distr.grid.common.*;
import org.jgap.distr.grid.util.*;
import org.jgap.distr.grid.wan.*;
import org.jgap.gp.*;
import org.jgap.gp.impl.*;
import org.jgap.util.*;

import com.thoughtworks.xstream.io.xml.*;

/**
 * A client defines work for the grid and sends it to the JGAPServer.
 * Use this class as base class for your grid client implementations.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public class JGAPClientGP
    extends Thread {
  /**@todo in dateiname requester/worker kodieren*/
  /**@todo auch schlechtere ergebnisse einmixen: die direkt empfangenen
   * gleich wieder in einen request reinstecken --> aber mischen verschiedener
   * results in einen request!*/

  /**@todo small, medium, large work requests*/
  /**@todo re-evaluate each result on behalf of another worker: keep separate
   * lookup-table for all requests --> m_resultsVerified, m_resultsPersister */
  /**@todo remove old requests from online store automatically*/
  /**@todo info when work request has been taken*/
  /**@todo info when worker logs on --> evaluate logon files*/
  /**@todo top results in eigener datei speichern,
   * komprimierung durch weglassen überfl. infos, siehe xml --> injection after reload*/
  /**@todo copy good results to online folder*/

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.21 $";

  public static final String APP_VERSION = "1.02a";

  /**@todo store version in external file*/

  public static final String MODULE_CS = "CS";

  public static final String CLIENT_DATABASE = "clientdbGP.jgap";

  public static final String RESULTS_DATABASE = "results.jgap";

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

  public static final Object[][] FIELDSTOSKIP = new Object[][] { {GPPopulation.class,
      "m_fitnessRank"}, {GPPopulation.class, "m_fittestProgram"}, {GPPopulation.class,
      "m_changed"}, {GPPopulation.class, "m_sorted"}, {GPPopulation.class,
      "m_fittestToAdd"}, {BaseGPChromosome.class,
      "m_ind"},
  };

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

  private boolean m_list;

  private String m_workDir;

  /**
   * Dir for not too bad results
   */
  private String m_ntbResultsDir;

  private String m_runID;

  private boolean m_endless;

  private ClientStatus m_objects;

  private PersistableObject m_persister;

  private ResultVerification m_resultsVerified;

  private PersistableObject m_resultsPersister;

  private int m_requestIdx;

  private boolean m_no_comm;

  private boolean m_no_evolution;

  private int m_max_fetch_results;

  public JGAPClientGP(GridNodeClientConfig a_gridconfig,
                      String a_clientClassName,
                      boolean a_WANMode,
                      boolean a_receiveOnly, boolean a_list, boolean a_no_comm,
                      boolean a_no_evolution, boolean a_endless,
                      int a_max_fetch_results)
      throws Exception {
    this(null, a_gridconfig, a_clientClassName, a_WANMode, a_receiveOnly,
         a_list, a_no_comm, a_no_evolution, a_endless, a_max_fetch_results);
    m_gcmed = new DummyGridClientMediator(m_gridconfig);
  }

  public JGAPClientGP(IGridClientMediator a_gcmed,
                      GridNodeClientConfig a_gridconfig,
                      String a_clientClassName, boolean a_WANMode,
                      boolean a_receiveOnly, boolean a_list, boolean a_no_comm,
                      boolean a_no_evolution, boolean a_endless,
                      int a_max_fetch_results)
      throws Exception {
    log.info("This is JGAP Grid version " + APP_VERSION);
    m_runID = getRunID();
    log.info("ID of this run: " + m_runID);
    if (a_clientClassName == null || a_clientClassName.length() < 1) {
      throw new IllegalArgumentException(
          "Please specify a class name of the configuration!");
    }
    m_WANMode = a_WANMode;
    m_receiveOnly = a_receiveOnly;
    if (m_receiveOnly) {
      log.info("Only receive results");
    }
    m_list = a_list;
    if (m_list) {
      log.info("List requests and results");
    }
    m_endless = a_endless;
    if (m_endless) {
      log.info("Endless run");
    }
    m_no_comm = a_no_comm;
    if (m_no_comm) {
      log.info("Don't send or receive anything");
    }
    m_no_evolution = a_no_evolution;
    if (m_no_evolution) {
      log.info("Don't execute genetic evolution");
    }
    m_max_fetch_results = a_max_fetch_results;
    if (m_max_fetch_results <= 0) {
      m_max_fetch_results = 500;
    }
    log.info("Maximum number of results to fetch at once: " +
             m_max_fetch_results);
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
    /**@todo ab 2. zyklus ist pop.grösse nur 1 !*/
    JGAPRequestGP req = new JGAPRequestGP(m_gridconfig.getSessionName(),
                                          m_runID + "_" + m_requestIdx, 0,
                                          m_gridConfig);
    m_requestIdx++;
    req.setWorkerReturnStrategy(m_gridConfig.getWorkerReturnStrategy());
    req.setGenotypeInitializer(m_gridConfig.getGenotypeInitializer());
    req.setEvolveStrategy(m_gridConfig.getWorkerEvolveStrategy());
    MasterInfo requester = new MasterInfo(true);
    req.setRequesterInfo(requester);
    req.setRequestDate(DateKit.now());
    // If evolution takes place on client only:
    // ----------------------------------------
//    req.setEvolveStrategy(null);
    //
    setWorkRequest(req);
    m_gcmed = a_gcmed;
    init();
  }

  private void init()
      throws Exception {
    if (getWorkDirectory() == null) {
      String workDir = FileKit.getCurrentDir() + "/work/" + "storage";
      workDir = FileKit.getConformPath(workDir);
      setWorkDirectory(workDir);
    }
    m_ntbResultsDir = FileKit.addSubDir(getWorkDirectory(), "ntb", true);
    FileKit.createDirectory(m_ntbResultsDir);
    log.info("NTB dir: " + m_ntbResultsDir);
//    m_workDirResults =FileKit.getCurrentDir() + "/work/" + "results";
//    m_workDirResults = FileKit.getConformPath(workDir);
    // Try to load previous object information.
    // ----------------------------------------
    File f = new File(getWorkDirectory(), CLIENT_DATABASE);
    m_persister = new PersistableObject(f);
    m_objects = (ClientStatus) m_persister.load();
    if (m_objects == null) {
      m_objects = new ClientStatus();
      m_persister.setObject(m_objects);
    }
    // Try to load previous request information.
    // -----------------------------------------
    f = new File(getWorkDirectory(), RESULTS_DATABASE);
    m_resultsPersister = new PersistableObject(f);
    m_resultsVerified = (ResultVerification) m_resultsPersister.load();
    if (m_resultsVerified == null) {
      m_resultsVerified = new ResultVerification();
      m_resultsPersister.setObject(m_resultsVerified);
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
   * @param a_workRequests work requests pending to be sent
   *
   * @return true: do send work requests, false: don't send any work request
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.3.3
   */
  protected boolean beforeSendWorkRequests(JGAPRequestGP[] a_workRequests)
      throws Exception {
    return true;
  }

  /**
   * Called in run() before generating work requests for sending.
   * Override in sub classes if needed.
   *
   * @return true: do generate work requests, false: don't generate any work
   * request
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.3.3
   */
  protected boolean beforeGenerateWorkRequests()
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

  protected void errorOnSendWorkRequests(Throwable uex,
      JGAPRequestGP[] a_workRequests)
      throws Exception {
  }

  /**
   * Called in run() before one evolution step is executed.
   * Override in sub classes if needed.
   *
   * @param a_gcmed the GridClient mediator
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.3.3
   */
  protected void beforeEvolve(IGridClientMediator a_gcmed)
      throws Exception {
  }

  /**
   * Called in run() after one evolution step is executed.
   * Override in sub classes if needed.
   *
   * @param a_gcmed the GridClient mediator
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.3.3
   */
  protected void afterEvolve(IGridClientMediator a_gcmed)
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
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.3.3
   */
  protected void onError(Exception a_ex)
      throws Exception {
    // Do not handle any eror specifically.
    // ------------------------------------
    throw a_ex;
  }

  /**
   * Called in case of any unhandled error when trying to delete a request or
   * result.
   * Override in sub classes if needed.
   *
   * @param a_ex exception object expressing the error
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.3.4
   */
  protected void onDeleteError(Exception a_ex)
      throws Exception {
    // Do not handle any eror specifically.
    // ------------------------------------
    throw a_ex;
  }

  /**
   * Called in run() on error when receiving work results.
   * Override in sub classes if needed.
   *
   * @param a_workRequests for which to receive results
   * @param a_ex Exception occured
   * @throws Exception rethrow an unhandled exception!
   *
   * @author Klaus Meffert
   * @since 3.3.4
   */
  protected void onErrorReceiveWorkResults(JGAPRequestGP[] a_workRequests,
      Exception a_ex)
      throws Exception {
    // Do not handle any eror specifically.
    // ------------------------------------
    throw a_ex;
  }

  /**
   * Threaded: Splits work, sends it to workers and receives computed solutions.
   *
   * @author Klaus Meffert
   * @since 3.01
   */
  public void run() {
    try {
      try {
        // Check for updates.
        // ------------------
        String libDir = "D:\\jgap\robocode\\rjgrid\\lib\\";
//        checkForUpdates("http://www.klaus-meffert.de/", libDir, m_workDir);
      } catch (Exception ex) {
        log.error("Check for updates failed", ex);
      }
      do {
        boolean showResultsError = false;
        do {
          boolean doBreak = false;
          try {
            onBeginOfRunning();
            if (!showResultsError) {
              // Show stats about best results for current application.
              // ------------------------------------------------------
              try {
                showCurrentResults();
              } catch (Exception ex) {
                log.error("Error during showing current results", ex);
                showResultsError = true;
              }
            }
            // Do deferred deletion of results.
            // --------------------------------
            Iterator<String> it = m_objects.getResults().keySet().iterator();
            boolean modified = false;
            try {
              while (it.hasNext()) {
                String key = it.next();
                String value = (String) m_objects.getResults().get(key);
                if (startsWith(value, "delete:")) {
                  log.info("Delete result (deferred), key: " + key);
                  try {
                    m_gcmed.removeMessage(key);
                  } catch (MalformedURLException mex) {
                    log.warn("Invalid key", mex);
                  } catch (Exception ex) {
                    onDeleteError(ex);
                    continue;
                  }
                  it.remove();
//                  m_objects.getResults().remove(key);
                  modified = true;
                }
                else {
                  if (startsWith(value, "delete")) {
                    it.remove();
                    modified = true;
                  }
                }
              }
            } finally {
              if (modified) {
                m_persister.save();
                modified = false;
              }
            }
            try {
              try {
                if (m_list) {
                  // List existing requests and results with extended information.
                  // -------------------------------------------------------------
                  listRequests();
                  listResults();
                }
                if (!m_receiveOnly && !m_no_evolution) {
                  // Initialize evolution.
                  // ---------------------
                  IClientEvolveStrategyGP clientEvolver = m_gridConfig.
                      getClientEvolveStrategy();
                  if (clientEvolver != null) {
                    clientEvolver.initialize(m_gcmed, getConfiguration(),
                        m_gridConfig.getClientFeedback());
                  }
                }
                if (!m_no_evolution) {
                  // Do the evolution.
                  // -----------------
                  beforeEvolve(m_gcmed);
                  evolve(m_gcmed, m_receiveOnly);
                  afterEvolve(m_gcmed);
                }
                doBreak = true;
              } catch (Exception ex) {
                log.error("Error: ", ex);
                throw ex;
              }
            } finally {
              Throwable t = null;
              try {
                try {
                  m_gcmed.stop();
                } catch (Throwable t1) {
                  t = t1;
                }
              } finally {
                log.info("Calling afterStopped");
                afterStopped(t);
                if (doBreak) {
                  break;
                }
              }
            }
          } catch (Exception ex1) {
            try {
              log.info("before onError");
              onError(ex1);
            } catch (Exception ex) {
              log.fatal("Unpredicted error", ex);
              m_gridConfig.getClientFeedback().error(
                  "Error while doing the work",
                  ex);
              try {
//              m_gcmed.disconnect();
              } catch (Exception ex2) {
                log.warn("Precautios disconnect failed.", ex2);
              }
              sleep(10000);
            }
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
      try {
        m_gcmed.disconnect();
      } catch (Exception ex) {
        log.warn("Disconnect after interruption failed", ex);
      }
    } catch (Throwable t) {
      t.printStackTrace();
    }
    log.info("Stopping client");
  }

  protected JGAPRequestGP[] sendWorkRequests(int a_evolutionIndex,
      IClientEvolveStrategyGP evolver, IRequestSplitStrategyGP splitter,
      IClientFeedbackGP feedback)
      throws Exception {
    JGAPRequestGP[] workRequests = null;
    if (beforeGenerateWorkRequests()) {
      log.info("Beginning evolution cycle " + a_evolutionIndex);
      try {
//      m_clientEvolveStrategy.beforeGenerateWorkResults();
        workRequests = evolver.generateWorkRequests(m_workReq, splitter, null);
        feedback.setProgressMaximum(0);
        feedback.setProgressMaximum(workRequests.length - 1);
        for (int i = 0; i < workRequests.length; i++) {
          log.info("Setting up work request " + i);
          presetPopulation(workRequests[i]);
        }
        if (beforeSendWorkRequests(workRequests)) {
          /**@todo merge previous results in req.getPopulation()*/
          if (!m_no_comm) {
            try {
              sendWorkRequests(workRequests);
              return workRequests;
            } catch (Exception ex) {
              throw new WorkRequestsSendException(ex, workRequests);
            }
          }
          else {
            return workRequests;
          }
        }
        else {
          return null;
        }
      } catch (Exception ex) {
        ex.printStackTrace();
        throw ex;
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
      req.setRequestDate(DateKit.now());
      GPPopulation pop = req.getPopulation();
      if (pop == null || pop.isFirstEmpty()) {
        log.debug("Population to send to worker is empty!");
      }
      else {
        GPGenotype.checkErroneousPop(pop, " before sending to worker", true);
        /**@todo hier ist fehler aufgetreten!*/
      }
      m_gridConfig.getClientFeedback().sendingFragmentRequest(req);
      MessageContext context = new MessageContext(MODULE_CS,
          CONTEXT_WORK_REQUEST, CONTEXT_ID_EMPTY);
      context.setVersion(APP_VERSION);
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
    if (m_WANMode) {
      // First, get a list of all work results.
      // --------------------------------------
      MessageContext context = new MessageContext(MODULE_WS,
          /**@todo later: SC*/
          CONTEXT_WORK_RESULT, CONTEXT_ID_EMPTY);
      List results = m_gcmed.listResults(context, null, null);
      // Then, iterate over them and receive one after another.
      // ------------------------------------------------------
      if (results == null || results.size() < 1) {
        log.info("No earlier results found.");
      }
      else {
        int i = 0;
        int len = results.size();
        log.info(len + " results found.");
        if (len > getMaxFetchResults()) {
          len = getMaxFetchResults();
          log.info("Fetching only " + len + " results.");
        }
        /**@todo sort results according to post date, the oldest first*/
        for (Object resultStub : results) {
          if (i >= m_max_fetch_results) {
            break;
          }
          feedback.setProgressValue(i);
          JGAPResultGP result = receiveWorkResult(resultStub, feedback, false);
          if (result != null) {
            log.info(" Generic data: " + result.getGenericData());
            /**@todo config.params wie popsite, evol.anz dazu*/
            log.info(" Title: " + result.getTitle());
            IGPProgram best = result.getPopulation().determineFittestProgram();
            String key = result.getID();
            // Check if result already received, and if, skip it
            if (m_objects.getResults().get(key) != null) {
              log.info("Already received result detected, key: "+key);
              continue;
            }
            if (best == null) {
              log.info("Empty result received!");
            }
            m_objects.getResults().put(key, "received");
            // Work with the result.
            // ---------------------
            m_gridConfig.getClientEvolveStrategy().resultReceived(result);
            try {
              // Remove result from online store.
              // ---------------------------------
              try {
                log.info("Removing result from online store");
                if (false && result.getGenericData() != null &&
                    WANData.class.isAssignableFrom(result.getGenericData().
                    getClass())) {
                  WANData wanData = (WANData) result.getGenericData();
                  m_gcmed.removeMessage(wanData.getUri());
                }
                else {
                  m_gcmed.removeMessage(resultStub);
                }
              } catch (Exception ex) {
                log.warn("Deletion of result failed, deferring...", ex);
                key = getKeyFromObject(resultStub);
                if (key != null) {
                  log.info(" Key for later deletion: " + key);
                  m_objects.getResults().put(key, "delete:");
                }
                else {
                  log.info("Deferred deletion not possible: key unknown");
                }
              }
            } finally {
              m_persister.save();
            }
            i++;
            resultReceived(best);
            MasterInfo worker = result.getWorkerInfo();
            if (worker != null) {
              log.info(" Worker IP " + worker.m_IPAddress + ", host " +
                       worker.m_name);
            }
            // Store result to disk if it is fit enough.
            // -----------------------------------------
            double minFitness;
            minFitness = m_gridConfig.getMinFitnessToStore();
            if (minFitness < 0.0001d) {/**@todo allow fitness 0.0*/
              minFitness = 5000;
            }
            if (best != null && best.getFitnessValue() >= minFitness) {
              String filename = getResultFilename(result);
              log.info("Writing result to file " + filename);
              writeToFile(best, m_workDir, filename);
            }
            // Now remove the result from the online store.
            // --------------------------------------------
            /**@todo do this here explicitely and not in receiveWorkResult*/
          }
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
        if(m_gridConfig.getClientEvolveStrategy().isEvolutionFinished(0)) {
          break;
        }
      }
    }
  }

  protected String getResultFilename(JGAPResultGP a_result) {
    IGPProgram fittest = WANUtils.getFittest(a_result);
    String fitness = "";
    if (fittest == null) {
      // Should not happen at all!
      log.error("No fittest program found!");
    }
    else {
      fitness = NumberKit.niceDecimalNumber(fittest.getFitnessValue(), 2);
    }
    return "result_"
        + fitness
        + "_"
        + getRunID()
        + "_"
        + a_result.getID()
        + "_"
        + a_result.getSessionName()
        + "_" + a_result.getChunk()
        + ".jgap";
  }

  private JGAPResultGP receiveWorkResult(Object a_result,
      IClientFeedbackGP feedback, boolean a_remove)
      throws Exception {
    // Object reference is realized via context id.
    // --------------------------------------------
    MessageContext context = new MessageContext(MODULE_WS /**@todo later: SC*/,
        CONTEXT_WORK_RESULT, a_result);
    GridMessageWorkResult gmwr = (GridMessageWorkResult) m_gcmed.
        getGridMessage(context, null, TIMEOUT_SECONDS, WAITTIME_SECONDS,
                       a_remove);
    if (gmwr == null) {
      throw new WorkResultNotFoundException();
    }
    else {
      String s = " ";
      if (a_remove) {
        s += "and removed from WAN";
      }
      log.info("Work result received" + s);
    }
    JGAPResultGP workResult = (JGAPResultGP) gmwr.getWorkResult();
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
    IGPProgram best = workResult.getFittest();
    if (best == null) {
      best = workResult.getPopulation().determineFittestProgram();
    }
    resultReceived(best);
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
          // -----------------------------------------------------------------
          long lastListing = m_objects.getLastListingRequestsMillis();
          long current = System.currentTimeMillis();
          if (current - lastListing > 60 * 60 * 1) { //60 Seconds * 60 Minutes * 1 Hour
            // Do a listing again after 60 minutes or more.
            // --------------------------------------------
            MessageContext context = new MessageContext(MODULE_CS,
                CONTEXT_WORK_REQUEST, CONTEXT_ID_EMPTY);
            List requests = a_gcmed.listRequests(context, null, null);
            m_objects.setLastListingRequestsMillis(current);
            m_persister.save();
            if (requests != null && requests.size() > 100) {
              deferRequests = true;
              log.info("Deferring creating and sending further requests"
                       + ", maximum reached ("
                       + requests.size() + " found).");
            }
            if (requests != null && requests.size() > 0) {
              // Remove requests from database that are not in list any more.
              // ------------------------------------------------------------
              Map foundKeys = new HashMap();
              Object first = requests.get(0);
              if (String.class.isAssignableFrom(first.getClass())) {
                // Requests of type String can be handled directly.
                // ------------------------------------------------
                for (Object key : requests) {
                  foundKeys.put(key, "");
                }
              }
              else {
                // Requests of type that sub classes have to handle.
                // -------------------------------------------------
                for (Object obj : requests) {
                  Object key = getKeyFromObject(obj);
                  if (key != null) {
                    foundKeys.put(key, "");
                  }
                }
              }
              removeEntries(foundKeys, m_objects.getRequests());
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
      if (!m_no_comm) {
        try {
          receiveWorkResults(workRequests);
        } catch (Exception ex) {
          onErrorReceiveWorkResults(workRequests, ex);
        }
      }
      if (!a_receiveOnly && !m_no_evolution) {
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
      else {
        a_gcmed.disconnect();
        log.info("Sleeping a while before beginning again...");
        Thread.sleep(40000);
        a_gcmed.connect();
      }
    } while (true);
    try {
      a_gcmed.disconnect();
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
    JGAPGPXStream xstream = new JGAPGPXStream();
    File f = new File(a_dir, a_filename);
//    FileOutputStream fos = new FileOutputStream(f);
    FileWriter fw = new FileWriter(f);
    CompactWriter compact = new CompactWriter(fw);
    xstream.marshal(a_obj, compact);
    fw.close();
  }

  public void setWorkDirectory(String a_workDir)
      throws IOException {
    m_workDir = a_workDir;
    FileKit.createDirectory(m_workDir);
    log.info("Work dir: " + m_workDir);
  }

  public String getWorkDirectory() {
    return m_workDir;
  }

  protected void checkForUpdates(String a_URL, String a_libDir,
                                 String a_workDir)
      throws Exception {
    GridKit.updateModuleLibrary(a_URL, "rjgrid", a_libDir, a_workDir);
  }

  /**
   * Override in sub classes: list available requests
   */
  protected void listRequests() {
  }

  /**
   * Override in sub classes: list available results
   *
   * @author Klaus Meffert
   * @since 3.3.3
   */
  protected void listResults() {
  }

  /**
   * @return false: normal mode, true: do not communicate with the server
   *
   * @author Klaus Meffert
   * @since 3.3.3
   */
  public boolean isNoCommunication() {
    return m_no_comm;
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
    try {
      // Setup logging.
      // --------------
      MainCmd.setUpLog4J("client", true);
      // Command line options.
      // ---------------------
      GridNodeClientConfig config = new GridNodeClientConfig();
      Options options = makeOptions();
      CommandLine cmd = MainCmd.parseCommonOptions(options, config, args);
      SystemKit.printHelp(cmd, options);
      String networkMode = cmd.getOptionValue("l", "LAN");
      boolean inWAN;
      if (networkMode != null && networkMode.equals("LAN")) {
        inWAN = false;
      }
      else {
        inWAN = true;
      }
      if (!cmd.hasOption("config")) {
        System.out.println(
            "Please provide a name of the grid configuration class to use");
        System.out.println("An example class would be "
                           +
                           "examples.grid.fitnessDistributed.GridConfiguration");
        System.exit(1);
      }
//      if (args.length < 2) {
//        System.out.println(
//            "Please provide an application name of the grid (textual identifier");
//        System.exit(1);
//      }
      String clientClassName = cmd.getOptionValue("config");
      boolean receiveOnly = cmd.hasOption("receive_only");
      boolean list = cmd.hasOption("list");
      boolean noComm = cmd.hasOption("no_comm");
      boolean noEvolution = cmd.hasOption("no_evolution");
      boolean endless = cmd.hasOption("endless");
      int max_fetch_results = Integer.valueOf(cmd.getOptionValue(
          "max_fetch_results", "0"));
      // Setup and start client.
      // -----------------------
      JGAPClientGP client = new JGAPClientGP(config, clientClassName, inWAN,
          receiveOnly, list, noComm, noEvolution, endless, max_fetch_results);
      // Start the threaded process.
      // ---------------------------
      client.start();
      client.join();
    } catch (Exception ex) {
      ex.printStackTrace();
      System.exit(1);
    }
  }

  protected static Options makeOptions() {
    Options options = new Options();
    options.addOption("l", true, "LAN or WAN");
    options.addOption("no_comm", false,
                      "Don't receive any results, don't send requests");
    options.addOption("no_evolution", false,
                      "Don't perform genetic evolution");
    options.addOption("receive_only", false,
                      "Only receive results, don't send requests");
    options.addOption("endless", false, "Run endlessly");
    options.addOption("config", true, "Grid configuration's class name");
    options.addOption("list", false,
                      "List requests and results");
    options.addOption("max_fetch_results", true,
                      "Maximum number of results to fetch at once");
    options.addOption("help", false,
                      "Display all available options");
    return options;
  }

  protected void removeEntries(Map a_cachedKeys, Map a_foundKeys) {
    Iterator it = a_cachedKeys.keySet().iterator();
    while (it.hasNext()) {
      Object key = it.next();
      if (!a_foundKeys.containsKey(key)) {
        it.remove();
      }
    }
  }

  /**
   * Override in sub classes.
   *
   * @param a_obj the object to get the key from
   * @return the key of the object
   *
   * @throws Exception
   */
  protected String getKeyFromObject(Object a_obj)
      throws Exception {
    return null;
  }

  /**
   * New results has been received. Care that the best of them are stored
   * in case it is a top 3 result.
   *
   * @param a_pop the fittest results received for a work request
   * @return true: new top result
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.3.3
   */
  protected boolean resultReceived(GPPopulation a_pop)
      throws Exception {
    boolean isTopResult = false;
    for (IGPProgram prog : a_pop.getGPPrograms()) {
      if (prog != null) {
        if (resultReceived(prog)) {
          isTopResult = true;
        }
      }
    }
    return isTopResult;
  }

  /**
   * A new result has been received. Care that it is stored to top list on disk
   * in case it is a top 3 result. Also store it in other cases and if the
   * result is not too bad to be able to mix it in when generating new work
   * requests.
   *
   * @param a_fittest the fittest result received for a work request
   *
   * @return true: new top result
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.3.3
   */
  protected boolean resultReceived(IGPProgram a_fittest)
      throws Exception {
    if (a_fittest == null) {
      return false;
    }
    /**@todo jeden Worker einer von n (rein logischen) Gruppen zuteilen.
     * Pro logischer Gruppe top n Ergebnisse halten
     */
    try {
      Map<String, List> topAll = m_objects.getTopResults();
      String appid = m_gridConfig.getContext().getAppId();
      List<IGPProgram> topApp = topAll.get(appid);
      if (topApp == null) {
        topApp = new Vector();
        topAll.put(appid, topApp);
      }
      int fitter = 0;
      Iterator<IGPProgram> it = topApp.iterator();
      Object worstEntry = null;
      double worstFitness = -1;
      String norm = a_fittest.toStringNorm(0);
      int count = 0;
      double a_fitness = a_fittest.getFitnessValue();
      if (a_fitness > 12500) {
        // Store online as a backup.
        // -------------------------
        log.info("Backup up good result");
        String title = "fitness_" + NumberKit.niceDecimalNumber(a_fitness, 2);
        m_gcmed.backupResult(a_fittest, "goodResults", title);
      }
      else {
        if (a_fitness > 1750) {
          // Store not too bad result for mixing it in to new work requests.
          // ---------------------------------------------------------------
          log.info("Storing not too bad result for later reusage");
          String title = "ntb_fitness_"
              + DateKit.getNowAsString()
              + "_"
              + NumberKit.niceDecimalNumber(a_fitness, 2)
              + ".jgap";
          // Store in separate subdir.
          // -------------------------
          saveResult(m_ntbResultsDir, title, a_fittest);
        }
      } while (it.hasNext()) {
        IGPProgram prog = (IGPProgram) it.next();
        // Don't allow identical results.
        // ------------------------------
        if (prog.toStringNorm(0).equals(norm)) {
          fitter = 100;
          break;
        }
        double fitness = prog.getFitnessValue();
        if (Math.abs(fitness - a_fittest.getFitnessValue()) < 0.001) {
          fitter = 100;
          break;
        }
        else if (fitness >= a_fitness) {
          fitter++;
        }
        // Determine the worst entry for later replacement.
        // ------------------------------------------------
        if (worstEntry == null ||
            getConfiguration().getGPFitnessEvaluator().
            isFitter(worstFitness, fitness)) {
          worstEntry = prog;
          worstFitness = fitness;
        }
        count++;
      }
      boolean result = true;
      if (fitter < 3 || count > 3) { /**@todo make configurable*/
        // Remove worst result yet and add new fit result.
        // -----------------------------------------------
        if (worstEntry != null && count >= 3) {
          /**@todo compare with toStringNorm(0), use remove(int) instead of remove(Object)*/
          if (!topApp.remove(worstEntry)) {
            log.error("Removing of worst entry failed");
          }
        }
        if (fitter < 3) {
          try {
            GPGenotype.checkErroneousProg(a_fittest, " add top fit", true, false);
          } catch (Throwable t) {
            log.warn("Received program not valid!");
            result = false;
          }
          if (result) {
            a_fitness = a_fittest.getFitnessValue();
            if (a_fitness < 1000) { /**@todo ist nur test!*/
              result = false;
            }
            else {
              topApp.add(a_fittest);
              log.info("Added fit program, fitness: " +
                       NumberKit.niceDecimalNumber(a_fitness, 2));
              log.info("Solution: " + a_fittest.toStringNorm(0));
              result = true;
            }
          }
        }
        else {
          log.info(
              "Result not better than top results received, removed obsolete top result");
          result = false;
        }
      }
      else {
        log.info("Result not better than top results received");
        result = false;
      }
      if (result) {
        /**@todo skip unnecessary data, inject it after reload*/
        //m_persister.save(true, JGAPClientGP.FIELDSTOSKIP);
        m_persister.save();
      }
      return result;
    } catch (Exception ex) {
      ex.printStackTrace();
      throw ex;
    }
  }

  /**
   * Saves a result to disk.
   *
   * @param a_dir the directory to put the result into
   * @param a_filename name of the file to write
   * @param a_obj the result object to write
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.3.4
   */
  protected void saveResult(String a_dir, String a_filename,
                            IGPProgram a_obj)
      throws Exception {
    String filename = FileKit.addFilename(a_dir, a_filename);
    PersistableObject po = new PersistableObject(filename);
    po.setObject(a_obj);
    po.save();
  }

  public String[] getFilenames(String a_dir)
      throws Exception {
    String[] files = FileKit.listFilesInDir(a_dir, null);
    return files;
  }

  /**
   * Presets initial population to be included for input to workers.
   *
   * @param a_workRequest the work request that is about to be sent.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.3.3
   */
  protected void presetPopulation(JGAPRequestGP a_workRequest)
      throws Exception {
    // Merge previously stored results with new requests.
    // Often preset them as input for worker, sometimes give worker an
    // empty population.
    // -------------------------------------------------------------------
    RandomGenerator randGen = getConfiguration().getRandomGenerator();
    double d = randGen.nextDouble();
    if (d > 0.15d) {
      Map<String, List> topAll = m_objects.getTopResults();
      String appid = m_gridConfig.getContext().getAppId();
      List<IGPProgram> topApp = topAll.get(appid);
      int added = 0;
//      int index = 0;
      GPPopulation pop = a_workRequest.getPopulation();
      IGPProgram[] programs = pop.getGPPrograms();
//      while (index < pop.getPopSize() && pop.getGPProgram(index) != null) {
//        index++;
//      }
      List toAdd = new Vector();
      if (topApp != null && topApp.size() > 0) {
        // Merge top results in.
        // ---------------------
        for (IGPProgram prog : topApp) {
          GPGenotype.checkErroneousProg(prog, " before add preset", true);
          toAdd.add(prog);
          added++;
          if (added >= 3 || randGen.nextDouble() > 0.6d) {
            break;
          }
        }
      }
      // Merge not too bad results in.
      // -----------------------------
      String[] results = getFilenames(m_ntbResultsDir);
      if (results != null && results.length > 0) {
        int count = randGen.nextInt(Math.min(5, results.length));
        if (count > 0) {
          if (count > results.length) {
            count = results.length;
          }
          for (int i = 0; i < count; i++) {
            int index = randGen.nextInt(results.length);
            String filename = FileKit.addFilename(m_ntbResultsDir,
                results[index]);
            /**@todo remove results[index]*/
            PersistableObject po = new PersistableObject(filename);
            IGPProgram ntb = (IGPProgram) po.load();
            log.info("Presetting with NTB result");
            added++;
            toAdd.add(ntb);
          }
        }
      }
      // Now merge old and new programs to one pool.
      // -------------------------------------------
      int len = programs.length;
      if (len > 0) {
        len = 0;
        while (len < programs.length && programs[len] != null) {
          len++;
        }
        IGPProgram[] programsNew = (IGPProgram[]) toAdd.toArray(new IGPProgram[] {});
        int size = len + toAdd.size();
        IGPProgram[] allPrograms = new IGPProgram[size];
        if (len > 0) {
          System.arraycopy(programs, 0, allPrograms, 0, len);
        }
        System.arraycopy(programsNew, 0, allPrograms, len, programsNew.length);
        pop.setGPPrograms(allPrograms);
        log.info("Population preset with " + added + " additional programs");
      }
    }
  }

  protected void showCurrentResults()
      throws Exception {
    String appid = m_gridConfig.getContext().getAppId();
    Map<String, List> topAll = m_objects.getTopResults();
    List<IGPProgram> topApp = topAll.get(appid);
    if (topApp != null && topApp.size() > 0) {
      log.info("Top evolved results yet:");
      log.info("------------------------");
      boolean changed = false;
      Iterator<IGPProgram> it = topApp.iterator();
      while (it.hasNext()) {
        IGPProgram prog = it.next();
        try {
          GPGenotype.checkErroneousProg(prog, " as top result", false, true);
        } catch (Throwable t) {
          // Remove invalid program.
          // -----------------------
          it.remove();
          changed = true;
          continue;
        }
        double fitness = prog.getFitnessValue();
        log.info("Fitness " +
                 NumberKit.niceDecimalNumber(fitness, 2));
        if (fitness < 1000) {
          log.info("Removing too bad result with fitness "
                   + NumberKit.niceDecimalNumber(fitness, 2));
          it.remove();
          changed = true;
        }
      }
      if (changed) {
        m_persister.save();
      }
      log.info("");
    }
    else {
      log.info("No top results yet.");
    }
  }

  public int getMaxFetchResults() {
    return m_max_fetch_results;
  }

  private boolean startsWith(String s, String a_prefix) {
    if (s == null || a_prefix == null) {
      return false;
    }
    return s.startsWith(a_prefix);
  }

}
