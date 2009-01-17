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
import cern.jet.random.*;

/**
 * Provides an implementation of RandomGenerator using the Colt package.
 * It can be downloaded at http://hoschek.home.cern.ch/hoschek/colt/</p>
 *
 * @author Johnathan Kool
 * @since 2.4
 */
public class ColtRandomGenerator
    implements RandomGenerator {
  /**
   * Returns the next pseudorandom, uniformly distributed int value
   * from this random number generator's sequence. The general contract
   * of nextInt is that one int value is pseudorandomly generated and
   * returned. All 2^32  possible int values are produced with
   * (approximately) equal probability.
   *
   * @return a pseudorandom integer value
   */
  public int nextInt() {
    return Uniform.staticNextIntFromTo(Integer.MIN_VALUE, Integer.MAX_VALUE);
  }

  /**
   * Returns a pseudorandom, uniformly distributed int value between
   * 0 (inclusive) and the specified value (exclusive), drawn from this
   * random number generator's sequence. The general contract of nextInt
   * is that one int value in the specified range is pseudorandomly
   * generated and returned. All n possible int values are produced with
   * (approximately) equal probability.
   *
   * @param ceiling upper ceiling to consider for return value
   * @return a pseudorandom integer value between 0 and the given
   * ceiling - 1, inclusive
   */
  public int nextInt(int ceiling) {
    return Uniform.staticNextIntFromTo(0, ceiling - 1);
  }

  /**
   * Returns the next pseudorandom, uniformly distributed long value from
   * this random number generator's sequence. The general contract of
   * nextLong() is that one long value is pseudorandomly generated and
   * returned. All 2^64 possible long values are produced with
   * (approximately) equal probability.
   *
   * @return a psuedorandom long value
   */
  public long nextLong() {
    return Uniform.staticNextLongFromTo(Long.MIN_VALUE,
                                        Long.MAX_VALUE);
  }

  /**
   * Returns the next pseudorandom, uniformly distributed double value
   * between 0.0 and 1.0 from this random number generator's sequence.
   *
   * @return a psuedorandom double value
   */
  public double nextDouble() {
    return Uniform.staticNextDouble();
  }

  /**
   * Returns the next pseudorandom, uniformly distributed float value
   * between 0.0 and 1.0 from this random number generator's sequence.
   *
   * @return a psuedorandom float value
   */
  public float nextFloat() {
    return Uniform.staticNextFloatFromTo(0.0f, 1.0f);
  }

  /**
   * Returns the next pseudorandom, uniformly distributed boolean value
   * from this random number generator's sequence. The general contract
   * of nextBoolean is that one boolean value is pseudorandomly generated
   * and returned. The values true and false are produced with
   * (approximately) equal probability.
   *
   * @return a pseudorandom boolean value
   */
  public boolean nextBoolean() {
    return Uniform.staticNextBoolean();
  }
}
