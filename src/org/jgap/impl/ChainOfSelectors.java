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

import java.util.*;

import org.jgap.*;

/**
 * Ordered chain of NaturalSelectors. With this container you can plugin
 * NaturalSelector implementations which will be performed either before (pre-)
 * or after (post-selectors) registered genetic operations have been applied.
 * @see Genotype
 * @see Configuration
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class ChainOfSelectors {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.8 $";

  /**
   * Ordered list holding the NaturalSelector's.
   * Intentionally used as a decorator and not via inheritance!
   */
  private List selectors;

  public ChainOfSelectors() {
    selectors = new Vector();
  }

  /**
   * Adds a natural selector to the chain
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
    selectors.add(a_selector);
  }

  /**
   *
   * @param c Collection
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void addAll(Collection c)
      throws InvalidConfigurationException {
    Iterator it = c.iterator();
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
    return selectors.size();
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
    return selectors.hashCode();
  }

  /**
   *
   * @param o Object
   * @return boolean
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public boolean equals(Object o) {
    return selectors.equals(o);
  }

  /**
   * Returns a Selector with specific index in the list
   * @param index the index of the Selector to read from the list
   * @return NaturalSelector
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public NaturalSelector get(int index) {
    return (NaturalSelector) selectors.get(index);
  }

  /**
   *
   * @author Klaus Meffert
   * @since 1.1
   *
   */
  public void clear() {
    selectors.clear();
  }

  /**
   * @return Iterator for iterating over list of selectors
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public Iterator iterator() {
    return selectors.iterator();
  }
}
