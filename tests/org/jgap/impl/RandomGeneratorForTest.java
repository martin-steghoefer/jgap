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
 * A random generator only determined for testing purposes.
 * With this, you can specify the next value which will be returned.
 * It is also possible to specify a sequence to be appearing.
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class RandomGeneratorForTest
    implements RandomGenerator {

  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.10 $";

  private long m_nextLong;
  private double m_nextDouble;
  private float m_nextFloat;
  private boolean m_nextBoolean;
  private int[] m_nextIntSequence;
  private double m_nextGaussian;
  private int m_intIndex;
  public RandomGeneratorForTest() {
  }

  public RandomGeneratorForTest(int nextInt) {
    this();
    setNextInt(nextInt);
  }

  public RandomGeneratorForTest(double nextDouble) {
    this();
    setNextDouble(nextDouble);
  }

  public RandomGeneratorForTest(float nextFloat) {
    this();
    setNextFloat(nextFloat);
  }

  public RandomGeneratorForTest(long nextLong) {
    this();
    setNextLong(nextLong);
  }

  public RandomGeneratorForTest(boolean nextBoolean) {
    this();
    setNextBoolean(nextBoolean);
  }

  public int nextInt() {
    int result = m_nextIntSequence[m_intIndex++];
    if (m_intIndex >= m_nextIntSequence.length) {
      m_intIndex = 0;
    }
    return result;
  }

  public int nextInt(int ceiling) {
    return nextInt() % ceiling;
  }

  public long nextLong() {
    return m_nextLong;
  }

  public double nextDouble() {
    return m_nextDouble;
  }

  public double nextGaussian() {
    return m_nextGaussian;
  }

  public float nextFloat() {
    return m_nextFloat;
  }

  public boolean nextBoolean() {
    return m_nextBoolean;
  }

  public void setNextBoolean(boolean a_nextBoolean) {
    m_nextBoolean = a_nextBoolean;
  }

  public void setNextDouble(double a_nextDouble) {
    m_nextDouble = a_nextDouble % 1.0d;
  }

  public void setNextGaussian(double a_nextDouble) {
    m_nextGaussian = a_nextDouble;
  }

  public void setNextFloat(float a_nextFloat) {
    m_nextFloat = a_nextFloat % 1.0f;
  }

  public void setNextInt(int a_nextInt) {
    setNextIntSequence(new int[] {a_nextInt});
  }

  public void setNextLong(long a_nextLong) {
    m_nextLong = a_nextLong;
  }

  public void setNextIntSequence(int[] a_sequence) {
    m_intIndex = 0;
    m_nextIntSequence = a_sequence;
  }
}
