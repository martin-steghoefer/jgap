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

import org.jgap.gp.terminal.*;
import org.jgap.gp.impl.*;
import org.jgap.*;

/**
 * Abstract base class for all implementations of IGPChromosome.
 *
 * @author Klaus Meffert
 * @since 3.01
 */
public abstract class BaseGPChromosome
    implements IGPChromosome {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.8 $";

  /**
   * The configuration object to use.
   */
  private GPConfiguration m_configuration;

  /**
   * The individual the chromosome belongs to.
   */
  private IGPProgram m_ind;

  public BaseGPChromosome(GPConfiguration a_configuration)
      throws InvalidConfigurationException {
    if (a_configuration == null) {
      throw new InvalidConfigurationException(
          "Configuration to be set must not"
          + " be null!");
    }
    m_configuration = a_configuration;
  }

  public BaseGPChromosome(GPConfiguration a_configuration, IGPProgram a_ind)
      throws InvalidConfigurationException {
    this(a_configuration);
    m_ind = a_ind;
  }

  /**
   * @return the individual containing this chromosome
   *
   * @author Klaus Meffert
   * @since 3.01 (since 3.0 in ProgramChromosome)
   */
  public IGPProgram getIndividual() {
    return m_ind;
  }

  /**
   * Sets the individual the chromosome belongs to.
   *
   * @param a_ind the individual containing this chromosome
   *
   * @author Klaus Meffert
   * @since 3.01 (since 3.0 in ProgramChromosome)
   */
  public void setIndividual(IGPProgram a_ind) {
    if (a_ind == null) {
      throw new IllegalArgumentException("Individual must not be null");
    }
    m_ind = a_ind;
  }

  /**
   * Gets the i'th terminal in this chromosome. The nodes are counted in a
   * depth-first manner, with node zero being the first terminal in this
   * chromosome.
   *
   * @param a_index the i'th terminal to get
   * @return the terminal
   *
   * @author Klaus Meffert
   * @since 3.01 (since 3.0 in ProgramChromosome)
   */
  public int getTerminal(int a_index) {
    CommandGene[] functions = getFunctions();
    int len = functions.length;
    for (int j = 0; j < len && functions[j] != null; j++) {
      if (functions[j].getArity(m_ind) == 0) {
        if (--a_index < 0) {
          return j;
        }
      }
    }
    return -1;
  }

  /**
   * Gets the a_index'th function in this chromosome. The nodes are counted in a
   * depth-first manner, with node zero being the first function in this
   * chromosome.
   *
   * @param a_index the a_index'th function to get
   * @return the function
   *
   * @author Klaus Meffert
   * @since 3.01 (since 3.0 in ProgramChromosome)
   */
  public int getFunction(int a_index) {
    CommandGene[] functions = getFunctions();
    int len = functions.length;
    for (int j = 0; j < len && functions[j] != null; j++) {
      if (functions[j].getArity(m_ind) != 0) {
        if (--a_index < 0) {
          return j;
        }
      }
    }
    return -1;
  }

  /**
   * Gets the a_index'th terminal of the given type in this chromosome. The
   * nodes are counted in a depth-first manner, with node zero being the first
   * terminal of the given type in this chromosome.
   *
   * @param a_index the a_index'th terminal to get
   * @param a_type the type of terminal to get
   * @param a_subType the subtype to consider
   * @return the index of the terminal found, or -1 if no appropriate terminal
   * was found
   *
   * @author Klaus Meffert
   * @since 3.01 (since 3.0 in ProgramChromosome)
   */
  public int getTerminal(int a_index, Class a_type, int a_subType) {
    CommandGene[] functions = getFunctions();
    int len = functions.length;
    for (int j = 0; j < len && functions[j] != null; j++) {
      if ( (a_subType == 0 || functions[j].getSubReturnType() == a_subType)
          && functions[j].getReturnType() == a_type
          && functions[j].getArity(m_ind) == 0) {
        if (--a_index < 0) {
          return j;
        }
      }
    }
    return -1;
  }

  /**
   * Gets the i'th function of the given return type in this chromosome. The
   * nodes are counted in a depth-first manner, with node zero being the first
   * function of the given type in this chromosome.
   *
   * @param a_index the i'th function to get
   * @param a_type the type of function to get
   * @param a_subType the subtype to consider
   * @return the index of the function found, or -1 if no appropriate function
   * was found
   *
   * @author Klaus Meffert
   * @since 3.01 (since 3.0 in ProgramChromosome)
   */
  public int getFunction(int a_index, Class a_type, int a_subType) {
    CommandGene[] functions = getFunctions();
    int len = functions.length;
    for (int j = 0; j < len && functions[j] != null; j++) {
      if (functions[j].getReturnType() == a_type
          && (a_subType == 0 || a_subType == functions[j].getSubReturnType())
          && functions[j].getArity(m_ind) != 0) {
        if (--a_index < 0) {
          return j;
        }
      }
    }
    return -1;
  }

  /**
   * @return the number of terminals in this chromosome
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public int numTerminals() {
    int count = 0;
    CommandGene[] functions = getFunctions();
    int len = functions.length;
    for (int i = 0; i < len && functions[i] != null; i++) {
      if (functions[i].getArity(m_ind) == 0) {
        count++;
      }
    }
    return count;
  }

  /**
   * @return the number of functions in this chromosome
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public int numFunctions() {
    int count = 0;
    CommandGene[] functions = getFunctions();
    int len = functions.length;
    for (int i = 0; i < len && functions[i] != null; i++) {
      if (functions[i].getArity(m_ind) != 0) {
        count++;
      }
    }
    return count;
  }

  /**
   * Counts the number of terminals of the given type in this chromosome.
   *
   * @param a_type the type of terminal to count
   * @param a_subType the subtype to consider
   * @return the number of terminals in this chromosome
   *
   * @author Klaus Meffert
   * @since 3.01 (since 3.0 in ProgramChromosome)
   */
  public int numTerminals(Class a_type, int a_subType) {
    int count = 0;
    CommandGene[] functions = getFunctions();
    int len = functions.length;
    for (int i = 0; i < len && functions[i] != null; i++) {
      if (functions[i].getArity(m_ind) == 0
          && functions[i].getReturnType() == a_type
          && (a_subType == 0 || functions[i].getSubReturnType() == a_subType)) {
        count++;
      }
    }
    return count;
  }

  /**
   * Counts the number of functions of the given type in this chromosome.
   *
   * @param a_type the type of function to count
   * @param a_subType the subtype to consider
   * @return the number of functions in this chromosome.
   *
   * @author Klaus Meffert
   * @since 3.01 (since 3.0 in ProgramChromosome)
   */
  public int numFunctions(Class a_type, int a_subType) {
    int count = 0;
    CommandGene[] functions = getFunctions();
    int len = functions.length;
    for (int i = 0; i < len && functions[i] != null; i++) {
      if (functions[i].getArity(m_ind) != 0
          && functions[i].getReturnType() == a_type
          && (a_subType == 0 || functions[i].getSubReturnType() == a_subType)) {
        count++;
      }
    }
    return count;
  }

  /**
   * Gets the a_index'th node in this chromosome. The nodes are counted in a
   * depth-first manner, with node zero being the root of this chromosome.
   *
   * @param a_index the node number to get
   * @return the node
   *
   * @author Klaus Meffert
   * @since 3.01
   */
  public CommandGene getNode(int a_index) {
    if (a_index >= getFunctions().length || getFunctions()[a_index] == null) {
      return null;
    }
    return getFunctions()[a_index];
  }

  /**
   * Helper: Find GP command with given class and return index of it.
   *
   * @param a_n return the n'th found command (starting at zero)
   * @param a_class the class to find a command for
   * @return index of first found matching GP command, or -1 if none found
   *
   * @author Klaus Meffert
   * @since 3.01
   */
  public int getCommandOfClass(int a_n, Class a_class) {
    CommandGene[] functions = getFunctions();
    int len = functions.length;
    for (int j = 0; j < len && functions[j] != null; j++) {
      if (functions[j].getClass() == a_class) {
        if (--a_n < 0) {
          return j;
        }
      }
    }
    return -1;
  }

  /**
   * Helper: Find GP command being assignable from given class.
   *
   * @param a_n return the n'th found command (starting at zero)
   * @param a_class the class to find a command for
   * @return index of first found matching GP command, or -1 if none found
   *
   * @author Klaus Meffert
   * @since 3.3
   */
  public int getAssignableFromClass(int a_n, Class a_class) {
    CommandGene[] functions = getFunctions();
    int len = functions.length;
    for (int j = 0; j < len && functions[j] != null; j++) {
      if (a_class.isAssignableFrom(functions[j].getClass())) {
        if (--a_n < 0) {
          return j;
        }
      }
    }
    return -1;
  }

  /**
   * Helper: Find GP Variable with given return type and return index of it.
   *
   * @param a_n return the n'th found command (starting at zero)
   * @param a_returnType the return type to find a Variable for
   * @return index of first found matching GP command, or -1 if none found
   *
   * @author Klaus Meffert
   * @since 3.01 (since 3.0 in ProgramChromosome)
   */
  public int getVariableWithReturnType(int a_n, Class a_returnType) {
    CommandGene[] functions = getFunctions();
    int len = functions.length;
    for (int j = 0; j < len && functions[j] != null; j++) {
      if (functions[j].getClass() == Variable.class) {
        Variable v = (Variable) functions[j];
        if (v.getReturnType() == a_returnType) {
          if (--a_n < 0) {
            return j;
          }
        }
      }
    }
    return -1;
  }

  public GPConfiguration getGPConfiguration() {
    return m_configuration;
  }

}
