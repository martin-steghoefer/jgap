/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.audit;

import java.io.*;

import java.awt.image.*;

import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.data.category.*;
import org.jgap.*;
import org.jgap.data.*;
import org.jgap.impl.*;
import org.jgap.xml.*;
import org.w3c.dom.*;

/**
 * Same logic as in MinimizingMakeChange except that we are using the new
 * audit capabilities provided by JGAP 2.2
 *
 * @author Klaus Meffert
 * @since 2.2
 */
public class CoinsExample {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  /**
   * The total number of times we'll let the population evolve.
   */
  private static final int MAX_ALLOWED_EVOLUTIONS = 80;

  /**
   * Executes the genetic algorithm to determine the minimum number of
   * coins necessary to make up the given target amount of change. The
   * solution will then be written to System.out.
   *
   * @param a_targetChangeAmount The target amount of change for which this
   *                             method is attempting to produce the minimum
   *                             number of coins.
   * @throws Exception
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 1.0
   */
  public static void makeChangeForAmount(int a_targetChangeAmount)
      throws
      Exception {

    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    PlotOrientation or = PlotOrientation.VERTICAL;

    // Start with a DefaultConfiguration, which comes setup with the
    // most common settings.
    // -------------------------------------------------------------
    Configuration conf = new DefaultConfiguration();
    conf.setPreservFittestIndividual(true);

    // Set the fitness function we want to use, which is our
    // MinimizingMakeChangeFitnessFunction. We construct it with
    // the target amount of change passed in to this method.
    // ---------------------------------------------------------
    FitnessFunction myFunc =
        new CoinsExampleFitnessFunction(a_targetChangeAmount);
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
    sampleGenes[0] = new IntegerGene(0, 3 * 10); // Quarters
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
    conf.setPopulationSize(50);

    PermutingConfiguration pconf = new PermutingConfiguration(conf);
    pconf.addGeneticOperatorSlot(new CrossoverOperator());
    pconf.addGeneticOperatorSlot(new MutationOperator());

    pconf.addNaturalSelectorSlot(new BestChromosomesSelector());
    pconf.addNaturalSelectorSlot(new WeightedRouletteSelector());

    pconf.addRandomGeneratorSlot(new StockRandomGenerator());
//    pconf.addRandomGeneratorSlot(new GaussianRandomGenerator());

    pconf.addFitnessFunctionSlot(new CoinsExampleFitnessFunction(a_targetChangeAmount));

    /**@todo class Evaluator:
     * input: + PermutingConfiguration
     *        + Number of evaluation runs pers config (to turn off randomness
     *          as much as possible)
     *        + output facility (data container)
     *        + optional: event subscribers
     */

    int k=0;
    while (pconf.hasNext()) {

      // Create random initial population of Chromosomes.
      // ------------------------------------------------
      Genotype population = Genotype.randomInitialGenotype(pconf.next());

      // Evolve the population. Since we don't know what the best answer
      // is going to be, we just evolve the max number of times.
      // ---------------------------------------------------------------
      for (int i = 0; i < MAX_ALLOWED_EVOLUTIONS; i++) {
        population.evolve();
        // add current best fitness to chart
        double fitness = population.getFittestChromosome().getFitnessValue();
        if (i % 3 == 0) {
//          String s = String.valueOf(k)+"_"+
          String s = String.valueOf(i);
          dataset.setValue(fitness, "Fitness "+k, s);
        }
      }

      // Display the best solution we found.
      // -----------------------------------
      Chromosome bestSolutionSoFar = population.getFittestChromosome();
      System.out.println("The best solution has a fitness value of " +
                         bestSolutionSoFar.getFitnessValue());
      System.out.println("It contained the following: ");
      System.out.println("\t" +
                         CoinsExampleFitnessFunction.
                         getNumberOfCoinsAtGene(
          bestSolutionSoFar, 0) + " quarters.");
      System.out.println("\t" +
                         CoinsExampleFitnessFunction.
                         getNumberOfCoinsAtGene(
          bestSolutionSoFar, 1) + " dimes.");
      System.out.println("\t" +
                         CoinsExampleFitnessFunction.
                         getNumberOfCoinsAtGene(
          bestSolutionSoFar, 2) + " nickels.");
      System.out.println("\t" +
                         CoinsExampleFitnessFunction.
                         getNumberOfCoinsAtGene(
          bestSolutionSoFar, 3) + " pennies.");
      System.out.println("For a total of " +
                         CoinsExampleFitnessFunction.amountOfChange(
          bestSolutionSoFar) + " cents in " +
                         CoinsExampleFitnessFunction.
                         getTotalNumberOfCoins(
          bestSolutionSoFar) + " coins.");
      k++;
    }
    // Create chart
    JFreeChart chart = ChartFactory.createLineChart(
        "JGAP: Evolution progress",
        "Evolution cycle", "Fitness value", dataset, or, true /*legend*/,
        true
        /*tooltips*/
        , false /*urls*/);
    BufferedImage image = chart.createBufferedImage(640, 480);
    FileOutputStream fo = new FileOutputStream("c:\\chart.jpg");
    ChartUtilities.writeBufferedImageAsJPEG(fo, 0.7f, image);
  }

  public static void main(String[] args) {
    if (args.length != 1) {
      System.out.println("Syntax: CoinsExample <amount>");
    }
    else {
      try {
        int amount = Integer.parseInt(args[0]);
        if (amount < 1 ||
            amount >= CoinsExampleFitnessFunction.MAX_BOUND) {
          System.out.println("The <amount> argument must be between 1 and "
                             +
                             (CoinsExampleFitnessFunction.MAX_BOUND - 1)
                             + ".");
        }
        else {
          try {
            makeChangeForAmount(amount);
          }
          catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
      catch (NumberFormatException e) {
        System.out.println(
            "The <amount> argument must be a valid integer value");
      }
    }
  }
}
