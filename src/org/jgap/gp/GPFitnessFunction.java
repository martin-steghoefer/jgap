/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp;

import org.apache.log4j.Logger;

/**
 * Fitness function for GP-Programs.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public abstract class GPFitnessFunction
    implements java.io.Serializable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.11 $";

  public final static double NO_FITNESS_VALUE = -1.0000000d;

  public final static double MAX_FITNESS_VALUE = Double.MAX_VALUE / 2;

  private transient static Logger LOGGER = Logger.getLogger(GPFitnessFunction.class);

  /**
   * The fitness value computed during the previous run
   */
  private double m_lastComputedFitnessValue = NO_FITNESS_VALUE;

  /**
   * Default constructor.
   *
   * @author Klaus Meffert
   * @since 3.3.1
   */
  public void GPFitnessFunction() {

  }

  /**
   * Retrieves the fitness value of the given GPProgram. The fitness
   * value will be a positive double.
   *
   * @param a_program the GPProgram for which to compute and return the
   * fitness value
   * @return the fitness value of the given GPProgram
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public final double getFitnessValue(final IGPProgram a_program) {
    // Delegate to the evaluate() method to actually compute the
    // fitness value. If the returned value is less than zero
    // we throw a runtime exception.
    // ---------------------------------------------------------
    double fitnessValue;
    try {
      fitnessValue = evaluate(a_program);
    } catch (IllegalStateException iex) {
      LOGGER.debug(iex.getMessage());
      fitnessValue = NO_FITNESS_VALUE;
      return fitnessValue;
    }
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
   * Determine the fitness of the given GPProgram instance. The higher the
   * return value, the more fit the instance. This method should always
   * return the same fitness value for two equivalent GPProgram instances.
   *
   * @param a_subject the GPProgram instance to evaluate
   *
   * @return positive double reflecting the fitness rating of the given
   * GPProgram. Note that if a non-positive double is returned, a
   * RuntimeException should be generated
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  protected abstract double evaluate(IGPProgram a_subject);
}
