/*
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

package org.jgap.impl;

import org.jgap.Configuration;
import org.jgap.Gene;
import org.jgap.RandomGenerator;
import org.jgap.UnsupportedRepresentationException;

/**
 * A Gene implementation that supports two possible values (alleles) for each
 * gene: true and false.
 * <p>
 * NOTE: Since this Gene implementation only supports two different
 * values (true and false), there's only a 50% chance that invocation
 * of the setToRandomValue() method will actually change the value of
 * this Gene (if it has a value). As a result, it may be desirable to
 * use a higher overall mutation rate when this Gene implementation
 * is in use.
 *
 * @author Neil Rotstan, Klaus Meffert
 * @since 1.0
 */
public class BooleanGene
    implements Gene {
  /**
   * Shared constant representing the "true" boolean value. Shared constants
   * are used to save memory so that a new Boolean object doesn't have to
   * be constructed each time.
   */
  protected static final Boolean TRUE_BOOLEAN = new Boolean(true);
  /**
   * Shared constant representing the "false" boolean value. Shared constants
   * are used to save memory so that a new Boolean object doesn't have to
   * be constructed each time.
   */
  protected static final Boolean FALSE_BOOLEAN = new Boolean(false);
  /**
   * References the internal boolean value of this Gene.
   */
  protected Boolean m_value = null;
  /**
   * Constructs a new BooleanGene with default settings.
   */
  public BooleanGene() {
  }

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
   * @param a_activeConfiguration The current active configuration.
   * @return A new Gene instance of the same type and with the same
   *         setup as this concrete Gene.
   */
  public Gene newGene(Configuration a_activeConfiguration) {
    return new BooleanGene();
  }

  /**
   * Sets the value of this Gene to the new given value. This class
   * expects the value to be a Boolean instance.
   *
   * @param a_newValue the new value of this Gene instance.
   */
  public void setAllele(Object a_newValue) {
    m_value = (Boolean) a_newValue;
  }

  /**
   * Retrieves a string representation of this Gene that includes any
   * information required to reconstruct it at a later time, such as its
   * value and internal state. This string will be used to represent this
   * Gene in XML persistence. This is an optional method but, if not
   * implemented, XML persistence and possibly other features will not be
   * available. An UnsupportedOperationException should be thrown if no
   * implementation is provided.
   *
   * @return A string representation of this Gene's current state.
   * @throws UnsupportedOperationException to indicate that no implementation
   *         is provided for this method.
   */
  public String getPersistentRepresentation() throws
      UnsupportedOperationException {
    return toString();
  }

  /**
   * Sets the value and internal state of this Gene from the string
   * representation returned by a previous invocation of the
   * getPersistentRepresentation() method. This is an optional method but,
   * if not implemented, XML persistence and possibly other features will not
   * be available. An UnsupportedOperationException should be thrown if no
   * implementation is provided.
   *
   * @param a_representation the string representation retrieved from a
   *                         prior call to the getPersistentRepresentation()
   *                         method.
   *
   * @throws UnsupportedOperationException to indicate that no implementation
   *         is provided for this method.
   * @throws UnsupportedRepresentationException if this Gene implementation
   *         does not support the given string representation.
   */
  public void setValueFromPersistentRepresentation(String a_representation) throws
      UnsupportedRepresentationException {
    if (a_representation != null) {
      if (a_representation.equals("null")) {
        m_value = null;
      }
      else if (a_representation.equals("true")) {
        m_value = TRUE_BOOLEAN;
      }
      else if (a_representation.equals("false")) {
        m_value = FALSE_BOOLEAN;
      }
      else {
        throw new UnsupportedRepresentationException(
            "Unknown boolean gene representation: " +
            a_representation);
      }
    }
  }

  /**
   * Retrieves the value represented by this Gene. All values returned
   * by this class will be Boolean instances.
   *
   * @return the Boolean value of this Gene.
   */
  public Object getAllele() {
    return m_value;
  }

  /**
   * Retrieves the boolean value of this Gene. This may be more convenient
   * in some cases than the more general getAllele() method.
   *
   * @return the boolean value of this Gene.
   */
  public boolean booleanValue() {
    return m_value.booleanValue();
  }

  /**
   * Sets the value (allele) of this Gene to a random legal value. This
   * method exists for the benefit of mutation and other operations that
   * simply desire to randomize the value of a gene.
   * <p>
   * NOTE: Since this Gene implementation only supports two different
   * values (true and false), there's only a 50% chance that invocation
   * of this method will actually change the value of this Gene (if
   * it has a value). As a result, it may be desirable to use a higher
   * overall mutation rate when this Gene implementation is in use.
   *
   * @param a_numberGenerator The random number generator that should be
   *                          used to create any random values. It's important
   *                          to use this generator to maintain the user's
   *                          flexibility to configure the genetic engine
   *                          to use the random number generator of their
   *                          choice.
   */
  public void setToRandomValue(RandomGenerator a_numberGenerator) {
    if (a_numberGenerator.nextBoolean() == true) {
      m_value = TRUE_BOOLEAN;
    }
    else {
      m_value = FALSE_BOOLEAN;
    }
  }

  /**
   * Compares this BooleanGene with the specified object for order. A
   * false value is considered to be less than a true value. A null value
   * is considered to be less than any non-null value.
   *
   * @param  other the BooleanGene to be compared.
   * @return  a negative integer, zero, or a positive integer as this object
   *		is less than, equal to, or greater than the specified object.
   *
   * @throws ClassCastException if the specified object's type prevents it
   *         from being compared to this BooleanGene.
   */
  public int compareTo(Object other) {
    BooleanGene otherBooleanGene = (BooleanGene) other;
    // First, if the other gene is null, then this is the greater gene.
    // ----------------------------------------------------------------
    if (otherBooleanGene == null) {
      return 1;
    }
    else if (otherBooleanGene.m_value == null) {
      // If our value is also null, then we're the same. Otherwise,
      // we're the greater gene.
      // ----------------------------------------------------------
      return m_value == null ? 0 : 1;
    }
    // The Boolean class doesn't implement the Comparable interface, so
    // we have to do the comparison ourselves.
    // ----------------------------------------------------------------
    if (m_value.booleanValue() == false) {
      if (otherBooleanGene.m_value.booleanValue() == false) {
        // Both are false and therefore the same. Return zero.
        // ---------------------------------------------------
        return 0;
      }
      else {
        // This allele is false, but the other one is true. This
        // allele is the lesser.
        // -----------------------------------------------------
        return -1;
      }
    }
    else if (otherBooleanGene.m_value.booleanValue() == true) {
      // Both alleles are true and therefore the same. Return zero.
      // ----------------------------------------------------------
      return 0;
    }
    else {
      // This allele is true, but the other is false. This allele is
      // the greater.
      // -----------------------------------------------------------
      return 1;
    }
  }

  /**
   * Compares this BooleanGene with the given object and returns true if
   * the other object is a BooleanGene and has the same value as this
   * BooleanGene. Otherwise it returns false.
   *
   * @param other the object to compare to this BooleanGene for equality.
   * @return true if this BooleanGene is equal to the given object,
   *         false otherwise.
   */
  public boolean equals(Object other) {
    try {
      return compareTo(other) == 0;
    }
    catch (ClassCastException e) {
      // If the other object isn't a BooleanGene, then we're not equal.
      // --------------------------------------------------------------
      return false;
    }
  }

  /**
   * Retrieves the hash code value of this BooleanGene.
   *
   * @return this BooleanGene's hash code.
   */
  public int hashCode() {
    // If the internal Boolean hasn't been set, return zero. Otherwise,
    // just return the Boolean's hash code.
    // ----------------------------------------------------------------
    if (m_value == null) {
      return 0;
    }
    else {
      return m_value.hashCode();
    }
  }

  /**
   * Retrieves a string representation of this BooleanGene's value that
   * may be useful for display purposes.
   *
   * @return a string representation of this BooleanGene's value.
   */
  public String toString() {
    if (m_value == null) {
      return "null";
    }
    else {
      return m_value.toString();
    }
  }

  /**
   * Executed by the genetic engine when this Gene instance is no
   * longer needed and should perform any necessary resource cleanup.
   */
  public void cleanup() {
    // No specific cleanup is necessary for this implementation.
    // ---------------------------------------------------------
  }

  /**
   * @return the size of the gene, i.e the number of atomic elements.
   *         Always 1 for BooleanGene
   *
   * @since 1.1
   */
  public int size() {
    return 1;
  }

  /**
   * Applies a mutation of a given intensity (percentage) onto the atomic
   * element at given index (NumberGenes only have one atomic element)
   * @param index index of atomic element, between 0 and size()-1
   * @param a_percentage percentage of mutation (greater than -1 and smaller
   *        than 1).
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void applyMutation(int index, double a_percentage) {
    if (a_percentage >= 0) {
      // change to TRUE
      // ---------------
      if (!m_value.booleanValue()) {
        m_value = new Boolean(true);
      }
    }
    else {
      // change to FALSE
      // ---------------
      if (m_value.booleanValue()) {
        m_value = new Boolean(false);
      }
    }
  }
}
