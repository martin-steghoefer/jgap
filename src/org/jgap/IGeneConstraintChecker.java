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
 * Interface for checking whether a given allele value is valid to be set for
 * a given gene instance. The contained validate()-method will be called in
 * the setAllele(Object) method of Gene implementations.
 *
 * @author Klaus Meffert
 * @since 2.0
 */
public interface IGeneConstraintChecker {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.1 $";

  /**
   * Check if a given allele value is valid for the given gene instance.
   * @param a_gene the gene the given allele is to be validated for
   * @param a_alleleValue the allele value to be validated
   * @return true: allele may be set for gene; false: validity check failed
   * @throws RuntimeException if the checker cannot decide whether the given
   * allele is valid or not
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  boolean verify(Gene a_gene, Object a_alleleValue)
      throws RuntimeException;
}
