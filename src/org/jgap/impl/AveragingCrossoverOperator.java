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
 * The averaging crossover operator randomly selects two Chromosomes from the
 * population and "mates" them by randomly picking a gene and then
 * swapping that gene and all subsequent genes between the two
 * Chromosomes. The two modified Chromosomes are then added to the
 * list of candidate Chromosomes. This operation is performed half
 * as many times as there are Chromosomes in the population.
 * Additionally, the loci of crossing over are cached for each index, i.e.,
 * after randomizing the loci for each index once, they don't change again
 *
 * @author Klaus Meffert
 * @since 2.0
 */
public class AveragingCrossoverOperator
    implements GeneticOperator {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.6 $";

  /**
   * Random generator for randomizing the loci for crossing over
   */
  private RandomGenerator crossoverGenerator;

  /**
   * The current crossover rate used by this crossover operator.
   */
  protected int m_crossoverRate;

  /**
   * Cache for alreadycrandomized loci for crossing over
   */
  private Map loci;

  /**
   * Calculator for dynamically determining the crossover rate. If set to
   * null the value of m_crossoverRate will be used instead.
   */
  private IUniversalRateCalculator m_crossoverRateCalc;

  private void init() {
    loci = new Hashtable();
  }

  /**
   * Using the same random generator for randomizing the loci for crossing
   * over as for selecting the genes to be crossed over
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public AveragingCrossoverOperator() {
    init();
    crossoverGenerator = null;
    m_crossoverRate = 2;
  }

  /**
   * Using a different random generator for randomizing the loci for
   * crossing over than for selecting the genes to be crossed over
   * @param generatorForAveraging RandomGenerator
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public AveragingCrossoverOperator(RandomGenerator generatorForAveraging) {
    init();
    crossoverGenerator = generatorForAveraging;
    m_crossoverRate = 2;
  }

  /**
   * Constructs a new instance of this CrossoverOperator with a specified
   * crossover rate calculator, which results in dynamic crossover being turned
   * on.
   * @param a_crossoverRateCalculator calculator for dynamic crossover rate
   *        computation
   *
   * @author Klaus Meffert (copied from CrossoverOperator)
   * @since 2.0
   */
  public AveragingCrossoverOperator(IUniversalRateCalculator a_crossoverRateCalculator) {
    this();
    setCrossoverRateCalc(a_crossoverRateCalculator);
  }

  /**
   * Sets the crossover rate calculator
   * @param a_crossoverRateCalculator The new calculator
   *
   * @author Klaus Meffert  (copied from CrossoverOperator)
   * @since 2.0
   */
  private void setCrossoverRateCalc(IUniversalRateCalculator a_crossoverRateCalculator){
      m_crossoverRateCalc = a_crossoverRateCalculator;
  }

  /**
   * Crossover that acts as a perturbed mean of two individuals.
    * x_i = p*x1_i + (1-p)*x2_i
    * p - uniform random value over [0,1].
    * Averaging over line means p is same for every i,
    * averaging over space if different p is chosen for each i.
   * @see CrossoverOperator for general description.
   * also see feature request 708774
   * @param a_population Chromosome[]
   * @param a_candidateChromosomes List
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void operate(final Population a_population,
                      final List a_candidateChromosomes) {
    // Work out the number of crossovers that should be performed
    int numCrossovers = 0;
    if (m_crossoverRateCalc == null){
        numCrossovers = a_population.size() / m_crossoverRate;
    }
    else{
        numCrossovers = a_population.size() / m_crossoverRateCalc.calculateCurrentRate();
    }

    RandomGenerator generator = Genotype.getConfiguration().getRandomGenerator();
    if (crossoverGenerator == null) {
      crossoverGenerator = generator;
    }
    // For each crossover, grab two random chromosomes, pick a random
    // locus (gene location), and then swap that gene and all genes
    // to the "right" (those with greater loci) of that gene between
    // the two chromosomes.
    // --------------------------------------------------------------
    for (int i = 0; i < numCrossovers; i++) {
      Chromosome firstMate = (Chromosome)
          a_population.getChromosome(generator.nextInt(a_population.size())).clone();
      Chromosome secondMate = (Chromosome)
          a_population.getChromosome(generator.nextInt(a_population.size())).clone();
      Gene[] firstGenes = firstMate.getGenes();
      Gene[] secondGenes = secondMate.getGenes();
      int locus = getLocus(crossoverGenerator, i, firstGenes.length);
      // Swap the genes.
      // ---------------
      Object firstAllele;
      for (int j = locus; j < firstGenes.length; j++) {
        firstAllele = firstGenes[j].getAllele();
        firstGenes[j].setAllele(secondGenes[j].getAllele());
        secondGenes[j].setAllele(firstAllele);
      }
      // Add the modified chromosomes to the candidate pool so that
      // they'll be considered for natural selection during the next
      // phase of evolution.
      // -----------------------------------------------------------
      a_candidateChromosomes.add(firstMate);
      a_candidateChromosomes.add(secondMate);
    }

  }

  /**
   * Returns the crossover location for a given index.
   * For each index the crossover locatio  is the same, therefor it is cached!
   * @param generator to generate random values the first time
   * @param index the index of the crossover operation
   * @param max upper boundary for random generator
   * @return crossover location for a given index
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  protected int getLocus(RandomGenerator generator, int index, int max) {
    Integer locus = (Integer)loci.get(new Integer(index));
    if (locus == null) {
      locus = new Integer(generator.nextInt(max));
      loci.put(new Integer(index), locus);
    }
    return locus.intValue();
  }
}
