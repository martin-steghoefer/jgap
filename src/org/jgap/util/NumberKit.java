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

/**
 * Utility routines related to numbers.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public class NumberKit {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  /**
   * Returns the hex value of "c" or -1 if there is no corresponding hex value.
   *
   * @param c hex character to convert
   * @return integer value of the character
   *
   * @author taken from somewhere
   * @since 3.2
   */
  public static int hexValue(char c) {
    if ('0' <= c && c <= '9') {
      return c - '0';
    }
    if ('A' <= c && c <= 'F') {
      return c - 'A' + 10;
    }
    if ('a' <= c && c <= 'f') {
      return c - 'a' + 10;
    }
    return -1;
  }
}
