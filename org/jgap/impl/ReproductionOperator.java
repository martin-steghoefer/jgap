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
package org.jgap.impl;

import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.GeneticOperator;

import java.util.List;


/**
 * The reproduction operator loops through each of the Chromosomes in the
 * population and adds them to the list of candidate chromosomes. This
 * guarantees that each Chromosome in the current population remains a
 * candidate for selection for the next population. Note that if a
 * reproduction operator is not included in the list of configured genetic
 * operators, then Chromosomes in the genotype population may not become
 * candidates for natural selection.
 */
public class ReproductionOperator implements GeneticOperator
{
    /**
     * The operate method will be invoked on each of the genetic operators
     * referenced by the current Configuration object during the evolution
     * phase. Operators are given an opportunity to run in the order that
     * they are added to the Configuration. Implementations of this method
     * may reference the population of Chromosomes as it was at the beginning
     * of the evolutionary phase and/or they may instead reference the
     * candidate Chromosomes, which are the results of prior genetic operators.
     * In either case, only Chromosomes added to the list of candidate
     * chromosomes will be considered for natural selection. Implementations
     * should never modify the original population, but should first make copies
     * of the Chromosomes selected for modification and operate upon the copies.
     *
     * @param a_activeConfiguration The current active genetic configuration.
     * @param a_population The population of chromosomes from the current
     *                     evolution prior to exposure to any genetic operators.
     *                     Chromosomes in this array should never be modified.
     * @param a_candidateChromosomes The pool of chromosomes that are candidates
     *                               for the next evolved population. Only these
     *                               chromosomes will go to the natural
     *                               phase, so it's important to add any
     *                               modified copies of Chromosomes to this
     *                               list if it's desired for them to be
     *                               considered for natural selection.
     */
    public void operate( final Configuration a_activeConfiguration,
                         final Chromosome[] a_population,
                         final List a_candidateChromosomes )
    {
        // Loop over the chromosomes in the population and add each one to
        // the candidate chromosomes pool so that they'll be considered for
        // the next phase of evolution. This is technically a no-no, but
        // refraining from making copies here of every Chromosome will save
        // significant time and memory.
        // ----------------------------------------------------------------
        for ( int i = 0; i < a_population.length; i++ )
        {
            a_candidateChromosomes.add( a_population[ i ] );
        }
    }
}

