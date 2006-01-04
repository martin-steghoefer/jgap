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
  private final static String CVS_REVISION = "$Revision: 1.55 $";

  public static final double DELTA = 0.000000001d;

  /**
   * Constants for toString()
   */
  public final static String S_FITNESS_VALUE = "Fitness value";

  public final static String S_ALLELES = "Alleles";

  public final static String S_APPLICATION_DATA = "Application data";

  public final static String S_SIZE = "Size";

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
  private boolean m_isSelectedForNextGeneration;

  /**
   * Stores the fitness value of this Chromosome as determined by the
   * active fitness function. A value of -1 indicates that this field
   * has not yet been set with this Chromosome's fitness values (valid
   * fitness values are always positive).
   *
   * @since 2.0 (until 1.1: type int)
   */
  protected double m_fitnessValue = FitnessFunction.NO_FITNESS_VALUE;

  /**
   * Method compareTo(): Should we also consider the application data when
   * comparing? Default is "false" as "true" means a Chromosome's losing its
   * identity when application data is set differently!
   *
   * @since 2.2
   */
  private boolean m_compareAppData;

  /**
   * Optional helper class for checking if a given allele value to be set
   * for a given gene is valid. If not, the allele value may not be set for the
   * gene or the gene type (e.g. IntegerGene) is not allowed in general!
   *
   * @since 2.5
   */
  private IGeneConstraintChecker m_geneAlleleChecker;

  /**
   * Default constructor
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public Chromosome() {
  }

  /**
   * Constructor for specifying the number of genes
   * @param a_desiredSize number of genes the chromosome contains of
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public Chromosome(int a_desiredSize) {
    if (a_desiredSize <= 0) {
      throw new IllegalArgumentException(
          "Chromosome size must be greater than zero");
    }
    m_genes = new Gene[a_desiredSize];
  }

  /**
   * Constructs a Chromosome of the given size separate from any specific
   * Configuration. This constructor will use the given sample Gene to
   * construct a new Chromosome instance containing genes all of the same
   * type as the sample Gene. This can be useful for constructing sample
   * chromosomes that use the same Gene type for all of their genes and that
   * are to be used to setup a Configuration object.
   *
   * @param a_sampleGene a concrete sampleGene instance that will be used
   * as a template for all of the genes in this Chromosome
   * @param a_desiredSize the desired size (number of genes) of this Chromosome
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 1.0
   */
  public Chromosome(Gene a_sampleGene, int a_desiredSize) {
    this(a_desiredSize);
    initFromGene(a_sampleGene);
  }

  public Chromosome(Gene a_sampleGene, int a_desiredSize,
                    IGeneConstraintChecker a_constraintChecker)
      throws InvalidConfigurationException {
   this(a_desiredSize);
   initFromGene(a_sampleGene);
   setConstraintChecker(a_constraintChecker);
  }

  protected void initFromGene(Gene a_sampleGene) {
    // Do sanity checking to make sure the parameters we were
    // given are valid.
    // ------------------------------------------------------
    if (a_sampleGene == null) {
      throw new IllegalArgumentException(
          "Sample Gene cannot be null.");
    }
    // Populate the array of genes it with new Gene instances
    // created from the sample gene.
    // ------------------------------------------------------
    for (int i = 0; i < m_genes.length; i++) {
      m_genes[i] = a_sampleGene.newGene();
    }
  }

  /**
   * Constructs a Chromosome separate from any specific Configuration. This
   * can be useful for constructing sample chromosomes that are to be used
   * to setup a Configuration object.
   *
   * @param a_initialGenes the initial genes of this Chromosome
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public Chromosome(Gene[] a_initialGenes) {
    initWithGenes(a_initialGenes);
  }

  /**
   * Constructs a Chromosome separate from any specific Configuration. This
   * can be useful for constructing sample chromosomes that are to be used
   * to setup a Configuration object. Additionally, a constraint checker can be
   * specified. It is used right here to verify the validity of the gene types
   * supplied.
   *
   * @param a_initialGenes the initial genes of this Chromosome
   * @param a_constraintChecker constraint checker to use
   * @throws InvalidConfigurationException in case the constraint checker
   * reports a configuration error
   *
   * @author Klaus Meffert
   * @since 2.5
   */
  public Chromosome(Gene[] a_initialGenes,
                    IGeneConstraintChecker a_constraintChecker)
      throws InvalidConfigurationException {
    initWithGenes(a_initialGenes);
    setConstraintChecker(a_constraintChecker);
  }

  /**
   * Helper: called by constructors only
   * @param a_initialGenes the initial genes of this Chromosome
   *
   * @author Klaus Meffert
   * @since 2.5
   */
  protected void initWithGenes(Gene[] a_initialGenes) {
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
   * Returns a copy of this Chromosome. The returned instance can evolve
   * independently of this instance. Note that, if possible, this method
   * will first attempt to acquire a Chromosome instance from the active
   * ChromosomePool (if any) and set its value appropriately before
   * returning it. If that is not possible, then a new Chromosome instance
   * will be constructed and its value set appropriately before returning.
   *
   * @return copy of this Chromosome
   * @throws IllegalStateException instead of CloneNotSupportedException
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
    IChromosomePool pool = Genotype.getConfiguration().getChromosomePool();
    if (pool != null) {
      Chromosome copy = pool.acquireChromosome();
      if (copy != null) {
        Gene[] genes = copy.getGenes();
        for (int i = 0; i < size(); i++) {
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
          // Reset fitness value
          // -------------------
          copy.m_fitnessValue = FitnessFunction.NO_FITNESS_VALUE;
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
    Gene[] copyOfGenes = new Gene[size()];
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
          // Therefor just copy the reference *uuumh*.
          // -------------------------------------------------
          ret.setApplicationData(getApplicationData());
        }
      }
      // Reset fitness value
      // -------------------
      ret.m_fitnessValue = FitnessFunction.NO_FITNESS_VALUE;
      return ret;
    }
    catch (CloneNotSupportedException cex) {
      // rethrow as IllegalStateException to be backward compatible and have
      // a more convenient handling.
      // -------------------------------------------------------------------
      throw new IllegalStateException(cex.getMessage());
    }
  }

  /**
   * Returns the Gene at the given index (locus) within the Chromosome. The
   * first gene is at index zero and the last gene is at the index equal to
   * the size of this Chromosome - 1.
   *
   * @param a_desiredLocus index of the gene value to be returned
   * @return Gene at the given index
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
   * @return an array of the Genes contained within this Chromosome
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public synchronized Gene[] getGenes() {
    return m_genes;
  }

  /**
   * Returns the size of this Chromosome (the number of genes it contains).
   * A Chromosome's size is constant and will not change, until setGenes(...)
   * is used.
   *
   * @return number of genes contained within this Chromosome instance
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 1.0
   */
  public int size() {
    if (m_genes == null) {
      // only possible when using default constructor
      return 0;
    }
    else {
      return m_genes.length;
    }
  }

  /**
   * Retrieves the fitness value of this Chromosome, as determined by the
   * active fitness function. If a bulk fitness function is in use and
   * has not yet assigned a fitness value to this Chromosome, then -1 is
   * returned.
   *
   * @return a positive double value representing the fitness of this
   * Chromosome, or -1 if a bulk fitness function is in use and has not yet
   * assigned a fitness value to this Chromosome
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 2.0 (until 1.1: return type int)
   */
  public double getFitnessValue() {
    if (m_fitnessValue >= 0.000d) {
      return m_fitnessValue;
    }
    else {
      return calcFitnessValue();
    }
  }

  /**
   * @return fitness value of this chromosome determined via the registered
   * fitness function
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  protected double calcFitnessValue() {
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
   * of this Chromosome
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public void setFitnessValue(double a_newFitnessValue) {
    if (a_newFitnessValue >= 0 &&
        Math.abs(m_fitnessValue - a_newFitnessValue) > 0.0000001) {
      m_fitnessValue = a_newFitnessValue;
    }
  }

  /**
   * @return a string representation of this Chromosome, useful
   * for display purposes.
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 1.0
   */
  public String toString() {
    StringBuffer representation = new StringBuffer();
    representation.append(S_SIZE + ":" + size());
    representation.append(", " + S_FITNESS_VALUE + ":" + getFitnessValue());
    representation.append(", " + S_ALLELES + ":");
    representation.append("[");
    // Append the representations of each of the gene Alleles.
    // -------------------------------------------------------
    for (int i = 0; i < m_genes.length; i++) {
      if (i > 0) {
        representation.append(", ");
      }
      if (m_genes[i] == null) {
        representation.append("null");
      }
      else {
        representation.append(m_genes[i].toString());
      }
    }
    representation.append("]");
    String appData;
    if (getApplicationData() != null) {
      appData = getApplicationData().toString();
    }
    else {
      appData = "null";
    }
    representation.append(", " + S_APPLICATION_DATA + ":" + appData);
    return representation.toString();
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
   * instance is invalid
   * @throws IllegalArgumentException if the given Configuration instance
   * is null
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
    IChromosomePool pool = Genotype.getConfiguration().getChromosomePool();
    if (pool != null) {
      Chromosome randomChromosome = pool.acquireChromosome();
      if (randomChromosome != null) {
        Gene[] genes = randomChromosome.getGenes();
        RandomGenerator generator = Genotype.getConfiguration().
            getRandomGenerator();
        for (int i = 0; i < genes.length; i++) {
          genes[i].setToRandomValue(generator);
        }
        randomChromosome.m_fitnessValue = FitnessFunction.NO_FITNESS_VALUE;
        return randomChromosome;
      }
    }
    // If we got this far, then we weren't able to get a Chromosome from
    // the pool, so we have to construct a new instance and build it from
    // scratch.
    // ------------------------------------------------------------------
    Chromosome sampleChromosome =
        Genotype.getConfiguration().getSampleChromosome();
    sampleChromosome.m_fitnessValue = FitnessFunction.NO_FITNESS_VALUE;
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
   * @param other the object to compare against
   * @return true: if the objects are the same, false otherwise
   *
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
   * @return the hash code of this Chromosome
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
   * @param other the Chromosome against which to compare this chromosome
   * @return a negative number if this chromosome is "less than" the given
   * chromosome, zero if they are equal to each other, and a positive number if
   * this chromosome is "greater than" the given chromosome
   *
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
    int size = size();
    Chromosome otherChromosome = (Chromosome) other;
    Gene[] otherGenes = otherChromosome.m_genes;
    // If the other Chromosome doesn't have the same number of genes,
    // then whichever has more is the "greater" Chromosome.
    // --------------------------------------------------------------
    if (otherChromosome.size() != size) {
      return size() - otherChromosome.size();
    }
    // Next, compare the gene values (alleles) for differences. If
    // one of the genes is not equal, then we return the result of its
    // comparison.
    // ---------------------------------------------------------------
    for (int i = 0; i < size; i++) {
      int comparison = m_genes[i].compareTo(otherGenes[i]);
      if (comparison != 0) {
        return comparison;
      }
    }
    if (m_compareAppData) {
      // Compare application data.
      // -------------------------
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
   * otherwise
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
   * @return true if this Chromosome has been selected, false otherwise
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
        NO_FITNESS_VALUE;
    m_isSelectedForNextGeneration = false;
    // Next we want to try to release this Chromosome to a ChromosomePool
    // if one has been setup so that we can save a little time and memory
    // next time a Chromosome is needed.
    // ------------------------------------------------------------------
    // Now fetch the active ChromosomePool from the Configuration object
    // and, if the pool exists, release this Chromosome to it.
    // -----------------------------------------------------------------
    IChromosomePool pool = Genotype.getConfiguration().getChromosomePool();
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
      for (int i = 0; i < size(); i++) {
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
   * @param a_newData the new application-specific data to attach to this
   * Chromosome
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
   * in the fitness function. JGAP ignores this data functionally.
   *
   * @return the application-specific data previously attached to this
   * Chromosome, or null if there is no data attached
   *
   * @author Neil Rotstan
   * @since 1.1
   */
  public Object getApplicationData() {
    return m_applicationData;
  }

  /**
   * Sets the genes for the chromosome.
   * @param a_genes the genes to set for the chromosome
   *
   * @throws InvalidConfigurationException in case constraint checker is
   * provided
   *
   * @author Klaus Meffert
   */
  public void setGenes(Gene[] a_genes) throws InvalidConfigurationException{
//    for (int i=0;i<a_genes.length;i++) {
//      if (a_genes[i]==null) {
//        throw new RuntimeException("Gene may not be null!");
//      }
//    }
    m_genes = a_genes;
    verify();
  }

  /**
   * Should we also consider the application data when comparing? Default is
   * "false" as "true" means a Chromosome is losing its identity when
   * application data is set differently!
   *
   * @param a_doCompare true: consider application data in method compareTo
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

  /**
   * Sets the constraint checker to be used for this gene whenever method
   * setAllele(Object) is called.
   *
   * @param a_constraintChecker the constraint checker to be set
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 2.5
   */
  public void setConstraintChecker(IGeneConstraintChecker a_constraintChecker)
      throws InvalidConfigurationException {
    m_geneAlleleChecker = a_constraintChecker;
    verify();
  }

  /**
   * @return IGeneConstraintChecker the constraint checker to be used whenever
   * method setAllele(Object) is called.
   *
   * @author Klaus Meffert
   * @since 2.5
   */
  public IGeneConstraintChecker getConstraintChecker() {
    return m_geneAlleleChecker;
  }

  /**
   * Verifies the state of the chromosome. Especially takes care of the
   * constraint checker set (if any).
   *
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 2.5
   */
  protected void verify()
      throws InvalidConfigurationException {
    if (getConstraintChecker() != null) {
      int len = getGenes().length;
      for (int i = 0; i < len; i++) {
        Gene gene = getGene(i);
        if (!getConstraintChecker().verify(gene, null)) {
          throw new InvalidConfigurationException("The gene type "
                                                  + gene.getClass().getName()
                                                  +
                                                  " is not allowed to be used in"
                                                  +
                                                  " the chromosome due to the"
                                                  + " constraint checker used.");
        }
      }
    }
  }
}
