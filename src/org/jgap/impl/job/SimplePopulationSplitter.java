/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
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
  private final static String CVS_REVISION = "$Revision: 1.4 $";

  private int m_count;

  /**
   * @param a_count number of chunks to create by splitting the population.
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public SimplePopulationSplitter(int a_count) {
    if (a_count < 1) {
      throw new IllegalArgumentException("Count must be greater than zero!");
    }
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
    int popSize = a_pop.size();
    int count = m_count;
    int chunkSize = popSize / count;
    if (chunkSize < 1) {
      chunkSize = 1;
      count = 1;
    }
    Population[] result = new Population[count];
    int index = 0;
    for (int i = 0; i < count; i++) {
      Population chunk = new Population(a_pop.getConfiguration(), chunkSize);
      // Fill chunk with chromosomes.
      // ----------------------------
      for (int j = 0; j < chunkSize; j++) {
        chunk.addChromosome(a_pop.getChromosome(index));
        index++;
      }
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
