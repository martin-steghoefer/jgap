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


public class MaxFunction extends FitnessFunction
{
    /**
     * This example implementation calculates the fitness value of Chromosomes
     * using BooleanGene implementations. It simply returns a fitness value
     * equal to the numeric binary value of the bits. In other words, it
     * optimizes the numeric value of the genes interpreted as bits. It should
     * be noted that, for clarity, this function literally returns the binary
     * value of the Chromosome's genes interpreted as bits. However, it would
     * be better to return the value raised to a fixed power to exaggerate the
     * difference between the higher values. For example, the difference
     * between 254 and 255 is only about .04%, which isn't much incentive for
     * the selector to choose 255 over 254. However, if you square the values,
     * you then get 64516 and 65025, which is a difference of 0.8%--twice
     * as much and, therefore, twice the incentive to select the higher
     * value.
     */
    public int evaluate( Chromosome a_subject )
    {
        int total = 0;

        for ( int i = 0; i < a_subject.size(); i++ )
        {
            Gene value = a_subject.getGene( a_subject.size() - ( i + 1 ) );
            if ( ((Boolean) value.getAllele()).booleanValue() )
            {
                total += Math.pow( 2.0, (double) i );
            }
        }

        return total;
    }
}
