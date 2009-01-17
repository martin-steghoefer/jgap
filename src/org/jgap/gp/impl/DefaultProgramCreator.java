/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp.impl;

import org.jgap.*;
import org.jgap.gp.*;

/**
 * Used by DefaultPopulationCreator to create GP programs.
 *
 * @author Klaus Meffert
 * @since 3.2.1
 */
public class DefaultProgramCreator
    implements IProgramCreator {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.4 $";

  public IGPProgram create(GPConfiguration a_conf, int a_programIndex,
                           Class[] a_types, Class[][] a_argTypes,
                           CommandGene[][] a_nodeSets, int[] a_minDepths,
                           int[] a_maxDepths, int a_maxNodes, int a_depth,
                           boolean a_grow, int a_tries,
                           boolean[] a_fullModeAllowed)
      throws InvalidConfigurationException {
    GPProgram program = new GPProgram(a_conf, a_types, a_argTypes, a_nodeSets,
                                      a_minDepths, a_maxDepths, a_maxNodes);
    program.growOrFull(a_depth, a_grow, a_maxNodes, a_fullModeAllowed,
                       a_tries);
    return program;
  }
}
