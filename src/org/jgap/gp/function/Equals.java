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
 * The equals operation.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class Equals
    extends MathCommand implements ICloneable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.9 $";

  private Class m_type;

  public Equals(final GPConfiguration a_conf, Class a_type)
      throws InvalidConfigurationException {
    this(a_conf, a_type, 0, null);
  }

  public Equals(final GPConfiguration a_conf, Class a_type, int a_subReturnType,
                int[] a_subChildTypes)
      throws InvalidConfigurationException {
    super(a_conf, 2, CommandGene.BooleanClass, a_subReturnType, a_subChildTypes);
    m_type = a_type;
  }

  public String toString() {
    return "Equals(&1, &2)";
  }

  /**
   * @return textual name of this command
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public String getName() {
    return "Equals";
  }

  public boolean execute_boolean(ProgramChromosome c, int n, Object[] args) {
    if (m_type == CommandGene.BooleanClass) {
      return c.execute_boolean(n, 0, args) == c.execute_boolean(n, 1, args);
    }
    else if (m_type == CommandGene.IntegerClass) {
      return c.execute_int(n, 0, args) == c.execute_int(n, 1, args);
    }
    else if (m_type == CommandGene.LongClass) {
      return c.execute_long(n, 0, args) == c.execute_long(n, 1, args);
    }
    else if (m_type == CommandGene.DoubleClass) {
      return Math.abs(c.execute_double(n, 0, args) -
                      c.execute_double(n, 1, args)) < DELTA;
    }
    else if (m_type == CommandGene.FloatClass) {
      return Math.abs(c.execute_float(n, 0, args) -
                      c.execute_float(n, 1, args)) < DELTA;
    }
    else if (m_type == CommandGene.VoidClass) {
      return c.execute_object(n, 0, args).equals(c.execute_object(n, 1, args));
    }
    throw new UnsupportedOperationException("Unsupported type " + m_type +
        " for Equals-command!");
  }

  public Class getChildType(IGPProgram a_ind, int a_index) {
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
      Equals result = new Equals(getGPConfiguration(), m_type,
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
    if (a_other == null || ! (a_other instanceof Equals)) {
      return false;
    }
    if (!super.equals(a_other)) {
      return false;
    }
    Equals other = (Equals) a_other;
    return new EqualsBuilder().append(m_type, other.m_type).isEquals();
  }
}
