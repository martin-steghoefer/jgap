/*
 * Copyright 2001, Neil Rotstan
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

import java.util.*;


/**
 * Chromosomes represent fixed-length collections of genes.
 * In the current implementation, this is the lowest
 * level class since genes are, themselves, represented
 * as individual bits. Each gene can have two values
 * (true or false). This should be suitable for most
 * applications, but it's always possible for a fitness
 * function to consider multiple genes as a single unit
 * if more than two possible values are desired.
 * In the future, a Gene class and Allele interface may be
 * created that will allow more flexibility in this regard.
 * <p>
 * Chromosomes support reproduction, crossover, and mutation.
 * Additional mechanisms for chromosome evolution are
 * planned for future versions.
 *
 * @author Neil Rotstan (neil at bluesock.org)
 */
public class Chromosome
{
  private static final int DEFAULT_MUTATION_RATE = 1000;

  private final int mutationRate;
  private final BitSet genes;
  private final int numberOfGenes;

  private static Random generator = new Random();


  /**
   * Constructs this Chromosome instance with the given set of genes.
   * Each gene is represented by a single bit in the BitSet. For
   * convenience, the randomInitialChromosome method is provided
   * that will take care of generating a Chromosome instance of a
   * specified size. This constructor exists in case it's desirable
   * to initialize Chromosomes with specific gene values.
   *
   * @param initialGenes  The set of genes with which to initialize
   *                      this Chromosome instance. Each bit in the
   *                      BitSet represents a single gene.
   */
  public Chromosome( BitSet initialGenes, int chromosomeLength )
  {
    if( initialGenes == null )
    {
      throw new IllegalArgumentException( "Genes cannot be null." );
    }

    else if( chromosomeLength <= 0 )
    {
      throw new IllegalArgumentException( 
        "Chromosome length must be positive" );
    }

    genes = initialGenes;
    numberOfGenes = chromosomeLength;
    mutationRate = DEFAULT_MUTATION_RATE;
  }


  /**
   * Constructs this Chromosome instance with the given set of genes
   * and the given mutation rate. Each gene is represented by a
   * single bit in the BitSet. For convenience, 
   * the randomInitialChromosome method is provided that will take
   * care of generating a Chromosome instance of a specified size.
   * This constructor exists in case it's desirable to initialize
   * Chromosomes with specific gene values.
   *
   * @param initialGenes  The set of genes with which to initialize
   *                      this Chromosome instance. Each bit in the
   *                      BitSet represents a single gene.
   *
   * @param desiredMutationRate  The desired chances of mutation
   *                             expressed as the fraction 1/n where
   *                             n is this parameter. For example, if
   *                             1000 were given, then statistically
   *                             one out of every thousand genes 
   *                             processed would incur mutation during
   *                             evolution.
   */
  public Chromosome( BitSet initialGenes, int chromosomeLength,
                     int desiredMutationRate )
  {
    if( initialGenes == null )
    {
      throw new IllegalArgumentException( "Genes cannot be null." );
    }

    else if( chromosomeLength <= 0 )
    {
      throw new IllegalArgumentException(
        "Chromosomes must have a positive length" );
    }

    genes = initialGenes;
    numberOfGenes = chromosomeLength;
    mutationRate = desiredMutationRate;
  }


  /**
   * Returns a copy of this Chromosome. The returned instance can
   * evolve independently of this instance.
   *
   * @return A copy of this Chromosome.
   */
  public Chromosome reproduce()
  {
    return new Chromosome( 
      (BitSet) genes.clone(), numberOfGenes, mutationRate );
  }


  /**
   * Performs basic crossover between this Chromosome instance and
   * the given instance. A locus (element index) is randomly selected
   * and all genes with a locus greater or equal to that randomly
   * selected are then swapped between the two Chromosome instances.
   * For example, suppose this Chromosome had the genes 1010 and the
   * given Chromosome had the genes 0011. This method would choose a
   * random index from 0-3. Let's say it chose 2. Then it would swap
   * the genes with indexes >= 2 between the two chromosomes. This
   * chromosome would then be 1011 and the given Chromosome would be
   * 0010. 
   *
   * Note that the given Chromosome must have the same number of
   * genes as this Chromosome or an IllegalArgumentException will be
   * thrown.
   *
   * @param mate: The Chromosome to "mate" with this one via the
   *              crossover mechanism described above.
   *
   * @throws IllegalArgumentException if the size of the given
   *         Chromosome is not the same as the size of this
   *         Chromosome.
   */
  public void crossover( Chromosome mate )
  {
    int locus = generator.nextInt( numberOfGenes );
    boolean currentAllele;

    for( int i = locus; i < numberOfGenes; i++ )
    {
      currentAllele = genes.get( i );
      
      if( mate.genes.get( i ) )
      {
        genes.set( i );
      }

      else
      {
        genes.clear( i );
      }

      if( currentAllele )
      {
        mate.genes.set( i );
      }

      else
      {
        mate.genes.clear( i );
      }
    }
  }


  /**
   * Runs through the genes of this Chromosome, possibly mutating
   * some in the process. For each gene, a random number is chosen
   * between zero and the mutation rate provided at construction
   * (or the default rate if none was provided). If the value of
   * the random number is 0, then the gene's bit will be flipped.
   */
  public void mutate()
  {
    for( int i = 0 ; i < numberOfGenes; i++ )
    {
      if( generator.nextInt( mutationRate ) == 0 )
      {
        if( genes.get( i ) )
        {
          genes.clear( i );
        }
  
        else
        {
          genes.set( i );
        }
      }
    }
  }


  /**
   * Returns the allele (value) of the gene at the given locus
   * (index) within the Chromosome. The first gene is at locus
   * zero and the last gene is at the locus equal to the size
   * of this Chromosome - 1.
   *
   * @param locus: The index of the gene value to be returned.
   * @return The boolean value of the indicated gene.
   */
  public boolean getAllele( int locus )
  {
    return genes.get( locus );
  }


  /**
   * Returns the size of this Chromosome (the number of genes).
   * A Chromosome's size is constant and will never change.
   *
   * @return The number of genes in this Chromosome instance.
   */
  public int size()
  {
    return numberOfGenes;
  }


  /**
   * Returns a string representation of this Chromosome, useful
   * for debugging purposes.
   *
   * @return A string representation of this Chromosome.
   */
  public String toString()
  {
    return genes.toString();
  }


  /**
   * Convenience method that returns a newly constructed Chromosome
   * instance of the given size with a random population of genes.
   * This method will instantiate the Chromosome with the default
   * mutation rate.
   *
   * @param size: The number of genes the Chromosome should contain.
   */
  public static Chromosome randomInitialChromosome( int size )
  {
    return randomInitialChromosome( size, DEFAULT_MUTATION_RATE );
  }


  /**
   * Convenience method that returns a newly constructed Chromosome
   * instance of the given size with a random population of genes
   * and instantiated with the given mutation rate.
   *
   * @param size: The number of genes the Chromosome should contain.
   * @param mutationRate: The desired mutation rate for this
   *                      Chromosome.
   */
  public static Chromosome randomInitialChromosome( int size,
                                                    int mutationRate )
  {
    BitSet genes = new BitSet( size );
    
    for( int i = 0; i < size; i++ )
    {
      if( generator.nextInt( 2 ) == 0 )
      {
        genes.set( i );
      }

      else
      {
        genes.clear( i );
      }
    }

    return new Chromosome( genes, size, mutationRate );
  }
}  
