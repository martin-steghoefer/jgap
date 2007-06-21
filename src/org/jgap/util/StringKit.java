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
  private final static String CVS_REVISION = "$Revision: 1.1 $";

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
  public static String decode(String a_string)       {
    try {
      return new URLCodec().decode(a_string, "UTF-8");
    } catch (UnsupportedEncodingException uex) {
      throw new Error("UTF-8 encoding should always be supported!", uex);
    } catch (DecoderException dex) {
      throw new Error("UTF-8 encoding should always be supported!", dex);
    }
  }
}
