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

import java.util.*;
import org.jgap.*;

/**
 * A Gene implementation that supports a double values for its allele.
 * Upper and lower bounds may optionally be provided to restrict the range
 * of legal values allowed by this Gene instance.<p>
 * Partly copied from IntegerGene.
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class DoubleGene
    extends NumberGene implements IPersistentRepresentation {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.40 $";

  /**
   * The upper bounds of values represented by this Gene. If not explicitly
   * provided by the user, this should be set to Double.MAX_VALUE.
   */
  private double m_upperBound;

  /**
   * The lower bounds of values represented by this Gene. If not explicitly
   * provided by the user, this should be set to Double.MIN_VALUE
   */
  private double m_lowerBound;

  /**
   * Constructs a new DoubleGene with default settings. No bounds will
   * be put into effect for values (alleles) of this Gene instance, other
   * than the standard range of double values.<p>
   * Attention: The configuration used is the one set with the static method
   * Genotype.setConfiguration.
   *
   * @throws InvalidConfigurationException
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 1.1
   */
  public DoubleGene()
      throws InvalidConfigurationException {
    this(Genotype.getStaticConfiguration());
  }

  /**
   * Constructs a new DoubleGene with default settings. No bounds will
   * be put into effect for values (alleles) of this Gene instance, other
   * than the standard range of double values.
   *
   * @param a_config the configuration to use
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public DoubleGene(final Configuration a_config)
      throws InvalidConfigurationException {
    this(a_config, - (Double.MAX_VALUE / 2),
         Double.MAX_VALUE / 2);
  }

  /**
   * Constructs a new DoubleGene with the specified lower and upper
   * bounds for values (alleles) of this Gene instance.
   *
   * @param a_config the configuration to use
   * @param a_lowerBound the lowest value that this Gene may possess,
   * inclusively
   * @param a_upperBound the highest value that this Gene may possess,
   * inclusively
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public DoubleGene(final Configuration a_config, final double a_lowerBound,
                    final double a_upperBound)
      throws InvalidConfigurationException {
    super(a_config);
    m_lowerBound = a_lowerBound;
    m_upperBound = a_upperBound;
  }

  /**
   * Provides an implementation-independent means for creating new Gene
   * instances.
   *
   * @return a new Gene instance of the same type and with the same
   * setup as this concrete Gene
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  protected Gene newGeneInternal() {
    try {
      DoubleGene result = new DoubleGene(getConfiguration(), m_lowerBound,
                                         m_upperBound);
      return result;
    }
    catch (InvalidConfigurationException iex) {
      throw new IllegalStateException(iex.getMessage());
    }
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
   * @author Klaus Meffert
   * @since 1.1
   */
  public String getPersistentRepresentation() {
    // The persistent representation includes the value, lower bound,
    // and upper bound. Each is separated by a colon.
    // --------------------------------------------------------------
    String s;
    if (getInternalValue() == null) {
      s = "null";
    }
    else {
      s = getInternalValue().toString();
    }
    return s + PERSISTENT_FIELD_DELIMITER + m_lowerBound
        + PERSISTENT_FIELD_DELIMITER + m_upperBound;
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
   * @author Klaus Meffert
   * @since 1.1
   */
  public void setValueFromPersistentRepresentation(String a_representation)
      throws UnsupportedRepresentationException {
    /**@todo unify first part of method with IntegerGene*/
    if (a_representation != null) {
      StringTokenizer tokenizer =
          new StringTokenizer(a_representation,
                              PERSISTENT_FIELD_DELIMITER);
      // Make sure the representation contains the correct number of
      // fields. If not, throw an exception.
      // -----------------------------------------------------------
      if (tokenizer.countTokens() != 3) {
        throw new UnsupportedRepresentationException(
            "The format of the given persistent representation "
            + " is not recognized: it does not contain three tokens: "
            + a_representation);
      }
      String valueRepresentation = tokenizer.nextToken();
      String lowerBoundRepresentation = tokenizer.nextToken();
      String upperBoundRepresentation = tokenizer.nextToken();
      // First parse and set the representation of the value.
      // ----------------------------------------------------
      if (valueRepresentation.equals("null")) {
        setAllele(null);
      }
      else {
        try {
          setAllele(new Double(Double.parseDouble(valueRepresentation)));
        }
        catch (NumberFormatException e) {
          throw new UnsupportedRepresentationException(
              "The format of the given persistent representation " +
              "is not recognized: field 1 does not appear to be " +
              "a double value.");
        }
      }
      // Now parse and set the lower bound.
      // ----------------------------------
      try {
        m_lowerBound =
            Double.parseDouble(lowerBoundRepresentation);
      }
      catch (NumberFormatException e) {
        throw new UnsupportedRepresentationException(
            "The format of the given persistent representation " +
            "is not recognized: field 2 does not appear to be " +
            "a double value.");
      }
      // Now parse and set the upper bound.
      // ----------------------------------
      try {
        m_upperBound =
            Double.parseDouble(upperBoundRepresentation);
      }
      catch (NumberFormatException e) {
        throw new UnsupportedRepresentationException(
            "The format of the given persistent representation " +
            "is not recognized: field 3 does not appear to be " +
            "a double value.");
      }
    }
  }

  /**
   * Retrieves the double value of this Gene, which may be more convenient in
   * some cases than the more general getAllele() method.
   *
   * @return the double value of this Gene
   * @since 1.1
   */
  public double doubleValue() {
    return ( (Double) getAllele()).doubleValue();
  }

  /**
   * Sets the value (allele) of this Gene to a random Double value between
   * the lower and upper bounds (if any) of this Gene.
   *
   * @param a_numberGenerator the random number generator that should be used
   * to create any random values. It's important to use this generator to
   * maintain the user's flexibility to configure the genetic engine to use the
   * random number generator of their choice
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void setToRandomValue(RandomGenerator a_numberGenerator) {
    // maps the randomly determined value to the current bounds.
    // ---------------------------------------------------------
    setAllele(new Double( (m_upperBound - m_lowerBound) *
                         a_numberGenerator.nextDouble() + m_lowerBound));
  }

  /**
   * Compares to objects by first casting them into their expected type
   * (e.g. Integer for IntegerGene) and then calling the compareTo-method
   * of the casted type.
   *
   * @param o1 first object to be compared, which is always not null
   * @param o2 second object to be compared, which is always not null
   * @return a negative integer, zero, or a positive integer as this object
   * is less than, equal to, or greater than the object provided for comparison
   *
   * @since 1.1
   */
  protected int compareToNative(Object o1, Object o2) {
    return ( (Double) o1).compareTo( (Double) o2);
  }

  /**
   * Maps the value of this DoubleGene to within the bounds specified by
   * the m_upperBounds and m_lowerBounds instance variables. The value's
   * relative position within the double range will be preserved within the
   * bounds range (in other words, if the value is about halfway between the
   * double max and min, then the resulting value will be about halfway
   * between the upper bounds and lower bounds). If the value is null or
   * is already within the bounds, it will be left unchanged.
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 1.1
   */
  protected void mapValueToWithinBounds() {
    if (getAllele() != null) {
      Double d_value = ( (Double) getAllele());
      if (d_value.isInfinite()) {
        // Here we have to break to avoid a stack overflow.
        // ------------------------------------------------
        return;
      }
      // If the value exceeds either the upper or lower bounds, then
      // map the value to within the legal range. To do this, we basically
      // calculate the distance between the value and the double min,
      // then multiply it with a random number and then care that the lower
      // boundary is added.
      // ------------------------------------------------------------------
      if (d_value.doubleValue() > m_upperBound ||
          d_value.doubleValue() < m_lowerBound) {
        RandomGenerator rn;
        if (getConfiguration() != null) {
          rn = getConfiguration().getRandomGenerator();
        }
        else {
          rn = new StockRandomGenerator();
        }
//        setAllele(new Double((rn.nextDouble()
//                             * (0.001d*(m_upperBound - m_lowerBound)))/0.001d + m_lowerBound));
        setAllele(new Double((rn.nextDouble()
                             * ((m_upperBound - m_lowerBound))) + m_lowerBound));
      }
    }
  }

  /**
   * See interface Gene for description.
   * @param index ignored (because there is only 1 atomic element)
   * @param a_percentage percentage of mutation (greater than -1 and smaller
   * than 1)
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void applyMutation(int index, double a_percentage) {
    double range = (m_upperBound - m_lowerBound) * a_percentage;
    double newValue = doubleValue() + range;
    setAllele(new Double(newValue));
  }

  /**
   * Modified hashCode() function to return different hashcodes for differently
   * ordered genes in a chromosome.
   * @return -3 if no allele set, otherwise value return by BaseGene.hashCode()
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public int hashCode() {
    if (getInternalValue() == null) {
      return -3;
    }
    else {
      return super.hashCode();
    }
  }

  /**
   * @return string representation of this Gene's value that may be useful for
   * display purposes
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public String toString() {
    String s = "DoubleGene(" + m_lowerBound + "," + m_upperBound + ")"
        + "=";
    if (getInternalValue() == null) {
      s += "null";
    }
    else {
      s += getInternalValue().toString();
    }
    return s;
  }

  /**
   * @return the lower bound set
   * @author Klaus Meffert
   * @since 3.0
   */
  public double getLowerBound() {
    return m_lowerBound;
  }

  /**
   * @return the upper bound set
   * @author Klaus Meffert
   * @since 3.0
   */
  public double getUpperBound() {
    return m_upperBound;
  }
}
