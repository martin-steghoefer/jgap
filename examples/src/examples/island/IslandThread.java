/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.island;

import org.jgap.*;
import org.jgap.impl.*;
import examples.*;

/**
 * Simple example of an island thread. It utilizes the former example class
 * MinimizingMakeChangeFitnessFunction for reasons of simplicity.
 *
 * @author Klaus Meffert
 * @since 3.5
 */
public class IslandThread
    extends Thread {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  private Genotype gen;

  private int m_nextNumber;

  private IChromosome m_best;

  private boolean m_finished;

  public IslandThread(int nextNumber)
      throws Exception {
    m_nextNumber = nextNumber;
    String threadKey = Thread.currentThread().getId() + "/" + m_nextNumber;
    System.out.println("Starting thread: " + nextNumber);
    Configuration conf = new DefaultConfiguration(threadKey, threadKey);
    FitnessFunction myFunc =
        new MinimizingMakeChangeFitnessFunction(93);
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
    gen = Genotype.randomInitialGenotype(conf);
  }

  static boolean locked = false;

  public void run() {
    try {
      for (int i = 1; i <= 500; i++) {
        gen.evolve();
        Thread.currentThread().sleep( (int) Math.random() *
                                     (20 + m_nextNumber * 5));
      }
      m_finished = true;
      m_best = gen.getFittestChromosome();
      // Use a lock to avoid cluttered output of best solution.
      // ------------------------------------------------------
      while (locked) {
        Thread.currentThread().sleep(1);
      }
      try {
        locked = true;
        System.out.println("Thread " + m_nextNumber + ": Best solution:");
        System.out.println("  Fitness value: " +
                           m_best.getFitnessValue());
        System.out.print("  Solutions contains: ");
        System.out.print(MinimizingMakeChangeFitnessFunction.
                         getNumberOfCoinsAtGene(
                             m_best, 0) + " quarters");
        System.out.print(", " + MinimizingMakeChangeFitnessFunction.
                         getNumberOfCoinsAtGene(m_best, 1) + " dimes");
        System.out.print(", " + MinimizingMakeChangeFitnessFunction.
                         getNumberOfCoinsAtGene(m_best, 2) + " nickels");
        System.out.print(", " + MinimizingMakeChangeFitnessFunction.
                         getNumberOfCoinsAtGene(m_best, 3) + " pennies");
        System.out.println(" for a total of " +
                           MinimizingMakeChangeFitnessFunction.amountOfChange(
                               m_best) + " cents in " +
                           MinimizingMakeChangeFitnessFunction.
                           getTotalNumberOfCoins(
                               m_best) + " coins.\n");
      } finally {
        locked = false;
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public boolean isFinished() {
    return m_finished;
  }

  public IChromosome getBestSolution() {
    if (!m_finished) {
      throw new RuntimeException("Thread not finished yet!");
    }
    return m_best;
  }
}
