/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp;

import org.jgap.*;
import org.jgap.gp.impl.*;

/**
 * Abstract base class for GP-commands related to mathematical calculation.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public abstract class MathCommand
    extends CommandGene {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.9 $";

  public MathCommand(final GPConfiguration a_conf, int a_arity,
                     Class a_returnType)
      throws InvalidConfigurationException {
    this(a_conf, a_arity, a_returnType, 0, null);
  }

  /**
   * Allows specifying a sub return type and sub child types.
   *
   * @param a_conf GPConfiguration
   * @param a_arity int
   * @param a_returnType Class
   * @param a_subReturnType int
   * @param a_subChildTypes int[]
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public MathCommand(final GPConfiguration a_conf, int a_arity,
                     Class a_returnType, int a_subReturnType,
                     int[] a_subChildTypes)
      throws InvalidConfigurationException {
    super(a_conf, a_arity, a_returnType, a_subReturnType, a_subChildTypes);
  }

  /**
   * Allows specifying a sub return type and a single sub child type.
   *
   * @param a_conf GPConfiguration
   * @param a_arity int
   * @param a_returnType Class
   * @param a_subReturnType int
   * @param a_subChildType int
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public MathCommand(final GPConfiguration a_conf, int a_arity,
                     Class a_returnType, int a_subReturnType,
                     int a_subChildType)
      throws InvalidConfigurationException {
    super(a_conf, a_arity, a_returnType, a_subReturnType, a_subChildType);
  }
}
