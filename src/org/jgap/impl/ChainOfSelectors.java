/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.impl;

import java.io.*;
import java.util.*;
import org.jgap.*;
import org.jgap.util.*;

/**
 * Ordered chain of NaturalSelectors. With this container you can plugin
 * NaturalSelector implementations which will be performed either before (pre-)
 * or after (post-selectors) registered genetic operations have been applied.
 *
 * @see Configuration
 * @see Genotype
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class ChainOfSelectors
    implements Serializable, ICloneable, Comparable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.19 $";

  /**
   * Ordered list holding the NaturalSelector's.
   * Intentionally used as a decorator and not via inheritance!
   */
  private List m_selectors;

  private Configuration m_conf;

  /**
   * Only for dynamic instantiation.
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public ChainOfSelectors() {
    this(Genotype.getStaticConfiguration());
  }

  public ChainOfSelectors(Configuration a_conf) {
    m_selectors = new Vector();
    m_conf = a_conf;
  }

  /**
   * Adds a natural selector to the chain.
   *
   * @param a_selector the selector to be added
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 1.1 (previously part of class Configuration)
   */
  public void addNaturalSelector(NaturalSelector a_selector)
      throws InvalidConfigurationException {
    if (a_selector == null) {
      throw new InvalidConfigurationException(
          "This Configuration object is locked. Settings may not be " +
          "altered.");
    }
    m_selectors.add(a_selector);
  }

  /**
   *
   * @param a_c Collection to add all elements from
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void addAll(Collection a_c)
      throws InvalidConfigurationException {
    Iterator it = a_c.iterator();
    while (it.hasNext()) {
      NaturalSelector selector = (NaturalSelector) it.next();
      addNaturalSelector(selector);
    }
  }

  /**
   * @return number of selectors in list
   *
   * @author Klaus Meffert
   * @since 1.1 (previously part of class Configuration)
   */
  public int size() {
    return m_selectors.size();
  }

  /**
   * @return true if number of selectors is zero
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public boolean isEmpty() {
    return size() == 0;
  }

  public int hashCode() {
    return m_selectors.hashCode();
  }

  /**
   *
   * @param a_obj Object
   * @return boolean
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public boolean equals(final Object a_obj) {
    try {
      ChainOfSelectors c2 = (ChainOfSelectors) a_obj;
      if (c2 == null) {
        return false;
      }
      return m_selectors.equals(c2.m_selectors);
    } catch (ClassCastException cex) {
      return false;
    }
  }

  /**
   * Returns a Selector with specific index in the list.
   *
   * @param a_index the index of the Selector to read from the list
   * @return NaturalSelector
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public NaturalSelector get(final int a_index) {
    return (NaturalSelector) m_selectors.get(a_index);
  }

  /**
   * Clears all registered selectors.
   *
   * @author Klaus Meffert
   * @since 1.1
   *
   */
  public void clear() {
    m_selectors.clear();
  }

  /**
   * @return Iterator for iterating over list of selectors
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public Iterator iterator() {
    return m_selectors.iterator();
  }

  /**
   * @return deep clone of this instance
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public Object clone() {
    try {
      ChainOfSelectors result = new ChainOfSelectors(m_conf);
      List v = new Vector();
      for (int i = 0; i < m_selectors.size(); i++) {
        INaturalSelector o = (INaturalSelector) m_selectors.get(i);
        Object clone;
        ICloneHandler handler = m_conf.getJGAPFactory().getCloneHandlerFor(
            o, null);
        if (handler != null) {
          clone = handler.perform(o, null, null);
        }
        else {
          throw new IllegalStateException("No clone handler found for class "
              + o.getClass().getName());
        }
        v.add(clone);
      }
      result.m_selectors = v;
      return result;
    } catch (Throwable t) {
      throw new CloneException(t);
    }
  }

  /**
   * The compareTo-method. Here we simply compare the class names of the
   * contained selectors as INaturalSelector does not contain interface
   * Comparable.
   *
   * @param a_other the other object to compare
   * @return -1, 0, 1
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public int compareTo(Object a_other) {
    if (a_other == null) {
      return 1;
    }
    else {
      ChainOfSelectors other = (ChainOfSelectors) a_other;
      int size = m_selectors.size();
      if (other.m_selectors.size() < size) {
        return 1;
      }
      if (other.m_selectors.size() > m_selectors.size()) {
        return -1;
      }
      for (int i = 0; i < size; i++) {
        // Normally we would do the following:
//        INaturalSelector selector = (INaturalSelector)m_selectors.get(i);
//        INaturalSelector selectorOther = (INaturalSelector)other.m_selectors.get(i);
//        int result = selector.compareTo(selectorOther);

        // But INaturalSelector does not support Cmparable, so:
        String name = m_selectors.get(i).getClass().getName();
        String nameOther = other.m_selectors.get(i).getClass().getName();
        int result = name.compareTo(nameOther);
        if (result != 0) {
          return result;
        }
      }
    }
    return 0;
  }
}
