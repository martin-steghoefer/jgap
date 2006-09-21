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

import org.apache.commons.cli.*;
import org.homedns.dade.jcgrid.*;
import org.homedns.dade.jcgrid.worker.*;
import org.apache.log4j.*;
import org.homedns.dade.jcgrid.cmd.*;

public class JGAPWorker {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  private final static String className=JGAPWorker.class.getName();
  private static Logger log=Logger.getLogger(className);

  public JGAPWorker(GridNodeWorkerConfig a_config)
      throws Exception {
    // Start all required Workers

    GridWorker[] gw = new GridWorker[a_config.getWorkerCount()];
    for (int i = 0; i < a_config.getWorkerCount(); i++) {
      gw[i] = new GridWorker();
      gw[i].setNodeConfig( (GridNodeGenericConfig) a_config.clone());
      ( (GridNodeGenericConfig) gw[i].getNodeConfig()).
          setSessionName(a_config.getSessionName() + "_" + i);
      ( (GridNodeGenericConfig) gw[i].getNodeConfig()).
          setWorkingDir(a_config.getWorkingDir() + "_" + i);
      gw[i].setWorker(new MyGAWorker());
      gw[i].setWorkerFeedback(new MyWorkerFeedback());
      gw[i].start();
    }
    // Wait shutdown

    for (int i = 0; i < a_config.getWorkerCount(); i++)
      gw[i].waitShutdown();
  }

  public static void main(String[] args)
      throws Exception {
    MainCmd.setUpLog4J("worker", true);
    GridNodeWorkerConfig config = new GridNodeWorkerConfig();
    Options options=new Options();
    CommandLine cmd=MainCmd.parseCommonOptions(options,config,args);
    //start worker
    new JGAPWorker(config);
  }
}
