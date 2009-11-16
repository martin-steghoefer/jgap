/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.distr.grid.common;

import java.io.*;

/**
 * Holds information to identify the context in which a solution should be
 * evolved.
 *
 * @author Klaus Meffert
 * @since 3.3.3
 */
public class BasicContext implements Serializable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  private String m_appId;

  private Object m_contextId;

  public BasicContext() {

  }
  public BasicContext(String a_appId, Object a_contextId) {
    setAppId(a_appId);
    setContextId(a_contextId);
  }

  public Object getContextId() {
    return m_contextId;
  }

  /**
   * @return context ID as a string, as it should be a string most times (but
   * not always!)
   */
  public String getContextIdAsString() {
    return (String)m_contextId;
  }

  /**
   * Sets the context id. It is an object, not a string, as a request or a
   * response could be available as a header-object and not by its ID.
   *
   * @param a_contextId the context id to set
   */
  public void setContextId(Object a_contextId) {
    m_contextId = a_contextId;
  }

  public String getAppId() {
    return m_appId;
  }

  public void setAppId(String a_appId) {
    m_appId = a_appId;
  }
}
