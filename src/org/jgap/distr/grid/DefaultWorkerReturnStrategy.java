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

import org.jgap.*;

/**
 * Default and simple implementation for IWorkerReturnStrategy.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public class DefaultWorkerReturnStrategy
    implements IWorkerReturnStrategy {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  /**
   * Simple returns the fittest chromosome.
   *
   * @param a_req JGAPRequest
   * @param a_genotype Genotype
   * @return JGAPResult
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public JGAPResult assembleResult(JGAPRequest a_req, Genotype a_genotype) {
    try {
      IChromosome fittest = a_genotype.getFittestChromosome();
      Population pop = new Population(a_req.getConfiguration(), fittest);
      JGAPResult result = new JGAPResult(a_req.getSessionName(), a_req.getRID(),
          pop, 1);
      return result;
    } catch (Throwable t) {
      throw new RuntimeException(t);
    }
  }
}
