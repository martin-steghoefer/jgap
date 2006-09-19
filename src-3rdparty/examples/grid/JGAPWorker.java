/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.grid;

import org.homedns.dade.jcgrid.*;
import org.homedns.dade.jcgrid.worker.*;

public class JGAPWorker {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  public JGAPWorker()
      throws Exception {
    GridNodeWorkerConfig config = new GridNodeWorkerConfig();
    // Start all required Workers

    GridWorker[] gw = new GridWorker[config.getWorkerCount()];
    for (int i = 0; i < config.getWorkerCount(); i++) {
      gw[i] = new GridWorker();
      gw[i].setNodeConfig( (GridNodeGenericConfig) config.clone());
      ( (GridNodeGenericConfig) gw[i].getNodeConfig()).
          setSessionName(config.getSessionName() + "_" + i);
      ( (GridNodeGenericConfig) gw[i].getNodeConfig()).
          setWorkingDir(config.getWorkingDir() + "_" + i);
      gw[i].setWorker(new MyGAWorker());
      gw[i].setWorkerFeedback(new MyWorkerFeedback());
      gw[i].start();
    }
    // Wait shutdown

    for (int i = 0; i < config.getWorkerCount(); i++)
      gw[i].waitShutdown();
  }

  public static void main(String[] args)
      throws Exception {
    //start worker
    new JGAPWorker();
  }
}
