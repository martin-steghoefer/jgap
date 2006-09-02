/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp.function;

import org.jgap.*;
import org.jgap.gp.*;
import org.jgap.gp.impl.*;

/**
 * The multiply operation with three argument (X * Y * Z).
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class Multiply3
    extends MathCommand {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.4 $";

  public Multiply3(final GPConfiguration a_conf, Class a_type)
      throws InvalidConfigurationException {
    super(a_conf, 3, a_type);
  }

  public String toString() {
    return "&1 * &2 * &3";
  }

  public int execute_int(ProgramChromosome c, int n, Object[] args) {
    return c.execute_int(n, 0, args) * c.execute_int(n, 1, args) *
        c.execute_int(n, 2, args);
  }

  public long execute_long(ProgramChromosome c, int n, Object[] args) {
    return c.execute_long(n, 0, args) * c.execute_long(n, 1, args) *
        c.execute_long(n, 2, args);
  }

  public float execute_float(ProgramChromosome c, int n, Object[] args) {
    return c.execute_float(n, 0, args) * c.execute_float(n, 1, args) *
        c.execute_float(n, 2, args);
  }

  public double execute_double(ProgramChromosome c, int n, Object[] args) {
    return c.execute_double(n, 0, args) * c.execute_double(n, 1, args) *
        c.execute_double(n, 2, args);
  }

  public Object execute_object(ProgramChromosome c, int n, Object[] args) {
    return ( (Compatible) ( (Compatible) c.execute_object(n, 0, args)).
            execute_multiply3(c.execute_object(n, 1, args))).execute_multiply3(
                c.execute_object(n, 2, args));
  }

  protected interface Compatible {
    public Object execute_multiply3(Object o);
  }
}
