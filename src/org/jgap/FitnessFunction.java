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
package org.jgap;

/**
 * Fitness functions are used to determine how optimal a particular solution
 * is relative to other solutions. This abstract class should be extended and
 * the evaluate() method implemented. The fitness function is given a Chromosome
 * to evaluate and should return a positive integer that reflects its fitness
 * value. The higher the value, the more fit the Chromosome. The actual range
 * of fitness values isn't important (other than the fact that they must be
 * positive integers): it's the relative difference as a percentage that
 * tends to determine the success or failure of a Chromosome. So in other words,
 * two Chromosomes with respective fitness values of 1 and 100 have the same
 * relative fitness to each other as two Chromosomes with respective fitness
 * values of 10 and 1000 (in each case, the first is 1% as fit as the second).
 * <p>
 * Note: Two Chromosomes with equivalent sets of genes should always be
 * assigned the same fitness value by any implementation of this interface.
 *
 * @author Neil Rotstan
 * @author Klaus Meffert
 * @since 1.0
 */
public abstract class FitnessFunction
    implements java.io.Serializable {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.8 $";

  private static double NO_FITNESS_VALUE = -1.0000000d;

  /**
   * Retrieves the fitness value of the given Chromosome. The fitness
   * value will be a positive double.
   *
   * @param a_subject the Chromosome for which to compute and return the
   *                  fitness value.
   * @return the fitness value of the given Chromosome.
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 2.0 (until 1.1: return type int)
   */
  public final double getFitnessValue(Chromosome a_subject) {
    // Delegate to the evaluate() method to actually compute the
    // fitness value. If the returned value is less than one,
    // then we throw a runtime exception.
    // ---------------------------------------------------------
    double fitnessValue = evaluate(a_subject);
    if (fitnessValue < 0.00000000d) {
      throw new RuntimeException(
          "Fitness values must be positive! Received value: " +
          fitnessValue);
    }
    return fitnessValue;
  }

  /**
   * Determine the fitness of the given Chromosome instance. The higher the
   * return value, the more fit the instance. This method should always
   * return the same fitness value for two equivalent Chromosome instances.
   *
   * @param a_subject: The Chromosome instance to evaluate.
   *
   * @return A positive double reflecting the fitness rating of the given
   *         Chromosome. Note that if a non-positive double is returned,
   *         a RuntimeException should be generated.
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 2.0 (until 1.1: return type int)
   */
  protected abstract double evaluate(Chromosome a_subject);

  /**
   * @return the double value that indicated that there is no fitness value
   * assigned yet
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public double getNoFitnessValue() {
    return NO_FITNESS_VALUE;
  }
}
