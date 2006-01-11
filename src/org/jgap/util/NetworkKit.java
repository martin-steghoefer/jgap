/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.util;

import java.net.*;

/**
 * Houses network-related functionality.
 *
 * @author Klaus Meffert
 * @since 2.4
 */
public final class NetworkKit {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  /**
   * Private constructor because it's  utility class
   */
  private NetworkKit() {

  }

  /**
   * @return IP address string in textual presentation
   * @throws UnknownHostException
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public static String getLocalIPAddress()
      throws UnknownHostException {
    return InetAddress.getLocalHost().getHostAddress();
  }

  /**
   * @return name of the computer calling this function locally
   * @throws UnknownHostException
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public static String getLocalHostName()
      throws UnknownHostException {
    return InetAddress.getLocalHost().getHostName();
  }

}
