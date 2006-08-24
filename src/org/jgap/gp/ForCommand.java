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
import org.jgap.gp.*;

/**
 * The for-loop.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class ForCommand
    extends MathCommand {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.4 $";

  private Class m_typeVar;

  public ForCommand(final Configuration a_conf, Class a_typeVar)
      throws InvalidConfigurationException {
    super(a_conf, 2, CommandGene.VoidClass);
    m_typeVar = a_typeVar;
  }

  protected Gene newGeneInternal() {
    return null;/**@todo implement if necessary*/
  }

  public String toString() {
    return "for(int i=0;i<&1;i++) { &1 }";
  }

  public void execute_void(ProgramChromosome c, int n, Object[] args) {
    int x = c.execute_int(n, 0, args);/**@todo consider m_typeVar*/
    if (x > 15) {
      x = 15;/**@todo parameterize*/
    }
    for (int i = 0; i < x; i++) {
      c.execute_void(n, 1, args);
    }
  }

//  public int execute_int(ProgramChromosome c, int n, Object[] args) {
//    int x = c.execute_int(n, 0, args);
//    if (x > 15) {
//      x = 15;/**@todo parameterize*/
//    }
//    int value = 0;
//    for (int i = 0; i < x; i++) {
//      value = c.execute_int(n, 1, args);
//    }
//    return value;
//  }

  public boolean isValid(ProgramChromosome a_program) {
    return true;
  }

  public Class getChildType(int i) {
    if (i == 0) {
      // Loop counter variable
      return m_typeVar;
    }
    else {
      // Subprogram
      return CommandGene.VoidClass;
    }
  }
}
