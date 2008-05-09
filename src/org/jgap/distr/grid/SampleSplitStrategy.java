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
 * Sample implementation of IRequestSplitStrategy. Here, a request is
 * transformed into 20 single requests, each to be computed by a worker.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public class SampleSplitStrategy
    implements IRequestSplitStrategy {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  private Configuration m_config;

  public SampleSplitStrategy(Configuration a_config) {
    m_config = a_config;
  }

  public Configuration getConfiguration() {
    return m_config;
  }

  /**
   * Creates 20 single requests out of one. The single requests are each
   * processed by one worker at a time.
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
    final int runs = 20;
    JGAPRequest[] result = new JGAPRequest[runs];
    for (int i = 0; i < runs; i++) {
      // Setup JGAP configuration for worker.
      // ------------------------------------
      Configuration config = getConfiguration().newInstance(i + "",
          "config " + i);
//      Configuration config = new DefaultConfiguration(i+"","chromosome " + i);
//      config.setEventManager(new EventManager());
//      config.setPopulationSize(1);
//      config.setFitnessFunction(getConfiguration().getFitnessFunction());
//      IChromosome sample = (IChromosome) getConfiguration().getSampleChromosome().
//          clone();
//      config.setSampleChromosome(sample);
      result[i] = (JGAPRequest) a_request.newInstance("JGAP-Grid Request " + i,
          i);
    }
    return result;
  }
}
