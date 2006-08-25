/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp.function;

import org.jgap.*;
import org.jgap.gp.*;

/**
 * The for-loop.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class ForLoop
    extends MathCommand {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  private Class m_typeVar;

  private int m_maxLoop;

  /**
   *
   * @param a_conf the configuration to use
   * @param a_typeVar Class of the loop counter terminakl (e.g. IntegerClass)
   * @param a_maxLoop the maximum number of loops to perform
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public ForLoop(final Configuration a_conf, Class a_typeVar, int a_maxLoop)
      throws InvalidConfigurationException {
    super(a_conf, 2, CommandGene.VoidClass);
    m_typeVar = a_typeVar;
    m_maxLoop = a_maxLoop;
  }

  protected Gene newGeneInternal() {
    return null; /**@todo implement if necessary*/
  }

  public String toString() {
    return "for(int i=0;i<&1;i++) { &2 }";
  }

  public void execute_void(ProgramChromosome c, int n, Object[] args) {
    int x;
    if (m_typeVar == CommandGene.IntegerClass) {
      x = c.execute_int(n, 0, args);
    }
    else if (m_typeVar == CommandGene.LongClass) {
      x = (int) c.execute_long(n, 0, args);
    }
    else if (m_typeVar == CommandGene.DoubleClass) {
      x = (int) Math.round(c.execute_double(n, 0, args));
    }
    else if (m_typeVar == CommandGene.FloatClass) {
      x = (int) Math.round(c.execute_float(n, 0, args));
    }
    else {
      throw new RuntimeException("Type " + m_typeVar +
                                 " not supported by ForLoop");
    }
    if (x > m_maxLoop) {
      x = m_maxLoop;
    }
    for (int i = 0; i < x; i++) {
      c.execute_void(n, 1, args);
    }
  }

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
