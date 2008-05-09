/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.data.config;

/**
 * Exception throw when there is an error reading or loading the config file
 * describing the GUI for JGAP configuration.
 *
 * @author Siddhartha Azad
 * @since 2.4
 * */
public class MetaConfigException
    extends Exception {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  /**
   * Constructs a new MetaConfigException instance with the
   * given error message.
   *
   * @param a_message an error message describing the reason this exception
   * is being thrown
   *
   * @author Siddhartha Azad
   * @since 2.4
   */
  public MetaConfigException(final String a_message) {
    super(a_message);
  }
}
