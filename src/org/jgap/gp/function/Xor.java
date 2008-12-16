/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp.function;

import org.jgap.*;
import org.jgap.gp.*;
import org.jgap.gp.impl.*;
import org.jgap.util.*;

/**
 * The boolean xor operation.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class Xor
    extends MathCommand
implements ICloneable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.7 $";

  public Xor(final GPConfiguration a_conf)
      throws InvalidConfigurationException {
    super(a_conf, 2, CommandGene.BooleanClass);
  }

  public CommandGene applyMutation(int index, double a_percentage)
      throws InvalidConfigurationException {
    CommandGene mutant;
    if (a_percentage < 0.5d) {
      mutant = new And(getGPConfiguration());
    }
    else {
      mutant = new Or(getGPConfiguration());
    }
    return mutant;
  }

  public String toString() {
    return "&1 ^ &2";
  }

  /**
   * @return textual name of this command
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public String getName() {
    return "Xor";
  }

  public boolean execute_boolean(ProgramChromosome c, int n, Object[] args) {
    return c.execute_boolean(n, 0, args) ^ c.execute_boolean(n, 1, args);
  }

  /**
   * Clones the object. Simple and straight forward implementation here.
   *
   * @return cloned instance of this object
   *
   * @author Klaus Meffert
   * @since 3.4
   */
  public Object clone() {
    try {
      Xor result = new Xor(getGPConfiguration());
      return result;
    } catch (Exception ex) {
      throw new CloneException(ex);
    }
  }
}
