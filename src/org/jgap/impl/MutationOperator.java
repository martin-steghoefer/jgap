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
import org.jgap.data.config.*;

/**
 * The mutation operator runs through the genes in each of the Chromosomes
 * in the population and mutates them in statistical accordance to the
 * given mutation rate. Mutated Chromosomes are then added to the list of
 * candidate Chromosomes destined for the natural selection process.
 *
 * This MutationOperator supports both fixed and dynamic mutation rates.
 * A fixed rate is one specified at construction time by the user. A dynamic
 * rate is determined by this class if no fixed rate is provided, and is
 * calculated based on the size of the Chromosomes in the population. Details
 * are specified in the DefaultMutationRateCalculator class.
 *
 * @author Neil Rotstan
 * @author Klaus Meffert
 * @since 1.0
 */
public class MutationOperator
    extends BaseGeneticOperator implements Configurable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.49 $";

  /**
   * Calculator for dynamically determining the mutation rate. If set to
   * null the value of m_mutationRate will be used. Replaces the previously used
   * boolean m_dynamicMutationRate.
   */
  private IUniversalRateCalculator m_mutationRateCalc;

  private MutationOperatorConfigurable m_config = new
      MutationOperatorConfigurable();

  /**
   * Constructs a new instance of this MutationOperator without a specified
   * mutation rate, which results in dynamic mutation being turned on. This
   * means that the mutation rate will be automatically determined by this
   * operator based upon the number of genes present in the chromosomes.
   * <p>
   * Attention: The configuration used is the one set with the static method
   * Genotype.setConfiguration.
   *
   * @throws InvalidConfigurationException
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 1.0
   */
  public MutationOperator()
      throws InvalidConfigurationException {
    this(Genotype.getStaticConfiguration());
  }

  /**
   * @param a_conf the configuration to use
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public MutationOperator(final Configuration a_conf)
      throws InvalidConfigurationException {
    super(a_conf);
    setMutationRateCalc(new DefaultMutationRateCalculator(a_conf));
  }

  /**
   * Constructs a new instance of this MutationOperator with a specified
   * mutation rate calculator, which results in dynamic mutation being turned
   * on.
   * @param a_config the configuration to use
   * @param a_mutationRateCalculator calculator for dynamic mutation rate
   * computation
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public MutationOperator(final Configuration a_config,
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
   * @author Neil Rotstan
   * @since 1.1
   */
  public MutationOperator(final Configuration a_config,
                          final int a_desiredMutationRate)
      throws InvalidConfigurationException {
    super(a_config);
    m_config.m_mutationRate = a_desiredMutationRate;
    setMutationRateCalc(null);
  }

  /**
   * @param a_population the population of chromosomes from the current
   * evolution prior to exposure to any genetic operators. Chromosomes in this
   * array should not be modified. Please notice, that the call in
   * Genotype.evolve() to the implementations of GeneticOperator overgoes this
   * due to performance issues
   * @param a_candidateChromosomes the pool of chromosomes that have been
   * mutated
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 1.0
   */
  public void operate(final Population a_population,
                      final List a_candidateChromosomes) {
    if (a_population == null || a_candidateChromosomes == null) {
      // Population or candidate chromosomes list empty:
      // nothing to do.
      // -----------------------------------------------
      return;
    }
    if (m_config.m_mutationRate == 0 && m_mutationRateCalc == null) {
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
    //
    for (int i = 0; i < size; i++) {
      IChromosome chrom = a_population.getChromosome(i);
      Gene[] genes1 = chrom.getGenes();
      IChromosome copyOfChromosome = null;
      Gene[] genes = null;
      // For each Chromosome in the population...
      // ----------------------------------------
      for (int j = 0; j < genes1.length; j++) {
        if (m_mutationRateCalc != null) {
          // If it's a dynamic mutation rate then let the calculator decide
          // whether the current gene should be mutated.
          // --------------------------------------------------------------
          mutate = m_mutationRateCalc.toBePermutated(chrom, j);
        }
        else {
          // Non-dynamic, so just mutate based on the the current rate.
          // In fact we use a rate of 1/m_mutationRate.
          // ----------------------------------------------------------
          mutate = (generator.nextInt(m_config.m_mutationRate) == 0);
        }
        if (mutate) {
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
          // would be as many elements as the string is long , for an
          // IntegerGene, it is always one element.
          // --------------------------------------------------------------
          if (genes[j] instanceof ICompositeGene) {
            ICompositeGene compositeGene = (ICompositeGene) genes[j];
            if (m_monitorActive) {
              compositeGene.setUniqueIDTemplate(chrom.getGene(j).getUniqueID(), 1);
            }
            for (int k = 0; k < compositeGene.size(); k++) {
              mutateGene(compositeGene.geneAt(k), generator);
              if (m_monitorActive) {
                compositeGene.geneAt(k).setUniqueIDTemplate(
                    ( (ICompositeGene) chrom.getGene(j)).geneAt(k).getUniqueID(),
                    1);
              }
            }
          }
          else {
            mutateGene(genes[j], generator);
            if (m_monitorActive) {
              genes[j].setUniqueIDTemplate(chrom.getGene(j).getUniqueID(), 1);
            }
          }
        }
      }
    }
  }

  /**
   * Helper: mutate all atomic elements of a gene.
   *
   * @param a_gene the gene to be mutated
   * @param a_generator the generator delivering the amount of mutation
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  private void mutateGene(final Gene a_gene, final RandomGenerator a_generator) {
    for (int k = 0; k < a_gene.size(); k++) {
      // Retrieve value between 0 and 1 (not included) from generator.
      // Then map this value to range -1 and 1 (-1 included, 1 not).
      // -------------------------------------------------------------
      double percentage = -1 + a_generator.nextDouble() * 2;
      // Mutate atomic element by calculated percentage.
      // -----------------------------------------------
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
   *
   * @param a_mutationRateCalc MutationRateCalculator
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void setMutationRateCalc(final IUniversalRateCalculator
                                  a_mutationRateCalc) {
    m_mutationRateCalc = a_mutationRateCalc;
    if (m_mutationRateCalc != null) {
      m_config.m_mutationRate = 0;
    }
  }

  /**
   *
   * @param a_mutationRate new rate of mutation, expressed as
   * the denominator of the 1 / X fraction. For example, 1000 would result
   * in 1/1000 genes being mutated on average. A mutation rate of zero disables
   * mutation entirely
   *
   * @author Klaus Meffert
   * @since 3.2.2
   */
  public void setMutationRate(int a_mutationRate) {
    m_config.m_mutationRate = a_mutationRate;
    setMutationRateCalc(null);
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
    } catch (ClassCastException cex) {
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
    MutationOperator op = (MutationOperator) a_other;
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
    if (m_config.m_mutationRate != op.m_config.m_mutationRate) {
      if (m_config.m_mutationRate > op.m_config.m_mutationRate) {
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
    return m_config.m_mutationRate;
  }

  class MutationOperatorConfigurable
      implements java.io.Serializable {
    /**
     * The current mutation rate used by this MutationOperator, expressed as
     * the denominator in the 1 / X ratio. For example, X = 1000 would
     * mean that, on average, 1 / 1000 genes would be mutated. A value of zero
     * disables mutation entirely.
     */
    public int m_mutationRate;
  }
}
