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

/**
 * Puts a stone on a board. Accepts two parameters, representing X and Y
 * coordinates on the board.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public class PutStone
    extends CommandGene implements ICloneable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.5 $";

  private Board m_board;

  private int m_color;

  public PutStone(final GPConfiguration a_conf, Board a_board, int a_color)
      throws InvalidConfigurationException {
    this(a_conf, a_board, a_color, 0, null);
  }

  public PutStone(final GPConfiguration a_conf, Board a_board, int a_color,
                  int a_subReturnType, int[] a_subChildTypes)
      throws InvalidConfigurationException {
    super(a_conf, 2, CommandGene.VoidClass, a_subReturnType, a_subChildTypes);
    m_board = a_board;
    m_color = a_color;
  }

  public String toString() {
    return "put_stone(&1, &2)";
  }

  public void execute_void(ProgramChromosome c, int n, Object[] args) {
    check(c);
    int x = c.execute_int(n, 0, args);
    int y = c.execute_int(n, 1, args);
//    if (!c.getGene(1).getClass().getName().equals("org.jgap.gp.function.ReadTerminalIndexed")
//        ||
//        !c.getGene(2).getClass().getName().equals("org.jgap.gp.function.ReadTerminalIndexed")) {
//      int a = 2;
//      a *= 3;
//    }
    // Put stone on board.
    // -------------------
    boolean gameWon = m_board.putStone(x, y, m_color);
    // If game won, quit GP-program.
    // -----------------------------
    if (gameWon) {
      throw new GameWonException(m_color, "Game won by color " + m_color);
    }
  }

  protected void check(ProgramChromosome a_program) {
    if (m_board.getLastColor() == m_color) {
      throw new IllegalStateException("Only one stone of a color per round!");
    }
  }

  /**
   * Determines which type a specific child of this command has.
   *
   * @param a_ind ignored here
   * @param a_chromNum index of child
   * @return type of the a_chromNum'th child
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public Class getChildType(IGPProgram a_ind, int a_chromNum) {
//    if (a_chromNum == 0 || a_chromNum == 1) {
    return CommandGene.IntegerClass;
//    }
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
      PutStone result = new PutStone(getGPConfiguration(), m_board, m_color,
                                     getSubReturnType(), getSubChildTypes());
      return result;
    } catch (Throwable t) {
      throw new CloneException(t);
    }
  }
}
