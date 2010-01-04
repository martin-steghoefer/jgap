/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.distr;

import java.io.*;

import org.jgap.util.*;

/**
 * Holds information about a computing entity.
 *
 * @author Klaus Meffert
 * @since 2.4
 */
public class MasterInfo implements Serializable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.5 $";

  /**
   * The computer's address over which it is reachable in the network
   */
  public String m_IPAddress;

  /**
   * Name of the grid user associated with the IP address
   */
  public String m_username;

  /**
   * Host name of the computer
   */
  public String m_name;

  /**
   * Unique ID of a worker
   */
  public String m_GUID;

  public MasterInfo()
      throws Exception {
    this(false);
  }

  public MasterInfo(boolean a_preset)
      throws Exception {
    if (a_preset) {
      m_IPAddress = NetworkKit.getLocalIPAddress();
      m_name = NetworkKit.getLocalHostName();
      m_GUID = StringKit.removeChar(NetworkKit.getMACAddress(), ':');
    }
  }
}
