/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.distr.grid.wan;

import java.util.*;

/**
 * Contains header information as well as the real content.
 *
 * @author Klaus Meffert
 * @since 3.3.3
 */
public class ReceivedPacket {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  private String m_title;

  private Map m_headerData;

  private Object m_data;

  private String m_appid;

  private String m_URI;

  public ReceivedPacket(String a_title, Map a_headerData, Object a_data,
                        String a_URI) {
    m_title = a_title;
    m_appid = makeAppid(m_title);
    m_headerData = a_headerData;
    m_data = a_data;
    m_URI = a_URI;
  }

  public Object getData() {
    return m_data;
  }

  public Map getHeaderData() {
    return m_headerData;
  }

  public String getTitle() {
    return m_title;
  }

  public String getAppid() {
    return m_appid;
  }

  private String makeAppid(String a_title) {
    if (a_title == null || a_title.length() < 1) {
      return "";
    }
    int index = a_title.indexOf("_");
    if (index > 0) {
      return a_title.substring(0, index);
    }
    return "";
  }

  public String getURI() {
    return m_URI;
  }

  public void clearHeaderData() {
    m_headerData.clear();
  }
}
