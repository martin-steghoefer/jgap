/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap;

import java.io.*;
import org.jgap.util.*;

/**
 * Interface for chromosomes.
 * Normally, you would start using BaseChromosome which implements this
 * interface to build your own chromosome classes.
 *
 * @author Klaus Meffert
 * @since 2.6
 */
public interface IChromosome
    extends Comparable, ICloneable, Serializable, IUniqueKey {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.17 $";

  /**
   * Constants for toString()
   */
  public final static String S_FITNESS_VALUE = "Fitness value";

  public final static String S_ALLELES = "Alleles";

  public final static String S_APPLICATION_DATA = "Application data";

  public final static String S_SIZE = "Size";

  /**
   * Returns the Gene at the given index (locus) within the Chromosome. The
   * first gene is at index zero and the last gene is at the index equal to
   * the size of this Chromosome - 1.
   *
   * @param a_desiredLocus index of the gene value to be returned
   * @return Gene at the given index
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 2.6
   */
  Gene getGene(int a_desiredLocus);

  /**
   * Retrieves the set of genes that make up this Chromosome. This method
   * exists primarily for the benefit of GeneticOperators that require the
   * ability to manipulate Chromosomes at a low level.
   *
   * @return an array of the Genes contained within this Chromosome
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 2.6
   */
  Gene[] getGenes();

  /**
   * Sets the genes for the chromosome.
   * @param a_genes the genes to set for the chromosome
   *
   * @throws InvalidConfigurationException in case constraint checker is
   * provided
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  void setGenes(Gene[] a_genes)
      throws InvalidConfigurationException;

  /**
   * Returns the size of this Chromosome (the number of genes it contains).
   * A Chromosome's size is constant and will not change, until setGenes(...)
   * is used.
   *
   * @return number of genes contained within this Chromosome instance
   *
   * @author Klaus Meffert
   * @author Neil Rotstan
   * @since 2.6
   */
  int size();

  /**
   * Sets the fitness value of this Chromosome. This method is for use
   * by bulk fitness functions and should not be invokved from anything
   * else (except test cases).
   *
   * @param a_newFitnessValue a positive integer representing the fitness
   * of this Chromosome
   *
   * @author Neil Rotstan
   * @since 2.6
   */
  void setFitnessValue(double a_newFitnessValue);

  /**
   * Sets the fitness value of this Chromosome directly without any
   * constraint checks, conversions or checks. Only use if you know what
   * you do.
   *
   * @param a_newFitnessValue a positive integer representing the fitness
   * of this Chromosome
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  void setFitnessValueDirectly(double a_newFitnessValue);

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
   * @since 2.6
   */
  double getFitnessValue();

  /**
   * @return the lastly computed fitness value, or FitnessFunction.NO_FITNESS_VALUE
   * in case no value has been computed yet.
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  double getFitnessValueDirectly();

  /**
   * Sets whether this Chromosome has been selected by the natural selector
   * to continue to the next generation or manually (e.g. via an add-method).
   *
   * @param a_isSelected true if this Chromosome has been selected, false
   * otherwise
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 2.6
   */
  void setIsSelectedForNextGeneration(boolean a_isSelected);

  /**
   * Retrieves whether this Chromosome has been selected by the natural
   * selector to continue to the next generation.
   *
   * @return true if this Chromosome has been selected, false otherwise
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 2.6
   */
  boolean isSelectedForNextGeneration();

  /**
   * Sets the constraint checker to be used for this gene whenever method
   * setAllele(Object) is called.
   *
   * @param a_constraintChecker the constraint checker to be set
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  void setConstraintChecker(IGeneConstraintChecker a_constraintChecker)
      throws InvalidConfigurationException;

  /**
   * This sets the application-specific data that is attached to this
   * Chromosome. Attaching application-specific data may be useful for
   * some applications when it comes time to evaluate this Chromosome
   * in the fitness function.
   *
   * @param a_newData the new application-specific data to attach to this
   * Chromosome. Should be an instance of IApplicationData
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 2.6
   */
  void setApplicationData(Object a_newData);

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
   * @author Klaus Meffert
   * @since 2.6
   */
  Object getApplicationData();

  /**
   * Invoked when this Chromosome is no longer needed and should perform
   * any necessary cleanup. Note that this method will attempt to release
   * this Chromosome instance to the active ChromosomePool, if any.
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 2.6
   */
  void cleanup();

  /**
   * @return the configuration set
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  Configuration getConfiguration();

  /**
   * Increases the number of evolutionary rounds of chromosome in which it has
   * not been changed by one.
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  void increaseAge();

  /**
   * Reset age of chromosome because it has been changed.
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  void resetAge();

  /**
   * @param a_age set the age of the chromosome, see BestChromosomesSelector
   * for a use-case
   *
   * @author Klaus Meffert
   * @since 3.3.3
   */
  void setAge(int a_age);

  /**
   * @return 0: Chromosome newly created in this generation. This means it
   * does not need being crossed over with another newly created one
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  int getAge();

  /**
   * Increase information of number of genetic operations performed on
   * chromosome in current evolution round.
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  void increaseOperatedOn();

  /**
   * Resets the information of how many genetic operators have been performed
   * on the chromosome in the current round of evolution.
   *
   * @author Klaus Meffert
   * @since 3.2
   *
   */
  void resetOperatedOn();

  /**
   * @return number of genetic operations performed on chromosome in current
   * evolution round
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  int operatedOn();

}
