/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.audit;

import java.io.*;
import org.jgap.*;
import org.jgap.impl.*;
import examples.*;
import org.jgap.audit.*;
import java.util.*;

/**
 * Demonstrates how to use evolution monitors to stop evolution when certain
 * criteria are met. You may want to rerun the example several times to see
 * different criteria match. Concerning your system, an adaptation of the timed
 * monitor (max. runtime) may be necessary.
 * <p/>
 * Please see class examples.MinimizingMakeChange for the original example,
 * which was just extended by monitors here.
 *
 * @author Klaus Meffert
 * @since 3.4.4
 */
public class EvolutionMonitorExample {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  /**
   * Executes the genetic algorithm to determine the minimum number of
   * coins necessary to make up the given target amount of change. The
   * solution will then be written to System.out.
   *
   * @param a_targetChangeAmount the target amount of change for which this
   * method is attempting to produce the minimum number of coins
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.4.4
   */
  public static void makeChangeForAmount(int a_targetChangeAmount)
      throws Exception {
    // Start with a DefaultConfiguration, which comes setup with the
    // most common settings.
    // -------------------------------------------------------------
    Configuration conf = new DefaultConfiguration();
    // Care that the fittest individual of the current population is
    // always taken to the next generation.
    // Consider: With that, the pop. size may exceed its original
    // size by one sometimes!
    // -------------------------------------------------------------
    conf.setPreservFittestIndividual(true);
    // Set the fitness function we want to use, which is our
    // MinimizingMakeChangeFitnessFunction. We construct it with
    // the target amount of change passed in to this method.
    // ---------------------------------------------------------
    FitnessFunction myFunc =
        new MinimizingMakeChangeFitnessFunction(a_targetChangeAmount);
    conf.setFitnessFunction(myFunc);
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
    // ------------------------------------------------
    Genotype population = Genotype.randomInitialGenotype(conf);
    long startTime = System.currentTimeMillis();
    // Configure monitors to let the evolution stop when defined criteria
    // are met.
    // ------------------------------------------------------------------
    List monitors = new Vector();
    monitors.add(new TimedMonitor(6));
    monitors.add(new FitnessImprovementMonitor(1, 3, 5.0d));
    IEvolutionMonitor monitor = new ChainedMonitors(monitors, 2);
    // Start the evolution.
    // --------------------
    List<String> messages = population.evolve(monitor);
    if (messages.size() > 0) {
      for (String msg : messages) {
        System.out.println("Message from monitor: " + msg+"\n");
      }
    }
    // Evaluate the result.
    // --------------------
    long endTime = System.currentTimeMillis();
    System.out.println("Total evolution time: " + (endTime - startTime)
                       + " ms");
    // Display the best solution we found.
    // -----------------------------------
    IChromosome bestSolutionSoFar = population.getFittestChromosome();
    System.out.println("The best solution has a fitness value of " +
                       bestSolutionSoFar.getFitnessValue());
    System.out.println("It contained the following: ");
    System.out.println("\t" +
                       MinimizingMakeChangeFitnessFunction.
                       getNumberOfCoinsAtGene(
                           bestSolutionSoFar, 0) + " quarters.");
    System.out.println("\t" +
                       MinimizingMakeChangeFitnessFunction.
                       getNumberOfCoinsAtGene(
                           bestSolutionSoFar, 1) + " dimes.");
    System.out.println("\t" +
                       MinimizingMakeChangeFitnessFunction.
                       getNumberOfCoinsAtGene(
                           bestSolutionSoFar, 2) + " nickels.");
    System.out.println("\t" +
                       MinimizingMakeChangeFitnessFunction.
                       getNumberOfCoinsAtGene(
                           bestSolutionSoFar, 3) + " pennies.");
    System.out.println("For a total of " +
                       MinimizingMakeChangeFitnessFunction.amountOfChange(
                           bestSolutionSoFar) + " cents in " +
                       MinimizingMakeChangeFitnessFunction.
                       getTotalNumberOfCoins(
                           bestSolutionSoFar) + " coins.");
  }

  /**
   * Main method to start the example.
   *
   * @param args ignored here
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.4.4
   */
  public static void main(String[] args)
      throws Exception {
    makeChangeForAmount(93);
  }
}
