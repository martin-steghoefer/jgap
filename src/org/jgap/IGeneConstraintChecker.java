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

/**
 * Interface for checking whether a given allele value is valid to be set for
 * a given gene instance. The contained validate()-method will be called in
 * the setAllele(Object) method of Gene implementations.
 *
 * @author Klaus Meffert
 * @since 2.0
 */
public interface IGeneConstraintChecker
    extends java.io.Serializable {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.8 $";

  /**
   * Check if a given allele value is valid for the given gene instance.
   *
   * @param a_gene the gene the given allele is to be validated for
   * @param a_alleleValue the allele value to be validated
   * @param a_chromosome the chromosome the gene is contained (or null, if
   * unknown)
   * @param a_geneIndex the index the gene is contained in the chromosome at,
   * or -1 if unknown
   * @return true: allele may be set for gene; false: validity check failed
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  boolean verify(Gene a_gene, Object a_alleleValue, IChromosome a_chromosome,
                 int a_geneIndex);
}
