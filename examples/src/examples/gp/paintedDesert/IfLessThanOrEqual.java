/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.gp.paintedDesert;

import org.jgap.*;
import org.jgap.gp.*;
import org.jgap.gp.impl.*;

/**
 * A four argument version of if-less-than-or-equal (IFLTE) construct.  If the
 * first argument is less than the second argument, then return the third
 * argument, else the return the fourth argument.  Mimics the Lisp function from
 * Koza.
 *
 * @author Scott Mueller
 * @since 3.2
 */
public class IfLessThanOrEqual
    extends CommandGene {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  /**
   * Returns the type of the arguments for this function
   */
  private Class m_type;

  /**
   * Creates the 4 argument IfLessThanOrEqual.
   *
   * @param a_conf
   * @param a_type
   * @throws InvalidConfigurationException
   */
  public IfLessThanOrEqual(final GPConfiguration a_conf, Class a_type)
      throws InvalidConfigurationException {
    super(a_conf, 4, CommandGene.VoidClass);
    m_type = a_type;
  }

  /**
   * Reutrns the program listing name
   */
  public String toString() {
    return "if(&1) <= (&2) then (&3) else(&4)";
  }

  /**
   * @return textual name of this command
   *
   */
  public String getName() {
    return "IfElse4Arg";
  }

  /**
   * Executes the IfLessThanOrEqual for void arguments
   */
  public void execute_void(ProgramChromosome c, int n, Object[] args) {
    check(c);
    boolean condition;
    if (m_type == CommandGene.IntegerClass) {
      condition = c.execute_int(n, 0, args) <= c.execute_int(n, 1, args);
    }
    else if (m_type == CommandGene.BooleanClass) {
      condition = c.execute_boolean(n, 0, args);
    }
    else if (m_type == CommandGene.LongClass) {
      condition = c.execute_long(n, 0, args) <= c.execute_long(n, 1, args);
    }
    else if (m_type == CommandGene.DoubleClass) {
      condition = c.execute_double(n, 0, args) <= c.execute_double(n, 1, args);
    }
    else if (m_type == CommandGene.FloatClass) {
      condition = c.execute_float(n, 0, args) <= c.execute_float(n, 1, args);
    }
    else {
      throw new IllegalStateException("IfElse: cannot process type " + m_type);
    }
    if (condition) {
      c.execute_void(n, 2, args);
    }
    else {
      c.execute_void(n, 3, args);
    }
  }

  /**
   * Determines which type a specific child of this command has.
   *
   * @param a_ind ignored here
   * @param a_chromNum index of child
   * @return type of the a_chromNum'th child
   *
   */
  public Class getChildType(IGPProgram a_ind, int a_chromNum) {
    if (a_chromNum == 0 || a_chromNum == 1) {
      return m_type;
    }
    return CommandGene.VoidClass;
  }
}
