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

package org.jgap;


/**
 * Natural selectors are responsible for actually selecting a specified number
 * of Chromosome specimens from a population, using the fitness values as a
 * guide. Usually fitness is treated as a statistic probability of survival,
 * not as the sole determining factor. Therefore, Chromosomes with higher
 * fitness values are more likely to survive than those with lesser fitness
 * values, but it's not guaranteed.
 */
public interface NaturalSelector extends java.io.Serializable
{
    /**
     * Add a Chromosome instance and corresponding fitness value to this
     * selector's working pool of Chromosomes.
     *
     * @param a_activeConfigurator: The current active Configuration to be used
     *                              during the add process.
     * @param a_chromosomeToAdd: The specimen to add to the pool.
     * @param a_chromosomeFitnessValue: The specimen's fitness value.
     */
    public void add( Configuration a_activeConfigurator,
                     Chromosome a_chromosomeToAdd,
                     int a_chromosomeFitnessValue );

    /**
     * Select a given number of Chromosomes from the pool that will continue
     * to survive. This selection should be guided by the fitness values, but
     * fitness should be treated as a statistical probability of survival,
     * not as the sole determining factor. In other words, Chromosomes with
     * higher fitness values are more likely to be selected than those with
     * lower fitness values, but it's not guaranteed.
     *
     * @param a_activeConfiguration: The current active Configuration that is
     *                               to be used during the selection process.
     * @param a_howManyToSelect: The number of Chromosomes to select.
     *
     * @return An array of the selected Chromosomes.
     */
    public Chromosome[] select( Configuration a_activeConfiguration,
                                int a_howManyToSelect );


    /**
     * Empty out the working pool of Chromosomes.
     */
    public void empty();
}

