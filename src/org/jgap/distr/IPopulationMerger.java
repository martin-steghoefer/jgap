/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.distr;

import org.jgap.*;

/**
 * Interface for implementations allowing to merge two independent
 * populations together into one combined population.
 *
 * @author Klaus Meffert
 * @since 2.0
 */
public interface IPopulationMerger {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.6 $";

  /**
   * Merges two populations into one that has the given size.
   *
   * @param a_population1 first population
   * @param a_population2 second population
   * @param a_new_population_size size of merged population
   * @return the resulting population
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  Population mergePopulations(Population a_population1,
                              Population a_population2,
                              int a_new_population_size);
}
