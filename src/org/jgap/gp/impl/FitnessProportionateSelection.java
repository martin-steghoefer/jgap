/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp.impl;

import java.io.*;
import java.util.*;

import org.jgap.gp.*;

/**
 * Selects individuals proportionally according to their adjusted fitness.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class FitnessProportionateSelection
    implements INaturalGPSelector, Serializable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  public GPProgram select(GPGenotype a_genotype) {
    double chosen = a_genotype.getGPConfiguration().getRandomGenerator().
        nextFloat() * a_genotype.getTotalFitness();
    int num = 0;
    GPPopulation pop = a_genotype.getGPPopulation();
    num = Arrays.binarySearch(pop.getFitnessRanks(), (float) chosen);
    if (num >= 0) {
      return pop.getGPProgram(num);
    }
    else {
      for (num = 1; num < pop.size(); num++) {
        if (chosen < pop.getFitnessRank(num)) {
          break;
        }
      }
      num--;
      if (num >= pop.size() - 1) {
        num = a_genotype.getGPConfiguration().getRandomGenerator().
            nextInt(pop.size() - 1);
      }
      return pop.getGPProgram(num);
    }
  }
}
