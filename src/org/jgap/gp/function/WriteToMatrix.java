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
 * Stores a value to a two-dimensional matrix in internal memory.
 *
 * @author Klaus Meffert
 * @since 3.4.3
 */
public class WriteToMatrix
    extends CommandGene implements ICloneable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  /**
   * Symbolic name of the matrix. Must correspond with a chosen name for
   * ReadFromMatrix.
   */
  private String m_matrixName;

  public WriteToMatrix(final GPConfiguration a_conf, String a_matrixName)
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
  public WriteToMatrix(final GPConfiguration a_conf, String a_matrixName,
                       int a_subChildType)
      throws InvalidConfigurationException {
    // Arity 3 = column, row, value
    super(a_conf, 3, CommandGene.VoidClass, 0, new int[] {a_subChildType, a_subChildType,0});
    if (a_matrixName == null || a_matrixName.length() < 1) {
      throw new IllegalArgumentException("Matrix name must not be empty!");
    }
    m_matrixName = a_matrixName;
  }

  /**
   * Allows setting the sub child types of all three children individually.
   *
   * @param a_conf GPConfiguration
   * @param a_matrixName String
   * @param a_subChildType int
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.6
   */
  public WriteToMatrix(final GPConfiguration a_conf, String a_matrixName,
                       int a_subChildType1, int a_subChildType2, int a_subChildType3)
      throws InvalidConfigurationException {
    // Arity 3 = column, row, value
    super(a_conf, 3, CommandGene.VoidClass, 0, new int[] {a_subChildType1, a_subChildType2, a_subChildType3});
    if (a_matrixName == null || a_matrixName.length() < 1) {
      throw new IllegalArgumentException("Matrix name must not be empty!");
    }
    m_matrixName = a_matrixName;
  }

  public String toString() {
    return "writeToMatrix(" + m_matrixName + ", &1, &2, &3)";
  }

  /**
   * @return textual name of this command
   *
   * @author Klaus Meffert
   * @since 3.4.3
   */
  public String getName() {
    return "WriteToMatrix(" + m_matrixName + ")";
  }

  @Override
  public void execute_void(ProgramChromosome c, int n, Object[] args) {
    check(c);
    int col;
    int row;
    row = c.execute_int(n, 1, args);
    int maxlen = getGPConfiguration().getMatrix(m_matrixName).length;
    if(row >= maxlen) {
      throw new IllegalStateException("Not valid: Row > "+(maxlen-1));
    }
    col = c.execute_int(n, 0, args);
    maxlen = getGPConfiguration().getMatrix(m_matrixName)[0].length;
    if(col >= maxlen) {
      throw new IllegalStateException("Not valid: Col > "+(maxlen-1));
    }
//    char value;
//    value = (Character)(c.execute_object(n, 2, args));
    int value;
    value = (Integer)(c.execute_int(n, 2, args));
    // Write to matrix.
    // ----------------
    getGPConfiguration().setMatrix(m_matrixName, col, row, value);
  }

  public boolean isAffectGlobalState() {
    return true;
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
    if (a_chromNum == 0 || a_chromNum == 1) {
      return CommandGene.IntegerClass;
    }
    //    return char.class;
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
    WriteToMatrix other = (WriteToMatrix) a_other;
    return new CompareToBuilder()
        .append(m_matrixName, other.m_matrixName)
        .append(getSubChildTypes(), other.getSubChildTypes())
        .toComparison();
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
      WriteToMatrix other = (WriteToMatrix) a_other;
      return super.equals(a_other) && new EqualsBuilder()
          .append(m_matrixName, other.m_matrixName)
        .append(getSubChildTypes(), other.getSubChildTypes())
          .isEquals();
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
      int[] subChilds = getSubChildTypes();
      WriteToMatrix result = new WriteToMatrix(getGPConfiguration(),
          m_matrixName, subChilds[0], subChilds[1], subChilds[2]);
      return result;
    } catch (Exception ex) {
      throw new CloneException(ex);
    }
  }
}
