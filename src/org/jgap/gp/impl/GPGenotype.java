/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp.impl;

import java.io.*;
import java.util.*;

import org.apache.log4j.*;
import org.jgap.*;
import org.jgap.distr.grid.gp.*;
import org.jgap.event.*;
import org.jgap.gp.*;
import org.jgap.gp.terminal.*;
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
  private final static String CVS_REVISION = "$Revision: 1.60 $";

  private transient static Logger LOGGER = Logger.getLogger(GPGenotype.class);

  /**
   * The array of GPProgram's that make-up this GPGenotype's population
   */
  private GPPopulation m_population;

  /**
   * The current configuration instance
   */
  private GPConfiguration m_configuration;

  private transient static GPConfiguration m_staticConfiguration;

  /**
   * Fitness value of the best solution
   */
  private double m_bestFitness;

  /**
   * Sum of fitness values over all chromosomes
   */
  private double m_totalFitness;

  /**
   * Best solution found
   */
  private IGPProgram m_allTimeBest;

  private double m_allTimeBestFitness;

  /**
   * Is full mode with program construction allowed?
   */
  private boolean m_fullModeAllowed[];

  /**
   * Return type per chromosome
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

  private Map m_variables;

  private IGPProgram m_fittestToAdd;

  private boolean m_cloneWarningGPProgramShown;

//  private boolean[] disabledChromosomes;

  /**
   * Default constructor. Ony use with dynamic instantiation.
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public GPGenotype()
      throws InvalidConfigurationException {
    init();
  }

  /**
   * Preferred constructor to use, if not using the static method
   * randomInitialGenotype.
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
    this(a_configuration, a_population, a_types, a_argTypes, a_nodeSets,
         a_minDepths, a_maxDepths, a_maxNodes, null);
  }

  /**
   * See above constructor. Parameter a_popCreator is used in grid context only.
   *
   * @param a_configuration GPConfiguration
   * @param a_population GPPopulation
   * @param a_types Class[]
   * @param a_argTypes Class[][]
   * @param a_nodeSets CommandGene[][]
   * @param a_minDepths int[]
   * @param a_maxDepths int[]
   * @param a_maxNodes int
   * @param a_popCreator IPopulationCreator
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.6
   */
  public GPGenotype(GPConfiguration a_configuration, GPPopulation a_population,
                    Class[] a_types, Class[][] a_argTypes,
                    CommandGene[][] a_nodeSets, int[] a_minDepths,
                    int[] a_maxDepths, int a_maxNodes,
                    IGPPopulationInitializer a_popCreator)
      throws InvalidConfigurationException {
    // Sanity checks: Make sure neither the Configuration, the array
    // of Chromosomes, nor any of the Genes inside the array are null.
    // ---------------------------------------------------------------
    if (a_configuration == null) {
      throw new IllegalArgumentException(
          "The configuration instance must not be null.");
    }
    setGPConfiguration(a_configuration);
    if (a_population == null) {
      throw new IllegalArgumentException(
          "The population must not be null.");
    }
    setGPPopulation(a_population);
    if(a_popCreator != null) {
      // Initialize the population (normally only occurs for grid workers).
      // ------------------------------------------------------------------
      GPGenotype gen = a_popCreator.execute();
      GPPopulation newPopulation = gen.getGPPopulation();
      if (a_configuration.getPopulationSize() >= a_population.size()) {
        IGPProgram[] programs = new IGPProgram[a_configuration.
            getPopulationSize()];
        System.arraycopy(newPopulation.getGPPrograms(), 0, programs, 0,
                         newPopulation.getGPPrograms().length);
        a_population.setGPPrograms(programs);
      }
    }
    for (int i = 0; i < a_population.size(); i++) {
      if (a_population.getGPProgram(i) == null) {
        throw new IllegalArgumentException(
            "The GPProgram instance at index " + i + " in population" +
            " is null, which is forbidden in general.");
      }
    }
    init();
    m_types = a_types;
    m_argTypes = a_argTypes;
    m_nodeSets = a_nodeSets;
    m_maxDepths = a_maxDepths;
    m_minDepths = a_minDepths;
    m_maxNodes = a_maxNodes;
    m_variables = new Hashtable();
    m_allTimeBestFitness = FitnessFunction.NO_FITNESS_VALUE;
    // Lock the settings of the configuration object so that it cannot
    // be altered.
    // ---------------------------------------------------------------
    getGPConfiguration().lockSettings();
  }

  protected void init() {
//    disabledChromosomes = new boolean[100];
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
   * @return created genotype with initialized population
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
   * @return created genotype with initialized population
   *
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public static GPGenotype randomInitialGenotype(final GPConfiguration a_conf,
      Class[] a_types,
      Class[][] a_argTypes,
      CommandGene[][] a_nodeSets,
      int[] a_minDepths,
      int[] a_maxDepths,
      int a_maxNodes,
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
   * Creates a genotype with a randomly created initial population.
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
   * @return created genotype with initialized population
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
    return randomInitialGenotype(a_conf, a_types, a_argTypes, a_nodeSets,
                                 a_minDepths, a_maxDepths, a_maxNodes,
                                 a_fullModeAllowed, a_verboseOutput,
                                 new DefaultPopulationCreator());
  }

  /**
   * Allows to use a custom mechanism for population creation.
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
   * @param a_popCreator mechanism fior creating the population
   *
   * @return GPGenotype
   *
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.2.2
   */
  public static GPGenotype randomInitialGenotype(final GPConfiguration a_conf,
      Class[] a_types, Class[][] a_argTypes, CommandGene[][] a_nodeSets,
      int[] a_minDepths, int[] a_maxDepths, int a_maxNodes,
      boolean[] a_fullModeAllowed, boolean a_verboseOutput,
      IPopulationCreator a_popCreator)
      throws InvalidConfigurationException {
    // Check preconditions.
    // --------------------
    if (a_argTypes.length != a_fullModeAllowed.length
        || (a_minDepths != null && a_argTypes.length != a_minDepths.length)
        || (a_maxDepths != null && a_argTypes.length != a_maxDepths.length)
        || a_argTypes.length != a_types.length) {
      throw new IllegalArgumentException("a_argTypes must have same length as"
          + " a_types, a_minDepths, a_maxDepths and a_fullModeAllowed");
    }
    if (a_conf.getPopulationSize() < 1) {
      throw new IllegalArgumentException("Set the population size in the"
          + " configuration!");
    }
    // Clean up memory.
    // ----------------
    System.gc();
    if (a_verboseOutput) {
      LOGGER.info("Creating initial population");
      LOGGER.info("Mem free: "
                  + SystemKit.niceMemory(SystemKit.getTotalMemoryMB()) + " MB");
    }
    // Create initial population.
    // --------------------------
    GPPopulation pop = new GPPopulation(a_conf, a_conf.getPopulationSize());
    try {
      a_popCreator.initialize(pop, a_types, a_argTypes, a_nodeSets, a_minDepths,
                              a_maxDepths, a_maxNodes, a_fullModeAllowed);
    } catch (Exception ex) {
      throw new InvalidConfigurationException(ex);
    }
    // Clean up memory.
    // ----------------
    System.gc();
    if (a_verboseOutput) {
      LOGGER.info("Mem free after creating population: "
                  + SystemKit.niceMemory(SystemKit.getTotalMemoryMB()) + " MB");
    }
    checkErroneousPop(pop, " after creating population/2");
    // Determine which GP functions are never used as child.
    // -----------------------------------------------------
    Map<String, CommandGene> invalidNodes = verifyChildNodes(a_conf, a_types,
        a_nodeSets);
    outputWarning(invalidNodes);
    // Determine the depths for which GP functions are not possible.
    // -------------------------------------------------------------
    Map<CommandGene, int[]>
        invalidDepthNodes = verifyDepthsForNodes(pop, a_conf,
        a_types, a_minDepths, a_maxDepths, a_maxNodes, a_nodeSets);
    outputDepthInfo(invalidDepthNodes);
    /**@todo remove unused nodes from configuration*/

    GPGenotype gp = new GPGenotype(a_conf, pop, a_types, a_argTypes, a_nodeSets,
                                   a_minDepths, a_maxDepths, a_maxNodes);
    gp.m_fullModeAllowed = a_fullModeAllowed;
    // Publish GP variables to configuration to make them accessible globally.
    // -----------------------------------------------------------------------
    Iterator it = gp.m_variables.keySet().iterator();
    while (it.hasNext()) {
      /**@todo optimize access to map*/
      String varName = (String) it.next();
      Variable var = (Variable) gp.m_variables.get(varName);
      a_conf.putVariable(var);
    }
    gp.checkErroneousPop(gp.getGPPopulation(), " after creating population/2");
    return gp;
  }

  public GPConfiguration getGPConfiguration() {
    return m_configuration;
  }

  /**
   * @return the static configuration to use with Genetic Programming
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public static GPConfiguration getStaticGPConfiguration() {
    return m_staticConfiguration;
  }

  /**
   * Sets the static configuration to use with the Genetic Programming.
   *
   * @param a_configuration the static configuration to use
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public static void setStaticGPConfiguration(GPConfiguration a_configuration) {
    m_staticConfiguration = a_configuration;
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
    int offset = getGPConfiguration().getGenerationNr();
    int evolutions;
    if (a_evolutions < 0) {
      evolutions = Integer.MAX_VALUE;
    }
    else {
      evolutions = a_evolutions;
    }
//    getGPPopulation().sort(new GPFitnessComparator());
    for (int i = 0; i < evolutions; i++) {
      if (m_verbose) {
        if (i % 25 == 0) {
          String freeMB = SystemKit.niceMemory(SystemKit.getFreeMemoryMB());
          LOGGER.info("Evolving generation "
                      + (i + offset)
                      + ", memory free: "
                      + freeMB
                      + " MB");
        }
      }
      evolve();
      calcFitness();
    }
  }

  /**
   * Calculates the fitness value of all programs, of the best solution as well
   * as the total fitness (sum of all fitness values).
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
    boolean bestPreserved = false;
    for (int i = 0; i < pop.size() && pop.getGPProgram(i) != null; i++) {
      IGPProgram program = pop.getGPProgram(i);
      /**@todo get information from fitness function how calculation happened.
       * In case of Robocode: return the robot competed against, in case the
       * -enemies option was used without -battleAll
       */
      double fitness;
      try {
        fitness = program.getFitnessValue();
      } catch (IllegalStateException iex) {
        fitness = Double.NaN;
      }
      // Don't acceppt Infinity or NaN as a result.
      // ------------------------------------------
      if (Double.isInfinite(fitness) || Double.isNaN(fitness)) {
        continue;
      }
      if (best == null || evaluator.isFitter(fitness, m_bestFitness)) {
        best = program;
        m_bestFitness = fitness;
        if (m_allTimeBest != null && !bestPreserved) {
          if (best.toStringNorm(0).equals(m_allTimeBest.toStringNorm(0))) {
            bestPreserved = true;
          }
        }
      }
      totalFitness += fitness;
    }
    m_totalFitness = totalFitness;
    best = pop.determineFittestProgram();
    if (best != null) {
      m_bestFitness = best.getFitnessValue();
      /**@todo do something similar here as with Genotype.preserveFittestChromosome*/
      if (m_allTimeBest == null
          || evaluator.isFitter(m_bestFitness, m_allTimeBestFitness)) {
        pop.setChanged(true);
        try {
          ICloneHandler cloner = getGPConfiguration().getJGAPFactory().
              getCloneHandlerFor(best, null);
          if (cloner == null) {
            m_allTimeBest = best;
            if (!m_cloneWarningGPProgramShown) {
              LOGGER.info("Warning: cannot clone instance of "
                          + best.getClass());
              m_cloneWarningGPProgramShown = true;
            }
          }
          else {
            m_allTimeBest = (IGPProgram) cloner.perform(best, null, null);
          }
        } catch (Exception ex) {
          m_allTimeBest = best;
          ex.printStackTrace();
        }
        m_allTimeBestFitness = m_bestFitness;
        // Fire an event to indicate a new best solution.
        // ----------------------------------------------
        /**@todo introduce global value object to be passed to the listener*/
        try {
          getGPConfiguration().getEventManager().fireGeneticEvent(
              new GeneticEvent(GeneticEvent.GPGENOTYPE_NEW_BEST_SOLUTION, this));
        } catch (IllegalArgumentException iex) {
          iex.printStackTrace();
          /**@todo should not happen but does with ensureUniqueness(..)*/
        }
        if (m_verbose) {
          // Output the new best solution found.
          // -----------------------------------
          outputSolution(m_allTimeBest);
        }
      }
    }
    if (!bestPreserved && m_allTimeBest != null) {
      addFittestProgram(m_allTimeBest);
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
   * Outputs the best solution until now.
   *
   * @param a_best the fittest ProgramChromosome
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void outputSolution(IGPProgram a_best) {
    if (a_best == null) {
      LOGGER.debug("No best solution (null)");
      return;
    }
    double bestValue = a_best.getFitnessValue();
    if (Double.isInfinite(bestValue)) {
      LOGGER.debug("No best solution (infinite)");
      return;
    }
    LOGGER.info("Best solution fitness: " +
                NumberKit.niceDecimalNumber(bestValue, 2));
    LOGGER.info("Best solution: " + a_best.toStringNorm(0));
    String depths = "";
    int size = a_best.size();
    for (int i = 0; i < size; i++) {
      if (i > 0) {
        depths += " / ";
      }
      depths += a_best.getChromosome(i).getDepth(0);
    }
    if (size == 1) {
      LOGGER.info("Depth of chrom: " + depths);
    }
    else {
      LOGGER.info("Depths of chroms: " + depths);
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
      GPPopulation newPopulation = new GPPopulation(oldPop, false);
      if (m_fittestToAdd != null) {
        newPopulation.addFittestProgram(m_fittestToAdd);
        m_fittestToAdd = null;
      }
      float val;
      RandomGenerator random = getGPConfiguration().getRandomGenerator();
      GPConfiguration conf = getGPConfiguration();
      // Determine how many new individuals will be added to the new generation.
      // -----------------------------------------------------------------------
      int popSize1 = (int) Math.round(popSize * (1 - conf.getNewChromsPercent()));
      double crossProb = conf.getCrossoverProb()
          / (conf.getCrossoverProb() + conf.getReproductionProb());
      int crossover = 0;
      int reproduction = 0;
      int creation = 0;
      checkErroneousPop(getGPPopulation(), " (before evolution)", true);
      final int maxTries = getGPConfiguration().getProgramCreationMaxtries();
      // Do crossing over.
      // -----------------
      for (int i = 0; i < popSize1; i++) {
        // Clear the stack for each GP program.
        // ------------------------------------
        getGPConfiguration().clearStack();
        val = random.nextFloat();
        // Note that if we only have one slot left to fill, we don't do
        // crossover, but fall through to reproduction.
        // ------------------------------------------------------------
        if (i < popSize - 1 && val < crossProb) {
          crossover++;
          // Actually do the crossover here.
          // -------------------------------
          IGPProgram i1 = conf.getSelectionMethod().select(this);
          IGPProgram i2 = conf.getSelectionMethod().select(this);
          int tries = 0;
          do {
            try {
              checkErroneousProg(i1,
                                 " at start of evolution (index " + i +
                                 "/01)", false);
              if (i1 != i2) {
                // Crossing over the a program with itself does not lead anywhere.
                // ---------------------------------------------------------------
                checkErroneousProg(i2,
                                   " at start of evolution (index " + i +
                                   "/02)", false);
                IGPProgram[] newIndividuals = conf.getCrossMethod().operate(
                    i1,
                    i2);
                newPopulation.setGPProgram(i, newIndividuals[0]);
                newPopulation.setGPProgram(i + 1, newIndividuals[1]);
                try {
                  checkErroneousProg(newIndividuals[0],
                                     " at start of evolution (index " + i +
                                     "/11)", false);
                } catch (RuntimeException t) {
                  writeToFile(i1, i2, newIndividuals[0],
                              "Error in first X-over program");
                  throw t;
                }
                try {
                  checkErroneousProg(newIndividuals[1],
                                     " at start of evolution (index " + i +
                                     "/12)", false);
                } catch (RuntimeException t) {
                  writeToFile(i1, i2, newIndividuals[1],
                              "Error in second X-over program");
                  throw t;
                }
              }
              else {
                newPopulation.setGPProgram(i, i1);
                newPopulation.setGPProgram(i + 1, i2);
              }
              i++;
              break;
            } catch (IllegalStateException iex) {
              tries++;
              if ( (maxTries > 0 && tries >= maxTries) || tries > 40) {
                if (!getGPConfiguration().isMaxNodeWarningPrinted()) {
                  LOGGER.error(
                      "Warning: Maximum number of nodes allowed may be too small");
                  getGPConfiguration().flagMaxNodeWarningPrinted();
                }
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
          } while (true)
          ;
        }
        else { //if (val < conf.getCrossoverProb() + conf.getReproductionProb()) {
          // Reproduction only.
          // ------------------
          reproduction++;
          newPopulation.setGPProgram(i, conf.getSelectionMethod().select(this));
        }
      }
      // Add new random programs.
      // ------------------------
      for (int i = popSize1; i < popSize; i++) {
        creation++;
        // Randomly determine depth between minInitDepth and maxInitDepth.
        // ---------------------------------------------------------------
        int depth = conf.getMinInitDepth()
            + random.nextInt(conf.getMaxInitDepth() - conf.getMinInitDepth()
                             + 1);
        int tries = 0;
        int nogc = 0;
        do {
          try {
            // Randomize grow option as growing produces a valid program
            // more likely than the full mode.
            // ---------------------------------------------------------
            boolean grow;
            if (i % 2 == 0 || random.nextInt(8) > 6) {
              grow = true;
            }
            else {
              grow = false;
            }
            /**@todo use program creator in case such is registered and returns
             * a non-null program
             */
            IGPProgram program = newPopulation.create(i, m_types, m_argTypes,
                m_nodeSets, m_minDepths, m_maxDepths, depth, grow,
                m_maxNodes, m_fullModeAllowed, tries);
            newPopulation.setGPProgram(i, program);
            checkErroneousProg(program,
                               " when adding a program, evolution (index " + i +
                               ")", true);
            LOGGER.debug("Added new GP program (depth parameter: "
                         + depth
                         + ", "
                         + tries
                         + " tries)");
            break;
          } catch (IllegalStateException iex) {
            tries++;
            nogc++;
            /**@todo instead of re-using prototype, create a program anyway
             * (ignoring the validator) in case it is the last try.
             * Or even better: Make the validator return a defect rate!
             */
            if ( (maxTries > 0 && tries > maxTries) || tries > 40) {
              LOGGER.debug(
                  "Creating random GP program failed (depth "
                  + depth
                  + ", "
                  + tries
                  + " tries), will use prototype");
              // Try cloning a previously generated valid program.
              // -------------------------------------------------
              IGPProgram program = cloneProgram(getGPConfiguration().
                  getPrototypeProgram());
              if (program != null) {
                // Cloning worked.
                // ---------------
                newPopulation.setGPProgram(i, program);
                break;
              }
              else {
                if (getGPConfiguration().getPrototypeProgram() == null) {
                  throw new IllegalStateException(
                      "Cloning: Prototype program was null");
                }
                else {
                  throw new IllegalStateException(
                      "Cloning of prototype program failed, " +
                      iex.getMessage());
                }
              }
            }
            if (nogc > 5) {
              nogc = 0;
              System.gc();
            }
          }
        } while (true)
        ;
      }
      LOGGER.debug("Did "
                   + crossover + " x-overs, "
                   + reproduction + " reproductions, "
                   + creation + " creations");
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
        // Do G.C. for cleanup and to avoid 100% CPU load.
        // -----------------------------------------------
        System.gc();
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * Retrieves the GPProgram in the population with the best fitness
   * value.
   *
   * @return the GPProgram with the highest fitness value, or null if there
   * are no programs in this Genotype
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public synchronized IGPProgram getFittestProgram() {
    IGPProgram fittestPop = getGPPopulation().determineFittestProgram();
    if (fittestPop == null) {
      return m_allTimeBest;
    }
    double fittest;
    if (m_allTimeBest != null) {
      fittest = m_allTimeBest.getFitnessValue();
    }
    else {
      fittest = FitnessFunction.NO_FITNESS_VALUE;
    }
    if (m_allTimeBest != null &&
        getGPConfiguration().getGPFitnessEvaluator().isFitter(fittest,
        fittestPop.getFitnessValue())) {
      return m_allTimeBest;
    }
    else {
      m_allTimeBest = null;
      return fittestPop;
    }
  }

  /**
   * Retrieves the GPProgram in the population with the highest fitness
   * value. Only considers programs for which the fitness value has already
   * been computed.
   *
   * @return the GPProgram with the best fitness value, or null if there
   * are no programs with known fitness value in this Genotype
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public synchronized IGPProgram getFittestProgramComputed() {
    return getGPPopulation().determineFittestProgramComputed();
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
  public void setGPConfiguration(GPConfiguration a_configuration) {
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
    IGPProgram validProgram = a_original;
    ICloneHandler cloner = getGPConfiguration().getJGAPFactory().
        getCloneHandlerFor(validProgram, null);
    if (cloner != null) {
      try {
        IGPProgram program = (IGPProgram) cloner.perform(
            validProgram, null, null);
        return program;
      } catch (Exception ex) {
        LOGGER.error(ex.getMessage(), ex);
        return null;
      }
    }
    return null;
  }

  /**
   * Stores a Variable.
   *
   * @param a_var the Variable to store
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void putVariable(Variable a_var) {
    m_variables.put(a_var.getName(), a_var);
  }

  /**
   * @param a_varName name of variable to retriebe
   * @return Variable instance or null, if not found
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public Variable getVariable(String a_varName) {
    return (Variable) m_variables.get(a_varName);
  }

  /**
   * Adds a GP program to this Genotype. Does nothing when given null.
   * The injection is actually executed in method create(..) of GPPopulation.
   *
   * @param a_toAdd the program to add
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void addFittestProgram(final IGPProgram a_toAdd) {
    if (a_toAdd != null) {
      m_fittestToAdd = a_toAdd;
    }
  }

  /**
   * Fills up the population with random programs if necessary.
   *
   * @param a_num the number of programs to add
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void fillPopulation(final int a_num)
      throws InvalidConfigurationException {
    IGPProgram sampleProg = getGPConfiguration().getPrototypeProgram();
    if (sampleProg == null) {
      /**@todo care about*/
    }
    Class sampleClass = sampleProg.getClass();
    IInitializer chromIniter = getGPConfiguration().getJGAPFactory().
        getInitializerFor(sampleProg, sampleClass);
    if (chromIniter == null) {
      throw new InvalidConfigurationException("No initializer found for class "
          + sampleClass);
    }
    try {
      for (int i = 0; i < a_num; i++) {
        /**@todo implement filling up population*/
//        getGPPopulation().addChromosome( (IChromosome) chromIniter.perform(sampleProg,
//            sampleClass, null));
      }
    } catch (Exception ex) {
      throw new IllegalStateException(ex);
    }
  }

//  /**
//   * Disabling a chromosome is equivalent to not declaring it. However, if you
//   * skip a declaration, indices will shift. With this method it is easier
//   * skipping a chromosome.
//   *
//   * @param a_index index of the chromosome to disable.
//   *
//   * @author Klaus Meffert
//   * @since 3.2.2
//   */
//  public void disableChromosome(int a_index) {
//    if (a_index < 0 || a_index >= disabledChromosomes.length) {
//      throw new IllegalArgumentException("Invalid index!");
//    }
//    disabledChromosomes[a_index] = true;
//  }
//
//  /**
//   *
//   * @param a_index index of the chromosome to check.
//   * @return true if chromosome disabled
//   *
//   * @author Klaus Meffert
//   * @since 3.2.2
//   */
//  public boolean isDisabledChromosome(int a_index) {
//    if (a_index < 0 || a_index >= disabledChromosomes.length) {
//      throw new IllegalArgumentException("Invalid index!");
//    }
//    return disabledChromosomes[a_index];
//  }

  public static void checkErroneousPop(GPPopulation pop, String s) {
    checkErroneousPop(pop, s, false);
  }

  public static void checkErroneousPop(GPPopulation a_pop, String a_s,
                                       boolean a_clearFitness) {
    if (a_pop == null) {
      return;
    }
    checkErroneousPop(a_pop, a_s, a_clearFitness,
                      a_pop.getGPConfiguration().isVerifyPrograms());
  }

  public static void checkErroneousPop(GPPopulation a_pop, String a_s,
                                       boolean a_clearFitness,
                                       boolean a_active) {
    if (a_pop == null) {
      return;
    }
    if (!a_active) {
      // Verification not activated!
      // ---------------------------
      return;
    }
    int popSize1 = a_pop.size();
    for (int i = 0; i < popSize1; i++) {
      IGPProgram a_prog = a_pop.getGPProgram(i);
      checkErroneousProg(a_prog, a_s, a_clearFitness, a_active);
    }
  }

  public static void checkErroneousProg(IGPProgram prog, String s) {
    checkErroneousProg(prog, s, false);
  }

  public static void checkErroneousProg(IGPProgram a_prog, String a_s,
                                        boolean a_clearFitness) {
    if (a_prog == null) {
      return;
    }
    checkErroneousProg(a_prog, a_s, a_clearFitness,
                       a_prog.getGPConfiguration().isVerifyPrograms());
  }

  public static void checkErroneousProg(IGPProgram a_prog, String s,
                                        boolean a_clearFitness,
                                        boolean a_active) {
    if (a_prog == null) {
      return;
    }
    if (!a_active) {
      // Verification not activated!
      // ---------------------------
      return;
    }
    // Has program already been verified?
    // ----------------------------------
    /**@todo impl. cache*/

    if (a_clearFitness) {
      // Reset fitness value.
      // --------------------
      a_prog.setFitnessValue(GPFitnessFunction.NO_FITNESS_VALUE);
    }
    try {
      a_prog.getFitnessValue();
    } catch (Throwable ex) {
      String msg = "Invalid program detected" + s + "!";
      LOGGER.fatal(msg);
      throw new RuntimeException(msg, ex);
    }
  }

  /**
   * Write three GP programs being involved in crossover as a string to a file
   * for debug purposes.
   *
   * @param i1 first program
   * @param i2 second program
   * @param inew resulting program
   * @param header text for header text
   *
   * @author Klaus Meffert
   */
  private void writeToFile(IGPProgram i1, IGPProgram i2, IGPProgram inew,
                           String header) {
    StringBuffer sb = new StringBuffer();
    try {
      sb.append(header);
      sb.append("First Program to cross over\n");
      sb.append(getProgramString(i1));
      sb.append("\nSecond Program to cross over\n");
      sb.append(getProgramString(i2));
      sb.append("\nResulting Program after cross over\n");
      sb.append(getProgramString(inew));
      String filename = DateKit.getNowAsString();
      File f = new File(filename);
      FileWriter fw = new FileWriter(f);
      fw.write(sb.toString());
      fw.close();
    } catch (IOException iex) {
      System.out.println(sb.toString());
      iex.printStackTrace();
    }
  }

  /**
   * Utility function.
   *
   * @param i1 program to get textual representation for
   * @return textual representation of the given program
   *
   * @author Klaus Meffert
   */
  private StringBuffer getProgramString(IGPProgram i1) {
    int size = i1.size();
    int size2;
    ProgramChromosome chrom;
    CommandGene gene;
    StringBuffer result = new StringBuffer();
    for (int i = 0; i < size; i++) {
      chrom = i1.getChromosome(i);
      result.append("Chromosome " + i + ", class " + chrom.getClass() + "\n");
      size2 = chrom.size();
      for (int j = 0; j < size2; j++) {
        gene = chrom.getGene(j);
        result.append("Gene " + j + ", class " + gene.getClass() +
                      ", toString: " + gene.toString() + " \n");
      }
    }
    return result;
  }

  /**
   * Determine impossible functions and terminals to obtimize creation of GP
   * programs.
   *
   * @param a_conf GP configuration
   * @param a_types Class[]
   * @param a_nodeSets CommandGene[][]
   * @return Map
   *
   * @author Klaus Meffert
   * @since 3.4.4
   */
  public static Map<String, CommandGene> verifyChildNodes(
      GPConfiguration a_conf, Class[] a_types, CommandGene[][] a_nodeSets) {
    List<CommandGene> triedNodes;
    Map<String, CommandGene> invalidNodes = new Hashtable();
    // For every chromosome in the GP program.
    // ---------------------------------------
    for (int i = 0; i < a_nodeSets.length; i++) {
      triedNodes = new Vector();
      // Determine impossible functions and terminals.
      // ---------------------------------------------
      for (int j = 0; j < a_nodeSets[i].length; j++) {
        CommandGene node = a_nodeSets[i][j];
        if (triedNodes.contains(node)) {
          continue;
        }
        triedNodes.add(node);
        // Verify if node is possible.
        // ---------------------------
        int arity = node.getArity(null);
        Class[] childTypes;
        int[] subChildTypes;
        if (arity > 0) {
          childTypes = new Class[arity];
          for (int k = 0; k < arity; k++) {
            childTypes[k] = node.getChildType(null, k);
          }
          if (node.getSubChildTypes() != null) {
            subChildTypes = new int[arity];
            for (int k = 0; k < arity; k++) {
              subChildTypes[k] = node.getSubChildType(k);
            }
          }
          else {
            subChildTypes = new int[arity];
          }
        }
        else {
          childTypes = null;
          subChildTypes = null;
        }
        if (arity > 0) {
          // Is there any other node fitting as a child for the currently
          // examined node?
          // ------------------------------------------------------------
          for (int l = 0; l < arity; l++) {
            if (!nodeExists(a_nodeSets[i], childTypes[l], subChildTypes[l])) {
              String s = i + "," + j;
              invalidNodes.put(s, node);
              break;
            }
          }
        }
      }
    }
    return invalidNodes;
  }

  /**
   * Is there a node with the needed return type and sub return type within the
   * given function set?
   *
   * @param a_functionSet collection of available nodes
   * @param a_returnType needed return type
   * @param a_subReturnType needed sub return type
   * @return true: node found
   *
   * @author Klaus Meffert
   * @since 3.4.4
   */
  protected static boolean nodeExists(CommandGene[] a_functionSet,
                                      Class a_returnType,
                                      int a_subReturnType) {
    for (int i = 0; i < a_functionSet.length; i++) {
      if (a_functionSet[i].getReturnType() == a_returnType
          && (a_subReturnType == 0
              || a_subReturnType == a_functionSet[i].getSubReturnType())) {
        return true;
      }
    }
    return false;
  }

  /**
   * Outputs the nodes that are never used.
   *
   * @param invalidNodes never used nodes
   *
   * @author Klaus Meffert
   * @since 3.4.4
   */
  protected static void outputWarning(Map<String, CommandGene> invalidNodes) {
    if (invalidNodes != null && invalidNodes.size() > 0) {
      LOGGER.warn("Your configuration contains commands that are not used:");
      Iterator it = invalidNodes.values().iterator();
      while (it.hasNext()) {
        CommandGene node = (CommandGene) it.next();
        LOGGER.warn(" " + node.getClass().getName());
      }
    }
    else {
      LOGGER.info("Your configuration does not contain unused commands,"
                  + " this is good");
    }
  }

  /**
   * Outputs the nodes that are never used because they would result in creating
   * GP programs which would not satisfy the depth constraints given in
   * configuration.
   *
   * @param a_invalidDepths impossible depths for nodes
   *
   * @author Klaus Meffert
   * @since 3.4.4
   */
  protected static void outputDepthInfo(Map<CommandGene, int[]> a_invalidDepths) {
    if (a_invalidDepths != null && a_invalidDepths.size() > 0) {
      LOGGER.info("Your configuration contains commands that are not possible"
                  + "for certain depths: ");
      Iterator it = a_invalidDepths.keySet().iterator();
      while (it.hasNext()) {
        CommandGene node = (CommandGene) it.next();
        LOGGER.info(" " + node.getClass().getName());
        int[] depths = (int[]) a_invalidDepths.get(node);
        String s = "";
        for (int i = 0; i < depths.length; i++) {
          if (i > 0) {
            s += ", ";
          }
          s += depths[i];
        }
        LOGGER.info("   Impossible depths: " + s);
      }
    }
  }

  public static Map<CommandGene, int[]> verifyDepthsForNodes(GPPopulation a_pop,
      GPConfiguration a_conf, Class[] a_types, int[] a_minDepths,
      int[] a_maxDepths, int a_maxNodes, CommandGene[][] a_nodeSets) {
    List<CommandGene> triedNodes;
    List<CommandGene> possibleNodes;
    Map<CommandGene, int[]> impossibleDepths = new Hashtable();
    for (int i = 0; i < a_nodeSets.length; i++) {
      // Determine impossible functions and terminals.
      // ---------------------------------------------
      for (int j = 0; j < a_nodeSets[i].length; j++) {
        CommandGene nodeToCheck = a_nodeSets[i][j];
        triedNodes = new Vector();
        // Determine all possible nodes for current node.
        // ----------------------------------------------
        possibleNodes = new Vector();
        /**@todo impl*/
        for (int k = 0; k < a_nodeSets[i].length; k++) {
          CommandGene nodeInQuestion = a_nodeSets[i][k];
          if (triedNodes.contains(nodeInQuestion)) {
            continue;
          }
          boolean valid = false;
          for (int l = 0; l < nodeToCheck.size(); l++) {
            IGPProgram ind = a_pop.getGPProgram(0);
            if (nodeInQuestion.getReturnType() ==
                nodeToCheck.getChildType(ind, l)) {
              if (nodeInQuestion.getSubReturnType() == 0 ||
                  nodeInQuestion.getSubReturnType() ==
                  nodeToCheck.getSubChildType(l)) {
                valid = true;
                break;
              }
            }
          }
          if (valid) {
            possibleNodes.add(nodeInQuestion);
          }
          triedNodes.add(nodeInQuestion);
        }
        // Determine possible depths for each node.
        // Do not consider nodes already in progress.
        // ------------------------------------------
        /**@todo impl*/
      }
    }
    return impossibleDepths;
  }
}
