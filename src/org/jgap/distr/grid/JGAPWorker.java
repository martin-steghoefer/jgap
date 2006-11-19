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
import org.homedns.dade.jcgrid.*;
import org.homedns.dade.jcgrid.worker.*;

/**
 * A worker receives work units from a JGAPServer and sends back computed
 * solutions to a JGAPServer.
 *
 * @author Klaus Meffert
 * @since 3.01
 */
public class JGAPWorker {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  private final static String className = JGAPWorker.class.getName();

  private static Logger log = Logger.getLogger(className);

  public JGAPWorker(GridNodeWorkerConfig a_config, Class a_workerClass,
                    Class a_workerFeedbackClaas)
      throws Exception {
    // Start all required Workers.
    // ---------------------------
    GridWorker[] gw = new GridWorker[a_config.getWorkerCount()];
    for (int i = 0; i < a_config.getWorkerCount(); i++) {
      gw[i] = new GridWorker();
      gw[i].setNodeConfig( (GridNodeGenericConfig) a_config.clone());
      ( (GridNodeGenericConfig) gw[i].getNodeConfig()).
          setSessionName(a_config.getSessionName() + "_" + i);
      ( (GridNodeGenericConfig) gw[i].getNodeConfig()).
          setWorkingDir(a_config.getWorkingDir() + "_" + i);
      // Instantiate worker and workerfeedback.
      // --------------------------------------
      Worker myWorker = (Worker) a_workerClass.newInstance();
      GridWorkerFeedback myWorkerFeedback = (GridWorkerFeedback)
          a_workerFeedbackClaas.newInstance();
      gw[i].setWorker(myWorker);
      gw[i].setWorkerFeedback(myWorkerFeedback);
      gw[i].start();
    }
    // Wait for shutdown of workers.
    // -----------------------------
    for (int i = 0; i < a_config.getWorkerCount(); i++)
      gw[i].waitShutdown();
  }
}
