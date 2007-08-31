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
 * Interface for Genes being composed by other genes.
 *
 * @author Klaus Meffert
 * @since 2.6
 */
public interface ICompositeGene extends Gene {
  /** String containing the CVS revision. Read out via reflection!*/
  static final String CVS_REVISION = "$Revision: 1.4 $";

  /**
   * Adds a gene to the composed Gene
   * @param a_gene the gene to add
   *
   * @author Klaus Meffert
   * @since 2.6 (since earlier in CompositeGene)
   */
  void addGene(Gene a_gene);

  /**
   * @param a_index index to return the gene at
   * @return the gene at the given index
   *
   * @author Klaus Meffert
   * @since 2.6 (since 1.1 in CompositeGene)
   */
  Gene geneAt(int a_index);
}
