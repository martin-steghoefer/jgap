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
 * Base class for all Genes based on numbers.
 *
 * @author Klaus Meffert
 * @since 1.1 (most code moved and adapted from IntegerGene)
 */
public abstract class NumberGene
    extends BaseGene {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.23 $";

  /**
   * References the internal value (allele) of this Gene
   * E.g., for DoubleGene this is of type Double
   */
  private Object m_value;

  public NumberGene(Configuration a_config) throws InvalidConfigurationException {
    super(a_config);
  }

  /**
   * Compares this NumberGene with the specified object (which must also
   * be a NumberGene) for order, which is determined by the number value of
   * this Gene compared to the one provided for comparison.
   *
   * @param a_other the NumberGene to be compared to this NumberGene
   * @return a negative integer, zero, or a positive integer as this object
   * is less than, equal to, or greater than the object provided for comparison
   *
   * @throws ClassCastException if the specified object's type prevents it
   * from being compared to this NumberGene
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public int compareTo(final Object a_other) {
    NumberGene otherGene = (NumberGene) a_other;
    // First, if the other gene (or its value) is null, then this is
    // the greater allele. Otherwise, just use the overridden compareToNative
    // method to perform the comparison.
    // ----------------------------------------------------------------------
    if (otherGene == null) {
      return 1;
    }
    else if (otherGene.m_value == null) {
      // Check if type corresponds (because we could have a type not inherited
      // from NumberGene).
      // ---------------------------------------------------------------------
      if (!otherGene.getClass().equals(getClass())) {
        throw new ClassCastException(
            "Comparison not possible: different types!");
      }
      // If our value is also null, then we're the same. Otherwise,
      // this is the greater gene.
      // ----------------------------------------------------------
      if (m_value == null) {
        if (isCompareApplicationData()) {
          return compareApplicationData(getApplicationData(),
                                        otherGene.getApplicationData());
        }
        else {
          return 0;
        }
      }
      else {
        return 1;
      }
    }
    else {
      try {
        if (!otherGene.getClass().equals(getClass())) {
          throw new ClassCastException(
              "Comparison not possible: different types!");
        }
        if (m_value == null) {
          return -1;
        }
        int res = compareToNative(m_value, otherGene.m_value);
        if (res == 0) {
          if (isCompareApplicationData()) {
            return compareApplicationData(getApplicationData(),
                                          otherGene.getApplicationData());
          }
          else {
            return 0;
          }
        }
        else {
          return res;
        }
      }
      catch (ClassCastException e) {
        throw e;
      }
    }
  }

  /**
   * Compares to objects by first casting them into their expected type
   * (e.g. Integer for IntegerGene) and then calling the compareTo-method
   * of the casted type.
   *
   * @param a_o1 first object to be compared, always is not null
   * @param a_o2 second object to be compared, always is not null
   * @return a negative integer, zero, or a positive integer as this object
   * is less than, equal to, or greater than the object provided for comparison
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  protected abstract int compareToNative(Object a_o1, Object a_o2);

  /**
   * Sets the value (allele) of this Gene to the new given value. This class
   * expects the value to be an instance of current type (e.g. Integer).
   * If the value is above or below the upper or lower bounds, it will be
   * mappped to within the allowable range.
   *
   * @param a_newValue the new value of this Gene instance
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void setAllele(final Object a_newValue) {
    if (getConstraintChecker() != null) {
      if (!getConstraintChecker().verify(this, a_newValue, null, -1)) {
        return;
      }
    }
    m_value = a_newValue;
    // If the value isn't between the upper and lower bounds of this
    // Gene, map it to a value within those bounds.
    // -------------------------------------------------------------
    mapValueToWithinBounds();
  }

  /**
   * Maps the value of this NumberGene to within the bounds specified by
   * the m_upperBounds and m_lowerBounds instance variables. The value's
   * relative position within the integer range will be preserved within the
   * bounds range (in other words, if the value is about halfway between the
   * integer max and min, then the resulting value will be about halfway
   * between the upper bounds and lower bounds). If the value is null or
   * is already within the bounds, it will be left unchanged.
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  protected abstract void mapValueToWithinBounds();

  protected Object getInternalValue() {
    return m_value;
  }
}
