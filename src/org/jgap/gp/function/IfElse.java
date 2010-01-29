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
 * The if-then-else construct.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class IfElse
    extends CommandGene implements ICloneable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.14 $";

  private Class m_type;

  public IfElse(final GPConfiguration a_conf, Class a_type)
      throws InvalidConfigurationException {
    this(a_conf, a_type, 0, null);
  }

  public IfElse(final GPConfiguration a_conf, Class a_type, int a_subReturnType,
                int[] a_subChildTypes)
      throws InvalidConfigurationException {
    super(a_conf, 3, CommandGene.VoidClass, a_subReturnType, a_subChildTypes);
    m_type = a_type;
  }

  public String toString() {
    return "if(&1) then (&2) else(&3)";
  }

  /**
   * @return textual name of this command
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public String getName() {
    return "IfElse";
  }

  public boolean execute_boolean(ProgramChromosome c, int n, Object[] args) {
    check(c);
    boolean condition;
    if (m_type == CommandGene.IntegerClass) {
      condition = c.execute_int(n, 0, args) > 0;
    }
    else if (m_type == CommandGene.BooleanClass) {
      condition = c.execute_boolean(n, 0, args);
    }
    else if (m_type == CommandGene.LongClass) {
      condition = c.execute_long(n, 0, args) > 0;
    }
    else if (m_type == CommandGene.DoubleClass) {
      condition = c.execute_double(n, 0, args) > 0;
    }
    else if (m_type == CommandGene.FloatClass) {
      condition = c.execute_float(n, 0, args) > 0;
    }
    else {
      throw new IllegalStateException("IfElse: cannot process type " + m_type);
    }
    if (condition) {
      return c.execute_boolean(n, 1, args);
    }
    else {
      return c.execute_boolean(n, 2, args);
    }
  }

  public int execute_int(ProgramChromosome c, int n, Object[] args) {
    check(c);
    boolean condition;
    if (m_type == CommandGene.IntegerClass) {
      condition = c.execute_int(n, 0, args) > 0;
    }
    else if (m_type == CommandGene.BooleanClass) {
      condition = c.execute_boolean(n, 0, args);
    }
    else if (m_type == CommandGene.LongClass) {
      condition = c.execute_long(n, 0, args) > 0;
    }
    else if (m_type == CommandGene.DoubleClass) {
      condition = c.execute_double(n, 0, args) > 0;
    }
    else if (m_type == CommandGene.FloatClass) {
      condition = c.execute_float(n, 0, args) > 0;
    }
    else {
      throw new IllegalStateException("IfElse: cannot process type " + m_type);
    }
    if (condition) {
      return c.execute_int(n, 1, args);
    }
    else {
      return c.execute_int(n, 2, args);
    }
  }

   public void execute_void(ProgramChromosome c, int n, Object[] args) {
    check(c);
    boolean condition;
    if (m_type == CommandGene.IntegerClass) {
      condition = c.execute_int(n, 0, args) > 0;
    }
    else if (m_type == CommandGene.BooleanClass) {
      condition = c.execute_boolean(n, 0, args);
    }
    else if (m_type == CommandGene.LongClass) {
      condition = c.execute_long(n, 0, args) > 0;
    }
    else if (m_type == CommandGene.DoubleClass) {
      condition = c.execute_double(n, 0, args) > 0;
    }
    else if (m_type == CommandGene.FloatClass) {
      condition = c.execute_float(n, 0, args) > 0;
    }
    else {
      throw new IllegalStateException("IfElse: cannot process type " + m_type);
    }
    if (condition) {
      c.execute_void(n, 1, args);
    }
    else {
      c.execute_void(n, 2, args);
    }
  }

  /**
   * Determines which type a specific child of this command has.
   *
   * @param a_ind ignored here
   * @param a_chromNum index of child
   * @return type of the a_chromNum'th child
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public Class getChildType(IGPProgram a_ind, int a_chromNum) {
    if (a_chromNum == 0) {
      return m_type;
    }
    return CommandGene.VoidClass;
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
      IfElse result = new IfElse(getGPConfiguration(), m_type,
                                 getSubReturnType(), getSubChildTypes());
      return result;
    } catch (Exception ex) {
      throw new CloneException(ex);
    }
  }

  /**
   * The equals method.
   *
   * @param a_other the other object to compare
   * @return true if the objects are seen as equal
   *
   * @author Frank Pape
   * @since 3.4.3
   */
  public boolean equals(Object a_other) {
    if (a_other == null || ! (a_other instanceof IfElse)) {
      return false;
    }
    if (!super.equals(a_other)) {
      return false;
    }
    IfElse other = (IfElse) a_other;
    return new EqualsBuilder().append(m_type, other.m_type).isEquals();
  }
}
