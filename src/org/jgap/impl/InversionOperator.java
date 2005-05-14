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
  private final static String CVS_REVISION = "$Revision: 1.1 $";

   /**
   * Constructs a new instance of this operator
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public InversionOperator() {
  }

  /**
   * @param a_population The population of chromosomes from the current
   *                     evolution prior to exposure to any genetic operators.
   *                     Chromosomes in this array should never be modified.
   * @param a_candidateChromosomes The pool of chromosomes that are candidates
   * for the next evolved population. Only these chromosomes will go to the
   * natural phase, so it's important to add any modified copies of Chromosomes
   * to this list if it's desired for them to be considered for natural
   * selection.
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public void operate(final Population a_population,
                      final List a_candidateChromosomes) {

    // Work out the number of crossovers that should be performed
    int size = Math.min(Genotype.getConfiguration().getPopulationSize(),
                        a_population.size());

    RandomGenerator generator = Genotype.getConfiguration().getRandomGenerator();
    // For the inversion, grab a random chromosome, pick a random
    // locus (gene location), and then swap that gene and all genes
    // to the "right" (those with greater loci) of that gene with the left
    // genes.
    // -------------------------------------------------------------------
    int index1, index2;

      index1 = generator.nextInt(size);
      Chromosome chrom1 = a_population.getChromosome(index1);
      Chromosome firstMate = (Chromosome)chrom1.clone();
      Gene[] firstGenes = firstMate.getGenes();
      int locus = generator.nextInt(firstGenes.length);
      // Swap the genes.
      // ---------------
      Gene[] invertedGenes = new Gene[firstGenes.length];
      Gene gene1;
      Gene gene2;
      Object firstAllele;
      int index = 0;
      for (int j = locus; j < firstGenes.length; j++) {
        invertedGenes[index++] = firstGenes[j];
      }
      for (int j = 0; j < locus; j++) {
        invertedGenes[index++] = firstGenes[j];
      }
      firstMate.setGenes(invertedGenes);

      // Add the modified chromosome to the candidate pool so that it'll be
      // considered for natural selection during the next phase of evolution.
      // --------------------------------------------------------------------
      a_candidateChromosomes.add(firstMate);
  }

}