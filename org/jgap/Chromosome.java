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
 *
 * @author Neil Rotstan
 */
public class Chromosome implements Cloneable, java.io.Serializable {
  protected final Configuration gaConf;
  protected final int numberOfGenes;
  protected final BitSet genes;

  /**
   * Constructs this Chromosome instance with the given set of genes.
   * Each gene is represented by a single bit in the BitSet. For
   * convenience, the randomInitialChromosome method is provided
   * that will take care of generating a Chromosome instance of a
   * specified size. This constructor exists in case it's desirable
   * to initialize Chromosomes with specific gene values.
   *
   * @param configuration A configuration instance that controls
   *                      the behavior of this GA run. Note that
   *                      it must be in a valid state at the time
   *                      of invocation, or an InvalidConfigurationException
   *                      will be thrown.
   * @param initialGenes  The set of genes with which to initialize
   *                      this Chromosome instance. Each bit in the
   *                      BitSet represents a single gene.
   *
   * @throws InvalidConfigurationException if the given Configuration
   *         instance is null or invalid.
   */
  public Chromosome(Configuration configuration, BitSet initialGenes)
         throws InvalidConfigurationException {
    if(initialGenes == null) {
      throw new IllegalArgumentException( "Genes cannot be null." );
    }

    if (configuration == null) {
      throw new InvalidConfigurationException(
        "Configuration instance must not be null");
    }

    configuration.lockSettings();
    gaConf = configuration;

    genes = initialGenes;
    numberOfGenes = gaConf.getChromosomeSize();
  }


  /**
   * Returns a copy of this Chromosome. The returned instance can
   * evolve independently of this instance.
   *
   * @return A copy of this Chromosome.
   */
  public synchronized Object clone() {
    try {
      return new Chromosome(gaConf, (BitSet) genes.clone());
    }
    catch (InvalidConfigurationException e) {
      // This should never happen because the configuration has already
      // been validated and locked for this instance of the Chromosome,
      // and the configuration given to the new instance should be
      // identical.
      throw new RuntimeException(
        "Fatal Error: reproduce operation produced an " +
        "InvalidConfigurationException. This should never happen." +
        "Please report this as a bug.");
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
  public synchronized boolean getAllele(int locus) {
    return genes.get(locus);
  }


  /**
   * Retrieves the set of genes that make up this Chromosome.
   * This method exists primarily for the benefit of GeneticOperators
   * that require the ability to manipulate Chromosomes at a low leve.
   *
   * @return a BitSet of genes.
   */
  public synchronized BitSet getGenes() {
    return genes;
  }

  
  /**
   * Returns the size of this Chromosome (the number of genes).
   * A Chromosome's size is constant and will never change.
   *
   * @return The number of genes in this Chromosome instance.
   */
  public int size() {
    return numberOfGenes;
  }


  /**
   * Returns a string representation of this Chromosome, useful
   * for debugging purposes.
   *
   * @return A string representation of this Chromosome.
   */
  public String toString() {
    return genes.toString();
  }


  /**
   * Convenience method that returns a newly constructed Chromosome
   * setup according to the settings in the given Configuration
   * instance and assigned random gene values.
   *
   * @param gaConf A Configuration instance that controls the
   *               execution of this GA. Note that it must be
   *               in a valid state at the time of this invocation,
   *               or an InvalidConfigurationException will be thrown.
   *
   * @throws InvalidConfigurationException if the given Configuration
   *         instance is null or invalid.
   */
  public static Chromosome randomInitialChromosome(Configuration gaConf)
                           throws InvalidConfigurationException {
    if (gaConf == null) {
      throw new InvalidConfigurationException(
        "Configuration instance must not be null");
    }

    gaConf.lockSettings();
    int size = gaConf.getChromosomeSize();

    BitSet genes = new BitSet(size);
    
    for(int i = 0; i < size; i++) {
      // Java 1.4 provides a Bitset.set() method that accepts a boolean,
      // which would clean up the following code a little bit. But
      // slightly cleaner code doesn't seem an adequate reason to create
      // a dependency on version 1.4.
      if(gaConf.getRandomGenerator().nextBoolean()) {
        genes.set(i);
      }
      else {
        genes.clear(i);
      }
    }

    return new Chromosome(gaConf, genes);
  }
}
  
