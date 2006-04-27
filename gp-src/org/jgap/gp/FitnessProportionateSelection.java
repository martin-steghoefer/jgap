/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Selects individuals proportionally according to their adjusted fitness.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class FitnessProportionateSelection
    extends SelectionMethod
    implements Serializable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  public ProgramChromosome select(GPGenotype world) {
    double chosen = world.getConfiguration().getRandomGenerator().nextFloat() *
        world.getTotalFitness();
    int num = 0;
    GPPopulation pop = world.getGPPopulation();
    num = Arrays.binarySearch(pop.fitnessRank, (float) chosen);
    if (num >= 0) {
      return (ProgramChromosome) pop.getChromosome(num);
    }
    else {
      for (num = 1; num < pop.size(); num++) {
        if (chosen < pop.fitnessRank[num]) {
          break;
        }
      }
      num--;
      if (num >= pop.size() - 1) {
        num = world.getConfiguration().getRandomGenerator().nextInt(pop.size());
      }
      return (ProgramChromosome) pop.getChromosome(num);
    }
    /*
         for (num=0; chosen>=0 && num<popSize; num++)
           try {
            chosen -= world.getPopulation().getIndividual(num).getFitness();
           } catch (ArrayIndexOutOfBoundsException ex) {
             System.out.println("FitnessProportionateSelection:");
             System.out.println("pop size = " + popSize);
             System.out.println("num = " + num);
             System.out.println("chosen = " + chosen);
             System.out.println("origChosen = " + origChosen);
            System.out.println("World fitness = " + world.getTotalFitness());
             throw ex;
           }
         if (num>=popSize) // Can happen if chosen==totalFitness
           num=popSize-1;
     */

//    return world.getPopulation().getIndividual(num);
  }
}
