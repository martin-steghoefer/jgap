/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.grid.mathProblemDistributed;

import org.jgap.*;
import org.jgap.gp.*;
import org.jgap.gp.impl.*;
import org.jgap.distr.grid.*;
import org.jgap.distr.grid.gp.*;

/**
 * Return the top 10 results to the client.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public class MyWorkerReturnStrategy
    implements IWorkerReturnStrategyGP {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  /**
   * Determines the top 10 chromosomes and returns them.
   *
   * @param a_req JGAPRequest
   * @param a_genotype Genotype
   * @return JGAPResult
   * @throws Exception in case of any error
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public JGAPResultGP assembleResult(JGAPRequestGP a_req, GPGenotype a_genotype)
      throws Exception {
    IGPProgram best = a_genotype.getGPPopulation().determineFittestProgram();
    JGAPResultGP result = new JGAPResultGP(a_req.getSessionName(), a_req.getRID(),
                                       best, 1);
    return result;
  }
}
