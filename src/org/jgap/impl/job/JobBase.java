/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
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
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  private JobData m_data;

  public JobBase(JobData a_data) {
    m_data = a_data;
  }

  public void run() {
    try {
      execute(m_data);
    } catch (Exception ex) {
      /**@todo what to do here?*/
      ex.printStackTrace();
    }
  }

  public JobData getJobData() {
    /**@todo maybe we should make the returned m_data immutable (via cloning, e.g.)*/
    return m_data;
  }
}
