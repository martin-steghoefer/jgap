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
 * The switch construct:
 *   if <condition>
 *     then return <result of command 1>
 *     else return <result of command 2>
 *
 * @author Klaus Meffert, Johannes Goebel
 * @since 3.6
 */
public class Switch
    extends MathCommand implements ICloneable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  private Class m_type;

  public Switch(final GPConfiguration a_conf, Class a_type)
      throws InvalidConfigurationException {
    super(a_conf, 3, CommandGene.FloatClass);
    m_type = a_type;
  }

  public String toString() {
    return "(&1 ? &2 : &3)";
  }

  /**
   * @return textual name of this command
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public String getName() {
    return "Switch";
  }

  public float execute_float(ProgramChromosome c, int n, Object[] args) {
    boolean condition = true;
    try {
      condition = c.execute_boolean(n, 0, args);
    } catch (UnsupportedOperationException u) {
      throw new IllegalStateException(
          "Switch: cannot process boolean condition");
    }
    return condition ? c.execute_float(n, 1, args) : c.execute_float(n, 2, args);
  }

  public double execute_double(ProgramChromosome c, int n, Object[] args) {
    boolean condition = true;
    try {
      condition = c.execute_boolean(n, 0, args);
    } catch (UnsupportedOperationException u) {
      throw new IllegalStateException(
          "Switch: cannot process boolean condition");
    }
    return condition ? c.execute_double(n, 1, args) :
        c.execute_double(n, 2, args);
  }

  public boolean execute_boolean(ProgramChromosome c, int n, Object[] args) {
    boolean condition = true;
    try {
      condition = c.execute_boolean(n, 0, args);
    } catch (UnsupportedOperationException u) {
      throw new IllegalStateException(
          "Switch: cannot process boolean condition");
    }
    return condition ? c.execute_boolean(n, 1, args) :
        c.execute_boolean(n, 2, args);
  }

  public int execute_int(ProgramChromosome c, int n, Object[] args) {
    boolean condition = true;
    try {
      condition = c.execute_boolean(n, 0, args);
    } catch (UnsupportedOperationException u) {
      throw new IllegalStateException(
          "Switch: cannot process boolean condition");
    }
    return condition ? c.execute_int(n, 1, args) : c.execute_int(n, 2, args);
  }

  public long execute_long(ProgramChromosome c, int n, Object[] args) {
    boolean condition = true;
    try {
      condition = c.execute_boolean(n, 0, args);
    } catch (UnsupportedOperationException u) {
      throw new IllegalStateException(
          "Switch: cannot process boolean condition");
    }
    return condition ? c.execute_long(n, 1, args) : c.execute_long(n, 2, args);
  }

  public Object execute_object(ProgramChromosome c, int n, Object[] args) {
    boolean condition = true;
    try {
      condition = c.execute_boolean(n, 0, args);
    } catch (UnsupportedOperationException u) {
      throw new IllegalStateException(
          "Switch: cannot process boolean condition");
    }
    return condition ? c.execute_object(n, 1, args) :
        c.execute_object(n, 2, args);
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
    if (a_chromNum == 0)
      return CommandGene.BooleanClass;
    else
      return m_type;
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
      Switch result = new Switch(getGPConfiguration(), this.m_type);
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
    if (a_other == null || ! (a_other instanceof Switch)) {
      return false;
    }
    if (!super.equals(a_other)) {
      return false;
    }
    Switch other = (Switch) a_other;
    return new EqualsBuilder().append(this.m_type, other.m_type).isEquals();
  }
}
