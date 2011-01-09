/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.distr.grid.gp;

import org.jgap.gp.impl.GPGenotype;
import org.jgap.InvalidConfigurationException;
import org.jgap.gp.impl.GPConfiguration;
import org.jgap.gp.CommandGene;

/**
 * Allows to initialize a GP population right when creating a GPGenotype.
 * Is called within constructor of GPGenotype.
 *
 * See examples.grid.mathProblemDistrubuted for an example.
 *
 * @author Klaus Meffert
 * @since 3.6
 */
public interface IGPPopulationInitializer {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.2 $";

  void setUp(GPConfiguration a_conf, Class[] a_types, Class[][] argTypes,
             CommandGene[][] a_nodeSets, int a_maxNodes,
             boolean a_verboseOutput)
      throws Exception;

  GPGenotype execute()
      throws InvalidConfigurationException;
}
