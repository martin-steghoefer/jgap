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
import org.jgap.audit.*;
import org.jgap.event.*;

/**
 * Breeder for genetic algorithms. Runs the evolution process.
 *
 * @author Klaus Meffert
 * @author Vasilis
 * @since 3.5
 */
public class GABreeder
    extends BreederBase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.21 $";

  private transient Configuration m_lastConf;

  private transient Population m_lastPop;

  //It contains clones of the chromosomes with the fitness value removed
  //We will use it to remove the duplicates
  List<IChromosome> m_allChromosomesSoFar;

  public GABreeder() {
    super();
    m_allChromosomesSoFar = new ArrayList<IChromosome> ();
  }

  /**
   * Evolves the population of chromosomes within a genotype. This will
   * execute all of the genetic operators added to the present active
   * configuration and then invoke the natural selector to choose which
   * chromosomes will be included in the next generation population.
   *
   * @param a_pop the population to evolve
   * @param a_conf the configuration to use for evolution
   *
   * @return evolved population
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public Population evolve(Population a_pop, Configuration a_conf) {
    Population pop = a_pop;
    BulkFitnessFunction bulkFunction = a_conf.getBulkFitnessFunction();
    boolean monitorActive = a_conf.getMonitor() != null;
    IChromosome fittest = null;
    // If first generation: Set age to one to allow genetic operations,
    // see CrossoverOperator for an illustration.
    // ----------------------------------------------------------------
    if (a_conf.getGenerationNr() == 0) {
      int size = pop.size();
      for (int i = 0; i < size; i++) {
        IChromosome chrom = pop.getChromosome(i);
        chrom.increaseAge();
      }
      // If a bulk fitness function has been provided, call it.
      // ------------------------------------------------------
      if (bulkFunction != null) {
        try {
          pop = bulkFunctionEvaluation(a_conf, bulkFunction, pop, monitorActive);
        } catch (InvalidConfigurationException ex) {
          throw new RuntimeException(ex);
        }
        // Increase number of generations.
        // -------------------------------
        a_conf.incrementGenerationNr();
        // Fire an event to indicate we've performed an evolution.
        // -------------------------------------------------------
        m_lastPop = pop;
        m_lastConf = a_conf;
        a_conf.getEventManager().fireGeneticEvent(
            new GeneticEvent(GeneticEvent.GENOTYPE_EVOLVED_EVENT, this));
        return pop;
      }
    }
    else {
      // Select fittest chromosome in case it should be preserved and we are
      // not in the very first generation.
      // -------------------------------------------------------------------
      if (a_conf.isPreserveFittestIndividual()) {
        /**@todo utilize jobs. In pop do also utilize jobs, especially for fitness
         * computation*/
        fittest = pop.determineFittestChromosome(0, pop.size() - 1);
      }
    }
    if (a_conf.getGenerationNr() > 0 && bulkFunction == null) {
      // Adjust population size to configured size (if wanted).
      // Theoretically, this should be done at the end of this method.
      // But for optimization issues it is not. If it is the last call to
      // evolve() then the resulting population possibly contains more
      // chromosomes than the wanted number. But this is no bad thing as
      // more alternatives mean better chances having a fit candidate.
      // If it is not the last call to evolve() then the next call will
      // ensure the correct population size by calling keepPopSizeConstant.
      // ------------------------------------------------------------------
      keepPopSizeConstant(pop, a_conf);
    }
    // Ensure fitness value of all chromosomes is udpated.
    // ---------------------------------------------------
    if (monitorActive) {
      // Monitor that fitness value of chromosomes is being updated.
      // -----------------------------------------------------------
      a_conf.getMonitor().event(
          IEvolutionMonitor.MONITOR_EVENT_BEFORE_UPDATE_CHROMOSOMES1,
          a_conf.getGenerationNr(), new Object[] {pop});
    }
    updateChromosomes(pop, a_conf);
    if (monitorActive) {
      // Monitor that fitness value of chromosomes is being updated.
      // -----------------------------------------------------------
      a_conf.getMonitor().event(
          IEvolutionMonitor.MONITOR_EVENT_AFTER_UPDATE_CHROMOSOMES1,
          a_conf.getGenerationNr(), new Object[] {pop});
    }
    // Apply certain NaturalSelectors before GeneticOperators will be executed.
    // ------------------------------------------------------------------------
    pop = applyNaturalSelectors(a_conf, pop, true);
    int newChromIndex = pop.size();
    // Execute all of the Genetic Operators.
    // -------------------------------------
    applyGeneticOperators(a_conf, pop);
    // Reset fitness value of genetically operated chromosomes.
    // Normally, this should not be necessary as the Chromosome class
    // initializes each newly created chromosome with
    // FitnessFunction.NO_FITNESS_VALUE. But who knows which Chromosome
    // implementation is used or if cloning is utilized.
    // ----------------------------------------------------------------
    int currentPopSize = pop.size();
    for (int i = newChromIndex; i < currentPopSize; i++) {
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
    int size = Math.min(newChromIndex, currentPopSize);
    for (int i = 0; i < size; i++) {
      IChromosome chrom = pop.getChromosome(i);
      chrom.increaseAge();
      // Mark chromosome as not being operated on.
      // -----------------------------------------
      chrom.resetOperatedOn();
    }
    // If a bulk fitness function has been provided, call it.
    // ------------------------------------------------------
    if (bulkFunction != null & a_conf.getGenerationNr() > 0) {
      try {
        pop = bulkFunctionEvaluation(a_conf, bulkFunction, pop, monitorActive);
      } catch (InvalidConfigurationException ex) {
        throw new RuntimeException(ex);
      }
    }
    // Ensure fitness value of all chromosomes is udpated.
    // ---------------------------------------------------
    if (monitorActive) {
      // Monitor that fitness value of chromosomes is being updated.
      // -----------------------------------------------------------
      a_conf.getMonitor().event(
          IEvolutionMonitor.MONITOR_EVENT_BEFORE_UPDATE_CHROMOSOMES2,
          a_conf.getGenerationNr(), new Object[] {pop});
    }
    updateChromosomes(pop, a_conf);
    if (monitorActive) {
      // Monitor that fitness value of chromosomes is being updated.
      // -----------------------------------------------------------
      a_conf.getMonitor().event(
          IEvolutionMonitor.MONITOR_EVENT_AFTER_UPDATE_CHROMOSOMES2,
          a_conf.getGenerationNr(), new Object[] {pop});
    }
    // Apply certain NaturalSelectors after GeneticOperators have been applied.
    // ------------------------------------------------------------------------
    pop = applyNaturalSelectors(a_conf, pop, false);
    // Fill up population randomly if size dropped below specified percentage
    // of original size.
    // ----------------------------------------------------------------------
    fillPopulationRandomlyToOriginalSize(a_conf, pop);
    IChromosome newFittest = reAddFittest(pop, fittest);
    if (monitorActive && newFittest != null) {
      // Monitor that fitness value of chromosomes is being updated.
      // -----------------------------------------------------------
      a_conf.getMonitor().event(
          IEvolutionMonitor.MONITOR_EVENT_READD_FITTEST,
          a_conf.getGenerationNr(), new Object[] {pop, fittest});
    }
    // Increase number of generations.
    // -------------------------------
    a_conf.incrementGenerationNr();
    // Fire an event to indicate we've performed an evolution.
    // -------------------------------------------------------
    m_lastPop = pop;
    m_lastConf = a_conf;
    a_conf.getEventManager().fireGeneticEvent(
        new GeneticEvent(GeneticEvent.GENOTYPE_EVOLVED_EVENT, this));
    return pop;
  }

  private void fillPopulationRandomlyToOriginalSize(Configuration a_conf,
      Population pop) {
    boolean monitorActive = a_conf.getMonitor() != null;
    // Fill up population randomly if size dropped below specified percentage
    // of original size.
    // ----------------------------------------------------------------------
    if (a_conf.getMinimumPopSizePercent() > 0) {
      int sizeWanted = a_conf.getPopulationSize();
      int popSize;
      int minSize = (int) Math.round(sizeWanted *
                                     (double) a_conf.getMinimumPopSizePercent()
                                     / 100);
      popSize = pop.size();
      if (popSize < minSize) {
        IChromosome newChrom;
        IChromosome sampleChrom = a_conf.getSampleChromosome();
        Class sampleChromClass = sampleChrom.getClass();
        IInitializer chromIniter = a_conf.getJGAPFactory().
            getInitializerFor(sampleChrom, sampleChromClass);
        while (pop.size() < minSize) {
          try {
            /**@todo utilize jobs as initialization may be time-consuming as
             * invalid combinations may have to be filtered out*/
            newChrom = (IChromosome) chromIniter.perform(sampleChrom,
                sampleChromClass, null);
            if (monitorActive) {
              // Monitor that fitness value of chromosomes is being updated.
              // -----------------------------------------------------------
              a_conf.getMonitor().event(
                  IEvolutionMonitor.MONITOR_EVENT_BEFORE_ADD_CHROMOSOME,
                  a_conf.getGenerationNr(), new Object[] {pop, newChrom});
            }
            pop.addChromosome(newChrom);
          } catch (Exception ex) {
            throw new RuntimeException(ex);
          }
        }
      }
    }
  }

  public Configuration getLastConfiguration() {
    return m_lastConf;
  }

  public Population getLastPopulation() {
    return m_lastPop;
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

  /**
   * Cares that population size is kept constant and does not exceed the desired
   * size.
   *
   * @param a_pop Population
   * @param a_conf Configuration
   */
  protected void keepPopSizeConstant(Population a_pop, Configuration a_conf) {
    if (a_conf.isKeepPopulationSizeConstant()) {
      try {
        a_pop.keepPopSizeConstant();
      } catch (InvalidConfigurationException iex) {
        throw new RuntimeException(iex);
      }
    }
  }

  protected IChromosome reAddFittest(Population a_pop, IChromosome a_fittest) {
    // Determine if all-time fittest chromosome is in the population.
    // --------------------------------------------------------------
    if (a_fittest != null && !a_pop.contains(a_fittest)) {
      // Re-add fittest chromosome to current population.
      // ------------------------------------------------
      a_pop.addChromosome(a_fittest);
      return a_fittest;
    }
    return null;
  }

  protected void updateChromosomes(Population a_pop, Configuration a_conf) {
    int currentPopSize = a_pop.size();
    // Ensure all chromosomes are updated.
    // -----------------------------------
    BulkFitnessFunction bulkFunction = a_conf.getBulkFitnessFunction();
    boolean bulkFitFunc = (bulkFunction != null);
    if (!bulkFitFunc) {
      for (int i = 0; i < currentPopSize; i++) {
        IChromosome chrom = a_pop.getChromosome(i);
        chrom.getFitnessValue();
      }
    }
  }

  private Population removeEvaluatedChromosomes(Population a_from_pop,
      Configuration a_config)
      throws InvalidConfigurationException {
    Population to_pop = new Population(a_config);
    IChromosome selectedChromosome;
    for (int i = 0; i < a_from_pop.size(); i++) {
      selectedChromosome = a_from_pop.getChromosome(i);
      if (selectedChromosome.getFitnessValueDirectly() ==
          FitnessFunction.NO_FITNESS_VALUE) {
        to_pop.addChromosome(selectedChromosome);
      }
    }
    return to_pop;
  }

  private Population removeChromosomesWithoutFitnessValue(Population a_from_pop,
      Configuration a_config)
      throws InvalidConfigurationException {
    Population to_pop = new Population(a_config);
    IChromosome selectedChromosome;
    for (int i = 0; i < a_from_pop.size(); i++) {
      selectedChromosome = a_from_pop.getChromosome(i);
      if (selectedChromosome.getFitnessValueDirectly() !=
          FitnessFunction.NO_FITNESS_VALUE) {
        to_pop.addChromosome(selectedChromosome);
      }
    }
    return to_pop;
  }

  public Population bulkFunctionEvaluation(Configuration a_conf,
      BulkFitnessFunction a_bulkFunction, Population a_pop,
      boolean a_monitorActive)
      throws InvalidConfigurationException {
    if (a_bulkFunction != null) {
      if (a_monitorActive) {
        // Monitor that bulk fitness will be called for evaluation.
        // --------------------------------------------------------
        a_conf.getMonitor().event(
            IEvolutionMonitor.MONITOR_EVENT_BEFORE_BULK_EVAL,
            a_conf.getGenerationNr(), new Object[] {a_bulkFunction, a_pop});
      }
      //remove chromosomes which have been already evaluated
      Population popForBulkFunction = removeEvaluatedChromosomes(a_pop, a_conf);
      if (!m_allChromosomesSoFar.isEmpty()) {
        Iterator it = popForBulkFunction.getChromosomes().iterator();
        //remove dublicates
        while (it.hasNext()) {
          IChromosome a_chrom1 = (IChromosome) it.next();
          if (m_allChromosomesSoFar.contains( (IChromosome) a_chrom1)) {
            it.remove();
          }
        }
      }
      fillPopulationRandomlyToOriginalSize(a_conf, popForBulkFunction);
      //When we call it here, it will remove non evaluated chromosomes
      keepPopSizeConstant(popForBulkFunction, a_conf);
      if (popForBulkFunction.size() > 0) {
        /**@todo utilize jobs: bulk fitness function is not so important for a
         * prototype! */
        a_bulkFunction.evaluate(popForBulkFunction);
      }
      //Remove the fitness value and add evaluated elements to
      //the List with NO_FITNESS_VALUE
      //and add the NEW elements to pop
      Iterator it2 = popForBulkFunction.getChromosomes().iterator();
      while (it2.hasNext()) {
        Chromosome chrom0 = (Chromosome) it2.next();
        Chromosome chrom = (Chromosome) chrom0.clone();
        chrom.setFitnessValueDirectly(FitnessFunction.NO_FITNESS_VALUE);
        m_allChromosomesSoFar.add(chrom);
        if (!a_pop.getChromosomes().contains( (IChromosome) chrom0)) {
          a_pop.addChromosome( (Chromosome) chrom0);
        }
      }
      //remove chromosomes which have been already evaluated
      a_pop = removeChromosomesWithoutFitnessValue(a_pop, a_conf);
      if (a_monitorActive) {
        // Monitor that bulk fitness has been called for evaluation.
        // ---------------------------------------------------------
        a_conf.getMonitor().event(
            IEvolutionMonitor.MONITOR_EVENT_AFTER_BULK_EVAL,
            a_conf.getGenerationNr(), new Object[] {a_bulkFunction, a_pop});
      }
    }
    return a_pop;
  }
}
