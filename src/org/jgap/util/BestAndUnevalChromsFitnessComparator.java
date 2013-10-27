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
 * Ccomparator for chromosomes:
 * 1) Chromosome without fitness value is regarded fitter than chromosome with
 *    fitness value
 * 2) Chromosome with fitness value is regared fitter than another chromosome
 *    with fitness value, in case the used fitness evaluator says so.
 *
 * Usage example:
 *   Collections.sort(
 *     population.getChromosomes(), new BestAndUnevalChromsFitnessComparator() );
 *
 * @author Klaus Meffert
 * @since 3.63
 */
public class BestAndUnevalChromsFitnessComparator
    implements Comparator {
  private FitnessEvaluator m_fitnessEvaluator;

  /**
   * Constructs the comparator using the DefaultFitnessEvaluator
   *
   * @author Klaus Meffert
   * @since 3.63
   */
  public BestAndUnevalChromsFitnessComparator() {
    this(new DefaultFitnessEvaluator());
  }

  /**
   * @param a_evaluator the fitness evaluator to use
   *
   * @author Klaus Meffert
   * @since 3.63
   */
  public BestAndUnevalChromsFitnessComparator(FitnessEvaluator a_evaluator) {
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
   * @author Klaus Meffert
   * @since 3.63
   */
  public int compare(final Object a_chromosome1, final Object a_chromosome2) {
    IChromosome chromosomeOne = (IChromosome) a_chromosome1;
    IChromosome chromosomeTwo = (IChromosome) a_chromosome2;
    double fitness1 = chromosomeOne.getFitnessValueDirectly();
    double fitness2 = chromosomeTwo.getFitnessValueDirectly();
    if (Math.abs(fitness1 - FitnessFunction.NO_FITNESS_VALUE) <
        FitnessFunction.DELTA) {
      if (Math.abs(fitness2 - FitnessFunction.NO_FITNESS_VALUE) <
          FitnessFunction.DELTA) {
        // Both Chromosomes have no fitness value
        return 0;
      }
      // Chromosome 1 has no fitness value, Chromosome 2 has a fitness value
      return -1;
    }
    if (Math.abs(fitness2 - FitnessFunction.NO_FITNESS_VALUE) <
        FitnessFunction.DELTA) {
      // Chromosome 2 has no fitness value, Chromosome 1 has a fitness value
      return 1;
    }
    // Both Chromosomes have a fitness value
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
