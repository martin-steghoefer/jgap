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

import org.jgap.gp.*;
import org.jgap.*;
import org.jgap.gp.impl.*;
import org.jgap.util.*;

public class TransferBoardToMemory
    extends CommandGene implements ICloneable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  private Board m_board;

  private int m_startMemoryIndex;

  public TransferBoardToMemory(final GPConfiguration a_conf, Board a_board,
                               int a_startMemoryIndex)
      throws InvalidConfigurationException {
    this(a_conf, a_board, a_startMemoryIndex, 0);
  }

  public TransferBoardToMemory(final GPConfiguration a_conf, Board a_board,
                               int a_startMemoryIndex, int a_subReturnType)
      throws InvalidConfigurationException {
    super(a_conf, 0, CommandGene.VoidClass, a_subReturnType, null);
    m_board = a_board;
    m_startMemoryIndex = a_startMemoryIndex;
  }

  public String toString() {
    return "transfer_Board_to_Mem(" + m_startMemoryIndex + ")";
  }

  /**
   * @return textual name of this command
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public String getName() {
    return "Transfer Board to Memory (index " + m_startMemoryIndex + ")";
  }

  /**
   * Executes the command.
   *
   * @param c ProgramChromosome
   * @param n ignored here
   * @param args ignored here
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void execute_void(ProgramChromosome c, int n, Object[] args) {
    check(c);
    int index = m_startMemoryIndex;
    for (int x = 0; x < Board.WIDTH; x++) {
      for (int y = 0; y < Board.HEIGHT; y++) {
        int boardValue = m_board.readField(x + 1, y + 1);
        getGPConfiguration().storeIndexedMemory(index++, new Integer(boardValue));
      }
    }
  }

  /**
   * Clones the object. Simple and straight forward implementation here.
   *
   * @return cloned instance of this object
   *
   * @author Klaus Meffert
   * @since 3.4
   */
  public Object clone() {
    try {
      TransferBoardToMemory result = new TransferBoardToMemory(
          getGPConfiguration(), m_board, m_startMemoryIndex, getSubReturnType());
      return result;
    } catch (Throwable t) {
      throw new CloneException(t);
    }
  }
}
