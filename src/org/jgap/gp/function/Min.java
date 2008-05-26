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
 * Returns the smaller of two valus.
 *
 * @author Klaus Meffert
 * @since 3.3.4
 */
public class Min
    extends MathCommand {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  public Min(final GPConfiguration a_conf, Class type)
      throws InvalidConfigurationException {
    super(a_conf, 1, type);
  }

  public String toString() {
    return "min &1";
  }

  /**
   * @return textual name of this command
   *
   * @author Klaus Meffert
   * @since 3.3.4
   */
  public String getName() {
    return "Min";
  }

  public float execute_float(ProgramChromosome c, int n, Object[] args) {
    float f1 = c.execute_float(n, 0, args);
    float f2 = c.execute_float(n, 1, args);
    return Math.min(f1, f2);
  }

  public double execute_double(ProgramChromosome c, int n, Object[] args) {
    double f1 = c.execute_double(n, 0, args);
    double f2 = c.execute_double(n, 1, args);
    return Math.min(f1, f2);
  }

}