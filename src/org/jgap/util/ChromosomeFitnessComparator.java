/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.util;

import java.util.Comparator;
import org.jgap.*;

/**
 * Simple comparator to allow the sorting of Chromosome lists with the highest
 * fitness value in first place of the list.
 * Usage example:
 *   Collections.sort(
 *     population.getChromosomes(), new ChromosomeFitnessComparator() );
 *
 * @author Charles Kevin Hill, Klaus Meffert
 * @since 2.4
 */
public class ChromosomeFitnessComparator
    implements Comparator {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.9 $";

  private FitnessEvaluator m_fitnessEvaluator;

  /**
   * Constructs the comparator using the DefaultFitnessEvaluator
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public ChromosomeFitnessComparator() {
    this(new DefaultFitnessEvaluator());
  }

  /**
   * @param a_evaluator the fitness evaluator to use
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public ChromosomeFitnessComparator(FitnessEvaluator a_evaluator) {
    if (a_evaluator == null) {
      throw new IllegalArgumentException("Evaluator must not be null");
    }
    m_fitnessEvaluator = a_evaluator;
  }

  /**
   * Compares two chromosomes by using a FitnessEvaluator.
   *
   * @param a_chromosome1 the first chromosome to compare
   * @param a_chromosome2 the second chromosome to compare
   * @return -1 if a_chromosome1 is fitter than a_chromosome2, 1 if it is the
   * other way round and 0 if both are equal
   * @author Charles Kevin Hill, Klaus Meffert
   * @since 2.6
   */
  public int compare(final Object a_chromosome1, final Object a_chromosome2) {
    IChromosome chromosomeOne = (IChromosome) a_chromosome1;
    IChromosome chromosomeTwo = (IChromosome) a_chromosome2;
    if (m_fitnessEvaluator.isFitter(chromosomeOne, chromosomeTwo)) {
      return -1;
    }
    else if (m_fitnessEvaluator.isFitter(chromosomeTwo, chromosomeOne)) {
      return 1;
    }
    else {
      return 0;
    }
  }
}
