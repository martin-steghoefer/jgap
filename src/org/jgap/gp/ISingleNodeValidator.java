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
 * Checks whether a single node is valid during GP program creation.
 * This helps to avoid useless tries in program creation.
 *
 * @author Klaus Meffert
 * @since 3.6
 */
public interface ISingleNodeValidator {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.1 $";

  /**
   * Checks if the given a_function is allowed in the current context.
   *
   * @param a_chromIndex index of the chromosome within the GP program
   * @param a_pc the instance of the ProgramChromosome
   * @param a_functionSet the current function set within the program chromosome
   * (given as input parameter for convenience, could also be determined via
   * a_pc.m_genes)
   * @param a_function the function that should be added, if allowed
   * @param a_returnType the class the return type is required to have
   * @param a_subReturnTyp the sub return type the return type is required to
   * have
   * @param a_index the index of the up-to-date last command gene in the
   * program chromosome
   * @return true if a_function's class exists within a_functionSet
   *
   * @author Klaus Meffert
   * @since 3.6
   */
  boolean isAllowed(int a_chromIndex, ProgramChromosome a_pc,
                           CommandGene[] a_functionSet, CommandGene a_function,
                           Class a_returnType, int a_subReturnTyp, int a_index);

}
