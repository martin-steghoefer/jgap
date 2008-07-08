/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp.terminal;

import org.jgap.*;
import org.jgap.gp.*;
import org.jgap.gp.impl.*;

/**
 * The boolean value false.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class False
    extends MathCommand implements IMutateable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.8 $";

  public False(final GPConfiguration a_conf)
      throws InvalidConfigurationException {
    super(a_conf, 0, CommandGene.BooleanClass);
  }

  public CommandGene applyMutation(int index, double a_percentage)
      throws InvalidConfigurationException {
    CommandGene mutant = new True(getGPConfiguration());
    return mutant;
  }

  public String toString() {
    return "false";
  }

  /**
   * @return textual name of this command
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public String getName() {
    return "Boolean value false";
  }

  public boolean execute_boolean(ProgramChromosome c, int n, Object[] args) {
    return false;
  }

  public Class getChildType(IGPProgram a_ind, int a_index) {
    return null;
  }
}
