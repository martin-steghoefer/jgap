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
 * A three argument version of if-less-than-zero (IFLTZ) construct.  If the
 * first argument is less than zero, then return the second argument, else the
 * return the third argument.  Mimics the Lisp function from Koza.
 *
 * @author Scott Mueller
 * @since 3.2
 */
public class IfLessThanZero
    extends CommandGene {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  /**
   * Returns the type of the arguments
   */
  private Class m_type;

  /**
   * Constructor for the less than zero function
   * @param a_conf
   * @param a_type
   * @throws InvalidConfigurationException
   */
  public IfLessThanZero(final GPConfiguration a_conf, Class a_type)
      throws InvalidConfigurationException {
    super(a_conf, 3, CommandGene.VoidClass);
    m_type = a_type;
  }

  /**
   * Program listing for the function
   */
  public String toString() {
    return "if(&1) < 0 then (&2) else(&3)"; //KM: &2 and &3 were &3 and &4
  }

  /**
   * @return textual name of this command
   *
   */
  public String getName() {
    return "IfLessThanZero3Arg (&1) then (&2) else(&3)";
  }

  /**
   * Executes the If less than zero function for void arguments
   */
  public void execute_void(ProgramChromosome c, int n, Object[] args) {
    check(c);
    boolean condition;
    if (m_type == CommandGene.IntegerClass) {
      condition = c.execute_int(n, 0, args) < 0;
    }
    else if (m_type == CommandGene.BooleanClass) {
      condition = c.execute_boolean(n, 0, args);
    }
    else if (m_type == CommandGene.LongClass) {
      condition = c.execute_long(n, 0, args) < 0;
    }
    else if (m_type == CommandGene.DoubleClass) {
      condition = c.execute_double(n, 0, args) < 0;
    }
    else if (m_type == CommandGene.FloatClass) {
      condition = c.execute_float(n, 0, args) < 0;
    }
    else {
      throw new IllegalStateException("IfLessThanZero: cannot process type " +
                                      m_type);
    }
    if (condition) {
      c.execute_void(n, 1, args);
    }
    else {
      c.execute_void(n, 2, args);
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
    if (a_chromNum == 0) {
      return m_type;
    }
    return CommandGene.VoidClass;
  }
}
