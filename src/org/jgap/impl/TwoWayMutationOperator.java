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
 * Considers two levels of mutation. At first, this mutation operator assumes
 * all genes within a chromosome to having a different impact on the result
 * when mutated. For that, a gene with fewer impact is selected for mutation
 * more likely than one with bigger impact. After a gene has been selected for
 * mutation, it is indeed mutated in a traditional way.
 * <p>
 * See class examples.dynamicMutation.DynamicMutationExample for usage,
 * currently this class only works with that example!
 *
 * @author Klaus Meffert
 * @since 2.6
 */
public class TwoWayMutationOperator
    extends BaseGeneticOperator {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.9 $";

  /**
   * The current mutation rate used by this MutationOperator, expressed as
   * the denominator in the 1 / X ratio. For example, X = 1000 would
   * mean that, on average, 1 / 1000 genes would be mutated. A value of zero
   * disables mutation entirely.
   */
  private int m_mutationRate;

  /**
   * Calculator for dynamically determining the mutation rate. If set to
   * null the value of m_mutationRate will be used. Replaces the previously used
   * boolean m_dynamicMutationRate.
   */
  private IUniversalRateCalculator m_mutationRateCalc;

  /**
   * Constructs a new instance of this MutationOperator without a specified
   * mutation rate, which results in dynamic mutation being turned on. This
   * means that the mutation rate will be automatically determined by this
   * operator based upon the number of genes present in the chromosomes.<p>
   * Attention: The configuration used is the one set with the static method
   * Genotype.setConfiguration.
   *
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public TwoWayMutationOperator()
      throws InvalidConfigurationException {
    this(Genotype.getStaticConfiguration(),
         new DefaultMutationRateCalculator(Genotype.getStaticConfiguration()));
  }

  /**
   * Constructs a new instance of this MutationOperator without a specified
   * mutation rate, which results in dynamic mutation being turned on. This
   * means that the mutation rate will be automatically determined by this
   * operator based upon the number of genes present in the chromosomes.
   *
   * @param a_config the configuration to use
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public TwoWayMutationOperator(final Configuration a_config)
      throws InvalidConfigurationException {
    this(a_config, new DefaultMutationRateCalculator(a_config));
  }

  /**
   * Constructs a new instance of this MutationOperator with a specified
   * mutation rate calculator, which results in dynamic mutation being turned
   * on.
   *
   * @param a_config the configuration to use
   * @param a_mutationRateCalculator calculator for dynamic mutation rate
   * computation
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public TwoWayMutationOperator(final Configuration a_config,
                                final IUniversalRateCalculator
                                a_mutationRateCalculator)
      throws InvalidConfigurationException {
    super(a_config);
    setMutationRateCalc(a_mutationRateCalculator);
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
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public TwoWayMutationOperator(final Configuration a_config,
                                final int a_desiredMutationRate)
      throws InvalidConfigurationException {
    super(a_config);
    m_mutationRate = a_desiredMutationRate;
    setMutationRateCalc(null);
  }

  /**
   * @param a_population see interface
   * @param a_candidateChromosomes see interface
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void operate(final Population a_population,
                      final List a_candidateChromosomes) {
    if (a_population == null || a_candidateChromosomes == null) {
      // Population or candidate chromosomes list empty:
      // nothing to do.
      // -----------------------------------------------
      return;
    }
    if (m_mutationRate == 0 && m_mutationRateCalc == null) {
      // If the mutation rate is set to zero and dynamic mutation rate is
      // disabled, then we don't perform any mutation.
      // ----------------------------------------------------------------
      return;
    }
    // Determine the mutation rate. If dynamic rate is enabled, then
    // calculate it using the IUniversalRateCalculator instance.
    // Otherwise, go with the mutation rate set upon construction.
    // -------------------------------------------------------------
    boolean mutate = false;
    RandomGenerator generator = getConfiguration().getRandomGenerator();
    // It would be inefficient to create copies of each Chromosome just
    // to decide whether to mutate them. Instead, we only make a copy
    // once we've positively decided to perform a mutation.
    // ----------------------------------------------------------------
    int size = Math.min(getConfiguration().getPopulationSize(),
                        a_population.size());
    IGeneticOperatorConstraint constraint = getConfiguration().
        getJGAPFactory().getGeneticOperatorConstraint();
    for (int i = 0; i < size; i++) {
      IChromosome chrom = a_population.getChromosome(i);
      Gene[] genes = chrom.getGenes();
      IChromosome copyOfChromosome = null;
      // For each Chromosome in the population...
      // ----------------------------------------
      double d = generator.nextDouble();
      int geneIndex;
      /**@todo make this configurable, it is a first test,
       * see example DynamicMutationExample*/
      if (d >= (1 - 0.7462d)) {
        geneIndex = 3;
      }
      else if (d >= (1 - 0.7462d - 0.1492537d)) {
        geneIndex = 2;
      }
      else if (d >= (1 - 0.7462d - 0.1492537d - 0.07462686d)) {
        geneIndex = 1;
      }
      else {
        geneIndex = 0;
      }
      if (geneIndex >= genes.length) {
        geneIndex = genes.length - 1;
      }

      if (m_mutationRateCalc != null) {
        // If it's a dynamic mutation rate then let the calculator decide
        // whether the current gene should be mutated.
        // --------------------------------------------------------------
        mutate = m_mutationRateCalc.toBePermutated(chrom, geneIndex);
      }
      else {
        // Non-dynamic, so just mutate based on the the current rate.
        // In fact we use a rate of 1/m_mutationRate.
        // ----------------------------------------------------------
        mutate = (generator.nextInt(m_mutationRate) == 0);
      }
      if (mutate) {
//          if (m_mutationManager != null) {
//            if (!m_mutationManager.doMutate(chrom,j)) {
//              continue;
//            }
//          }
        // Verify that crossover allowed.
        // ------------------------------
        /**@todo move to base class, refactor*/
        if (constraint != null) {
          List v = new Vector();
          v.add(chrom);
          if (!constraint.isValid(a_population, v, this)) {
            continue;
          }
        }
        // Now that we want to actually modify the Chromosome,
        // let's make a copy of it (if we haven't already) and
        // add it to the candidate chromosomes so that it will
        // be considered for natural selection during the next
        // phase of evolution. Then we'll set the gene's value
        // to a random value as the implementation of our
        // "mutation" of the gene.
        // ---------------------------------------------------
        if (copyOfChromosome == null) {
          // ...take a copy of it...
          // -----------------------
          copyOfChromosome = (IChromosome) chrom.clone();
          // ...add it to the candidate pool...
          // ----------------------------------
          a_candidateChromosomes.add(copyOfChromosome);
          // ...then mutate all its genes...
          // -------------------------------
          genes = copyOfChromosome.getGenes();
          // In case monitoring is active, support it.
          // -----------------------------------------
          if (m_monitorActive) {
            copyOfChromosome.setUniqueIDTemplate(chrom.getUniqueID(), 1);
          }
        }
        // Process all atomic elements in the gene. For a StringGene this
        // would be the length of the string, for an IntegerGene, it is
        // always one element.
        // --------------------------------------------------------------
        if (genes[geneIndex] instanceof ICompositeGene) {
          ICompositeGene compositeGene = (ICompositeGene) genes[geneIndex];
          if (m_monitorActive) {
            compositeGene.setUniqueIDTemplate(chrom.getGene(geneIndex).
                getUniqueID(), 1);
          }
          for (int k = 0; k < compositeGene.size(); k++) {
            mutateGene(compositeGene.geneAt(k), generator);
            if (m_monitorActive) {
              compositeGene.geneAt(k).setUniqueIDTemplate(
                  ( (ICompositeGene) chrom.getGene(geneIndex)).geneAt(k).
                  getUniqueID(),
                  1);
            }
          }
        }
        else {
          mutateGene(genes[geneIndex], generator);
          if (m_monitorActive) {
            genes[geneIndex].setUniqueIDTemplate(chrom.getGene(geneIndex).
                getUniqueID(), 1);
          }
        }
      }
    }
  }

  /**
   * Helper: mutate all atomic elements of a gene
   * @param a_gene the gene to be mutated
   * @param a_generator the generator delivering amount of mutation
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  private void mutateGene(final Gene a_gene, final RandomGenerator a_generator) {
    for (int k = 0; k < a_gene.size(); k++) {
      // 1. Select a gene (assuming genes are ordered regarding their impact
      //                   on the result, if mutated)
      // -------------------------------------------------------------------

      // Retrieve value between 0 and 1 (not included) from generator.
      // Then map this value to range -1 and 1 (-1 included, 1 not).
      // -------------------------------------------------------------
      double percentage = -1 + a_generator.nextDouble() * 2;
      // 2. Mutate atomic element by calculated percentage.
      // --------------------------------------------------
      a_gene.applyMutation(k, percentage);
    }
  }

  /**
   * @return the MutationRateCalculator used
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public IUniversalRateCalculator getMutationRateCalc() {
    return m_mutationRateCalc;
  }

  /**
   * Sets the MutationRateCalculator to be used for determining the strength of
   * mutation.
   * @param a_mutationRateCalc MutationRateCalculator
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void setMutationRateCalc(final IUniversalRateCalculator
                                  a_mutationRateCalc) {
    m_mutationRateCalc = a_mutationRateCalc;
    if (m_mutationRateCalc != null) {
      m_mutationRate = 0;
    }
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
    TwoWayMutationOperator op = (TwoWayMutationOperator) a_other;
    if (m_mutationRateCalc == null) {
      if (op.m_mutationRateCalc != null) {
        return -1;
      }
    }
    else {
      if (op.m_mutationRateCalc == null) {
        return 1;
      }
      else {
      }
    }
    if (m_mutationRate != op.m_mutationRate) {
      if (m_mutationRate > op.m_mutationRate) {
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

  public int getMutationRate() {
    return m_mutationRate;
  }
}
