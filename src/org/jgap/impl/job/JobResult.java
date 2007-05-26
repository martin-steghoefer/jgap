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
