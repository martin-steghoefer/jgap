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
package org.jgap;


/**
 * Bulk fitness functions are used to determine how optimal a group of
 * solutions are relative to each other. Bulk fitness functions can be
 * useful (vs. normal fitness functions) when fitness of a particular
 * solution cannot be easily computed in isolation, but instead is
 * dependent upon the fitness of its fellow solutions that are also
 * under consideration. This abstract class should be extended and the
 * evaluateChromosomes() method implemented to evaluate each of the
 * Chromosomes given in an array and set their fitness values prior
 * to returning.
 */
public abstract class BulkFitnessFunction implements java.io.Serializable
{
    /**
     * Calculates and sets the fitness values on each of the given
     * Chromosomes via their setFitness() method.
     *
     * @param a_subjects the Chromosomes for which the fitness values
     *                   must be computed and set.
     */
    public abstract void evaluate( Chromosome[] a_subjects );
}

