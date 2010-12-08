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
 * Resets a two-dimensional matrix in internal memory by setting each cell to
 * an initial value.
 *
 * @author Klaus Meffert
 * @since 3.4.3
 */
public class ResetMatrix
    extends CommandGene implements ICloneable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  /**
   * Symbolic name of the matrix. Must correspond with a chosen name for
   * ReadFromMatrix.
   */
  private String m_matrixName;

  private char m_filler;

  /**
   * Allows setting a sub child type.
   *
   * @param a_conf the configuration to use
   * @param a_matrixName name of the matrix
   * @param a_filler the character to fill the matrix with
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.4.3
   */
  public ResetMatrix(final GPConfiguration a_conf, String a_matrixName,
                     char a_filler)
      throws InvalidConfigurationException {
    super(a_conf, 0, CommandGene.VoidClass, 0);
    if (a_matrixName == null || a_matrixName.length() < 1) {
      throw new IllegalArgumentException("Matrix name must not be empty!");
    }
    m_matrixName = a_matrixName;
    m_filler = a_filler;
  }

  public String toString() {
    return "resetMatrix(" + m_matrixName + ", '" + m_filler + "')";
  }

  /**
   * @return textual name of this command
   *
   * @author Klaus Meffert
   * @since 3.4.3
   */
  public String getName() {
    return "ResetMatrix(" + m_matrixName + ", '" + m_filler + "')";
  }

  @Override
  public void execute_void(ProgramChromosome c, int n, Object[] args) {
    // Reset the matrix.
    // -----------------
    getGPConfiguration().resetMatrix(m_matrixName, m_filler);
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
    ResetMatrix other = (ResetMatrix) a_other;
    return new CompareToBuilder().append(m_matrixName, other.m_matrixName).
        append(m_filler, other.m_filler).toComparison();
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
      ResetMatrix other = (ResetMatrix) a_other;
      return super.equals(a_other) && new EqualsBuilder().
          append(m_matrixName, other.m_matrixName).
          append(m_filler, other.m_filler).isEquals();
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
      ResetMatrix result = new ResetMatrix(getGPConfiguration(),
          m_matrixName, m_filler);
      return result;
    } catch (Exception ex) {
      throw new CloneException(ex);
    }
  }
}
