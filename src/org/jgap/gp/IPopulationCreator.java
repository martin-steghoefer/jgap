/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp;

import org.jgap.gp.impl.*;
import org.jgap.InvalidConfigurationException;

/**
 *
 * Interface for initializing the complete population within
 * GPGenotype.randomInitializeGenotype.
 *
 * @author Klaus Meffert
 * @since 3.2.2
 */
public interface IPopulationCreator {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.2 $";

  /**
   * Initializes the given population.
   *
   * @param a_pop GPPopulation
   * @param a_types Class[]
   * @param a_argTypes Class[][]
   * @param a_nodeSets CommandGene[][]
   * @param a_minDepths int[]
   * @param a_maxDepths int[]
   * @param a_maxNodes int
   * @param a_fullModeAllowed boolean[]
   * @throws InvalidConfigurationException in case of any error
   *
   * @author Klaus Meffert
   * @since 3.2.2
   */
  void initialize(GPPopulation a_pop, Class[] a_types, Class[][] a_argTypes,
                  CommandGene[][] a_nodeSets, int[] a_minDepths,
                  int[] a_maxDepths, int a_maxNodes,
                  boolean[] a_fullModeAllowed)
      throws InvalidConfigurationException;
}
