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

public class IsOwnColor
    extends CommandGene implements ICloneable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.4 $";

  private int m_color;

  private int m_subChildType; // Only needed for cloning!

  public IsOwnColor(final GPConfiguration a_conf, int a_color)
      throws InvalidConfigurationException {
    this(a_conf, a_color, 0, 0);
  }

  public IsOwnColor(final GPConfiguration a_conf, int a_color,
                    int a_subReturnType, int a_subChildType)
      throws InvalidConfigurationException {
    super(a_conf, 1, CommandGene.BooleanClass, a_subReturnType, a_subChildType);
    m_subChildType = a_subChildType;
    m_color = a_color;
  }

  public String toString() {
    return "isOwnColor(&1)";
  }

  public boolean execute_boolean(ProgramChromosome c, int n, Object[] args) {
    check(c);
    int color = c.execute_int(n, 0, args);
    return color == m_color;
  }

  /**
   * @return textual name of this command
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public String getName() {
    return "If Color";
  }

  public Object clone() {
    try {
      IsOwnColor result = new IsOwnColor(getGPConfiguration(), m_color,
          getSubReturnType(), m_subChildType);
      return result;
    } catch (Throwable t) {
      throw new CloneException(t);
    }
  }

  public Class getChildType(IGPProgram a_ind, int a_chromNum) {
    return CommandGene.IntegerClass;
  }
}
