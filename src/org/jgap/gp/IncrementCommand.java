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
 * The increment-by-one operation.
 *
 * @author Konrad Odell
 * @author Klaus Meffert
 * @since 3.0
 */
public class IncrementCommand
    extends MathCommand {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.2 $";

  public IncrementCommand(final Configuration a_conf, Class a_type)
      throws InvalidConfigurationException {
    super(a_conf, 1, a_type);
  }

  protected Gene newGeneInternal() {
    try {
      Gene gene = new IncrementCommand(getConfiguration(), getReturnType());
      return gene;
    }
    catch (InvalidConfigurationException iex) {
      throw new IllegalStateException(iex.getMessage());
    }
  }

  public void applyMutation(int index, double a_percentage) {
    // Here, we could mutate the parameter of the command.
    // This is not applicable for this command, just do nothing
  }

  public String toString() {
    return "INC(&1)";
  }

  public int execute_int(ProgramChromosome c, int n, Object[] args) {
    return c.execute_int(n, 1, args) + 1;
  }

  public long execute_long(ProgramChromosome c, int n, Object[] args) {
    return c.execute_long(n, 1, args) + 1;
  }

  public float execute_float(ProgramChromosome c, int n, Object[] args) {
    return c.execute_float(n, 1, args) + 1;
  }

  public double execute_double(ProgramChromosome c, int n, Object[] args) {
    return c.execute_double(n, 1, args) + 1;
  }

/**@todo execute_object*/

  public static interface Compatible {
    public Object execute_increment(Object o);
  }


}
