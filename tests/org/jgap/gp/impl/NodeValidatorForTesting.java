package org.jgap.gp.impl;

import org.jgap.gp.impl.*;
import org.jgap.gp.*;

/**
 * For testing purposes only.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public class NodeValidatorForTesting
    implements INodeValidator {
  public boolean validate(ProgramChromosome a_chrom, CommandGene a_node,
                          CommandGene a_rootNode,
                          int a_tries, int a_num, int a_recurseLevel,
                          Class a_type, CommandGene[] a_functionSet,
                          int a_depth, boolean a_grow, int a_childIndex,
                          boolean a_fullProgram) {
    return true;
  }
}
