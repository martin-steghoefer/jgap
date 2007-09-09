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

import org.jgap.*;
import org.jgap.gp.*;
import org.jgap.gp.impl.*;

/**
 * If food-ahead then execute <child 0> else execute <child 1>.
 *
 * @author Klaus Meffert
 * @since 3.01
 */
public class IfFoodAheadElse
    extends AntCommand {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.7 $";

  private int m_lookAheadFields;

  /**
   * Standard constructor for the classic ant problem. Looks ahead 1 field.
   *
   * @param a_conf the configuration to use
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.01
   */
  public IfFoodAheadElse(final GPConfiguration a_conf)
      throws InvalidConfigurationException {
    this(a_conf, 1);
  }

  /**
   * Allows to specify how many fields to look ahead.
   *
   * @param a_conf the configuration to use
   * @param a_lookAheadFields look ahead n fields
   *
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.01
   */
  public IfFoodAheadElse(final GPConfiguration a_conf, int a_lookAheadFields)
      throws InvalidConfigurationException {
    super(a_conf, 2, CommandGene.VoidClass);
    m_lookAheadFields = a_lookAheadFields;
  }

  public void execute_void(ProgramChromosome a_chrom, int a_n, Object[] a_args) {
    AntMap map = getMap(a_chrom);
    int x = map.getPosX();
    int y = map.getPosY();
    int orient = map.getOrientation();
    int cell = AntMap.ERROR;
    switch (orient) {
      case AntMap.O_DOWN:
        if (y >= map.getHeight() - m_lookAheadFields) {
          cell = AntMap.EMPTY;
        }
        else {
          cell = map.getFromMap(x, y + m_lookAheadFields);
        }
        break;
      case AntMap.O_LEFT:
        if (x < m_lookAheadFields) {
          cell = AntMap.EMPTY;
        }
        else {
          cell = map.getFromMap(x - m_lookAheadFields, y);
        }
        break;
      case AntMap.O_RIGHT:
        if (x >= map.getWidth() - m_lookAheadFields) {
          cell = AntMap.EMPTY;
        }
        else {
          cell = map.getFromMap(x + m_lookAheadFields, y);
        }
        break;
      case AntMap.O_UP:
        if (y < m_lookAheadFields) {
          cell = AntMap.EMPTY;
        }
        else {
          cell = map.getFromMap(x, y - m_lookAheadFields);
        }
        break;
    }
    if (cell == AntMap.ERROR) {
      throw new IllegalStateException("IfFoodAheadElse: illegal cell content");
    }
    if (cell == AntMap.FOOD) {
      a_chrom.execute_void(a_n, 0, a_args);
    }
    else {
      a_chrom.execute_void(a_n, 1, a_args);
    }
  }

  public String toString() {
    if (m_lookAheadFields == 1) {
      return "if-food (&1) else (&2)";
    }
    else {
      return "if-food(" + m_lookAheadFields + ") (&1) else (&2)";
    }
  }
}
