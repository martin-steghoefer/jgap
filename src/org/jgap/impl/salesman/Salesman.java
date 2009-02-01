/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.impl.salesman;

import java.io.*;

import org.jgap.*;
import org.jgap.event.*;
import org.jgap.impl.*;

/**
 * The class solves the travelling salesman problem.
 * The traveling salesman problem, or TSP for short, is this: given a finite
 *  number of 'cities' along with the cost of travel between each pair of
 * them, find the cheapest way of visiting all the cities and returning to
 * your starting point.)
 *
 * @author Audrius Meskauskas
 * @author Neil Rotstan, Klaus Meffert (reused code fragments)
 * @since 2.0
 *
 * @see
 *  <ul>
 *   <li>J. Grefenstette, R. Gopal, R. Rosmaita, and D. Gucht.
 *     <i>Genetic algorithms for the traveling salesman problem</i>.
 *     In Proceedings of the Second International Conference on Genetic
 *     Algorithms. Lawrence Eribaum Associates, Mahwah, NJ, 1985.
 *   </li>
 *   <li>
 *    <a href="http://ecsl.cs.unr.edu/docs/techreports/gong/node3.html">
 *      Sushil J. Louis & Gong Li</a> (explanatory material)
 *   </li>
 *   <li>
 *     <a href="http://www.tsp.gatech.edu www.tsp.gatech.edu">TPS web site</a>
 *  </li>
 * </ul>
 */
public abstract class Salesman
    implements Serializable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.22 $";

  private Configuration m_config;

  private int m_maxEvolution = 128;

  private int m_populationSize = 512;

  /**
   * Override this method to compute the distance between "cities",
   * indicated by these two given genes. The algorithm is not dependent
   * on the used type of genes.
   *
   * @param a_from first gene, representing a city
   * @param a_to second gene, representing a city
   * @return the distance between two cities represented as genes
   *
   * @author Audrius Meskauskas
   * @since 2.0
   */
  public abstract double distance(Gene a_from, Gene a_to);

  /**
   * Override this method to create a single sample chromosome, representing
   * a list of "cities". Each gene corresponds a single "city" and
   * can appear only once. By default, the first gene corresponds
   * a "city" where the salesman starts the journey.
   * It never changes its position. This can be changed by setting other
   * start offset with setStartOffset( ).  Other genes will be shuffled to
   * create the initial random population.
   *
   * @param a_initial_data the same object as was passed to findOptimalPath.
   * It can be used to specify the task more precisely if the class is
   * used for solving multiple tasks
   * @return a sample chromosome
   *
   * @author Audrius Meskauskas
   * @since 2.0
   */
  public abstract IChromosome createSampleChromosome(Object a_initial_data);

  /**
   * Return the fitness function to use.
   *
   * @param a_initial_data the same object as was passed to findOptimalPath.
   * It can be used to specify the task more precisely if the class is
   * used for solving multiple tasks
   * @return an applicable fitness function
   *
   * @author Audrius Meskauskas
   * @since 2.0
   */
  public FitnessFunction createFitnessFunction(final Object a_initial_data) {
    return new SalesmanFitnessFunction(this);
  }

  /**
   * Create a configuration. The configuration should not contain
   * operators for odrinary crossover and mutations, as they make
   * chromosoms invalid in this task. The special operators
   * SwappingMutationOperator and GreedyCrossober should be used instead.
   *
   * @param a_initial_data the same object as was passed to findOptimalPath.
   * It can be used to specify the task more precisely if the class is
   * used for solving multiple tasks
   *
   * @return created configuration
   *
   * @throws InvalidConfigurationException
   *
   * @author Audrius Meskauskas
   * @since 2.0
   */
  public Configuration createConfiguration(final Object a_initial_data)
      throws InvalidConfigurationException {
    // This is copied from DefaultConfiguration.
      // -----------------------------------------
      Configuration config = new Configuration();
      BestChromosomesSelector bestChromsSelector =
          new BestChromosomesSelector(config, 1.0d);
      bestChromsSelector.setDoubletteChromosomesAllowed(false);
      config.addNaturalSelector(bestChromsSelector, true);
      config.setRandomGenerator(new StockRandomGenerator());
      config.setMinimumPopSizePercent(0);
      config.setEventManager(new EventManager());
      config.setFitnessEvaluator(new DefaultFitnessEvaluator());
      config.setChromosomePool(new ChromosomePool());
      // These are different:
      // --------------------
      config.addGeneticOperator(new GreedyCrossover(config));
      config.addGeneticOperator(new SwappingMutationOperator(config, 20));
      return config;
  }

  /**
   * @return maximal number of iterations for population to evolve
   *
   * @author Audrius Meskauskas
   * @since 2.0
   */
  public int getMaxEvolution() {
    return m_maxEvolution;
  }

  /** Set the maximal number of iterations for population to evolve
   * (default 512).
   * @param a_maxEvolution sic
   *
   * @author Audrius Meskauskas
   * @since 2.0
   */
  public void setMaxEvolution(final int a_maxEvolution) {
    m_maxEvolution = a_maxEvolution;
  }

  /**
   * @return population size for this solution
   *
   * @since 2.0
   */
  public int getPopulationSize() {
    return m_populationSize;
  }

  /**
   * Set an population size for this solution (default 512)
   *
   * @param a_populationSize sic
   *
   * @since 2.0
   */
  public void setPopulationSize(final int a_populationSize) {
    m_populationSize = a_populationSize;
  }

  /**
   * Executes the genetic algorithm to determine the
   * optimal path between the cities.
   *
   * @param a_initial_data can be a record with fields, specifying the
   * task more precisely if the class is used to solve multiple tasks.
   * It is passed to createFitnessFunction, createSampleChromosome and
   * createConfiguration
   *
   * @throws Exception
   * @return chromosome representing the optimal path between cities
   *
   * @author Audrius Meskauskas
   * @since 2.0
   */
  public IChromosome findOptimalPath(final Object a_initial_data)
      throws Exception {
    m_config = createConfiguration(a_initial_data);
    FitnessFunction myFunc = createFitnessFunction(a_initial_data);
    m_config.setFitnessFunction(myFunc);
    // Now we need to tell the Configuration object how we want our
    // Chromosomes to be setup. We do that by actually creating a
    // sample Chromosome and then setting it on the Configuration
    // object.
    // --------------------------------------------------------------
    IChromosome sampleChromosome = createSampleChromosome(a_initial_data);
    m_config.setSampleChromosome(sampleChromosome);
    // Finally, we need to tell the Configuration object how many
    // Chromosomes we want in our population. The more Chromosomes,
    // the larger number of potential solutions (which is good for
    // finding the answer), but the longer it will take to evolve
    // the population (which could be seen as bad). We'll just set
    // the population size to 500 here.
    // ------------------------------------------------------------
    m_config.setPopulationSize(getPopulationSize());
    // Create random initial population of Chromosomes.
    // ------------------------------------------------

    // As we cannot allow the normal mutations if this task,
    // we need multiple calls to createSampleChromosome.
    // -----------------------------------------------------
    IChromosome[] chromosomes =
        new IChromosome[m_config.getPopulationSize()];
    Gene[] samplegenes = sampleChromosome.getGenes();
    for (int i = 0; i < chromosomes.length; i++) {
      Gene[] genes = new Gene[samplegenes.length];
      for (int k = 0; k < genes.length; k++) {
        genes[k] = samplegenes[k].newGene();
        genes[k].setAllele(samplegenes[k].getAllele());
      }
      chromosomes[i] = new Chromosome(m_config, genes);
    }
    // Create the genotype. We cannot use Genotype.randomInitialGenotype,
    // Because we need unique gene values (representing the indices of the
    // cities of our problem).
    // -------------------------------------------------------------------
    Genotype population = new Genotype(m_config,
                                       new Population(m_config, chromosomes));
    IChromosome best = null;
    // Evolve the population. Since we don't know what the best answer
    // is going to be, we just evolve the max number of times.
    // ---------------------------------------------------------------
    Evolution:
        for (int i = 0; i < getMaxEvolution(); i++) {
      population.evolve();
      best = population.getFittestChromosome();
    }
    // Return the best solution we found.
    // ----------------------------------
    return best;
  }

  private int m_startOffset = 1;

  /**
   * Sets a number of genes at the start of chromosome, that are
   * excluded from the swapping. In the Salesman task, the first city
   * in the list should (where the salesman leaves from) probably should
   * not change as it is part of the list. The default value is 1.
   *
   * @param a_offset start offset for chromosome
   *
   * @since 2.0
   */
  public void setStartOffset(final int a_offset) {
    m_startOffset = a_offset;
  }

  /**
   * Gets a number of genes at the start of chromosome, that are
   * excluded from the swapping. In the Salesman task, the first city
   * in the list should (where the salesman leaves from) probably should
   * not change as it is part of the list. The default value is 1.
   *
   * @return start offset for chromosome
   *
   * @since 2.0
   */
  public int getStartOffset() {
    return m_startOffset;
  }

  public Configuration getConfiguration() {
    return m_config;
  }
}
