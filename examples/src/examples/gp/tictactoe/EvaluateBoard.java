/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.gp.tictactoe;

import org.jgap.*;
import org.jgap.gp.*;
import org.jgap.gp.impl.*;
import org.jgap.util.*;

/**
 * Evaluates the board. Generates one unique number for each board position.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public class EvaluateBoard
    extends CommandGene implements ICloneable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.4 $";

  private Board m_board;

  private int m_index;

  private Class m_type;
  private int m_subChildType;

  public EvaluateBoard(final GPConfiguration a_conf, Board a_board,
                       int a_index)
      throws InvalidConfigurationException {
    this(a_conf, a_board, a_index, 0);
  }

  public EvaluateBoard(final GPConfiguration a_conf, Board a_board,
                       int a_index, int a_subReturnType)
      throws InvalidConfigurationException {
    super(a_conf, 0, CommandGene.VoidClass, a_subReturnType, null);
    m_board = a_board;
    m_index = a_index;
    m_type = CommandGene.IntegerClass;
    m_subChildType = -1;
  }

  public EvaluateBoard(final GPConfiguration a_conf, Board a_board,
                       Class a_type)
      throws InvalidConfigurationException {
    this(a_conf, a_board, a_type, 0, 0);
  }

  public EvaluateBoard(final GPConfiguration a_conf, Board a_board,
                       Class a_type, int a_subReturnType, int a_subChildType)
      throws InvalidConfigurationException {
    super(a_conf, 1, CommandGene.VoidClass, a_subReturnType, a_subChildType);
    m_board = a_board;
    m_index = -1;
    m_type = a_type;
    m_subChildType = a_subChildType;
  }

  public String toString() {
    if (m_index >= 0) {
      return "eval_board(" + m_index + ")";
    }
    else {
      return "eval_board(&1)";
    }
  }

  public Class getChildType(IGPProgram a_ind, int a_chromNum) {
    if (m_index < 0) {
      return m_type;
    }
    else {
      return null;
    }
  }

  /**
   * @return textual name of this command
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public String getName() {
    if (m_index >= 0) {
      return "Evaluate Board(" + m_index + ")";
    }
    else {
      return "Evaluate Board(" + m_index + ")";
    }
  }

  public void execute_void(ProgramChromosome c, int n, Object[] args) {
    check(c);
    int evaluation = 0;
    int index = 0;
    for (int x = 0; x < Board.WIDTH; x++) {
      for (int y = 0; y < Board.HEIGHT; y++) {
        // Add 1 to board value to eliminate zeros.
        // ----------------------------------------
        int boardValue = m_board.readField(x + 1, y + 1) + 1;
        // Adapt base of exponentiation to get unique values.
        // --------------------------------------------------
        evaluation += Math.pow(3 + (index * 2), (boardValue));
        index++;
      }
    }
    int memoryIndex;
    if (m_index < 0) {
      memoryIndex = c.execute_int(n, 0, args);
      /**@todo support other types than integer*/
    }
    else {
      memoryIndex = m_index;
    }
    getGPConfiguration().storeIndexedMemory(memoryIndex, new Integer(evaluation));
  }

  /**
   * Clones the object. Simple and straight forward implementation here.
   *
   * @return cloned instance of this object
   *
   * @author Klaus Meffert
   * @since 3.6
   */
  public Object clone() {
    try {
      EvaluateBoard result;
      if (m_subChildType >= 0) {
        result = new EvaluateBoard(getGPConfiguration(), m_board,
            m_type, getSubReturnType(), m_subChildType);
      }
      else {
        result = new EvaluateBoard(getGPConfiguration(), m_board, m_index,
            getSubReturnType());
      }
      return result;
    } catch (Throwable t) {
      throw new CloneException(t);
    }
  }
}
