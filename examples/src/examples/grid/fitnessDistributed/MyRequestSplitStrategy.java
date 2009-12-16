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
 * Sample implementation of IRequestSplitStrategy to split a single request
 * into multiple requests for workers.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public class MyRequestSplitStrategy
    implements IRequestSplitStrategy {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  private Configuration m_config;

  public MyRequestSplitStrategy(Configuration a_config) {
    m_config = a_config;
  }

  public Configuration getConfiguration() {
    return m_config;
  }

  /**
   * Creates single requests to be sent to workers. Here, each request consists
   * of a single chromosome for which to calculate the fitness value.
   *
   * @param a_request the request to split
   * @return single requests to be computed by workers
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public JGAPRequest[] split(JGAPRequest a_request)
      throws Exception {
    Population pop = a_request.getPopulation();
    // Sort Population to only work with the most fittest individuals.
    // This is necessary as a Population can grow further than given
    // with the Configuration (it has to do with performance, sorry).
    // ---------------------------------------------------------------
    if (m_config.getGenerationNr() > 0) {
      // Only sort by fitness after the initial generation as in
      // generation 0 no requests have been processed on the workers.
      // ------------------------------------------------------------
      pop.sortByFitness();
    }
    int count = getConfiguration().getPopulationSize();
    JGAPRequest[] result = new JGAPRequest[count];
    for (int i = 0; i < count; i++) {
      // Setup JGAP configuration for worker.
      // ------------------------------------
      Configuration config = getConfiguration().newInstance(i + "",
          "chromosome " + i);
      // Create single worker request.
      // -----------------------------
      IChromosome chrom = pop.getChromosome(i);
      result[i] = (JGAPRequest) a_request.newInstance("Chromosome " + i,
          i);
      result[i].setPopulation(new Population(config, chrom));
    }
    return result;
  }
}
