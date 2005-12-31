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
 * A Gene implementation that supports two possible values (alleles, 1 and 0)
 * with a fixed length of alleles.
 * <p>
 * NOTE: Since this Gene implementation only supports two different
 * values (1 and 0), there's only a 50% chance that invocation
 * of the setToRandomValue() method will actually change the value of
 * this Gene (if it has a value). As a result, it may be desirable to
 * use a higher overall mutation rate when this Gene implementation
 * is in use.
 * <p>
 * Partly adapted stuff from the JAGA (Java API for Genetic Algorithms)
 * package (see {@link http://www.jaga.org}).
 *
 * @author Klaus Meffert
 * @since 2.0
 */
public class FixedBinaryGene
    extends BaseGene {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.25 $";

  private int m_length;

  private int[] m_value;

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
    if (0 != m_length % 32) {
      ++bufSize;
    }
    m_value = new int[bufSize];
    for (int i = 0; i < bufSize; i++) {
      m_value[i] = 0;
    }
  }

  public Gene newGene() {
    FixedBinaryGene result = new FixedBinaryGene(m_length);
    result.setConstraintChecker(getConstraintChecker());
    return result;
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
    if (getConstraintChecker() != null) {
      if (!getConstraintChecker().verify(this, a_newValue)) {
        return;
      }
    }
    int[] bits = (int[]) a_newValue;
    for (int i = 0; i < bits.length; i++) {
      setBit(i, bits[i]);
    }
  }

  public Object getAllele() {
    int[] bits = new int[getLength()];
    for (int i = 0; i < getLength(); i++) {
      if (getBit(i)) {
        bits[i] = 1;
      }
      else {
        bits[i] = 0;
      }
    }
    return bits;
  }

  public int[] getIntValues() {
    return m_value;
  }

  public boolean getBit(int m_index) {
    checkIndex(m_index);
    return getUnchecked(m_index);
  }

  public void setBit(int m_index, boolean m_value) {
    checkIndex(m_index);
    setUnchecked(m_index, m_value);
  }

  public void setBit(int m_index, int m_value) {
    if (m_value > 0) {
      if (m_value != 1) {
        throw new IllegalArgumentException("Only values 0 and 1 are valid!");
      }
      setBit(m_index, true);
    }
    else {
      if (m_value != 0) {
        throw new IllegalArgumentException("Only values 0 and 1 are valid!");
      }
      setBit(m_index, false);
    }
  }

  public void setBit(int m_from, int m_to, boolean m_value) {
    checkSubLength(m_from, m_to);
    for (int i = m_from; i < m_to; setUnchecked(i++, m_value));
  }

  public void setBit(int from, int to, FixedBinaryGene values) {
    if (values.getLength() == 0) {
      throw new IllegalArgumentException("Length of values must be > 0");
    }
    checkSubLength(from, to);
    int iV = 0;
    for (int i = from; i <= to; i++, iV++) {
      if (iV >= values.getLength()) {
        iV = 0;
      }
      setUnchecked(i, values.getUnchecked(iV));
    }
  }

  public FixedBinaryGene substring(int m_from, int m_to) {
    int len = checkSubLength(m_from, m_to);
    FixedBinaryGene substring = new FixedBinaryGene(len);
    for (int i = m_from; i <= m_to; i++)
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
    checkIndex(to);
    int sublen = to - from + 1;
    if (0 >= sublen) {
      throw new IllegalArgumentException("must have 'from' <= 'to', but has "
                                         + from + " > " + to);
    }
    return sublen;
  }

  protected void checkIndex(int index)
      throws IndexOutOfBoundsException {
    if (index < 0 || index >= getLength()) {
      throw new IndexOutOfBoundsException("index is " + index
                                          + ", but must be in [0, "
                                          + (getLength() - 1) + "]");
    }
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
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void setValueFromPersistentRepresentation(String a_representation)
      throws UnsupportedOperationException, UnsupportedRepresentationException {
    if (a_representation != null) {
      if (isValidRepresentation(a_representation)) {
        a_representation = a_representation.substring(1,
            a_representation.length() - 1);
        StringTokenizer st = new StringTokenizer(a_representation, ",");
        int index = 0;
        while (st.hasMoreTokens()) {
          int i = Integer.parseInt(st.nextToken());
          setBit(index++, i);
        }
        if (index < getLength()) {
          throw new UnsupportedRepresentationException(
              "Invalid gene representation: " + a_representation);
        }
      }
      else {
        throw new UnsupportedRepresentationException(
            "Invalid gene representation: " + a_representation);
      }
    }
    else {
      throw new UnsupportedRepresentationException(
          "The input parameter must not be null!");
    }
  }

  /**
   * Verifies if the String is a valid representation of this Gene type
   * in general (bit values will not be checked)
   * @param a_representation String
   * @return boolean
   * @author Klaus Meffert
   * @since 2.0
   */
  private boolean isValidRepresentation(String a_representation) {
    if (a_representation == null) {
      return false;
    }
    if (!a_representation.startsWith("[") || !a_representation.endsWith("]")) {
      return false;
    }
    return true;
  }

  public void setToRandomValue(RandomGenerator a_numberGenerator) {
    if (a_numberGenerator == null) {
      throw new IllegalArgumentException("Random Generator must not be null!");
    }
    int len = getLength();
    for (int i = 0; i < len; i++) {
      setBit(i, a_numberGenerator.nextBoolean());
    }
  }

  /**
   * @return String representation
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public String toString() {
    int len = getLength();
    String s = "FixedBinaryGene[";
    int value;
    for (int i = 0; i < len; i++) {
      if (getBit(i)) {
        value = 1;
      }
      else {
        value = 0;
      }
      if (i == 0) {
        s += value;
      }
      else {
        s += "," + value;
      }
    }
    return s + "]";
  }

  /**
   * @return the size of the gene, i.e the number of atomic elements.
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public int size() {
    return m_value.length;
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
      if (!getBit(index)) {
        setBit(index, true);
      }
    }
    else if (a_percentage < 0) {
      // change to 0
      // ---------------
      if (getBit(index)) {
        setBit(index, false);
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
    int thisLen = getLength();
    int otherLen = otherGene.getLength();
    if (thisLen != otherLen) {
      if (thisLen > otherLen) {
        return 1;
      }
      else {
        return -1;
      }
    }
    boolean b1, b2;
    for (int i = 0; i < thisLen; i++) {
      b1 = getBit(i);
      b2 = otherGene.getBit(i);
      if (b1) {
        if (!b2) {
          return 1;
        }
      }
      else {
        if (b2) {
          return -1;
        }
      }
    }
    // Determine int value of binary representation
    return 0;
  }

  protected Object getInternalValue() {
    return m_value;
  }

  /**
   * Modified hashCode() function to return different hashcodes for differently
   * ordered genes in a chromosome --> does not work as internal value always
   * initialized!
   * @return this Gene's hash code
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public int hashCode() {
    int result;
    if (m_value.length < 1) {
      result = -9945;
    }
    else {
      result = 0;
    }
    for (int i=0;i<m_value.length;i++) {
      if (m_value[i] == 0) {
        result += 31 * result + 47;
      }
      else {
        result += 31 * result + 91;
      }
    }
    return result;
  }
}
