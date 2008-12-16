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
 * NO Operation. Does exactly nothing. Useful when a terminal with return type
 * void is needed.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class NOP
    extends MathCommand {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.9 $";

  public NOP(final GPConfiguration a_conf)
      throws InvalidConfigurationException {
    this(a_conf, 0);
  }

  public NOP(final GPConfiguration a_conf, int a_subReturnType)
      throws InvalidConfigurationException {
    this(a_conf, CommandGene.VoidClass, a_subReturnType);
  }

  public NOP(final GPConfiguration a_conf, Class a_returnType,
             int a_subReturnType)
      throws InvalidConfigurationException {
    super(a_conf, 0, a_returnType, a_subReturnType, null);
  }

  public String toString() {
    return "NOP";
  }

  /**
   * @return textual name of this command
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public String getName() {
    return "NOP (NO Operation)";
  }

  public void execute_void(ProgramChromosome c, int n, Object[] args) {
    // Do nothing here as it is NOP.
    // ----------------------------
    ;
  }

  /**
   * Executes this node as an object.
   *
   * @param c ignored here
   * @param n ignored here
   * @param args ignored here
   * @return nothing but exception
   * @throws UnsupportedOperationException
   *
   * @author Klaus Meffert
   * @since 3.2.1
   */
  public Object execute_object(ProgramChromosome c, int n, Object[] args) {
    return new StringBuffer(";");
  }
}
