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
import java.util.*;
import org.jgap.util.*;

/**
 * A collection of (key, value) tupels
 *
 * @author Klaus Meffert
 * @since 2.3
 */
public class KeyedValues
    implements ICloneable, Serializable {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.7 $";

  /** Data storage */
  private List m_data;

  /**
   * Creates a new collection (initially empty).
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public KeyedValues() {
    m_data = Collections.synchronizedList(new ArrayList());
  }

  /**
   * @return number of items in the collection
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public int size() {
    return m_data.size();
  }

  /**
   * @param a_index the index of the item to return the value for
   * @return the value at given index
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public Number getValue(final int a_index) {
    Number result;
    final KeyedValue kval = (KeyedValue) m_data.get(a_index);
    if (kval != null) {
      result = kval.getValue();
    }
    else {
      result = null;
    }
    return result;
  }

  /**
   * @param a_index the item index to retrieve the key for, starting at 0
   *
   * @return the row key for item at given index
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public Comparable getKey(final int a_index) {
    Comparable result;
    final KeyedValue item = (KeyedValue) m_data.get(a_index);
    if (item != null) {
      result = item.getKey();
    }
    else {
      result = null;
    }
    return result;
  }

  /**
   * @param a_key the key to search for
   *
   * @return index for a given key or -1 if the key is not found
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public int getIndex(final Comparable a_key) {
    int i = 0;
    final Iterator iterator = m_data.iterator();
    while (iterator.hasNext()) {
      final KeyedValue kv = (KeyedValue) iterator.next();
      if (kv.getKey() != null) {
        if (kv.getKey().equals(a_key)) {
          return i;
        }
      }
      else {
        if (a_key == null) {
          return i;
        }
      }
      i++;
    }
    // key not found
    return -1;
  }

  /**
   * Returns the keys for the values in the collection
   *
   * @return The keys (never null)
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public List getKeys() {
    final List result = new java.util.ArrayList();
    final Iterator iterator = m_data.iterator();
    while (iterator.hasNext()) {
      final KeyedValue kv = (KeyedValue) iterator.next();
      result.add(kv.getKey());
    }
    return result;
  }

  /**
   * Returns the value for a given key.  For unknown keys, null is returned
   *
   * @param a_key the key
   *
   * @return the value for the key
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public Number getValue(final Comparable a_key) {
    Number result;
    final int index = getIndex(a_key);
    if (index >= 0) {
      result = getValue(index);
    }
    else {
      result = null;
    }
    return result;
  }

  /**
   * Updates an existing value, or adds a new value to the collection
   *
   * @param a_key the key
   * @param a_value the value
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public void setValue(final Comparable a_key, final Number a_value) {
    final int keyIndex = getIndex(a_key);
    if (keyIndex >= 0) {
      final KeyedValue kv = (KeyedValue) m_data.get(keyIndex);
      kv.setValue(a_value);
    }
    else {
      final KeyedValue kv = new KeyedValue(a_key, a_value);
      m_data.add(kv);
    }
  }

  /**
   * Tests if this object is equal to another
   *
   * @param a_obj the other object
   *
   * @return true: this object is equal to the other one
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public boolean equals(final Object a_obj) {
    if (a_obj == null) {
      return false;
    }
    if (a_obj == this) {
      return true;
    }
    if (! (a_obj instanceof KeyedValues)) {
      return false;
    }
    final KeyedValues kvs = (KeyedValues) a_obj;
    final int count = size();
    if (count != kvs.size()) {
      return false;
    }
    for (int i = 0; i < count; i++) {
      final Comparable k1 = getKey(i);
      final Comparable k2 = kvs.getKey(i);
      if (!k1.equals(k2)) {
        return false;
      }
      final Number v1 = getValue(i);
      final Number v2 = kvs.getValue(i);
      if (v1 == null) {
        if (v2 != null) {
          return false;
        }
      }
      else {
        if (!v1.equals(v2)) {
          return false;
        }
      }
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
    if (m_data.size() == 0) {
      return -29;
    }
    else {
      return m_data.hashCode();
    }
  }

  /**
   * @return clone of the current instance
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public Object clone() {
    try {
      final KeyedValues clone = (KeyedValues)super.clone();
      clone.m_data = Collections.synchronizedList(new ArrayList());
      final Iterator iterator = m_data.iterator();
      while (iterator.hasNext()) {
        final KeyedValue kv = (KeyedValue) iterator.next();
        clone.m_data.add(kv.clone());
      }
      return clone;
    } catch (CloneNotSupportedException cex) {
      throw new CloneException(cex);
    }
  }
}
