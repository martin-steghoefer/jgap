/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.eval;

import java.io.*;
import java.util.*;
import org.jgap.*;

/**
 * Container for holding a given number of populations. Serves as a history
 * object for later evaluation.
 *
 * @author Klaus Meffert
 * @since 2.0
 */
public class PopulationHistory
    implements Serializable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.7 $";

  private List m_populations;

  private int m_maxSize;

  /**
   * Constructor.
   *
   * @param a_maxSize the maximum number of Population objects to hold, or
   * zero if there is no limit.
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public PopulationHistory(final int a_maxSize) {
    m_populations = new Vector();
    if (a_maxSize < 0) {
      throw new IllegalArgumentException("Maximum size must be greater"
          + " or equal to zero!");
    }
    m_maxSize = a_maxSize;
  }

  public Population getPopulation(final int a_count) {
    if (a_count >= m_populations.size()) {
      return null;
    }
    else {
      return (Population) m_populations.get(a_count);
    }
  }

  /**
   * Adds a population to the history. If the maximum size of this container
   * is exceeded, then the population added earliest is removed.
   *
   * @param a_population the population to be added
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void addPopulation(final Population a_population) {
    if (a_population == null) {
      throw new IllegalArgumentException("Population must not be null");
    }
    m_populations.add(0, a_population);
    int popSize = m_populations.size();
    if (m_maxSize != 0 && popSize > m_maxSize) {
      m_populations.remove(popSize - 1);
    }
  }

  /**
   * @author Klaus Meffert
   * @since 2.0
   */
  public void removeAllPopulations() {
    m_populations.removeAll(m_populations);
  }

  public int size() {
    return m_populations.size();
  }

  public List getPopulations() {
    return m_populations;
  }

  /**
   * Sets the list of populations to the list provided.
   *
   * @param a_populations list of populations to be set
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void setPopulations(final List a_populations) {
    m_populations = a_populations;
    int popSize = m_populations.size();
    if (m_maxSize != 0 && popSize > m_maxSize) {
      for (int i = m_maxSize; i < popSize; i++) {
        m_populations.remove(m_maxSize);
      }
    }
  }

  /**
   * @param a_other other object to compare to
   * @return true other object seen as equal
   *
   * @author Klaus Meffert
   * @since 3.5
   */
  public boolean equals(Object a_other) {
    PopulationHistory other = (PopulationHistory) a_other;
//    if(other.m_maxSize != m_maxSize) {
    //      return false;
    //    }
    for (int i = 0; i < size(); i++) {
      Population pop1 = getPopulation(i);
      Population pop2 = other.getPopulation(i);
      if (!pop1.equals(pop2)) {
        return false;
      }
    }
    return true;
  }
}
