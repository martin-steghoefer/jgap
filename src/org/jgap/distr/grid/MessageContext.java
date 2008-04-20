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

/**
 * Context of a message.
 *
 * @author Klaus Meffert
 * @since 3.3.3
 */
public class MessageContext {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  private String m_module;

  private String m_context;

  private String m_contextid;

  public MessageContext() {
  }

  public MessageContext(String a_module, String a_context, String a_contextid) {
    m_module = a_module;
    m_context = a_context;
    m_contextid = a_contextid;
  }

  public String getContext() {
    return m_context;
  }

  public String getContextid() {
    return m_contextid;
  }

  public String getModule() {
    return m_module;
  }

  public void setModule(String a_module) {
    m_module = a_module;
  }

  public void setContextid(String a_contextid) {
    m_contextid = a_contextid;
  }

  public void setContext(String a_context) {
    m_context = a_context;
  }
}
