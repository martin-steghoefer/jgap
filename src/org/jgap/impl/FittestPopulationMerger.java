/*
 * This file is part of JGAP.
 *
 * JGAP is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * JGAP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser Public License
 * along with JGAP; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package org.jgap.impl;

import java.util.List;
import java.util.Comparator;
import java.util.Collections;

import org.jgap.Chromosome;
import org.jgap.Population;
import org.jgap.Genotype;
import org.jgap.FitnessEvaluator;
import org.jgap.distr.IPopulationMerger;

/**
 * A implementation of the IPopulationMerger interface that
 * merges two populations as specified based on the fitness
 * function, that is, the n fittest chromosomes are returned
 * in the new population, where n is supplied by parameter.
 * @author Henrique Goulart
 * @since 2.0
 */
public class FittestPopulationMerger implements IPopulationMerger {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

    /* The method that merges the Populations.
     * @see org.jgap.distr.IPopulationMerger#mergePopulations(org.jgap.Population, org.jgap.Population, int)
     */
    public Population mergePopulations(Population a_population1, Population a_population2, int a_new_population_size) {
        //All the chromosomes are placed in the first population for sorting.
        a_population1.addChromosomes(a_population2);
        //A sorting is made according to the chromosomes fitness values
        //See the private class FitnessChromosomeComparator below to understand.
        List allChromosomes = a_population1.getChromosomes();
        Collections.sort(allChromosomes, new FitnessChromosomeComparator());
        //Then a new population is created and the fittest "a_new_population_size" chromosomes
        //are added.
        Chromosome[] chromosomes = (Chromosome[]) allChromosomes.toArray(new Chromosome[0]);
        Population mergedPopulation = new Population(a_new_population_size);
        for (int i = 0; i < a_new_population_size && i < chromosomes.length; i++)
           mergedPopulation.addChromosome(chromosomes[i]);
        //The merged population is then returned.
        return mergedPopulation;
    }

    /**
     * This class is used to sort the merged population chromosomes
     * according to their fitness values. For convenience, the
     * sorting is done in a reverse way, so this comparator
     * returns 1 if the first chromosome has a LOWER fitness value.
     * @author Henrique Goulart
     */
    private class FitnessChromosomeComparator implements Comparator {

        /**Reference to the current FitnessEvaluator Object,
         * used for comparing chromosomes */
        private FitnessEvaluator fEvaluator = Genotype.getConfiguration().getFitnessEvaluator();

        /** Implements the compare method using the fitness function.
         * The comparation is implemented in a reverse way to make the
         * merging easier (the list of chromosomes is sorted in a
         * descending fitness value order).
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        public int compare(Object o1, Object o2) {
          /**@todo use FitnessEvaluator instead of static comparation!*/
            //The two objects passed are always
            //Chromosomes, so a cast must be made.
            Chromosome chr1 = (Chromosome) o1;
            Chromosome chr2 = (Chromosome) o2;
            //Reverse comparison.
            if (fEvaluator.isFitter(chr2.getFitnessValue(),chr1.getFitnessValue()))
                return 1;
            else if (fEvaluator.isFitter(chr1.getFitnessValue(),chr2.getFitnessValue()))
                return -1;
            else return 0;
        }
    }

}
