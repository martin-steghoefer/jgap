/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.grid.fitnessDistributed;

import org.apache.commons.cli.*;
import org.homedns.dade.jcgrid.cmd.*;
import org.homedns.dade.jcgrid.worker.*;
import org.jgap.distr.grid.*;

/**
 * Convenience call to start both the server and a worker at once. Only for
 * demonstration purposes. Normally, you would start the server and the
 * worker(s) separately!
 *
 * @author Klaus Meffert
 * @since 3.01
 */
public class ServerAndWorker {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  /**
   * Convenience (demo) start of both the server and a worker.
   *
   * @param args might not work here in this simple example as distinct options
   * between server and worker could lead to parsing errors.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.01
   */
  public static void main(String[] args)
      throws Exception {
    // Start server.
    // ------------
    new JGAPServer(args);
    // Setup worker configuration.
    // ---------------------------
    Options options = new Options();
    GridNodeWorkerConfig config = new GridNodeWorkerConfig();
    CommandLine cmd = MainCmd.parseCommonOptions(options, config, args);
    // Start worker.
    // -------------
    new JGAPWorkers(config, MyGAWorker.class, MyWorkerFeedback.class);
  }
}
