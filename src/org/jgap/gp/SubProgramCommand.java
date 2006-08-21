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
 * A connector for indipendent subprograms (subtrees). Each subtree except the
 * last one must have a memory- or stak-modifying command (such as push or
 * store), otherwise there is no connection between the subtrees (which would
 * be useless bloating).
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class SubProgramCommand
    extends MathCommand {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  /**
   * Number of subprograms.
   */
  private int m_subtrees;

  public SubProgramCommand(final Configuration a_conf, Class type,
                           int a_subtrees)
      throws InvalidConfigurationException {
    super(a_conf, a_subtrees, type);
    m_subtrees = a_subtrees;
  }

  protected Gene newGeneInternal() {
    try {
      Gene gene = new SubProgramCommand(getConfiguration(), getReturnType(),
                                        m_subtrees);
      return gene;
    }
    catch (InvalidConfigurationException iex) {
      throw new IllegalStateException(iex.getMessage());
    }
  }

  public String toString() {
    String ret = "";
    for (int i = 1; i < m_subtrees; i++) {
      ret += "&" + i + " --> ";
    }
    ret += "&" + m_subtrees;
    return ret;
  }

  public int execute_int(ProgramChromosome c, int n, Object[] args) {
    int value = -1;
    for (int i = 0; i < m_subtrees; i++) {
      value = c.execute_int(n, i, args);
    }
    return value;
  }

  public long execute_long(ProgramChromosome c, int n, Object[] args) {
    long value = -1;
    for (int i = 0; i < m_subtrees; i++) {
      value = c.execute_long(n, i, args);
    }
    return value;
  }

  public float execute_float(ProgramChromosome c, int n, Object[] args) {
    float value = -1;
    for (int i = 0; i < m_subtrees; i++) {
      value = c.execute_float(n, i, args);
    }
    return value;
  }

  public double execute_double(ProgramChromosome c, int n, Object[] args) {
    double value = -1;
    for (int i = 0; i < m_subtrees; i++) {
      value = c.execute_double(n, i, args);
    }
    return value;
  }

  public Object execute_object(ProgramChromosome c, int n, Object[] args) {
    Object value = null;
    for (int i = 0; i < m_subtrees; i++) {
      value = c.execute_object(n, i, args);
    }
    return value;
  }

  public static interface Compatible {
    public Object execute_subprogram(Object o);
  }
}
