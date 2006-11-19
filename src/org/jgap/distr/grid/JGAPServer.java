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

import org.apache.commons.cli.*;
import org.apache.log4j.*;
import org.homedns.dade.jcgrid.cmd.*;
import org.homedns.dade.jcgrid.server.*;

/**
 * A grid server able receiving work requests from JGAPClients, sending
 * work units to JGAPWorkers, receiving solutions from JGAPWorkers, and
 * sending back these solutions to the requesting JGAPClient.
 *
 * @author Klaus Meffert
 * @since 3.01
 */
public class JGAPServer {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  private final static String className = JGAPServer.class.getName();

  private static Logger log = Logger.getLogger(className);

  public JGAPServer(String[] args)
      throws Exception {
    GridServer gs = new GridServer();
    Options options = new Options();
    CommandLine cmd = MainCmd.parseCommonOptions(options, gs.getNodeConfig(),
        args);
    // Start Server
    gs.start();
  }

  public static void main(String[] args)
      throws Exception {
    MainCmd.setUpLog4J("server", true);
    //start server
    new JGAPServer(args);
  }
}
