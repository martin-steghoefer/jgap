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

import org.jgap.event.GenotypeEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


/**
 * Genotypes represent fixed-length collections or "populations" of
 * Chromosomes. As an instance of a genotype is evolved, all of its
 * Chromosomes are also evolved.
 */
public class Genotype implements java.io.Serializable
{
    protected Configuration m_activeConfiguration;
    protected Chromosome[] m_chromosomes;
    protected List m_workingPool;


    /**
     * Constructs a new Genotype instance with the given array
     * of Chromosomes and the given a_activeConfiguration settings. Note
     * that the Configuration instance must be in a valid state
     * when this method is invoked, or a InvalidconfigurationException
     * will be thrown.
     *
     * @param a_activeConfiguration: An instance of a Configuration object
     *                       that will control the behavior of this
     *                       GA execution.
     * @param a_initialChromosomes: The Chromosome population to be
     *                            managed by this Genotype instance.
     *
     * @throws InvalidConfigurationException if the given Configuration
     *         is null or in an invalid state.
     */
    public Genotype( Configuration a_activeConfiguration,
                     Chromosome[] a_initialChromosomes )
           throws InvalidConfigurationException
    {
        if ( a_activeConfiguration == null )
        {
            throw new InvalidConfigurationException(
                    "The Configuration instance must not be null." );
        }

        a_activeConfiguration.lockSettings();
        m_chromosomes = a_initialChromosomes;
        m_activeConfiguration = a_activeConfiguration;

        m_workingPool = new ArrayList();
    }


    /**
     * Retrieve the array of Chromosomes that make up this Genotype instance.
     *
     * @return The chromosomes that make up this Genotype.
     */
    public synchronized Chromosome[] getChromosomes()
    {
        return m_chromosomes;
    }


    /**
     * Retrieve the Chromosome in the population with the highest fitnes value.
     *
     * @return The Chromosome with the highest fitness value, or null if
     *         there are no chromosomes in this Genotype.
     */
    public synchronized Chromosome getFittestChromosome()
    {
        if ( m_chromosomes.length == 0 )
        {
            return null;
        }

        // Set the highest fitness value to that of the first chromosome.
        // Then loop over the rest of the chromosomes and see if any has
        // a higher fitness value.
        // --------------------------------------------------------------
        Chromosome fittestChromosome = m_chromosomes[ 0 ];
        long fittestValue =
            m_activeConfiguration.getFitnessFunction().getFitnessValue(
                                      fittestChromosome );

        for ( int i = 1; i < m_chromosomes.length; i++ )
        {
            long fitnessValue =
                m_activeConfiguration.getFitnessFunction().getFitnessValue(
                                          m_chromosomes[ i ] );

            if ( fitnessValue > fittestValue )
            {
                fittestChromosome = m_chromosomes[ i ];
                fittestValue = fitnessValue;
            }
        }

        return fittestChromosome;
    }


    /**
     * Evolve the collection of Chromosomes within this Genotype. This will
     * execute all of the genetic operators added to the present active
     * Configuration and then invoke the natural selector to choose which
     * chromosomes will be included in the next population. Note that the
     * population size always remains constant.
     */
    public synchronized void evolve()
    {
        // Reset the auto-exaggeration tracking in the fitness function that
        // is specific to each evolution cycle.
        // -----------------------------------------------------------------
        FitnessFunction activeFitnessFunction =
            m_activeConfiguration.getFitnessFunction();

        activeFitnessFunction.resetValuesForThisEvolutionCycle();

        // Execute all of the Genetic Operators.
        // -------------------------------------
        List geneticOperators = m_activeConfiguration.getGeneticOperators();
        Iterator operatorIterator = geneticOperators.iterator();

        while ( operatorIterator.hasNext() )
        {
            ( (GeneticOperator) operatorIterator.next() ).operate(
                    m_activeConfiguration, m_chromosomes, m_workingPool );
        }

        // If the auto-exaggeration feature is enabled, then we're going
        // to remap the fitness values of the chromosomes to the full
        // spectrum of fitness values that have been computed thus far.
        // By spreading them out along the spectrum, we exaggerate their
        // statistical difference and make it more likely that the
        // natural selector will choose the more optimal solutions from
        // among a group of otherwise very similar solutions.
        //
        // Here we compute the two range of values: the total range,
        // which is the range of fitness values given out during this
        // entire genetic run; and the currentRange, which is the range
        // of fitness values given out just during this evolution cycle.
        // As we add Chromosomes to the natural selector, we'll use these
        // ranges to remap each Chromosome's fitness values.
        // -------------------------------------------------------------
        int totalRange = 1;
        int currentRange = 1;

        if( m_activeConfiguration.isAutoExaggerationEnabled() )
        {
            // For now, run through all of the Chromosomes and compute their
            // fitness functions to preload the tracking values we need.
            // Later, we'll make this more efficient.
            // -------------------------------------------------------------
            Iterator iterator = m_workingPool.iterator();
            while ( iterator.hasNext() )
            {
                Chromosome currentChromosome = (Chromosome) iterator.next();
                activeFitnessFunction.getFitnessValue( currentChromosome );
            }

            totalRange = Math.max( 1,
                activeFitnessFunction.getMostFitValueThisGeneticRun() -
                activeFitnessFunction.getLeastFitValueThisGeneticRun() );

            currentRange = Math.max( 1,
                activeFitnessFunction.getMostFitValueThisEvolutionCycle() -
                activeFitnessFunction.getLeastFitValueThisEvolutionCycle() );
        }

        // Add the chromosomes in the working pool to the natural selector.
        // ----------------------------------------------------------------
        Iterator iterator = m_workingPool.iterator();

        while ( iterator.hasNext() )
        {
            Chromosome currentChromosome = (Chromosome) iterator.next();
            int originalFitness =
                activeFitnessFunction.getFitnessValue( currentChromosome );

            // If auto-exaggeration is enabled, go ahead and remap the
            // fitness value now.
            // -------------------------------------------------------
            int currentFitness = originalFitness;
            if( m_activeConfiguration.isAutoExaggerationEnabled() )
            {
                currentFitness = (int)
                    (( (long) currentFitness * (long) totalRange) /
                     currentRange );
            }

            m_activeConfiguration.getNaturalSelector().add(
                    m_activeConfiguration,
                    currentChromosome,
                    currentFitness );
        }

        // Repopulate the population of chromosomes with those selected
        // by the natural selector.
        // ------------------------------------------------------------
        m_chromosomes = m_activeConfiguration.getNaturalSelector().select(
                                                  m_activeConfiguration,
                                                  m_chromosomes.length );

        // Fire an event to indicate we've performed an evolution.
        // -------------------------------------------------------
        m_activeConfiguration.getEventManager().fireGenotypeEvolvedEvent(
                new GenotypeEvent( this ) );

        // Remove the selected chromosomes from the working pool and then
        // cleanup up any that are leftover in the pool and no longer needed.
        // ------------------------------------------------------------------
        for( int i = 0; i < m_chromosomes.length; i++ )
        {
            m_workingPool.remove( m_chromosomes[i] );
        }

        Iterator leftoverChromosomeIterator = m_workingPool.iterator();
        while( leftoverChromosomeIterator.hasNext() )
        {
            ( (Chromosome) leftoverChromosomeIterator.next() ).cleanup();
        }

        m_workingPool.clear();

        // Clean up the natural selector.
        // ------------------------------
        m_activeConfiguration.getNaturalSelector().empty();
    }


    /**
     * Return a string representation of this Genotype instance,
     * useful for debugging purposes.
     *
     * @return A string representation of this Genotype instance.
     */
    public String toString()
    {
        StringBuffer buffer = new StringBuffer();

        for ( int i = 0; i < m_chromosomes.length; i++ )
        {
            buffer.append( m_chromosomes[ i ].toString() );
            buffer.append( " [" );
            buffer.append(
                m_activeConfiguration.getFitnessFunction().getFitnessValue(
                    m_chromosomes[ i ] ) );
            buffer.append( ']' );
            buffer.append( '\n' );
        }

        return buffer.toString();
    }


    /**
     * Convenience method that returns a newly constructed Genotype
     * instance configured according to the given Configuration instance.
     * The population of Chromosomes will be randomly generated.
     * <p>
     * Note that the given Configuration instance must be in a valid state
     * at the time this method is invoked, or an InvalidConfigurationException
     * will be thrown.
     *
     * @return A newly constructed Genotype instance.
     *
     * @throws InvalidConfigurationException if the given Configuration
     *         instance is null or invalid.
     */
    public static Genotype randomInitialGenotype(
                               Configuration a_activeConfiguration )
                           throws InvalidConfigurationException
    {
        if ( a_activeConfiguration == null )
        {
            throw new InvalidConfigurationException(
                    "The Configuration instance must not be null." );
        }

        a_activeConfiguration.lockSettings();

        // Create an array of chromosomes equal to the desired size in the
        // active Configuration and then populate that array with randomly
        // initialized Chromosome instances.
        // ---------------------------------------------------------------
        int populationSize = a_activeConfiguration.getPopulationSize();
        Chromosome[] chromosomes = new Chromosome[ populationSize ];

        for ( int i = 0; i < populationSize; i++ )
        {
            chromosomes[ i ] =
                Chromosome.randomInitialChromosome( a_activeConfiguration );
        }

        return new Genotype( a_activeConfiguration, chromosomes );
    }


    /**
     * Compares this Genotype against the specified object. The result is true
     * if and only if the argument is an instance of the Genotype class, has
     * exactly the same number of chromosomes as the given Genotype, and, for
     * each chromosome in this Genotype, there is an equal chromosome in the
     * given Genotype. The chromosomes do not need to appear in the same order
     * within the population.
     *
     * @param other The object to compare against.
     * @return true if the objects are the same, false otherwise.
     */
    public boolean equals( Object other )
    {
        try
        {
            Genotype otherGenotype = (Genotype) other;

            // First, make sure the other Genotype has the same number of
            // chromosomes as this one.
            // ----------------------------------------------------------
            if ( m_chromosomes.length != otherGenotype.m_chromosomes.length )
            {
                return false;
            }

            // Next, prepare to compare the chromosomes of the other Genotype
            // against the chromosomes of this Genotype. To make this a lot
            // simpler, we first sort the chromosomes in both this Genotype
            // and the one we're comparing against. This won't affect the
            // genetic algorithm (it doesn't care about the order), but makes
            // our life much easier here.
            // --------------------------------------------------------------
            Arrays.sort( m_chromosomes );
            Arrays.sort( otherGenotype.m_chromosomes );

            for ( int i = 0; i < m_chromosomes.length; i++ )
            {
                if ( !( m_chromosomes[ i ].equals(
                            otherGenotype.m_chromosomes[ i ] ) ) )
                {
                    return false;
                }
            }

            return true;
        }
        catch ( ClassCastException e )
        {
            return false;
        }
    }
}

