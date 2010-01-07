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

import java.io.Serializable;

/**
 * Genes represent the discrete components of a potential solution
 * (the Chromosome). This interface exists so that custom gene implementations
 * can be easily plugged-in, which can add a great deal of flexibility and
 * convenience for many applications. Note that it's very important that
 * implementations of this interface also implement the equals() method.
 * Without a proper implementation of equals(), some genetic operations will
 * fail to work properly.
 * <p>
 * When implementing a new Gene type, extend it from {@link org.jgap.BaseGene}!
 *
 * @author Neil Rotstan
 * @author Klaus Meffert
 * @since 1.0
 */
public interface Gene
    extends Comparable, Serializable, IUniqueKey {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.26 $";

  /**
   * Represents the delimiter that is used to separate fields in the
   * persistent representation.
   */
  final static String PERSISTENT_FIELD_DELIMITER = ":";

  /**
   * Provides an implementation-independent means for creating new Gene
   * instances. The new instance that is created and returned should be
   * setup with any implementation-dependent configuration that this Gene
   * instance is setup with (aside from the actual value, of course). For
   * example, if this Gene were setup with bounds on its value, then the
   * Gene instance returned from this method should also be setup with
   * those same bounds. This is important, as the JGAP core will invoke this
   * method on each Gene in the sample Chromosome in order to create each
   * new Gene in the same respective gene position for a new Chromosome.
   * <p>
   * It should be noted that nothing is guaranteed about the actual value
   * of the returned Gene and it should therefore be considered to be
   * undefined.
   *
   * @return a new Gene instance of the same type and with the same setup as
   * this concrete Gene
   *
   * @since 1.0
   */
  Gene newGene();

  /**
   * Sets the value of this Gene to the new given value. The actual
   * type of the value is implementation-dependent.
   *
   * @param a_newValue the new value of this Gene instance
   *
   * @since 1.0
   */

  void setAllele(Object a_newValue);

  /**
   * Retrieves the value represented by this Gene. The actual type
   * of the value is implementation-dependent.
   *
   * @return the value of this Gene.
   *
   * @since 1.0
   */
  Object getAllele();

  /**
   * Retrieves a string representation of the value of this Gene instance
   * that includes any information required to reconstruct it at a later
   * time, such as its value and internal state. This string will be used to
   * represent this Gene instance in XML persistence. This is an optional
   * method but, if not implemented, XML persistence and possibly other
   * features will not be available. An UnsupportedOperationException should
   * be thrown if no implementation is provided.
   *
   * @return string representation of this Gene's current state
   * @throws UnsupportedOperationException to indicate that no implementation
   * is provided for this method
   *
   * @since 1.0
   */
  String getPersistentRepresentation()
      throws UnsupportedOperationException;

  /**
   * Sets the value and internal state of this Gene from the string
   * representation returned by a previous invocation of the
   * getPersistentRepresentation() method. This is an optional method but,
   * if not implemented, XML persistence and possibly other features will not
   * be available. An UnsupportedOperationException should be thrown if no
   * implementation is provided.
   *
   * @param a_representation the string representation retrieved from a
   * prior call to the getPersistentRepresentation() method
   *
   * @throws UnsupportedOperationException to indicate that no implementation
   * is provided for this method
   * @throws UnsupportedRepresentationException if this Gene implementation
   * does not support the given string representation
   *
   * @since 1.0
   */
  void setValueFromPersistentRepresentation(String a_representation)
      throws UnsupportedOperationException, UnsupportedRepresentationException;

  /**
   * Sets the value of this Gene to a random legal value for the
   * implementation. This method exists for the benefit of mutation and other
   * operations that simply desire to randomize the value of a gene.
   *
   * @param a_numberGenerator The random number generator that should be used
   * to create any random values. It's important to use this generator to
   * maintain the user's flexibility to configure the genetic engine to use the
   * random number generator of their choice
   *
   * @since 1.0
   */
  void setToRandomValue(RandomGenerator a_numberGenerator);

  /**
   * Executed by the genetic engine when this Gene instance is no
   * longer needed and should perform any necessary resource cleanup.
   */
  void cleanup();

  /**
   * @return a string representation of the gene
   *
   * @since 1.1 (in the interface)
   */
  String toString();

  /**
   * @return the size of the gene, i.e the number of atomic elements.
   * Always 1 for numbers
   *
   * @since 1.1
   */
  int size();

  /**
   * Applies a mutation of a given intensity (percentage) onto the atomic
   * element at given index (NumberGenes only have one atomic element)
   * @param index index of atomic element, between 0 and size()-1
   * @param a_percentage percentage of mutation (greater than -1 and smaller
   * than 1).
   *
   * @since 1.1
   */
  void applyMutation(int index, double a_percentage);

  /**
   * This sets the application-specific data that is attached to this Gene.
   * Attaching application-specific data may be useful for
   * some applications when it comes time to distinguish a Gene from another.
   * JGAP ignores this data functionally.
   *
   * @param a_newData the new application-specific data to attach to this
   * Gene
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  void setApplicationData(Object a_newData);

  /**
   * Retrieves the application-specific data that is attached to this Gene.
   * Attaching application-specific data may be useful for
   * some applications when it comes time to distinguish a Gene from another.
   * JGAP ignores this data functionally.
   *
   * @return the application-specific data previously attached to this Gene,
   * or null if there is no data attached
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  Object getApplicationData();

  /**
   * Should we also consider the application data when comparing? Default is
   * "false" as "true" means a Gene is losing its identity when
   * application data is set differently!
   *
   * @param a_doCompare true: consider application data in method compareTo
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  void setCompareApplicationData(boolean a_doCompare);

  /*
   * @return should we also consider the application data when comparing?
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  boolean isCompareApplicationData();

  /**
   * @return energy of the gene
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public double getEnergy();

  /**
   * Sets the energy of the gene
   * @param a_energy the energy to set
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  void setEnergy(double a_energy);

  /**
   * Sets the constraint checker to be used for this gene whenever method
   * setAllele(Object) is called.
   * @param a_constraintChecker the constraint checker to be set
   *
   * @author Klaus Meffert
   * @since 2.6 (moved from CompositeGene, where it was since 2.0)
   */
  void setConstraintChecker(
      final IGeneConstraintChecker a_constraintChecker);

  /**
   * @return the configuration used
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  Configuration getConfiguration();
}
