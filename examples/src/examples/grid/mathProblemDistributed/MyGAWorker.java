/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.grid.mathProblemDistributed;

import org.homedns.dade.jcgrid.*;
import org.jgap.*;
import org.jgap.distr.grid.gp.*;

/**
 * Receives work, computes a solution and returns the solution to the requester.
 * This is done by solely using the JGAP standard mechanism of the super class,
 * JGAPWorker. You could do differently, see other examples provided with JGAP.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public class MyGAWorker
    extends JGAPWorkerGP {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  /**
   * Executes the evolution and returns the result.
   *
   * @param a_work WorkRequest
   * @param a_workDir String
   * @return WorkResult
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public WorkResult doWork(WorkRequest a_work, String a_workDir)
      throws Exception {
   // Doing the evolution as always just means:
   // -----------------------------------------
    return super.doWork(a_work, a_workDir);
  }

}
