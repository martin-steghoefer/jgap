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

/**
 * Utility routines related to numbers.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public class NumberKit {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.5 $";

  /**
   * Returns the hex value of "c" or -1 if there is no corresponding hex value.
   *
   * @param a_c hex character to convert
   * @return integer value of the character
   *
   * @author unknown
   * @since 3.2
   */
  public static int hexValue(char a_c) {
    if ('0' <= a_c && a_c <= '9') {
      return a_c - '0';
    }
    if ('A' <= a_c && a_c <= 'F') {
      return a_c - 'A' + 10;
    }
    if ('a' <= a_c && a_c <= 'f') {
      return a_c - 'a' + 10;
    }
    return -1;
  }

  private static final char[] DIGITS = {'0', '1', '2', '3', '4', '5', '6',
      '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

  /**
   * Transforms a byte to a character array of hex octets.
   * Taken from UUID.
   *
   * @param a_in the byte
   * @return the hex byte array
   */
  public static char[] asChars(byte a_in) {
    return asChars(a_in, 2);
  }

  /**
   * Transforms a byte to a character array of hex octets.
   * Taken from UUID.
   *
   * @param a_in the byte
   * @param a_length the number of octets to produce
   * @return the hex byte array
   */
  public static char[] asChars(byte a_in, int a_length) {
    char[] out = new char[a_length--];
    for (int i = a_length; i > -1; i--) {
      out[i] = DIGITS[ (byte) (a_in & 0x0F)];
      a_in >>= 4;
    }
    return out;
  }

  /**
   * Parses a short from a hex encoded number. This method will skip
   * all characters that are not 0-9 and a-f (the String is lower cased first).
   *
   * @param s the String to extract a short from, may not be null
   * @return 0 if the String does not contain any interesting characters
   * @throws NullPointerException if the String is null
   *
   * @since 3.3.3
   */
  public static short parseShort(String s)
      throws NullPointerException {
    s = s.toLowerCase();
    short out = 0;
    byte shifts = 0;
    char c;
    for (int i = 0; i < s.length() && shifts < 4; i++) {
      c = s.charAt(i);
      if ( (c > 47) && (c < 58)) {
        out <<= 4;
        ++shifts;
        out |= c - 48;
      }
      else if ( (c > 96) && (c < 103)) {
        ++shifts;
        out <<= 4;
        out |= c - 87;
      }
    }
    return out;
  }

  /**
   * Formats a number as a string having the total length of a_places, filling
   * up needed characters with a_filler.
   *
   * @param a_number the number to format
   * @param a_places total length of output string
   * @param a_filler fill character
   * @return formatted number
   *
   * @author Klaus Meffert
   * @since 3.3.3
   */
  public static String niceNumber(int a_number, int a_places, char a_filler) {
    String s = a_number + "";
    while (s.length() < a_places) {
      s = a_filler + s;
    }
    return s;
  }

  /**
   * Removes decimal places if there are more than a_decimals.
   *
   * @param a_number the number to convert to a string
   * @param a_decimals maximum number of decimal places allowed
   *
   * @return nicified string
   *
   * @author Klaus Meffert
   * @since 3.3.3
   */
  public static String niceDecimalNumber(double a_number, int a_decimals) {
    String s = a_number + "";
    int index = s.indexOf('.');
    if (index > 0) {
      // Do not remove anything if "E" is contained in the number string.
      // ----------------------------------------------------------------
      if (s.indexOf('E',index) > 0) {
        return s;
      }
      if (index + a_decimals >= s.length()) {
        a_decimals = s.length() - index - 1;
      }
      s = s.substring(0, index + a_decimals + 1);
      if (s.lastIndexOf('.') == s.length() - 1) {
        if (a_decimals < 1) {
          s = s.substring(0, s.length() - 1);
        }
        else {
          for (int i = 0; i < a_decimals; i++) {
            s += "0";
          }
        }
      }
    }
    return s;
  }
}
