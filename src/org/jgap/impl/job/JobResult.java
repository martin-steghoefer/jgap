/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.impl.job;

import org.jgap.*;

public abstract class JobResult implements java.io.Serializable {
  private Configuration m_config;

  public JobResult() {

  }

  public Configuration getConfiguration() {
    return m_config;
  }

  public void setConfiguration(Configuration a_config) {
    m_config = a_config;
  }

}
