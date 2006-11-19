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
import org.homedns.dade.jcgrid.cmd.*;
import org.homedns.dade.jcgrid.worker.*;
import org.jgap.distr.grid.*;

/**
 * Convenience call to start both the server and a worker at once. Only for
 * demonstration purposes. Normally, you would start the server and the worker(s)
 * separately!
 *
 * @author Klaus Meffert
 * @since 3.01
 */
public class ServerAndWorker {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  /**
   * Convenience (demo) start of both the server and a worker.
   *
   * @param args might not work as distinct options between server and worker
   * could lead to parsing errors.
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
    // Setup configuration.
    // --------------------
    Options options = new Options();
    GridNodeWorkerConfig config = new GridNodeWorkerConfig();
    CommandLine cmd = MainCmd.parseCommonOptions(options, config, args);
    // Start worker.
    // -------------
    new JGAPWorker(config, MyGAWorker.class, MyWorkerFeedback.class);
  }
}
