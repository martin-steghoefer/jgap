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

public class ReadBoard
    extends CommandGene implements ICloneable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.4 $";

  private Board m_board;

  public ReadBoard(final GPConfiguration a_conf, Board a_board)
      throws InvalidConfigurationException {
    this(a_conf, a_board, 0, null);
  }

  public ReadBoard(final GPConfiguration a_conf, Board a_board,
                   int a_subReturnType, int[] a_subChildTypes)
      throws InvalidConfigurationException {
    super(a_conf, 2, CommandGene.IntegerClass, a_subReturnType, a_subChildTypes);
    m_board = a_board;
  }

  public String toString() {
    return "read_board(&1, &2)";
  }

  /**
   * @return textual name of this command
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public String getName() {
    return "Read Board(x,y)";
  }

  public int execute_int(ProgramChromosome c, int n, Object[] args) {
    check(c);
    int x = c.execute_int(n, 0, args);
    int y = c.execute_int(n, 1, args);
    // Store in memory.
    // ----------------
    return m_board.readField(x, y);
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
      ReadBoard result = new ReadBoard(getGPConfiguration(), m_board,
                                       getSubReturnType(), getSubChildTypes());
      return result;
    } catch (Throwable t) {
      throw new CloneException(t);
    }
  }
}
