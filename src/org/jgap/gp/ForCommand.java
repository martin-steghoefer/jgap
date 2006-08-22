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
 * The for-loop.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class ForCommand
    extends MathCommand {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  public ForCommand(final Configuration a_conf, Class type)
      throws InvalidConfigurationException {
    super(a_conf, 2, type);
  }

  protected Gene newGeneInternal() {
    return null;/**@todo implement if necessary*/
  }

  public void applyMutation(int index, double a_percentage) {
    // This is not applicable for this command, just do nothing
    System.err.println("appliedMutation");
  }

  public String toString() {
    return "for(int i=0;i<&1;i++)";
  }

  public int execute_int(ProgramChromosome c, int n, Object[] args) {
    /**@todo check if elements deferring the state are available in the sub
     * branch. If not, the sub branch needs only be executed once.
     * Appropriate elements are, for example: PushCommand and PopCommand
     */
    int x = c.execute_int(n, 0, args);
    if (x > 15) {
      x = 15;/**@todo parameterize*/
    }
    int value = 0;
    for (int i = 0; i < x; i++) {
      value = c.execute_int(n, 1, args);
    }
    return value;
  }

  public static interface Compatible {
    public Object execute_for(Object o);
  }
}
