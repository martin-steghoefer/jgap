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

import org.jgap.*;

/**
 * Interface for GP commands that are mutateable.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public interface IMutateable {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.4 $";

  /**
   * Mutates a CommandGene.
   *
   * @param a_index references the part of a multipart object, normally not
   * relevant
   * @param a_percentage the mutation rate (0.0 to 1.0)
   * @return the mutant
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  CommandGene applyMutation(int a_index, double a_percentage)
      throws InvalidConfigurationException;

}
