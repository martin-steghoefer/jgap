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
 * A connector for indipendent subprograms (subtrees). Each subtree except the
 * last one must have a memory- or stack-modifying command (such as push or
 * store), otherwise there is no connection between the subtrees (which would
 * be useless bloating).
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class SubProgram
    extends MathCommand {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  /**
   * Number of subprograms.
   */
  private int m_subtrees;

  private Class[] m_types;

  public SubProgram(final Configuration a_conf, Class[] a_types)
      throws InvalidConfigurationException {
    super(a_conf, a_types.length, a_types[a_types.length-1]);
    if (a_types.length < 1) {
      throw new IllegalArgumentException("Number of subtrees must be >= 1");
    }
    m_types = a_types;
    m_subtrees = a_types.length;
  }

  protected Gene newGeneInternal() {
    try {
      Gene gene = new SubProgram(getConfiguration(), m_types);
      return gene;
    }
    catch (InvalidConfigurationException iex) {
      throw new IllegalStateException(iex.getMessage());
    }
  }

  public String toString() {
    String ret = "sub[";
    for (int i = 1; i < m_subtrees; i++) {
      ret += "&" + i + " --> ";
    }
    ret += "&" + m_subtrees + "]";
    return ret;
  }

  public int execute_int(ProgramChromosome c, int n, Object[] args) {
    check(c);
//    if (n > 0) {
//      /**@todo make following dynamic resp. add a parameter to the constructor*/
//      throw new IllegalStateException("Subprogram must be the root");
//    }
    int value = -1;
    for (int i = 0; i < m_subtrees; i++) {
      if (i < m_subtrees - 1) {
        c.execute_void(n, i, args);
      }
      else {
        value = c.execute_int(n, i, args);/**@todo evaluate m_types*/
      }
//      if (i < m_subtrees - 1) {
//        ( (GPConfiguration) getConfiguration()).storeThruput(i,
//            new Integer(value));
//      }
    }
    return value;
  }

  public void execute_void(ProgramChromosome c, int n, Object[] args) {
    check(c);
    for (int i = 0; i < m_subtrees; i++) {
      c.execute_void(n, i, args);
    }
  }

  public long execute_long(ProgramChromosome c, int n, Object[] args) {
    check(c);
    long value = -1;
    for (int i = 0; i < m_subtrees; i++) {
      value = c.execute_long(n, i, args);
    }
    return value;
  }

  public float execute_float(ProgramChromosome c, int n, Object[] args) {
    check(c);
    float value = -1;
    for (int i = 0; i < m_subtrees; i++) {
      value = c.execute_float(n, i, args);
    }
    return value;
  }

  public double execute_double(ProgramChromosome c, int n, Object[] args) {
    check(c);
    double value = -1;
    for (int i = 0; i < m_subtrees; i++) {
      value = c.execute_double(n, i, args);
    }
    return value;
  }

  public Object execute_object(ProgramChromosome c, int n, Object[] args) {
    check(c);
    Object value = null;
    for (int i = 0; i < m_subtrees; i++) {
      value = c.execute_object(n, i, args);
    }
    return value;
  }

  public boolean isValid(ProgramChromosome a_program) {
    if (a_program.getIndividual() != a_program.getIndividual()) {
      int x = 2;
    }
    return true;
  }

  public Class getChildType(GPProgram a_ind, int a_chromNum) {
    return m_types[a_chromNum];
  }
}
