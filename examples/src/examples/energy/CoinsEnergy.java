/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.energy;

import org.jgap.*;
import org.jgap.impl.*;

/**
 * THIS EXAMPLE IS NOT IMPLEMENTED FULLY!
 * For general description, see examples.MinimizingMakeChange.<p>
 * Additionally, each to coin an energy value is assigned (new feature since
 * JGAP version 2.4). Energy is interpreted here as weight of a coin. You could
 * think of a coins holder that wants a low total weight as possible and that
 * is capable of only holding a given maximum weight.
 *
 * @author Klaus Meffert
 * @since 2.4
 */
public class CoinsEnergy {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

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
   * @param a_maxWeight the maximum weight allowed in sum over all coins
   * @throws Exception
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 1.0
   */
  public static void makeChangeForAmount(int a_targetChangeAmount,
                                         double a_maxWeight)
      throws Exception {
    // Start with a DefaultConfiguration, which comes setup with the
    // most common settings.
    // -------------------------------------------------------------
    Configuration conf = new DefaultConfiguration();
    conf.setPreservFittestIndividual(true);
    conf.setKeepPopulationSizeConstant(false);
    // Set the fitness function we want to use, which is our
    // MinimizingMakeChangeFitnessFunction. We construct it with
    // the target amount of change passed in to this method.
    // ---------------------------------------------------------
    FitnessFunction myFunc =
        new CoinsEnergyFitnessFunction(a_targetChangeAmount, a_maxWeight);
//    conf.setFitnessFunction(myFunc);
    conf.setBulkFitnessFunction(new BulkFitnessOffsetRemover(myFunc));
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
    IntegerGene gene = new IntegerGene(0, 3 * 10);
    gene.setConstraintChecker(new EnergyGeneConstraintChecker());
    sampleGenes[0] = gene; // Quarters
    sampleGenes[1] = new IntegerGene(0, 2 * 10); // Dimes
    sampleGenes[2] = new IntegerGene(0, 1 * 10); // Nickels
    sampleGenes[3] = new IntegerGene(0, 4 * 10); // Pennies
    Chromosome sampleChromosome = new Chromosome(sampleGenes);
    conf.setSampleChromosome(sampleChromosome);
    // Finally, we need to tell the Configuration object how many
    // Chromosomes we want in our population. The more Chromosomes,
    // the larger number of potential solutions (which is good for
    // finding the answer), but the longer it will take to evolve
    // the population (which could be seen as bad).
    // ------------------------------------------------------------
    conf.setPopulationSize(80);

    // Create random initial population of Chromosomes.
    // ------------------------------------------------
    Genotype population = Genotype.randomInitialGenotype(conf);
    // Initialize energys of Gene's. Each Gene represents a coin with a
    // specific value, and each coin with different value has a specific
    // weight. Not necessarily a higher weight for higher coin values!
    // (as in real life!).
    /**@todo */

    // Evolve the population. Since we don't know what the best answer
    // is going to be, we just evolve the max number of times.
    // ---------------------------------------------------------------
    for (int i = 0; i < MAX_ALLOWED_EVOLUTIONS; i++) {
      population.evolve();
    }

    // Display the best solution we found.
    // -----------------------------------
    Chromosome bestSolutionSoFar = population.getFittestChromosome();
    System.out.println("The best solution has a fitness value of " +
                       bestSolutionSoFar.getFitnessValue());
    System.out.println("It contained the following: ");
    System.out.println("\t" +
                       CoinsEnergyFitnessFunction.
                       getNumberOfCoinsAtGene(
        bestSolutionSoFar, 0) + " quarters.");
    System.out.println("\t" +
                       CoinsEnergyFitnessFunction.
                       getNumberOfCoinsAtGene(
        bestSolutionSoFar, 1) + " dimes.");
    System.out.println("\t" +
                       CoinsEnergyFitnessFunction.
                       getNumberOfCoinsAtGene(
        bestSolutionSoFar, 2) + " nickels.");
    System.out.println("\t" +
                       CoinsEnergyFitnessFunction.
                       getNumberOfCoinsAtGene(
        bestSolutionSoFar, 3) + " pennies.");
    System.out.println("For a total of " +
                       CoinsEnergyFitnessFunction.amountOfChange(
        bestSolutionSoFar) + " cents in " +
                       CoinsEnergyFitnessFunction.
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
    if (args.length != 2) {
      System.out.println("Syntax: MinimizingMakeChange <amount> <max weight>");
    }
    else {
      int amount = getValue(args, 0);
      int weight = getValue(args, 1);
      makeChangeForAmount(amount, weight);
    }
  }

  protected static int getValue(String[] args, int index) {
    int value;
    try {
      value = Integer.parseInt(args[index]);
      return value;
    }
    catch (NumberFormatException e) {
      System.out.println(
          "The " + (index+1) + ". argument must be a valid integer value");
      System.exit(1);
      return -1; // does not matter
    }
  }

  /**
   * Uses to set the energy when a new allele is set
   * @author Klaus Meffert
   * @since 2.4
   */
  public static class EnergyGeneConstraintChecker implements IGeneConstraintChecker {
    public final static double[] coinWeights = {1.0d, 2.0d, 8.0d, 3.0d};

    /**
     * Check if a given allele value is valid for the given gene instance.
     * @param a_gene the gene the given allele is to be validated for
     * @param a_alleleValue the allele value to be validated
     * @return true: allele may be set for gene; false: validity check failed
     * @throws RuntimeException if the checker cannot decide whether the given
     * allele is valid or not
     *
     * @author Klaus Meffert
     * @since 2.4
     */
    public boolean verify(Gene a_gene, Object a_alleleValue)
        throws RuntimeException {
      double computedWeight = 0.0d;/**todo compute*/
      // We need to figure out what type of coin (penny, nickel, dime, quarter)
      // the current Gene represents. This is not trivial as it depends on the
      // index of the Gene within the Chromosome. The Chromosome is not
      // accessible by the Gene!
      // ----------------------------------------------------------------------
      a_gene.setEnergy(computedWeight);
      // No verification here, always conform.
      // -------------------------------------
      return true;
    }
  }
}
