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
 * Abstract implementation of interface INaturalSelector
 *
 * @author Klaus Meffert
 * @since 2.0
 */
public abstract class NaturalSelector
    implements INaturalSelector {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.8 $";

  /**
   * Add a Chromosome instance to this selector's working pool of Chromosomes.
   *
   * @param a_chromosomeToAdd The specimen to add to the pool.
   *
   * @author Neil Rotstan
   * @since 1.0
   */
 protected abstract void add(Chromosome a_chromosomeToAdd);


  public NaturalSelector() {

  }

}
