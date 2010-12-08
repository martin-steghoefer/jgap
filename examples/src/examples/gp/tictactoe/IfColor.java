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
 * The if-then construct. If-Condition is: if specific color, then
 * execute X else do nothing.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public class IfColor
    extends CommandGene implements ICloneable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  private Class m_type;

  private int m_color;

  public IfColor(final GPConfiguration a_conf, Class a_type,
                 int a_color)
      throws InvalidConfigurationException {
    this(a_conf, a_type, a_color, 0, null);
  }

  /**
   * Allows setting the sub child type.
   *
   * @param a_conf GPConfiguration
   * @param a_type Class
   * @param a_color int
   * @param a_subChildType1 int
   * @param a_subChildType2 int
   * @throws InvalidConfigurationException
   *
   * @since 3.6
   */
  public IfColor(final GPConfiguration a_conf, Class a_type,
                 int a_color, int a_subChildType1, int a_subChildType2)
      throws InvalidConfigurationException {
    this(a_conf, a_type, a_color, 0, new int[]{a_subChildType1, a_subChildType2});
  }

  public IfColor(final GPConfiguration a_conf, Class a_type,
                 int a_color, int a_subReturnType, int[] a_subChildTypes)
      throws InvalidConfigurationException {
    super(a_conf, 2, CommandGene.VoidClass, a_subReturnType, a_subChildTypes);
    m_type = a_type;
    m_color = a_color;
  }

  public String toString() {
    return "if iscolor(" + m_color + ", &1) then (&2)";
  }

  /**
   * @return textual name of this command
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public String getName() {
    return "If is Color";
  }

  public void execute_void(ProgramChromosome c, int n, Object[] args) {
    check(c);
    boolean condition;
    if (m_type == CommandGene.IntegerClass) {
      condition = c.execute_int(n, 0, args) == m_color;
    }
    else if (m_type == CommandGene.LongClass) {
      condition = c.execute_long(n, 0, args) == m_color;
    }
    else {
      throw new IllegalStateException("IfColor: cannot process type " + m_type);
    }
    if (condition) {
      c.execute_void(n, 1, args);
    }
  }

  public int execute_int(ProgramChromosome c, int n, Object[] args) {
    check(c);
    int value = c.execute_int(n, 0, args);
    if (value == m_color) {
      c.execute_void(n, 1, args);
    }
    return value;
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
    if (a_chromNum == 0) {
      return m_type;
    }
    return CommandGene.VoidClass;
  }

  public Object clone() {
    try {
      int[] subChildTypes = getSubChildTypes();
      if (subChildTypes != null) {
        subChildTypes = (int[])subChildTypes.clone();
      }
      IfColor result = new IfColor(getGPConfiguration(), m_type, m_color,
                                   getSubReturnType(), subChildTypes);
      return result;
    } catch (Throwable t) {
      throw new CloneException(t);
    }
  }
}
