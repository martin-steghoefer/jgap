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

import org.jgap.gp.*;
import org.jgap.*;

/**
 * Default implementation of interface IPopulationCreator. Creates a complete
 * population with JGAP standard means.
 *
 * @author Klaus Meffert
 * @since 3.2.2
 */
public class DefaultPopulationCreator
    implements IPopulationCreator {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.8 $";

  private IProgramCreator m_programCreator;

  public DefaultPopulationCreator() {
    this(new DefaultProgramCreator());
  }

  public DefaultPopulationCreator(IProgramCreator a_programCreator) {
    m_programCreator = a_programCreator;
  }

  /**
   * Initializes the given population by creating individuals for it.
   *
   * @param a_pop the population to initialize
   * @param a_types the type for each chromosome, the length of the array
   * represents the number of chromosomes
   * @param a_argTypes the types of the arguments to each chromosome, must be an
   * array of arrays, the first dimension of which is the number of chromosomes
   * and the second dimension of which is the number of arguments to the
   * chromosome
   * @param a_nodeSets the nodes which are allowed to be used by each chromosome,
   * must be an array of arrays, the first dimension of which is the number of
   * chromosomes and the second dimension of which is the number of nodes
   * @param a_minDepths contains the minimum depth allowed for each chromosome
   * @param a_maxDepths contains the maximum depth allowed for each chromosome
   * @param a_maxNodes reserve space for a_maxNodes number of nodes
   * @param a_fullModeAllowed array of boolean values. For each chromosome there
   * is one value indicating whether the full mode for creating chromosome
   * generations during evolution is allowed (true) or not (false)
   *
   * @throws InvalidConfigurationException in case of any error
   *
   * @author Klaus Meffert
   * @since 3.2.2
   */
  public void initialize(GPPopulation a_pop, Class[] a_types,
                         Class[][] a_argTypes, CommandGene[][] a_nodeSets,
                         int[] a_minDepths, int[] a_maxDepths, int a_maxNodes,
                         boolean[] a_fullModeAllowed)
      throws InvalidConfigurationException {
    /**@todo pass params (e.g. advanced yes/no for Robocode)*/
    a_pop.create(a_types, a_argTypes, a_nodeSets, a_minDepths, a_maxDepths,
                 a_maxNodes, a_fullModeAllowed, m_programCreator);
  }
}
