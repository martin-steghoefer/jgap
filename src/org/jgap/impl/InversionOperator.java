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
 * The inversion operator randomly selects one chromosomes from the
 * population and inverses it by randomly picking a splitting locus on which
 * to swap the first part with the last part of the chromosome.
 *
 * @author Klaus Meffert
 * @since 2.3
 */
public class InversionOperator
    implements GeneticOperator {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.5 $";

  /**
   * Constructs a new instance of this operator.
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public InversionOperator() {
  }

  /**
   * @author Klaus Meffert
   * @since 2.3
   */
  public void operate(final Population a_population,
                      final List a_candidateChromosomes) {
    // Work out the number of crossovers that should be performed.
    // -----------------------------------------------------------
    int size = Math.min(Genotype.getConfiguration().getPopulationSize(),
                        a_population.size());
    RandomGenerator generator = Genotype.getConfiguration().getRandomGenerator();
    // For the inversion, grab a random chromosome, pick a random
    // locus (gene location), and then swap that gene and all genes
    // to the "right" (those with greater loci) of that gene with the left
    // genes.
    // -------------------------------------------------------------------
    int index1;
    index1 = generator.nextInt(size);
    Chromosome chrom1 = a_population.getChromosome(index1);
    Chromosome firstMate = (Chromosome) chrom1.clone();
    Gene[] firstGenes = firstMate.getGenes();
    int locus = generator.nextInt(firstGenes.length);
    // Swap the genes.
    // ---------------
    Gene[] invertedGenes = new Gene[firstGenes.length];
    int index = 0;
    int len = firstGenes.length;
    for (int j = locus; j < len; j++) {
      invertedGenes[index++] = firstGenes[j];
    }
    for (int j = 0; j < locus; j++) {
      invertedGenes[index++] = firstGenes[j];
    }
    try {
      firstMate.setGenes(invertedGenes);
    }
    catch (InvalidConfigurationException cex) {
      // Rethrow to have an unchecked exception.
      // ---------------------------------------
      throw new Error(cex);
    }
    // Add the modified chromosome to the candidate pool so that it'll be
    // considered for natural selection during the next phase of evolution.
    // --------------------------------------------------------------------
    a_candidateChromosomes.add(firstMate);
  }
}
