/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.gp.anttrail;

import org.jgap.gp.impl.*;
import org.jgap.*;

/**
 * Move the ant. Allows to specify how many times in a row a move will be
 * executed. The classic ant problem uses 1 as parameter value.
 *
 * @author Klaus Meffert
 * @since 3.01
 */
public class Move
    extends AntCommand {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.4 $";

  private int m_moves;

  /**
   * Standard constructor for classic ant problem.
   *
   * @param a_conf the configuration to use
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.01
   */
  public Move(final GPConfiguration a_conf)
      throws InvalidConfigurationException {
    this(a_conf, 1);
  }

  /**
   * Allows to move more than one time in a row.
   *
   * @param a_conf the configuration to use
   * @param a_moves number of moves to execute in a row
   *
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.01
   */
  public Move(final GPConfiguration a_conf, int a_moves)
      throws InvalidConfigurationException {
    super(a_conf);
    m_moves = a_moves;
  }

  public void execute_void(ProgramChromosome a_chrom, int a_n, Object[] a_args) {
    AntMap map = getMap(a_chrom);
    int x = map.getPosX();
    int y = map.getPosY();
    int orient = map.getOrientation();
    for (int i = 0; i < m_moves; i++) {
      switch (orient) {
        case AntMap.O_DOWN:
          y++;
          if (y >= map.getHeight()) {
            throw new IllegalStateException("y bigger than height");
          }
          map.setPosY(y);
          break;
        case AntMap.O_LEFT:
          x--;
          if (x < 0) {
            throw new IllegalStateException("x smaller zero");
          }
          map.setPosX(x);
          break;
        case AntMap.O_RIGHT:
          x++;
          if (x >= map.getWidth()) {
            throw new IllegalStateException("x bigger than width");
          }
          map.setPosX(x);
          break;
        case AntMap.O_UP:
          y--;
          if (y < 0) {
            throw new IllegalStateException("y smaller zero");
          }
          map.setPosY(y);
          break;
        default:
          throw new IllegalStateException("Illegal orientation");
      }
      map.IncrementMoveCounter();
    }
  }

  public String toString() {
    if (m_moves == 1) {
      return "move";
    }
    else {
      return "move" + m_moves;
    }
  }
}
