/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */

package org.jgap.supergenes;

import org.jgap.*;
import org.jgap.impl.*;

/**
 * Sample fitness function for the MakeChange example,
 * including supergenes.
 * @author Neil Rotstan
 * @author Klaus Meffert
 * @author Audrius Meskauskas (subsequent adaptation)
 * @since 2.0
 */
abstract class p_abstractChangFitnessFunction extends FitnessFunction {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  private final int m_targetAmount;

  public p_abstractChangFitnessFunction(int a_targetAmount) {
    if (a_targetAmount < 1 || a_targetAmount > 99) {
      throw new IllegalArgumentException(
          "Change amount must be between 1 and 99 cents.");
    }
    m_targetAmount = a_targetAmount;
  }

  /**
   * Determine the fitness of the given Chromosome instance. The higher the
   * return value, the more fit the instance. This method should always
   * return the same fitness value for two equivalent Chromosome instances.
   *
   * @param a_subject: The Chromosome instance to evaluate.
   *
   * @return A positive integer reflecting the fitness rating of the given
   *         Chromosome.
   * @since 2.0 (until 1.1: return type int)
   */
  public double evaluate(Chromosome a_subject) {
    // The fitness value measures both how close the value is to the
    // target amount supplied by the user and the total number of coins
    // represented by the solution. We do this in two steps: first,
    // we consider only the represented amount of change vs. the target
    // amount of change and return higher fitness values for amounts
    // closer to the target, and lower fitness values for amounts further
    // away from the target. If the amount equals the target, then we go
    // to step 2, which returns a higher fitness value for solutions
    // representing fewer total coins, and lower fitness values for
    // solutions representing more total coins.
    // ------------------------------------------------------------------
    int changeAmount = amountOfChange(a_subject);
    int totalCoins = getTotalNumberOfCoins(a_subject);
    int changeDifference = Math.abs(m_targetAmount - changeAmount);
    // Step 1: Determine distance of amount represented by solution from
    // the target amount. Since we know  the maximum amount of change is
    // 99 cents, we'll subtract the difference in change between the
    // solution amount and the target amount from 99. That will give
    // the desired effect of returning higher values for amounts
    // closer to the target amount and lower values for amounts
    // further away from the target amount.
    // -----------------------------------------------------------------
    int fitness = (99 - changeDifference);
    // Step 2: If the solution amount equals the target amount, then
    // we add additional fitness points for solutions representing fewer
    // total coins.
    // -----------------------------------------------------------------
    if (changeAmount == m_targetAmount) {
      // was fitness += 100 - (10 * totalCoins);
      // The function should be more tolearant to the large amount of coins:
      // -------------------------------------------------------------------
      fitness += 100 - totalCoins;
    }
    // Make sure fitness value is always positive.
    // -------------------------------------------
    return Math.max(1, fitness);
  }

  /**
   * Calculates the total amount of change (in cents) represented by
   * the given potential solution and returns that amount.
   *
   * @param a_potentialSolution The pontential solution to evaluate.
   * @return The total amount of change (in cents) represented by the
   *         given solution.
   */
  public int amountOfChange(Chromosome a_potentialSolution) {
    int numQuarters = getNumberOfCoinsAtGene(a_potentialSolution,
     p_SupergeneTest.QUARTERS);
    int numDimes = getNumberOfCoinsAtGene(a_potentialSolution,
     p_SupergeneTest.DIMES);
    int numNickels = getNumberOfCoinsAtGene(a_potentialSolution,
     p_SupergeneTest.NICKELS);
    int numPennies = getNumberOfCoinsAtGene(a_potentialSolution,
     p_SupergeneTest.PENNIES);

    return p_abstractSupergeneTest.amountOfChange
     (numQuarters, numDimes, numNickels, numPennies);
  }

  /**
   * Retrieves the number of coins represented by the given potential
   * solution at the given gene position.
   *
   * @param a_potentialSolution The potential solution to evaluate.
   * @param a_position The gene position to evaluate.
   * @return the number of coins represented by the potential solution
   *         at the given gene position.
   */
  public int getNumberOfCoinsAtGene(Chromosome a_potentialSolution,
   int a_code) {
     Gene g = getResponsibleGene(a_potentialSolution, a_code);
     return ((IntegerGene) g).intValue();
  }

  /**
   * Returns the total number of coins represented by all of the genes in
   * the given potential solution.
   *
   * @param a_potentialsolution The potential solution to evaluate.
   * @return The total number of coins represented by the given Chromosome.
   */
  public int getTotalNumberOfCoins(Chromosome a_c) {
      return
       getNumberOfCoinsAtGene(a_c, p_SupergeneTest.QUARTERS)+
       getNumberOfCoinsAtGene(a_c, p_SupergeneTest.DIMES)+
       getNumberOfCoinsAtGene(a_c, p_SupergeneTest.NICKELS)+
       getNumberOfCoinsAtGene(a_c, p_SupergeneTest.PENNIES);
  }

  /** Get the gene, responsible for the number of coins, corresponding
   * this code */
  public abstract Gene getResponsibleGene(Chromosome a_chromosome, int a_code);

}
