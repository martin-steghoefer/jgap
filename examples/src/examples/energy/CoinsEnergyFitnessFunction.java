/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.energy;

import org.jgap.*;

/**
 * Sample fitness function for the CoinsEnergy example. Adapted from
 * examples.MinimizingMakeChangeFitnessFunction
 *
 * @author Klaus Meffert
 * @since 2.4
 */
public class CoinsEnergyFitnessFunction
    extends FitnessFunction {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.5 $";

  private final int m_targetAmount;

  private final double m_maxWeight;

  public static final int MAX_BOUND = 10000;
  public static final double MAX_WEIGHT = 500;

  private static final double ZERO_DIFFERENCE_FITNESS = Math.sqrt(MAX_BOUND);

  public CoinsEnergyFitnessFunction(int a_targetAmount, double a_maxWeight) {
    if (a_targetAmount < 1 || a_targetAmount >= MAX_BOUND) {
      throw new IllegalArgumentException(
          "Change amount must be between 1 and " + MAX_BOUND + " cents.");
    }

    if (a_maxWeight < 0 || a_maxWeight >= MAX_WEIGHT) {
      throw new IllegalArgumentException(
          "Max weight must be greater than 0 and not greater than "
          + MAX_WEIGHT + " grammes");
    }
    m_targetAmount = a_targetAmount;
    m_maxWeight = a_maxWeight;
  }

  /**
   * Determine the fitness of the given Chromosome instance. The higher the
   * return value, the more fit the instance. This method should always
   * return the same fitness value for two equivalent Chromosome instances.
   *
   * @param a_subject the Chromosome instance to evaluate
   *
   * @return positive double reflecting the fitness rating of the given
   * Chromosome
   * @since 2.0 (until 1.1: return type int)
   * @author Neil Rotstan, Klaus Meffert, John Serri
   */
  public double evaluate(IChromosome a_subject) {
    // The fitness value measures both how close the value is to the
    // target amount supplied by the user and the total number of coins
    // represented by the solution. We do this in two steps: first,
    // we consider only the represented amount of change vs. the target
    // amount of change and return higher fitness values for amounts
    // closer to the target, and lower fitness values for amounts further
    // away from the target. Then we go to step 2, which returns a higher
    // fitness value for solutions representing fewer total coins, and
    // lower fitness values for solutions representing more total coins.
    // ------------------------------------------------------------------
    int changeAmount = amountOfChange(a_subject);
    int totalCoins = getTotalNumberOfCoins(a_subject);
    int changeDifference = Math.abs(m_targetAmount - changeAmount);

    double fitness;

    // Step 1: Determine total sum of energies (interpreted as weights here)
    // of coins. If higher than the given maximum value, the solution is not
    // accepted in any way, i.e. the fitness value is then set to the worst
    // value.
    double totalWeight = getTotalWeight(a_subject);
    if (totalWeight > m_maxWeight) {
      if (a_subject.getConfiguration().getFitnessEvaluator().isFitter(2, 1)) {
        return 1.0d;
      }
      else {
        return MAX_BOUND;
      }
    }

    if (a_subject.getConfiguration().getFitnessEvaluator().isFitter(2, 1)) {
      fitness = MAX_BOUND;
    }
    else {
      fitness = 0.0d;
    }

    // Step 2: Determine distance of amount represented by solution from
    // the target amount.
    // -----------------------------------------------------------------
    if (a_subject.getConfiguration().getFitnessEvaluator().isFitter(2, 1)) {
      fitness -= changeDifferenceBonus(MAX_BOUND/3, changeDifference);
    }
    else {
      fitness += changeDifferenceBonus(MAX_BOUND/3, changeDifference);
    }

    // Step 3: We divide the fitness value by a penalty based on the number of
    // coins. The higher the number of coins the higher the penalty and the
    // smaller the fitness value.
    // And inversely the smaller number of coins in the solution the higher
    // the resulting fitness value.
    // -----------------------------------------------------------------------
    if (a_subject.getConfiguration().getFitnessEvaluator().isFitter(2, 1)) {
      fitness -= computeCoinNumberPenalty(MAX_BOUND/3, totalCoins);
    }
    else {
      fitness += computeCoinNumberPenalty(MAX_BOUND/3, totalCoins);
    }

    // Step 4: Penalize higher weight (= engery) values.
    // -------------------------------------------------
    if (a_subject.getConfiguration().getFitnessEvaluator().isFitter(2, 1)) {
      fitness -= computeWeightPenalty(MAX_BOUND/3, totalWeight);
    }
    else {
      fitness += computeWeightPenalty(MAX_BOUND/3, totalWeight);
    }

    // Make sure fitness value is always positive.
    // -------------------------------------------
    return Math.max(1.0d, fitness);
  }

  /**
   * Bonus calculation of fitness value.
   * @param a_maxFitness maximum fitness value appliable
   * @param a_changeDifference change difference in coins for the coins problem
   * @return bonus for given change difference
   * @author Klaus Meffert
   * @since 2.3
   */
  protected double changeDifferenceBonus(double a_maxFitness,
                                         int a_changeDifference) {
    if (a_changeDifference == 0) {
      return a_maxFitness;
    }
    else {
      // we arbitrarily work with half of the maximum fitness as basis for
      // non-optimal solutions (concerning change difference)
      return Math.min(a_maxFitness, Math.pow(a_changeDifference, 2.2d));
    }
  }

  /**
   * Calculates the penalty to apply to the fitness value based on the ammount
   * of coins in the solution
   *
   * @param a_maxFitness maximum fitness value allowed
   * @param a_coins number of coins in the solution
   * @return penalty for the fitness value base on the number of coins
   *
   * @author John Serri
   * @since 2.4
   */
  protected double computeCoinNumberPenalty(double a_maxFitness, int a_coins) {
    if (a_coins == 1) {
      // we know the solution cannot have less than one coin
      return 0;
    }
    else {
      if (a_coins < 1) {
        return a_maxFitness;
      }
      // The more coins the more penalty, but not more than the maximum
      // fitness value possible. Let's avoid linear behavior and use
      // exponential penalty calculation instead
      return (Math.min(a_maxFitness, Math.pow(a_coins, 1.3d)));
    }
  }

  /**
   * Calculates the total amount of change (in cents) represented by
   * the given potential solution and returns that amount.
   *
   * @param a_potentialSolution the potential solution to evaluate
   * @return the total amount of change (in cents) represented by the
   * given solution
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public static int amountOfChange(IChromosome a_potentialSolution) {
    int numQuarters = getNumberOfCoinsAtGene(a_potentialSolution, 0);
    int numDimes = getNumberOfCoinsAtGene(a_potentialSolution, 1);
    int numNickels = getNumberOfCoinsAtGene(a_potentialSolution, 2);
    int numPennies = getNumberOfCoinsAtGene(a_potentialSolution, 3);
    return (numQuarters * 25) + (numDimes * 10) + (numNickels * 5) +
        numPennies;
  }

  /**
   * Retrieves the number of coins represented by the given potential
   * solution at the given gene position.
   *
   * @param a_potentialSolution the potential solution to evaluate
   * @param a_position the gene position to evaluate
   * @return the number of coins represented by the potential solution at the
   * given gene position
   *
   * @author Neil Rotstan
   * @since 1.0
   */
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
   * @since 2.4
   */
  public static int getTotalNumberOfCoins(IChromosome a_potentialsolution) {
    int totalCoins = 0;
    int numberOfGenes = a_potentialsolution.size();
    for (int i = 0; i < numberOfGenes; i++) {
      totalCoins += getNumberOfCoinsAtGene(a_potentialsolution, i);
    }
    return totalCoins;
  }

  /**
   * Returns the total weight of all coins.
   *
   * @param a_potentialSolution the potential solution to evaluate
   * @return total weight of all coins
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public static double getTotalWeight(IChromosome a_potentialSolution) {
    double totalWeight = 0.0d;
    int numberOfGenes = a_potentialSolution.size();
    for (int i = 0; i < numberOfGenes; i++) {
      int coinsNumber = getNumberOfCoinsAtGene(a_potentialSolution,i);
      totalWeight += a_potentialSolution.getGene(i).getEnergy() * coinsNumber;
    }
    return totalWeight;
  }

  /**
   *
   * @param a_maxFitness the maximum fitness value allowed
   * @param a_weight the coins weight of the current solution
   * @return the penalty computed
   * @author Klaus Meffert
   * @since 2.4
   */
  protected double computeWeightPenalty(double a_maxFitness, double a_weight) {
    if (a_weight <= 0) {
      // we know the solution cannot have less than one coin
      return 0;
    }
    else {
      // The more weight the more penalty, but not more than the maximum
      // fitness value possible. Let's avoid linear behavior and use
      // exponential penalty calculation instead
      return (Math.min(a_maxFitness, a_weight * a_weight));
    }
  }

}
