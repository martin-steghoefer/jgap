/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap;

/**
 * This exception is typically thrown when the
 * setValueFromPersistentRepresentation() method of a Gene class is unable
 * to process the string representation it has been given, either because that
 * representation is not supported by that Gene implementation or because
 * the representation is corrupt.
 *
 * @author Neil Rotstan
 * @since 1.0
 */
public class UnsupportedRepresentationException
    extends Exception {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.6 $";

  /**
   * Constructs a new UnsupportedRepresentationException instance with the
   * given error message.
   *
   * @param a_message an error message describing the reason this exception
   * is being thrown
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public UnsupportedRepresentationException(final String a_message) {
    super(a_message);
  }
}
