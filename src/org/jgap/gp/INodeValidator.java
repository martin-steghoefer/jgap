/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp;

import org.jgap.gp.impl.*;

/**
 * A node validator checks whether a certain node is valid for a given
 * evolution. See ProgramChromosome.growOrFullNode(..).<p>
 * Purpose of this validator: If you have an idea of how the shape of your
 * evolved program must look like regarding some spots (like the first node
 * in the first chromosome to be evolved) then you can constrain your
 * imaginations with this validator. But take care that constraining too much
 * ( e.g., while not considering your available functions) may lead to an
 * endless loop. The endless loop can be avoided by stopping after a number
 * of tries (see method validate, parameter a_tries).
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public interface INodeValidator
    extends java.io.Serializable {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.7 $";

  /**
   * Validates a_node in the context of a_chrom. Considers the recursion level
   * (a_recursLevel), the type needed (a_type) for the node, the functions
   * available (a_functionSet) and the depth of the whole chromosome needed
   * (a_depth), and whether grow mode is used (a_grow is true) or not.
   *
   * @param a_chrom the chromosome that will contain the node, if valid
   * @param a_node the selected node to be validated
   * @param a_rootNode the root node of a_node, may be null for top nodes
   * @param a_tries number of times the validator has been called, useful for
   * stopping by returning true if the number exceeds a limit
   * @param a_num the chromosome's index in the individual of this chromosome
   * @param a_recurseLevel level of recursion
   * @param a_type the return type of the node needed
   * @param a_functionSet the array of available functions
   * @param a_depth the needed depth of the program chromosome
   * @param a_grow true: use grow mode, false: use full mode
   * @param a_childIndex index of the child in the parent node to which it
   * belongs (-1 if node is root node)
   * @param a_fullProgram true: whole program is available in a_chrom
   * @return true: node is valid; false: node is invalid
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  boolean validate(ProgramChromosome a_chrom, CommandGene a_node,
                   CommandGene a_rootNode, int a_tries,
                   int a_num, int a_recurseLevel, Class a_type,
                   CommandGene[] a_functionSet, int a_depth,
                   boolean a_grow, int a_childIndex, boolean a_fullProgram);

}
