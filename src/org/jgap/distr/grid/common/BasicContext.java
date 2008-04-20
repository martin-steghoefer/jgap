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

/**
 * Holds information to identify the context in which a solution should be
 * evolved.
 *
 * @author Klaus Meffert
 * @since 3.3.3
 */
public class BasicContext {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  private String m_appId;

  private String m_contextId;

  public BasicContext() {

  }
  public BasicContext(String a_appId, String a_contextId) {
    setAppId(a_appId);
    setContextId(a_contextId);
  }

  public String getContextId() {
    return m_contextId;
  }

  public void setContextId(String a_contextId) {
    m_contextId = a_contextId;
  }

  public String getAppId() {
    return m_appId;
  }

  public void setAppId(String a_appId) {
    m_appId = a_appId;
  }
}
