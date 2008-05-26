/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp.function;

import org.jgap.*;
import org.jgap.gp.*;
import org.jgap.gp.impl.*;

/**
 * Returns the absolute value of a number.
 *
 * @author Klaus Meffert
 * @since 3.3.4
 */
public class Abs
    extends MathCommand {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  public Abs(final GPConfiguration a_conf, Class type)
      throws InvalidConfigurationException {
    super(a_conf, 1, type);
  }

  public String toString() {
    return "abs &1";
  }

  /**
   * @return textual name of this command
   *
   * @author Klaus Meffert
   * @since 3.3.4
   */
  public String getName() {
    return "Abs";
  }

  public float execute_float(ProgramChromosome c, int n, Object[] args) {
    float f = c.execute_float(n, 0, args);
    return Math.abs(f);
  }

  public double execute_double(ProgramChromosome c, int n, Object[] args) {
    double d = c.execute_double(n, 0, args);
    return Math.abs(d);
  }

  public Object execute_object(ProgramChromosome c, int n, Object[] args) {
    return ( (Compatible) c.execute_object(n, 0, args)).execute_abs();
  }

  protected interface Compatible {
    public Object execute_abs();
  }
}