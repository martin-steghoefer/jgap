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
 * A Gene implementation that supports two possible values (alleles, 1 and 0)
 * with a fixed length of alleles
 * <p>
 * NOTE: Since this Gene implementation only supports two different
 * values (1 and 0), there's only a 50% chance that invocation
 * of the setToRandomValue() method will actually change the value of
 * this Gene (if it has a value). As a result, it may be desirable to
 * use a higher overall mutation rate when this Gene implementation
 * is in use.
 * <p>
 * Partly adapted stuff from the JAGA (Java API for Genetic Algorithms)
 * package (see http://www.jaga.org).
 *
 * @author Klaus Meffert
 * @since 2.0
 */
public class FixedBinaryGene
    implements Gene {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.7 $";

  private int m_length;

  private int[] m_value;

  /**
   * Optional helper class for checking if a given allele value to be set
   * is valid. If not the allele value may not be set for the gene!
   */
  private IGeneConstraintChecker m_geneAlleleChecker;

  /**
   *
   * @param a_length the fixed length of the gene
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public FixedBinaryGene(int a_length) {
    if (a_length < 1) {
      throw new IllegalArgumentException("Length must be greater than zero!");
    }
    m_length = a_length;
    int bufSize = m_length / 32;
    if (0 != m_length % 32)
      ++bufSize;
    m_value = new int[bufSize];
    for (int i = 0; i < bufSize; m_value[i++] = 0);
  }

  public Gene newGene() {
    return new FixedBinaryGene(m_length);
  }

  public FixedBinaryGene(final FixedBinaryGene toCopy) {
    m_length = toCopy.getLength();
    int bufSize = m_length / 32;
    if (0 != m_length % 32)
      ++bufSize;
    m_value = new int[bufSize];
    System.arraycopy(toCopy.getValue(), 0, m_value, 0, m_value.length);
  }

  protected int[] getValue() {
    return m_value;
  }

  public int getLength() {
    return m_length;
  }

  public Object clone() {
    FixedBinaryGene copy = new FixedBinaryGene(getLength());
    System.arraycopy(m_value, 0, copy.getValue(), 0, copy.getLength());
    return copy;
  }

  public void setAllele(Object a_newValue) {
    if (a_newValue == null) {
      throw new IllegalArgumentException("Allele must not be null!");
    }
    if ( ( (int[]) a_newValue).length != getLength()) {
      throw new IllegalArgumentException("Length of allele must be equal to"
                                         + " fixed length set ("
                                         + getLength()
                                         + ")");
    }
    if (m_geneAlleleChecker != null) {
      if (!m_geneAlleleChecker.verify(this, a_newValue)) {
        return;
      }
    }
    m_value = (int[]) a_newValue;
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

  public Object getAllele() {
    return m_value;
  }

  public int[] getIntValues() {
    return m_value;
  }

  public void setBit(int m_index, boolean m_value) {
    checkIndex(m_index);
    setUnchecked(m_index, m_value);
  }

  public void setBit(int m_from, int m_to, boolean m_value) {
    checkSubLength(m_from, m_to);
    for (int i = m_from; i < m_to; setUnchecked(i++, m_value));
  }

  public void setBit(int from, int to, FixedBinaryGene values) {
    if (values.getLength() == 0)
      throw new IllegalArgumentException("Length of values must be > 0");
    int len = checkSubLength(from, to);
    int iV = 0;
    for (int i = from; i < to; i++, iV++) {
      if (iV >= values.getLength())
        iV = 0;
      setUnchecked(i, values.getUnchecked(iV));
    }
  }

  public FixedBinaryGene substring(int m_from, int m_to) {
    int len = checkSubLength(m_from, m_to);
    FixedBinaryGene substring = new FixedBinaryGene(len);
    for (int i = m_from; i < m_to; i++)
      substring.setUnchecked(i - m_from, getUnchecked(i));
    return substring;
  }

  public void flip(int index) {
          checkIndex(index);
          int segment = index / 32;
          int offset = index % 32;
          int mask = 0x1 << (32 - offset - 1);
          m_value[segment] ^= mask;
  }


  protected int checkSubLength(int from, int to)
      throws IndexOutOfBoundsException {
    checkIndex(from);
    checkIndex(to - 1);
    int sublen = to - from;
    if (0 > sublen)
      throw new IllegalArgumentException("must have 'from' <= 'to', but has "
                                         + from + " > " + to);
    return sublen;
  }

  protected void checkIndex(int index)
      throws IndexOutOfBoundsException {
    if (index < 0 || index >= getLength())
      throw new IndexOutOfBoundsException("index is " + index
                                          + ", but must be in [0, "
                                          + (getLength() - 1) + "]");
  }

  protected boolean getUnchecked(int index) {
    int segment = index / 32;
    int offset = index % 32;
    int mask = 0x1 << (32 - offset - 1);
    return 0 != (m_value[segment] & mask);
  }

  public void setUnchecked(int index, boolean value) {
    int segment = index / 32;
    int offset = index % 32;
    int mask = 0x1 << (32 - offset - 1);
    if (value)
      m_value[segment] |= mask;
    else
      m_value[segment] &= ~mask;
  }

  public String getPersistentRepresentation()
      throws UnsupportedOperationException {
    return m_value.toString();
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
   * @since 2.0
   */
  public void setValueFromPersistentRepresentation(String a_representation)
      throws UnsupportedOperationException, UnsupportedRepresentationException {
    if (a_representation != null) {
      if (a_representation.equals("null")) {
        m_value = null;
      }
      else if (isValidRepresentation(a_representation)) {
        /**@todo implement*/
      }
      else {
        throw new UnsupportedRepresentationException(
            "Invalid gene representation: " +
            a_representation);
      }
    }
    else {
      throw new UnsupportedRepresentationException(
          "The input parameter must not be null!");
    }
  }

  /**
   * Verifies if the String is a valid representation of this Gene type
   * @param a_representation String
   * @return boolean
   * @author Klaus Meffert
   * @since 2.0
   */
  private boolean isValidRepresentation(String a_representation) {
    /**@todo implement*/
    return true;
  }

  public void setToRandomValue(RandomGenerator a_numberGenerator) {
    int len = getLength();
    for (int i = 0; i < len; i++) {
      if (a_numberGenerator.nextBoolean() == true) {
        m_value[i] = 1;
      }
      else {
        m_value[i] = 0;
      }
    }
  }

  /**
   * Executed by the genetic engine when this Gene instance is no
   * longer needed and should perform any necessary resource cleanup.
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void cleanup() {
    // No specific cleanup is necessary for this implementation.
    // ---------------------------------------------------------
  }

  /**
   * @return String
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public String toString() {
    if (m_value == null) {
      return "null";
    }
    else {
      int len = getLength();
      String s = "[";
      int value;
      for (int i = 0; i < len; i++) {
        value = m_value[i];
        if (i == 0) {
          s += value;
        }
        else {
          s += "," + value;
        }
      }
      return s + "]";
    }
  }

  /**
   * @return the size of the gene, i.e the number of atomic elements.
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public int size() {
    return getLength();
  }

  /**
   * Retrieves the hash code value of this Gene.
   *
   * @return this Gene's hash code.
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public int hashCode() {
    // If the internal value hasn't been set, return zero. Otherwise,
    // just return the value's hash code.
    // ----------------------------------------------------------------
    if (m_value == null) {
      return 0;
    }
    else {
      return m_value.hashCode();
    }
  }

  /**
   * Applies a mutation of a given intensity (percentage) onto the atomic
   * element at given index
   * @param index index of atomic element, between 0 and size()-1
   * @param a_percentage percentage of mutation (greater than -1 and smaller
   *        than 1).
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void applyMutation(int index, double a_percentage) {
    if (index < 0 || index >= getLength()) {
      throw new IllegalArgumentException(
          "Index must be between 0 and getLength() - 1");
    }
    if (a_percentage > 0) {
      // change to 1
      // ---------------
      if (m_value[index] == 0) {
        m_value[index] = 1;
      }
    }
    else if (a_percentage < 0) {
      // change to 0
      // ---------------
      if (m_value[index] == 1) {
        m_value[index] = 0;
      }
    }
  }

  /**
   * Compares this Gene with the specified object for order. A
   * 0 value is considered to be less than a 1 value. A null value
   * is considered to be less than any non-null value.
   * A Gene is greater than another one of the same length if it has more 1's
   * than the other one. If there is the same number of 1's the Gene with the
   * highest value (binary to int) is greater.
   *
   * @param  other the FixedBinaryGene to be compared.
   * @return  a negative integer, zero, or a positive integer as this object
   *		is less than, equal to, or greater than the specified object.
   *
   * @throws ClassCastException if the specified object's type prevents it
   *         from being compared to this Gene.
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public int compareTo(Object other) {
    FixedBinaryGene otherGene = (FixedBinaryGene) other;
    // First, if the other gene is null, then this is the greater gene.
    // ----------------------------------------------------------------
    if (otherGene == null) {
      return 1;
    }
    else if (otherGene.m_value == null) {
      // If our value is also null, then we're the same. Otherwise,
      // we're the greater gene.
      // ----------------------------------------------------------
      return m_value == null ? 0 : 1;
    }
    int thisLen = getLength();
    int otherLen = otherGene.getLength();
    if (thisLen != otherLen) {
      if (thisLen > otherLen) {
        return 1;
      }
    }
    else {
      return -1;
    }
    // Count number of 1's for this Gene
    int this1s = 0;
    for (int i = 0; i < thisLen; i++) {
      if (m_value[i] == 1) {
        this1s++;
      }
      // Count number of 1's for other Gene
      int other1s = 0;
      for (i = 0; i < thisLen; i++) {
        if (otherGene.m_value[i] == 1) {
          other1s++;
        }
      }
      if (this1s != other1s) {
        if (this1s > other1s) {
          return 1;
        }
        else {
          return -1;
        }
      }
    }
    // Determine int value of binary representation
    return 0;
  }
}
