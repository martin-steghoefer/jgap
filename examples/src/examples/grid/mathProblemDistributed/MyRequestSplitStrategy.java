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

import org.jgap.*;
import org.jgap.gp.*;
import org.jgap.gp.impl.*;
import org.jgap.distr.grid.gp.*;

/**
 * Sample implementation of IRequestSplitStrategy to split a single request
 * into multiple requests for workers.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public class MyRequestSplitStrategy
    implements IRequestSplitStrategyGP {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.7 $";

  private GPConfiguration m_config;

  public MyRequestSplitStrategy(GPConfiguration a_config) {
    m_config = a_config;
  }

  public GPConfiguration getConfiguration() {
    return m_config;
  }

  /**
   * Creates single requests to be sent to workers. Here, each request consists
   * of a number of chromosome from the original population (determined here)
   * plus the rest of the chromosomes to be initialized randomly at each worker.
   *
   * @param a_request the request to split
   * @return single requests to be computed by workers
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public JGAPRequestGP[] split(JGAPRequestGP a_request)
      throws Exception {
    GPPopulation pop = a_request.getPopulation();
    // Is evolution started the first time?
    // ------------------------------------
    boolean firstTime;
    if (pop == null || pop.size() < 1) {
      firstTime = true;
    }
    else {
      firstTime = false;
    }
    if (!firstTime) {
      // Sort population to have the fittest individuals at the beginning.
      // -----------------------------------------------------------------
      pop.sortByFitness();
    }
    // Generate 20 work requests.
    // --------------------------
    int requests = 20; // number of requests to create
    JGAPRequestGP[] result = new JGAPRequestGP[requests];
    // Only send 10% of the population to the workers.
    // -----------------------------------------------
    int count = getConfiguration().getPopulationSize() / 10;
    if(count < 1) {
      count = 1;
    }
    for (int j = 0; j < requests; j++) {
      result[j] = (JGAPRequestGP) a_request.newInstance("Population " + j, "todo"+j,0);
      // Setup JGAP configuration for worker.
      // ------------------------------------
      GPConfiguration config = getConfiguration().newInstanceGP(j + "",
          "population " + j);
      // Assemble population for one request.
      // ------------------------------------
      RandomGenerator rand = getConfiguration().getRandomGenerator();
      GPPopulation workPop = new GPPopulation(config, count);
      if (!firstTime) {
        for (int i = 0; i < count; i++) {
          IGPProgram chrom;
          if (rand.nextDouble() > 0.2d) {
            // Take one of the best chromosomes.
            // ---------------------------------
            chrom = pop.getGPProgram(i);
          }
          else {
            // Take one of the ordinary chromosomes.
            // -------------------------------------
            chrom = pop.getGPProgram(i + count);
          }
          workPop.setGPProgram(i, chrom);
        }
      }
      result[j].setPopulation(workPop);
    }
    return result;
  }
}
