package org.jgap.distr.grid;

import org.jgap.event.*;
import org.jgap.*;
import org.jgap.impl.*;

public class RequestSplitStrategy {

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
//      IChromosome sample = new Chromosome(config, new BooleanGene(config), 16);
      IChromosome sample = (IChromosome)getConfiguration().getSampleChromosome().clone();
      config.setSampleChromosome(sample);
      result[i] = (JGAPRequest)a_request.newInstance("JGAP-Grid Request " + i,i);
//          new MyRequest("JGAP-Grid Request " + i, i, config);
    }
    return result;
  }

}
