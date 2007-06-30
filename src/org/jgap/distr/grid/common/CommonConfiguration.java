/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */

package org.jgap.distr.grid.common;

import java.io.*;
import org.jgap.util.*;
import org.apache.log4j.*;

/**
 *
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public abstract class CommonConfiguration {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  private transient Logger LOGGER = Logger.getLogger(CommonConfiguration.class);

  /**
   * Working directory
   */
  private String m_workDir;

  /**
   * Directory where the libraries to compute work units reside
   */
  private String m_libDir;

  /**
   * Connect string to the server
   */
  private String m_server;

  public CommonConfiguration() {
  }

  public CommonConfiguration(String a_workDir, String a_libDir) {
    this();
    setWorkDir(a_workDir);
    setLibDir(a_libDir);
  }

  public void setWorkDir(String a_workDir) {
    try {
      m_workDir = FileKit.addSubDir(FileKit.getCurrentDir(), a_workDir, true);
      LOGGER.info("Using work directory " + m_workDir);
      if (!FileKit.directoryExists(m_workDir)) {
        LOGGER.info("  Directory does not exist yet. Wil create it.");
        FileKit.createDirectory(m_workDir);

      }
    }
    catch (IOException iex) {
      throw new RuntimeException("Work directory " + a_workDir + " is invalid!");
    }
  }

  public String getWorkDir() {
    return m_workDir;
  }

  public void setLibDir(String a_libDir) {
    try {
      m_libDir = FileKit.addSubDir(FileKit.getCurrentDir(), a_libDir, true);
      LOGGER.info("Using lib directory " + m_libDir);
      if (!FileKit.directoryExists(m_libDir)) {
        LOGGER.info("  Directory does not exist yet. Wil create it.");
        FileKit.createDirectory(m_libDir);

      }
    }
    catch (IOException iex) {
      throw new RuntimeException("Lib directory " + a_libDir + " is invalid!");
    }
  }

  public String getLibDir() {
    return m_libDir;
  }

  public void setServerAddress(String a_server) {
    m_server = a_server;
  }

  public String getServerAddress() {
    return m_server;
  }
}
