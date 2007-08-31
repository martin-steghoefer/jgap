/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.xml;

/**
 * An ImproperXMLException will be thrown when an XML document or element is
 * parsed but is found to be structured improperly or missing required data.
 * The error message should be consulted for the exact reason the exception
 * is being thrown.
 *
 * @author Neil Rotstan
 * @author Klaus Meffert
 * @since 1.0
 */
public class ImproperXMLException
    extends Exception {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.7 $";

  /**
   * Constructs a new ImproperXMLException instance with the given error
   * message.
   *
   * @param a_message an error message describing the reason this exception
   * is being thrown
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public ImproperXMLException(final String a_message) {
    super(a_message);
  }
}
