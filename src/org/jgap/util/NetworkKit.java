/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.util;

import java.net.*;
import java.io.*;

/**
 * Houses network related functionality.
 *
 * @author Klaus Meffert
 * @since 2.4
 */
public final class NetworkKit {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.5 $";

  /**
   * Private constructor because it's a utility class.
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

  /**
   * Taken and adapted from UUID.
   *
   * @return MAC address of the current computer.
   *
   * @since 3.3.3
   */
  public static String getMACAddress() {
    String macAddress = null;
    Process p = null;
    BufferedReader in = null;
    try {
      String osname = System.getProperty("os.name");
      if (osname.startsWith("Windows")) {
        p = Runtime.getRuntime().exec(
            new String[] {"ipconfig", "/all"}, null);
      }
      // Solaris code must appear before the generic code
      else if (osname.startsWith("Solaris") || osname.startsWith("SunOS")) {
        String hostName = SystemKit.getFirstLineOfCommand(new String[] {"uname",
            "-n"});
        if (hostName != null) {
          p = Runtime.getRuntime().exec(
              new String[] {"/usr/sbin/arp", hostName}, null);
        }
      }
      else if (new File("/usr/sbin/lanscan").exists()) {
        p = Runtime.getRuntime().exec(
            new String[] {"/usr/sbin/lanscan"}, null);
      }
      else if (new File("/sbin/ifconfig").exists()) {
        p = Runtime.getRuntime().exec(
            new String[] {"/sbin/ifconfig", "-a"}, null);
      }
      if (p != null) {
        in = new BufferedReader(new InputStreamReader(
            p.getInputStream()), 128);
        String l = null;
        while ( (l = in.readLine()) != null) {
          macAddress = StringKit.parse(l);
          if (macAddress != null
              && NumberKit.parseShort(macAddress) != 0xff)
            break;
        }
      }
    } catch (SecurityException ex) {} catch (IOException ex) {} finally {
      if (p != null) {
        if (in != null) {
          try {
            in.close();
          } catch (IOException ex) {}
        }
        try {
          p.getErrorStream().close();
        } catch (IOException ex) {}
        try {
          p.getOutputStream().close();
        } catch (IOException ex) {}
        p.destroy();
      }
    }
    return macAddress;
  }


  public static void main(String[] args) {
    System.out.println(getMACAddress());
  }
}
