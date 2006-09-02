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
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.jgap.gp.impl.*;

/**
 * A loop.
 *
 * @author Klaus Meffert
 * @since 3.01
 */
public class Loop
    extends CommandGene {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  private Class m_typeVar;

  private int m_count;

  /**
   * Constructor.
   *
   * @param a_conf the configuration to use
   * @param a_typeVar Class of the loop counter terminakl (e.g. IntegerClass)
   * @param a_count the number of loops to perform
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.01
   */
  public Loop(final GPConfiguration a_conf, Class a_typeVar, int a_count)
      throws InvalidConfigurationException {
    super(a_conf, 1, CommandGene.VoidClass);
    m_typeVar = a_typeVar;
    m_count = a_count;
  }

  public String toString() {
    return "loop(" + m_count + ", &1 }";
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
    if (a_other == null) {
      return 1;
    }
    else {
      Loop other = (Loop) a_other;
      return new CompareToBuilder()
          .append(m_typeVar, other.m_typeVar)
          .append(m_count, other.m_count)
          .toComparison();
    }
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
    if (a_other == null) {
      return false;
    }
    else {
      try {
        Loop other = (Loop) a_other;
        return new EqualsBuilder()
            .append(m_typeVar, other.m_typeVar)
            .append(m_count, other.m_count)
            .isEquals();
      } catch (ClassCastException cex) {
        return false;
      }
    }
  }
}
