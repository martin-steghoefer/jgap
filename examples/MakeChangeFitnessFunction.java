/*
 * Copyright 2001, 2002 Neil Rotstan
 *
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
package examples;

import org.jgap.Chromosome;
import org.jgap.FitnessFunction;

/**
 * The MakeChangeFitnessFunction evaluates Chromosomes with
 * four genes which represent the four denominations of
 * American coins: quarters, dimes, nickels, and pennies.
 * Each gene is represented by an IntegerAllele, and the
 * value of that allele is the number of coins for the coin
 * type assigned to that gene. The fitness function adds up
 * the total value of the coinage and returns a fitness
 * value that measures how close the value of the coins is
 * to a target amount supplied by the user.
 */
public class MakeChangeFitnessFunction extends FitnessFunction
{
    private final int m_targetAmount;

    /**
     * Constructs this MakeChangeFitnessFunction with the desired amount
     * of change to make.
     *
     * @param a_targetAmount The desired amount of change, in cents. This
     *                       value must be between 1 and 99 cents.
     */
    public MakeChangeFitnessFunction( int a_targetAmount )
    {
        if( a_targetAmount < 1 || a_targetAmount > 99 )
        {
            throw new IllegalArgumentException(
                "Change amount must be between 1 and 99 cents." );
        }

        m_targetAmount = a_targetAmount;
    }

    /**
     * This is the method we must define for our fitness function. It
     * should evaluate the presented potential solution and return a
     * measurement of its fitness as an integer value. The higher the value,
     * the better the solution. The lower the value, the worse the solution.
     *
     * @param a_potentialSolution The potential solution to evaluate.
     * @return the fitness measurement of the potential solution.
     */
    public int evaluate( Chromosome a_potentialSolution )
    {
        int amount = amountOfChange( a_potentialSolution );

        // The fitness value measures how close the value is to the
        // target amount supplied by the user. For closer amounts, we
        // want to return a higher fitness value. For amounts further
        // away, we want to return a lower fitness value. Since we know
        // the maximum amount of change is 99 cents, we'll take the
        // absolute value of the difference between the target amount and
        // our change amount, and subtract that from 99. That will give
        // the desired effect of returning higher values for amounts
        // closer to the target amount and lower values for amounts
        // further away from the target amount.
        // --------------------------------------------------------------
        return 99 - Math.abs( m_targetAmount - amount );
    }


    /**
     * Calculates the total amount of change (in cents) represented by
     * the given potential solution and returns that amount.
     *
     * @param a_potentialSolution The pontential solution to evaluate.
     * @return The total amount of change (in cents) represented by the
     *         given solution.
     */
    public static int amountOfChange( Chromosome a_potentialSolution )
    {
        int numQuarters = getNumberOfCoins( a_potentialSolution, 0 );
        int numDimes = getNumberOfCoins( a_potentialSolution, 1 );
        int numNickels = getNumberOfCoins( a_potentialSolution, 2 );
        int numPennies = getNumberOfCoins( a_potentialSolution, 3 );

        return ( numQuarters * 25 ) + ( numDimes * 10 ) + ( numNickels * 5 ) +
               numPennies;
    }


    /**
     * Retrieves the number of coins represented by the given pontential
     * solution at the given gene position.
     *
     * @param a_potentialSolution The potential solution to evaluate.
     * @param a_position The gene position to evaluate.
     * @return the number of coins represented by the potential solution
     *         at the given gene position.
     */
    public static int getNumberOfCoins( Chromosome a_potentialSolution,
                                        int a_position )
    {
        Integer numCoins =
          (Integer) a_potentialSolution.getAllele( a_position ).getValue();

        return numCoins.intValue();
    }
}
