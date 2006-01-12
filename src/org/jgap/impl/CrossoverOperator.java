/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.impl;

import java.util.*;
import org.jgap.*;

/**
 * The crossover operator randomly selects two Chromosomes from the
 * population and "mates" them by randomly picking a gene and then
 * swapping that gene and all subsequent genes between the two
 * Chromosomes. The two modified Chromosomes are then added to the
 * list of candidate Chromosomes.
 *
 * This CrossoverOperator supports both fixed and dynamic crossover rates.
 * A fixed rate is one specified at construction time by the user. This
 * operation is performed 1/m_crossoverRate as many times as there are
 * Chromosomes in the population. A dynamic rate is one determined by
 * this class if no fixed rate is provided.
 *
 * @author Neil Rotstan
 * @author Klaus Meffert
 * @author Chris Knowles
 * @since 1.0
 */
public class CrossoverOperator
    implements GeneticOperator, Comparable {
  /**@todo add base class, also use for other GeneticOperator's.
   * Move compareTo there etc.
   */

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.23 $";

  /**
   * The current crossover rate used by this crossover operator.
   */
  private int m_crossoverRate;

  /**
   * Calculator for dynamically determining the crossover rate. If set to
   * null the value of m_crossoverRate will be used instead.
   */
  private IUniversalRateCalculator m_crossoverRateCalc;

  /**
   * Constructs a new instance of this CrossoverOperator without a specified
   * crossover rate, this results in dynamic crossover rate being turned off.
   * This means that the crossover rate will be fixed at populationsize/2.
   *
   * @author Chris Knowles
   * @since 2.0
   */
  public CrossoverOperator() {
    //set the default crossoverRate to be populationsize/2
    m_crossoverRate = 2;
    setCrossoverRateCalc(null);
  }

  /**
   * Constructs a new instance of this CrossoverOperator with a specified
   * crossover rate calculator, which results in dynamic crossover being turned
   * on.
   *
   * @param a_crossoverRateCalculator calculator for dynamic crossover rate
   * computation
   *
   * @author Chris Knowles
   * @since 2.0
   */
  public CrossoverOperator(final IUniversalRateCalculator
                           a_crossoverRateCalculator) {
    setCrossoverRateCalc(a_crossoverRateCalculator);
  }

  /**
   * Constructs a new instance of this CrossoverOperator with the given
   * crossover rate.
   *
   * @param a_desiredCrossoverRate the desired rate of crossover
   *
   * @author Chris Knowles
   * @since 2.0
   */
  public CrossoverOperator(final int a_desiredCrossoverRate) {
    if (a_desiredCrossoverRate < 1) {
      throw new IllegalArgumentException("Crossover rate must be greater zero");
    }
    m_crossoverRate = a_desiredCrossoverRate;
    setCrossoverRateCalc(null);
  }

  /**
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 2.0 (earlier versions referenced the Configuration object)
   */
  public void operate(final Population a_population,
                      final List a_candidateChromosomes) {
    // Work out the number of crossovers that should be performed.
    // -----------------------------------------------------------
    int size = Math.min(Genotype.getConfiguration().getPopulationSize(),
                        a_population.size());
    int numCrossovers = 0;
    if (m_crossoverRateCalc == null) {
      numCrossovers = size / m_crossoverRate;
    }
    else {
      numCrossovers = size / m_crossoverRateCalc.calculateCurrentRate();
    }
    RandomGenerator generator = Genotype.getConfiguration().getRandomGenerator();
    IGeneticOperatorConstraint constraint = Genotype.getConfiguration().
        getJGAPFactory().getGeneticOperatorConstraint();

    // For each crossover, grab two random chromosomes, pick a random
    // locus (gene location), and then swap that gene and all genes
    // to the "right" (those with greater loci) of that gene between
    // the two chromosomes.
    // --------------------------------------------------------------
    int index1, index2;
    for (int i = 0; i < numCrossovers; i++) {
      index1 = generator.nextInt(size);
      index2 = generator.nextInt(size);
      Chromosome chrom1 = a_population.getChromosome(index1);
      Chromosome chrom2 = a_population.getChromosome(index2);

      // Verify that crossover allowed.
      // ------------------------------
      /**@todo move to base class, refactor*/
      if (constraint != null) {
        List v = new Vector();
        v.add(chrom1);
        v.add(chrom2);
        if (!constraint.isValid(a_population, v, this)) {
          continue;
        }
      }

      // Clone the chromosomes.
      // ----------------------
      Chromosome firstMate = (Chromosome) chrom1.clone();
      Chromosome secondMate = (Chromosome) chrom2.clone();
      Gene[] firstGenes = firstMate.getGenes();
      Gene[] secondGenes = secondMate.getGenes();
      int locus = generator.nextInt(firstGenes.length);
      // Swap the genes.
      // ---------------
      Gene gene1;
      Gene gene2;
      Object firstAllele;
      for (int j = locus; j < firstGenes.length; j++) {
        // Make a distinction for ICompositeGene for the first gene.
        // -------------------------------------------------------
        if (firstGenes[j] instanceof ICompositeGene) {
          // Randomly determine gene to be considered.
          // -----------------------------------------
          index1 = generator.nextInt(firstGenes[j].size());
          gene1 = ( (ICompositeGene) firstGenes[j]).geneAt(index1);
        }
        else {
          gene1 = firstGenes[j];
        }
        // Make a distinction for CompositeGene for the second gene.
        // --------------------------------------------------------
        if (secondGenes[j] instanceof ICompositeGene) {
          //randomly determine gene to be considered
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
   * Sets the crossover rate calculator.
   *
   * @param a_crossoverRateCalculator the new calculator
   *
   * @author Chris Knowles
   * @since 2.0
   */
  private void setCrossoverRateCalc(final IUniversalRateCalculator
                                    a_crossoverRateCalculator) {
    m_crossoverRateCalc = a_crossoverRateCalculator;
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
   * Compares the given GeneticOperator to this GeneticOperator.
   *
   * @param a_other the instance against which to compare this instance
   * @return a negative number if this instance is "less than" the given
   * instance, zero if they are equal to each other, and a positive number if
   * this is "greater than" the given instance
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public int compareTo(Object a_other) {
    if (a_other == null) {
      return 1;
    }
    CrossoverOperator op = (CrossoverOperator) a_other;
    if (m_crossoverRateCalc == null) {
      if (op.m_crossoverRateCalc != null) {
        return -1;
      }
    }
    else {
      if (op.m_crossoverRateCalc == null) {
        return 1;
      }
      else {
        // here, we could compare the rate calculators
//        if (!m_crossoverRateCalc.equals(op.m_crossoverRateCalc)) {
//          return -1; //arbitrary
//        }
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
}
