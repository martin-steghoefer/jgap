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
import org.jgap.event.*;

/**
 * Genotype for GP Programs.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class GPGenotype
    extends Genotype implements Runnable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.17 $";

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
//  private static ProgramChromosome m_allTimeBest;
  private static GPProgram m_allTimeBest;

  /**
   * Is full mode with program construction allowed?
   */
  private boolean m_fullModeAllowed[];

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
   * @param a_configuration the configuration to use
   * @param a_population the initialized population to use
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public GPGenotype(GPConfiguration a_configuration,
                    GPPopulation a_population)
      throws InvalidConfigurationException {
    super(a_configuration);
    // Sanity checks: Make sure neither the Configuration, the array
    // of Chromosomes, nor any of the Genes inside the array are null.
    // ---------------------------------------------------------------
    if (a_configuration == null) {
      throw new IllegalArgumentException(
          "The Configuration instance may not be null.");
    }
    if (a_population == null) {
      throw new IllegalArgumentException(
          "The Population may not be null.");
    }
    for (int i = 0; i < a_population.size(); i++) {
      if (a_population.getGPProgram(i) == null) {
        throw new IllegalArgumentException(
            "The GPProgram instance at index " + i + " in population" +
            " is null, which is forbidden in general.");
      }
    }
    setPopulation(a_population);
    setConfiguration(a_configuration);
    // Lock the settings of the configuration object so that it cannot
    // be altered.
    // ---------------------------------------------------------------
    getConfiguration().lockSettings();
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
   * @param a_maxNodes reserve space for a_maxNodes number of nodes
   * @return created population
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public static GPGenotype randomInitialGenotype(final GPConfiguration a_conf,
      Class[] a_types, Class[][] a_argTypes, CommandGene[][] a_nodeSets,
      int a_maxNodes)
      throws InvalidConfigurationException {
    int[] minDepths = null;
    int[] maxDepths = null;
    return randomInitialGenotype(a_conf, a_types, a_argTypes, a_nodeSets,
                                 minDepths, maxDepths, a_maxNodes);
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
   * @param a_minDepths array of minimum depths to use: for each chromosome
   * one entry
   * @param a_maxDepths array of maximum depths to use: for each chromosome
   * one entry
   * @param a_maxNodes reserve space for a_maxNodes number of nodes
   * @return created population
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public static GPGenotype randomInitialGenotype(final GPConfiguration a_conf,
      Class[] a_types, Class[][] a_argTypes, CommandGene[][] a_nodeSets,
      int[] a_minDepths, int[] a_maxDepths, int a_maxNodes)
      throws InvalidConfigurationException {
    boolean[] fullModeAllowed = new boolean[a_types.length];
    for (int i = 0; i < a_types.length; i++) {
      fullModeAllowed[i] = true;
    }
    return randomInitialGenotype(a_conf, a_types, a_argTypes, a_nodeSets,
                                 a_minDepths, a_maxDepths, a_maxNodes,
                                 fullModeAllowed);
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
   * @param a_minDepths array of minimum depths to use: for each chromosome
   * one entry
   * @param a_maxDepths  array of maximum depths to use: for each chromosome
   * one entry
   * @param a_maxNodes reserve space for a_maxNodes number of nodes
   * @param a_fullModeAllowed array of boolean values. For each chromosome there
   * is one value indicating whether the full mode for creating chromosome
   * generations during evolution is allowed (true) or not (false)
   * @return created population
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public static GPGenotype randomInitialGenotype(final GPConfiguration a_conf,
      Class[] a_types, Class[][] a_argTypes, CommandGene[][] a_nodeSets,
      int[] a_minDepths, int[] a_maxDepths, int a_maxNodes,
      boolean[] a_fullModeAllowed)
      throws InvalidConfigurationException {
    System.gc();
    System.out.println("Memory consumed before creating population: "
                       + getTotalMemoryMB() + "MB");
    System.out.println("Creating initial population");
    GPPopulation pop = new GPPopulation(a_conf, a_conf.getPopulationSize());
    pop.create(a_types, a_argTypes, a_nodeSets, a_minDepths, a_maxDepths,
               a_maxNodes, a_fullModeAllowed);
    System.gc();
    System.out.println("Memory used after creating population: "
                       + getTotalMemoryMB() + "MB");
    GPGenotype gp = new GPGenotype(a_conf, pop);
    gp.m_fullModeAllowed = a_fullModeAllowed;
    return gp;
  }

  /**
   * @return total memory available by the VM in megabytes.
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public static double getTotalMemoryMB() {
    return (Runtime.getRuntime().totalMemory() / 1024 / 1024);
  }

  /**
   * @return free memory available in the VM in megabytes.
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public static double getFreeMemoryMB() {
    return (Runtime.getRuntime().freeMemory() / 1024 / 1024);
  }

  public static GPConfiguration getGPConfiguration() {
    return (GPConfiguration) getConfiguration();
  }

  static class GPFitnessComparator
      implements Comparator {
    public int compare(Object o1, Object o2) {
      if (! (o1 instanceof GPProgram) ||
          ! (o2 instanceof GPProgram))
        throw new ClassCastException(
            "FitnessComparator must operate on GPProgram's");
      double f1 = ( (GPProgram) o1).getFitnessValue();
      double f2 = ( (GPProgram) o2).getFitnessValue();
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
   * Evolves the population n times.
   *
   * @param a_evolutions number of evolution
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void evolve(int a_evolutions) {
    ( (GPPopulation) getPopulation()).sort(new GPFitnessComparator());
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
        System.out.println("Evolving generation " + i
                           + ", memory free: " + getFreeMemoryMB() + " MB");
      }
      evolve();
    }
    calcFitness();
  }

  /**
   * Calculates the fitness value of all programs, of the best solution as
   * well as the total fitness (sum of all fitness values).
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void calcFitness() {
    double totalFitness = 0.0d;
    for (int i = 0;
         i < getGPPopulation().size() && getGPPopulation().getGPProgram(i) != null;
         i++) {
      GPProgram program = getGPPopulation().getGPProgram(i);
      if (program.getFitnessValue() < 0.0d) {
        // Program wasn't reproduced from the previous generation.
        // -------------------------------------------------------
        System.err.println(" program.calcFitnessValue() begin: " + i);
        program.calcFitnessValue();
        System.err.println(" program.calcFitnessValue() end: " + i);
      }
      totalFitness += program.getFitnessValue();
    }
    m_totalFitness = totalFitness;
    GPProgram best = getGPPopulation().determineFittestProgram();
    /**@todo do something similar here as with Genotype.preserveFittestChromosome*/
    m_bestFitness = best.getFitnessValue();
    if (m_allTimeBest == null
        || m_bestFitness < m_allTimeBest.getFitnessValue()) {
      m_allTimeBest = best;
      /**@todo inform listeners*/
      outputSolution(best);
    }
  }

  /**
   * @return the all-time best solution found
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public GPProgram getAllTimeBest() {
    return m_allTimeBest;
  }

  /**
   * Outputs the best solution currently found.
   * @param best the fittest ProgramChromosome
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void outputSolution(GPProgram best) {
    System.out.println(" Best solution fitness: " + best.getFitnessValue());
    System.out.println(" Best solution: " + best.toString2(0));
    String depths = "";
    int size = best.size();
    for (int i = 0; i < size; i++) {
      if (i > 0) {
        depths += " / ";
      }
      depths += best.getChromosome(0).getDepth(0);
    }
    if (size == 1) {
      System.out.println(" Depths of chromosome: " + depths);
    }
    else {
      System.out.println(" Depths of chromosomes: " + depths);
    }
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
      GPPopulation oldPop = getGPPopulation();
      GPPopulation newPopulation = new GPPopulation(oldPop);
      float val;
      RandomGenerator random = getConfiguration().getRandomGenerator();
      /**@todo make configurable, reactivate*/
      int popSize1 = (int) Math.round(popSize * 0.7d);
      for (int i = 0; i < popSize1; i++) {
        // Clear the stack for each GP program (=ProgramChromosome).
        // ---------------------------------------------------------
        getGPConfiguration().clearStack();
        val = random.nextFloat();
        // Note that if we only have one slot left to fill, we don't do
        // crossover, but fall through to reproduction.
        // ------------------------------------------------------------
        if (i < popSize - 1 && val < getGPConfiguration().getCrossoverProb()) {
          // Do crossover.
          // -------------
          GPProgram i1 = getGPConfiguration().getSelectionMethod().
              select(this);
          GPProgram i2 = getGPConfiguration().getSelectionMethod().
              select(this);
          GPProgram[] newIndividuals = getGPConfiguration().
              getCrossMethod().operate(i1, i2);
          newPopulation.setGPProgram(i++, newIndividuals[0]);
          newPopulation.setGPProgram(i, newIndividuals[1]);
        }
        else if (val <
                 getGPConfiguration().getCrossoverProb() +
                 getGPConfiguration().getReproductionProb()) {
          // Reproduction only.
          // ------------------
          newPopulation.setGPProgram(i,
                                     getGPConfiguration().getSelectionMethod().
                                     select(this));
        }
      }
      // Add new chromosomes randomly.
      // -----------------------------
      for (int i = popSize1; i < popSize; i++) {
        // Determine depth randomly and between maxInitDepth and 2*maxInitDepth.
        // ---------------------------------------------------------------------
        int depth = getGPConfiguration().getMaxInitDepth() - 2
            + random.nextInt(2);
        GPProgram program = newPopulation.create(depth, (i % 2) == 0,
            m_fullModeAllowed);
        newPopulation.setGPProgram(i, program);
      }
      // Now set the new population as the active one.
      // ---------------------------------------------
      setPopulation(newPopulation);
      // Increase number of generation.
      // ------------------------------
      getConfiguration().incrementGenerationNr();
      // Fire an event to indicate we've performed an evolution.
      // -------------------------------------------------------
      getConfiguration().getEventManager().fireGeneticEvent(
          new GeneticEvent(GeneticEvent.GPGENOTYPE_EVOLVED_EVENT, this));
    } catch (InvalidConfigurationException iex) {
      // This should never happen.
      // -------------------------
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

  /**
   * Sample implementation of method to run GPGenotype as a thread.
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void run() {
    try {
      while (true) {
        evolve();
        calcFitness();
        // Pause between evolutions to avoid 100% CPU load.
        // ------------------------------------------------
        Thread.sleep(10);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * Retrieves the GPProgram in the population with the highest fitness
   * value.
   *
   * @return the GPProgram with the highest fitness value, or null if there
   * are no programs in this Genotype
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 1.0
   */
  public synchronized GPProgram getFittestProgram() {
    return getGPPopulation().determineFittestProgram();
  }
}
