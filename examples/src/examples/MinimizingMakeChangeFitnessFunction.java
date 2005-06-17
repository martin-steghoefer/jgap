/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples;

import org.jgap.*;

/**
 * Sample fitness function for the MakeChange example.
 *
 * @author Neil Rotstan
 * @author Klaus Meffert
 * @since 1.0
 */
public class MinimizingMakeChangeFitnessFunction
    extends FitnessFunction {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.11 $";

  private final int m_targetAmount;

  public static final int MAX_BOUND = 1000;

  private static final double ZERO_DIFFERENCE_FITNESS = 10;

  public MinimizingMakeChangeFitnessFunction(int a_targetAmount) {
    if (a_targetAmount < 1 || a_targetAmount >= MAX_BOUND) {
      throw new IllegalArgumentException(
          "Change amount must be between 1 and " + MAX_BOUND + " cents.");
    }
    m_targetAmount = a_targetAmount;
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
   */
  public double evaluate(Chromosome a_subject) {
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
    double fitness;
    if (changeDifference == 0) {
      fitness = ZERO_DIFFERENCE_FITNESS;
    }
    else {
      fitness = 1.0 / changeDifference;
    }
    // Step 2: We divide the fitness value by a penalty based on the number of
    // coins. The higher the number of coins the higher the penalty and the
    // smaller the fitness value.
    // And inversely the smaller number of coins in the solution the higher
    // the resulting fitness value.
    // -----------------------------------------------------------------------
    fitness /= computeCoinNumberPenalty(totalCoins);
    // Make sure fitness value is always positive.
    // -------------------------------------------
    return Math.max(0.00001d, fitness);
  }

  /**
   * Calculates the penalty to apply to the fitness value based on the ammount
   * of coins in the solution
   *
   * @param a_coins number of coins in the solution
   * @return penalty for the fitness value base on the number of coins
   *
   * @author John Serri
   * @since 2.2
   */
  protected double computeCoinNumberPenalty(int a_coins) {
    if (a_coins == 0) {
      return MAX_BOUND * MAX_BOUND;
    }
    else {
      return (a_coins * a_coins);
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
  public static int amountOfChange(Chromosome a_potentialSolution) {
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
  public static int getNumberOfCoinsAtGene(Chromosome a_potentialSolution,
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
  public static int getTotalNumberOfCoins(Chromosome a_potentialsolution) {
    int totalCoins = 0;
    int numberOfGenes = a_potentialsolution.size();
    for (int i = 0; i < numberOfGenes; i++) {
      totalCoins += getNumberOfCoinsAtGene(a_potentialsolution, i);
    }
    return totalCoins;
  }
}
