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

import java.util.List;


/**
 * A GeneticOperator represents an operation that takes place on
 * a population of Chromosomes during the evolution process. Examples
 * of genetic operators include reproduction, crossover, and mutation.
 */
public interface GeneticOperator extends java.io.Serializable
{
    /**
     * The operate method will be invoked on each of the genetic operators
     * referenced by the current Configuration object during the evolution
     * phase. Operators are given an opportunity to run in the order that
     * they are added to the Configuration. Implementations of this method
     * may reference the population of Chromosomes as it was at the beginning
     * of the evolutionary phase or the candidate Chromosomes, which are the
     * results of prior genetic operators. In either case, only Chromosomes
     * added to the list of candidate chromosomes will be considered for
     * natural selection. Implementations should never modify the original
     * population.
     *
     * @param a_activeConfiguration The current active genetic configuration.
     * @param a_population The population of chromosomes from the current
     *                     evolution prior to exposure to any genetic operators.
     *                     Chromosomes in this array should never be modified.
     * @param a_candidateChromosomes The pool of chromosomes that are candidates
     *                               for the next evolved population. Any
     *                               chromosomes that are modified by this
     *                               genetic operator that should be considered
     *                               for natural selection should be added to
     *                               the candidate chromosomes.
     */
    public void operate( final Configuration a_activeConfiguration,
                         final Chromosome[] a_population,
                         final List a_candidateChromosomes );
}

