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

package org.jgap.distr;

import org.jgap.*;

/**
 * Interface for implementations allowing to merge two or more independent
 * Population's to be merged together into one Population.
 *
 * @author Klaus Meffert
 * @since 2.0
 */
public interface IPopulationMerger {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.2 $";

  /**
   * Merges two Population's into one that has the given size.
   * @param a_population1 Population first Population
   * @param a_population2 Population second Population
   * @param a_new_population_size int size of merged Population
   * @return Population the resulting Population
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  Population mergePopulations(Population a_population1,
                              Population a_population2,
                              int a_new_population_size);
}
