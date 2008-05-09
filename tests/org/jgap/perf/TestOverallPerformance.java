/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.perf;

import java.util.*;
import org.jgap.*;
import org.jgap.impl.*;

/**
 * This class provides an implementation of an overall performance test.
 * To obtain this, the provided example has been modified slightly, regarding
 * the random number generator. We use a static number generator here which does
 * not deserve the name "random generator". With that we have a determined
 * calculation path that results in reproducable results.
 * By executing the example several times we get a performance measurement.
 * The measured time has to be compared to other results manually as with
 * different hardware equipment the numbers vary a lot.
 *
 * @author Klaus Meffert
 * @since 2.0
 */
public class TestOverallPerformance {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.7 $";

  /**
   * The total number of times we'll let the population evolve.
   */
  private static final int MAX_ALLOWED_EVOLUTIONS = 1000;

  /**
   * Executes the genetic algorithm to determine the minimum number of
   * coins necessary to make up the given target amount of change. The
   * solution will then be written to System.out.
   *
   * @param a_targetChangeAmount the target amount of change for which this
   * method is attempting to produce the minimum number of coins
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void makeChangeForAmount(int a_targetChangeAmount)
      throws Exception {
    // Start with a DefaultConfiguration, which comes setup with the
    // most common settings.
    // -------------------------------------------------------------
    Configuration.reset();
    Configuration conf = new DefaultConfiguration();
    RandomGeneratorForTesting gen = new RandomGeneratorForTesting();
    gen.setNextDouble(0.5d);
    gen.setNextBoolean(true);
    gen.setNextInt(3);
    gen.setNextFloat(0.7f);
    gen.setNextLong(6);
    conf.setRandomGenerator(gen);
    // Set the fitness function we want to use, which is our
    // MinimizingMakeChangeFitnessFunction. We construct it with
    // the target amount of change passed in to this method.
    // ---------------------------------------------------------
    FitnessFunction myFunc =
        new TestOverallPerformanceFitnessFunc(a_targetChangeAmount);
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
    // Here we use "fantasy" coins just to have more genes and bloat the time
    // consumed for test performance test!
    // --------------------------------------------------------------
    Gene[] sampleGenes = new Gene[10];
    sampleGenes[0] = new IntegerGene(conf, 0, 3); // Quarters
    sampleGenes[1] = new IntegerGene(conf, 0, 2); // Dimes
    sampleGenes[2] = new IntegerGene(conf, 0, 1); // Nickels
    sampleGenes[3] = new IntegerGene(conf, 0, 4); // Pennies
    sampleGenes[4] = new IntegerGene(conf, 0, 3); // A
    sampleGenes[5] = new IntegerGene(conf, 0, 1); // B
    sampleGenes[6] = new IntegerGene(conf, 0, 1); // C
    sampleGenes[7] = new IntegerGene(conf, 0, 2); // D
    sampleGenes[8] = new IntegerGene(conf, 0, 3); // E
    sampleGenes[9] = new IntegerGene(conf, 0, 1); // F
    Chromosome sampleChromosome = new Chromosome(conf, sampleGenes);
    conf.setSampleChromosome(sampleChromosome);
    // Finally, we need to tell the Configuration object how many
    // Chromosomes we want in our population. The more Chromosomes,
    // the larger number of potential solutions (which is good for
    // finding the answer), but the longer it will take to evolve
    // the population (which could be seen as bad). We'll just set
    // the population size to 10000 here. It is that big because of performance
    // test issues!
    // ------------------------------------------------------------
    conf.setPopulationSize(10000);
    // Create random initial population of Chromosomes.
    // ------------------------------------------------
    Genotype population = Genotype.randomInitialGenotype(conf);
    // Evolve the population. Since we don't know what the best answer
    // is going to be, we just evolve the max number of times.
    // ---------------------------------------------------------------
    for (int i = 0; i < MAX_ALLOWED_EVOLUTIONS; i++) {
      population.evolve();
    }
    // Determine the best solution we found.
    // -------------------------------------
    population.getFittestChromosome();
  }

  /**
   * Execute the performance test.
   *
   * @param args ignored
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public static void main(String[] args)
      throws Exception {
    final int amount = 287;
    final int numRuns = 20;
    long starttime, timeMillis;
    System.out.println("Test started.");
    // get current time
    starttime = getCurrentMilliseconds();
    for (int i = 0; i < numRuns; i++) {
      TestOverallPerformance runner = new TestOverallPerformance();
      runner.makeChangeForAmount(amount);
    }
    // calculate time of run
    timeMillis = getCurrentMilliseconds() - starttime;
    System.out.println("Overall time needed for executing performance test: "
                       + timeMillis + " [millisecs]");
  }

  /**
   * @return current time in milliseconds
   */
  private static long getCurrentMilliseconds() {
    Calendar cal = Calendar.getInstance(TimeZone.getDefault());
    return cal.getTimeInMillis();
  }
}
