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

import org.jgap.*;
import org.jgap.distr.grid.*;

/**
 * Just compute the fitness value in our example, where only fitness value
 * computation is dirtibuted.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public class MyWorkerReturnStrategy
    implements IWorkerReturnStrategy {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  /**
   * Computes fitness of one chromosome and returns it.
   *
   * @param a_req JGAPRequest
   * @param a_genotype Genotype
   * @return JGAPResult
   * @throws Exception in case of any error
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public JGAPResult assembleResult(JGAPRequest a_req, Genotype a_genotype)
      throws Exception {
    IChromosome chrom = a_req.getPopulation().getChromosome(0);
    // Do the actual fitness computation here.
    // ---------------------------------------
    chrom.getFitnessValue();
    Population pop = new Population(a_req.getConfiguration(), chrom);
    MyResult result = new MyResult(a_req.getSessionName(), a_req.getRID(),
                                   pop, 1);
    return result;
  }
}
