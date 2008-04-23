/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.multiobjective;

import java.util.*;
import org.jgap.*;
import org.jgap.impl.*;

/**
 * Example for a multiobjective problem. Here, we have a function F with one
 * input parameter t and two output values F1 and F2, with F1 = t²
 * and F2 = (t - 2)². This example is from Goldberg (pp. 199), who adapted it
 * from Schaffer (1984).
 *
 * @author Klaus Meffert
 * @since 2.6
 */
public class MultiObjectiveExample {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.6 $";

  /**
   * The total number of times we'll let the population evolve.
   */
  private static final int MAX_ALLOWED_EVOLUTIONS = 200;

  /**
   * Executes the genetic algorithm.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void execute()
      throws Exception {
    // Start with a DefaultConfiguration, which comes setup with the
    // most common settings.
    // -------------------------------------------------------------
    Configuration conf = new DefaultConfiguration();
    // Add BestChromosomesSelector with doublettes allowed.
    // ----------------------------------------------------
    conf.removeNaturalSelectors(true);
    BestChromosomesSelector bestChromsSelector = new BestChromosomesSelector(
        conf, 0.95d);
    bestChromsSelector.setDoubletteChromosomesAllowed(true);
    conf.addNaturalSelector(bestChromsSelector, true);

    conf.reset();
    conf.setFitnessEvaluator(new MOFitnessEvaluator());
    conf.setPreservFittestIndividual(false);
    conf.setKeepPopulationSizeConstant(false);
    // Set the fitness function we want to use, which is our
    // MinimizingMakeChangeFitnessFunction. We construct it with
    // the target amount of change passed in to this method.
    // ---------------------------------------------------------
    BulkFitnessFunction myFunc =
        new MultiObjectiveFitnessFunction();
    conf.setBulkFitnessFunction(myFunc);

    // Set sample chromosome.
    // ----------------------
    Gene[] sampleGenes = new Gene[1];
    sampleGenes[0] = new DoubleGene(conf, MultiObjectiveFitnessFunction.MIN_X,
                                    MultiObjectiveFitnessFunction.MAX_X);
    IChromosome sampleChromosome = new Chromosome(conf, sampleGenes);
    conf.setSampleChromosome(sampleChromosome);

    // Finally, we need to tell the Configuration object how many
    // Chromosomes we want in our population. The more Chromosomes,
    // the larger number of potential solutions (which is good for
    // finding the answer), but the longer it will take to evolve
    // the population (which could be seen as bad).
    // ------------------------------------------------------------
    conf.setPopulationSize(500);
    // Create random initial population of Chromosomes.
    // ------------------------------------------------
    Genotype population = Genotype.randomInitialGenotype(conf);
    // Evolve the population. Since we don't know what the best answer
    // is going to be, we just evolve the max number of times.
    // ---------------------------------------------------------------
    for (int i = 0; i < MAX_ALLOWED_EVOLUTIONS; i++) {
      population.evolve();
    }
    // Remove solutions that are not Pareto-optimal.
    // ---------------------------------------------
    List chroms = population.getPopulation().getChromosomes();
    int size = population.getPopulation().getChromosomes().size();
    int i = 0;
    boolean removed = false;
    MOFitnessComparator comp = new MOFitnessComparator();
    while (i<size-1) {
      IChromosome chrom1 = population.getPopulation().getChromosome(i);
      int j = i + 1;
      while (j < size) {
        IChromosome chrom2 = population.getPopulation().getChromosome(j);
        int res = comp.compare(chrom1, chrom2);
        if (res != 0) {
          if (res == -1) {
            population.getPopulation().getChromosomes().remove(i);
            size--;
            removed = true;
            break;
          }
          else {
            population.getPopulation().getChromosomes().remove(j);
            size--;
          }
        }
        else {
          j++;
        }
      }
      if (removed) {
        removed = false;
      }
      else {
        i++;
      }
    }
    // Print all Pareto-optimal solutions.
    // -----------------------------------
    Collections.sort(chroms, comp);
    for (int k=0;k<chroms.size();k++) {
      Chromosome bestSolutionSoFar = (Chromosome) chroms.get(k);
      System.out.println(MultiObjectiveFitnessFunction.
                         getVector(bestSolutionSoFar));
    }
  }

  /**
   * Main method to run the example.
   *
   * @param args ignored
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public static void main(String[] args)
      throws Exception {
    MultiObjectiveExample instance = new MultiObjectiveExample();
    instance.execute();
  }

  /**
   * @author Klaus Meffert
   * @since 2.6
   */
  public class MOFitnessComparator
      implements java.util.Comparator {

    public int compare(final Object a_chrom1, final Object a_chrom2) {
      List v1 = ( (Chromosome) a_chrom1).getMultiObjectives();
      List v2 = ( (Chromosome) a_chrom2).getMultiObjectives();
      int size = v1.size();
      if (size != v2.size()) {
        throw new RuntimeException("Size of objectives inconsistent!");
      }
      boolean better1 = false;
      boolean better2 = false;
      for (int i = 0; i < size; i++) {
        double d1 = ( (Double) v1.get(i)).doubleValue();
        double d2 = ( (Double) v2.get(i)).doubleValue();
        if (d1 < d2) {
          better1 = true;
        }
        else if (d2 < d1) {
          better2 = true;
        }
      }
      if (better1) {
        if (better2) {
          return 0;
        }
        else {
          return 1;
        }
      }
      else {
        if (better2) {
          return -1;
        }
        else {
          return 0;
        }
      }
    }
  }
}
