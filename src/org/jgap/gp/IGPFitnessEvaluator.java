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

/**
 * Interface for a fitness evaluator used in a Genotype to determine how to
 * interpret the fitness value. The fitness value can either be interpreted
 * straight forward as a fitness indicator (the higher the better). Or it could
 * be seen as a defect rate (the lower the better).
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public interface IGPFitnessEvaluator
    extends java.io.Serializable {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.6 $";

  /**
   * Compares the first given fitness value with the second and returns true
   * if the first one is fitter than the second one. Otherwise returns false.
   *
   * @param a_fitness_value1 first fitness value
   * @param a_fitness_value2 second fitness value
   * @return true: first fitness value fitter than second
   *
   * @author Klaus Meffert
   * @since 2.0 (until 1.1: input types int)
   */
  boolean isFitter(double a_fitness_value1, double a_fitness_value2);

  boolean isFitter(IGPProgram a_prog1, IGPProgram a_prog2);
}
