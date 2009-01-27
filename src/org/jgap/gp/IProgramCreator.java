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
import org.jgap.*;

/**
 *
 * Interface for creating a single program (IGPProgram).
 *
 * @author Klaus Meffert
 * @since 3.2.2
 */
public interface IProgramCreator {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.3 $";

  /**
   * Initializes the given population by adding new individuals to it.
   *
   * @param a_conf the configuration to use
   * @param a_programIndex index of the current program in the population,
   * starting with 0
   * @param a_types Class[]
   * @param a_argTypes Class[][]
   * @param a_nodeSets CommandGene[][]
   * @param a_minDepths int[]
   * @param a_maxDepths int[]
   * @param a_maxNodes int
   * @param a_depth the required depth of the program
   * @param a_grow true: use grow mode, false: use full mode
   * @param a_tries maximum number of tries allowed to produce a valid program
   * @param a_fullModeAllowed boolean[]
   *
   * @return the newly created program
   *
   * @throws InvalidConfigurationException in case of any error
   *
   * @author Klaus Meffert
   * @since 3.2.2
   */
  IGPProgram create(GPConfiguration a_conf, int a_programIndex, Class[] a_types,
                    Class[][] a_argTypes, CommandGene[][] a_nodeSets,
                    int[] a_minDepths, int[] a_maxDepths, int a_maxNodes,
                    int a_depth, boolean a_grow, int a_tries,
                    boolean[] a_fullModeAllowed)
      throws InvalidConfigurationException;
}

