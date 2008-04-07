/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.grid.fitnessDistributed;

import org.homedns.dade.jcgrid.*;
import org.jgap.*;
import org.jgap.distr.grid.*;

/**
 * Receives work, computes a solution and returns the solution to the requester.
 *
 * @author Klaus Meffert
 * @since 3.01
 */
public class MyGAWorker
    extends JGAPWorker {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.5 $";

  /**
   * Executes the evolution and returns the result.
   *
   * @param work WorkRequest
   * @param workDir String
   * @return WorkResult
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.01
   */
  public WorkResult doWork(WorkRequest work, String workDir)
      throws Exception {
    // Here we could use our own class descended from JGAPRequest
    //    MyRequest req = ( (MyRequest) work);
    // For the result we could also use an individual class such as MyResult
    // It would be also possible to do different computations than in
    // super.doWork(...)
    //    MyResult res = (MyResult)super.doWork(work, workDir);

    // Doing the evolution as always just means:
//    return super.doWork(work, workDir);

    // But we want to only calculate the fitness value of the chromosomes
    // passed. In our case this is only one chromosome.
    // ------------------------------------------------------------------
    JGAPRequest req = ( (JGAPRequest) work);
    IChromosome chrom = req.getPopulation().getChromosome(0);
    // Do the actual fitness computation here.
    // ---------------------------------------
    chrom.getFitnessValue();
    Population pop = new Population(req.getConfiguration(), chrom);
    MyResult result = new MyResult(req.getSessionName(), req.getRID(), pop, 1);
    return result;
  }

}
