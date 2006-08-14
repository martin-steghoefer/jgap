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
 * The if-then-else construct.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class IfElseCommand
    extends MathCommand {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  public IfElseCommand(final Configuration a_conf, Class type)
      throws InvalidConfigurationException {
    super(a_conf, 3, type);
  }

  protected Gene newGeneInternal() {
    return null;
  }

  public void applyMutation(int index, double a_percentage) {
    // This is not applicable for this command, just do nothing
    System.err.println("appliedMutation");
  }

  public String toString() {
    return "if-then-else";
  }

  public int execute_int(ProgramChromosome c, int n, Object[] args) {
    int x = c.execute_int(n, 0, args);
    int value = 0;
    if (x>=0) {
      value = c.execute_int(n, 1, args);
    }
    else {
      value = c.execute_int(n, 2, args);
    }
    return value;
  }

  public static interface Compatible {
    public Object execute_add(Object o);
  }
}
