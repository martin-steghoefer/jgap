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

import org.jgap.event.EventManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * The Configuration class represents the current configuration of
 * plugins and flags necessary to execute the genetic algorithm (such
 * as fitness function, natural selector, mutation rate, and so on).
 * <p>
 * Note that, while during setup, the settings, flags, and other
 * values may be set multiple times. But once the lockSettings() method
 * is invoked, they cannot be changed. The default behavior of the
 * Genotype constructor is to invoke this method, meaning that
 * once a Configuration object is passed to a Genotype, it cannot
 * be subsequently modified. There is no mechanism for unlocking
 * the settings once they are locked.
 * <p>
 * Not all configuration options are required. See the documentation
 * for each of the respective mutator methods to determine whether
 * it is required to provide a value for that setting, and what the
 * setting will default to if not.
 */
public class Configuration implements java.io.Serializable
{
    /**
     * References the current fitness function that will be used to evaluate
     * chromosomes during the natural selection process.
     */
    private FitnessFunction m_objectiveFunction = null;

    /**
     * References the NaturalSelector implementation that will be used to
     * determine which chromosomes are chosen to be a part of the next
     * generation population.
     */
    private NaturalSelector m_populationSelector = null;

    /**
     * References an Allele instance that serves as a sample of the type
     * of Alleles that is to be used to represent genes.
     */
    private Allele m_sampleAllele = null;

    /**
     * References the random number generator implementation that is to be
     * used for the generation of any random numbers during the various
     * genetic operations and processes.
     */
    private RandomGenerator m_randomGenerator = null;

    /**
     * References the EventManager that is to be used for the notification
     * of genetic events and the management of event subscribers.
     */
    private EventManager m_eventManager = null;

    /**
     * Stores all of the GeneticOperator implementations that are to be used
     * to operate upon the chromosomes of a population prior to natural
     * selection. In general, operators will be executed in the order that
     * they are added to this list.
     */
    private List m_geneticOperators = new ArrayList();

    /**
     * The number of genes that will be stored in each chromosome in the
     * population.
     */
    private int m_chromosomeSize = 0;

    /**
     * The number of chromosomes that will be stored in the Genotype.
     */
    private int m_populationSize = 0;

    /**
     * Indicates whether the settings of this Configuration instance have
     * been locked. Prior to locking, the settings may be set and reset
     * as desired. Once this flag is set to true, no settings may be
     * altered.
     */
    private boolean m_settingsLocked = false;


    /**
     * Set the fitness function to be used for this genetic algorithm.
     * The fitness function is responsible for evaluating a given
     * Chromosome and returning an integer that represents its
     * worth as a candidate solution. These values are typically
     * used by the natural selector to determine which Chromosome
     * instances will be allowed to move on to the next round
     * of evolution, and which will instead be eliminated.
     *
     * This setting is required.
     *
     * @param a_functionToSet: The fitness function to be used.
     *
     * @throws InvalidConfigurationException if the fitness function
     *         is not satisfactory or if this object is locked.
     */
    public synchronized void setFitnessFunction( FitnessFunction a_functionToSet )
                             throws InvalidConfigurationException
    {
        verifyChangesAllowed();

        if ( a_functionToSet == null )
        {
            throw new InvalidConfigurationException(
                "FitnessFunction instance may not be null." );
        }

        m_objectiveFunction = a_functionToSet;
    }


    /**
     * Retrieve the fitness function being used by this genetic algorithm.
     *
     * @return The fitness function used by this genetic algorithm.
     */
    public FitnessFunction getFitnessFunction()
    {
        return m_objectiveFunction;
    }


    /**
     * Sets a sample instance of the Allele implementation that is to be used
     * to represent genes. This allows type information to be gathered, as
     * well as new Allele instances (of the desired implementation) to be
     * easily generated via the Allele's newAllele() method.
     *
     * @param a_sampleAlleleToSet The allele instance to be used as the sample.
     *
     * @throws InvalidConfigurationException if the given instance is not
     *         satisfactory or if this Configuration is locked.
     */
    public void setSampleAllele( Allele a_sampleAlleleToSet )
                throws InvalidConfigurationException
    {
        verifyChangesAllowed();

        // Sanity check: Make sure that the given instance isn't null.
        // -----------------------------------------------------------
        if( a_sampleAlleleToSet == null )
        {
            throw new InvalidConfigurationException(
                "The sample Allele instance may not be null." );
        }

        // Everything appears to be ok, so assign the instance variable.
        // -------------------------------------------------------------
        m_sampleAllele = a_sampleAlleleToSet;
    }


    /**
     * Retrieves the sample instance of the Allele implementation that is to
     * be used to represent genes. This allows type information to be gathered,
     * as well as new Allele instances (of the desired implementation) to be
     * easily generated via the Allele's newAllele() method.
     *
     * @return a reference to the sample Allele instance.
     */
    public Allele getSampleAllele()
    {
        return m_sampleAllele;
    }


    /**
     * Set the natural selector to be used for this genetic algorithm.
     * The natural selector is responsible for actually selecting
     * which Chromosome instances are allowed to move on to the next
     * round of evolution (usually based on the fitness values
     * provided by the fitness function).
     *
     * This setting is required.
     *
     * @param a_selectorToSet The natural selector to be used.
     *
     * @throws InvalidConfigurationException if the natural selector
     *         is not satisfactory or this object is locked.
     */
    public synchronized void setNaturalSelector( NaturalSelector a_selectorToSet )
                             throws InvalidConfigurationException
    {
        verifyChangesAllowed();

        if ( a_selectorToSet == null )
        {
            throw new InvalidConfigurationException(
                    "Natural Selector instance may not be null." );
        }

        m_populationSelector = a_selectorToSet;
    }


    /**
     * Retrieve the natural selector being used by this genetic
     * algorithm.
     *
     * @return The natural selector used by this genetic algorithm.
     */
    public NaturalSelector getNaturalSelector()
    {
        return m_populationSelector;
    }


    /**
     * Set the random generator to be used for this genetic algorithm.
     * The random generator is responsible for generating random numbers,
     * which are used throughout the process of genetic evolution and
     * selection.
     *
     * This setting is required.
     *
     * @param a_generatorToSet The random generator to be used.
     *
     * @throws InvalidConfigurationException if the random generator
     *         is not satisfactory or this object is locked.
     */
    public synchronized void setRandomGenerator( RandomGenerator a_generatorToSet )
                             throws InvalidConfigurationException
    {
        verifyChangesAllowed();

        if ( a_generatorToSet == null )
        {
            throw new InvalidConfigurationException(
                    "RandomGenerator instance may not be null." );
        }

        m_randomGenerator = a_generatorToSet;
    }


    /**
     * Retrieve the random generator being used by this genetic
     * algorithm.
     *
     * @return The random generator used by this genetic algorithm.
     */
    public RandomGenerator getRandomGenerator()
    {
        return m_randomGenerator;
    }


    /**
     * Add a genetic operator for use in this algorithm. Genetic operators
     * represent evolutionary steps that, when combined, make up the
     * evolutionary process. Examples of genetic operators are reproduction,
     * crossover, and mutation. During the evolution process, all of the
     * genetic operators added via this method are invoked in the order
     * they were added.
     *
     * At least one genetic operator must be provided.
     *
     * @param a_operatorToAdd The genetic operator to be added.
     *
     * @throws InvalidConfigurationException if the genetic operator
     *         is null a_operatorToAdd this robject is locked.
     */
    public synchronized void addGeneticOperator( GeneticOperator a_operatorToAdd )
                             throws InvalidConfigurationException
    {
        verifyChangesAllowed();

        if ( a_operatorToAdd == null )
        {
            throw new InvalidConfigurationException(
                    "GeneticOperator instance may not be null." );
        }

        m_geneticOperators.add( a_operatorToAdd );
    }


    /**
     * Retrieve the genetic operators added for this genetic algorithm.
     * Note that once this Configuration instance is locked, a new,
     * immutable list of operators is used and any lists previously
     * retrieved with this method will no longer reflect the actual
     * list in use.
     *
     * @return The list of genetic operators added to this Configuration
     */
    public List getGeneticOperators()
    {
        return m_geneticOperators;
    }


    /**
     * Set the chromosome size to be used for this genetic algorithm.
     * The chromosome size is a fixed value that represntes the
     * number of genes represented by each Chromosome.
     *
     * This setting is required.
     *
     * @param a_sizeOfChromosomes The chromosome size to be used.
     *
     * @throws InvalidConfigurationException if the chromosome size
     *         is not satisfactory or this object is locked.
     */
    public synchronized void setChromosomeSize( int a_sizeOfChromosomes )
                             throws InvalidConfigurationException
    {
        verifyChangesAllowed();

        m_chromosomeSize = a_sizeOfChromosomes;
    }


    /**
     * Retrieve the chromosome size being used by this genetic
     * algorithm.
     *
     * @return The chromosome size used by this genetic algorithm.
     */
    public int getChromosomeSize()
    {
        return m_chromosomeSize;
    }


    /**
     * Set the population size to be used for this genetic algorithm.
     * The population size is a fixed value that represntes the
     * number of Chromosomes represented in a Genotype.
     *
     * This setting is required.
     *
     * @param a_sizeOfPopulation The population size to be used.
     *
     * @throws InvalidConfigurationException if the population size
     *         is not satisfactory or this object is locked.
     */
    public synchronized void setPopulationSize( int a_sizeOfPopulation )
                             throws InvalidConfigurationException
    {
        verifyChangesAllowed();

        m_populationSize = a_sizeOfPopulation;
    }


    /**
     * Retrieve the population size being used by this genetic
     * algorithm.
     *
     * @return The population size used by this genetic algorithm.
     */
    public int getPopulationSize()
    {
        return m_populationSize;
    }


    /**
     * Sets the EventManager that is to be associated with this configuration.
     * The EventManager is responsible for the management of event subscribers
     * and event notifications.
     *
     * @param a_eventManagerToSet the EventManager instance to use in this
     *                            configuration.
     *
     * @throws InvalidConfigurationException if the population size
     *         is not satisfactory or this object is locked.
     */
    public void setEventManager( EventManager a_eventManagerToSet )
                throws InvalidConfigurationException
    {
        verifyChangesAllowed();

        if( a_eventManagerToSet == null )
        {
            throw new InvalidConfigurationException(
                "The EventManager instance may not be null." );
        }

        m_eventManager = a_eventManagerToSet;
    }


    /**
     * Retrieves the EventManager associated with this configuration.
     * The EventManager is responsible for the management of event subscribers
     * and event notifications.
     *
     * @return the actively configured EventManager instance.
     */
    public EventManager getEventManager()
    {
        return m_eventManager;
    }


    /**
     * Lock all of the settings in this configuration object. Once
     * this method is successfully invoked, none of the settings may
     * be changed. There is no way to unlock this object once it is locked.
     * <p>
     * Prior to returning successfully, this method will first invoke the
     * verifyStateIsValid() method to make sure that any required configuration
     * options have been properly set. If it detects a problem, it will
     * throw an InvalidConfigurationException and leave the object unlocked.
     * <p>
     * It's possible to test whether is object is locked through the
     * isLocked() method.
     * <p>
     * It is ok to lock an object more than once. In this case, this method
     * does nothing and simply returns.
     *
     * @throws InvalidConfigurationException if the object is in an invalid
     *         state at the time of invocation.
     */
    public synchronized void lockSettings()
                             throws InvalidConfigurationException
    {
        if ( !m_settingsLocked )
        {
            verifyStateIsValid();

            // Make genetic operators list immutable.
            // --------------------------------------
            m_geneticOperators =
                Collections.unmodifiableList( m_geneticOperators );

            m_settingsLocked = true;
        }
    }


    /**
     * Retrieve the lock status of this object.
     *
     * @return true if this object has been locked by a previous successful
     *         call to the lockSettings() method, false otherwise.
     */
    public boolean isLocked()
    {
        return m_settingsLocked;
    }


    /**
     * Test the state of this object to make sure it's valid. This generally
     * consists of verifying that required settings have, in fact, been set.
     * If this object is not in a valid state, then an exception will be
     * thrown detailing the reason the state is not valid.
     *
     * @throws InvalidConfigurationException if the state of this Configuration
     *         is not valid. The error message in the exception will detail
     *         the reason for invalidity.
     */
    public synchronized void verifyStateIsValid()
                             throws InvalidConfigurationException
    {
        // First, make sure all of the required fields have been set to
        // appropriate values.
        // ------------------------------------------------------------
        if( m_objectiveFunction == null )
        {
            throw new InvalidConfigurationException(
                "A desired fitness function must be specified in the active " +
                "configuration." );
        }

        if( m_sampleAllele == null )
        {
            throw new InvalidConfigurationException(
                "A sample instance of the desired concrete Allele " +
                "implementation must be specified in the active " +
                "configuration." );
        }

        if( m_populationSelector == null )
        {
            throw new InvalidConfigurationException(
                "A desired natural selector must be specified in the active " +
                "configuration." );
        }

        if( m_randomGenerator == null )
        {
            throw new InvalidConfigurationException(
                "A desired random number generator must be specified in the " +
                "active configuration." );
        }

        if( m_eventManager == null )
        {
            throw new InvalidConfigurationException(
                "A desired event manager must be specified in the active " +
                "configuration." );
        }

        if( m_geneticOperators.isEmpty() )
        {
            throw new InvalidConfigurationException(
                "At least one genetic operator must be specified in the " +
                "configuration." );
        }

        if( m_chromosomeSize <= 0 )
        {
            throw new InvalidConfigurationException(
                "A chromosome size greater than zero must be specified in " +
                "the active configuration." );
        }

        if( m_populationSize <= 0 )
        {
            throw new InvalidConfigurationException(
                "A genotype size greater than zero must be specified in " +
                "the active configuration." );
        }

        // Next, it's critical that the Allele implementation has a working
        // equals() methods, or else the genetic engine will end up
        // failing in mysterious and unpredictable ways. We therefore verify
        // right here that this method is working properly.
        // -------------------------------------------------------------------
        Allele sampleCopy = m_sampleAllele.newAllele( this );
        sampleCopy.setValue( m_sampleAllele.getValue() );

        if( !( sampleCopy.equals( m_sampleAllele ) ) )
        {
            throw new InvalidConfigurationException(
                "The sample Allele does not appear to have a working " +
                "equals() method. When tested, the method returned false " +
                "when comparing the sample allele with an allele of the" +
                "same type possessing the same value. " );
        }
    }


    /**
     * Makes sure that this object isn't locked. If it is, then an
     * exception is thrown with an appropriate message indicating
     * that settings in this object may not be altered. This method
     * should be invoked by any mutator method in this object prior
     * to making any state alterations.
     *
     * @throws InvalidConfigurationException if this object is locked.
     */
    protected void verifyChangesAllowed()
                   throws InvalidConfigurationException
    {
        if ( m_settingsLocked )
        {
            throw new InvalidConfigurationException(
                "This Configuration object is locked. Settings may not be " +
                "altered." );
        }
    }
}

