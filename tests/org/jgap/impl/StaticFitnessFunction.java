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
package org.jgap.impl;

import org.jgap.*;

/**
 * Fitness function always returning the same value
 * Only for testing purpose
 * @author Klaus Meffert
 * @since 1.1
 */
public class StaticFitnessFunction
    extends FitnessFunction {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  /**
   * @since 2.0 (until 1.1: type int)
   */
  private double staticFitnessValue;

  /**
   * @param staticFitnessValue double
   * @since 2.0 (until 1.1: input type int)
   */
  public StaticFitnessFunction(double staticFitnessValue) {
    this.staticFitnessValue = staticFitnessValue;
  }

  /**
   * @param chrom Chromosome
   * @return double
   * @since 2.0 (until 1.1: return type int)
   */
  public double evaluate(Chromosome chrom) {
    double result = staticFitnessValue;
    return result;
  }

  /**
   * @return double
   * @since 2.0 (until 1.1: return type int)
   */
  public double getStaticFitnessValue() {
    return staticFitnessValue;
  }

  /**
   * @param staticFitnessValue double
   * @since 2.0 (until 1.1: type int)
   */
  public void setStaticFitnessValue(double staticFitnessValue) {
    this.staticFitnessValue = staticFitnessValue;
  }
}
