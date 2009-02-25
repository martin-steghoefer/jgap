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
 * Pushes a value onto the stack.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class Push
    extends CommandGene implements ICloneable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.13 $";

  private Class m_type;

  public Push(final GPConfiguration a_conf, Class a_type)
      throws InvalidConfigurationException {
    this(a_conf, a_type, 0, 0);
  }

  public Push(final GPConfiguration a_conf, Class a_type, int a_subReturnType,
              int a_subChildType)
      throws InvalidConfigurationException {
    super(a_conf, 1, CommandGene.VoidClass, a_subReturnType, a_subChildType);
    m_type = a_type;
  }

  public String toString() {
    return "push &1";
  }

  /**
   * @return textual name of this command
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public String getName() {
    return "Push";
  }

  public void execute_void(ProgramChromosome c, int n, Object[] args) {
    check(c);
    int value = c.execute_int(n, 0, args);
    // Push onto stack.
    // ----------------
    pushIt(new Integer(value));
  }

  public int execute_int(ProgramChromosome c, int n, Object[] args) {
    check(c);
    int value = c.execute_int(n, 0, args);
    // Push onto stack.
    // ----------------
    pushIt(new Integer(value));
    return value;
  }

  public long execute_long(ProgramChromosome c, int n, Object[] args) {
    check(c);
    long value = c.execute_long(n, 0, args);
    // Push onto stack.
    // ----------------
    pushIt(new Long(value));
    return value;
  }

  public double execute_double(ProgramChromosome c, int n, Object[] args) {
    check(c);
    double value = c.execute_double(n, 0, args);
    pushIt(new Double(value));
    return value;
  }

  public float execute_float(ProgramChromosome c, int n, Object[] args) {
    check(c);
    float value = c.execute_float(n, 0, args);
    pushIt(new Float(value));
    return value;
  }

  public Object execute_object(ProgramChromosome c, int n, Object[] args) {
    check(c);
    Object value = c.execute_object(n, 0, args);
    pushIt(value);
    return value;
  }

  public boolean isAffectGlobalState() {
    return true; /**@todo use this information*/
  }

  public boolean isValid(ProgramChromosome a_program) {
    /**@todo consider n (execute_int...)*/
    return a_program.getCommandOfClass(0, Pop.class) >= 0;
  }

  /**
   * Helper method.
   * @param a_value the value to push onto the stack
   */
  protected void pushIt(Object a_value) {
    getGPConfiguration().pushToStack(a_value);
  }

  public Class getChildType(IGPProgram a_ind, int a_chromNum) {
    return m_type;
  }

  /**
   * The compareTo-method.
   *
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
    Push other = (Push) a_other;
    return new CompareToBuilder()
        .append(m_type, other.m_type)
        .toComparison();
  }

  /**
   * The equals-method.
   *
   * @param a_other the other object to compare
   * @return true if the objects are seen as equal
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public boolean equals(Object a_other) {
    try {
      Push other = (Push) a_other;
      return super.equals(a_other) && new EqualsBuilder()
          .append(m_type, other.m_type)
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
      Push result = new Push(getGPConfiguration(), m_type,
                             getSubReturnType(), getSubChildType(0));
      return result;
    } catch (Exception ex) {
      throw new CloneException(ex);
    }
  }
}
