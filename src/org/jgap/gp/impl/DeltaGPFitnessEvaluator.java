/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp.impl;

import org.jgap.gp.*;

/**
 * Fitness evaluator taking input as delta values. Thus a lower value is seen
 * as fitter than a higher number.
 *
 * @author Klaus Meffert
 * @since 3.1
 */
public class DeltaGPFitnessEvaluator
    implements IGPFitnessEvaluator, Cloneable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  /**
   * Compares the first given fitness value with the second and returns true
   * if the first one is smaller than the second one. Otherwise returns false.
   *
   * @param a_fitness_value1 first fitness value
   * @param a_fitness_value2 second fitness value
   * @return true: first fitness value greater than second
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public boolean isFitter(final double a_fitness_value1,
                          final double a_fitness_value2) {
    if (!Double.isNaN(a_fitness_value1) &&
        !Double.isNaN(a_fitness_value2)) {
      return a_fitness_value1 < a_fitness_value2;
    }
    else if (Double.isNaN(a_fitness_value1)) {
      return false;
    }
    return true;
  }

  /*
   * @author Klaus Meffert
   * @since 3.1
   */
  public boolean isFitter(IGPProgram a_prog1, IGPProgram a_prog2) {
    return isFitter(a_prog1.getFitnessValue(), a_prog2.getFitnessValue());
  }

  /*
   * @author Klaus Meffert
   * @since 3.1
   */
  public boolean equals(Object a_object) {
    DeltaGPFitnessEvaluator eval = (DeltaGPFitnessEvaluator) a_object;
    return true;
  }

  /*
   * @author Klaus Meffert
   * @since 3.1
   */
  public int compareTo(Object a_object) {
    DeltaGPFitnessEvaluator eval = (DeltaGPFitnessEvaluator) a_object;
    return 0;
  }

  /**
   * @return deep clone of this instance
   *
   * @author Klaus Meffert
   * @since 3.3.3
   */
  public Object clone() {
    DeltaGPFitnessEvaluator result = new DeltaGPFitnessEvaluator();
    return result;
  }
}
