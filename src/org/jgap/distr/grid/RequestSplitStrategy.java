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

import org.jgap.event.*;
import org.jgap.*;
import org.jgap.impl.*;

/**
 * Implementation of IRequestSplitStrategy to split a single request
 * into multiple requests for workers.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public class RequestSplitStrategy {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.4 $";

  private Configuration m_config;

  public RequestSplitStrategy(Configuration a_config) {
    m_config = a_config;
  }

  public Configuration getConfiguration() {
    return m_config;
  }
  /**
   * Creates single requests to be sent to workers.
   *
   * @return single requests to be computed by workers
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.01
   */
  public JGAPRequest[] split(JGAPRequest a_request)
      throws Exception {
    final int runs = 20;
    JGAPRequest[] result = new JGAPRequest[runs];
    for (int i = 0; i < runs; i++) {
      /**@todo support cloning of m_config*/
      Configuration config = new DefaultConfiguration("config " + i, i + "");
      config.setEventManager(new EventManager());
      config.setPopulationSize(getConfiguration().getPopulationSize());
      config.setFitnessFunction(getConfiguration().getFitnessFunction());
      IChromosome sample = (IChromosome)getConfiguration().getSampleChromosome().clone();
      config.setSampleChromosome(sample);
      result[i] = (JGAPRequest)a_request.newInstance("JGAP-Grid Request " + i,i);
//          new MyRequest("JGAP-Grid Request " + i, i, config);
    }
    return result;
  }

}
