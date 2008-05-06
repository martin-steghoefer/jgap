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
import org.apache.commons.codec.net.*;
import org.apache.commons.codec.*;

/**
 * String-related utility functions.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public class StringKit {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  /**
   * Encodes a string.
   *
   * @param a_string the string to encode
   * @return encoded string
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public static String encode(String a_string) {
    try {
      return new URLCodec().encode(a_string, "UTF-8");
    } catch (UnsupportedEncodingException uex) {
      throw new Error("UTF-8 encoding should always be supported!");
    }
  }

  /**
   * Decodes an encoded string.
   *
   * @param a_string the encoded string to decode
   * @return decoded string
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public static String decode(String a_string) {
    try {
      return new URLCodec().decode(a_string, "UTF-8");
    } catch (UnsupportedEncodingException uex) {
      throw new Error("UTF-8 encoding should always be supported!", uex);
    } catch (DecoderException dex) {
      throw new Error("UTF-8 encoding should always be supported!", dex);
    }
  }

  /**
   * Attempts to find a pattern in the given String. Taken from UUID
   *
   * @param in the String, may not be null
   * @return the substring that matches this pattern or null
   *
   * @since 3.3.3
   */
  public static String parse(String in) {
    // lanscan

    int hexStart = in.indexOf("0x");
    if (hexStart != -1 && in.indexOf("ETHER") != -1) {
      int hexEnd = in.indexOf(' ', hexStart);
      if (hexEnd > hexStart + 2) {
        return in.substring(hexStart, hexEnd);
      }
    }
    int octets = 0;
    int lastIndex, old, end;
    if (in.indexOf('-') > -1) {
      in = in.replace('-', ':');
    }
    lastIndex = in.lastIndexOf(':');
    if (lastIndex > in.length() - 2)
      return null;
    end = Math.min(in.length(), lastIndex + 3);
    ++octets;
    old = lastIndex;
    while (octets != 5 && lastIndex != -1 && lastIndex > 1) {
      lastIndex = in.lastIndexOf(':', --lastIndex);
      if (old - lastIndex == 3 || old - lastIndex == 2) {
        ++octets;
        old = lastIndex;
      }
    }
    if (octets == 5 && lastIndex > 1) {
      return in.substring(lastIndex - 2, end).trim();
    }
    return null;
  }

  /**
   * Fills a string with a given filler until length a_len is reached.
   * @param a_s String
   * @param a_len int
   * @param a_char char
   * @return result string
   *
   * @author Klaus Meffert
   * @since 3.3.3
   */
  public static String fill(String a_s, int a_len, char a_char) {
    String result = a_s;
    while (result.length() < a_len) {
      result += a_char;
    }
    return result;
  }

  /**
   * Removes all occurrences of a given char from a string
   * @param a_s the string
   * @param a_c the char to remove
   * @return result string
   *
   * @author Klaus Meffert
   * @since 3.3.3
   */
  public static String removeChar(String a_s, char a_c) {
    String result = a_s;
    if (result != null) {
      result = result.replaceAll("" + a_c, "");
    }
    return result;
  }
}
