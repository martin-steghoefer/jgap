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

import java.util.*;
import org.jgap.*;

/**
 * A Gene implementation that supports a double values for its allele.
 * Upper and lower bounds may optionally be provided to restrict the range
 * of legal values allowed by this Gene instance.
 * Partly copied from IntegerGene.
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class DoubleGene
    extends NumberGene
    implements Gene {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.10 $";

  /**
   * Represents the constant range of values supported by doubles.
   */
  protected final static double DOUBLE_RANGE = Double.MAX_VALUE;

  /**
   * The upper bounds of values represented by this Gene. If not explicitly
   * provided by the user, this should be set to Double.MAX_VALUE.
   */
  protected double m_upperBounds;

  /**
   * The lower bounds of values represented by this Gene. If not explicitly
   * provided by the user, this should be set to Double.MIN_VALUE
   */
  protected double m_lowerBounds;

  /**
   * Stores the number of double range units that a single bounds-range
   * unit represents. For example, if the double range is -2 billion to
   * +2 billion and the bounds range is -1 billion to +1 billion, then
   * each unit in the bounds range would map to 2 units in the double
   * range. The value of this variable would therefore be 2. This mapping
   * unit is used to map illegal allele values that are outside of the
   * bounds to legal allele values that are within the bounds.
   */
  protected double m_boundsUnitsToDoubleUnits;

  /**
   * Optional helper class for checking if a given allele value to be set
   * is valid. If not the allele value may not be set for the gene!
   */
  private IGeneConstraintChecker m_geneAlleleChecker;

  /**
   * Constructs a new DoubleGene with default settings. No bounds will
   * be put into effect for values (alleles) of this Gene instance, other
   * than the standard range of double values.
   *
   * @since 1.1
   */
  public DoubleGene() {
    m_lowerBounds = - ( Double.MAX_VALUE / 2);
    m_upperBounds = Double.MAX_VALUE;
    calculateBoundsUnitsToDoubleUnitsRatio();
  }

  /**
   * Constructs a new DoubleGene with the specified lower and upper
   * bounds for values (alleles) of this Gene instance.
   *
   * @param a_lowerBounds The lowest value that this Gene may possess,
   *                      inclusive.
   * @param a_upperBounds The highest value that this Gene may possess,
   *                      inclusive.
   * @author Klaus Meffert
   * @since 1.1
   */
  public DoubleGene(double a_lowerBounds, double a_upperBounds) {
    m_lowerBounds = a_lowerBounds;
    m_upperBounds = a_upperBounds;
    calculateBoundsUnitsToDoubleUnitsRatio();
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
   * @param a_activeConfiguration ignored here.
   * @return A new Gene instance of the same type and with the same
   *         setup as this concrete Gene.
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public Gene newGene(Configuration a_activeConfiguration) {
    return new DoubleGene(m_lowerBounds, m_upperBounds);
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
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public String getPersistentRepresentation()
      throws
      UnsupportedOperationException {
    // The persistent representation includes the value, lower bound,
    // and upper bound. Each is separated by a colon.

    // --------------------------------------------------------------
    return toString() + PERSISTENT_FIELD_DELIMITER + m_lowerBounds +
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
   *                         prior call to the getPersistentRepresentation()
   *                         method.
   *
   * @throws UnsupportedOperationException to indicate that no implementation
   *         is provided for this method.
   * @throws UnsupportedRepresentationException if this Gene implementation
   *         does not support the given string representation.
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void setValueFromPersistentRepresentation(String a_representation)
      throws
      UnsupportedRepresentationException {
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
              new Double(Double.parseDouble(valueRepresentation));
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
        m_lowerBounds =
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
        m_upperBounds =
            Double.parseDouble(upperBoundRepresentation);
      }
      catch (NumberFormatException e) {
        throw new UnsupportedRepresentationException(
            "The format of the given persistent representation " +
            "is not recognized: field 3 does not appear to be " +
            "a double value.");
      }
      // We need to recalculate the bounds units to double units
      // ratio since our lower and upper bounds have probably just
      // been changed.
      // -------------------------------------------------------------
      calculateBoundsUnitsToDoubleUnitsRatio();
    }
  }

  /**
   * Retrieves the double value of this Gene, which may be more convenient in
   * some cases than the more general getAllele() method.
   *
   * @return the double value of this Gene.
   * @since 1.1
   */
  public double doubleValue() {
    return ( (Double) m_value).doubleValue();
  }

  /**
   * Sets the value (allele) of this Gene to a random Double value between
   * the lower and upper bounds (if any) of this Gene.
   *
   * @param a_numberGenerator The random number generator that should be
   *                          used to create any random values. It's important
   *                          to use this generator to maintain the user's
   *                          flexibility to configure the genetic engine
   *                          to use the random number generator of their
   *                          choice.
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void setToRandomValue(RandomGenerator a_numberGenerator) {
    m_value = new Double(a_numberGenerator.nextDouble());
    // If the value isn't between the upper and lower bounds of this
    // DoubleGene, map it to a value within those bounds.
    // -------------------------------------------------------------
    mapValueToWithinBounds();
  }

  /**
   * Compares to objects by first casting them into their expected type
   * (e.g. Integer for IntegerGene) and then calling the compareTo-method
   * of the casted type.
   * @param o1 first object to be compared, always is not null
   * @param o2 second object to be compared, always is not null
   * @return a negative integer, zero, or a positive integer as this object
   *	       is less than, equal to, or greater than the object provided for
   *         comparison.
   * @since 1.1
   */
  protected int compareToNative(Object o1, Object o2) {
    return ( (Double) o1).compareTo(o2);
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
   * @author Klaus Meffert
   * @since 1.1
   */
  protected void mapValueToWithinBounds() {
    if (m_value != null) {
      Double d_value = ( (Double) m_value);
      // If the value exceeds either the upper or lower bounds, then
      // map the value to within the legal range. To do this, we basically
      // calculate the distance between the value and the double min,
      // determine how many bounds units that represents, and then add
      // that number of units to the upper bound.
      // -----------------------------------------------------------------
      if (d_value.doubleValue() > m_upperBounds ||
          d_value.doubleValue() < m_lowerBounds) {
/*
        double mult = (DOUBLE_RANGE - d_value.doubleValue())
            / DOUBLE_RANGE;
        m_value = new Double( (m_upperBounds - m_lowerBounds) * mult +
                             m_lowerBounds);
        double m = ( (Double) m_value).doubleValue();
*/
        double differenceFromDoubleMin = Double.MIN_VALUE +
            d_value.doubleValue();
        double differenceFromBoundsMin =
            (differenceFromDoubleMin / m_boundsUnitsToDoubleUnits);
        m_value =
            new Double(m_upperBounds + differenceFromBoundsMin);
      }
    }
  }

  /**
   * Calculates and sets the m_boundsUnitsToDoubleUnits field based
   * on the current lower and upper bounds of this DoubleGene. For example,
   * if the double range is -2 billion to +2 billion and the bounds range
   * is -1 billion to +1 billion, then each unit in the bounds range would
   * map to 2 units in the double range. The m_boundsUnitsToDoubleUnits
   * field would therefore be 2. This mapping unit is used to map illegal
   * allele values that are outside of the bounds to legal allele values that
   * are within the bounds.
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  protected void calculateBoundsUnitsToDoubleUnitsRatio() {
    double divisor = m_upperBounds - m_lowerBounds + 1.0d;
    if (divisor == 0) {
      m_boundsUnitsToDoubleUnits = DOUBLE_RANGE;
    }
    else {
      m_boundsUnitsToDoubleUnits = DOUBLE_RANGE / divisor;
    }
  }

  /**
   * See interface Gene for description
   * @param index must always be 1 (because there is only 1 atomic element)
   * @param a_percentage percentage of mutation (greater than -1 and smaller
   *        than 1).
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void applyMutation(int index, double a_percentage) {
    double newValue = doubleValue() * (1 + a_percentage);
    setAllele(new Double(newValue));
  }

  /**
   * See NumberGene.setAllele(Object)
   * @param a_newValue sic
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void setAllele(Object a_newValue) {
    Double d = (Double) a_newValue;
    if (a_newValue != null) {
      double fromD = d.doubleValue();
      if (fromD < m_lowerBounds || fromD > m_upperBounds) {
        throw new IllegalArgumentException(
            "Allele must be a double value matching"
            + " the lower and upper bounds of the"
            + " chromosome ["
            +m_lowerBounds
            +", "
            +m_upperBounds
            +"] !");
      }
    }
    if (m_geneAlleleChecker != null) {
      if (!m_geneAlleleChecker.verify(this, a_newValue)) {
        return;
      }
    }
    m_value = a_newValue;
    mapValueToWithinBounds();
  }

  /**
   * Sets the constraint checker to be used for this gene whenever method
   * setAllele(Object a_newValue) is called
   * @param a_constraintChecker the constraint checker to be set
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void setConstraintChecker(IGeneConstraintChecker a_constraintChecker) {
    m_geneAlleleChecker = a_constraintChecker;
  }

  /**
   * @return IGeneConstraintChecker the constraint checker to be used whenever
   * method setAllele(Object a_newValue) is called
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public IGeneConstraintChecker getConstraintChecker() {
    return m_geneAlleleChecker;
  }

}
