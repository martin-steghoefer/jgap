/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.impl.salesman;

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
 * @author <font size="-1">Neil Rotstan, Klaus Meffert (reused code fragments)
 * </font>
 * @since 2.0
 *
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
public abstract class Salesman {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.11 $";

  private int max_evolution = 128;

  private int population_size = 512;

  private int m_acceptable_cost = -1;


  /**
   * Override this method to compute the distance between "cities",
   * indicated by these two given genes. The algorithm is not dependent
   * on the used type of genes.
   * @param a_from first gene, representing a city
   * @param a_to second gene, representing a city
   * @return the distance between two cities represented as genes
   */
  public abstract double distance(Gene a_from, Gene a_to);

  /**
   * Override this method to create a single sample chromosome, representing
   * al list of "cities". Each gene corresponds a single "city" and
   * can appear only once. By default, the first gene corresponds
   * a "city" where the salesman starts the journey.
   * It never changes its position. This can be changed by setting other
   * start offset with setStartOffset( ).  Other genes will be shuffled to
   * create the initial random population.
   *
   * @param initial_data the same object as was passed to findOptimalPath.
   * It can be used to specify the task more precisely if the class is
   * used for solving multiple tasks
   * @return a sample chromosome
   *
   * @author Audrius Meskauskas
   */
  public abstract Chromosome createSampleChromosome(Object initial_data);

  /** Return the fitness function. The function, returned by this method,
   * calls {@link org.jgap.impl.salesman.Salesman#distance
   * distance(Object from, Object to) }
   *
   * @param initial_data the same object as was passed to findOptimalPath.
   * It can be used to specify the task more precisely if the class is
   * used for solving multiple tasks.
   * @return an applicable fitness function
   *
   * @author Audrius Meskauskas
   */
  public FitnessFunction createFitnessFunction(Object initial_data) {
    return new SalesmanFitnessFunction(this);
  }

  /** Create a configuration. The configuration should not contain
   * operators for odrinary crossover and mutations, as they make
   * chromosoms invalid in this task. The special operators
   * SwappingMutationOperator and GreedyCrossober should be used instead.
   * @param initial_data the same object as was passed to findOptimalPath.
   * It can be used to specify the task more precisely if the class is
   * used for solving multiple tasks
   * @return created configuration
   *
   * @author Audrius Meskauskas
   */
  public Configuration createConfiguration(Object initial_data) {
    try {
      // This is copied from DefaultConfiguration
      // ----------------------------------------
      Configuration config = new Configuration();
      BestChromosomesSelector bestChromsSelector =
          new BestChromosomesSelector(1.0d);
      bestChromsSelector.setDoubletteChromosomesAllowed(false);
      config.addNaturalSelector(bestChromsSelector, true);
      config.setRandomGenerator(new StockRandomGenerator());
      config.setMinimumPopSizePercent(0);
      config.setEventManager(new EventManager());
      config.setFitnessEvaluator(new DefaultFitnessEvaluator());
      config.setChromosomePool(new ChromosomePool());
      // These are different:
      // -----------------------------------------
      config.addGeneticOperator(new GreedyCrossover());
      config.addGeneticOperator(new SwappingMutationOperator(20));
      return config;
    }
    catch (InvalidConfigurationException e) {
      throw new RuntimeException(
          "Fatal error: DefaultConfiguration class could not use its " +
          "own stock configuration values. This should never happen. " +
          "Please report this as a bug to the JGAP team.");
    }
  }

  /**
   * The solution process breaks after the total path length drops below this
   * limit. The default value (-1) will never be achieved, and evolution stops
   * after getMaxEvolution() iterations.
   * @return satisfying cost allowed to conditionally stop before an optimal
   * solution has been found.
   */
  public int getAcceptableCost() {
    return m_acceptable_cost;
  }

  public void setAcceptableCost(int an_AcceptableCost) {
    this.m_acceptable_cost = an_AcceptableCost;
  }

  /**
   * Get the maximal number of iterations for population to evolve.
   * @return int
   */
  public int getMaxEvolution() {
    return max_evolution;
  }

  /** Set the maximal number of iterations for population to evolve
   * (default 512).
   * @param a_max_evolution sic
   */
  public void setMaxEvolution(int a_max_evolution) {
    this.max_evolution = a_max_evolution;
  }

  /**
   * @return population size for this solution
   */
  public int getPopulationSize() {
    return population_size;
  }

  /**
   * Set an population size for this solution (default 512)
   * @param a_population_size sic
   */
  public void setPopulationSize(int a_population_size) {
    this.population_size = a_population_size;
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
   */
  public Chromosome findOptimalPath(Object a_initial_data)
      throws Exception {
    Genotype.setConfiguration(createConfiguration(a_initial_data));
    FitnessFunction myFunc = createFitnessFunction(a_initial_data);
    Genotype.getConfiguration().setFitnessFunction(myFunc);
    // Now we need to tell the Configuration object how we want our
    // Chromosomes to be setup. We do that by actually creating a
    // sample Chromosome and then setting it on the Configuration
    // object.
    // --------------------------------------------------------------
    Chromosome sampleChromosome = createSampleChromosome(a_initial_data);
    Genotype.getConfiguration().setSampleChromosome(sampleChromosome);
    // Finally, we need to tell the Configuration object how many
    // Chromosomes we want in our population. The more Chromosomes,
    // the larger number of potential solutions (which is good for
    // finding the answer), but the longer it will take to evolve
    // the population (which could be seen as bad). We'll just set
    // the population size to 500 here.
    // ------------------------------------------------------------
    Genotype.getConfiguration().setPopulationSize(population_size);
    // Create random initial population of Chromosomes.
    // ------------------------------------------------

    // As we cannot allow the normal mutations if this task,
    // we need multiple calls to createSampleChromosome.
    // -----------------------------------------------------
    Chromosome chromosomes[] =
        new Chromosome[Genotype.getConfiguration().getPopulationSize()];
    Gene[] s_genes = sampleChromosome.getGenes();
    for (int i = 0; i < chromosomes.length; i++) {
      Gene[] genes = new Gene[s_genes.length];
      for (int k = 0; k < genes.length; k++) {
        genes[k] = s_genes[k].newGene();
        genes[k].setAllele(s_genes[k].getAllele());
      }
      shuffle(genes);
      chromosomes[i] = new Chromosome(genes);
    }
    Genotype population = new Genotype(Genotype.getConfiguration(),
                                       new Population(chromosomes));
    Chromosome best = null;
    // Evolve the population. Since we don't know what the best answer
    // is going to be, we just evolve the max number of times.
    // ---------------------------------------------------------------
    Evolution:
        for (int i = 0; i < max_evolution; i++) {
      population.evolve();
      best = population.getFittestChromosome();
      if (best.getFitnessValue() >= getAcceptableCost()) {
        break Evolution;
      }
    }
    // Return the best solution we found.
    // -----------------------------------
    return best;
  }

  protected void shuffle(Gene[] a_genes) {
    Gene t;
    // shuffle:
    for (int r = 0; r < 10 * a_genes.length; r++) {
      for (int i = m_startOffset; i < a_genes.length; i++) {
        int p =
            m_startOffset +
            Genotype.getConfiguration().getRandomGenerator().
            nextInt(a_genes.length - m_startOffset);
        t = a_genes[i];
        a_genes[i] = a_genes[p];
        a_genes[p] = t;
      }
    }
  }

  private int m_startOffset = 1;

  /**
   * Sets a number of genes at the start of chromosome, that are
   * excluded from the swapping. In the Salesman task, the first city
   * in the list should (where the salesman leaves from) probably should
   * not change as it is part of the list. The default value is 1.
   * @param a_offset start offset for chromosome
   */
  public void setStartOffset(int a_offset) {
    m_startOffset = a_offset;
  }

  /**
   * Gets a number of genes at the start of chromosome, that are
   * excluded from the swapping. In the Salesman task, the first city
   * in the list should (where the salesman leaves from) probably should
   * not change as it is part of the list. The default value is 1.
   * @return start offset for chromosome
   */
  public int getStartOffset() {
    return m_startOffset;
  }
}
