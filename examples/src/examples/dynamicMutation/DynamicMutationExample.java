/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.dynamicMutation;

import org.jgap.*;
import org.jgap.impl.*;

/**
 * Experiment on how to dynamically adapt the mutation rate for different
 * genes. This example works with coins (see MinimizingMakeChange for
 * documentation). The idea is that a quarter has more impact onto the solution
 * than a penny, so a quarter should mutate less frequently, probably.
 *
 * @author Klaus Meffert
 * @since 2.6
 */
public class DynamicMutationExample {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.6 $";

  /**
   * The total number of times we'll let the population evolve.
   */
  private static final int MAX_ALLOWED_EVOLUTIONS = 200;

  /**
   * Executes the genetic algorithm to determine the minimum number of
   * coins necessary to make up the given target amount of change. The
   * solution will then be written to System.out.
   *
   * @param a_targetChangeAmount the target amount of change for which this
   * method is attempting to produce the minimum number of coins
   * @throws Exception
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 1.0
   */
  public static void makeChangeForAmount(int a_targetChangeAmount)
      throws Exception {
    // Start with a DefaultConfiguration, which comes setup with the
    // most common settings.
    // -------------------------------------------------------------
    Configuration conf = new DefaultConfiguration();
    // Add custom mutation operator
    conf.getGeneticOperators().clear();
//    IUniversalRateCalculator mutCalc = new CoinsMutationRateCalc();
    TwoWayMutationOperator mutOp = new TwoWayMutationOperator(conf, 7);
    conf.addGeneticOperator(mutOp);
    conf.addGeneticOperator(new CrossoverOperator(conf));
    conf.setPreservFittestIndividual(!true);
    conf.setKeepPopulationSizeConstant(false);
    // Set the fitness function we want to use, which is our
    // MinimizingMakeChangeFitnessFunction. We construct it with
    // the target amount of change passed in to this method.
    // ---------------------------------------------------------
    FitnessFunction myFunc =
        new DynamicMutationFitnessFunction(a_targetChangeAmount);
//    conf.setFitnessFunction(myFunc);
    conf.setBulkFitnessFunction(new BulkFitnessOffsetRemover(myFunc));
    conf.reset();
    conf.setFitnessEvaluator(new DeltaFitnessEvaluator());
    // Now we need to tell the Configuration object how we want our
    // Chromosomes to be setup. We do that by actually creating a
    // sample Chromosome and then setting it on the Configuration
    // object. As mentioned earlier, we want our Chromosomes to each
    // have four genes, one for each of the coin types. We want the
    // values (alleles) of those genes to be integers, which represent
    // how many coins of that type we have. We therefore use the
    // IntegerGene class to represent each of the genes. That class
    // also lets us specify a lower and upper bound, which we set
    // to sensible values for each coin type.
    // --------------------------------------------------------------
    Gene[] sampleGenes = new Gene[4];
    sampleGenes[0] = new IntegerGene(conf, 0, 3 * 10); // Quarters
    sampleGenes[1] = new IntegerGene(conf, 0, 2 * 10); // Dimes
    sampleGenes[2] = new IntegerGene(conf, 0, 1 * 10); // Nickels
    sampleGenes[3] = new IntegerGene(conf, 0, 4 * 10); // Pennies
    IChromosome sampleChromosome = new Chromosome(conf, sampleGenes);
    conf.setSampleChromosome(sampleChromosome);
    // Finally, we need to tell the Configuration object how many
    // Chromosomes we want in our population. The more Chromosomes,
    // the larger number of potential solutions (which is good for
    // finding the answer), but the longer it will take to evolve
    // the population (which could be seen as bad).
    // ------------------------------------------------------------
    conf.setPopulationSize(80);
    // Create random initial population of Chromosomes.
    // Here we try to read in a previous run via XMLManager.readFile(..)
    // for demonstration purpose!
    // -----------------------------------------------------------------
    Genotype population;
    // Initialize the population randomly
    population = Genotype.randomInitialGenotype(conf);
    // Evolve the population. Since we don't know what the best answer
    // is going to be, we just evolve the max number of times.
    // ---------------------------------------------------------------
    for (int i = 0; i < MAX_ALLOWED_EVOLUTIONS; i++) {
      population.evolve();
    }
    // Display the best solution we found.
    // -----------------------------------
    IChromosome bestSolutionSoFar = population.getFittestChromosome();
    System.out.println("The best solution has a fitness value of " +
                       bestSolutionSoFar.getFitnessValue());
    System.out.println("It contained the following: ");
    System.out.println("\t" +
                       DynamicMutationFitnessFunction.
                       getNumberOfCoinsAtGene(
        bestSolutionSoFar, 0) + " quarters.");
    System.out.println("\t" +
                       DynamicMutationFitnessFunction.
                       getNumberOfCoinsAtGene(
        bestSolutionSoFar, 1) + " dimes.");
    System.out.println("\t" +
                       DynamicMutationFitnessFunction.
                       getNumberOfCoinsAtGene(
        bestSolutionSoFar, 2) + " nickels.");
    System.out.println("\t" +
                       DynamicMutationFitnessFunction.
                       getNumberOfCoinsAtGene(
        bestSolutionSoFar, 3) + " pennies.");
    System.out.println("For a total of " +
                       DynamicMutationFitnessFunction.amountOfChange(
        bestSolutionSoFar) + " cents in " +
                       DynamicMutationFitnessFunction.
                       getTotalNumberOfCoins(
        bestSolutionSoFar) + " coins.");
  }

  /**
   * Main method. A single command-line argument is expected, which is the
   * amount of change to create (in other words, 75 would be equal to 75
   * cents).
   *
   * @param args amount of change in cents to create
   * @throws Exception
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 1.0
   */
  public static void main(String[] args)
      throws Exception {
    if (args.length != 1) {
      System.out.println("Syntax: DynamicMutationExample <amount>");
    }
    else {
      int amount = 0;
      try {
        amount = Integer.parseInt(args[0]);
      }
      catch (NumberFormatException e) {
        System.out.println(
            "The <amount> argument must be a valid integer value");
        System.exit(1);
      }
      if (amount < 1 ||
          amount >= DynamicMutationFitnessFunction.MAX_BOUND) {
        System.out.println("The <amount> argument must be between 1 and "
                           +
                           (DynamicMutationFitnessFunction.MAX_BOUND - 1)
                           + ".");
      }
      else {
        makeChangeForAmount(amount);
      }
    }
  }

  /**
   * This class only is an experiment!
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public static class CoinsMutationRateCalc
      implements IUniversalRateCalculator {
    private int m_evolution;

    private double m_rate0 = 0.2d;

    private double m_rate1 = 0.6d;

    private double m_rate2 = 0.7d;

    private double m_rate3 = 1.0d;

    public int calculateCurrentRate() {
      int size;
      size = 15;
      if (size < 1) {
        size = 1;
      }
      return size;
    }

    public boolean toBePermutated(IChromosome a_chrom, int a_geneIndex) {
      RandomGenerator generator
          = a_chrom.getConfiguration().getRandomGenerator();
      double mult = 0.0d;
      switch (a_geneIndex) {
        case 0:
          mult = get(0);
          break;
        case 1:
          mult = m_rate1;
          break;
        case 2:
          mult = m_rate2;
          break;
        case 3:
          mult = m_rate3;
          m_evolution++;
          break;
      }
      return (generator.nextDouble() < (1 / calculateCurrentRate()) * mult);
    }

    private double get(int a_index) {
      if (m_evolution > 90) {
        m_rate0 = 1.0d;
      }
      else if (m_evolution > 60) {
        m_rate0 = 0.75d;
      }
      else if (m_evolution > 30) {
        m_rate0 = 0.5d;
      }
      else if (m_evolution > 15) {
        m_rate0 = 0.4d;
      }
      return m_rate0;
    }
  }
}
