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

import org.jgap.FitnessFunction;
import org.jgap.Chromosome;


/**
 * Sample fitness function for the MakeChange example.
 */
class MakeChangeFitnessFunction extends FitnessFunction
{
    private final int m_targetAmount;

    public MakeChangeFitnessFunction( int a_targetAmount )
    {
        m_targetAmount = a_targetAmount;
    }

    /**
     * Determine the fitness of the given Chromosome instance. The higher the
     * return value, the more fit the instance. This method should always
     * return the same fitness value for two equivalent Chromosome instances.
     *
     * @param a_subject: The Chromosome instance to evaluate.
     *
     * @return The fitness rating of the given Chromosome.
     */
    public int evaluate( Chromosome a_subject )
    {
        int changeAmount = MakeChange.calculateChangeAmount( a_subject );
        int totalCoins = MakeChange.calculateNumberOfCoins( a_subject );
        int changeDifference = Math.abs( m_targetAmount - changeAmount );

        int fitness = (119 - changeDifference) - totalCoins;

        // Bias towards the correct change amount by penalizing an additional
        // 10 fitness points if the amount is incorrect.
        // ------------------------------------------------------------------
        if( changeAmount != m_targetAmount )
        {
            fitness -= 10;
        }

        // Make sure we return a fitness value >= 1.
        // -----------------------------------------
        return Math.max( 1, fitness );
    }
}
