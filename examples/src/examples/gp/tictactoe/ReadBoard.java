package examples.gp.tictactoe;

import org.jgap.gp.*;
import org.jgap.*;
import org.jgap.gp.impl.*;

public class ReadBoard
    extends CommandGene {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

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
}
