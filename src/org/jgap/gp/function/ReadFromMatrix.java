/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp.function;

import org.apache.commons.lang.builder.*;
import org.jgap.*;
import org.jgap.gp.*;
import org.jgap.gp.impl.*;
import org.jgap.util.*;

/**
 * Reads a value from a two-dimensional matrix in internal memory.
 *
 * @author Klaus Meffert
 * @since 3.4.3
 */
public class ReadFromMatrix
        extends CommandGene implements ICloneable {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";
  /**
   * Symbolic name of the matrix. Must correspond with a chosen name for
   * ReadFromMatrix.
   */
  private String m_matrixName;

  public ReadFromMatrix(final GPConfiguration a_conf, String a_matrixName)
          throws InvalidConfigurationException {
    this(a_conf, a_matrixName, 0);
  }

  /**
   * Allows setting a sub child type.
   *
   * @param a_conf GPConfiguration
   * @param a_matrixName String
   * @param a_subChildType int
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.4.3
   */
  public ReadFromMatrix(final GPConfiguration a_conf, String a_matrixName,
          int a_subChildType)
          throws InvalidConfigurationException {
    // Arity 2 = column, row
    super(a_conf, 2, CommandGene.CharacterClass, 0, new int[]{a_subChildType});
    if (a_matrixName == null || a_matrixName.length() < 1) {
      throw new IllegalArgumentException("Matrix name must not be empty!");
    }
    m_matrixName = a_matrixName;
  }

  public String toString() {
    return "readFromMatrix(" + m_matrixName + ", &1, &2)";
  }

  /**
   * @return textual name of this command
   *
   * @author Klaus Meffert
   * @since 3.4.3
   */
  public String getName() {
    return "ReadFromMatrix(" + m_matrixName + ")";
  }

  @Override
  public Object execute_object(ProgramChromosome c, int n, Object[] args) {
    int col;
    int row;
    col = c.execute_int(n, 0, args);
    row = c.execute_int(n, 1, args);
    // Read from matrix.
    // -----------------
    return getGPConfiguration().readMatrix(m_matrixName, col, row);
  }

  /**
   * Determines which type a specific child of this command has.
   *
   * @param a_ind ignored here
   * @param a_chromNum index of child
   * @return type of the a_chromNum'th child
   *
   * @author Klaus Meffert
   * @since 3.4.3
   */
  public Class getChildType(IGPProgram a_ind, int a_chromNum) {
    return CommandGene.IntegerClass;
  }

  /**
   * The compareTo-method.
   *
   * @param a_other the other object to compare
   * @return -1, 0, 1
   *
   * @author Klaus Meffert
   * @since 3.4.3
   */
  public int compareTo(Object a_other) {
    int result = super.compareTo(a_other);
    if (result != 0) {
      return result;
    }
    ReadFromMatrix other = (ReadFromMatrix) a_other;
    return new CompareToBuilder().append(m_matrixName,
            other.m_matrixName).append(getSubChildTypes(),
            other.getSubChildTypes()).toComparison();
  }

  /**
   * The equals-method.
   *
   * @param a_other the other object to compare
   * @return true if the objects are seen as equal
   *
   * @author Klaus Meffert
   * @since 3.4.3
   */
  public boolean equals(Object a_other) {
    try {
      ReadFromMatrix other = (ReadFromMatrix) a_other;
      return super.equals(a_other) && new EqualsBuilder().append(m_matrixName,
              other.m_matrixName).append(getSubChildTypes(),
              other.getSubChildTypes()).isEquals();
    } catch (ClassCastException cex) {
      return false;
    }
  }

  /**
   * Clones the object. Simple and straight forward implementation here.
   *
   * @return cloned instance of this object
   *
   * @author Klaus Meffert
   * @since 3.4.3
   */
  public Object clone() {
    try {
      ReadFromMatrix result = new ReadFromMatrix(getGPConfiguration(),
              m_matrixName, getSubChildType(0));
      return result;
    } catch (Exception ex) {
      throw new CloneException(ex);
    }
  }
}
