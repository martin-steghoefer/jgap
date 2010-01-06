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

import java.util.*;
import org.jgap.*;

/**
 * The averaging crossover operator randomly selects two Chromosomes from the
 * population and "mates" them by randomly picking a gene and then swapping that
 * gene and all subsequent genes between the two Chromosomes. The two modified
 * Chromosomes are then added to the list of candidate Chromosomes. This
 * operation is performed half as many times as there are Chromosomes in
 * the population.
 * Additionally, the loci of crossing over are cached for each index, i.e.,
 * after randomizing the loci for each index once, they don't change again.
 *
 * If you work with CompositeGene's, this operator expects them to contain
 * genes of the same type (e.g. IntegerGene). If you have mixed types, please
 * provide your own crossover operator.
 *
 * @author Klaus Meffert
 * @since 2.0
 */
public class AveragingCrossoverOperator
    extends BaseGeneticOperator {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.29 $";

  /**
   * Random generator for randomizing the loci for crossing over
   */
  private RandomGenerator m_crossoverGenerator;

  /**
   * The current crossover rate used by this crossover operator.
   */
  private int m_crossoverRate;

  /**
   * Cache for alreadycrandomized loci for crossing over
   */
  private Map m_loci;

  /**
   * Calculator for dynamically determining the crossover rate. If set to
   * null the value of m_crossoverRate will be used instead.
   */
  private IUniversalRateCalculator m_crossoverRateCalc;

  private void init() {
    m_loci = new Hashtable();
    m_crossoverRate = 2;
  }

  /**
   * Using the same random generator for randomizing the loci for crossing
   * over as for selecting the genes to be crossed over.<p>
   * Attention: The configuration used is the one set with the static method
   * Genotype.setConfiguration.
   *
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public AveragingCrossoverOperator()
      throws InvalidConfigurationException {
    this(Genotype.getStaticConfiguration(), (RandomGenerator)null);
  }

  /**
   * Using the same random generator for randomizing the loci for crossing
   * over as for selecting the genes to be crossed over.
   *
   * @param a_configuration the configuration to use
   *
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public AveragingCrossoverOperator(final Configuration a_configuration)
      throws InvalidConfigurationException {
    this(a_configuration, (RandomGenerator)null);
  }

  /**
   * Using a different random generator for randomizing the loci for
   * crossing over than for selecting the genes to be crossed over
   * @param a_configuration the configuration to use
   * @param a_generatorForAveraging RandomGenerator to use
   *
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.0 (since 2.0 without a_configuration)
   */
  public AveragingCrossoverOperator(final Configuration a_configuration,
                                    final RandomGenerator
                                    a_generatorForAveraging)
      throws InvalidConfigurationException {
    super(a_configuration);
    init();
    m_crossoverGenerator = a_generatorForAveraging;
  }

  /**
   * Constructs a new instance of this CrossoverOperator with a specified
   * crossover rate calculator, which results in dynamic crossover being turned
   * on.
   * @param a_configuration the configuration to use
   * @param a_crossoverRateCalculator calculator for dynamic crossover rate
   * computation
   *
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.0 (since 2.0 without a_configuration)
   */
  public AveragingCrossoverOperator(final Configuration a_configuration,
                                    final IUniversalRateCalculator
                                    a_crossoverRateCalculator)
      throws InvalidConfigurationException {
    super(a_configuration);
    init();
    setCrossoverRateCalc(a_crossoverRateCalculator);
  }

  /**
   * Sets the crossover rate calculator
   * @param a_crossoverRateCalculator the new calculator
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  private void setCrossoverRateCalc(final IUniversalRateCalculator
                                    a_crossoverRateCalculator) {
    m_crossoverRateCalc = a_crossoverRateCalculator;
  }

  /**
   * Crossover that acts as a perturbed mean of two individuals.
   * x_i = p*x1_i + (1-p)*x2_i
   * p - uniform random value over [0,1].
   * Averaging over line means p is same for every i,
   * averaging over space if different p is chosen for each i.
   * See CrossoverOperator for general description, also see feature request
   * 708774
   *
   * @param a_population Chromosome[]
   * @param a_candidateChromosomes List
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void operate(final Population a_population,
                      final List a_candidateChromosomes) {
    // Determine the number of crossovers that should be performed
    int size = Math.min(getConfiguration().getPopulationSize(),
                        a_population.size());
    int numCrossovers = 0;
    if (m_crossoverRateCalc == null) {
      numCrossovers = size / m_crossoverRate;
    }
    else {
      numCrossovers = size / m_crossoverRateCalc.calculateCurrentRate();
    }
    RandomGenerator generator = getConfiguration().getRandomGenerator();
    if (m_crossoverGenerator == null) {
      m_crossoverGenerator = generator;
    }
    // For each crossover, grab two random chromosomes, pick a random
    // locus (gene location), and then swap that gene and all genes
    // to the "right" (those with greater loci) of that gene between
    // the two chromosomes.
    // --------------------------------------------------------------
    int index1, index2;
    for (int i = 0; i < numCrossovers; i++) {
      index1 = generator.nextInt(size);
      index2 = generator.nextInt(size);
      IChromosome origChrom1 = a_population.getChromosome(index1);
      IChromosome origChrom2 = a_population.getChromosome(index2);
      IChromosome firstMate = (IChromosome)origChrom1.clone();
      IChromosome secondMate = (IChromosome)origChrom2.clone();
      // In case monitoring is active, support it.
      // -----------------------------------------
      if (m_monitorActive) {
        firstMate.setUniqueIDTemplate(origChrom1.getUniqueID(), 1);
        firstMate.setUniqueIDTemplate(origChrom2.getUniqueID(), 2);
        secondMate.setUniqueIDTemplate(origChrom1.getUniqueID(), 1);
        secondMate.setUniqueIDTemplate(origChrom2.getUniqueID(), 2);
      }
      Gene[] firstGenes = firstMate.getGenes();
      Gene[] secondGenes = secondMate.getGenes();
      int locus = getLocus(m_crossoverGenerator, i, firstGenes.length);
      // Swap the genes.
      // ---------------
      Gene gene1;
      Gene gene2;
      Object firstAllele;
      for (int j = locus; j < firstGenes.length; j++) {
        // Make a distinction to ICompositeGene for the first gene.
        // --------------------------------------------------------
        if (firstGenes[j] instanceof ICompositeGene) {
          // Randomly determine gene to be considered
          index1 = generator.nextInt(firstGenes[j].size());
          gene1 = ( (ICompositeGene) firstGenes[j]).geneAt(index1);
        }
        else {
          gene1 = firstGenes[j];
        }
        // Make a distinction to ICompositeGene for the second gene.
        // ---------------------------------------------------------
        if (secondGenes[j] instanceof CompositeGene) {
          // Randomly determine gene to be considered
          index2 = generator.nextInt(secondGenes[j].size());
          gene2 = ( (ICompositeGene) secondGenes[j]).geneAt(index2);
        }
        else {
          gene2 = secondGenes[j];
        }
        firstAllele = gene1.getAllele();
        gene1.setAllele(gene2.getAllele());
        gene2.setAllele(firstAllele);
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
   *
   * @param a_generator to generate random values the first time
   * @param a_index the index of the crossover operation
   * @param a_max upper boundary for random generator
   *
   * @return crossover location for a given index
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  protected int getLocus(final RandomGenerator a_generator, final int a_index,
                         final int a_max) {
    Integer locus = (Integer) m_loci.get(new Integer(a_index));
    if (locus == null) {
      locus = new Integer(a_generator.nextInt(a_max));
      m_loci.put(new Integer(a_index), locus);
    }
    return locus.intValue();
  }

  /**
   * Compares this GeneticOperator against the specified object. The result is
   * true if and the argument is an instance of this class and is equal wrt the
   * data.
   *
   * @param a_other the object to compare against
   * @return true: if the objects are the same, false otherwise
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public boolean equals(final Object a_other) {
    try {
      return compareTo(a_other) == 0;
    }
    catch (ClassCastException cex) {
      return false;
    }
  }

  /**
   * Compares the given object to this one.
   *
   * @param a_other the instance against which to compare this instance
   * @return a negative number if this instance is "less than" the given
   * instance, zero if they are equal to each other, and a positive number if
   * this is "greater than" the given instance
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public int compareTo(final Object a_other) {
    if (a_other == null) {
      return 1;
    }
    AveragingCrossoverOperator op = (AveragingCrossoverOperator) a_other;
    if (m_crossoverRateCalc == null) {
      if (op.m_crossoverRateCalc != null) {
        return -1;
      }
    }
    else {
      if (op.m_crossoverRateCalc == null) {
        return 1;
      }
    }
    if (m_crossoverRate != op.m_crossoverRate) {
      if (m_crossoverRate > op.m_crossoverRate) {
        return 1;
      }
      else {
        return -1;
      }
    }
    // Everything is equal. Return zero.
    // ---------------------------------
    return 0;
  }

  /**
   * @param a_rate crossover rate to use by this crossover operator
   * @author Klaus Meffert
   * @since 2.6
   */
  public void setCrossoverRate(int a_rate) {
    m_crossoverRate = a_rate;
  }
}
