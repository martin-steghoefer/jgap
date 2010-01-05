/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.audit;

import java.io.*;

import org.jgap.util.*;

/**
 * A (key, value) tupel.
 *
 * @author Klaus Meffert
 * @since 2.3
 */
public class KeyedValue
    implements ICloneable, Serializable {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.5 $";

  private Comparable m_key;

  private Number m_value;

  /**
   * Creates a new (key, value) tupel.
   *
   * @param a_key the key
   * @param a_value the value, could be null
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public KeyedValue(final Comparable a_key, final Number a_value) {
    m_key = a_key;
    m_value = a_value;
  }

  /**
   * @return key of the tupel
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public Comparable getKey() {
    return m_key;
  }

  /**
   * @return value of the tupel
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public synchronized Number getValue() {
    return m_value;
  }

  /**
   * Sets the value for the key
   *
   * @param a_value the value to set for the key
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public synchronized void setValue(final Number a_value) {
    m_value = a_value;
  }

  /**
   * Tests if this object is equal to another.
   *
   * @param a_object the other object
   *
   * @return true: this object is equal to other one
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public boolean equals(final Object a_object) {
    if (this == a_object) {
      return true;
    }
    if (! (a_object instanceof KeyedValue)) {
      return false;
    }
    final KeyedValue defaultKeyedValue = (KeyedValue) a_object;
    if (m_key != null ? !m_key.equals(defaultKeyedValue.m_key)
        : defaultKeyedValue.m_key != null) {
      return false;
    }
    if (m_value != null ? !m_value.equals(defaultKeyedValue.m_value)
        : defaultKeyedValue.m_value != null) {
      return false;
    }
    return true;
  }

  /**
   * @return hash code of the instance
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public int hashCode() {
    int result;
    if (m_key == null) {
      result = -37;
    }
    else {
      result = m_key.hashCode();
    }
    result = 41 * result;
    if (m_value == null) {
      result += -3;
    }
    else {
      result += m_value.hashCode();
    }
    return result;
  }

  /**
   * @return clone of the current instance
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public Object clone() {
    try {
      KeyedValue clone = (KeyedValue)super.clone();
      return clone;
    } catch (CloneNotSupportedException cex) {
      throw new CloneException(cex);
    }
  }
}
