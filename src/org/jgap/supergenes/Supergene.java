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
package org.jgap.supergenes;

import org.jgap.Gene;

/**
 * <p>Supergene represents several genes, which usually control closely
 * related aspects of the phenotype. The Supergene mutates
 * only in such way, that the allele combination remains valid.
 * Mutations, that make allele combination invalid, are rejected
 * inside {@link org.jgap.Gene#applyMutation } method. Supergene components can also be
 * a Supergenes, creating the tree-like structures in this way.
 *</p><p>
 * In biology, the invalid combinations
 * represents completely broken metabolic chains, unbalanced
 * signaling pathways (activator without supressor) and so on.
 *</p><p>
 * At <i>least about 5 % of the randomly
 * generated Supergene suparallele values should be valid.</i> If the valid
 * combinations represents too small part of all possible combinations,
 * it can take too long to find the suitable mutation that does not brake
 * a supergene. If you face this problem, try to split the supergene into
 * several sub-supergenes.
 * </p>
 *
 * @author Audrius Meskauskas
 */

public interface Supergene extends Gene {

    /** String containing the CVS revision. Read out via reflection!*/
    final static String CVS_REVISION = "0.0.1 alpha-pre";

    /**
     * Test the allele combination of this supergene for validity.
     * The default implementation usually calls isValid(getGenes());
     * @see Note in the interface header. *
     * @return true only if the supergene allele combination is valid.
     */
    boolean isValid();

    /**
     * Test the given allele combination for validity, using this supergene
     * validation method. You should always implement this method.
     * @see Note in the interface header.
     * @return true only if the supergene allele combination is valid.
     */
    boolean isValid(Gene [] a_case);

    /**
     * Get the array of genes - components of this supergene.
     * The supergene components may be supergenes itself.
     */
    Gene [] getGenes();

    /**
     * Returns the Gene at the given index (locus) within the Chromosome. The
     * first gene is at index zero and the last gene is at the index equal to
     * the size of this Chromosome - 1.
     *
     * @param a_desiredLocus: The index of the gene value to be returned.
     * @return The Gene at the given index.
     */
    Gene getGene(int index);

    /** Sets if the Supergene can mutate in the way it becomes invalid.
     * By default, the Supergene only mutates in the was it stays valid.
     * This control can be switched of if the Supergene is used in
     * algorithms that call
     * {@link org.jgap.supergenes.Supergene#isValid isValid(Gene [] a_case)}
     * instead.
     * @param a_validate true (default) if all mutations and setting to
     * random value must result a valid supergene.
     */

    void setValidateWhenMutating(boolean a_validate);



}