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
import org.jgap.gp.impl.*;

/**
 * Abstract base class for GP-commands with a dynamic number of children.
 *
 * @author Klaus Meffert
 * @since 3.4
 */
public abstract class CommandDynamicArity
    extends CommandGene {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.2 $";

  /**
   * Default constructor, only for dynamic instantiation.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.4
   */
  public CommandDynamicArity()
      throws Exception {
    super();
  }

  public CommandDynamicArity(final GPConfiguration a_conf, int a_arityInitial,
                             int a_arityMin, int a_arityMax, Class a_returnType)
      throws InvalidConfigurationException {
    this(a_conf, a_arityInitial, a_arityMin, a_arityMax, a_returnType, 0);
  }

  public CommandDynamicArity(final GPConfiguration a_conf, int a_arityInitial,
                             int a_arityMin, int a_arityMax, Class a_returnType,
                             int a_subReturnType)
      throws InvalidConfigurationException {
    this(a_conf, a_arityInitial, a_arityMin, a_arityMax, a_returnType,
         a_subReturnType, null);
  }

  /**
   * Allows specifying a sub return type and sub child types.
   *
   * @param a_conf the configuration to use
   * @param a_arityInitial the number of children of the node
   * @param a_arityMin the minimum arity allowed/required
   * @param a_arityMax the maximum arity allowed/required
   * @param a_returnType type of the return value of the node
   * @param a_subReturnType sub type of the return type, optional usage
   * @param a_subChildTypes sub type of a child, optional usage
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.4
   */
  public CommandDynamicArity(final GPConfiguration a_conf, int a_arityInitial,
                             int a_arityMin, int a_arityMax, Class a_returnType,
                             int a_subReturnType, int[] a_subChildTypes)
      throws InvalidConfigurationException {
    super(a_conf, a_arityInitial, a_returnType, a_subReturnType,
          a_subChildTypes);
    if (a_arityMin < 1) {
      throw new RuntimeException("Minimum arity must not be less than one!");
    }
    if (a_arityMax < a_arityMin) {
      throw new RuntimeException(
          "Minimum arity must be less than maximum arity!");
    }
    if (a_arityInitial < a_arityMin || a_arityInitial > a_arityMax) {
      throw new RuntimeException("Initial arity must be between minimum and"
                                 + " maximum arity!");
    }
    setArityMin(a_arityMin);
    setArityMax(a_arityMax);
  }

  /**
   * Adaptation of the arity so that it represents a value within the interval
   * [m_arityMin, m_arityMax].
   *
   * @author Klaus Meffert
   * @since 3.4
   */
  public void dynamizeArity() {
    int arity = getArityMin()
        + getGPConfiguration().getRandomGenerator().nextInt(getArityMax()
        - getArityMin() + 1);
    setArity(arity);
  }
}
