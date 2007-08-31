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

import org.jgap.util.*;

/**
 * A default implementation of a fitness evaluator. This implementation is
 * straight forward: a higher fitness value is seen as fitter.
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class DefaultFitnessEvaluator
    implements FitnessEvaluator, ICloneable, Comparable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.13 $";

  /**
   * Compares the first given fitness value with the second and returns true
   * if the first one is greater than the second one. Otherwise returns false
   * @param a_fitness_value1 first fitness value
   * @param a_fitness_value2 second fitness value
   * @return true: first fitness value greater than second
   *
   * @author Klaus Meffert
   * @since 2.0 (until 1.1: input types int)
   */
  public boolean isFitter(final double a_fitness_value1,
                          final double a_fitness_value2) {
    return a_fitness_value1 > a_fitness_value2;
  }

  public boolean isFitter(IChromosome a_chrom1, IChromosome a_chrom2) {
    return isFitter(a_chrom1.getFitnessValue(), a_chrom2.getFitnessValue());
  }

  /**
   * @return deep clone of this instance
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public Object clone() {
    return new DefaultFitnessEvaluator();
  }

  /**
   * @param a_other sic
   * @return as always
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public int compareTo(Object a_other) {
    if (a_other.getClass().equals(getClass())) {
      return 0;
    }
    else {
      return getClass().getName().compareTo(a_other.getClass().getName());
    }
  }
}
