/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.impl;

import java.util.*;

import org.jgap.*;
import org.jgap.distr.*;

/**
 * A implementation of the IPopulationMerger interface that merges two
 * populations as specified based on the fitness function, that is, the n
 * fittest chromosomes are returned in the new population, where n is supplied
 * by parameter.
 *
 * @author Henrique Goulart
 * @since 2.0
 */
public class FittestPopulationMerger
    implements IPopulationMerger {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.9 $";

  /*
   * The method that merges the Populations.
   * @see org.jgap.distr.IPopulationMerger#mergePopulations(org.jgap.Population, org.jgap.Population, int)
   */
  public Population mergePopulations(Population a_population1,
                                     Population a_population2,
                                     int a_new_population_size) {
    //All the chromosomes are placed in the first population for sorting.
    a_population1.addChromosomes(a_population2);
    //A sorting is made according to the chromosomes fitness values
    //See the private class FitnessChromosomeComparator below to understand.
    List allChromosomes = a_population1.getChromosomes();
    Collections.sort(allChromosomes, new FitnessChromosomeComparator());
    //Then a new population is created and the fittest "a_new_population_size" chromosomes
    //are added.
    Chromosome[] chromosomes = (Chromosome[]) allChromosomes.toArray(new
        Chromosome[0]);
    Population mergedPopulation = new Population(a_new_population_size);
    for (int i = 0; i < a_new_population_size && i < chromosomes.length; i++) {
      mergedPopulation.addChromosome(chromosomes[i]);
    }
    //The merged population is then returned.
    return mergedPopulation;
  }

  /**
   * This class is used to sort the merged population chromosomes
   * according to their fitness values. For convenience, the
   * sorting is done in a reverse way, so this comparator
   * returns 1 if the first chromosome has a LOWER fitness value.
   *
   * @author Henrique Goulart
   * @since 2.0
   */
  private class FitnessChromosomeComparator
      implements Comparator {
    /**Reference to the current FitnessEvaluator Object,
     * used for comparing chromosomes */
    private FitnessEvaluator fEvaluator = Genotype.getConfiguration().
        getFitnessEvaluator();

    /**
     * Implements the compare method using the fitness function.
     * The comparation is implemented in a reverse way to make the
     * merging easier (the list of chromosomes is sorted in a
     * descending fitness value order).
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(Object o1, Object o2) {
      //The two objects passed are always
      //Chromosomes, so a cast must be made.
      Chromosome chr1 = (Chromosome) o1;
      Chromosome chr2 = (Chromosome) o2;
      //Reverse comparison.
      if (fEvaluator.isFitter(chr2.getFitnessValue(), chr1.getFitnessValue())) {
        return 1;
      }
      else if (fEvaluator.isFitter(chr1.getFitnessValue(), chr2.getFitnessValue())) {
        return -1;
      }
      else {
        return 0;
      }
    }
  }
}
