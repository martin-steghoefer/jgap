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

import org.jgap.*;
import org.jgap.event.*;

public class GABreeder
    extends BreederBase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.5 $";

  public GABreeder() {
    super();
  }

  public Population evolve(Population a_pop, Configuration config) {
    Population pop = a_pop;
    int originalPopSize = config.getPopulationSize();
    IChromosome fittest = null;
    // If first generation: Set age to one to allow genetic operations,
    // see CrossoverOperator for an illustration.
    // ----------------------------------------------------------------
    if (config.getGenerationNr() == 0) {
      int size = pop.size();
      for (int i = 0; i < size; i++) {
        IChromosome chrom = pop.getChromosome(i);
        chrom.increaseAge();
      }
    }
    else {
      // Select fittest chromosome in case it should be preserved and we are
      // not in the very first generation.
      // -------------------------------------------------------------------
      if (config.isPreserveFittestIndividual()) {
        /**@todo utilize jobs. In pop do also utilize jobs, especially for fitness
         * computation*/
        fittest = pop.determineFittestChromosome(0, originalPopSize - 1);
      }
    }
    // Adjust population size to configured size (if wanted).
    // Theoretically, this should be done at the end of this method.
    // But for optimization issues it is not. If it is the last call to
    // evolve() then the resulting population possibly contains more
    // chromosomes than the wanted number. But this is no bad thing as
    // more alternatives mean better chances having a fit candidate.
    // If it is not the last call to evolve() then the next call will
    // ensure the correct population size by calling keepPopSizeConstant.
    // ------------------------------------------------------------------
    if (config.isKeepPopulationSizeConstant()) {
      pop.keepPopSizeConstant();
    }
    int currentPopSize = pop.size();
    // Ensure all chromosomes are updated.
    // -----------------------------------
    BulkFitnessFunction bulkFunction =
        config.getBulkFitnessFunction();
    boolean bulkFitFunc = (bulkFunction != null);
    for (int i = 0; i < currentPopSize; i++) {
      IChromosome chrom = pop.getChromosome(i);
      if (!bulkFitFunc) {
        chrom.getFitnessValue();
      }
    }
    // Apply certain NaturalSelectors before GeneticOperators will be executed.
    // ------------------------------------------------------------------------
    pop = applyNaturalSelectors(config, pop, true);
    // Execute all of the Genetic Operators.
    // -------------------------------------
    applyGeneticOperators(config, pop);
    // Reset fitness value of genetically operated chromosomes.
    // Normally, this should not be necessary as the Chromosome
    // class initializes each newly created chromosome with
    // FitnessFunction.NO_FITNESS_VALUE. But who knows which
    // Chromosome implementation is used...
    // --------------------------------------------------------
    currentPopSize = pop.size();
    for (int i = originalPopSize; i < currentPopSize; i++) {
      IChromosome chrom = pop.getChromosome(i);
      chrom.setFitnessValueDirectly(FitnessFunction.NO_FITNESS_VALUE);
      // Mark chromosome as new-born.
      // ----------------------------
      chrom.resetAge();
      // Mark chromosome as being operated on.
      // -------------------------------------
      chrom.increaseOperatedOn();
    }
    // Increase age of all chromosomes which are not modified by genetic
    // operations.
    // -----------------------------------------------------------------
    for (int i = 0; i < originalPopSize; i++) {
      IChromosome chrom = pop.getChromosome(i);
      chrom.increaseAge();
      // Mark chromosome as not being operated on.
      // -----------------------------------------
      chrom.resetOperatedOn();
    }

    // Apply certain NaturalSelectors after GeneticOperators have been applied.
    // ------------------------------------------------------------------------
    pop = applyNaturalSelectors(config, pop, false);
    // If a bulk fitness function has been provided, call it.
    // ------------------------------------------------------
    if (bulkFunction != null) {
      /**@todo utilize jobs: bulk fitness function is not so important for a
       * prototype! */
      bulkFunction.evaluate(pop);
    }
    // Fill up population randomly if size dropped below specified percentage
    // of original size.
    // ----------------------------------------------------------------------
    if (config.getMinimumPopSizePercent() > 0) {
      int sizeWanted = config.getPopulationSize();
      int popSize;
      int minSize = (int) Math.round(sizeWanted *
                                     (double) config.getMinimumPopSizePercent()
                                     / 100);
      popSize = pop.size();
      if (popSize < minSize) {
        IChromosome newChrom;
        IChromosome sampleChrom = config.getSampleChromosome();
        Class sampleChromClass = sampleChrom.getClass();
        IInitializer chromIniter = config.getJGAPFactory().
            getInitializerFor(sampleChrom, sampleChromClass);
        while (pop.size() < minSize) {
          try {
            /**@todo utilize jobs: initialization may be time-consuming as
             * invalid combinations may have to be filtered out*/
            newChrom = (IChromosome) chromIniter.perform(sampleChrom,
                sampleChromClass, null);
            pop.addChromosome(newChrom);
          } catch (Exception ex) {
            throw new RuntimeException(ex);
          }
        }
      }
    }
    // Determine if all-time fittest chromosome is in the population.
    // --------------------------------------------------------------
    if (fittest != null && !pop.contains(fittest)) {
      // Re-add fittest chromosome to current population.
      // ------------------------------------------------
      pop.addChromosome(fittest);
    }
    // Increase number of generation.
    // ------------------------------
    config.incrementGenerationNr();
    // Fire an event to indicate we've performed an evolution.
    // -------------------------------------------------------
    config.getEventManager().fireGeneticEvent(
        new GeneticEvent(GeneticEvent.GENOTYPE_EVOLVED_EVENT, this));
    return pop;
  }

  /**
   * @return deep clone of this instance
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public Object clone() {
    return new GABreeder();
  }
}
