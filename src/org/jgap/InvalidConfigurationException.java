/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap;

/**
 * This exception is typically thrown when an invalid value has been
 * passed to a Configuration object, an attempt is made to lock a Configuration
 * object before all required settings have been provided, or an attempt is
 * made to alter a setting in a Configuration object after it has been
 * successfully locked.
 *
 * @author Neil Rotstan
 * @author Klaus Meffert
 * @since 1.0
 */
public class InvalidConfigurationException
    extends Exception {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.4 $";

  /**
   * Constructs a new InvalidConfigurationException instance with the
   * given error message.
   *
   * @param a_message An error message describing the reason this exception
   *                  is being thrown.
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public InvalidConfigurationException(String a_message) {
    super(a_message);
  }
}
