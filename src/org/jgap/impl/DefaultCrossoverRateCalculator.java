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

package src.org.jgap.impl;

import org.jgap.*;

/**
 * Default implementation of a dynamic CrossoverRateCalculator
 *
 * @author Chris Knowles
 * @since 2.0
 */
public class DefaultCrossoverRateCalculator implements IUniversalRateCalculator {
        
  /**
   * Calculates the dynamic crossover rate. This is chosen to be the chromosome
   * size. As the chromosome gets larger we assume that it is less likely to 
   * reproduce.
   *
   * @return calculated divisor of crossover rate 
   *
   * @author Chris Knowles
   * @since 2.0
   */
  public int calculateCurrentRate() {  
    int size = Genotype.getConfiguration().getChromosomeSize();
    if (size < 1) {
      size = 1;
    }
    return size;
  }
  
  /**
   * Determines whether crossover is to be carried out for a given population.
   * @return true   The DefaultCrossoverRateCalculator always returns a finite 
   *                rate.
   *
   * @author Chris Knowles
   * @since 2.0
   */
  public boolean toBePermutated()
  {
      return true;
  }
}
