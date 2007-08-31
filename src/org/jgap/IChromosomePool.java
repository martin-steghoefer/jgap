/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap;

/**
 * Interface for chromosome pools (e.g., see class ChromosomePool).
 *
 * @author Klaus Meffert
 * @since 2.6
 */
public interface IChromosomePool {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.3 $";

  /**
   * Attempts to acquire an Chromosome instance from the chromosome pool.
   *
   * @return a Chromosome instance from the pool or null if no Chromosome
   * instances are available in the pool
   *
   * @author Neil Rostan
   * @since 2.6 (since 1.0 in ChromosomePool)
   */
  IChromosome acquireChromosome();

  /**
   * Releases a Chromosome to the pool. It's not required that the Chromosome
   * originated from the pool--any Chromosome can be released to it. This
   * method should invoke the cleanup() method on each of the Chromosome's
   * genes prior to adding it back to the pool.
   *
   * @param a_chromosome the Chromosome instance to be released into the pool
   *
   * @author Neil Rostan
   * @since 2.6 (since 1.0 in ChromosomePool)
   */
  void releaseChromosome(IChromosome a_chromosome);
}
