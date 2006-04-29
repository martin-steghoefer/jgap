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
  private final static String CVS_REVISION = "$Revision: 1.4 $";

  private double m_bestFitness;

  private Map m_variables;

  private double m_totalFitness;

  private static ProgramChromosome m_allTimeBest;

  public GPGenotype(Population a_population)
      throws InvalidConfigurationException {
    this(GPGenotype.getGPConfiguration(), a_population);
  }

  public GPGenotype(GPConfiguration a_activeConfiguration,
                    Population a_population)
      throws InvalidConfigurationException {
    super(a_activeConfiguration, a_population);
    m_variables = new Hashtable();
  }

  public static Genotype randomInitialGenotype(Configuration
                                               a_activeConfiguration)
      throws InvalidConfigurationException {
    /**@todo not needed --> remove resp. provide it with base class*/
    return null;
  }

  /**
   * Creates the initial population for the world and computes the fitnesses
   * of all the individuals in the initial population.
   * <p>
   * Implementation note: the arguments of a chromosome, if any, are treated as
   * {@link com.groovyj.jgprog.functions.Variable Variable}s of name
   * "ARG"+argnum ARG0, ARG1, etc). These variables are automatically saved,
   * loaded before a call to the chromosome (via
   * {@link com.groovyj.jgprog.functions.ADF ADF}), and restored after the call.
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
  public static GPPopulation create(final GPConfiguration a_conf,
                                    Class[] a_types,
                                    Class[][] a_argTypes,
                                    CommandGene[][] a_nodeSets)
      throws InvalidConfigurationException {
    System.gc();
    System.out.println("Memory before create: "
                       + (Runtime.getRuntime().totalMemory() / 1024 / 1024) +
                       "M");
//    Object[] listeners = GPListeners.getListenerList();
//    for (int i = listeners.length - 1; i >= 0; i -= 2)
//      ( (GPListener) listeners[ i ]).setPopulationSize(popSize);
    System.out.println("Creating initial population");
    GPPopulation pop = new GPPopulation(a_conf, a_conf.getPopulationSize());
    pop.create(a_conf, a_types, a_argTypes, a_nodeSets);
    System.gc();
    System.out.println("Memory before computing initial fitnesses: "
                       + (Runtime.getRuntime().totalMemory() / 1024 / 1024) +
                       "M");
    return pop;
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
      return f1 > f2 ? 1 : (f1 == f2 ? 0 : -1);
//      return f1 > f2 ? -1 : (f1 == f2 ? 0 : 1);
    }
  }
  public void evolve(int n) {
    ( (GPPopulation) getPopulation()).sort(new FitnessComparator());
    //Here, we could do threading
    for (int i = 0; i < n; i++) {
      calcFitness();
      if (m_bestFitness < 0.000001) { /**@todo make configurable*/
        // Optimal solution found.
        return;
      }
      if (i % 25 == 0) { /**@todo make configurable*/
        System.out.println("Evolving generation " + i);
      }
      evolve();
    }
    calcFitness();
  }

  public void calcFitness() {
    double totalFitness = 0.0d;
    for (int i = 0; i < getPopulation().size(); i++) {
      IChromosome chrom = getPopulation().getChromosome(i);
      if (chrom.getFitnessValue() < 0.0d) {
        // Chromosome wasn't reproduced from the previous generation.
        try {
          chrom.setFitnessValue(chrom.getFitnessValue());
        }
        catch (Exception ex) {
          ex.printStackTrace();
          System.exit(1);
        }
      }
      else {
        /**@todo do crossing over etc.*/
      }
      totalFitness += chrom.getFitnessValue();
//      for (int j = listeners.length - 1; j >= 0; j -= 2)
//        ( (GPListener) listeners[ j ]).bumpEvaluationProgress();
    }
    m_totalFitness = totalFitness;
//    System.err.println("Total fitness: " + totalFitness);
    ProgramChromosome best = (ProgramChromosome) getPopulation().
        determineFittestChromosome();
    // do something siliar here as preserveFittestChromosome
    m_bestFitness = best.getFitnessValue();
    if (m_allTimeBest == null ||
        m_bestFitness < m_allTimeBest.getFitnessValue()) {
      if (Math.abs(m_bestFitness) < 0.000001) {
      }
      m_allTimeBest = best;
      outputSolution(best);
    }
  }

  public ProgramChromosome getAllTimeBest() {
    return m_allTimeBest;
  }

  public void outputSolution(ProgramChromosome best) {
    System.out.println(" Best solution fitness: " + best.getFitnessValue());
    System.out.println(" Best solution(normalized): " + best.toString2(0));
  }

  /**
   * Evolve the population by one generation. Probabilistically reproduces
   * and crosses individuals into a new population which then overwrites the
   * original population.
   *
   * @since 1.0
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
              getCrossMethod().cross(i1, i2);
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

  public double getTotalFitness() {
    return m_totalFitness;
  }
}
