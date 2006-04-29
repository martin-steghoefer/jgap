/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp;

import java.util.*;
import org.jgap.*;
import org.jgap.gp.*;
import org.jgap.gp.*;

/**
 * Genotype for GP Programs.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class GPGenotype
    extends Genotype {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.9 $";

  /**
   * Fitness value of the best solution.
   */
  private double m_bestFitness;

  /**
   * Sum of fitness values over all chromosomes.
   */
  private double m_totalFitness;

  /**
   * Best solution found.
   */
  private static ProgramChromosome m_allTimeBest;

  /**
   * Default constructor. Ony use with dynamic instantiation.
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public GPGenotype()
      throws InvalidConfigurationException {
    this(GPGenotype.getGPConfiguration(),
         new GPPopulation(GPGenotype.getGPConfiguration(),
                          GPGenotype.getGPConfiguration().getPopulationSize()));
  }

  /**
   * Preferred constructor to use, if not randomInitialGenotype.
   * @param a_activeConfiguration the configuration to use
   * @param a_population the initialized population to use
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public GPGenotype(GPConfiguration a_activeConfiguration,
                    Population a_population)
      throws InvalidConfigurationException {
    super(a_activeConfiguration, a_population);
  }

  /**
   * Creates a genotype with initial population for the world set.
   *
   * @param a_conf the configuration to use
   * @param a_types the type of each chromosome, the length is the number of
   * chromosomes
   * @param a_argTypes the types of the arguments to each chromosome, must be an
   * array of arrays, the first dimension of which is the number of chromosomes
   * and the second dimension of which is the number of arguments to the
   * chromosome
   * @param a_nodeSets the nodes which are allowed to be used by each
   * chromosome, must be an array of arrays, the first dimension of which is the
   * number of chromosomes and the second dimension of which is the number of
   * nodes. Note that it is not necessary to include the arguments of a
   * chromosome as terminals in the chromosome's node set. This is done
   * automatically
   * @return created population
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public static GPGenotype randomInitialGenotype(final GPConfiguration a_conf,
                                                 Class[] a_types,
                                                 Class[][] a_argTypes,
                                                 CommandGene[][] a_nodeSets)
      throws InvalidConfigurationException {
    /**@todo use listener*/
    System.gc();
    System.out.println("Memory consumed before creating population: "
                       + (Runtime.getRuntime().totalMemory() / 1024 / 1024) +
                       "M");
//    Object[] listeners = GPListeners.getListenerList();
//    for (int i = listeners.length - 1; i >= 0; i -= 2)
//      ( (GPListener) listeners[ i ]).setPopulationSize(popSize);
    System.out.println("Creating initial population");
    GPPopulation pop = new GPPopulation(a_conf, a_conf.getPopulationSize());
    pop.create(a_conf, a_types, a_argTypes, a_nodeSets);
    /**@todo use listener*/
    System.gc();
    System.out.println("Memory used after creating population: "
                       + (Runtime.getRuntime().totalMemory() / 1024 / 1024) +
                       "M");
    return new GPGenotype(a_conf, pop);
  }

  public static GPConfiguration getGPConfiguration() {
    return (GPConfiguration) getConfiguration();
  }

  static class FitnessComparator
      implements Comparator {
    public int compare(Object o1, Object o2) {
      if (! (o1 instanceof ProgramChromosome) ||
          ! (o2 instanceof ProgramChromosome))
        throw new ClassCastException(
            "FitnessComparator must operate on ProgramChromosomes");
      double f1 = ( (ProgramChromosome) o1).getFitnessValue();
      double f2 = ( (ProgramChromosome) o2).getFitnessValue();
      if (f1 > f2) {
        return 1;
      }
      else if (Math.abs(f1 - f2) < 0.000001) {
        return 0;
      }
      else {
        return -1;
      }
    }
  }

  /**
   * Evolves the population n times
   * @param a_evolutions number of evolution
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void evolve(int a_evolutions) {
    ( (GPPopulation) getPopulation()).sort(new FitnessComparator());
    // Here, we could do threading.
    for (int i = 0; i < a_evolutions; i++) {
      calcFitness();
      if (m_bestFitness < 0.000001) {
          /**@todo make configurable --> use listener*/
        // Optimal solution found, quit.
        // -----------------------------
        return;
      }
      if (i % 25 == 0) { /**@todo make configurable --> use listener*/
        System.out.println("Evolving generation " + i);
      }
      evolve();
    }
    calcFitness();
  }

  /**
   * Calculates the fitness value of all chromosomes, of the best solution as
   * well as the total fitness (sum of all fitness values).
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void calcFitness() {
    double totalFitness = 0.0d;
    for (int i = 0; i < getPopulation().size(); i++) {
      IChromosome chrom = getPopulation().getChromosome(i);
      if (chrom.getFitnessValue() < 0.0d) {
        // Chromosome wasn't reproduced from the previous generation.
        // ----------------------------------------------------------
        try {
          chrom.setFitnessValue(chrom.getFitnessValue());
        }
        catch (Exception ex) {
          ex.printStackTrace();
          System.exit(1);
        }
      }
      totalFitness += chrom.getFitnessValue();
//      for (int j = listeners.length - 1; j >= 0; j -= 2)
//        ( (GPListener) listeners[ j ]).bumpEvaluationProgress();
    }
    m_totalFitness = totalFitness;
    ProgramChromosome best = (ProgramChromosome) getPopulation().
        determineFittestChromosome();
    // Do something similar here as with Genotype.preserveFittestChromosome.
    m_bestFitness = best.getFitnessValue();
    if (m_allTimeBest == null ||
        m_bestFitness < m_allTimeBest.getFitnessValue()) {
      if (Math.abs(m_bestFitness) < 0.000001) {
      }
      m_allTimeBest = best;
      outputSolution(best);
    }
  }

  /**
   * @return the all-time best solution found
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public ProgramChromosome getAllTimeBest() {
    return m_allTimeBest;
  }

  /**
   * Outputs the best solution currently found.
   * @param best the fittest ProgramChromosome
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void outputSolution(ProgramChromosome best) {
    System.out.println(" Best solution fitness: " + best.getFitnessValue());
    System.out.println(" Best solution(normalized): " + best.toString2(0));
  }

  /**
   * Evolve the population by one generation. Probabilistically reproduces
   * and crosses individuals into a new population which then overwrites the
   * original population.
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void evolve() {
    try {
      int popSize = getGPConfiguration().getPopulationSize();
      GPPopulation newPopulation = new GPPopulation(getGPConfiguration(),
          popSize);
      float val;
      Random random = new Random();
//    Object[] listeners = GPListeners.getListenerList();
//    for (int i = listeners.length - 1; i >= 0; i -= 2)
//      ( (GPListener) listeners[i]).resetEvolutionProgress();
      for (int i = 0; i < popSize; i++) {
        val = random.nextFloat();
        // Note that if we only have one slot left to fill, we don't do
        // crossover, but fall through to reproduction.
        if (i < popSize - 1 && val < getGPConfiguration().getCrossoverProb()) {
          ProgramChromosome i1 = getGPConfiguration().getSelectionMethod().
              select(this);
          ProgramChromosome i2 = getGPConfiguration().getSelectionMethod().
              select(this);
          ProgramChromosome[] newIndividuals = getGPConfiguration().
              getCrossMethod().operate(i1, i2);
          newPopulation.setChromosome(i++, newIndividuals[0]);
          newPopulation.setChromosome(i, newIndividuals[1]);
//        for (int j = listeners.length - 1; j >= 0; j -= 2) {
//          ( (GPListener) listeners[j]).bumpEvolutionProgress();
//          ( (GPListener) listeners[j]).bumpEvolutionProgress();
//        }
        }
        else if (val <
                 getGPConfiguration().getCrossoverProb() +
                 getGPConfiguration().getReproductionProb()) {
          newPopulation.setChromosome(i,
                                      getGPConfiguration().getSelectionMethod().
                                      select(this));
//        for (int j = listeners.length - 1; j >= 0; j -= 2)
//          ( (GPListener) listeners[j]).bumpEvolutionProgress();
        }
      }
      setPopulation(newPopulation);
    }
    catch (InvalidConfigurationException iex) {
      // This should never happen.
      throw new IllegalStateException(iex.getMessage());
    }
  }

  public GPPopulation getGPPopulation() {
    return (GPPopulation)super.getPopulation();
  }

  /**
   * @return the total fitness, that is the fitness over all chromosomes
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public double getTotalFitness() {
    return m_totalFitness;
  }
}
