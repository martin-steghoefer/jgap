package org.jgap.impl.job;

import org.jgap.*;

/**
 * Simple implementation of IPopulationSplitter.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public class SimplePopulationSplitter
    implements IPopulationSplitter {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  private int m_count;

  /**
   * @param a_count number of chunks to create by splitting the population.
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public SimplePopulationSplitter(int a_count) {
    m_count = a_count;
  }

  /**
   * Splits a single population into smaller sub-populations.
   *
   * @param a_pop input population
   * @return resulting chunks
   * @throws Exception
   */
  public Population[] split(Population a_pop)
      throws Exception {
    Population[] result = new Population[m_count];
    int popSize = a_pop.size();
    int chunkSize = popSize / m_count;
    if (chunkSize < 1) {
      chunkSize = 1;
    }
    for (int i = 0; i < m_count; i++) {
      Population chunk = new Population(a_pop.getConfiguration(), chunkSize);
      result[i] = chunk;
      popSize -= chunkSize;
      if (popSize < 1) {
        break;
      }
      // Care for the last chunk.
      // ------------------------
      if (popSize < chunkSize) {
        chunkSize = popSize;
      }
    }
    return result;
  }
}
