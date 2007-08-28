/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.gp;

import org.jgap.gp.impl.*;
import org.jgap.gp.*;
import org.jgap.gp.function.*;
import org.jgap.gp.terminal.*;

/**
 * Validates evolved nodes for the Fibonacci problem. This is mainly to
 * demonstrate how the node validator works.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class FibonacciNodeValidator
    implements INodeValidator {
  /**
   * Validates a_node in the context of a_chrom during evolution. Considers the
   * recursion level (a_recursLevel), the type needed (a_type) for the node, the
   * functions available (a_functionSet) and the depth of the whole chromosome
   * needed (a_depth), and whether grow mode is used (a_grow is true) or not.
   *
   * @param a_chrom the chromosome that will contain the node, if valid (ignored
   * in this implementation)
   * @param a_node the node selected and to be validated
   * @param a_rootNode the root node of a_node, may be null for top nodes
   * @param a_tries number of times the validator has been called, useful for
   * stopping by returning true if the number exceeds a limit
   * @param a_num the chromosome's index in the individual of this chromosome
   * @param a_recurseLevel level of recursion
   * @param a_type the return type of the node needed
   * @param a_functionSet the array of available functions (ignored in this
   * implementation)
   * @param a_depth the needed depth of the program chromosome
   * @param a_grow true: use grow mode, false: use full mode (ignored in this
   * implementation)
   * @param a_childIndex index of the child in the parent node to which it
   * belongs (-1 if node is root node)
   * @param a_fullProgram true: whole program is available in a_chrom
   * @return true: node is valid; false: node is invalid
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public boolean validate(ProgramChromosome a_chrom, CommandGene a_node,
                          CommandGene a_rootNode,
                          int a_tries, int a_num, int a_recurseLevel,
                          Class a_type, CommandGene[] a_functionSet,
                          int a_depth, boolean a_grow, int a_childIndex,
                          boolean a_fullProgram) {
    if (a_fullProgram) {
      return true;
    }
    // Guard to avoid endless validation.
    // ----------------------------------
    if (a_tries > 10) {
      return true;
    }
    // Chromosome 0.
    // -------------
    if (a_num == 0) {
      // SubProgram forbidden other than as root.
      // ----------------------------------------
      if (a_recurseLevel > 0 && a_node.getClass() == SubProgram.class) {
        return false;
      }
      // SubProgram wanted as root.
      // --------------------------
      if (a_recurseLevel == 0 && a_node.getClass() != SubProgram.class) {
        return false;
      }
      if (a_recurseLevel == 1 && a_node.getClass() != StoreTerminal.class) {
        return false;
      }
    }
    // Chromosome 1.
    // -------------
    if (a_num == 1) {
      // ForLoop forbidden under root node
      if (a_recurseLevel > 0 && a_node.getClass() == ForLoop.class) {
        return false;
      }
      // ForLoop needed as root
      if (a_recurseLevel == 0 && a_node.getClass() != ForLoop.class) {
        return false;
      }
      // Variable forbidden other than directly under root
      if (a_recurseLevel > 1 && a_node.getClass() == Variable.class) {
        return false;
      }
      // Variable needed directly under root
      if (a_recurseLevel == 1 && a_depth == 1
          && a_node.getClass() != Variable.class) {
        return false;
      }
      // SubProgram forbidden other than directly under root
      if (a_recurseLevel > 1 && a_depth > 1
          && a_node.getClass() == SubProgram.class) {
        return false;
      }
      // SubProgram needed directly under root
      if (a_recurseLevel == 1 && a_depth > 1 && a_type == CommandGene.VoidClass
          && a_node.getClass() != SubProgram.class) {
        return false;
      }
      // AddAndStore or TransferMemory needed 2 under root
      if (a_recurseLevel == 2 && a_depth > 1 && a_type == CommandGene.VoidClass
          && a_node.getClass() != AddAndStore.class
          && a_node.getClass() != TransferMemory.class) {
        return false;
      }
      // AddAndStore or TransferMemory forbidden other than 2 under root
      if (a_recurseLevel != 2 && a_depth > 1 && a_type == CommandGene.VoidClass
          && (a_node.getClass() == AddAndStore.class
              || a_node.getClass() == TransferMemory.class)) {
        return false;
      }
    }
    return true;
  }
}
