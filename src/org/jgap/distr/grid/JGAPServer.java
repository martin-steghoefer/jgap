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

import java.io.*;
import org.apache.commons.cli.*;
import org.apache.log4j.*;
import org.homedns.dade.jcgrid.cmd.*;
import org.homedns.dade.jcgrid.server.*;

/**
 * A grid server is able to:
 *
 *   a) receive work requests from JGAPClients
 *   b) send work units to JGAPWorkers
 *   c) receive solutions from JGAPWorkers, and
 *   d) send back these solutions to the requesting JGAPClient.
 *
 * @author Klaus Meffert
 * @since 3.01
 */
public class JGAPServer {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.7 $";

  private final static String className = JGAPServer.class.getName();

  private static Logger log = Logger.getLogger(className);

  private GridServer m_gs;

  public JGAPServer(String[] args)
      throws Exception {
    m_gs = new GridServer(JGAPClientHandlerThread.class);
    Options options = new Options();
    CommandLine cmd = MainCmd.parseCommonOptions(options, m_gs.getNodeConfig(),
        args);
    // Start Server.
    // -------------
    m_gs.start();
//    addFile("c:/temp/jgap/jgap.jar");
  }

  // Just for testing purposes
  public void addFile(String a_filename)
      throws Exception {
    String path = m_gs.getVFSSessionPool().getPath();
    if (path == null) {
      return;
    }
    if (path.charAt(path.length() - 1) != '\\') {
      path += "\\";
    }
    copyFile(a_filename, path);
  }

  public static void copyFile(String source, String dest)
      throws Exception {
    File destFile = new File(dest);
    if (!destFile.isFile()) {
      String origFilename = new File(source).getName();
      dest = dest + origFilename;
    }
    File inputFile = new File(source);
    File outputFile = new File(dest);
//     FileReader in = new FileReader(inputFile);
//     FileWriter out = new FileWriter(outputFile);

    FileInputStream in;
    FileOutputStream out;
    in = new FileInputStream(inputFile);
    out = new FileOutputStream(outputFile);
    int c;
    while ( (c = in.read()) != -1) {
      out.write(c);
    }
    in.close();
    out.close();
  }

  public static void main(String[] args)
      throws Exception {
    MainCmd.setUpLog4J("server", true);
    // Create the server.
    // ------------------
    new JGAPServer(args);
  }
}
