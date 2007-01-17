/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
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
    implements Serializable, ICloneable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.12 $";

  /**
   * Ordered list holding the NaturalSelector's.
   * Intentionally used as a decorator and not via inheritance!
   */
  private Vector m_selectors;

  public ChainOfSelectors() {
    m_selectors = new Vector();
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
    }
    catch (ClassCastException cex) {
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
    ChainOfSelectors result = new ChainOfSelectors();
    result.m_selectors = (Vector)m_selectors.clone();
    return result;
  }

}
