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
 * Interface for a fitness evaluator used in a Genotype to determine how to
 * interpret the fitness value. The fitness value can either be interpreted
 * straight forward as a fitness indicator (the higher the better). Or it could
 * be seen as a defect rate (the lower the better).
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public interface FitnessEvaluator {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.2 $";

  /**
   * Compares the first given fitness value with the second and returns true
   * if the first one is greater than the second one. Otherwise returns false
   * @param a_fitness_value1 first fitness value
   * @param a_fitness_value2 second fitness value
   * @return true: first fitness value greater than second
   * @since 1.1
   */
  boolean isFitter(int a_fitness_value1, int a_fitness_value2);
}
