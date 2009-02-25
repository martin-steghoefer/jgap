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
import org.jgap.util.*;

/**
 * The add operation with four parameters (W + X + Y + Z).
 *
 * @author Klaus Meffert
 * @since 3.3.3.4
 */
public class Add4
    extends MathCommand implements ICloneable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  public Add4(final GPConfiguration a_conf, Class a_returnType)
      throws InvalidConfigurationException {
    super(a_conf, 4, a_returnType);
  }

  public String toString() {
    return "&1 + &2 + &3 + &4";
  }

  /**
   * @return textual name of this command
   *
   * @author Klaus Meffert
   * @since 3.3.3.4
   */
  public String getName() {
    return "Add4";
  }

  public int execute_int(ProgramChromosome c, int n, Object[] args) {
    return c.execute_int(n, 0, args) + c.execute_int(n, 1, args)
        + c.execute_int(n, 2, args) + c.execute_int(n, 3, args);
  }

  public long execute_long(ProgramChromosome c, int n, Object[] args) {
    return c.execute_long(n, 0, args) + c.execute_long(n, 1, args)
        + c.execute_long(n, 2, args) + c.execute_long(n, 3, args);
  }

  public float execute_float(ProgramChromosome c, int n, Object[] args) {
    return c.execute_float(n, 0, args) + c.execute_float(n, 1, args)
        + c.execute_float(n, 2, args) + c.execute_float(n, 3, args);
  }

  public double execute_double(ProgramChromosome c, int n, Object[] args) {
    return c.execute_double(n, 0, args) + c.execute_double(n, 1, args)
        + c.execute_double(n, 2, args) + c.execute_double(n, 3, args);
  }

  public Object execute_object(ProgramChromosome c, int n, Object[] args) {
    return ( (Compatible) ( (Compatible)
                           ( (Compatible) (c.execute_object(n, 0, args))).
                           execute_add4(c.execute_object(n, 1, args))).
            execute_add4(c.execute_object(n, 2, args))).
        execute_add4(c.execute_object(n, 3, args));
  }

  protected interface Compatible {
    public Object execute_add4(Object o);
  }
  /**
   * Clones the object. Simple and straight forward implementation here.
   *
   * @return cloned instance of this object
   *
   * @author Klaus Meffert
   * @since 3.4
   */
  public Object clone() {
    try {
      Add4 result = new Add4(getGPConfiguration(), getReturnType());
      return result;
    } catch (Exception ex) {
      throw new CloneException(ex);
    }
  }
}
