/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.impl;

import org.jgap.*;

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
 * @author Neil Rotstan
 * @author Klaus Meffert
 * @since 1.0
 */
public class BooleanGene
    extends BaseGene
    implements Gene {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.16 $";

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
   * Constructor
   *
   * @author Klaus Meffert
   * @since 2.4 (previously: implicitely existent)
   */
  public BooleanGene() {
  }

  /**
   * @param a_value allele value to setup the gene with
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public BooleanGene(boolean a_value) {
    m_value = new Boolean(a_value);
  }

  /**
   * @param a_value allele value to setup the gene with
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public BooleanGene(Boolean a_value) {
    if (a_value == null) {
      throw new IllegalArgumentException("Allele value may not be null. Use"
                                         + " no argument constructor if you"
                                         + " need to set allele to null"
                                         + " initially.");
    }
    else {
      m_value = a_value;
    }
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
   * @return a new Gene instance of the same type and with the same setup as
   * this concrete Gene.
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   */
  public Gene newGene() {
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
   * is provided for this method.
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   */
  public String getPersistentRepresentation()
      throws UnsupportedOperationException {
    String s;
    if (getInternalValue() == null) {
      s = "null";
    }
    else {
      s = getInternalValue().toString();
    }
    return s;
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
   * prior call to the getPersistentRepresentation() method.
   *
   * @throws UnsupportedOperationException to indicate that no implementation
   * is provided for this method.
   * @throws UnsupportedRepresentationException if this Gene implementation
   * does not support the given string representation.
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 1.0
   */
  public void setValueFromPersistentRepresentation(String a_representation)
      throws UnsupportedRepresentationException {
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
    else {
      throw new UnsupportedRepresentationException(
          "The input parameter must not be null!");
    }
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
   * used to create any random values. It's important to use this generator to
   * maintain the user's flexibility to configure the genetic engine to use the
   * random number generator of their choice.
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
   * is less than, equal to, or greater than the specified object.
   *
   * @throws ClassCastException if the specified object's type prevents it
   * from being compared to this BooleanGene.
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 1.0
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
    else if (m_value == null) {
      return otherBooleanGene.m_value == null ? 0 : -1;
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
   * Applies a mutation of a given intensity (percentage) onto the atomic
   * element at given index
   * @param index not used here
   * @param a_percentage percentage of mutation (greater than -1 and smaller
   * than 1).
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void applyMutation(int index, double a_percentage) {
    if (m_value == null) {
      m_value = new Boolean(false);
    }
    else if (a_percentage > 0) {
      // change to TRUE
      // ---------------
      if (!m_value.booleanValue()) {
        m_value = new Boolean(true);
      }
    }
    else if (a_percentage < 0) {
      // change to FALSE
      // ---------------
      if (m_value.booleanValue()) {
        m_value = new Boolean(false);
      }
    }
  }

  protected Object getInternalValue() {
    return m_value;
  }

  /**
   * Modified hashCode() function to return different hashcodes for differently
   * ordered genes in a chromosome
   * @return -2 if no allele set, otherwise value return by BaseGene.hashCode()
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public int hashCode() {
    if (getInternalValue() == null) {
      return -2;
    }
    else {
      return super.hashCode();
    }
  }

  /**
   * @return string representation of this Gene's value that may be useful for
   * display purposes.
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public String toString() {
    String s = "BooleanGene"
        + "=";
    if (getInternalValue() == null) {
      s += "null";
    }
    else {
      s += getInternalValue().toString();
    }
    return s;
  }
}
