/*
 * Copyright 2001-2003 Neil Rotstan
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

import org.jgap.Gene;
import org.jgap.Chromosome;
import org.jgap.FitnessFunction;


/**
 * This example implementation calculates the fitness value of Chromosomes
 * using BooleanGene implementations. It  treats the booleans as bits,
 * evaluates those bits taken together as an integer value, and then returns
 * that integer value as the fitness value. Even better would be if we
 * first raised the value to a fixed power to exaggerate the difference between
 * the higher values. For example, the difference  between 254 and 255 is only
 * about .04%, which isn't much incentive for the natural selector to choose
 * 255 over 254. However, if we squared the values, we would then get 64516 and
 * 65025, which is a difference of 0.8%--twice as much and, therefore, twice
 * the incentive to select the higher value. But we don't do that here.
 */
public class MaxFunction extends FitnessFunction
{
    /**
     * Determine the fitness of the given Chromosome instance. The higher the
     * return value, the more fit the instance. This method should always
     * return the same fitness value for two equivalent Chromosome instances.
     *
     * @param a_subject: The Chromosome instance to evaluate.
     *
     * @return A positive integer reflecting the fitness rating of the given
     *         Chromosome.
     */
    public int evaluate( Chromosome a_subject )
    {
        int fitnessValue = 0;
        Gene[] genes = a_subject.getGenes();

        for ( int i = genes.length - 1; i >= 0; i-- )
        {
            Gene value = genes[ i ];
            if ( ( (Boolean) value.getAllele() ).booleanValue() == true )
            {
                // A true value equals a bit value of 1.
                // -------------------------------------
                fitnessValue += Math.pow( 2.0, (double) i );
            }
        }

        return fitnessValue;
    }
}
