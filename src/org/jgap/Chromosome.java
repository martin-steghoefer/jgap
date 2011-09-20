/**
 * JGAP offers a dual license model(see below for specific license information):
 * + The LGPL may be used anytime.
 * + The MPL may be used if at least $20 have been donated to the JGAP project
 *   thru PayPal (see http://www.sourceforge.net/projects/jgap or, directly,
 *   http://sourceforge.net/donate/index.php?group_id=11618).
 *   Details about usage of JGAP under the MPL can be found at the homepage
 *   http://jgap.sourceforge.net/.
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
 * The initial Developer of the Original Code is Neil Rotstan. Portions created
 * by the initial Developer are Copyright (C) 2002- 2003 by Neil Rotstan.
 * All Rights Reserved.
 * Co-developer of the code is Klaus Meffert. Portions created by the co-
 * developer are Copyright (C) 2003-2006 by Klaus Meffert. All Rights Reserved.
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

import java.lang.reflect.*;
import java.util.*;

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
    extends BaseChromosome {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.105 $";

  /**
   * Application-specific data that is attached to this Chromosome.
   * This data may assist the application in evaluating this Chromosome
   * in the fitness function. JGAP does not operate on the data, aside
   * from allowing it to be set and retrieved, and considering it with
   * comparations (if user opted in to do so).
   */
  private Object m_applicationData;

  /**
   * Holds multiobjective values.
   *
   * @since 2.6
   * @todo move to new subclass of Chromosome (and introduce new interface
   * IMultiObjective with that)
   */
  private List m_multiObjective;

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
   * comparing? Default is "false", as "true" means a Chromosome's losing its
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
   * If set to true the method getFitnessValue() will always (re-)calculate the
   * fitness value. This may be necessary in case of environments where the
   * state changes without the chromosome to notice
   *
   * @since 3.2.2
   */
  private boolean m_alwaysCalculate;

  /**
   * Default constructor, only provided for dynamic instantiation.<p>
   * Attention: The configuration used is the one set with the static method
   * Genotype.setConfiguration.
   *
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public Chromosome()
      throws InvalidConfigurationException {
    this(Genotype.getStaticConfiguration());
  }

  /**
   * Constructor, provided for dynamic or minimal instantiation.
   *
   * @param a_configuration the configuration to use
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public Chromosome(final Configuration a_configuration)
      throws InvalidConfigurationException {
    super(a_configuration);
    m_alwaysCalculate = a_configuration.isAlwaysCalculateFitness();
  }

  /**
   * Constructor, provided for instantiation via persistent representation.
   *
   * @param a_configuration the configuration to use
   * @param a_persistentRepresentatuion valid persistent representation that
   * was most likely obtained via getPersistentRepresentation()
   * @throws InvalidConfigurationException
   * @throws UnsupportedRepresentationException
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public Chromosome(final Configuration a_configuration,
                    String a_persistentRepresentatuion)
      throws InvalidConfigurationException, UnsupportedRepresentationException {
    this(a_configuration);
    setValueFromPersistentRepresentation(a_persistentRepresentatuion);
  }

  /**
   * Constructor for specifying the number of genes.
   *
   * @param a_configuration the configuration to use
   * @param a_desiredSize number of genes the chromosome contains of
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public Chromosome(final Configuration a_configuration,
                    final int a_desiredSize)
      throws InvalidConfigurationException {
    this(a_configuration);
    if (a_desiredSize <= 0) {
      throw new IllegalArgumentException(
          "Chromosome size must be greater than zero");
    }
    setGenes(new Gene[a_desiredSize]);
  }

  /**
   * Constructs a Chromosome of the given size separate from any specific
   * Configuration. This constructor will use the given sample Gene to
   * construct a new Chromosome instance containing genes all of the same
   * type as the sample Gene. This can be useful for constructing sample
   * chromosomes that use the same Gene type for all of their genes and that
   * are to be used to setup a Configuration object.
   *
   * @param a_configuration the configuration to use
   * @param a_sampleGene a concrete sampleGene instance that will be used
   * as a template for all of the genes in this Chromosome
   * @param a_desiredSize the desired size (number of genes) of this Chromosome
   * @throws InvalidConfigurationException
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 1.0
   */
  public Chromosome(final Configuration a_configuration,
                    final Gene a_sampleGene, final int a_desiredSize)
      throws InvalidConfigurationException {
    this(a_configuration, a_desiredSize);
    initFromGene(a_sampleGene);
  }

  public Chromosome(final Configuration a_configuration, Gene a_sampleGene,
                    int a_desiredSize,
                    IGeneConstraintChecker a_constraintChecker)
      throws InvalidConfigurationException {
    this(a_configuration, a_desiredSize);
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
    int size = size();
    for (int i = 0; i < size; i++) {
      setGene(i, a_sampleGene.newGene());
    }
  }

  /**
   * Constructs a Chromosome separate from any specific Configuration. This
   * can be useful for constructing sample chromosomes that are to be used
   * to setup a Configuration object.
   *
   * @param a_configuration the configuration to use
   * @param a_initialGenes the initial genes of this Chromosome
   * @throws InvalidConfigurationException
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public Chromosome(final Configuration a_configuration, Gene[] a_initialGenes)
      throws InvalidConfigurationException {
    this(a_configuration, a_initialGenes == null ? 0 : a_initialGenes.length);
    checkGenes(a_initialGenes);
    setGenes(a_initialGenes);
  }

  /**
   * Constructs a Chromosome separate from any specific Configuration. This
   * can be useful for constructing sample chromosomes that are to be used
   * to setup a Configuration object. Additionally, a constraint checker can be
   * specified. It is used right here to verify the validity of the gene types
   * supplied.
   *
   * @param a_configuration the configuration to use
   * @param a_initialGenes the initial genes of this Chromosome
   * @param a_constraintChecker constraint checker to use
   * @throws InvalidConfigurationException in case the constraint checker
   * reports a configuration error
   *
   * @author Klaus Meffert
   * @since 2.5
   */
  public Chromosome(final Configuration a_configuration, Gene[] a_initialGenes,
                    IGeneConstraintChecker a_constraintChecker)
      throws InvalidConfigurationException {
    this(a_configuration, a_initialGenes.length);
    checkGenes(a_initialGenes);
    setGenes(a_initialGenes);
    setConstraintChecker(a_constraintChecker);
  }

  /**
   * Helper: called by constructors only to verify the initial genes.
   *
   * @param a_initialGenes the initial genes of this Chromosome to verify
   *
   * @author Klaus Meffert
   * @since 2.5
   */
  protected void checkGenes(Gene[] a_initialGenes) {
    // Sanity checks: make sure the genes array isn't null and
    // that none of the genes contained within it are null.
    // Check against null already done in constructors!
    // -------------------------------------------------------
    for (int i = 0; i < a_initialGenes.length; i++) {
      if (a_initialGenes[i] == null) {
        throw new IllegalArgumentException(
            "The gene at index " + i + " in the given array of " +
            "genes was found to be null. No gene in the array " +
            "may be null.");
      }
    }
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
    if (getConfiguration() == null) {
      throw new IllegalStateException(
          "The active Configuration object must be set on this " +
          "Chromosome prior to invocation of the clone() method.");
    }
    IChromosome copy = null;
    // Now, first see if we can pull a Chromosome from the pool and just
    // set its gene values (alleles) appropriately.
    // ------------------------------------------------------------
    IChromosomePool pool = getConfiguration().getChromosomePool();
    if (pool != null) {
      copy = pool.acquireChromosome();
      if (copy != null) {
        Gene[] genes = copy.getGenes();
        for (int i = 0; i < size(); i++) {
          genes[i].setAllele(getGene(i).getAllele());
        }
      }
    }
    try {
      if (copy == null) {
        // We couldn't fetch a Chromosome from the pool, so we need to create
        // a new one. First we make a copy of each of the Genes. We explicity
        // use the Gene at each respective gene location (locus) to create the
        // new Gene that is to occupy that same locus in the new Chromosome.
        // -------------------------------------------------------------------
        int size = size();
        if (size > 0) {
          Gene[] copyOfGenes = new Gene[size];
          for (int i = 0; i < size; i++) {
            copyOfGenes[i] = getGene(i).newGene();
            Object allele = getGene(i).getAllele();
            if (allele != null) {
              IJGAPFactory factory = getConfiguration().getJGAPFactory();
              if (factory != null) {
                ICloneHandler cloner = factory.
                    getCloneHandlerFor(allele, allele.getClass());
                if (cloner != null) {
                  try {
                    allele = cloner.perform(allele, null, this);
                  } catch (Exception ex) {
                    throw new RuntimeException(ex);
                  }
                }
                else {
                  /**@todo once output a warning: allele should be cloneable!*/
                }
              }
            }
            copyOfGenes[i].setAllele(allele);
          }
          // Now construct a new Chromosome with the copies of the genes and
          // return it. Also clone the IApplicationData object later on.
          // ---------------------------------------------------------------
          if (getClass() == Chromosome.class) {
            copy = new Chromosome(getConfiguration(), copyOfGenes);
          }
          else {
            try {
              // Try dynamic call of constructor. Attention: This may not
              // work for inner classes!
              // --------------------------------------------------------
              Constructor[] constr = getClass().getDeclaredConstructors();
              if (constr != null) {
                for (int i = 0; i < constr.length; i++) {
                  Class[] params = constr[i].getParameterTypes();
                  if (params != null && params.length == 1) {
                    if (params[0] == Configuration.class) {
                      copy = (IChromosome) constr[i].newInstance(new Object[] {
                          getConfiguration()});
                      copy.setGenes(copyOfGenes);
                    }
                  }
                }
              }
              if (copy == null) {
                // Enforce alternative cloning to get at least something.
                // ------------------------------------------------------
                throw new Exception(
                    "No appropriate constructor for cloning found.");
              }
            } catch (Exception ex) {
              ex.printStackTrace();
              // Do it the old way with the danger of getting a stack overflow.
              // --------------------------------------------------------------
              copy = (IChromosome) getConfiguration().getSampleChromosome().clone();
              copy.setGenes(copyOfGenes);
            }
          }
        }
        else {
          if (getClass() == Chromosome.class) {
            copy = new Chromosome(getConfiguration());
          }
          else {
            copy = (IChromosome) getConfiguration().getSampleChromosome().clone();
          }
        }
      }
      copy.setFitnessValue(m_fitnessValue);
      // Clone constraint checker.
      // -------------------------
      copy.setConstraintChecker(getConstraintChecker());
    } catch (InvalidConfigurationException iex) {
      throw new IllegalStateException(iex.getMessage());
    }
    // Also clone the IApplicationData object.
    // ---------------------------------------
    try {
      copy.setApplicationData(cloneObject(getApplicationData()));
    } catch (Exception ex) {
      throw new IllegalStateException(ex.getMessage());
    }
    // Clone multi-objective object if necessary and possible.
    // -------------------------------------------------------
    if (m_multiObjective != null) {
      if (getClass() == Chromosome.class) {
        try {
          ( (Chromosome) copy).setMultiObjectives( (List) cloneObject(
              m_multiObjective));
        } catch (Exception ex) {
          throw new IllegalStateException(ex.getMessage());
        }
      }
    }
    return copy;
  }

  /**
   * Clones an object by using clone handlers. If no deep cloning possible, then
   * return the reference.
   *
   * @param a_object the object to clone
   * @return the cloned object, or the object itself if no coning supported
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  protected Object cloneObject(Object a_object)
      throws Exception {
    return cloneObject(getConfiguration(), a_object, this);
  }

  /**
   * Static convenience method.
   * Clones an object by using clone handlers. If no deep cloning possible, then
   * return the reference.
   *
   * @param a_config a valid configuration to obtain the JGAPFactory from
   * @param a_object the object to clone
   * @param a_master the super object of a_object, e.g. a chromosome in case of
   * application to be cloned
   * @return the cloned object, or the object itself if no coning supported
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.5
   */
  public static Object cloneObject(Configuration a_config, Object a_object,
                                   Object a_master)
      throws Exception {
    if (a_object == null) {
      return null;
    }
    // Try to clone via a registered clone handler.
    // --------------------------------------------
    ICloneHandler cloner = a_config.getJGAPFactory().
        getCloneHandlerFor(a_object, a_object.getClass());
    if (cloner != null) {
      return cloner.perform(a_object, null, a_master);
    }
    else {
      // No cloning supported, so just return the reference.
      // ---------------------------------------------------
      return a_object;
    }
  }

  /**
   * Retrieves the fitness value of this Chromosome, as determined by the
   * active fitness function. If a bulk fitness function is in use and
   * has not yet assigned a fitness value to this Chromosome, then -1 is
   * returned.<p>
   * Attention: should not be called from toString() as the fitness value would
   * be computed if it was initial!
   *
   * @return a positive double value representing the fitness of this
   * Chromosome, or -1 if a bulk fitness function is in use and has not yet
   * assigned a fitness value to this Chromosome
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 2.0
   */
  public double getFitnessValue() {
    if (m_fitnessValue >= 0.000d && !m_alwaysCalculate) {
      return m_fitnessValue;
    }
    else {
      return calcFitnessValue();
    }
  }

  /**
   * @return the lastly computed fitness value, or FitnessFunction.NO_FITNESS_VALUE
   * in case no value has been computed yet.
   *
   * @author Klaus Meffert
   */
  public double getFitnessValueDirectly() {
    return m_fitnessValue;
  }

  /**
   * @return fitness value of this chromosome determined via the registered
   * fitness function
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  protected double calcFitnessValue() {
    if (getConfiguration() != null) {
      FitnessFunction normalFitnessFunction = getConfiguration().
          getFitnessFunction();
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
   * by bulk fitness functions and should not be invoked from anything
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
   * Sets the fitness value of this Chromosome directly without any
   * constraint checks, conversions or checks. Only use if you know what
   * you do.
   *
   * @param a_newFitnessValue a positive integer representing the fitness
   * of this Chromosome
   *
   * @author Klaus Meffert
   */
  public void setFitnessValueDirectly(double a_newFitnessValue) {
    m_fitnessValue = a_newFitnessValue;
  }

  /**
   * @return a string representation of this Chromosome, useful for display
   * purposes
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 1.0
   */
  public String toString() {
    StringBuffer representation = new StringBuffer();
    representation.append(S_SIZE + ":" + size());
    // Don't use getFitnessValue() here as it would then be initialized if
    // it was not. We want to capture the current state here!
    // -------------------------------------------------------------------
    representation.append(", " + S_FITNESS_VALUE + ":" + m_fitnessValue);
    representation.append(", " + S_ALLELES + ":");
    representation.append("[");
    // Append the representations of each of the genes' alleles.
    // ---------------------------------------------------------
    int size = size();
    for (int i = 0; i < size; i++) {
      if (i > 0) {
        representation.append(", ");
      }
      if (getGene(i) == null) {
        representation.append("null");
      }
      else {
        representation.append(getGene(i).toString());
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
   * @param a_configuration the configuration to use
   * @return randomly initialized Chromosome
   * @throws InvalidConfigurationException if the given Configuration
   * instance is invalid
   * @throws IllegalArgumentException if the given Configuration instance
   * is null
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 1.0
   */
  public static IChromosome randomInitialChromosome(
      Configuration a_configuration)
      throws InvalidConfigurationException {
    // Sanity check: make sure the given configuration isn't null.
    // -----------------------------------------------------------
    if (a_configuration == null) {
      throw new IllegalArgumentException(
          "Configuration instance must not be null");
    }
    // Lock the configuration settings so that they can't be changed
    // from now on.
    // -------------------------------------------------------------
    a_configuration.lockSettings();
    // First see if we can get a Chromosome instance from the pool.
    // If we can, we'll randomize its gene values (alleles) and then
    // return it.
    // -------------------------------------------------------------
    IChromosomePool pool = a_configuration.getChromosomePool();
    if (pool != null) {
      IChromosome randomChromosome = pool.acquireChromosome();
      if (randomChromosome != null) {
        Gene[] genes = randomChromosome.getGenes();
        RandomGenerator generator = a_configuration.getRandomGenerator();
        for (int i = 0; i < genes.length; i++) {
          genes[i].setToRandomValue(generator);
          /**@todo what about Gene's energy?*/
        }
        randomChromosome.setFitnessValueDirectly(FitnessFunction.
            NO_FITNESS_VALUE);
        return randomChromosome;
      }
    }
    // We weren't able to get a Chromosome from the pool, so we have to
    // construct a new instance and build it from scratch.
    // ------------------------------------------------------------------
    IChromosome sampleChromosome =
        a_configuration.getSampleChromosome();
    sampleChromosome.setFitnessValue(FitnessFunction.NO_FITNESS_VALUE);
    Gene[] sampleGenes = sampleChromosome.getGenes();
    Gene[] newGenes = new Gene[sampleGenes.length];
    RandomGenerator generator = a_configuration.getRandomGenerator();
    for (int i = 0; i < newGenes.length; i++) {
      // We use the newGene() method on each of the genes in the
      // sample Chromosome to generate our new Gene instances for
      // the Chromosome we're returning. This guarantees that the
      // new Genes are setup with all of the correct internal state
      // for the respective gene position they're going to inhabit.
      // -----------------------------------------------------------
      newGenes[i] = sampleGenes[i].newGene();
      // If application data is set, try to clone it as well.
      // ----------------------------------------------------
      Object appData = sampleGenes[i].getApplicationData();
      if(appData != null) {
        try {
          cloneObject(a_configuration, appData, sampleChromosome);
        } catch (Exception ex) {
          throw new InvalidConfigurationException("Application data of "
              +"sample chromsome is not cloneable",ex);
        }
      }
      // Set the gene's value (allele) to a random value.
      // ------------------------------------------------
      newGenes[i].setToRandomValue(generator);
      /**@todo what about Gene's energy?*/
    }
    // Finally, construct the new chromosome with the new random
    // genes values and return it.
    // ---------------------------------------------------------
      return new Chromosome(a_configuration, newGenes);
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
    } catch (ClassCastException cex) {
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
    // Do what java.util.AbstractList does.
    // ------------------------------------
    int geneHashcode;
    int hashCode = 1;
    if (getGenes() != null) {
      int size = size();
      for (int i = 0; i < size; i++) {
        Gene gene = getGene(i);
        if (gene == null) {
          geneHashcode = -55;
        }
        else {
          geneHashcode = gene.hashCode();
        }
        hashCode = 31 * hashCode + geneHashcode;
      }
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
    IChromosome otherChromosome = (IChromosome) other;
    Gene[] otherGenes = otherChromosome.getGenes();
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
      int comparison = getGene(i).compareTo(otherGenes[i]);
      if (comparison != 0) {
        return comparison;
      }
    }
    // Compare current fitness value.
    // ------------------------------
    if (m_fitnessValue != otherChromosome.getFitnessValueDirectly()) {
      FitnessEvaluator eval = getConfiguration().getFitnessEvaluator();
      if (eval != null) {
        if (eval.isFitter(m_fitnessValue,
                          otherChromosome.getFitnessValueDirectly())) {
          return 1;
        }
        else {
          return -1;
        }
      }
      else {
        // undetermined order, but unequal!
        // --------------------------------
        return -1;
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
          } catch (ClassCastException cex) {
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
    if (getConfiguration() == null) {
      throw new IllegalStateException(
          "The active Configuration object must be set on this " +
          "Chromosome prior to invocation of the cleanup() method.");
    }
    // First, reset our internal state.
    // --------------------------------
    m_fitnessValue = getConfiguration().getFitnessFunction().
        NO_FITNESS_VALUE;
    m_isSelectedForNextGeneration = false;
    // Next we want to try to release this Chromosome to a ChromosomePool
    // if one has been setup so that we can save a little time and memory
    // next time a Chromosome is needed.
    // ------------------------------------------------------------------
    // Now fetch the active ChromosomePool from the Configuration object
    // and, if the pool exists, release this Chromosome to it.
    // -----------------------------------------------------------------
    IChromosomePool pool = getConfiguration().getChromosomePool();
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
        getGene(i).cleanup();
      }
    }
  }

  /**
   * This sets the application-specific data that is attached to this
   * Chromosome. Attaching application-specific data may be useful for
   * some applications when it comes time to evaluate this Chromosome
   * in the fitness function. JGAP ignores this data, except for cloning and
   * comparison (latter only if opted in via setCompareApplicationData(..))
   *
   * @param a_newData the new application-specific data to attach to this
   * Chromosome. Should be an instance of IApplicationData
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
  public void setGenes(Gene[] a_genes)
      throws InvalidConfigurationException {
    super.setGenes(a_genes);
    verify(getConstraintChecker());
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
    verify(a_constraintChecker);
    m_geneAlleleChecker = a_constraintChecker;
  }

  /**
   * @return IGeneConstraintChecker the constraint checker to be used whenever
   * method setGenes(Gene[]) is called.
   *
   * @author Klaus Meffert
   * @since 2.5
   */
  public IGeneConstraintChecker getConstraintChecker() {
    return m_geneAlleleChecker;
  }

  /**
   * Verifies the state of the chromosome. Especially takes care of the
   * given constraint checker.
   * @param a_constraintChecker the constraint checker to verify
   *
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 2.5
   */
  protected void verify(IGeneConstraintChecker a_constraintChecker)
      throws InvalidConfigurationException {
    if (a_constraintChecker != null && getGenes() != null) {
      int len = getGenes().length;
      for (int i = 0; i < len; i++) {
        Gene gene = getGene(i);
        if (!a_constraintChecker.verify(gene, null, this, i)) {
          throw new InvalidConfigurationException(
              "The gene type "
              + gene.getClass().getName()
              + " is not allowed to be used in the chromosome due to the"
              + " constraint checker used.");
        }
      }
    }
  }

  // ------------------------------------
  // Begin of IInitializer implementation
  // ------------------------------------

  /**{@inheritDoc}*/
  public boolean isHandlerFor(Object a_obj, Class a_class) {
    if (a_class == Chromosome.class) {
      return true;
    }
    else {
      return false;
    }
  }

  /**{@inheritDoc}*/
  public Object perform(Object a_obj, Class a_class, Object a_params)
      throws Exception {
    return randomInitialChromosome(getConfiguration());
  }

  // ----------------------------------
  // End of IInitializer implementation
  // ----------------------------------
  public void setMultiObjectives(List a_values) {
    if (m_multiObjective == null) {
      m_multiObjective = new Vector();
    }
    m_multiObjective.clear();
    m_multiObjective.addAll(a_values);
  }

  public List getMultiObjectives() {
    return m_multiObjective;
  }
}
