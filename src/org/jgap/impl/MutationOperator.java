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

import java.util.List;

import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.Gene;
import org.jgap.GeneticOperator;
import org.jgap.MutationRateCalculator;
import org.jgap.RandomGenerator;


/**
 * The mutation operator runs through the genes in each of the Chromosomes
 * in the population and mutates them in statistical accordance to the
 * given mutation rate. Mutated Chromosomes are then added to the list of
 * candidate Chromosomes destined for the natural selection process.
 * <p>
 * This MutationOperator supports both fixed and dynamic mutation rates.
 * A fixed rate is one specified at construction time by the user. A dynamic
 * rate is one determined by this class if no fixed rate is provided, and is
 * calculated based on the size of the Chromosomes in the population such
 * that, on average, one gene will be mutated for every ten Chromosomes
 * processed by this operator.
 *
 * @author Neil Rotstan
 * @since 1.0
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
     * Calculator for dynamically determining the mutation rate. If set to
     * null the value of m_mutationRate will be used.
     * Replaces the previously used boolean m_dynamicMutationRate
     * @since 1.1
     */
    private MutationRateCalculator m_mutationRateCalc;


    /**
     * Constructs a new instance of this MutationOperator without a specified
     * mutation rate, which results in dynamic mutation being turned on. This
     * means that the mutation rate will be automatically determined by this
     * operator based upon the number of genes present in the chromosomes.
     */
    public MutationOperator()
    {
        setMutationRateCalc(new DefaultMutationRateCalculator());
    }

    /**
     * Constructs a new instance of this MutationOperator with a specified
     * mutation rate calculator, which results in dynamic mutation being turned
     * on.
     * @param a_mutationRateCalculator calculator for dynamic mutation rate
     *        computation
     */
    public MutationOperator(MutationRateCalculator a_mutationRateCalculator)
    {
        setMutationRateCalc(a_mutationRateCalculator);
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
        setMutationRateCalc(null);
    }


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
                         List a_candidateChromosomes )
    {
        // If the mutation rate is set to zero and dynamic mutation rate is
        // disabled, then we don't perform any mutation.
        // ----------------------------------------------------------------
        if ( m_mutationRate == 0 && m_mutationRateCalc == null)
        {
            return;
        }

        // Determine the mutation rate. If dynamic rate is enabled, then
        // calculate it based upon the number of genes in the chromosome.
        // Otherwise, go with the mutation rate set upon construction.
        // --------------------------------------------------------------
        int currentRate = m_mutationRateCalc != null ?
            m_mutationRateCalc.calculateCurrentRate(a_activeConfiguration):
                              m_mutationRate;

        RandomGenerator generator = a_activeConfiguration.getRandomGenerator();

        // It would be inefficient to create copies of each Chromosome just
        // to decide whether to mutate them. Instead, we only make a copy
        // once we've positively decided to perform a mutation.
        // ----------------------------------------------------------------
        for ( int i = 0; i < a_population.length; i++ )
        {
            Gene[] genes = a_population[ i ].getGenes();
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
                    // to a random value as the implementation of our
                    // "mutation" of the gene.
                    // ---------------------------------------------------
                    if ( copyOfChromosome == null )
                    {
                        copyOfChromosome =
                            (Chromosome) a_population[ i ].clone();

                        a_candidateChromosomes.add( copyOfChromosome );
                        genes = copyOfChromosome.getGenes();
                    }
                    /**@todo modify this to obtain the possibility of using
                     * Gaussian distribution for mutation (see req. 708772)
                     *
                     * Note: we really don't want a random generator here in
                     * any way. Sometimes yes, but sometimes we would want to
                     * use a (more or less) deterministic algorithm
                     */
                    genes[ j ].setToRandomValue( generator );
                }
            }
        }
    }

    public MutationRateCalculator getMutationRateCalc()
    {
        return m_mutationRateCalc;
    }
    public void setMutationRateCalc(MutationRateCalculator m_mutationRateCalc)
    {
        this.m_mutationRateCalc = m_mutationRateCalc;
        if (m_mutationRateCalc != null) {
            m_mutationRate = 0;
        }
    }
}

