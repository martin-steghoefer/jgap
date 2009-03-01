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
import java.awt.image.*;
import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.data.category.*;
import org.jgap.*;
import org.jgap.impl.*;
import org.jgap.audit.*;

/**
 * Same logic as in MinimizingMakeChange except that we are using the new
 * audit capabilities provided by JGAP 2.2
 *
 * @author Klaus Meffert
 * @since 2.2
 */
public class CoinsExample {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.25 $";

  /**
   * The total number of times we'll let the population evolve.
   */
  private static final int MAX_ALLOWED_EVOLUTIONS = 80;

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
    sampleGenes[0] = new IntegerGene(conf, 0, 3 * 10); // Quarters
    sampleGenes[1] = new IntegerGene(conf, 0, 2 * 10); // Dimes
    sampleGenes[2] = new IntegerGene(conf, 0, 1 * 10); // Nickels
    sampleGenes[3] = new IntegerGene(conf, 0, 4 * 10); // Pennies
    Chromosome sampleChromosome = new Chromosome(conf, sampleGenes);
    conf.setSampleChromosome(sampleChromosome);
    // Finally, we need to tell the Configuration object how many
    // Chromosomes we want in our population. The more Chromosomes,
    // the larger number of potential solutions (which is good for
    // finding the answer), but the longer it will take to evolve
    // the population (which could be seen as bad).
    // ------------------------------------------------------------
    conf.setPopulationSize(50);
    // Added here for demonstrating purposes is a permuting configuration.
    // It allows for evaluating which configuration could work best for
    // the given problem.
    // -------------------------------------------------------------------
    PermutingConfiguration pconf = new PermutingConfiguration(conf);
    pconf.addGeneticOperatorSlot(new CrossoverOperator(conf));
    pconf.addGeneticOperatorSlot(new MutationOperator(conf));
    pconf.addNaturalSelectorSlot(new BestChromosomesSelector(conf));
    pconf.addNaturalSelectorSlot(new WeightedRouletteSelector(conf));
    pconf.addRandomGeneratorSlot(new StockRandomGenerator());
    RandomGeneratorForTesting rn = new RandomGeneratorForTesting();
    rn.setNextDouble(0.7d);
    rn.setNextInt(2);
    pconf.addRandomGeneratorSlot(rn);
    pconf.addRandomGeneratorSlot(new GaussianRandomGenerator());
    pconf.addFitnessFunctionSlot(new CoinsExampleFitnessFunction(
        a_targetChangeAmount));
    Evaluator eval = new Evaluator(pconf);
    /**@todo class Evaluator:
     * input:
     *   + PermutingConfiguration
     *   + Number of evaluation runs pers config (to turn off randomness
     *     as much as possible)
     *   + output facility (data container)
     *   + optional: event subscribers
     * output:
     *   + averaged curve of fitness value thru all generations
     *   + best fitness value accomplished
     *   + average number of performance improvements for all generations
     */
    int permutation = 0;
    while (eval.hasNext()) {
      // Create random initial population of Chromosomes.
      // ------------------------------------------------
      Genotype population = Genotype.randomInitialGenotype(eval.next());
      for (int run = 0; run < 10; run++) {
        // Evolve the population. Since we don't know what the best answer
        // is going to be, we just evolve the max number of times.
        // ---------------------------------------------------------------
        for (int i = 0; i < MAX_ALLOWED_EVOLUTIONS; i++) {
          population.evolve();
          // Add current best fitness to chart.
          // ----------------------------------
          double fitness = population.getFittestChromosome().getFitnessValue();
          if (i % 3 == 0) {
            String s = String.valueOf(i);
//            Number n = eval.getValue("Fitness " + permutation, s);
//            double d;
//            if (n != null) {
//              // calculate historical average
//              d = n.doubleValue() + fitness/(run+1);
//            }
//            else {
//              d = fitness;
//            }
            eval.setValue(permutation, run, fitness, "" + permutation, s);
            eval.storeGenotype(permutation, run, population);
//            eval.setValue(permutation,run,fitness, new Integer(0), s);
          }
        }
      }
      // Display the best solution we found.
      // -----------------------------------
      IChromosome bestSolutionSoFar = population.getFittestChromosome();
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
      permutation++;
    }
    // Create chart: fitness values average over all permutations.
    // -----------------------------------------------------------

    // Construct JFreeChart Dataset.
    // -----------------------------
    KeyedValues2D myDataset = eval.calcAvgFitness( -1); //eval.getData();
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    for (int ii = 0; ii < myDataset.getColumnCount(); ii++) {
      for (int jj = 0; jj < myDataset.getRowCount(); jj++) {
        dataset.setValue(myDataset.getValue(myDataset.getRowKey(jj),
            myDataset.getColumnKey(ii)),
                         "Perm " + myDataset.getRowKey(jj),
                         myDataset.getColumnKey(ii));
      }
    }
    PlotOrientation or = PlotOrientation.VERTICAL;
    JFreeChart chart = ChartFactory.createLineChart(
        "JGAP: Evolution progress",
        "Evolution cycle", "Fitness value", dataset, or, true /*legend*/,
        true
        /*tooltips*/
        , false /*urls*/);
    BufferedImage image = chart.createBufferedImage(640, 480);
    FileOutputStream fo = new FileOutputStream(
        "c:\\JGAP_chart_fitness_values.jpg");
    ChartUtilities.writeBufferedImageAsJPEG(fo, 0.7f, image);
    // Performance metrics for each single permutation.
    // ------------------------------------------------
    int maxPerm = permutation - 1;
    double avgBestFitness = 0.0d;
    int avgBestGen = 0;
    double avgAvgFitness = 0.0d;
    double avgAvgDiv = 0.0d;
    double avgAvgBestD = 0.0d;
    for (int i = 0; i < maxPerm; i++) {
//      myDataset = eval.calcAvgFitness(i);
      Evaluator.GenotypeDataAvg dataAvg = eval.calcPerformance(i);
      System.err.println("-----------------------------");
      System.err.println("Perm " + i);
      System.err.println("Best Fitness " + dataAvg.bestFitnessValue);
      System.err.println(" Generation  " + dataAvg.bestFitnessValueGeneration);
      System.err.println(" BestFit/Gen " +
                         dataAvg.bestFitnessValue /
                         dataAvg.bestFitnessValueGeneration);
      System.err.println("Avg. Fitness " + dataAvg.avgFitnessValue);
      System.err.println("Avg. Div.    " + dataAvg.avgDiversityFitnessValue);
      System.err.println("Avg. BestD   " + dataAvg.avgBestDeltaFitnessValue);
      avgBestFitness += dataAvg.bestFitnessValue;
      avgBestGen += dataAvg.bestFitnessValueGeneration;
      avgAvgFitness += dataAvg.avgFitnessValue;
      avgAvgDiv += dataAvg.avgDiversityFitnessValue;
      avgAvgBestD += dataAvg.avgBestDeltaFitnessValue;
    }
    // Performance metrics for all permutations.
    // -----------------------------------------
    System.err.println("\nOverall Statistics for all permutations");
    System.err.println("----------------------------------------");
    System.err.println("Avg. Best Fitness     " + avgBestFitness / maxPerm);
    System.err.println("Avg. Best Generation  " + avgBestGen / maxPerm);
    System.err.println("Avg. Avg. Fitness     " + avgAvgFitness / maxPerm);
    System.err.println("Avg. Avg. Diversity   " + avgAvgDiv / maxPerm);
    System.err.println("Avg. Avg. BestD       " + avgAvgBestD / maxPerm);
    // Create chart: performance metrics for all permutations.
    // -----------------------------------------------------------
    dataset = new DefaultCategoryDataset();
    for (int ii = 0; ii < myDataset.getColumnCount(); ii++) {
      for (int jj = 0; jj < myDataset.getRowCount(); jj++) {
        dataset.setValue(myDataset.getValue(myDataset.getRowKey(jj),
            myDataset.getColumnKey(ii)),
                         myDataset.getRowKey(jj), myDataset.getColumnKey(ii));
      }
    }
    chart = ChartFactory.createLineChart(
        "JGAP: Evolution progress",
        "Evolution cycle", "Fitness value", dataset, or, true /*legend*/,
        true
        /*tooltips*/
        , false /*urls*/);
    image = chart.createBufferedImage(640, 480);
    fo = new FileOutputStream("c:\\JGAP_chart_fitness_values_1.jpg");
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
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      } catch (NumberFormatException e) {
        System.out.println(
            "The <amount> argument must be a valid integer value");
      }
    }
  }
}
