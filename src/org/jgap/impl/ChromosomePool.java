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

import org.jgap.*;

/**
 * Provides a pooling mechanism for Chromosome instances so that
 * discarded Chromosome instances can be recycled, thus saving memory and the
 * overhead of constructing new ones from scratch each time.
 *
 * @author Neil Rotstan
 * @author Klaus Meffert
 * @since 1.0
 */
public class ChromosomePool
    implements IChromosomePool {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.12 $";

  /**
   * The internal pool in which the Chromosomes are stored.
   */
  private Pool m_chromosomePool;

  /**
   * Constructor.
   *
   * @author Neil Rostan
   * @since 1.0
   */
  public ChromosomePool() {
    m_chromosomePool = new Pool();
  }

  /**
   * Attempts to acquire an Chromosome instance from the chromosome pool.
   * It should be noted that nothing is guaranteed about the value of the
   * Chromosome's genes and they should be treated as undefined.
   *
   * @return a Chromosome instance from the pool or null if no Chromosome
   * instances are available in the pool
   *
   * @author Neil Rostan
   * @since 1.0
   */
  public synchronized IChromosome acquireChromosome() {
    return (IChromosome) m_chromosomePool.acquirePooledObject();
  }

  /**
   * Releases a Chromosome to the pool. It's not required that the Chromosome
   * originated from the pool--any Chromosome can be released to it. This
   * method will invoke the cleanup() method on each of the Chromosome's
   * genes prior to adding it back to the pool.
   *
   * @param a_chromosome the Chromosome instance to be released into the pool
   *
   * @author Neil Rostan
   * @since 1.0
   */
  public synchronized void releaseChromosome(final IChromosome a_chromosome) {
    if (a_chromosome == null) {
      throw new IllegalArgumentException(
          "Chromosome instance must not be null!");
    }
    // First cleanup the chromosome's genes before returning it back
    // to the pool.
    // -------------------------------------------------------------
    Gene[] genes = a_chromosome.getGenes();
    int size = a_chromosome.size();
    for (int i = 0; i < size; i++) {
      genes[i].cleanup();
    }
    // Now add it to the pool.
    // -----------------------
    m_chromosomePool.releaseObject(a_chromosome);
  }
}
