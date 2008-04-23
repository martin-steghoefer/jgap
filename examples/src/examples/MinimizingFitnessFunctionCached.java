/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples;

import org.jgap.*;

/**
 * For any Javadoc see MinimizingMakeChangeFitnessFunction.<p>
 * Additionally, this fitness function is cached.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public class MinimizingFitnessFunctionCached
    extends CachedFitnessFunction {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  private final int m_targetAmount;

  public static final int MAX_BOUND = 4000;

  public MinimizingFitnessFunctionCached(int a_targetAmount) {
    if (a_targetAmount < 1 || a_targetAmount >= MAX_BOUND) {
      throw new IllegalArgumentException(
          "Change amount must be between 1 and " + MAX_BOUND + " cents.");
    }
    m_targetAmount = a_targetAmount;
  }

  public double evaluate(IChromosome a_subject) {
    boolean defaultComparation = a_subject.getConfiguration().
        getFitnessEvaluator().isFitter(2, 1);

    int changeAmount = amountOfChange(a_subject);
    int totalCoins = getTotalNumberOfCoins(a_subject);
    int changeDifference = Math.abs(m_targetAmount - changeAmount);
    double fitness;
    if (defaultComparation) {
      fitness = 0.0d;
    }
    else {
      fitness = MAX_BOUND/2;
    }
    // Step 1: Determine distance of amount represented by solution from
    // the target amount. If the change difference is greater than zero we
    // will divide one by the difference in change between the
    // solution amount and the target amount. That will give the desired
    // effect of returning higher values for amounts closer to the target
    // amount and lower values for amounts further away from the target
    // amount.
    // In the case where the change difference is zero it means that we have
    // the correct amount and we assign a higher fitness value.
    // ---------------------------------------------------------------------
    if (defaultComparation) {
      fitness += changeDifferenceBonus(MAX_BOUND/2, changeDifference);
    }
    else {
      fitness -= changeDifferenceBonus(MAX_BOUND/2, changeDifference);
    }
    // Step 2: We divide the fitness value by a penalty based on the number of
    // coins. The higher the number of coins the higher the penalty and the
    // smaller the fitness value.
    // And inversely the smaller number of coins in the solution the higher
    // the resulting fitness value.
    // -----------------------------------------------------------------------
    if (defaultComparation) {
      fitness -= computeCoinNumberPenalty(MAX_BOUND/2, totalCoins);
    }
    else {
      fitness += computeCoinNumberPenalty(MAX_BOUND/2, totalCoins);
    }
    // Make sure fitness value is always positive.
    // -------------------------------------------
    return Math.max(1.0d, fitness);
  }

  protected double changeDifferenceBonus(double a_maxFitness,
                                         int a_changeDifference) {
    if (a_changeDifference == 0) {
      return a_maxFitness;
    }
    else {
      // we arbitrarily work with half of the maximum fitness as basis for non-
      // optimal solutions (concerning change difference)
      if (a_changeDifference * a_changeDifference >= a_maxFitness / 2) {
        return 0.0d;
      }
      else {
        return a_maxFitness / 2 - a_changeDifference * a_changeDifference;
      }
    }
  }

  protected double computeCoinNumberPenalty(double a_maxFitness, int a_coins) {
    if (a_coins == 1) {
      // we know the solution cannot have less than one coin
      return 0;
    }
    else {
      // The more coins the more penalty, but not more than the maximum fitness
      // value possible. Let's avoid linear behavior and use
      // exponential penalty calculation instead
      return (Math.min(a_maxFitness, a_coins * a_coins));
    }
  }

  public static int amountOfChange(IChromosome a_potentialSolution) {
    int numQuarters = getNumberOfCoinsAtGene(a_potentialSolution, 0);
    int numDimes = getNumberOfCoinsAtGene(a_potentialSolution, 1);
    int numNickels = getNumberOfCoinsAtGene(a_potentialSolution, 2);
    int numPennies = getNumberOfCoinsAtGene(a_potentialSolution, 3);
    return (numQuarters * 25) + (numDimes * 10) + (numNickels * 5) +
        numPennies;
  }

  public static int getNumberOfCoinsAtGene(IChromosome a_potentialSolution,
                                           int a_position) {
    Integer numCoins =
        (Integer) a_potentialSolution.getGene(a_position).getAllele();
    return numCoins.intValue();
  }

  /**
   * Returns the total number of coins represented by all of the genes in
   * the given potential solution.
   *
   * @param a_potentialsolution the potential solution to evaluate
   * @return total number of coins represented by the given Chromosome
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public static int getTotalNumberOfCoins(IChromosome a_potentialsolution) {
    int totalCoins = 0;
    int numberOfGenes = a_potentialsolution.size();
    for (int i = 0; i < numberOfGenes; i++) {
      totalCoins += getNumberOfCoinsAtGene(a_potentialsolution, i);
    }
    return totalCoins;
  }
}
