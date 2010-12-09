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

import org.apache.commons.lang.builder.*;
import org.jgap.*;
import org.jgap.gp.*;
import org.jgap.gp.impl.*;
import org.jgap.util.*;

/**
 * A loop that executes a given number of times.
 *
 * @author Klaus Meffert
 * @since 3.01
 */
public class Loop
    extends CommandGene implements ICloneable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.8 $";

  private Class m_typeVar;

  private int m_count;

  /**
   * Constructor.
   *
   * @param a_conf the configuration to use
   * @param a_typeVar Class of the loop counter terminal (e.g. IntegerClass)
   * @param a_count the number of loops to perform
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.01
   */
  public Loop(final GPConfiguration a_conf, Class a_typeVar, int a_count)
      throws InvalidConfigurationException {
    this(a_conf, a_typeVar, a_count, 0, 0);
  }

  public Loop(final GPConfiguration a_conf, Class a_typeVar, int a_count,
              int a_subReturnType, int a_subChildType)
      throws InvalidConfigurationException {
    super(a_conf, 1, CommandGene.VoidClass, a_subReturnType, a_subChildType);
    m_typeVar = a_typeVar;
    m_count = a_count;
  }

  public String toString() {
    return "loop(" + m_count + ", &1 )";
  }

  /**
   * @return textual name of this command
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public String getName() {
    return "Loop";
  }

  public void execute_void(ProgramChromosome c, int n, Object[] args) {
    // Repeatedly execute the child.
    // -----------------------------
    for (int i = 0; i < m_count; i++) {
      c.execute_void(n, 0, args);
    }
  }

  public boolean isValid(ProgramChromosome a_program) {
    return true;
  }

  public Class getChildType(IGPProgram a_ind, int a_chromNum) {
    return CommandGene.VoidClass;
  }

  /**
   * The compareTo-method.
   *
   * @param a_other the other object to compare
   * @return -1, 0, 1
   *
   * @author Klaus Meffert
   * @since 3.01
   */
  public int compareTo(Object a_other) {
    int result = super.compareTo(a_other);
    if (result != 0) {
      return result;
    }
    Loop other = (Loop) a_other;
    return new CompareToBuilder()
        .append(m_typeVar, other.m_typeVar)
        .append(m_count, other.m_count)
        .toComparison();
  }

  /**
   * The equals-method.
   *
   * @param a_other the other object to compare
   * @return true if the objects are seen as equal
   *
   * @author Klaus Meffert
   * @since 3.01
   */
  public boolean equals(Object a_other) {
    try {
      Loop other = (Loop) a_other;
      return super.equals(a_other) && new EqualsBuilder()
          .append(m_typeVar, other.m_typeVar)
          .append(m_count, other.m_count)
          .isEquals();
    } catch (ClassCastException cex) {
      return false;
    }
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
      Loop result = new Loop(getGPConfiguration(), m_typeVar, m_count,
                             getSubReturnType(), getSubChildType(0));
      return result;
    } catch (Exception ex) {
      throw new CloneException(ex);
    }
  }

}
