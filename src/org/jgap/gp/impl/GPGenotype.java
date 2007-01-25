/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp.impl;

import java.io.*;
import java.util.*;
import org.jgap.*;
import org.jgap.gp.*;
import org.jgap.event.*;
import org.jgap.util.*;

/**
 * Genotype for GP Programs.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class GPGenotype
    implements Runnable, Serializable, Comparable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.15 $";

  /**
   * The array of GPProgram's that makeup the GPGenotype's population.
   */
  private GPPopulation m_population;

  /**
   * The current configuration instance.
   */
  private transient static GPConfiguration m_configuration;

  /**
   * Fitness value of the best solution.
   */
  private transient double m_bestFitness;

  /**
   * Sum of fitness values over all chromosomes.
   */
  private transient double m_totalFitness;

  /**
   * Best solution found.
   */
  private transient IGPProgram m_allTimeBest;

  /**
   * Is full mode with program construction allowed?
   */
  private boolean m_fullModeAllowed[];

  /**
   * Return type per chromosome.
   */
  private Class[] m_types;

  /**
   * Argument types for ADF's
   */
  private Class[][] m_argTypes;

  /**
   * Available GP-functions.
   */
  private CommandGene[][] m_nodeSets;

  /**
   * Minimum depth per each chromosome
   */
  private int[] m_minDepths;

  /**
   * Maximum depth per each chromosome
   */
  private int[] m_maxDepths;

  /**
   * Maximum number of nodes allowed per chromosome (when exceeded program
   * aborts)
   */
  private int m_maxNodes;

  /**
   * True: Output status information to console
   */
  private boolean m_verbose;

  /**
   * Default constructor. Ony use with dynamic instantiation.
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public GPGenotype()
      throws InvalidConfigurationException {
  }

  /**
   * Preferred constructor to use, if not randomInitialGenotype.
   *
   * @param a_configuration the configuration to use
   * @param a_population the initialized population to use
   * @param a_types the type for each chromosome, the length of the array
   * represents the number of chromosomes
   * @param a_argTypes the types of the arguments to each chromosome, must be an
   * array of arrays, the first dimension of which is the number of chromosomes
   * and the second dimension of which is the number of arguments to the
   * chromosome
   * @param a_nodeSets the nodes which are allowed to be used by each chromosome,
   * must be an array of arrays, the first dimension of which is the number of
   * chromosomes and the second dimension of which is the number of nodes
   * @param a_minDepths contains the minimum depth allowed for each chromosome
   * @param a_maxDepths contains the maximum depth allowed for each chromosome
   * @param a_maxNodes reserve space for a_maxNodes number of nodes
   *
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public GPGenotype(GPConfiguration a_configuration, GPPopulation a_population,
                    Class[] a_types, Class[][] a_argTypes,
                    CommandGene[][] a_nodeSets, int[] a_minDepths,
                    int[] a_maxDepths, int a_maxNodes)
      throws InvalidConfigurationException {
    // Sanity checks: Make sure neither the Configuration, the array
    // of Chromosomes, nor any of the Genes inside the array are null.
    // ---------------------------------------------------------------
    if (a_configuration == null) {
      throw new IllegalArgumentException(
          "The configuration instance may not be null.");
    }
    if (a_population == null) {
      throw new IllegalArgumentException(
          "The population may not be null.");
    }
    for (int i = 0; i < a_population.size(); i++) {
      if (a_population.getGPProgram(i) == null) {
        throw new IllegalArgumentException(
            "The GPProgram instance at index " + i + " in population" +
            " is null, which is forbidden in general.");
      }
    }
    m_types = a_types;
    m_argTypes = a_argTypes;
    m_nodeSets = a_nodeSets;
    m_maxDepths = a_maxDepths;
    m_minDepths = a_minDepths;
    m_maxNodes = a_maxNodes;
    setGPPopulation(a_population);
    setGPConfiguration(a_configuration);
    // Lock the settings of the configuration object so that it cannot
    // be altered.
    // ---------------------------------------------------------------
    getGPConfiguration().lockSettings();
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
   * @param a_verboseOutput true: output status information to console
   *
   * @return created population
   *
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public static GPGenotype randomInitialGenotype(final GPConfiguration a_conf,
      Class[] a_types, Class[][] a_argTypes, CommandGene[][] a_nodeSets,
      int a_maxNodes, boolean a_verboseOutput)
      throws InvalidConfigurationException {
    int[] minDepths = null;
    int[] maxDepths = null;
    return randomInitialGenotype(a_conf, a_types, a_argTypes, a_nodeSets,
                                 minDepths, maxDepths, a_maxNodes,
                                 a_verboseOutput);
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
   * @param a_verboseOutput true: output status information to console
   *
   * @return created population
   *
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public static GPGenotype randomInitialGenotype(final GPConfiguration a_conf,
      Class[] a_types, Class[][] a_argTypes, CommandGene[][] a_nodeSets,
      int[] a_minDepths, int[] a_maxDepths, int a_maxNodes,
      boolean a_verboseOutput)
      throws InvalidConfigurationException {
    boolean[] fullModeAllowed = new boolean[a_types.length];
    for (int i = 0; i < a_types.length; i++) {
      fullModeAllowed[i] = true;
    }
    return randomInitialGenotype(a_conf, a_types, a_argTypes, a_nodeSets,
                                 a_minDepths, a_maxDepths, a_maxNodes,
                                 fullModeAllowed, a_verboseOutput);
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
   * @param a_verboseOutput true: output status information to console
   *
   * @return created population
   *
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public static GPGenotype randomInitialGenotype(final GPConfiguration a_conf,
      Class[] a_types, Class[][] a_argTypes, CommandGene[][] a_nodeSets,
      int[] a_minDepths, int[] a_maxDepths, int a_maxNodes,
      boolean[] a_fullModeAllowed, boolean a_verboseOutput)
      throws InvalidConfigurationException {
    System.gc();
    if(a_verboseOutput) {
      System.out.println("Creating initial population");
      System.out.println("Memory consumed before creating population: "
                         + SystemKit.getTotalMemoryMB() + "MB");
    }
    GPPopulation pop = new GPPopulation(a_conf, a_conf.getPopulationSize());
    // Create initial population.
    // --------------------------
    pop.create(a_types, a_argTypes, a_nodeSets, a_minDepths, a_maxDepths,
               a_maxNodes, a_fullModeAllowed);
    System.gc();
    if(a_verboseOutput) {
      System.out.println("Memory used after creating population: "
                         + SystemKit.getTotalMemoryMB() + "MB");
    }
    GPGenotype gp = new GPGenotype(a_conf, pop, a_types, a_argTypes, a_nodeSets,
                                   a_minDepths, a_maxDepths, a_maxNodes);
    gp.m_fullModeAllowed = a_fullModeAllowed;
    return gp;
  }

  public static GPConfiguration getGPConfiguration() {
    return m_configuration;
  }

  static class GPFitnessComparator
      implements Comparator {
    public int compare(Object o1, Object o2) {
      if (! (o1 instanceof IGPProgram) ||
          ! (o2 instanceof IGPProgram))
        throw new ClassCastException(
            "FitnessComparator must operate on IGPProgram instances");
      double f1 = ( (IGPProgram) o1).getFitnessValue();
      double f2 = ( (IGPProgram) o2).getFitnessValue();
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
    getGPPopulation().sort(new GPFitnessComparator());
    // Here, we could do threading.
    for (int i = 0; i < a_evolutions; i++) {
      calcFitness();
      if (m_bestFitness < 0.000001) {
        // Optimal solution found, quit.
        // -----------------------------
        return;
      }
      if(m_verbose) {
        if (i % 25 == 0) {
          System.out.println("Evolving generation "
                             + i
                             + ", memory free: "
                             + SystemKit.getFreeMemoryMB()
                             + " MB");
        }
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
    GPPopulation pop = getGPPopulation();
    IGPProgram best = null;
    IGPFitnessEvaluator evaluator = getGPConfiguration().getGPFitnessEvaluator();
    m_bestFitness = FitnessFunction.NO_FITNESS_VALUE;
    for (int i = 0; i < pop.size() && pop.getGPProgram(i) != null; i++) {
      IGPProgram program = pop.getGPProgram(i);
      double fitness =program.getFitnessValue();
      if (best == null || evaluator.isFitter(fitness, m_bestFitness)){
        best = program;
        m_bestFitness = fitness;
      }
      totalFitness += fitness;
    }
    m_totalFitness = totalFitness;
//    best = pop.determineFittestProgram();
//    m_bestFitness = best.getFitnessValue();
    /**@todo do something similar here as with Genotype.preserveFittestChromosome*/
    if (m_allTimeBest == null
        || evaluator.isFitter(m_bestFitness,  m_allTimeBest.getFitnessValue())) {
      m_allTimeBest = best;
      // Fire an event to indicate a new best solution.
      // ----------------------------------------------
      getGPConfiguration().getEventManager().fireGeneticEvent(
          new GeneticEvent(GeneticEvent.GPGENOTYPE_NEW_BEST_SOLUTION, this));
      if(m_verbose) {
        // Output the new best solution found.
        // -----------------------------------
        outputSolution(best);
      }
    }
  }

  /**
   * @return the all-time best solution found
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public IGPProgram getAllTimeBest() {
    return m_allTimeBest;
  }

  /**
   * Outputs the best solution currently found.
   * @param a_best the fittest ProgramChromosome
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void outputSolution(IGPProgram a_best) {
    System.out.println(" Best solution fitness: " + a_best.getFitnessValue());
    System.out.println(" Best solution: " + a_best.toStringNorm(0));
    String depths = "";
    int size = a_best.size();
    for (int i = 0; i < size; i++) {
      if (i > 0) {
        depths += " / ";
      }
      depths += a_best.getChromosome(i).getDepth(0);
    }
    if (size == 1) {
      System.out.println(" Depth of chromosome: " + depths);
    }
    else {
      System.out.println(" Depths of chromosomes: " + depths);
    }
    System.out.println(" --------");
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
      RandomGenerator random = getGPConfiguration().getRandomGenerator();
      GPConfiguration conf = getGPConfiguration();
      // Determine how many new individuals will be added to the new generation.
      // -----------------------------------------------------------------------
      int popSize1 = (int) Math.round(popSize * (1 - conf.getNewChromsPercent()));
      for (int i = 0; i < popSize1; i++) {
        // Clear the stack for each GP program (=ProgramChromosome).
        // ---------------------------------------------------------
        getGPConfiguration().clearStack();
        val = random.nextFloat();
        // Note that if we only have one slot left to fill, we don't do
        // crossover, but fall through to reproduction.
        // ------------------------------------------------------------
        if (i < popSize - 1 && val < conf.getCrossoverProb()) {
          // Do crossover.
          // -------------
          IGPProgram i1 = conf.getSelectionMethod().select(this);
          IGPProgram i2 = conf.getSelectionMethod().select(this);
          int tries = 0;
          do {
            try {
              IGPProgram[] newIndividuals = conf.getCrossMethod().operate(i1,
                  i2);
              newPopulation.setGPProgram(i, newIndividuals[0]);
              newPopulation.setGPProgram(i+1, newIndividuals[1]);
              i++;
              break;
            } catch (IllegalStateException iex) {
              tries++;
              if (tries >= getGPConfiguration().getProgramCreationMaxtries()) {
                if (!getGPConfiguration().isMaxNodeWarningPrinted()) {
                  System.err.println(
                      "Warning: Maximum number of nodes allowed may be too small");
                  getGPConfiguration().flagMaxNodeWarningPrinted();
                  // Try cloning a previously generated valid program.
                  // -------------------------------------------------
                  IGPProgram program = cloneProgram(getGPConfiguration().
                      getPrototypeProgram());
                  if (program != null) {
                    newPopulation.setGPProgram(i++, program);
                    program = cloneProgram(getGPConfiguration().
                        getPrototypeProgram());
                    newPopulation.setGPProgram(i, program);
                    break;
                  }
                  else {
                    throw new IllegalStateException(iex.getMessage());
                  }
                }
              }
            }
          } while (true);
        }
        else if (val < conf.getCrossoverProb() + conf.getReproductionProb()) {
          // Reproduction only.
          // ------------------
          newPopulation.setGPProgram(i, conf.getSelectionMethod().select(this));
        }
      }
      // Add new chromosomes randomly.
      // -----------------------------
      for (int i = popSize1; i < popSize; i++) {
        // Determine depth randomly and between maxInitDepth and 2*maxInitDepth.
        // ---------------------------------------------------------------------
        int depth = conf.getMaxInitDepth() - 2 +
            random.nextInt(2); /**@todo make configurable*/
        int tries = 0;
        do {
          try {
            IGPProgram program = newPopulation.create(m_types, m_argTypes,
                m_nodeSets, m_minDepths, m_maxDepths, depth, (i % 2) == 0,
                m_maxNodes, m_fullModeAllowed);
            newPopulation.setGPProgram(i, program);
            break;
          } catch (IllegalStateException iex) {
            tries++;
            if (tries > getGPConfiguration().getProgramCreationMaxtries()) {
              // Try cloning a previously generated valid program.
              // -------------------------------------------------
              IGPProgram program = cloneProgram(getGPConfiguration().
                  getPrototypeProgram());
              if (program != null) {
                newPopulation.setGPProgram(i, program);
                break;
              }
              else {
                throw new IllegalStateException(iex.getMessage());
              }
            }
          }
        } while (true);
      }
      // Now set the new population as the active one.
      // ---------------------------------------------
      setGPPopulation(newPopulation);
      // Increase number of generation.
      // ------------------------------
      conf.incrementGenerationNr();
      // Fire an event to indicate we've performed an evolution.
      // -------------------------------------------------------
      conf.getEventManager().fireGeneticEvent(
          new GeneticEvent(GeneticEvent.GPGENOTYPE_EVOLVED_EVENT, this));
    } catch (InvalidConfigurationException iex) {
      // This should never happen.
      // -------------------------
      throw new IllegalStateException(iex.getMessage());
    }
  }

  public GPPopulation getGPPopulation() {
    return m_population;
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
   * Default implementation of method to run GPGenotype as a thread.
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void run() {
    try {
      while (!Thread.currentThread().interrupted()) {
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
   * @author Klaus Meffert
   * @since 3.0
   */
  public synchronized IGPProgram getFittestProgram() {
    return getGPPopulation().determineFittestProgram();
  }

  protected void setGPPopulation(GPPopulation a_pop) {
    m_population = a_pop;
  }

  /**
   * Sets the configuration to use with the Genetic Algorithm.
   * @param a_configuration the configuration to use
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public static void setGPConfiguration(GPConfiguration a_configuration) {
    m_configuration = a_configuration;
  }

  /**
   * Compares this entity against the specified object.
   *
   * @param a_other the object to compare against
   * @return true: if the objects are the same, false otherwise
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public boolean equals(Object a_other) {
    try {
      return compareTo(a_other) == 0;
    } catch (ClassCastException cex) {
      return false;
    }
  }

  /**
   * Compares this Genotype against the specified object. The result is true
   * if the argument is an instance of the Genotype class, has exactly the
   * same number of programs as the given Genotype, and, for each
   * GPProgram in this Genotype, there is an equal program in the
   * given Genotype. The programs do not need to appear in the same order
   * within the populations.
   *
   * @param a_other the object to compare against
   * @return a negative number if this genotype is "less than" the given
   * genotype, zero if they are equal to each other, and a positive number if
   * this genotype is "greater than" the given genotype
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public int compareTo(Object a_other) {
    try {
      // First, if the other Genotype is null, then they're not equal.
      // -------------------------------------------------------------
      if (a_other == null) {
        return 1;
      }
      GPGenotype otherGenotype = (GPGenotype) a_other;
      // First, make sure the other Genotype has the same number of
      // chromosomes as this one.
      // ----------------------------------------------------------
      int size1 = getGPPopulation().size();
      int size2 = otherGenotype.getGPPopulation().size();
      if (size1 != size2) {
        if (size1 > size2) {
          return 1;
        }
        else {
          return -1;
        }
      }
      // Next, prepare to compare the programs of the other Genotype
      // against the programs of this Genotype. To make this a lot
      // simpler, we first sort the programs in both this Genotype
      // and the one we're comparing against. This won't affect the
      // genetic algorithm (it doesn't care about the order), but makes
      // it much easier to perform the comparison here.
      // --------------------------------------------------------------
      Arrays.sort(getGPPopulation().getGPPrograms());
      Arrays.sort(otherGenotype.getGPPopulation().getGPPrograms());
      for (int i = 0; i < getGPPopulation().size(); i++) {
        int result = (getGPPopulation().getGPProgram(i).compareTo(
            otherGenotype.getGPPopulation().getGPProgram(i)));
        if (result != 0) {
          return result;
        }
      }
      return 0;
    } catch (ClassCastException e) {
      return -1;
    }
  }

  /***
   * Hashcode function for the genotype, tries to create a unique hashcode for
   * the chromosomes within the population. The logic for the hashcode is
   *
   * Step  Result
   * ----  ------
   *    1  31*0      + hashcode_0 = y(1)
   *    2  31*y(1)   + hashcode_1 = y(2)
   *    3  31*y(2)   + hashcode_2 = y(3)
   *    n  31*y(n-1) + hashcode_n-1 = y(n)
   *
   * Each hashcode is a number and the binary equivalent is computed and
   * returned.
   * @return the computed hashcode
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public int hashCode() {
    int i, size = getGPPopulation().size();
    IGPProgram prog;
    int twopower = 1;
    // For empty genotype we want a special value different from other hashcode
    // implementations.
    // ------------------------------------------------------------------------
    int localHashCode = -573;
    for (i = 0; i < size; i++, twopower = 2 * twopower) {
      prog = getGPPopulation().getGPProgram(i);
      localHashCode = 31 * localHashCode + prog.hashCode();
    }
    return localHashCode;
  }

  /**
   * @param a_verbose true: output status information to console
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void setVerboseOutput(boolean a_verbose) {
    m_verbose = a_verbose;
  }

  private IGPProgram cloneProgram(IGPProgram a_original) {
    IGPProgram validProgram = getGPConfiguration().
        getPrototypeProgram();
    ICloneHandler cloner = getGPConfiguration().getJGAPFactory().
        getCloneHandlerFor(validProgram, null);
    if (cloner != null) {
      try {
        IGPProgram program = (IGPProgram) cloner.perform(
            validProgram, null, null);
        return program;
      } catch (Exception ex) {
        return null;
      }
    }
    return null;
  }
}
