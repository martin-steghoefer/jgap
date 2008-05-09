/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap;

import java.io.*;

/**
 * The RandomGenerator interface provides an abstraction for the random
 * number implementation so that more rigorous or alternative implementations
 * can be provided as desired.
 * <p>
 * ATTENTION: nextDouble should only return values betwen 0 (inclusive)
 *            and 1 (exclusive!). The same holds for nextFloat.
 *
 * @author Neil Rotstan
 * @author Klaus Meffert
 * @since 1.0
 */
public interface RandomGenerator
    extends Serializable {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.8 $";

  /**
   * Returns the next pseudorandom, uniformly distributed int value
   * from this random number generator's sequence. The general contract
   * of nextInt is that one int value is pseudorandomly generated and
   * returned. All 2^32  possible int values are produced with
   * (approximately) equal probability.
   *
   * @return a pseudorandom integer value
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public int nextInt();

  /**
   * Returns a pseudorandom, uniformly distributed int value between
   * 0 (inclusive) and the specified value (exclusive), drawn from this
   * random number generator's sequence. The general contract of nextInt
   * is that one int value in the specified range is pseudorandomly
   * generated and returned. All n possible int values are produced with
   * (approximately) equal probability.
   * @param a_ceiling the upper boundary excluded
   *
   * @return a pseudorandom integer value between 0 and the given
   * ceiling - 1, inclusive
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public int nextInt(int a_ceiling);

  /**
   * Returns the next pseudorandom, uniformly distributed long value from
   * this random number generator's sequence. The general contract of
   * nextLong() is that one long value is pseudorandomly generated and
   * returned. All 2^64 possible long values are produced with
   * (approximately) equal probability.
   *
   * @return a psuedorandom long value
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public long nextLong();

  /**
   * Returns the next pseudorandom, uniformly distributed double value
   * between 0.0 and 1.0 from this random number generator's sequence.
   *
   * @return a psuedorandom double value GREATER/EQUAL 0 AND LESS THAN 1
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public double nextDouble();

  /**
   * Returns the next pseudorandom, uniformly distributed float value
   * between 0.0 and 1.0 from this random number generator's sequence.
   *
   * @return a psuedorandom float value
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public float nextFloat();

  /**
   * Returns the next pseudorandom, uniformly distributed boolean value
   * from this random number generator's sequence. The general contract
   * of nextBoolean is that one boolean value is pseudorandomly generated
   * and returned. The values true and false are produced with
   * (approximately) equal probability.
   *
   * @return a pseudorandom boolean value
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public boolean nextBoolean();
}
