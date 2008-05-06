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

import java.text.*;
import java.util.*;

/**
 * Utility functions related to date and time.
 *
 * @author Klaus Meffert
 * @since 3.3.3
 */
public class DateKit {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  private static Calendar m_cal = Calendar.getInstance();

  private static SimpleDateFormat m_sdfNow = new SimpleDateFormat(
      "yyyyMMddHHmmssSSS");

  private static SimpleDateFormat m_sdfToday = new SimpleDateFormat("yyyyMMdd");

  /**
   * @return now as a string, including milliseconds
   *
   * @author Klaus Meffert
   * @since 3.3.3
   */
  public static String getNowAsString() {
    return m_sdfNow.format(m_cal.getTime());
  }

  /**
   * @return today as a string
   *
   * @author Klaus Meffert
   * @since 3.3.3
   */
  public static String getTodayAsString() {
    return m_sdfToday.format(m_cal.getTime());
  }

  /**
   * @return now
   *
   * @author Klaus Meffert
   * @since 3.3.3
   */
  public static Date now() {
    return new Date();
  }
}
