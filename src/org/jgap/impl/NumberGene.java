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

import org.jgap.*;

/**
 * Base class for all Genes based on numbers.
 * Known implementations: IntegerGene, DoubleGene
 *
 * @author Klaus Meffert
 * @since 1.1 (most code moved and adapted from IntegerGene)
 */
public abstract class NumberGene
    implements Gene {

  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.7 $";

  /**
   * References the internal value (allele) of this Gene
   * E.g., for DoubleGene this is of type Double
   */
  protected Object m_value = null;

  /**
   * Optional helper class for checking if a given allele value to be set
   * is valid. If not the allele value may not be set for the gene!
   */
  private IGeneConstraintChecker m_geneAlleleChecker;

  /**
   * Executed by the genetic engine when this Gene instance is no
   * longer needed and should perform any necessary resource cleanup.
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void cleanup() {
    // No specific cleanup is necessary for this implementation.
    // ---------------------------------------------------------
  }

  /**
   * Compares this IntegerGene with the given object and returns true if
   * the other object is a IntegerGene and has the same value (allele) as
   * this IntegerGene. Otherwise it returns false.
   *
   * @param other the object to compare to this IntegerGene for equality.
   * @return true if this Gene is equal to the given object,
   *         false otherwise.
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public boolean equals(Object other) {
    try {
      return compareTo(other) == 0;
    }
    catch (ClassCastException e) {
      // If the other object isn't an Gene of current type
      // (like IntegerGene), then we're not equal.
      // -------------------------------------------------
      return false;
    }
  }

  /**
   * Retrieves the hash code value for this IntegerGene.
   *
   * @return this IntegerGene's hash code.
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public int hashCode() {
    // If our internal Integer is null, then return zero. Otherwise,
    // just return the hash code of the Integer.
    // -------------------------------------------------------------
    if (m_value == null) {
      return 0;
    }
    else {
      return m_value.hashCode();
    }
  }

  /**
   * Compares this IntegerGene with the specified object (which must also
   * be an IntegerGene) for order, which is determined by the integer
   * value of this Gene compared to the one provided for comparison.
   *
   * @param  other the IntegerGene to be compared to this IntegerGene.
   * @return a negative integer, zero, or a positive integer as this object
   *	       is less than, equal to, or greater than the object provided for
   *         comparison.
   *
   * @throws ClassCastException if the specified object's type prevents it
   *         from being compared to this IntegerGene.
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public int compareTo(Object other) {
    NumberGene otherGene = (NumberGene) other;
    // First, if the other gene (or its value) is null, then this is
    // the greater allele. Otherwise, just use the Integer's compareTo
    // method to perform the comparison.
    // ---------------------------------------------------------------
    if (otherGene == null) {
      return 1;
    }
    else if (otherGene.m_value == null) {
      // If our value is also null, then we're the same. Otherwise,
      // this is the greater gene.
      // ----------------------------------------------------------
      return m_value == null ? 0 : 1;
    }
    else {
      try {
        return compareToNative(m_value, otherGene.m_value);
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
   * @param o1 first object to be compared, always is not null
   * @param o2 second object to be compared, always is not null
   * @return a negative integer, zero, or a positive integer as this object
   *	       is less than, equal to, or greater than the object provided for
   *         comparison.
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  protected abstract int compareToNative(Object o1, Object o2);

  /**
   * Retrieves a string representation of this IntegerGene's value that
   * may be useful for display purposes.
   *
   * @return a string representation of this IntegerGene's value.
  *
   * @author Klaus Meffert
   * @since 1.1
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
   * Sets the value (allele) of this Gene to the new given value. This class
   * expects the value to be an instance of current type (e.g. Integer).
   * If the value is above or below the upper or lower bounds, it will be
   * mappped to within the allowable range.
   *
   * @param a_newValue the new value of this Gene instance.
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void setAllele(Object a_newValue) {
    if (m_geneAlleleChecker != null) {
      if (!m_geneAlleleChecker.verify(this, a_newValue)) {
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

  /**
   * Retrieves the value (allele) represented by this Gene. All values
   * returned by this class will be Integer instances.
   *
   * @return the Integer value of this Gene.
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public Object getAllele() {
    return m_value;
  }

  /**
   * @return the size of the gene, i.e the number of atomic elements.
   *         Always 1 for numbers
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public int size() {
    return 1;
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
   * @author Klaus Meffert
   * @since 1.1
   */
  protected abstract void mapValueToWithinBounds();

  /**
   * Applies a mutation of a given intensity (percentage) onto the atomic
   * element at given index (NumberGenes only have one atomic element)
   *
   * @param index index of atomic element, between 0 and size()-1
   * @param a_percentage percentage of mutation (greater than -1 and smaller
   *        than 1).
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public abstract void applyMutation(int index, double a_percentage);

}
