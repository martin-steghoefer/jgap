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

import org.jgap.Allele;
import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.GeneticOperator;
import org.jgap.RandomGenerator;

import java.util.List;


/**
 * The mutation operator runs through the genes in each of the Chromosome
 * in the population and mutates them in statistical accordance to the given
 * given mutation rate. Mutated Chromosomes are added to the list of candidate
 * Chromosomes.
 */
public class MutationOperator implements GeneticOperator
{
    /**
     * The current mutation rate used by this MutationOperator, expressed as
     * the denominator in the 1 / X ratio. For example, a value of 1000 would
     * mean that, on average, 1 / 1000 genes would be mutated. A value of zero
     * disabled mutation entirely.
     */
    protected int m_mutationRate;

    /**
     * Indicates whether dynamic mutation rate determination is enabled or
     * not. If enabled, then the mutation rate will be determined automatically
     * based upon the number of genes present in the chromosomes. If disabled,
     * then the value of m_mutationRate will be used.
     */
    protected boolean m_dynamicMutationRate;


    /**
     * Constructs a new instance of this MutationOperator without a specified
     * mutation rate, which results in dynamic mutation being turned on. This
     * means that the mutation rate will be automatically determined by this
     * operator based upon the number of genes present in the chromosomes.
     */
    public MutationOperator()
    {
        m_mutationRate = 0;
        m_dynamicMutationRate = true;
    }


    /**
     * Constructs a new instance of this MutationOperator with the given
     * mutation rate.
     *
     * @param a_desiredMutationRate The desired rate of mutation, expressed
     *                              as the denominator of the 1 / X fraction.
     *                              For example, 1000 would result in 1/1000
     *                              genes being mutated on average. A mutation
     *                              rate of zero disables mutation entirely.
     */
    public MutationOperator( int a_desiredMutationRate )
    {
        m_mutationRate = a_desiredMutationRate;
        m_dynamicMutationRate = false;
    }


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
                         List a_candidateChromosomes )
    {
        // If the mutation rate is set to zero and dynamic mutation rate is
        // disabled, then we don't perform any mutation.
        // ----------------------------------------------------------------
        if ( m_mutationRate == 0 && !m_dynamicMutationRate )
        {
            return;
        }

        // Determine the mutation rate. If dynamic rate is enabled, then
        // calculate it based upon the number of genes in the chromosome.
        // Otherwise, go with the mutation rate set upon construction.
        // --------------------------------------------------------------
        int currentRate = m_dynamicMutationRate ?
                              a_activeConfiguration.getChromosomeSize() * 10 :
                              m_mutationRate;

        RandomGenerator generator = a_activeConfiguration.getRandomGenerator();

        // It would be inefficient to create copies of each Chromosome just
        // to decide whether to mutate them. Instead, we only make a copy
        // once we've positively decided to perform a mutation.
        // ----------------------------------------------------------------
        for ( int i = 0; i < a_population.length; i++ )
        {
            Allele[] genes = a_population[ i ].getGenes();
            Chromosome copyOfChromosome = null;

            for ( int j = 0; j < genes.length; j++ )
            {
                if ( generator.nextInt( currentRate ) == 0 )
                {
                    // Now that we want to actually modify the Chromosome,
                    // let's make a copy of it (if we haven't already) and
                    // add it to the candidate chromosomes so that it will
                    // be considered for natural selection during the next
                    // phase of evolution. Then we'll set the gene's value
                    // to a random value.
                    // ---------------------------------------------------
                    if ( copyOfChromosome == null )
                    {
                        copyOfChromosome =
                            (Chromosome) a_population[ i ].clone();
                        a_candidateChromosomes.add( copyOfChromosome );
                        genes = copyOfChromosome.getGenes();
                    }

                    genes[ j ].setToRandomValue(
                        a_activeConfiguration.getRandomGenerator() );
                }
            }
        }
    }
}

