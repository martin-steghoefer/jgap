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
 * An implementation of a fitness evaluator interpreting the fitness as delta
 * value
 *
 * @author Klaus Meffert
 * @since 2.0
 */
public class DeltaFitnessEvaluator
    implements FitnessEvaluator {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  public DeltaFitnessEvaluator() {
  }

  /**
   * Compares the first given fitness value with the second and returns true
   * if the first one is smaller than the second one. Otherwise returns false
   * @param a_fitness_value1 first fitness value
   * @param a_fitness_value2 second fitness value
   * @return true: first fitness value smaller than second
   *
   * @since 2.0 (until 1.1: input types int)
   */
  public boolean isFitter(double a_fitness_value1, double a_fitness_value2) {
    return a_fitness_value1 < a_fitness_value2;
  }
}
