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
 * This genetic operator performs Gaussian mutation across
 * all genes in a given Chromosome.
 * Taken from JOONEGAP
 *
 * @author Klaus Meffert (modified JOONEGAP source)
 * @since 2.0
 */
public class GaussianMutationOperator
    implements GeneticOperator {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.9 $";

  private double m_deviation;

  private RandomGenerator m_rg;

  /**
   * Constructs a GaussianMutationOperator with a default
   * deviation of 0.05.
   */
  public GaussianMutationOperator() {
    this(0.05d);
  }

  /**
   * Constructs a GaussianMutationOperator with the given deviation.
   * @param a_deviation sic.
   *
   * @since 2.0
   */
  public GaussianMutationOperator(double a_deviation) {
    m_deviation = a_deviation;
  }

  /**
   * Executes the operation.
   * @param a_population containing chromosomes to be mutated
   * @param a_candidateChromosomes resulting chromosomes
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void operate(final Population a_population,
                      List a_candidateChromosomes) {
    RandomGenerator rn = Genotype.getConfiguration().getRandomGenerator();
    /**@todo resolve this more clearly!*/
    if (rn instanceof GaussianRandomGenerator) {
      setRandomGenerator((GaussianRandomGenerator)rn);
    }
    else if (rn instanceof RandomGenerator) {
      setRandomGenerator(rn);
    }
    else {
      setRandomGenerator(new GaussianRandomGenerator(1.0d));
    }
    for (int i = 0; i < a_population.size(); i++) {
      Gene[] genes = a_population.getChromosome(i).getGenes();
      // clone the Chromosome
      Chromosome copyOfChromosome = (Chromosome) a_population.getChromosome(i).
          clone();
      // Add the Chromosome to the candidate pool
      a_candidateChromosomes.add(copyOfChromosome);
      // ...then Gaussian mutate all its genes
      genes = copyOfChromosome.getGenes();
      for (int j = 0; j < genes.length; j++) {
        double nextGaussian = m_rg.nextDouble();
        double diff = nextGaussian * m_deviation;
        mutateGene(genes[j], diff);
      }
    }
  }

  /**
   * Helper: mutate all atomic elements of a gene
   * @param a_gene the gene to be mutated
   * @param a_percentage the percentage the gene is to be mutated with
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  private void mutateGene(Gene a_gene, double a_percentage) {
    for (int k = 0; k < a_gene.size(); k++) {
      // Mutate atomic element by given percentage.
      // ------------------------------------------
      a_gene.applyMutation(k, a_percentage);
    }
  }

  private void setRandomGenerator(RandomGenerator a_rg) {
    m_rg = a_rg;
  }
}
