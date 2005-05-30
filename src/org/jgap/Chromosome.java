/**
 * JGAP offers a dual license model(see below for specific license information):
 * +The LGPL may be used anytime.
 * +The MPL may be used if at least $20 have been donated to the JGAP project
 *  thru PayPal (see http://www.sourceforge.net/projects/jgap).
 *
 * Specific license information (MPL and LGPL)
 * -------------------------------------------
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 *
 * The Original Code is 'JGAP - Java Genetic Algorithms Package'.
 * The Initial Developer of the Original Code is Neil Rotstan. Portions created
 * by the Initial Developer are Copyright (C) 2002- 2003 by Neil Rotstan.
 * All Rights Reserved.
 * Co-developer of the code is Klaus Meffert. Portions created by the co-
 * developer are Copyright (C) 2003-2005 by Klaus Meffert. All Rights Reserved.
 * Contributor(s): all the names of the contributors are added in the source
 * code where applicable.
 *
 * Alternatively, the contents of this file may be used under the terms of the
 * LGPL license (the "GNU LESSER PUBLIC LICENSE"), in which case the
 * provisions of LGPL are applicable instead of those above.  If you wish to
 * allow use of your version of this file only under the terms of the LGPL
 * License and not to allow others to use your version of this file under
 * the MPL, indicate your decision by deleting the provisions above and
 * replace them with the notice and other provisions required by the LGPL.
 * If you do not delete the provisions above, a recipient may use your version
 * of this file under either the MPL or the LGPL.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the MPL as stated above or under the terms of the LGPL.
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser Public License for more
 * details.
 */
package org.jgap;

import java.io.*;
import org.jgap.impl.*;

/**
 * Chromosomes represent potential solutions and consist of a fixed-length
 * collection of genes. Each gene represents a discrete part of the solution.
 * Each gene in the Chromosome may be backed by a different concrete
 * implementation of the Gene interface, but all genes in a respective
 * position (locus) must share the same concrete implementation across
 * Chromosomes within a single population (genotype). In other words, gene 1
 * in a chromosome must share the same concrete implementation as gene 1 in all
 * other chromosomes in the population.
 *
 * @author Neil Rotstan
 * @author Klaus Meffert
 * @since 1.0
 */
public class Chromosome
    implements Comparable, Cloneable, Serializable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.37 $";

  public static final double DELTA = 0.000000001d;

  /**
   * Application-specific data that is attached to this Chromosome.
   * This data may assist the application in evaluating this Chromosome
   * in the fitness function. JGAP completely ignores the data, aside
   * from allowing it to be set and retrieved.
   */
  private Object m_applicationData;

  /**
   * The array of Genes contained in this Chromosome.
   */
  protected Gene[] m_genes;

  /**
   * Keeps track of whether or not this Chromosome has been selected by
   * the natural selector to move on to the next generation.
   */
  protected boolean m_isSelectedForNextGeneration = false;

  /**
   * Stores the fitness value of this Chromosome as determined by the
   * active fitness function. A value of -1 indicates that this field
   * has not yet been set with this Chromosome's fitness values (valid
   * fitness values are always positive).
   *
   * @since 2.0 (until 1.1: type int)
   */
  protected double m_fitnessValue = -1.0000000d;

  /**
   * Method compareTo(): Should we also consider the application data when
   * comparing? Default is "false" as "true" means a Chromosome's losing its
   * identity when application data is set differently!
   *
   * @since 2.2
   */
  private boolean m_compareAppData;

  /**
   * Constructs a Chromosome of the given size separate from any specific
   * Configuration. This constructor will use the given sample Gene to
   * construct a new Chromosome instance containing genes all of the same
   * type as the sample Gene. This can be useful for constructing sample
   * chromosomes that use the same Gene type for all of their genes and that
   * are to be used to setup a Configuration object.
   *
   * @param a_sampleGene A sample concrete Gene instance that will be used
   *                       as a template for all of the genes in this
   *                       Chromosome.
   * @param a_desiredSize The desired size (number of genes) of this
   *                      Chromosome.
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public Chromosome(Gene a_sampleGene, int a_desiredSize) {
    // Do some sanity checking to make sure the parameters we were
    // given are valid.
    // -----------------------------------------------------------
    if (a_sampleGene == null) {
      throw new IllegalArgumentException(
          "Sample Gene cannot be null.");
    }
    if (a_desiredSize <= 0) {
      throw new IllegalArgumentException(
          "Chromosome size must be positive.");
    }
    // Create the array of Genes and populate it with new Gene
    // instances created from the sample gene.
    // -------------------------------------------------------
    m_genes = new Gene[a_desiredSize];
    for (int i = 0; i < m_genes.length; i++) {
      m_genes[i] = a_sampleGene.newGene();
    }
  }

  /**
   * Constructs a Chromosome separate from any specific Configuration. This
   * can be useful for constructing sample chromosomes that are to be used
   * to setup a Configuration object.
   *
   * @param a_initialGenes The genes of this Chromosome.
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public Chromosome(Gene[] a_initialGenes) {
    // Sanity checks: make sure the genes array isn't null and
    // that none of the genes contained within it are null.
    // -------------------------------------------------------
    if (a_initialGenes == null) {
      throw new IllegalArgumentException(
          "The given array of genes cannot be null.");
    }
    for (int i = 0; i < a_initialGenes.length; i++) {
      if (a_initialGenes[i] == null) {
        throw new IllegalArgumentException(
            "The gene at index " + i + " in the given array of " +
            "genes was found to be null. No gene in the array " +
            "may be null.");
      }
    }
    m_genes = a_initialGenes;
  }

  /**
   *
   * @param a_size
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public Chromosome(int a_size) {
    /**@todo add test case*/
    m_genes = new Gene[a_size];
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
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 1.0
   */
  public synchronized Object clone() {
    // Before doing anything, make sure that a Configuration object
    // has been set on this Chromosome. If not, then throw an
    // IllegalStateException.
    // ------------------------------------------------------------
    if (Genotype.getConfiguration() == null) {
      throw new IllegalStateException(
          "The active Configuration object must be set on this " +
          "Chromosome prior to invocation of the clone() method.");
    }
    // Now, first see if we can pull a Chromosome from the pool and just
    // set its gene values (alleles) appropriately.
    // ------------------------------------------------------------
    ChromosomePool pool = Genotype.getConfiguration().getChromosomePool();
    if (pool != null) {
      Chromosome copy = pool.acquireChromosome();
      if (copy != null) {
        Gene[] genes = copy.getGenes();
        for (int i = 0; i < genes.length; i++) {
          genes[i].setAllele(m_genes[i].getAllele());
        }
        // Also clone the IApplicationData object.
        // ---------------------------------------
        try {
          if (getApplicationData() != null) {
            if (getApplicationData() instanceof IApplicationData) {
              copy.setApplicationData( ( (IApplicationData) getApplicationData()).
                                      clone());
            }
          }
          return copy;
        }
        catch (CloneNotSupportedException cex) {
          // rethrow as IllegalStateException to be backward compatible and have
          // a more convenient handling
          throw new IllegalStateException(cex.getMessage());
        }
      }
    }
    // If we get this far, then we couldn't fetch a Chromosome from the
    // pool, so we need to create a new one. First we make a copy of each
    // of the Genes. We explicity use the Gene at each respective gene
    // location (locus) to create the new Gene that is to occupy that same
    // locus in the new Chromosome.
    // -------------------------------------------------------------------
    Gene[] copyOfGenes = new Gene[m_genes.length];
//    if (m_genes.length == 0 || copyOfGenes.length == 0) {
//      throw new IllegalArgumentException("Genes length = 0!");
//    }
    for (int i = 0; i < copyOfGenes.length; i++) {
      copyOfGenes[i] = m_genes[i].newGene();
      copyOfGenes[i].setAllele(m_genes[i].getAllele());
    }
    // Now construct a new Chromosome with the copies of the genes and
    // return it. Also clone the IApplicationData object.
    // ---------------------------------------------------------------
    try {
      Chromosome ret = new Chromosome(copyOfGenes);
      if (getApplicationData() != null) {
        /**@todo support Cloneable interface, i.e. look for public clone()
         * method via introspection
         */
        if (getApplicationData() instanceof IApplicationData) {
          IApplicationData clonedAppData = (IApplicationData) ( (
              IApplicationData)
              getApplicationData()).clone();
          /*
           if (clonedAppData == null || !clonedAppData.equals(getApplicationData())) {
                      throw new CloneNotSupportedException(
                          "ApplicationData object attached"
                          + " to Chromosome clones not "
                          + "correctly!");
                    }
           */
          ret.setApplicationData(clonedAppData);
        }
        else {
          // Application data object does not support cloning.
          // Therefor just copy the reference *uuumh*
          ret.setApplicationData(getApplicationData());
        }
      }
      return ret;
    }
    catch (CloneNotSupportedException cex) {
      // rethrow as RuntimeException to be backward compatible and have
      // a more convenient handling
      throw new IllegalStateException(cex.getMessage());
    }
  }

  /**
   * Returns the Gene at the given index (locus) within the Chromosome. The
   * first gene is at index zero and the last gene is at the index equal to
   * the size of this Chromosome - 1.
   *
   * @param a_desiredLocus The index of the gene value to be returned.
   * @return The Gene at the given index.
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public synchronized Gene getGene(int a_desiredLocus) {
    return m_genes[a_desiredLocus];
  }

  /**
   * Retrieves the set of genes that make up this Chromosome. This method
   * exists primarily for the benefit of GeneticOperators that require the
   * ability to manipulate Chromosomes at a low level.
   *
   * @return an array of the Genes contained within this Chromosome.
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public synchronized Gene[] getGenes() {
    return m_genes;
  }

  /**
   * Returns the size of this Chromosome (the number of genes it contains).
   * A Chromosome's size is constant and will never change.
   *
   * @return The number of genes contained within this Chromosome instance.
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public int size() {
    return m_genes.length;
  }

  /**
   * Retrieves the fitness value of this Chromosome, as determined by the
   * active fitness function. If a bulk fitness function is in use and
   * has not yet assigned a fitness value to this Chromosome, then -1 is
   * returned.
   *
   * @return a positive double value representing the fitness of this
   *         Chromosome, or -1 if a bulk fitness function is in use and has
   *         not yet assigned a fitness value to this Chromosome.
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 2.0 (until 1.1: return type int)
   */
  public double getFitnessValue() {
    if (m_fitnessValue >=0.000d) {
      return m_fitnessValue;
    }
    if (Genotype.getConfiguration() != null) {
      FitnessFunction normalFitnessFunction =
          Genotype.getConfiguration().getFitnessFunction();
      if (normalFitnessFunction != null) {
        // Grab the "normal" fitness function and ask it to calculate our
        // fitness value.
        // --------------------------------------------------------------
        m_fitnessValue = normalFitnessFunction.getFitnessValue(this);
      }
    }
    return m_fitnessValue;
  }

  /**
   * Sets the fitness value of this Chromosome. This method is for use
   * by bulk fitness functions and should not be invokved from anything
   * else (except test cases).
   *
   * @param a_newFitnessValue a positive integer representing the fitness
   *                          of this Chromosome.
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public void setFitnessValue(double a_newFitnessValue) {
    if (a_newFitnessValue >= 0 && m_fitnessValue != a_newFitnessValue) {
      m_fitnessValue = a_newFitnessValue;
    }
  }

  /**
   * Returns a string representation of this Chromosome, useful
   * for some display purposes.
   *
   * @return A string representation of this Chromosome.
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 1.0
   */
  public String toString() {
    StringBuffer representation = new StringBuffer();
    representation.append("[ ");
    // Append the representations of each of the gene Alleles.
    // -------------------------------------------------------
    for (int i = 0; i < m_genes.length - 1; i++) {
      representation.append(m_genes[i].toString());
      representation.append(", ");
    }
    representation.append(m_genes[m_genes.length - 1].toString());
    representation.append(" ]");
    return representation.toString();
    /**@todo what about the IApplicationData object?*/
  }

  /**
   * Convenience method that returns a new Chromosome instance with its
   * genes values (alleles) randomized. Note that, if possible, this method
   * will acquire a Chromosome instance from the active ChromosomePool
   * (if any) and then randomize its gene values before returning it. If a
   * Chromosome cannot be acquired from the pool, then a new instance will
   * be constructed and its gene values randomized before returning it.
   *
   * @return randomly initialized Chromosome
   * @throws InvalidConfigurationException if the given Configuration
   *         instance is invalid.
   * @throws IllegalArgumentException if the given Configuration instance
   *         is null.
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public static Chromosome randomInitialChromosome()
      throws InvalidConfigurationException {
    // Sanity check: make sure the given configuration isn't null.
    // -----------------------------------------------------------
    if (Genotype.getConfiguration() == null) {
      throw new IllegalArgumentException(
          "Configuration instance must not be null");
    }
    // Lock the configuration settings so that they can't be changed
    // from now on.
    // -------------------------------------------------------------
    Genotype.getConfiguration().lockSettings();
    // First see if we can get a Chromosome instance from the pool.
    // If we can, we'll randomize its gene values (alleles) and then
    // return it.
    // ------------------------------------------------------------
    ChromosomePool pool = Genotype.getConfiguration().getChromosomePool();
    if (pool != null) {
      Chromosome randomChromosome = pool.acquireChromosome();
      if (randomChromosome != null) {
        Gene[] genes = randomChromosome.getGenes();
        RandomGenerator generator = Genotype.getConfiguration().
            getRandomGenerator();
        for (int i = 0; i < genes.length; i++) {
          genes[i].setToRandomValue(generator);
        }
        return randomChromosome;
      }
    }
    // If we got this far, then we weren't able to get a Chromosome from
    // the pool, so we have to construct a new instance and build it from
    // scratch.
    // ------------------------------------------------------------------
    Chromosome sampleChromosome =
        Genotype.getConfiguration().getSampleChromosome();
    Gene[] sampleGenes = sampleChromosome.getGenes();
    Gene[] newGenes = new Gene[sampleGenes.length];
    RandomGenerator generator = Genotype.getConfiguration().getRandomGenerator();
    for (int i = 0; i < newGenes.length; i++) {
      // We use the newGene() method on each of the genes in the
      // sample Chromosome to generate our new Gene instances for
      // the Chromosome we're returning. This guarantees that the
      // new Genes are setup with all of the correct internal state
      // for the respective gene position they're going to inhabit.
      // -----------------------------------------------------------
      newGenes[i] = sampleGenes[i].newGene();
      // Set the gene's value (allele) to a random value.
      // ------------------------------------------------
      newGenes[i].setToRandomValue(generator);
    }
    // Finally, construct the new chromosome with the new random
    // genes values and return it.
    // ---------------------------------------------------------
    return new Chromosome(newGenes);
  }

  /**
   * Compares this Chromosome against the specified object. The result is
   * true if and the argument is an instance of the Chromosome class
   * and has a set of genes equal to this one.
   *
   * @param other The object to compare against.
   * @return true if the objects are the same, false otherwise.
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 1.0
   */
  public boolean equals(Object other) {
    // If class is not equal, return false. Therefor catch
    // ClasscastException's. The cleaner way (commented out below) would
    // be too slow, indeed.
    // -----------------------------------------------------------------
    /*
       if (other != null &&
        !this.getClass ().getName ().equals (other.getClass ().getName ()))
        {
            return false;
        }
     */
    try {
      return compareTo(other) == 0;
    }
    catch (ClassCastException cex) {
      return false;
    }
  }

  /**
   * Retrieve a hash code for this Chromosome. Does not considers the order
   * of the Genes for all cases (especially when gene is empty).
   *
   * @return the hash code of this Chromosome.
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 1.0
   */
  public int hashCode() {
    // Do what {@link java.util.AbstractList} does.
    // --------------------------------------------
    int geneHashcode;
    int hashCode = 1;
    for (int i = 0; i < m_genes.length; i++) {
      geneHashcode = m_genes[i].hashCode();
      hashCode = 31 * hashCode + geneHashcode;
    }
    return hashCode;
  }

  /**
   * Compares the given Chromosome to this Chromosome. This chromosome is
   * considered to be "less than" the given chromosome if it has a fewer
   * number of genes or if any of its gene values (alleles) are less than
   * their corresponding gene values in the other chromosome.
   *
   * @param other The Chromosome against which to compare this chromosome.
   * @return a negative number if this chromosome is "less than" the given
   *         chromosome, zero if they are equal to each other, and a positive
   *         number if this chromosome is "greater than" the given chromosome.
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 1.0
   */
  public int compareTo(Object other) {
    // First, if the other Chromosome is null, then this chromosome is
    // automatically the "greater" Chromosome.
    // ---------------------------------------------------------------
    if (other == null) {
      return 1;
    }
    Chromosome otherChromosome = (Chromosome) other;
    Gene[] otherGenes = otherChromosome.m_genes;
    // If the other Chromosome doesn't have the same number of genes,
    // then whichever has more is the "greater" Chromosome.
    // --------------------------------------------------------------
    if (otherGenes.length != m_genes.length) {
      return m_genes.length - otherGenes.length;
    }
    // Next, compare the gene values (alleles) for differences. If
    // one of the genes is not equal, then we return the result of its
    // comparison.
    // ---------------------------------------------------------------
    for (int i = 0; i < m_genes.length; i++) {
      int comparison = m_genes[i].compareTo(otherGenes[i]);
      if (comparison != 0) {
        return comparison;
      }
    }
    if (m_compareAppData) {
      // Compare application data
      // ------------------------
      if (getApplicationData() == null) {
        if (otherChromosome.getApplicationData() != null) {
          return -1;
        }
      }
      else if (otherChromosome.getApplicationData() == null) {
        return 1;
      }
      else {
        if (getApplicationData() instanceof Comparable) {
          try {
            return ( (Comparable) getApplicationData()).compareTo(
                otherChromosome.getApplicationData());
          }
          catch (ClassCastException cex) {
            /**@todo improve*/
            return -1;
          }
        }
        else {
          return getApplicationData().getClass().getName().compareTo(
              otherChromosome.getApplicationData().getClass().getName());
        }
      }
    }
    // Everything is equal. Return zero.
    // ---------------------------------
    return 0;
  }

  /**
   * Sets whether this Chromosome has been selected by the natural selector
   * to continue to the next generation or manually (e.g. via an add-method).
   *
   * @param a_isSelected true if this Chromosome has been selected, false
   *                     otherwise.
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public void setIsSelectedForNextGeneration(boolean a_isSelected) {
    m_isSelectedForNextGeneration = a_isSelected;
  }

  /**
   * Retrieves whether this Chromosome has been selected by the natural
   * selector to continue to the next generation.
   *
   * @return true if this Chromosome has been selected, false otherwise.
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public boolean isSelectedForNextGeneration() {
    return m_isSelectedForNextGeneration;
  }

  /**
   * Invoked when this Chromosome is no longer needed and should perform
   * any necessary cleanup. Note that this method will attempt to release
   * this Chromosome instance to the active ChromosomePool, if any.
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public void cleanup() {
    if (Genotype.getConfiguration() == null) {
      throw new IllegalStateException(
          "The active Configuration object must be set on this " +
          "Chromosome prior to invocation of the cleanup() method.");
    }
    // First, reset our internal state.
    // --------------------------------
    m_fitnessValue = Genotype.getConfiguration().getFitnessFunction().
        getNoFitnessValue();
    m_isSelectedForNextGeneration = false;
    // Next we want to try to release this Chromosome to a ChromosomePool
    // if one has been setup so that we can save a little time and memory
    // next time a Chromosome is needed.
    // ------------------------------------------------------------------
    // Now fetch the active ChromosomePool from the Configuration object
    // and, if the pool exists, release this Chromosome to it.
    // -----------------------------------------------------------------
    ChromosomePool pool = Genotype.getConfiguration().getChromosomePool();
    if (pool != null) {
      // Note that the pool will take care of any gene cleanup for us,
      // so we don't need to worry about it here.
      // -------------------------------------------------------------
      pool.releaseChromosome(this);
    }
    else {
      // No pool is available, so we need to finish cleaning up, which
      // basically entails requesting each of our genes to clean
      // themselves up as well.
      // -------------------------------------------------------------
      for (int i = 0; i < m_genes.length; i++) {
        m_genes[i].cleanup();
      }
    }
  }

  /**
   * This sets the application-specific data that is attached to this
   * Chromosome. Attaching application-specific data may be useful for
   * some applications when it comes time to evaluate this Chromosome
   * in the fitness function. JGAP ignores this data.
   *
   * @param a_newData The new application-specific data to attach to this
   *                  Chromosome.
   *
   * @author Neil Rotstan
   * @since 1.1
   */
  public void setApplicationData(Object a_newData) {
    m_applicationData = a_newData;
  }

  /**
   * Retrieves the application-specific data that is attached to this
   * Chromosome. Attaching application-specific data may be useful for
   * some applications when it comes time to evaluate this Chromosome
   * in the fitness function. JGAP ignores this data.
   *
   * @return The application-specific data previously attached to this
   *         Chromosome, or null if there is no attached data.
   *
   * @author Neil Rotstan
   * @since 1.1
   */
  public Object getApplicationData() {
    return m_applicationData;
  }

  public void setGenes(Gene[] a_genes) {
//    for (int i=0;i<a_genes.length;i++) {
//      if (a_genes[i]==null) {
//        throw new RuntimeException("Gene may not be null!");
//      }
//    }
    m_genes = a_genes;
  }

  /**
   * Should we also consider the application data when comparing? Default is
   * "false" as "true" means a Chromosome is losing its identity when
   * application data is set differently!
   *
   * @param a_doCompare true consider application data in method compareTo
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void setCompareApplicationData(boolean a_doCompare) {
    m_compareAppData = a_doCompare;
  }

  /*
   * @return should we also consider the application data when comparing?
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public boolean isCompareApplicationData() {
    return m_compareAppData;
  }
}
