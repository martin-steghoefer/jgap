/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
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
 * @deprecated use TournamentSelector instead
 */
public class FitnessProportionateSelection
    implements INaturalGPSelector, Serializable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.7 $";

  /**
   * Constructor.
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public FitnessProportionateSelection() {
  }

  /**
   *
   * @param a_genotype GPGenotype
   * @return IGPProgram
   * @deprecated use TournamentSelector or WeightedGPRouletteSelector instead
   */
  public IGPProgram select(GPGenotype a_genotype) {
    double chosen = a_genotype.getGPConfiguration().getRandomGenerator().
        nextFloat() * a_genotype.getTotalFitness();
    int num = 0;
    GPPopulation pop = a_genotype.getGPPopulation();
    int popSize = pop.size();
    // Consider the fitness comparator used.
    // -------------------------------------
    IGPFitnessEvaluator evaluator = a_genotype.getGPConfiguration().getGPFitnessEvaluator();
    num = Arrays.binarySearch(pop.getFitnessRanks(), (float) chosen);
    if (num >= 0) {
      return pop.getGPProgram(num);
    }
    else {
      /**@todo implement for deltaMode*/
      boolean deltaMode;
      if (evaluator.isFitter(2.0d, 1.0d)) {
        deltaMode = false;
      }
      else {
        deltaMode = true;
      }
      for (num = 1; num < popSize; num++) {
        if (chosen < pop.getFitnessRank(num)) {
          break;
        }
      }
      num--;
      if (num >= popSize - 1) {
        if (popSize - 1 < 1) {
          num = 1;
        }
        else {
          // fall back to complete randomization.
          // ------------------------------------
          num = a_genotype.getGPConfiguration().getRandomGenerator().
              nextInt(popSize - 1);
        }
      }
      return pop.getGPProgram(num);
    }
  }
}
