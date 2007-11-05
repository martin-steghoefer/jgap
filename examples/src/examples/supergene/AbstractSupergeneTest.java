/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.supergene;

import org.jgap.*;
import org.jgap.impl.*;

/**
 * Abstract class for testing Supergene performance.
 *
 * @author Neil Rotstan
 * @author Klaus Meffert
 * @author Audrius Meskauskas
 * @since 2.0
 * */
public abstract class AbstractSupergeneTest {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.5 $";

  /**
   * Gene index for the dimes gene
   */
  public static final int DIMES = 0;

  /**
   * Gene index for the quarters gene.
   */
  public static final int QUARTERS = 1;

  /**
   * Gene index for the nickels gene
   * Only used in the alternative presentation  */
  public static final int NICKELS = 2;

  /**
   * Gene index for the pennies gene.
   * Only used in the alternative presentation  */
  public static final int PENNIES = 3;

  /**
   * The total number of times we'll let the population evolve.
   */
  public static int MAX_ALLOWED_EVOLUTIONS = 200;

  /**
   * Chromosome size.
   */
  public static int POPULATION_SIZE = 2000;

  public static boolean REPORT_ENABLED = true;

  /**
   * @param a_conf the configuration to use
   *
   * @return created Dimes gene instance
   */
  protected Gene getDimesGene(Configuration a_conf) {
    try {
      return new IntegerGene(a_conf, 0, 2); // 10?
    } catch (InvalidConfigurationException iex) {
      throw new IllegalStateException(iex.getMessage());
    }
    } ;
    /**
     * @param a_conf the configuration to use
     *
     * @return created Nickels gene instance
     */
    protected Gene getNickelsGene(Configuration a_conf) {
      try {
        return new IntegerGene(a_conf, 0, 5);
      } catch (InvalidConfigurationException iex) {
        throw new IllegalStateException(iex.getMessage());
      }
    }
    /**
     * @param a_conf the configuration to use
     *
     * @return created Pennies (1) gene instance
     */
    protected Gene getPenniesGene(Configuration a_conf) {
      try {
        return new IntegerGene(a_conf, 0, 7);
      } catch (InvalidConfigurationException iex) {
        throw new IllegalStateException(iex.getMessage());
      }
    }
    /**
     * @param a_conf the configuration to use
     *
     * @return created Quarters gene instance
     */
    protected Gene getQuartersGene(Configuration a_conf) {
      try {
        return new IntegerGene(a_conf, 0, 3);
      } catch (InvalidConfigurationException iex) {
        throw new IllegalStateException(iex.getMessage());
      }
    }
    /** Compute the money value from the coin information. */
    public static int amountOfChange(int a_numQuarters, int a_numDimes,
                                     int a_numNickels, int a_numPennies) {
      return (a_numQuarters * 25) + (a_numDimes * 10) + (a_numNickels * 5)
          + a_numPennies;
    }
    ;
    /**
     * Executes the genetic algorithm to determine the minimum number of
     * coins necessary to make up the given target amount of change. The
     * solution will then be written to System.out.
     *
     * @param a_targetChangeAmount the target amount of change for which this
     * method is attempting to produce the minimum number of coins
     *
     * @return absolute difference between the required and computed change
     * amount
     * @throws Exception
     */
    public abstract int makeChangeForAmount(int a_targetChangeAmount)
        throws Exception;

    /**
     * Write report on eveluation to the given stream.
     * @param a_fitnessFunction p_SupergeneChangeFitnessFunction
     * @param a_population Genotype
     * @return Chromosome
     */
    public IChromosome report(SupergeneChangeFitnessFunction a_fitnessFunction,
                              Genotype a_population) {
      IChromosome bestSolutionSoFar = a_population.getFittestChromosome();
      if (!REPORT_ENABLED) {
        return bestSolutionSoFar;
      }
      System.out.println("\nThe best solution has a fitness value of "
                         + bestSolutionSoFar.getFitnessValue());
      System.out.println("It contained the following: ");
      System.out.println("\t" + a_fitnessFunction.getNumberOfCoinsAtGene(
          bestSolutionSoFar, QUARTERS) + " quarters.");
      System.out.println("\t" + a_fitnessFunction.getNumberOfCoinsAtGene(
          bestSolutionSoFar, DIMES) + " dimes.");
      System.out.println("\t" + a_fitnessFunction.getNumberOfCoinsAtGene(
          bestSolutionSoFar, NICKELS) + " nickels.");
      System.out.println("\t" + a_fitnessFunction.getNumberOfCoinsAtGene(
          bestSolutionSoFar, PENNIES) + " pennies.");
      System.out.println("For a total of " + a_fitnessFunction.amountOfChange(
          bestSolutionSoFar) + " cents in "
                         + a_fitnessFunction.getTotalNumberOfCoins(
                             bestSolutionSoFar) + " coins.");
      return bestSolutionSoFar;
    }
    /**
     * If set to true (required for strict tests), only tasks with existing
     * solutions will be submitted as a test tasks.
     */
    public static boolean EXISTING_SOLUTIONS_ONLY = false;

    /**
     * Test the method, returns the sum of all differences between
     * the required and obtained excange amount. One exception counts
     * as 1000 on the error score.
     */
    public int test() {
      int s = 0;
      int e;
      for (int amount = 20; amount < 100; amount++) {
        try {
          if (REPORT_ENABLED) {
            System.out.println("EXCHANGING " + amount + " ");
          }
          // Do not solve cases without solutions
          if (EXISTING_SOLUTIONS_ONLY) {
            if (!Force.solve(amount)) {
              continue;
            }
          }
          // Need to reset the configuration because it needs to be changed each
          // time when looping.
          // -------------------------------------------------------------------
          DefaultConfiguration.reset();
          e = makeChangeForAmount(amount);
          if (REPORT_ENABLED) {
            System.out.println(" err " + e);
            System.out.println("---------------");
          }
          s = s + e;
        } catch (Exception ex) {
          ex.printStackTrace();
          s += 1000;
        }
      }
      if (REPORT_ENABLED) {
        System.out.println("Sum of errors " + s);
      }
      return s;
    }
    /**
     * Find and print the solution, return the solution error.
     *
     * @param a_conf the configuration to use
     *
     * @return absolute difference between the required and computed change
     */
    protected int solve(Configuration a_conf, int a_targetChangeAmount,
                        SupergeneChangeFitnessFunction a_fitnessFunction,
                        Gene[] a_sampleGenes)
        throws InvalidConfigurationException {
      IChromosome sampleChromosome = new Chromosome(a_conf, a_sampleGenes);
      a_conf.setSampleChromosome(sampleChromosome);
      // Finally, we need to tell the Configuration object how many
      // Chromosomes we want in our population. The more Chromosomes,
      // the larger number of potential solutions (which is good for
      // finding the answer), but the longer it will take to evolve
      // the population (which could be seen as bad). We'll just set
      // the population size to 500 here.
      // ------------------------------------------------------------
      a_conf.setPopulationSize(POPULATION_SIZE);
      // Create random initial population of Chromosomes.
      // ------------------------------------------------
      Genotype population = Genotype.randomInitialGenotype(a_conf);
      int s;
      Evolution:
          // Evolve the population, break if the the change solution is found.
          // -----------------------------------------------------------------
          for (int i = 0; i < MAX_ALLOWED_EVOLUTIONS; i++) {
        population.evolve();
        s = Math.abs(a_fitnessFunction.amountOfChange(population.
            getFittestChromosome())
                     - a_targetChangeAmount);
        if (s == 0) {
          break Evolution;
        }
      }
      // Display the best solution we found.
      // -----------------------------------
      IChromosome bestSolutionSoFar = report(a_fitnessFunction, population);
      return Math.abs(a_fitnessFunction.amountOfChange(bestSolutionSoFar)
                      - a_targetChangeAmount);
    }
  }
