package org.jgap.distr.grid.common;

import java.io.*;
import org.jgap.util.*;

public abstract class CommonConfiguration {
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
      System.out.println("Using work directory " + m_workDir);
      if (!FileKit.directoryExists(m_workDir)) {
        System.out.println("  Directory does not exist yet. Wil create it.");
        FileKit.createDirectory(m_workDir);

      }
    } catch (IOException iex) {
      throw new RuntimeException("Work directory " + a_workDir + " is invalid!");
    }
  }

  public String getWorkDir() {
    return m_workDir;
  }

  public void setLibDir(String a_libDir) {
    try {
      m_libDir = FileKit.addSubDir(FileKit.getCurrentDir(), a_libDir, true);
      System.out.println("Using lib directory " + m_libDir);
      if (!FileKit.directoryExists(m_libDir)) {
        System.out.println("  Directory does not exist yet. Wil create it.");
        FileKit.createDirectory(m_libDir);

      }
    } catch (IOException iex) {
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
