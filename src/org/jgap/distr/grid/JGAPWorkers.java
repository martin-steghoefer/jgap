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
import org.homedns.dade.jcgrid.*;
import org.homedns.dade.jcgrid.worker.*;
import org.apache.commons.cli.*;
import org.homedns.dade.jcgrid.cmd.MainCmd;

/**
 * A worker receives work units from a JGAPServer and sends back computed
 * solutions to a JGAPServer.
 *
 * @author Klaus Meffert
 * @since 3.01
 */
public class JGAPWorkers {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.6 $";

  private Class m_workerClass;

  private Class m_workerFeedbackClaas;

  private final static String className = JGAPWorkers.class.getName();

  private static Logger log = Logger.getLogger(className);

  public JGAPWorkers(String[] args)
      throws Exception {
    MainCmd.setUpLog4J("worker", true);
    GridNodeWorkerConfig config = new GridNodeWorkerConfig();
    Options options = new Options();
    CommandLine cmd = MainCmd.parseCommonOptions(options, config, args);
    if (config.getSessionName().equals("none")) {
      config.setSessionName("MyGAWorker_session");
    }
    getNeededFiles(config);
    startWork(config);
  }

  /**
   * Get jar and other files needed for computation from server .
   *
   * @param a_config GridNodeWorkerConfig
   */
  public void getNeededFiles(GridNodeWorkerConfig a_config) {
    /**@todo*/
    // determine files

    // request files

    // get files

    // class-load jar files
  }

  public JGAPWorkers(GridNodeWorkerConfig a_config)
      throws Exception {
    startWork(a_config);
  }

  protected void startWork(GridNodeWorkerConfig a_config)
      throws Exception {
    // Start all required workers.
    // ---------------------------
    GridWorker[] gw = new GridWorker[a_config.getWorkerCount()];
    for (int i = 0; i < a_config.getWorkerCount(); i++) {
      // Instantiate worker via reflection.
      // ----------------------------------
      gw[i] = new GridWorker();
      gw[i].setNodeConfig( (GridNodeGenericConfig) a_config.clone());
      ( (GridNodeGenericConfig) gw[i].getNodeConfig()).
          setSessionName(a_config.getSessionName() + "_" + i);
      ( (GridNodeGenericConfig) gw[i].getNodeConfig()).
          setWorkingDir(a_config.getWorkingDir() + "_" + i);
      Worker myWorker = new JGAPWorker();
      // Instantiate worker feedback.
      // ----------------------------
      gw[i].setWorker(myWorker);
      //      GridWorkerFeedback myWorkerFeedback = (GridWorkerFeedback)
//          a_workerFeedbackClaas.newInstance();
//      gw[i].setWorkerFeedback(myWorkerFeedback);
      // Start single worker.
      // --------------------
      gw[i].start();
    }
    // Wait for shutdown of workers.
    // -----------------------------
    for (int i = 0; i < a_config.getWorkerCount(); i++) {
      gw[i].waitShutdown();
    }
  }

  /**
   * Convenience method to start a worker or multiple instances of it.
   * For possible parameters see method
   * parseCommonOptions in class org.homedns.dade.jcgrid.cmd.MainCmd. The most
   * important parameters are:
   * -n <session name without spaces>
   * -s <server IP address>
   * -d <working directory>
   * -c <number of workers to run>
   *
   * @param args see above
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public static void main(String[] args)
      throws Exception {
    MainCmd.setUpLog4J("worker", true);
    GridNodeWorkerConfig config = new GridNodeWorkerConfig();
    Options options = new Options();
    CommandLine cmd = MainCmd.parseCommonOptions(options, config, args);
    // Start worker(s).
    // ----------------
    new JGAPWorkers(config);
  }
}
