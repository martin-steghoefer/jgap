/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.supergenes;

import org.jgap.*;

/**
 * <p>Supergene represents several genes, which usually control closely
 * related aspects of the phenotype. The Supergene mutates only in such way,
 * that the allele combination remains valid. Mutations, that make allele
 * combination invalid, are rejected inside {@link org.jgap.Gene#applyMutation }
 * method. Supergene components can also be a Supergene, creating the tree-like
 * structures in this way.
 *</p><p>
 * In biology, the invalid combinations represent completely broken metabolic
 * chains, unbalanced signaling pathways (activator without supressor) and so
 * on.
 *</p><p>
 * At <i>least about 5 % of the randomly generated Supergene suparallele values
 * should be valid.</i> If the valid combinations represents too small part of
 * all possible combinations, it can take too long to find the suitable mutation
 * that does not brake a supergene. If you face this problem, try to split the
 * supergene into several sub-supergenes.</p>
 *
 * @author Audrius Meskauskas
 */
public interface Supergene
    extends Gene, ICompositeGene {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.14 $";

  /**
   * Test the allele combination of this supergene for validity.
   * If a validator was previously set be calling setValidator(),
   * the decission is delegated to this validator. The derived
   * classes may have internal default validator for the case
   * when no external validator is set. See note in the interface header.
   *
   * @return true only if the supergene allele combination is valid
   */
  boolean isValid();

  /**
   * Get the array of genes - components of this supergene.
   * The supergene components may be supergenes itself.
   */
  Gene[] getGenes();

  /**
   * Returns the Gene at the given index (locus) within the Supergene. The
   * first gene is at index zero and the last gene is at the index equal to
   * the size of this Supergene - 1.
   *
   * @param a_index the index of the gene value to be returned
   * @return the Gene at the given index
   */
  Gene geneAt(int a_index);

  /**
   * Sets an object, responsible for deciding if the Supergene allele
   * combination is valid. If it is set to null, no validation is performed
   * (all combinations are assumed to be valid). The derived
   * classes may have internal default validator for the case
   * when no external validator is set.
   */
  void setValidator(SupergeneValidator a_validator);

  /**
   * Gets an object, responsible for deciding if the Supergene allele
   * combination is valid. If no external validator was set and the
   * class uses its own internal validation method, it still must be
   * able to return a validator, using the same method (typicallly,
   * such classes just return <i>this</i>.
   */
  SupergeneValidator getValidator();
}
