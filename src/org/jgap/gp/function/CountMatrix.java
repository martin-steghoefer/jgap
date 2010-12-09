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
 * Counts either the elements in a row, in a column or in a diagonal of a
 * two-dimensional matrix in internal memory.
 * The function can be controlled to either count empty or non-empty or a
 * specific character.
 *
 * @author Klaus Meffert
 * @since 3.4.3
 */
public class CountMatrix
        extends CommandGene implements ICloneable, IMutateable {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.5 $";
  /**
   * Symbolic name of the matrix. Must correspond with a chosen name for
   * ReadFromMatrix.
   */
  private String m_matrixName;
  private CountType m_countType;
  private CountMode m_countMode;
  private char m_emptyCharacter;
  private char m_specificCharacter;

  public CountMatrix(final GPConfiguration a_conf, String a_matrixName,
          CountType a_countType, CountMode a_countMode, char a_emptyCharacter,
          char a_specificCharacter)
          throws InvalidConfigurationException {
    this(a_conf, a_matrixName, a_countType, a_countMode, a_emptyCharacter,
            a_specificCharacter, 0);
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
  public CountMatrix(final GPConfiguration a_conf, String a_matrixName,
          CountType a_countType, CountMode a_countMode, char a_emptyCharacter,
          char a_specificCharacter, int a_subChildType)
          throws InvalidConfigurationException {
    // Arity 1 = index
    super(a_conf, 1, CommandGene.IntegerClass, 0, new int[]{a_subChildType});
    if (a_matrixName == null || a_matrixName.length() < 1) {
      throw new IllegalArgumentException("Matrix name must not be empty!");
    }
    m_matrixName = a_matrixName;
    m_countType = a_countType;
    m_countMode = a_countMode;
    m_emptyCharacter = a_emptyCharacter;
    m_specificCharacter = a_specificCharacter;
  }

  public String toString() {
    return "countMatrix(" + m_matrixName + ", &1)";
  }

  /**
   * @return textual name of this command
   *
   * @author Klaus Meffert
   * @since 3.4.3
   */
  public String getName() {
    return "CountMatrix(" + m_matrixName + ")";
  }

  @Override
  public int execute_int(ProgramChromosome c, int n, Object[] args) {
    int index;
    // Child = index --> only relevant if not replacement for whole matrix.
    // --------------------------------------------------------------------
    if (m_countType != CountType.MATRIX) {
      index = c.execute_int(n, 0, args);
    }
    else {
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
      switch (m_countType) {
        case ROW:
          for (int col = 0; col < cols; col++) {
            count += count(matrix[col][index]);
          }
          break;
        case COLUMN:
          for (int row = 0; row < rows; row++) {
            count += count(matrix[index][row]);
          }
          break;
        case MATRIX:
          for (int col = 0; col < cols; col++) {
          for (int row = 0; row < rows; row++) {
            count += count(matrix[col][row]);
          }
          }
          break;
        case DIAGONAL:
          if (cols != rows) {
            throw new IllegalArgumentException("Cannot count diagonal of the"
                    +" matrix, as the matrix is not square");
          }
          // Index less than half the number of columns/rows: first diagonal
          // Else: second diagonal
          if (index < cols / 2) {
            for (int cell = 0; cell < cols; cell++) {
              count += count(matrix[cell][cell]);
            }
          } else {
            for (int cell = cols - 1; cell > 0; cell--) {
              count += count(matrix[cell][cell]);
            }
          }
          break;
      }
    }
    return count;
  }

  /**
   * @param the character to compare
   * @return 1 if a match occured, 0 otherwise
   *
   * @author Klaus Meffert
   * @since 3.4.3
   */
  private int count(char a_char) {
    switch (m_countMode) {
      case NONEMPTY:
        if (a_char != m_emptyCharacter) {
          return 1;
        } else {
          return 0;
        }
      case EMPTY:
        if (a_char == m_emptyCharacter) {
          return 1;
        } else {
          return 0;
        }
      case SPECIFIC:
        if (a_char == m_specificCharacter) {
          return 1;
        } else {
          return 0;
        }
    }
    return 0;

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
    CountMatrix other = (CountMatrix) a_other;
    return new CompareToBuilder().append(m_matrixName, other.m_matrixName).
            append(m_countType, other.m_countType).
            append(m_countMode, other.m_countMode).
            append(m_emptyCharacter, other.m_emptyCharacter).
            append(m_specificCharacter, other.m_specificCharacter).
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
      CountMatrix other = (CountMatrix) a_other;
      return super.equals(a_other)
          && new EqualsBuilder().append(m_matrixName, other.m_matrixName).
              append(m_countType, other.m_countType).
              append(m_countMode, other.m_countMode).
              append(m_emptyCharacter, other.m_emptyCharacter).
              append(m_specificCharacter, other.m_specificCharacter).
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
      CountMatrix result = new CountMatrix(getGPConfiguration(),
              m_matrixName, m_countType, m_countMode, m_emptyCharacter, m_specificCharacter,
              getSubChildType(0));
      return result;
    } catch (Exception ex) {
      throw new CloneException(ex);
    }
  }

  public CommandGene applyMutation(int index, double a_percentage)
          throws InvalidConfigurationException {
    int type_ = getGPConfiguration().getRandomGenerator().nextInt(CountType.
            values().length);
    int mode = getGPConfiguration().getRandomGenerator().nextInt(CountMode.
            values().length);
    CountMatrix mutant = new CountMatrix(getGPConfiguration(),
            m_matrixName, CountType.values()[type_], CountMode.values()[mode],
            m_emptyCharacter, m_specificCharacter, getSubChildType(0));
    return mutant;
  }

  public enum CountType {
/**@todo count over whole matrix*/
    COLUMN(1),
    ROW(2),
    DIAGONAL(3),
    MATRIX(4);
    private int m_value;

    public int intValue() {
      return m_value;
    }

    CountType(int a_value) {
      m_value = a_value;
    }
  }

  public enum CountMode {

    EMPTY(1),
    NONEMPTY(2),
    SPECIFIC(3);
    private int m_value;

    public int intValue() {
      return m_value;
    }

    CountMode(int a_value) {
      m_value = a_value;
    }
  }
}
