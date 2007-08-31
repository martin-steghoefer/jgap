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

import java.io.*;

/**
 * Interface for jobs of any kind.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public interface IJob
    extends Serializable, Runnable {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.7 $";

  /**
   * Executes a job.
   *
   * @param a_data generic input parameters, as needed
   * @return result of execution
   *
   * @throws Exception in case of any error
   */
  JobResult execute(JobData a_data)
      throws Exception;

  JobData getJobData();

  void run();

  boolean isFinished();

  JobResult getResult();
}
