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
 * Replaces occurrences of specified characters in a two-dimensional matrix with
 * a given other character. Returns the number of replacements.
 *
 * @author Klaus Meffert
 * @since 3.4.3
 */
public class ReplaceInMatrix
        extends CommandGene implements ICloneable, IMutateable {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.6 $";
  /**
   * Symbolic name of the matrix. Must correspond with a chosen name for
   * ReadFromMatrix.
   */
  private String m_matrixName;
  private String m_targetCharacters;
  private char m_replacement;
  private ReplacementMode m_mode;

  public ReplaceInMatrix(final GPConfiguration a_conf, String a_matrixName,
          ReplacementMode a_mode, String a_targetCharacters,
          char a_replacement)
          throws InvalidConfigurationException {
    this(a_conf, a_matrixName, a_mode, a_targetCharacters,
            a_replacement, 0);
  }

  /**
   * Allows setting a sub child type.
   *
   * @param a_conf the configuration to use
   * @param a_matrixName name of the matrix
   * @param a_mode how to replace characters in the matrix
   * @param a_targetCharacters the characters to be replaced
   * @param a_replacement the character to replace others with
   * @param a_subChildType int
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.4.3
   */
  public ReplaceInMatrix(final GPConfiguration a_conf, String a_matrixName,
          ReplacementMode a_mode, String a_targetCharacters,
          char a_replacement, int a_subChildType)
          throws InvalidConfigurationException {
    super(a_conf, 1, CommandGene.IntegerClass, 0, new int[]{a_subChildType});
    if (a_matrixName == null || a_matrixName.length() < 1) {
      throw new IllegalArgumentException("Matrix name must not be empty!");
    }
    m_matrixName = a_matrixName;
    m_mode = a_mode;
    m_targetCharacters = a_targetCharacters;
    m_replacement = a_replacement;
  }

  public String toString() {
    return "replaceInMatrix(" + m_matrixName + ", '" + m_targetCharacters + "', '" +
        m_replacement + "')";
  }

  /**
   * @return textual name of this command
   *
   * @author Klaus Meffert
   * @since 3.4.3
   */
  public String getName() {
    return "ReplaceInMatrix(" + m_matrixName + ")";
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

  @Override
  public int execute_int(ProgramChromosome c, int n, Object[] args) {
    int index;
    // Child = index --> only relevant if not replacement for whole matrix.
    // --------------------------------------------------------------------
    if (m_mode != ReplacementMode.MATRIX) {
      index = c.execute_int(n, 0, args);
    } else {
      index = 0;
    }
    // Retrieve the matrix.
    // --------------------
    char[][] matrix = getGPConfiguration().getMatrix(m_matrixName);
    int count = 0;
    if (matrix != null) {
      int cols = matrix.length;
      if(index >= cols) {
        index = cols - 1;
      }
      else if (index < 0) {
        index = 0;
      }

      int rows = matrix[index].length;
      switch (m_mode) {
        case ROW:
          for (int col = 0; col < cols; col++) {
            replace(matrix[col][index]);
          }
          break;
        case COLUMN:
          for (int row = 0; row < rows; row++) {
            replace(matrix[index][row]);
          }
          break;
        case MATRIX:
          for (int col = 0; col < cols; col++) {
            for (int row = 0; row < rows; row++) {
              replace(matrix[col][row]);
            }
          }
          break;
        case DIAGONAL:
          if (cols != rows) {
            throw new IllegalArgumentException("Cannot count diagonal of the" + " matrix, as the matrix is not square");
          }
          // Index less than half the number of columns/rows: first diagonal
          // Else: second diagonal
          if (index < cols / 2) {
            for (int cell = 0; cell < cols; cell++) {
              replace(matrix[cell][cell]);
            }
          } else {
            for (int cell = cols - 1; cell > 0; cell--) {
              replace(matrix[cell][cell]);
            }
          }
          break;
      }
    }
    return count;
  }

    @Override
  public void execute_void(ProgramChromosome c, int n, Object[] args) {
      execute_int(c, n, args);
    }

  /**
   * @param a_char the character to replace
   * @return replaced character, in case of no replacement the input character
   * is returned.
   *
   * @author Klaus Meffert
   * @since 3.4.3
   */
  private char replace(char a_char) {
    if (m_targetCharacters.indexOf(a_char) >= 0) {
      return m_replacement;
    } else {
      return a_char;
    }
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
    ReplaceInMatrix other = (ReplaceInMatrix) a_other;
    return new CompareToBuilder().append(m_matrixName, other.m_matrixName).
            append(m_mode, other.m_mode).
            append(m_targetCharacters, other.m_targetCharacters).
            append(m_replacement, other.m_replacement).
            append(getSubChildTypes(), other.getSubChildTypes()).toComparison();
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
      ReplaceInMatrix other = (ReplaceInMatrix) a_other;
      return super.equals(a_other) && new EqualsBuilder().append(m_matrixName, other.m_matrixName).
              append(m_mode, other.m_mode).
              append(m_targetCharacters, other.m_targetCharacters).
              append(m_replacement, other.m_replacement).
              append(getSubChildTypes(), other.getSubChildTypes()).isEquals();
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
      ReplaceInMatrix result = new ReplaceInMatrix(getGPConfiguration(),
              m_matrixName, m_mode, m_targetCharacters, m_replacement,
              getSubChildType(0));
      return result;
    } catch (Exception ex) {
      throw new CloneException(ex);
    }
  }

  public CommandGene applyMutation(int index, double a_percentage)
          throws InvalidConfigurationException {
    int mode = getGPConfiguration().getRandomGenerator().nextInt(ReplacementMode.
            values().length);
    ReplaceInMatrix mutant = new ReplaceInMatrix(getGPConfiguration(),
            m_matrixName, ReplacementMode.values()[mode], m_targetCharacters, m_replacement,
            getSubChildType(0));
    return mutant;
  }

  public enum ReplacementMode {

    COLUMN(1),
    ROW(2),
    DIAGONAL(3),
    MATRIX(4);
    private int m_value;

    public int intValue() {
      return m_value;
    }

    ReplacementMode(int a_value) {
      m_value = a_value;
    }
  }
}
