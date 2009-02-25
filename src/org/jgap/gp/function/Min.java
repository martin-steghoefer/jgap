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
 * Returns the smaller of two values.
 *
 * @author Klaus Meffert
 * @since 3.3.4
 */
public class Min
    extends MathCommand implements ICloneable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.4 $";

  public Min(final GPConfiguration a_conf, Class a_returnType)
      throws InvalidConfigurationException {
    super(a_conf, 2, a_returnType);
  }

  public String toString() {
    return "min(&1, &2)";
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
      Min result = new Min(getGPConfiguration(), getReturnType());
      return result;
    } catch (Exception ex) {
      throw new CloneException(ex);
    }
  }
}
