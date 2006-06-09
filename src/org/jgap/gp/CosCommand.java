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
 * The cosine command.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class CosCommand
    extends MathCommand {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  public CosCommand(final Configuration a_conf, Class type)
      throws InvalidConfigurationException {
    super(a_conf, 1, type);
  }

  protected Gene newGeneInternal() {
    try {
      Gene gene = new CosCommand(getConfiguration(), getReturnType());
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
    return "cos";
  }

  public float execute_float(ProgramChromosome c, int n, Object[] args) {
    float f = c.execute_float(n, 0, args);
    // clip to -10000 -> 10000
    return (float) Math.cos(Math.max( -10000.0f, Math.min(f, 10000.0f)));
  }

  public double execute_double(ProgramChromosome c, int n, Object[] args) {
    double d = c.execute_double(n, 0, args);
    // clip to -10000 -> 10000
    return Math.cos(Math.max( -10000.0, Math.min(d, 10000.0)));
  }

  public Object execute_object(ProgramChromosome c, int n, Object[] args) {
    return ( (Compatible) c.execute_object(n, 0, args)).execute_cos();
  }

  public static interface Compatible {
    public Object execute_cos();
  }
}
