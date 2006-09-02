/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.gp.anttrail;

import org.jgap.*;
import org.jgap.gp.*;
import org.jgap.gp.impl.*;

/**
 * If food-ahead then execute <child 0> else execute <child 1>
 *
 * @author Klaus Meffert
 * @since 3.01
 */
public class IfFoodAheadElse
    extends AntCommand {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  private int m_lookAheadFields;

  public IfFoodAheadElse(final GPConfiguration a_conf)
      throws InvalidConfigurationException {
    this(a_conf, 1);
  }

  public IfFoodAheadElse(final GPConfiguration a_conf, int a_lookAheadFields)
      throws InvalidConfigurationException {
    super(a_conf, 2, CommandGene.VoidClass);
    m_lookAheadFields = a_lookAheadFields;
  }

  public void execute_void(ProgramChromosome a_ind, int a_n, Object[] a_args) {
    AntMap map = getMap();
    int x = map.getPosX();
    int y = map.getPosX();
    int orient = map.getOrientation();
    int cell = AntMap.ERROR;
    switch (orient) {
      case AntMap.O_DOWN:
        if (y >= map.getHeight() - m_lookAheadFields) {
          cell = AntMap.EMPTY;
        }
        cell = map.getFromMap(x, y + m_lookAheadFields);
        break;
      case AntMap.O_LEFT:
        if (x < m_lookAheadFields) {
          cell = AntMap.EMPTY;
        }
        else {
          cell = map.getFromMap(x - 1, y);
        }
        break;
      case AntMap.O_RIGHT:
        if (x >= map.getWidth() - m_lookAheadFields) {
          cell = AntMap.EMPTY;
        }
        cell = map.getFromMap(x + 1, y);
        break;
      case AntMap.O_UP:
        if (y < m_lookAheadFields) {
          cell = AntMap.EMPTY;
        }
        else {
          cell = map.getFromMap(x, y - 1);
        }
        break;
    }
    if (cell == AntMap.ERROR) {
      throw new IllegalStateException("IfFoodAheadElse: illegal cell content");
    }
    if (cell == AntMap.FOOD) {
      a_ind.execute_void(a_n, 0, a_args);
    }
    else {
      a_ind.execute_void(a_n, 1, a_args);
    }
    map.IncrementMoveCounter();
  }

  public String toString() {
    return "if-food (&1) else (&2)";
  }
}
