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
import org.jgap.util.*;

/**
 * Fitness functions are used to determine how optimal a particular solution
 * is relative to other solutions. This abstract class should be extended and
 * the evaluate() method implemented. The fitness function is given a Chromosome
 * to evaluate and should return a positive double that reflects its fitness
 * value. The higher the value, the more fit the Chromosome. The actual range
 * of fitness values isn't important (other than the fact that they must be
 * positive doubles): it's the relative difference as a percentage that
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
    implements Serializable, ICloneable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.22 $";

  public final static double NO_FITNESS_VALUE = -1.0000000d;

  public final static double DELTA = 0.0000001d;

  /**
   * The fitness value computed during the previous run
   */
  private double m_lastComputedFitnessValue = NO_FITNESS_VALUE;

  /**
   * Retrieves the fitness value of the given Chromosome. The fitness
   * value will be a positive double.
   *
   * @param a_subject the Chromosome for which to compute and return the
   * fitness value
   * @return the fitness value of the given Chromosome
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 2.0 (until 1.1: return type int)
   */
  public double getFitnessValue(final IChromosome a_subject) {
    // Delegate to the evaluate() method to actually compute the
    // fitness value. If the returned value is less than one,
    // then we throw a runtime exception.
    // ---------------------------------------------------------
    double fitnessValue = evaluate(a_subject);
    if (fitnessValue < 0.00000000d) {
      throw new RuntimeException(
          "Fitness values must be positive! Received value: "
          + fitnessValue);
    }
    m_lastComputedFitnessValue = fitnessValue;
    return fitnessValue;
  }

  /**
   * @return the last fitness value computed via method getFitnessValue(
   * Chromosome), or NO_FITNES_VALUE if the former method has not been called
   * yet
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public double getLastComputedFitnessValue() {
    return m_lastComputedFitnessValue;
  }

  /**
   * Determine the fitness of the given Chromosome instance. The higher the
   * return value, the more fit the instance. This method should always
   * return the same fitness value for two equivalent Chromosome instances.
   *
   * @param a_subject the Chromosome instance to evaluate
   *
   * @return positive double reflecting the fitness rating of the given
   * Chromosome. Note that if a non-positive double is returned, a
   * RuntimeException should be generated
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 2.0 (until 1.1: return type int)
   */
  protected abstract double evaluate(IChromosome a_subject);

  /**
   * Please override in your implementations!
   *
   * @return deep clone of the current instance
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public Object clone() {
    try {
      return super.clone();
    } catch (CloneNotSupportedException cex) {
      throw new CloneException(cex);
    }
  }
}
