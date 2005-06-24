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

import java.util.*;
import org.jgap.*;

/**
 * A Gene implementation that supports an integer values for its allele.
 * Upper and lower bounds may optionally be provided to restrict the range
 * of legal values allowed by this Gene instance.
 *
 * @author Neil Rotstan
 * @author Klaus Meffert
 * @since 1.0
 */
public class IntegerGene
    extends NumberGene
    implements Gene {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.23 $";

  /**
   * Represents the constant range of values supported by integers.
   */
  protected final static long INTEGER_RANGE = (long) Integer.MAX_VALUE -
      (long) Integer.MIN_VALUE;

  /**
   * The upper bounds of values represented by this Gene. If not explicitly
   * provided by the user, this should be set to Integer.MAX_VALUE.
   */
  protected int m_upperBounds;

  /**
   * The lower bounds of values represented by this Gene. If not explicitly
   * provided by the user, this should be set to Integer.MIN_VALUE
   */
  protected int m_lowerBounds;

  /**
   * Constructs a new IntegerGene with default settings. No bounds will
   * be put into effect for values (alleles) of this Gene instance, other
   * than the standard range of integer values.
   *
   * @author Neil Rostan
   * @author Klaus Meffert
   * @since 1.0
   */
  public IntegerGene() {
    this(Integer.MIN_VALUE, Integer.MAX_VALUE);
  }

  /**
   * Constructs a new IntegerGene with the specified lower and upper
   * bounds for values (alleles) of this Gene instance.
   *
   * @param a_lowerBounds the lowest value that this Gene may possess,
   * inclusive
   * @param a_upperBounds the highest value that this Gene may possess,
   * inclusive
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public IntegerGene(int a_lowerBounds, int a_upperBounds) {
    m_lowerBounds = a_lowerBounds;
    m_upperBounds = a_upperBounds;
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
   * this concrete Gene
   *
   * @author Neil Rostan
   * @since 1.0
   */
  public Gene newGene() {
    return new IntegerGene(m_lowerBounds, m_upperBounds);
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
   * @return string representation of this Gene's current state
   * @throws UnsupportedOperationException to indicate that no implementation
   * is provided for this method
   *
   * @author Neil Rostan
   * @since 1.0
   */
  public String getPersistentRepresentation()
      throws UnsupportedOperationException {
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
    return s + PERSISTENT_FIELD_DELIMITER + m_lowerBounds +
        PERSISTENT_FIELD_DELIMITER + m_upperBounds;
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
   * @author Neil Rostan
   * @since 1.0
   */
  public void setValueFromPersistentRepresentation(String a_representation)
      throws UnsupportedRepresentationException {
    if (a_representation != null) {
      StringTokenizer tokenizer =
          new StringTokenizer(a_representation,
                              PERSISTENT_FIELD_DELIMITER);
      // Make sure the representation contains the correct number of
      // fields. If not, throw an exception.
      // -----------------------------------------------------------
      if (tokenizer.countTokens() != 3) {
        throw new UnsupportedRepresentationException(
            "The format of the given persistent representation " +
            "is not recognized: it does not contain three tokens.");
      }
      String valueRepresentation = tokenizer.nextToken();
      String lowerBoundRepresentation = tokenizer.nextToken();
      String upperBoundRepresentation = tokenizer.nextToken();
      // First parse and set the representation of the value.

      // ----------------------------------------------------
      if (valueRepresentation.equals("null")) {
        m_value = null;
      }
      else {
        try {
          m_value =
              new Integer(Integer.parseInt(valueRepresentation));
        }
        catch (NumberFormatException e) {
          throw new UnsupportedRepresentationException(
              "The format of the given persistent representation " +
              "is not recognized: field 1 does not appear to be " +
              "an integer value.");
        }
      }
      // Now parse and set the lower bound.
      // ----------------------------------
      try {
        m_lowerBounds =
            Integer.parseInt(lowerBoundRepresentation);
      }
      catch (NumberFormatException e) {
        throw new UnsupportedRepresentationException(
            "The format of the given persistent representation " +
            "is not recognized: field 2 does not appear to be " +
            "an integer value.");
      }
      // Now parse and set the upper bound.
      // ----------------------------------
      try {
        m_upperBounds =
            Integer.parseInt(upperBoundRepresentation);
      }
      catch (NumberFormatException e) {
        throw new UnsupportedRepresentationException(
            "The format of the given persistent representation " +
            "is not recognized: field 3 does not appear to be " +
            "an integer value.");
      }
    }
  }

  /**
   * Retrieves the int value of this Gene, which may be more convenient in
   * some cases than the more general getAllele() method.
   *
   * @return the int value of this Gene.
   *
   * @author Neil Rostan
   * @since 1.0
   */
  public int intValue() {
    return ( (Integer) m_value).intValue();
  }

  /**
   * Sets the value (allele) of this Gene to a random Integer value between
   * the lower and upper bounds (if any) of this Gene.
   *
   * @param a_numberGenerator the random number generator that should be
   * used to create any random values. It's important to use this generator to
   * maintain the user's flexibility to configure the genetic engine to use the
   * random number generator of their choice
   *
   * @author Neil Rostan
   * @since 1.0
   */
  public void setToRandomValue(RandomGenerator a_numberGenerator) {
    m_value = new Integer( (int) ( (m_upperBounds - m_lowerBounds) *
                                  a_numberGenerator.nextDouble() +
                                  m_lowerBounds));
  }

  /**
   * Compares to objects by first casting them into their expected type
   * (e.g. Integer for IntegerGene) and then calling the compareTo-method
   * of the casted type.
   * @param o1 first object to be compared, always is not null
   * @param o2 second object to be compared, always is not null
   * @return a negative integer, zero, or a positive integer as this object is
   * less than, equal to, or greater than the object provided for comparison
   *
   * @author Neil Rostan
   * @since 1.0
   */
  protected int compareToNative(Object o1, Object o2) {
    return ( (Integer) o1).compareTo( (Integer) o2);
  }

  /**
   * Maps the value of this IntegerGene to within the bounds specified by
   * the m_upperBounds and m_lowerBounds instance variables. The value's
   * relative position within the integer range will be preserved within the
   * bounds range (in other words, if the value is about halfway between the
   * integer max and min, then the resulting value will be about halfway
   * between the upper bounds and lower bounds). If the value is null or
   * is already within the bounds, it will be left unchanged.
   *
   * @author Neil Rostan
   * @author Klaus Meffert
   * @since 1.0
   */
  protected void mapValueToWithinBounds() {
    if (m_value != null) {
      Integer i_value = ( (Integer) m_value);
      // If the value exceeds either the upper or lower bounds, then
      // map the value to within the legal range. To do this, we basically
      // calculate the distance between the value and the integer min,
      // determine how many bounds units that represents, and then add
      // that number of units to the upper bound.
      // -----------------------------------------------------------------
      if (i_value.intValue() > m_upperBounds ||
          i_value.intValue() < m_lowerBounds) {
        RandomGenerator rn;
        if (Genotype.getConfiguration() != null) {
          rn = Genotype.getConfiguration().getRandomGenerator();
        }
        else {
          rn = new StockRandomGenerator();
        }
        m_value = new Integer(rn.nextInt(
            m_upperBounds - m_lowerBounds) + m_lowerBounds);
      }
    }
  }

  /**
   * See interface Gene for description
   * @param index ignored (because there is only 1 atomic element)
   * @param a_percentage percentage of mutation (greater than -1 and smaller
   * than 1).
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void applyMutation(int index, double a_percentage) {
    double range = (m_upperBounds - m_lowerBounds) * a_percentage;
    if (m_value == null) {
      setAllele(new Integer( (int) range + m_lowerBounds));
    }
    else {
      int newValue = (int) Math.round(intValue() + range);
      setAllele(new Integer(newValue));
    }
  }

  /**
   * Modified hashCode() function to return different hashcodes for differently
   * ordered genes in a chromosome
   * @return -1 if no allele set, otherwise value return by BaseGene.hashCode()
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public int hashCode() {
    if (getInternalValue() == null) {
      return -1;
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
    String s = "IntegerGene(" + m_lowerBounds + "," + m_upperBounds + ")"
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
