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

import java.io.*;
import org.jgap.gp.impl.*;

/**
 * Interface for GP chromosomes. See ProgramChromosome for an implementation.
 *
 * @author Klaus Meffert
 * @since 3.01
 */
public interface IGPChromosome
    extends Serializable {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.7 $";

  /**
   * @return the individual containing this chromosome
   *
   * @author Klaus Meffert
   * @since 3.01
   */
  IGPProgram getIndividual();

  /**
   * Sets the individual the chromosome belongs to.
   *
   * @param a_ind the individual containing this chromosome
   *
   * @author Klaus Meffert
   * @since 3.01
   */
  void setIndividual(IGPProgram a_ind);

  /**
   * Clean up the chromosome.
   *
   * @author Klaus Meffert
   * @since 3.01
   */
  void cleanup();

  /**
   * Output program in "natural" notion (e.g.: "X + Y" for "X + Y")
   * @param a_startNode the node to start with
   * @return output in normalized notion
   *
   * @author Klaus Meffert
   * @since 3.01
   */
  String toStringNorm(final int a_startNode);

  /**
   * Recalculate the depths of each node.
   *
   * @author Klaus Meffert
   * @since 3.01
   */
  void redepth();

  /**
   * @return the number of terminals in this chromosome
   *
   * @author Klaus Meffert
   * @since 3.01
   */
  int numTerminals();

  /**
   * @return the number of functions in this chromosome
   *
   * @author Klaus Meffert
   * @since 3.01
   */
  int numFunctions();

  /**
   * Counts the number of terminals of the given type in this chromosome.
   *
   * @param a_type the type of terminal to count
   * @param a_subType the subtype to consider
   * @return the number of terminals in this chromosome
   *
   * @author Klaus Meffert
   * @since 3.01
   */
  int numTerminals(Class a_type, int a_subType);

  /**
   * Counts the number of functions of the given type in this chromosome.
   *
   * @param a_type the type of function to count
   * @param a_subType the subtype to consider
   * @return the number of functions in this chromosome.
   *
   * @author Klaus Meffert
   * @since 3.01
   */
  int numFunctions(Class a_type, int a_subType);

  /**
   * Gets the a_index'th node in this chromosome. The nodes are counted in a
   * depth-first manner, with node 0 being the root of this chromosome.
   *
   * @param a_index the node number to get
   * @return the node
   *
   * @author Klaus Meffert
   * @since 3.01
   */
  CommandGene getNode(int a_index);

  /**
   * Gets the a_child'th child of the a_index'th node in this chromosome. This
   * is the same as the a_child'th node whose depth is one more than the depth
   * of the a_index'th node.
   *
   * @param a_index the node number of the parent
   * @param a_child the child number (starting from 0) of the parent
   * @return the node number of the child, or -1 if not found
   *
   * @author Klaus Meffert
   * @since 3.01
   */
  int getChild(int a_index, int a_child);

  /**
   * Gets the i'th terminal in this chromosome. The nodes are counted in a
   * depth-first manner, with node 0 being the first terminal in this
   * chromosome.
   *
   * @param a_index the i'th terminal to get
   * @return the terminal
   *
   * @author Klaus Meffert
   * @since 3.01
   */
  int getTerminal(int a_index);

  /**
   * Gets the a_index'th function in this chromosome. The nodes are counted in a
   * depth-first manner, with node 0 being the first function in this
   * chromosome.
   *
   * @param a_index the a_index'th function to get
   * @return the function
   *
   * @author Klaus Meffert
   * @since 3.01
   */
  int getFunction(int a_index);

  /**
   * Gets the a_index'th terminal of the given type in this chromosome. The nodes
   * are counted in a depth-first manner, with node 0 being the first terminal of
   * the given type in this chromosome.
   *
   * @param a_index the a_index'th terminal to get
   * @param a_type the type of terminal to get
   * @param a_subType the subtype to consider
   * @return the index of the terminal found, or -1 if no appropriate terminal
   * was found
   *
   * @author Klaus Meffert
   * @since 3.01
   */
  int getTerminal(int a_index, Class a_type, int a_subType);

  /**
   * Gets the i'th function of the given type in this chromosome. The nodes are
   * counted in a depth-first manner, with node 0 being the first function of
   * the given type in this chromosome.
   *
   * @param a_index the i'th function to get
   * @param a_type the type of function to get
   * @param a_subType the subtype to consider
   * @return the index of the function found, or -1 if no appropriate function
   * was found
   *
   * @author Klaus Meffert
   * @since 3.01
   */
  int getFunction(int a_index, Class a_type, int a_subType);

  CommandGene[] getFunctions();

  /**
   * @return set of CommandGene instances allowed
   *
   * @author Klaus Meffert
   * @since 3.2.1
   */
  CommandGene[] getFunctionSet();

  GPConfiguration getGPConfiguration();

  /**
   * @return the persistent representation of the population, including all
   * GP programs
   *
   * @author Klaus Meffert
   * @since 3.3
   */
  String getPersistentRepresentation();
}
