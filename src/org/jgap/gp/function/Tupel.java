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
 * Holds a tupel of n values of arbitrary type. Useful to server as a return
 * type with more than one component.
 *
 * @author Klaus Meffert
 * @since 3.4.3
 */
public class Tupel
    extends CommandGene implements ICloneable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  private Class[] m_types;

  private Object[] m_values;

  public Tupel(final GPConfiguration a_conf, Class[] a_types)
      throws InvalidConfigurationException {
    this(a_conf, a_types, null);
  }

  public Tupel(final GPConfiguration a_conf, Class[] a_types,
               Class a_returnType)
      throws InvalidConfigurationException {
    super(a_conf, a_types.length, a_returnType, 0);
    m_types = a_types;
  }

  public String toString() {
    return "tupel(" + m_types + ")";
  }

  /**
   * @return textual name of this command
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public String getName() {
    return "Tupel";
  }

  public Object execute_object(ProgramChromosome c, int n, Object[] args) {
    int size = m_types.length;
    m_values = new Object[size];
    for (int i = 0; i < size; i++) {
      Object o = c.execute_object(n, i, args);
      m_values[i] = o;
    }
    return m_values;
  }

  /**
   * The compareTo-method.
   *
   * @param a_other the other object to compare
   * @return -1, 0, 1
   *
   * @author Klaus Meffert
   * @since 3.4.3
   */
  public int compareTo(Object a_other) {
    int result = super.compareTo(a_other);
    if (result != 0) {
      return result;
    }
    Tupel other = (Tupel) a_other;
    return new CompareToBuilder()
        .append(m_types, other.m_types)
        .toComparison();
  }

  /**
   * The equals-method.
   *
   * @param a_other the other object to compare
   * @return true if the objects are seen as equal
   *
   * @author Klaus Meffert
   * @since 3.4.3
   */
  public boolean equals(Object a_other) {
    try {
      Tupel other = (Tupel) a_other;
      return super.equals(a_other)
          && new EqualsBuilder()
          .append(m_types, other.m_types)
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
   * @since 3.4.3
   */
  public Object clone() {
    try {
      Tupel result = new Tupel(getGPConfiguration(), m_types, getReturnType());
      return result;
    } catch (Exception ex) {
      throw new CloneException(ex);
    }
  }
}
