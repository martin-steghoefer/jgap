/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp;

import org.jgap.*;
import org.jgap.gp.*;

/**
 * Pushes a value onto the stack.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class PushCommand
    extends MathCommand {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  public PushCommand(final Configuration a_conf, Class type)
      throws InvalidConfigurationException {
    super(a_conf, 1, type);
  }

  protected Gene newGeneInternal() {
    /**@todo not used for GP*/
    try {
      Gene gene = new PushCommand(getConfiguration(), getReturnType());
      return gene;
    }
    catch (InvalidConfigurationException iex) {
      throw new IllegalStateException(iex.getMessage());
    }
  }

  public void applyMutation(int index, double a_percentage) {
    // Here, we could mutate the parameter of the command.
    // This is not applicable for this command, just do nothing
    System.err.println("appliedMutation");
  }

  public String toString() {
    return "push &1";
  }

  public int execute_int(ProgramChromosome c, int n, Object[] args) {
    int value = c.execute_int(n, 0, args);
    //push onto stack
    ((GPConfiguration)getConfiguration()).pushToStack(new Integer(value));
    return value;
  }
  /**@todo execute for float, long, Object ...*/

  public static interface Compatible {
    public Object execute_add(Object o);
  }
}
