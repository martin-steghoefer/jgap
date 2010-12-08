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
import org.jgap.util.ICloneable;
import org.jgap.util.CloneException;

public class CountStones
    extends CommandGene implements ICloneable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.4 $";

  private Board m_board;

  private String m_memoryNameBase;

  private int m_color;

  public CountStones(final GPConfiguration a_conf, Board a_board, int a_color,
                     String a_memoryNameBase)
      throws InvalidConfigurationException {
    this(a_conf, a_board, a_color, a_memoryNameBase, 0);
  }

  public CountStones(final GPConfiguration a_conf, Board a_board, int a_color,
                     String a_memoryNameBase, int a_subReturnType)
      throws InvalidConfigurationException {
    super(a_conf, 0, CommandGene.VoidClass, a_subReturnType, null);
    m_board = a_board;
    m_memoryNameBase = a_memoryNameBase;
    m_color = a_color;
  }

  public String toString() {
    return "Count Stones(" + m_color + ", " + m_memoryNameBase + ")";
  }

  /**
   * @return textual name of this command
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public String getName() {
    return "Count Stones";
  }

  public void execute_void(ProgramChromosome c, int n, Object[] args) {
    check(c);
    // rows
    for (int i = 0; i < Board.WIDTH; i++) {
      readRow(i, m_memoryNameBase);
    }
    // columns
    for (int i = 0; i < Board.HEIGHT; i++) {
      readCol(i, m_memoryNameBase);
    }
    // diagonals
    readDia(0, m_memoryNameBase);
    readDia(1, m_memoryNameBase);
  }

  private void readRow(int i, String a_baseName) {
    String memoryName = a_baseName + "r" + i;
    int count = 0;
    for (int x = 0; x < Board.WIDTH; x++) {
      if (m_board.readField(x + 1, i + 1) == m_color) {
        count++;
      }
    }
    store(memoryName, count);
  }

  private void readCol(int i, String a_baseName) {
    String memoryName = a_baseName + "c" + i;
    int count = 0;
    for (int y = 0; y < Board.HEIGHT; y++) {
      if (m_board.readField(i + 1, y + 1) == m_color) {
        count++;
      }
    }
    store(memoryName, count);
  }

  private void readDia(int index, String a_baseName) {
    String memoryName = a_baseName + "d" + index;
    int count = 0;
    int x;
    int y;
    int increment;
    if (index == 0) {
      increment = 1;
      x = 1;
      y = 1;
    }
    else {
      increment = -1;
      x = Board.WIDTH;
      y = 1;
    }
    for (int i = 0; i < Board.HEIGHT; i++) {
      if (m_board.readField(x, y) == m_color) {
        count++;
      }
      y++;
      x = x + increment;
    }
    store(memoryName, count);
  }

  private void store(String memoryName, int a_count) {
    getGPConfiguration().storeInMemory(memoryName, new Integer(a_count));
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
      CountStones result = new CountStones(getGPConfiguration(), m_board,
          m_color, m_memoryNameBase, getSubReturnType());
      return result;
    } catch (Throwable t) {
      throw new CloneException(t);
    }
  }
}
