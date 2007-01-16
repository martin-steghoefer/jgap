/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.distr.grid;

import org.jgap.*;

/**
 * Default implementation of IGenotypeInitializer.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public class DefaultGenotypeInitializer
    implements IGenotypeInitializer {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  /**
   * Sets up a Genotype by adding the content of the given Population to it
   * and initializing the missing Chromosomes of the Genotype randomly.
   *
   * @param a_req a JGAPRequest object containing useful information
   * @param a_initialPop the Population to consider
   * @return the initialized Genotype
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public Genotype setupGenotype(JGAPRequest a_req, Population a_initialPop) {
    try {
      Genotype gen;
      Configuration conf = a_req.getConfiguration();
      if (a_initialPop == null || a_initialPop.size() < 1) {
        gen = Genotype.randomInitialGenotype(conf);
      }
      else {
        // Initialize genotype with given population.
        // ------------------------------------------
        gen = new Genotype(conf, a_initialPop);
        // Fill up population to get the desired size.
        // -------------------------------------------
        int size = conf.getPopulationSize() - a_initialPop.size();
        gen.fillPopulation(conf, gen.getPopulation(), conf.getSampleChromosome(),
                           size);
      }
      return gen;
    } catch (Throwable t) {
      throw new RuntimeException(t);
    }
  }
}
