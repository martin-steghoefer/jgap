/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.grid;

import org.jgap.event.*;
import org.jgap.*;
import org.jgap.impl.*;
import org.jgap.distr.grid.*;

/**
 * Sample implementation of IRequestSplitStrategy.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public class FitnessSplitStrategy
    implements IRequestSplitStrategy {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  private Configuration m_config;

  public FitnessSplitStrategy(Configuration a_config) {
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
    int count = pop.size();
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
