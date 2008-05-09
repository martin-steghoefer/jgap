/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
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
    extends BaseGene implements IPersistentRepresentation {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.32 $";

  /**
   * Shared constant representing the "true" boolean value. Shared constants
   * are used to save memory so that a new Boolean object doesn't have to
   * be constructed each time.
   */
  protected static final Boolean TRUE_BOOLEAN = Boolean.valueOf(true);

  /**
   * Shared constant representing the "false" boolean value. Shared constants
   * are used to save memory so that a new Boolean object doesn't have to
   * be constructed each time.
   */
  protected static final Boolean FALSE_BOOLEAN = Boolean.valueOf(false);

  /**
   * References the internal boolean value of this Gene.
   */
  private Boolean m_value;

  /**
   * Default constructor.<p>
   * Attention: The configuration used is the one set with the static method
   * Genotype.setConfiguration.
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public BooleanGene()
      throws InvalidConfigurationException {
    this(Genotype.getStaticConfiguration());
  }

  /**
   * @param a_config the configuration to use
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public BooleanGene(final Configuration a_config)
      throws InvalidConfigurationException {
    super(a_config);
  }

  /**
   * @param a_config the configuration to use
   * @param a_value allele value to setup the gene with
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public BooleanGene(final Configuration a_config, final boolean a_value)
      throws InvalidConfigurationException {
    super(a_config);
    m_value = new Boolean(a_value);
  }

  /**
   * @param a_config the configuration to use
   * @param a_value allele value to setup the gene with
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public BooleanGene(final Configuration a_config, final Boolean a_value)
      throws InvalidConfigurationException {
    super(a_config);
    if (a_value == null) {
      throw new IllegalArgumentException("Allele value must not be null. Use"
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
   * instances.
   *
   * @return a new Gene instance of the same type and with the same setup as
   * this concrete Gene
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   */
  protected Gene newGeneInternal() {
    try {
      return new BooleanGene(getConfiguration());
    }
    catch (InvalidConfigurationException iex) {
      throw new IllegalStateException(iex.getMessage());
    }
  }

  /**
   * Sets the value of this Gene to the new given value. This class
   * expects the value to be a Boolean instance.
   *
   * @param a_newValue the new value of this Gene instance
   */
  public void setAllele(final Object a_newValue) {
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
   * @return a string representation of this Gene's current state
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   */
  public String getPersistentRepresentation() {
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
   * prior call to the getPersistentRepresentation() method
   *
   * @throws UnsupportedOperationException to indicate that no implementation
   * is provided for this method
   * @throws UnsupportedRepresentationException if this Gene implementation
   * does not support the given string representation
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
   * @return the boolean value of this Gene
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
   * random number generator of their choice
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
   * @param a_other the BooleanGene to be compared
   * @return  a negative integer, zero, or a positive integer as this object
   * is less than, equal to, or greater than the specified object
   *
   * @throws ClassCastException if the specified object's type prevents it
   * from being compared to this BooleanGene
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 1.0
   */
  public int compareTo(Object a_other) {
    BooleanGene otherBooleanGene = (BooleanGene) a_other;
    // First, if the other gene is null, then this is the greater gene.
    // ----------------------------------------------------------------
    if (otherBooleanGene == null) {
      return 1;
    }
    else if (otherBooleanGene.m_value == null) {
      // If our value is also null, then we're possibly the same. Otherwise,
      // we're the greater gene.
      // -------------------------------------------------------------------
      if (m_value != null) {
        return 1;
      }
      else {
        if (isCompareApplicationData()) {
          return compareApplicationData(getApplicationData(),
                                        otherBooleanGene.getApplicationData());
        }
        else {
          return 0;
        }
      }
    }
    if (m_value == null) {
      if (otherBooleanGene.m_value == null) {
        if (isCompareApplicationData()) {
          return compareApplicationData(getApplicationData(),
                                        otherBooleanGene.getApplicationData());
        }
        else {
          return 0;
        }
      }
      else {
        return -1;
      }
    }
    // The Boolean class doesn't implement the Comparable interface, so
    // we have to do the comparison ourselves.
    // ----------------------------------------------------------------
    if (m_value.booleanValue() == false) {
      if (otherBooleanGene.m_value.booleanValue() == false) {
        // Both are false and therefore the same. Compare application data.
        // ----------------------------------------------------------------
        if (isCompareApplicationData()) {
          return compareApplicationData(getApplicationData(),
                                        otherBooleanGene.getApplicationData());
        }
        else {
          return 0;
        }
      }
      else {
        // This allele is false, but the other one is true. This
        // allele is the lesser.
        // -----------------------------------------------------
        return -1;
      }
    }
    else if (otherBooleanGene.m_value.booleanValue() == true) {
      // Both alleles are true and therefore the same. Compare application data.
      // -----------------------------------------------------------------------
      if (isCompareApplicationData()) {
        return compareApplicationData(getApplicationData(),
                                      otherBooleanGene.getApplicationData());
      }
      else {
        return 0;
      }
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
   * @param a_index not used here
   * @param a_percentage percentage of mutation (greater than -1 and smaller
   * than 1).
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void applyMutation(int a_index, double a_percentage) {
    if (m_value == null) {
      m_value = Boolean.valueOf(false);
    }
    else if (a_percentage > 0) {
      // change to TRUE
      // ---------------
      if (!m_value.booleanValue()) {
        m_value = Boolean.valueOf(true);
      }
    }
    else if (a_percentage < 0) {
      // change to FALSE
      // ---------------
      if (m_value.booleanValue()) {
        m_value = Boolean.valueOf(false);
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
