/*
 * This file is part of JGAP.
 *
 * JGAP is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * JGAP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser Public License
 * along with JGAP; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package org.jgap.impl;

import java.util.*;

import org.jgap.*;

/**
 * Ordered chain of NaturalSelectors. With this container you can plugin
 * NaturalSelector implementations which will be performed either before (pre-)
 * or after (post-selectors) registered genetic operations have been applied.
 * @see Genotype.evolve
 * @see Configuration.addNaturalSelector
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class ChainOfSelectors {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.5 $";

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
      throws
      InvalidConfigurationException {
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
