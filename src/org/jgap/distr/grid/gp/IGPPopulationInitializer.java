package org.jgap.distr.grid.gp;

import org.jgap.gp.impl.GPGenotype;
import org.jgap.InvalidConfigurationException;
import org.jgap.gp.impl.GPConfiguration;
import org.jgap.gp.CommandGene;

/**
 * Allows to initialize a GP population right when creating a GPGenotype.
 * Is called within constructor of GPGenotype.
 *
 * See examples.grid.mathProblemDistrubuted for an example
 *
 *
 * @author Klaus Meffert
 * @since 3.6
 */
public interface IGPPopulationInitializer {
  void setUp(GPConfiguration a_conf, Class[] a_types,Class[][] argTypes,
      CommandGene[][] a_nodeSets, int a_maxNodes, boolean a_verboseOutput) throws Exception;
  GPGenotype execute() throws InvalidConfigurationException;

}
