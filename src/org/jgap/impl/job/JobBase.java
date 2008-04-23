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

/**
 * Abstract base class for JGAP jobs.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public abstract class JobBase
    implements IJob {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.5 $";

  private JobData m_data;

  private boolean m_finished;

  private JobResult m_result;

  public JobBase(JobData a_data) {
    m_data = a_data;
  }

  public void run() {
    try {
      m_result = execute(m_data);
      if (m_result == null) {
        throw new IllegalStateException("Result must not be null!");
      }
    } catch (Exception ex) {
      /**@todo what to do here?*/
      ex.printStackTrace();
      throw new RuntimeException("Job failed");
    }
    setFinished();
  }

  public JobData getJobData() {
    return m_data;
  }

  public boolean isFinished() {
    return m_finished;
  }

  protected void setFinished() {
    m_finished = true;
  }

  public JobResult getResult() {
    if (!m_finished) {
      return null;
    }
    if (m_result == null) {
      throw new IllegalStateException("Result must not be null!");
    }
    return m_result;
  }
}
