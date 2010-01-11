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
 * This genetic operator performs Gaussian mutation across all genes in a
 * Chromosome.
 *
 * @author Klaus Meffert (modified JOONEGAP source)
 * @since 2.0
 */
public class GaussianMutationOperator
    extends BaseGeneticOperator {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.25 $";

  private double m_deviation;

  private RandomGenerator m_rg;

  /**
   * Constructs a GaussianMutationOperator with a default deviation of 0.05.
   * Attention: The configuration used is the one set with the static method
   * Genotype.setConfiguration.
   *
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public GaussianMutationOperator()
      throws InvalidConfigurationException {
    this(Genotype.getStaticConfiguration());
  }

  /**
   * Constructs a GaussianMutationOperator with a default deviation of 0.05.
   *
   * @param a_config the configuration to use
   *
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public GaussianMutationOperator(Configuration a_config)
      throws InvalidConfigurationException {
    this(a_config, 0.05d);
  }

  /**
   * Constructs a GaussianMutationOperator with the given deviation.
   *
   * @param a_configuration the configuration to use
   * @param a_deviation sic
   *
   * @throws InvalidConfigurationException
   *
   * @since 3.0 (since 2.0 without a_configuration)
   */
  public GaussianMutationOperator(final Configuration a_configuration,
                                  final double a_deviation)
      throws InvalidConfigurationException {
    super(a_configuration);
    m_deviation = a_deviation;
  }

  /**
   * Executes the operation.
   *
   * @param a_population containing chromosomes to be mutated
   * @param a_candidateChromosomes resulting chromosomes
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void operate(final Population a_population,
                      final List a_candidateChromosomes) {
    int size = Math.min(getConfiguration().getPopulationSize(),
                        a_population.size());
    if (m_rg == null) {
      RandomGenerator rn = getConfiguration().getRandomGenerator();
      m_rg = rn;
    }
    for (int i = 0; i < size; i++) {
      Gene[] genes = a_population.getChromosome(i).getGenes();
      IChromosome originalChrom = a_population.getChromosome(i);
      IChromosome copyOfChromosome = null;
      // For each Chromosome in the population...
      // ----------------------------------------
      for (int j = 0; j < genes.length; j++) {
        double nextGaussian = m_rg.nextDouble();
        double diff = nextGaussian * m_deviation;
        // ...take a copy of it...
        // -----------------------
        if (copyOfChromosome == null) {
          copyOfChromosome = (IChromosome) originalChrom.clone();
          // ...add it to the candidate pool...
          // ----------------------------------
          a_candidateChromosomes.add(copyOfChromosome);
          // ...then Gaussian mutate all its genes
          genes = copyOfChromosome.getGenes();
          // In case monitoring is active, support it.
          // -----------------------------------------
          if (m_monitorActive) {
            copyOfChromosome.setUniqueIDTemplate(originalChrom.getUniqueID(), 1);
          }
        }
        // Process all atomic elements in the gene. For a StringGene this
        // would be the length of the string, for an IntegerGene, it is
        // always one element.
        // --------------------------------------------------------------
        if (genes[j] instanceof CompositeGene) {
          CompositeGene compositeGene = (CompositeGene) genes[j];
          if (m_monitorActive) {
            compositeGene.setUniqueIDTemplate(originalChrom.getGene(j).
                getUniqueID(), 1);
          }
          for (int k = 0; k < compositeGene.size(); k++) {
            mutateGene(compositeGene.geneAt(k), diff);
            if (m_monitorActive) {
              compositeGene.geneAt(k).setUniqueIDTemplate(
                  ( (ICompositeGene) originalChrom.getGene(j)).geneAt(k).
                  getUniqueID(), 1);
            }
          }
        }
        else {
          mutateGene(genes[j], diff);
          if (m_monitorActive) {
            genes[j].setUniqueIDTemplate(originalChrom.getGene(j).getUniqueID(),
                1);
          }
        }
      }
    }
  }

  /**
   * Helper: mutate all atomic elements of a gene.
   *
   * @param a_gene the gene to be mutated
   * @param a_percentage the percentage the gene is to be mutated with
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  private void mutateGene(final Gene a_gene, final double a_percentage) {
    for (int k = 0; k < a_gene.size(); k++) {
      // Mutate atomic element by given percentage.
      // ------------------------------------------
      a_gene.applyMutation(k, a_percentage);
    }
  }

  /**
   * Compares the given GeneticOperator to this GeneticOperator.
   *
   * @param a_other the instance against which to compare this instance
   *
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
    GaussianMutationOperator op = (GaussianMutationOperator) a_other;
    if (m_deviation != op.m_deviation) {
      if (m_deviation > op.m_deviation) {
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
   * @return the deviation set
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public double getDeviation() {
    return m_deviation;
  }
}
