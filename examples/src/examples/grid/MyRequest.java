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

import org.homedns.dade.jcgrid.*;
import org.jgap.*;
import org.jgap.impl.*;
import org.jgap.event.*;

public class MyRequest
    extends WorkRequest {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  private Configuration m_config;

  public MyRequest(String name, int id, Configuration a_config) {
    super(name, id);
    m_config = a_config;
  }

  public MyRequest[] split()
      throws Exception {
    final int runs = 20;
    MyRequest[] result = new MyRequest[runs];
    for (int i = 0; i < runs; i++) {
      /**@todo support cloning of m_config*/
      Configuration config = new DefaultConfiguration("config " + i, i + "");
      config.setEventManager(new EventManager());
      config.setPopulationSize(m_config.getPopulationSize());
      config.setFitnessFunction(m_config.getFitnessFunction());
      IChromosome sample = new Chromosome(config, new BooleanGene(config), 16);
      config.setSampleChromosome(sample);
//      config.setSampleChromosome((IChromosome)m_config.getSampleChromosome().clone());
      result[i] = new MyRequest("JGAP-Grid Request " + i, i, config);
    }
    return result;
  }

  public Configuration getConfiguration() {
    return m_config;
  }
}
