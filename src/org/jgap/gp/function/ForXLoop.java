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
import org.jgap.gp.terminal.*;
import org.jgap.util.*;

/**
 * The for-loop loop from 0 to X-1.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class ForXLoop
    extends CommandGene implements ICloneable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.12 $";

  private Class m_type;

  public ForXLoop(final GPConfiguration a_conf, Class a_type)
      throws InvalidConfigurationException {
    super(a_conf, 1, CommandGene.VoidClass);
    m_type = a_type;
  }

  public String toString() {
    return "for(int i=0;i<X;i++) { &1 }";
  }

  /**
   * @return textual name of this command
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public String getName() {
    return "ForXLoop";
  }

  public void execute_void(ProgramChromosome c, int n, Object[] args) {
    check(c);
    int index = c.getVariableWithReturnType(0, m_type);
    if (index < 0) {
      throw new IllegalStateException("Variable missing for forX");
    }
    /**@todo only consider variables appearing before FORX in the program tree*/
    Variable var = (Variable) c.getNode(index);
    int x;
    // Get variable's value as integer ("for" cannot do anything else here).
    // ---------------------------------------------------------------------
    if (m_type == CommandGene.IntegerClass) {
      x = ( (Integer) var.getValue()).intValue();
    }
    else if (m_type == CommandGene.LongClass) {
      x = ( (Long) var.getValue()).intValue();
    }
    else if (m_type == CommandGene.DoubleClass) {
      x = ( (Double) var.getValue()).intValue();
    }
    else if (m_type == CommandGene.FloatClass) {
      x = ( (Float) var.getValue()).intValue();
    }
    else {
      throw new RuntimeException("Type " + m_type + " unknown in ForXCommand");
    }
    if (x > 15) {
      x = 15;
    }
    for (int i = 0; i < x; i++) {
      c.execute_void(n, 0, args);
    }
  }

  public boolean isValid(ProgramChromosome a_program) {
    return a_program.getVariableWithReturnType(0, m_type) >= 0;
  }

  public Class getChildType(IGPProgram a_ind, int a_chromNum) {
    return CommandGene.VoidClass;
  }

  public Class getReturnType() {
    return super.getReturnType();
  }

  /**
   * The compareTo-method.
   * @param a_other the other object to compare
   * @return -1, 0, 1
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public int compareTo(Object a_other) {
    int result = super.compareTo(a_other);
    if (result != 0) {
      return result;
    }
    ForXLoop other = (ForXLoop) a_other;
    return new CompareToBuilder()
        .append(m_type, other.m_type)
        .toComparison();
  }

  /**
   * The equals-method.
   * @param a_other the other object to compare
   * @return true if the objects are seen as equal
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public boolean equals(Object a_other) {
    try {
      ForXLoop other = (ForXLoop) a_other;
      return super.equals(a_other) && new EqualsBuilder()
          .append(m_type, other.m_type)
          .append(size(), other.size())
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
      ForXLoop result = new ForXLoop(getGPConfiguration(), m_type);
      return result;
    } catch (Exception ex) {
      throw new CloneException(ex);
    }
  }
}
