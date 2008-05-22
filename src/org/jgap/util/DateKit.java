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
  private final static String CVS_REVISION = "$Revision: 1.4 $";

  private static Calendar m_cal = Calendar.getInstance();

  public final static String DATEFORMAT_FULL_0 = "yyyyMMddHHmmssSSS";
  public final static String DATEFORMAT_FULL_1 = "yyyy/MM/dd HH:mm:ss:SSS";
  public final static String DATEFORMAT_NORMAL = "yyyy/MM/dd";

  private static SimpleDateFormat m_sdfNow = new SimpleDateFormat(DATEFORMAT_FULL_0);


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
   * @param a_date the date to be returned in a specific format
   * @param a_dateFormat the desired format of the date
   *
   * @return date in given format
   *
   * @author Klaus Meffert
   * @since 3.3.4
   */
  public static String dateToString(Date a_date, String a_dateFormat) {
    SimpleDateFormat sdf = new SimpleDateFormat(a_dateFormat);
    return sdf.format(a_date);
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
