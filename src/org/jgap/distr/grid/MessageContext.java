/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.distr.grid;

import org.jgap.distr.grid.common.*;

/**
 * Context of a message.
 *
 * @author Klaus Meffert
 * @since 3.3.3
 */
public class MessageContext
    extends BasicContext {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.4 $";

  private String m_module;

  private String m_context;

  private String m_userGrp;

  private String m_userID;

  private String m_version;

  public MessageContext() {
  }

  public MessageContext(String a_module, String a_context, Object a_contextid) {
    m_module = a_module;
    m_context = a_context;
    setContextId(a_contextid);
  }

  public String getContext() {
    return m_context;
  }

  public String getModule() {
    return m_module;
  }

  public void setModule(String a_module) {
    m_module = a_module;
  }

  public void setContext(String a_context) {
    m_context = a_context;
  }

  public String getUserID() {
    return m_userID;
  }

  public String getUserGrp() {
    return m_userGrp;
  }

  public void setUserGrp(String a_userGrp) {
    m_userGrp = a_userGrp;
  }

  public void setUserID(String a_userID) {
    m_userID = a_userID;
  }

  public String getVersion() {
    return m_version;
  }

  public void setVersion(String a_version) {
    m_version = a_version;
  }
}
