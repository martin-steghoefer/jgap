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
 * package (see <a href="http://www.jaga.org">jaga</a>).
 *
 * @author Klaus Meffert
 * @since 2.0
 */
public class FixedBinaryGene
    extends BaseGene implements IPersistentRepresentation {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.40 $";

  private int m_length;

  private int[] m_value;

  private static final int WORD_LEN_BITS = 32;

  /**
   *
   * @param a_config the configuration to use
   * @param a_length the fixed length of the gene
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public FixedBinaryGene(final Configuration a_config, final int a_length)
      throws InvalidConfigurationException {
    super(a_config);
    if (a_length < 1) {
      throw new IllegalArgumentException("Length must be greater than zero!");
    }
    m_length = a_length;
    int bufSize = m_length / WORD_LEN_BITS;
    if (0 != m_length % WORD_LEN_BITS) {
      ++bufSize;
    }
    m_value = new int[bufSize];
    for (int i = 0; i < bufSize; i++) {
      m_value[i] = 0;
    }
  }

  protected Gene newGeneInternal() {
    try {
      FixedBinaryGene result = new FixedBinaryGene(getConfiguration(), m_length);
      return result;
    }
    catch (InvalidConfigurationException iex) {
      throw new IllegalStateException(iex.getMessage());
    }
  }

  public FixedBinaryGene(final Configuration a_config,
                         final FixedBinaryGene a_toCopy)
      throws InvalidConfigurationException {
    super(a_config);
    m_length = a_toCopy.getLength();
    int bufSize = m_length / WORD_LEN_BITS;
    if (0 != m_length % WORD_LEN_BITS) {
      ++bufSize;
    }
    m_value = new int[bufSize];
    System.arraycopy(a_toCopy.getValue(), 0, m_value, 0, m_value.length);
  }

  protected int[] getValue() {
    return m_value;
  }

  public int getLength() {
    return m_length;
  }

  public Object clone() {
    try {
      return new FixedBinaryGene(getConfiguration(), this);
    }
    catch (InvalidConfigurationException iex) {
      throw new IllegalStateException(iex.getMessage());
    }
  }

  public void setAllele(final Object a_newValue) {
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
      if (!getConstraintChecker().verify(this, a_newValue, null, -1)) {
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

  /**
   * @return internal representation of the gene's state. Use getBit for reading
   * bits!
   */
  public int[] getIntValues() {
    return m_value;
  }

  public boolean getBit(final int a_index) {
    checkIndex(a_index);
    return getUnchecked(a_index);
  }

  public void setBit(final int a_index, final boolean a_value) {
    checkIndex(a_index);
    setUnchecked(a_index, a_value);
  }

  public void setBit(final int a_index, final int a_value) {
    if (a_value > 0) {
      if (a_value != 1) {
        throw new IllegalArgumentException("Only values 0 and 1 are valid!");
      }
      setBit(a_index, true);
    }
    else {
      if (a_value != 0) {
        throw new IllegalArgumentException("Only values 0 and 1 are valid!");
      }
      setBit(a_index, false);
    }
  }

  public void setBit(final int a_from, final int a_to, final boolean a_value) {
    checkSubLength(a_from, a_to);
    for (int i = a_from; i < a_to; i++) {
      setUnchecked(i, a_value);
    }
  }

  public void setBit(final int a_from, final int a_to,
                     final FixedBinaryGene a_values) {
    if (a_values.getLength() == 0) {
      throw new IllegalArgumentException("Length of values must be > 0");
    }
    checkSubLength(a_from, a_to);
    int iV = 0;
    for (int i = a_from; i <= a_to; i++, iV++) {
      if (iV >= a_values.getLength()) {
        iV = 0;
      }
      setUnchecked(i, a_values.getUnchecked(iV));
    }
  }

  public FixedBinaryGene substring(final int a_from, final int a_to) {
    try {
      int len = checkSubLength(a_from, a_to);
      FixedBinaryGene substring = new FixedBinaryGene(getConfiguration(), len);
      for (int i = a_from; i <= a_to; i++) {
        substring.setUnchecked(i - a_from, getUnchecked(i));
      }
      return substring;
    }
    catch (InvalidConfigurationException iex) {
      throw new IllegalStateException(iex.getMessage());
    }
  }

  public void flip(final int a_index) {
    checkIndex(a_index);
    int segment = a_index / WORD_LEN_BITS;
    int offset = a_index % WORD_LEN_BITS;
    int mask = 0x1 << (WORD_LEN_BITS - offset - 1);
    m_value[segment] ^= mask;
  }

  protected int checkSubLength(final int a_from, final int a_to) {
    checkIndex(a_from);
    checkIndex(a_to);
    int sublen = a_to - a_from + 1;
    if (0 >= sublen) {
      throw new IllegalArgumentException("must have 'from' <= 'to', but has "
                                         + a_from + " > " + a_to);
    }
    return sublen;
  }

  protected void checkIndex(final int a_index) {
    if (a_index < 0 || a_index >= getLength()) {
      throw new IndexOutOfBoundsException("index is " + a_index
                                          + ", but must be in [0, "
                                          + (getLength() - 1) + "]");
    }
  }

  protected boolean getUnchecked(final int a_index) {
    int segment = a_index / WORD_LEN_BITS;
    int offset = a_index % WORD_LEN_BITS;
    int mask = 0x1 << (WORD_LEN_BITS - offset - 1);
    return 0 != (m_value[segment] & mask);
  }

  public void setUnchecked(final int a_index, final boolean a_value) {
    int segment = a_index / WORD_LEN_BITS;
    int offset = a_index % WORD_LEN_BITS;
    int mask = 0x1 << (WORD_LEN_BITS - offset - 1);
    if (a_value) {
      m_value[segment] |= mask;
    }
    else {
      m_value[segment] &= ~mask;
    }
  }

  public String getPersistentRepresentation() {
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
   * prior call to the getPersistentRepresentation() method
   *
   * @throws UnsupportedRepresentationException if this Gene implementation
   * does not support the given string representation
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void setValueFromPersistentRepresentation(String a_representation)
      throws UnsupportedRepresentationException {
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
   * @param a_representation the representation to check
   * @return true: representation is valid in general
   * @author Klaus Meffert
   * @since 2.0
   */
  private boolean isValidRepresentation(final String a_representation) {
    if (!a_representation.startsWith("[") || !a_representation.endsWith("]")) {
      return false;
    }
    return true;
  }

  public void setToRandomValue(final RandomGenerator a_numberGenerator) {
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

  @Override
  public String getBusinessKey() {
    return toString();
  }

  /**
   * @return the size of the gene, i.e the number of atomic elements
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
   * @param a_index index of atomic element, between 0 and size()-1
   * @param a_percentage percentage of mutation (greater than -1 and smaller
   * than 1)
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void applyMutation(final int a_index, final double a_percentage) {
    if (a_index < 0 || a_index >= getLength()) {
      throw new IllegalArgumentException(
          "Index must be between 0 and getLength() - 1");
    }
    if (a_percentage > 0) {
      // change to 1
      // ---------------
      if (!getBit(a_index)) {
        setBit(a_index, true);
      }
    }
    else if (a_percentage < 0) {
      // change to 0
      // ---------------
      if (getBit(a_index)) {
        setBit(a_index, false);
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
   * @param a_other the FixedBinaryGene to be compared
   * @return a negative integer, zero, or a positive integer as this object
   * is less than, equal to, or greater than the specified object.
   *
   * @throws ClassCastException if the specified object's type prevents it
   * from being compared to this Gene
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public int compareTo(final Object a_other) {
    FixedBinaryGene otherGene = (FixedBinaryGene) a_other;
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
    // Compare application data, if possible.
    // --------------------------------------
    if (isCompareApplicationData()) {
      return compareApplicationData(getApplicationData(),
                                    otherGene.getApplicationData());
    }
    else {
      return 0;
    }
  }

  /**
   * Not called as getAllele() is overridden.
   *
   * @return same as getAllele()
   */
  protected Object getInternalValue() {
    return m_value;
  }

  /**
   * Modified hashCode() function to return different hashcodes for differently
   * ordered genes in a chromosome --> does not work as internal value always
   * initialized!
   *
   * @return this Gene's hash code
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public int hashCode() {
    int result = 0;
    for (int i = 0; i < m_value.length; i++) {
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
