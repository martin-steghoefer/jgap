package org.jgap.gp;

import java.io.Serializable;
import java.util.Arrays;
import org.jgap.*;

/**
 * Selects individuals proportionally according to their adjusted fitness.
 * <P>
 * Copyright (c) 2000 Robert Baruch. This code is released under
 * the <a href=http://www.gnu.org/copyleft/gpl.html>GNU General Public License</a> (GPL).<p>
 *
 * @author Robert Baruch (jgprog@sourceforge.net)
 * @version $Id: FitnessProportionateSelection.java,v 1.1 2006-04-26 08:30:06 klausikm Exp $
 */
public class FitnessProportionateSelection
    extends SelectionMethod
    implements Serializable {
  public ProgramChromosome select(GPGenotype world) {
    double chosen = world.getConfiguration().getRandomGenerator().nextFloat() *
        world.getTotalFitness();
    // float origChosen = chosen;
    int num = 0;
    GPPopulation pop = world.getGPPopulation();
    // int popSize = pop.individuals.length;

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
      if (num>=pop.size()-1) { //KM
        num = world.getConfiguration().getRandomGenerator().nextInt(pop.size());//KM
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
