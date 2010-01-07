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
 * Swaps the genes instead of mutating them. This kind of operator is
 * required by Traveling Salesman Problem. Copied and modified from
 * SwappingMutationOperator. This implementation takes a range as parameter and
 * uses it to only swap genes that are -/+ range indexes away from the gene
 * being mutated. This basically allows for local searches before propagating
 * through the entire problem space. Seems to work much better/quicker than a
 * random swapping across the entire chromosome.
 *
 * @author Audrius Meskauskas
 * @author Neil Rotstan
 * @author Klaus Meffert
 * @author Florian Hafner
 * @since 3.3.2
 */
public class RangedSwappingMutationOperator
    extends MutationOperator {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  private int m_startOffset = 0;

  private int m_range = 0;

  /**
   * Constructs a new instance of this operator.<p>
   * Attention: The configuration used is the one set with the static method
   * Genotype.setConfiguration.
   *
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.3.2
   */
  public RangedSwappingMutationOperator()
      throws InvalidConfigurationException {
    super();
    m_range = 0;
  }

  /**
   * @param a_config the configuration to use
   * @param a_range the maximum range to use for considering genes to be
   * swapped. The range is computed as the difference of the indices between
   * two genes
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.3.2
   */
  public RangedSwappingMutationOperator(final Configuration a_config,
                                        int a_range)
      throws InvalidConfigurationException {
    super(a_config);
    m_range = a_range;
  }

  /**
   * Constructs a new instance of this operator with a specified
   * mutation rate calculator, which results in dynamic mutation being turned
   * on.
   *
   * @param a_config the configuration to use
   * @param a_mutationRateCalculator calculator for dynamic mutation rate
   * computation
   * @param a_range the maximum range to use for considering genes to be
   * swapped. The range is computed as the difference of the indices between
   * two genes
   *
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.3.2
   */
  public RangedSwappingMutationOperator(final Configuration a_config,
      final IUniversalRateCalculator a_mutationRateCalculator, int a_range)
      throws InvalidConfigurationException {
    super(a_config, a_mutationRateCalculator);
    m_range = a_range;
  }

  /**
   * Constructs a new instance of this MutationOperator with the given
   * mutation rate.
   *
   * @param a_config the configuration to use
   * @param a_desiredMutationRate desired rate of mutation, expressed as
   * the denominator of the 1 / X fraction. For example, 1000 would result
   * in 1/1000 genes being mutated on average. A mutation rate of zero disables
   * mutation entirely
   * @param a_range the maximum range to use for considering genes to be
   * swapped. The range is computed as the difference of the indices between
   * two genes
   *
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.3.2
   */
  public RangedSwappingMutationOperator(final Configuration a_config,
      final int a_desiredMutationRate, int a_range)
      throws InvalidConfigurationException {
    super(a_config, a_desiredMutationRate);
    m_range = a_range;
  }

  /**
   * @param a_population the population of chromosomes from the current
   * evolution prior to exposure to any genetic operators. Chromosomes in this
   * array should not be modified. Please, notice, that the call in
   * Genotype.evolve() to the implementations of GeneticOperator overgoes this
   * due to performance issues
   * @param a_candidateChromosomes the pool of chromosomes that have been
   * selected for the next evolved population
   *
   * @author Audrius Meskauskas
   * @author Klaus Meffert
   * @since 3.3.2
   */
  public void operate(final Population a_population,
                      List a_candidateChromosomes) {
    // this was a private variable, now it is local reference.
    final IUniversalRateCalculator m_mutationRateCalc = getMutationRateCalc();
    // If the mutation rate is set to zero and dynamic mutation rate is
    // disabled, then we don't perform any mutation.
    // ----------------------------------------------------------------
    if (getMutationRate() == 0 && m_mutationRateCalc == null) {
      return;
    }
    // Determine the mutation rate. If dynamic rate is enabled, then
    // calculate it based upon the number of genes in the chromosome.
    // Otherwise, go with the mutation rate set upon construction.
    // --------------------------------------------------------------
    int currentRate;
    if (m_mutationRateCalc != null) {
      currentRate = m_mutationRateCalc.calculateCurrentRate();
    }
    else {
      currentRate = getMutationRate();
    }
    RandomGenerator generator = getConfiguration().getRandomGenerator();
    // It would be inefficient to create copies of each Chromosome just
    // to decide whether to mutate them. Instead, we only make a copy
    // once we've positively decided to perform a mutation.
    // ----------------------------------------------------------------
    int size = a_population.size();
    for (int i = 0; i < size; i++) {
      IChromosome x = a_population.getChromosome(i);
      // This returns null if not mutated:
      IChromosome xm = operate(x, currentRate, generator);
      if (xm != null) {
        a_candidateChromosomes.add(xm);
      }
    }
  }

  /**
   * Operate on the given chromosome with the given mutation rate.
   *
   * @param a_chrom chromosome to operate
   * @param a_rate mutation rate
   * @param a_generator random generator to use (must not be null)
   * @return mutated chromosome of null if no mutation has occured.
   *
   * @author Audrius Meskauskas
   * @author Florian Hafner
   * @since 3.3.2
   */
  protected IChromosome operate(final IChromosome a_chrom, final int a_rate,
                                final RandomGenerator a_generator) {
    IChromosome chromosome = null;
    // ----------------------------------------
    for (int j = m_startOffset; j < a_chrom.size(); j++) {
      // Ensure probability of 1/currentRate for applying mutation.
      // ----------------------------------------------------------
      if (a_generator.nextInt(a_rate) == 0) {
        if (chromosome == null) {
          chromosome = (IChromosome) a_chrom.clone();
          // In case monitoring is active, support it.
          // -----------------------------------------
          if (m_monitorActive) {
            chromosome.setUniqueIDTemplate(a_chrom.getUniqueID(), 1);
          }
        }
        Gene[] genes = chromosome.getGenes();
        if (m_range == 0) {
          m_range = genes.length;
        }
        Gene[] mutated = operate(a_generator, j, genes);
        // setGenes is not required for this operator, but it may
        // be needed for the derived operators.
        // ------------------------------------------------------
        try {
          chromosome.setGenes(mutated);
        } catch (InvalidConfigurationException cex) {
          throw new Error("Gene type not allowed by constraint checker", cex);
        }
      }
    }
    return chromosome;
  }

  /**
   * Operate on the given array of genes. This method is only called
   * when it is already clear that the mutation must occur under the given
   * mutation rate.
   *
   * @param a_generator a random number generator that may be needed to
   * perform a mutation
   * @param a_target_gene an index of gene in the chromosome that will mutate
   * @param a_genes the array of all genes in the chromosome
   * @return the mutated gene array
   *
   * @author Florian Hafner
   * @since 3.3.2
   */
  protected Gene[] operate(final RandomGenerator a_generator,
                           final int a_target_gene, final Gene[] a_genes) {
    // Other needs to be an integer that is within the range of the subject
    // target gene.

    int rand = a_generator.nextInt(2 * m_range);
    int other = (a_target_gene - m_range) + rand;
    if (other < 0) {
      other = 0;
    }
    if (other >= a_genes.length) {
      other = a_genes.length - 1; // Index is -1 of length
    }
    Gene t = a_genes[a_target_gene];
    a_genes[a_target_gene] = a_genes[other];
    a_genes[other] = t;
    if (m_monitorActive) {
      a_genes[a_target_gene].setUniqueIDTemplate(a_genes[other].getUniqueID(), 1);
      a_genes[other].setUniqueIDTemplate(a_genes[a_target_gene].getUniqueID(), 1);
    }
    return a_genes;
  }

  /**
   * Sets a number of genes at the start of chromosome, that are
   * excluded from the swapping. In the Salesman task, the first city
   * in the list should (where the salesman leaves from) probably should
   * not change as it is part of the list. The default value is 1.
   *
   * @param a_offset the offset to set
   *
   * @author Audrius Meskauskas
   * @since 3.3.2
   */
  public void setStartOffset(final int a_offset) {
    m_startOffset = a_offset;
  }

  /**
   * Gets a number of genes at the start of chromosome, that are
   * excluded from the swapping. In the Salesman task, the first city
   * in the list should (where the salesman leaves from) probably should
   * not change as it is part of the list. The default value is 1.
   *
   * @return the start offset
   *
   * @author Audrius Meskauskas
   * @since 3.3.2
   */
  public int getStartOffset() {
    return m_startOffset;
  }

  /**
   * Sets the mutation range. This specifies the genes before and after
   * which a subject gene is allowed to swap with
   *
   * @param a_range the offset to set
   *
   * @author Audrius Meskauskas
   * @since 3.3.2
   */
  public void setRange(final int a_range) {
    m_range = a_range;
  }

  /**
   * Gets the mutation range.
   *
   * @return the start offset
   *
   * @author Audrius Meskauskas
   * @since 3.3.2
   */
  public int getRange() {
    return m_range;
  }
}
