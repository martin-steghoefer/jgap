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

import org.jgap.impl.ChromosomePool;


/**
 * Chromosomes represent fixed-length collections of genes. In the current
 * implementation, this is the lowest level class since genes are, themselves,
 * represented as individual bits. Each gene can have two values (true or
 * false). This should be suitable for most applications, but it's always
 * possible for a fitness function to consider multiple genes as a single unit
 * if more than two possible values are desired. In the future, a Gene class
 * and Allele interface may be created that will allow more flexibility in this
 * regard.
 */
public class Chromosome implements Cloneable, java.io.Serializable, Comparable
{
    /**
     * The current active genetic configuration.
     */
    protected final Configuration m_activeConfiguration;

    /**
     * The array of Alleles that represent the values of the genes in this
     * Chromosome.
     */
    protected final Allele[] m_genes;

    /**
     * Keeps track of whether or not this Chromosome has been selected by
     * the natural selector to move on to the next generation.
     */
    protected boolean m_isSelected = false;

    /**
     * Stores the fitness value of this Chromosome as determined by the
     * active fitness function.
     */
    protected int m_fitnessValue = -1;


    /**
     * Constructs a Chromosome of the given size separate from any specific
     * Configuration. This constructor will use the given sample Allele to
     * create an Allele representation for each of its genes. This can be
     * useful for constructing sample chromosomes that use the same Allele
     * type for all of their genes and that are to be used to setup a
     * Configuration object.
     *
     * @param a_sampleAllele A sample concrete Allele instance representative
     *                       of the Alleles that should represent all genes
     *                       in this Chromosome instance.
     * @param a_desiredSize The desired size (number of genes) of this
     *                      Chromosome.
     */
    public Chromosome( Allele a_sampleAllele, int a_desiredSize )
    {
        if( a_sampleAllele == null )
        {
            throw new IllegalArgumentException(
                "Sample Allele cannot be null." );
        }

        if( a_desiredSize <= 0 )
        {
            throw new IllegalArgumentException(
                "Chromosome size must be greater than zero." );
        }

        m_genes = new Allele[ a_desiredSize ];
        for( int i = 0; i < m_genes.length; i++ )
        {
            m_genes[i] = a_sampleAllele.newAllele( null );
        }

        m_activeConfiguration = null;
    }


    /**
     * Constructs a Chromosome separate from any specific Configuration. This
     * can be useful for constructing sample chromosomes that are to be used
     * to setup a Configuration object.
     *
     * @param a_initialGenes The genes of this Chromosome.
     */
    public Chromosome( Allele[] a_initialGenes )
    {
        // Sanity check: make sure the genes aren't null.
        // ----------------------------------------------
        if ( a_initialGenes == null )
        {
            throw new IllegalArgumentException( "Genes cannot be null." );
        }

        m_genes = a_initialGenes;
        m_activeConfiguration = null;
    }


    /**
     * Constructs this Chromosome instance with the given array of gene values.
     * Each gene is represented by a single allele in the array. For
     * convenience, the randomInitialChromosome method is provided
     * that will take care of generating a Chromosome instance of a
     * specified size. This constructor exists in case it's desirable
     * to initialize Chromosomes with specific gene values.
     *
     * @param a_activeConfiguration A Configuration instance that controls
     *                              the behavior of this GA run. Note that
     *                              it must be in a valid state at the time
     *                              of invocation, or an
     *                              InvalidConfigurationException
     *                              will be thrown.
     * @param a_initialGenes The set of alleles representing the values of
     *                       the genes with which to initialize this
     *                       Chromosome instance. Each allele represents the
     *                       value of a single gene.
     *
     * @throws InvalidConfigurationException if the given Configuration
     *         instance is null or invalid.
     */
    public Chromosome( Configuration a_activeConfiguration,
                       Allele[] a_initialGenes )
           throws InvalidConfigurationException
    {
        // Sanity checks: make sure the parameters aren't null.
        // ----------------------------------------------------
        if ( a_initialGenes == null )
        {
            throw new IllegalArgumentException( "Genes cannot be null." );
        }

        if ( a_activeConfiguration == null )
        {
            throw new InvalidConfigurationException(
                "Configuration instance must not be null" );
        }

        // Lock the configuration settings so that they can't be changed from
        // now on, and then populate our instance variables.
        // ------------------------------------------------------------------
        a_activeConfiguration.lockSettings();
        m_activeConfiguration = a_activeConfiguration;

        m_genes = a_initialGenes;
    }


    /**
     * Returns a copy of this Chromosome. The returned instance can evolve
     * independently of this instance. Note that, if possible, this method
     * will first attempt to acquire a Chromosome instance from the active
     * ChromosomePool (if any) and set its value appropriately before
     * returning it. If that is not possible, then a new Chromosome instance
     * will be constructed and its value set appropriately before returning.
     *
     * @return A copy of this Chromosome.
     */
    public synchronized Object clone()
    {
        // First see if we can pull a Chromosome from the pool and just
        // set its allele values appropriately.
        // ------------------------------------------------------------
        ChromosomePool pool = m_activeConfiguration.getChromosomePool();
        if( pool != null )
        {
            Chromosome copy = pool.acquireChromosome();
            if( copy != null )
            {
                Allele[] genes = copy.getGenes();
                for( int i = 0; i < genes.length; i++ )
                {
                    genes[ i ].setValue( m_genes[ i ].getValue() );
                }

                return copy;
            }
        }

        // We couldn't fetch a Chromosome from the pool, so we need to
        // create a new one.  First we make a copy of each of the Alleles.
        //  We explicity use the Allele at each respective gene location
        // (locus) to create the new Allele that is to occupy that same
        // locus in the new Chromosome.
        // ---------------------------------------------------------------
        Allele[] copyOfGenes = new Allele[ m_genes.length ];

        for( int i = 0; i < copyOfGenes.length; i++ )
        {
            copyOfGenes[ i ] = m_genes[i].newAllele( m_activeConfiguration );
            copyOfGenes[ i ].setValue(  m_genes[i].getValue() );
        }

        // Now construct a new Chromosome with the copies of the genes and
        // return it.
        // ---------------------------------------------------------------
        try
        {
            return new Chromosome( m_activeConfiguration, copyOfGenes );
        }
        catch ( InvalidConfigurationException e )
        {
            // This should never happen because the configuration has already
            // been validated and locked for this instance of the Chromosome,
            // and the configuration given to the new instance should be
            // identical.
            // --------------------------------------------------------------
            throw new RuntimeException(
                    "Fatal Error: clone method produced an " +
                    "InvalidConfigurationException. This should never happen." +
                    "Please report this as a bug to the JGAP team." );
        }
    }


    /**
     * Returns the Allele representing the value of the gene at the given locus
     * (index) within the Chromosome. The first gene is at locus zero and the
     * last gene is at the locus equal to the size of this Chromosome - 1.
     *
     * @param a_desiredLocus: The index of the gene value to be returned.
     * @return The Allele representing the value of the indicated gene.
     */
    public synchronized Allele getAllele( int a_desiredLocus )
    {
        return m_genes[ a_desiredLocus ];
    }


    /**
     * Retrieves the set of genes that make up this Chromosome. This method
     * exists primarily for the benefit of GeneticOperators that require the
     * ability to manipulate Chromosomes at a low level.
     *
     * @return a BitSet of genes.
     */
    public synchronized Allele[] getGenes()
    {
        return m_genes;
    }


    /**
     * Returns the size of this Chromosome (the number of genes).
     * A Chromosome's size is constant and will never change.
     *
     * @return The number of genes in this Chromosome instance.
     */
    public int size()
    {
        return m_genes.length;
    }


    /**
     * Retrieves the fitness value of this Chromosome, as determined by the
     * active fitness function.
     *
     * @return a positive integer value representing the fitness of this
     *         Chromosome.
     */
    public int getFitnessValue()
    {
        if( m_fitnessValue < 0 )
        {
            m_fitnessValue =
                m_activeConfiguration.getFitnessFunction().
                    getFitnessValue( this );
        }

        return m_fitnessValue;
    }


    /**
     * Returns a string representation of this Chromosome, useful
     * for debugging purposes.
     *
     * @return A string representation of this Chromosome.
     */
    public String toString()
    {
        StringBuffer representation = new StringBuffer();
        representation.append( "[ " );

        // Append the representations of each of the gene Alleles.
        // -------------------------------------------------------
        for( int i = 0; i < m_genes.length - 1; i++ )
        {
            representation.append( m_genes[i].toString() );
            representation.append( ", " );
        }
        representation.append( m_genes[m_genes.length - 1].toString() );
        representation.append( " ]");

        return representation.toString();
    }


    /**
     * Convenience method that returns a new Chromosome instance with random
     * allele values. Note that, if possible, this method will acquire a
     * Chromosome instance from the active ChromosomePool (if any) and then
     * randomize its allele values before returning it. If a Chromosome
     * cannot be acquired from the pool, then a new instance will be
     * constructed and its allele values randomized before returning it.
     *
     * @param a_activeConfiguration The current active configuration.
     *
     * @throws InvalidConfigurationException if the given Configuration
     *         instance is null or invalid.
     */
    public static Chromosome randomInitialChromosome(
                                 Configuration a_activeConfiguration )
                             throws InvalidConfigurationException
    {
        // Sanity check: make sure the given configuration isn't null.
        // -----------------------------------------------------------
        if ( a_activeConfiguration == null )
        {
            throw new InvalidConfigurationException(
                    "Configuration instance must not be null" );
        }

        // Lock the configuration settings so that they can't be changed from
        // now on.
        // ------------------------------------------------------------------
        a_activeConfiguration.lockSettings();

        // First see if we can get a Chromosome instance from the pool.
        // If we can, we'll randomize its alleles and then return it.
        // ------------------------------------------------------------
        ChromosomePool pool = a_activeConfiguration.getChromosomePool();
        if( pool != null )
        {
            Chromosome randomChromosome = pool.acquireChromosome();
            if( randomChromosome != null )
            {
                Allele[] genes = randomChromosome.getGenes();
                RandomGenerator generator = 
                    a_activeConfiguration.getRandomGenerator();

                for( int i = 0; i < genes.length; i++ )
                {
                    genes[ i ].setToRandomValue( generator );
                }

                return randomChromosome;
            }
        }

        // We weren't able to get a Chromosome from the pool, so we have
        // to construct a new instance and build it from scratch.
        // -------------------------------------------------------------
        Chromosome sampleChromosome =
            a_activeConfiguration.getSampleChromosome();

        Allele[] sampleGenes = sampleChromosome.getGenes();
        int numberOfGenes = sampleChromosome.size();

        Allele[] newGenes = new Allele[ numberOfGenes ];
        RandomGenerator generator = a_activeConfiguration.getRandomGenerator();

        for ( int i = 0; i < numberOfGenes; i++ )
        {
            // We ues the newAllele() method on each of the alleles in the
            // sample Chromosome to generate our new Allele instances for
            // the Chromosome we're turning. This guarantees that the
            // new Alleles are setup with all of the correct internal state
            // for the respective gene position they're going to inhabit.
            // -----------------------------------------------------------
            newGenes[ i ] = sampleGenes[ i ].newAllele( a_activeConfiguration );

            // Set the allele to a random value.
            // -------------------------------
            newGenes[i].setToRandomValue( generator );
        }

        // Finally, construct the new chromosome with the new random
        // genes and return it.
        // ---------------------------------------------------------
        return new Chromosome( a_activeConfiguration, newGenes );
    }


    /**
     * Compares this Chromosome against the specified object. The result is
     * true if and only if the argument is an instance of the Chromosome class
     * and has a set of genes equal to this one.
     *
     * @param other The object to compare against.
     * @return true if the objects are the same, false otherwise.
     */
    public boolean equals( Object other )
    {
        try
        {
            Chromosome otherChromosome = (Chromosome) other;

            // Run through all of the genes in this Chromosome and compare
            // them against the genes of the other Chromosome.
            // -----------------------------------------------------------
            Allele[] otherGenes = otherChromosome.m_genes;

            for( int i = 0; i < m_genes.length; i++ )
            {
                if( !( m_genes[i].equals( otherGenes[i] ) ) )
                {
                    return false;
                }
            }

            // All of the genes are the same, so return true.
            // ----------------------------------------------
            return true;
        }
        catch ( ClassCastException e )
        {
            // If the given object is not a Chromosome, return false.
            // ------------------------------------------------------
            return false;
        }
    }


    /**
     * Retrieve a hash code for this Chromosome.
     *
     * @return the hash code of this Chromosome.
     */
    public int hashCode()
    {
        return getFitnessValue();
    }


    /**
     * Compares the given Chromosome to this Chromosome. This chromosome is
     * considered to be "less than" the given chromosome if any of its genes
     * are less than their corresponding genes in the other chromosome or,
     * if all of the comparisons are equal, if this chromosome contains fewer
     * genes than the other chromosome.
     *
     * @param other The Chromosome against which to compare this chromosome.
     * @return a negative number if this chromosome is "less than" the given
     *         chromosome, zero if they are equal to each other, and a positive
     *         number if this chromosome is "greater than" the given chromosome.
     */
    public int compareTo( Object other )
    {
        Chromosome otherChromosome = (Chromosome) other;

        // First, if the other Chromosome is null, then this chromosome is
        // automatically the "greater" Chromosome.
        // ---------------------------------------------------------------
        if( otherChromosome == null )
        {
            return 1;
        }

        // First we compare as many genes as possible for differences. If
        // one of the genes is not equal, then we return the result of its
        // comparison.
        // ---------------------------------------------------------------
        Allele[] otherGenes = otherChromosome.m_genes;

        int numberOfGenesToCompare =
            Math.min( m_genes.length, otherGenes.length );

        for( int i = 0; i < numberOfGenesToCompare; i++ )
        {
            int comparison = m_genes[i].compareTo( otherGenes[i] );

            if( comparison != 0 )
            {
                return comparison;
            }
        }

        // All of the compared genes were equal to each other. So now we
        // just do the comparison based on the length of the chromosomes.
        // If they are of equal length, then they will be equal. Otherwise
        // the shorter chromosome will be the "lesser" chromosome.
        // ---------------------------------------------------------------
        return m_genes.length - otherGenes.length;
    }


    /**
     * Sets whether this Chromosome has been selected by the natural selector
     * to continue to the next generation.
     *
     * @param a_isSelected true if this Chromosome has been selected, false
     *                     otherwise.
     */
    public void setIsSelected( boolean a_isSelected )
    {
        m_isSelected = a_isSelected;
    }


    /**
     * Retrieves whether this Chromosome has been selected by the natural
     * selector to continue to the next generation.
     *
     * @return true if this Chromosome has been selected, false otherwise.
     */
    public boolean isSelected()
    {
        return m_isSelected;
    }


    /**
     * Invoked when this Chromosome is no longer needed and should perform
     * any necessary cleanup. Note that this method will attempt to release
     * this Chromosome instance to the active ChromosomePool, if any.
     */
    public void cleanup()
    {
        // First, reset our internal state.
        // --------------------------------
        m_fitnessValue = -1;
        m_isSelected = false;

        // If a ChromosomePool is setup in the active configuration, then
        // release this Chromosome to the pool, thereby saving some
        // memory.
        // --------------------------------------------------------------
        ChromosomePool pool = m_activeConfiguration.getChromosomePool();
        if( pool != null )
        {
            // The pool will take care of any allele cleanup for us.
            // -----------------------------------------------------
            pool.releaseChromosome( this );
        }
        else
        {
            // We need to manually cleanup our alleles.
            // ----------------------------------------
            for( int i = 0; i < m_genes.length; i++ )
            {
                m_genes[ i ].cleanup();
            }
        }
    }
}

