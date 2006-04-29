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

import java.io.*;
import java.util.*;

/**
 * Selects individuals proportionally according to their adjusted fitness.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class FitnessProportionateSelection
    implements INaturalGPSelector, Serializable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.4 $";

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
  }
}
