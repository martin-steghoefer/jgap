/*
 * Copyright 2003 Klaus Meffert
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
package org.jgap;

/**
 * A default implementation of a fitness evaluator. This implementation is
 * straight forward
 *
 * @author Klaus Meffert
 * @since 1.1
 */

public class DefaultFitnessEvaluator implements FitnessEvaluator
{

    /** String containing the CVS revision. Read out via reflection!*/
    private final static String CVS_REVISION = "$Revision: 1.3 $";

    public DefaultFitnessEvaluator()
    {
    }

    /**
     * Compares the first given fitness value with the second and returns true
     * if the first one is greater than the second one. Otherwise returns false
     * @param a_fitness_value1 first fitness value
     * @param a_fitness_value2 second fitness value
     * @return true: first fitness value greater than second
     *
     * @since 1.1
     * @since 2.0 (until 1.1: input types int)
     */
    public boolean isFitter(double a_fitness_value1, double a_fitness_value2) {
        return a_fitness_value1 > a_fitness_value2;
    }

}
