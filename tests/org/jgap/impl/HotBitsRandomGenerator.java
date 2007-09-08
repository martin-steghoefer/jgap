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
import org.jgap.util.*;

/**
 * Random generator using the HotBits random generators (they are pluggable
 * here) and adapts the interface to org.jgap.RandomGenerator
 *
 * @author Klaus Meffert
 * @since 2.3
 */
public class HotBitsRandomGenerator
    implements RandomGenerator {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  private randomX m_randomGenerator;

  public HotBitsRandomGenerator(randomX a_randomGenerator) {
    m_randomGenerator = a_randomGenerator;
  }

  public randomX getRandomGenerator() {
    return m_randomGenerator;
  }

  public byte nextByte() {
    return m_randomGenerator.nextByte();
  }

  public int nextInt() {
    return m_randomGenerator.nextInt();
  }

  public int nextInt(int ceiling) {
    return m_randomGenerator.nextInt() % ceiling;
  }

  public long nextLong() {
    return m_randomGenerator.nextLong();
  }

  public double nextDouble() {
    return m_randomGenerator.nextDouble();
  }

  public float nextFloat() {
    return m_randomGenerator.nextFloat();
  }

  public boolean nextBoolean() {
    return m_randomGenerator.nextBit();
  }
}
