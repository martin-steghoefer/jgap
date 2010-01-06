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

import java.io.*;
import java.util.*;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;

/**
 * System-related utility functions.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public class SystemKit {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.9 $";

  /**
   * @return total memory available by the VM in megabytes.
   *
   * @author Klaus Meffert
   * @since 3.2 (since 3.0 in GPGenotype)
   */
  public static double getTotalMemoryMB() {
    return getTotalMemoryKB() / 1024;
  }

  /**
   * @return total memory available by the VM in kilobytes.
   *
   * @author Klaus Meffert
   * @since 3.2.1
   */
  public static double getTotalMemoryKB() {
    return (Runtime.getRuntime().totalMemory() / 1024);
  }

  /**
   * @return free memory available in the VM in megabytes.
   *
   * @author Klaus Meffert
   * @since 3.2 (since 3.0 in GPGenotype)
   */
  public static double getFreeMemoryMB() {
    return getFreeMemoryKB() / 1024;
  }

  /**
   * @return free memory available in the VM in kilobytes.
   *
   * @author Klaus Meffert
   * @since 3.2.1
   */
  public static double getFreeMemoryKB() {
    return (Runtime.getRuntime().freeMemory() / 1024);
  }

  /**
   * Nicifies a decimal string by cutting of all but two decimal places.
   *
   * @param a_mem the number to make nice
   * @return nicified number as a string
   *
   * @since 3.3.1
   */
  public static String niceMemory(double a_mem) {
    String freeMB = "" + a_mem;
    int index = freeMB.indexOf('.');
    int len = freeMB.length();
    if (len - index > 2) {
      freeMB = freeMB.substring(0, index + 2);
    }
    return freeMB;
  }

  /**
   * Returns the first line of the result of a shell command.
   * Taken from UUID.
   *
   * @param commands the commands to run
   * @return the first line of the command
   * @throws IOException
   *
   * @since 3.3.3
   */
  static String getFirstLineOfCommand(String[] commands)
      throws IOException {
    Process p = null;
    BufferedReader reader = null;
    try {
      p = Runtime.getRuntime().exec(commands);
      reader = new BufferedReader(new InputStreamReader(
          p.getInputStream()), 128);
      return reader.readLine();
    } finally {
      if (p != null) {
        if (reader != null) {
          try {
            reader.close();
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
  }

  /**
   * Prints all available comamnd line options.
   *
   * @param cmd the CommandLine object
   * @param options the Options list
   *
   * @author Klaus Meffert
   * @since 3.3.4
   */
  public static void printHelp(CommandLine cmd, Options options) {
    if (cmd.hasOption("help")) {
      System.out.println("");
      System.out.println(" Command line options:");
      System.out.println(" ---------------------");
      for (Object opt0 : options.getOptions()) {
        Option opt = (Option) opt0;
        String s = opt.getOpt();
        s = StringKit.fill(s, 20, ' ');
        System.out.println(" " + s + " - " + opt.getDescription());
      }
      System.exit(0);
    }
  }

  /**
   * @return a GUID
   *
   * @author Klaus Meffert
   * @since 3.5
   */
  public static String getGUID() {
    return UUID.randomUUID().toString();
  }

}
