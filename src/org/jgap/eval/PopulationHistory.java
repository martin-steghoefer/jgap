package org.jgap.eval;

import java.util.*;
import org.jgap.*;

/**
 * Container for holding a given number of populations. Serves as a history
 * object for later evaluation
 *
 * @author Klaus Meffert
 * @since 2.0
 */
public class PopulationHistory {
  private List populations;

  private int m_maxSize;

  public PopulationHistory(int a_maxSize) {
    populations = new Vector();
    m_maxSize = a_maxSize;
  }

  public Population getPopulation(int count) {
    return count >= populations.size() ? null :
        (Population) populations.get(count);
  }

  /**
   * Adds a population to the history. If the maximum size of this container
   * is exceeded after that then the oldest population added is removed
   * @param pop the population to be added
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void addPopulation(Population pop) {
    populations.add(0, pop);
    int popSize = populations.size();
    if (popSize > m_maxSize) {
      populations.remove(popSize - 1);
    }
  }

  /**
   * @author Klaus Meffert
   * @since 2.0
   */
  public void removeAllPopulations() {
    populations.removeAll(populations);
  }

  public int size() {
    return populations.size();
  }

  public List getPopulations() {
    return populations;
  }

  /**
   * Sets the list of populations to the list provided
   * @param populations list of populations to be set
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void setPopulations(List populations) {
    this.populations = populations;
    int popSize = populations.size();
    if (popSize > m_maxSize) {
      for (int i = m_maxSize; i < popSize; i++) {
        populations.remove(m_maxSize);
      }
    }
  }
}
