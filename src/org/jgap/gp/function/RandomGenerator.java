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
 * Returns a double/float value with a positive sign, greater than or equal to
 * 0.0 and less than 1.0.
 *
 * @author Klaus Meffert
 * @since 3.3.4
 */
public class RandomGenerator
    extends MathCommand implements ICloneable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  public RandomGenerator(final GPConfiguration a_conf, Class a_returnType)
      throws InvalidConfigurationException {
    super(a_conf, 0, a_returnType);
  }

  public String toString() {
    return "random";
  }

  /**
   * @return textual name of this command
   *
   * @author Klaus Meffert
   * @since 3.3.4
   */
  public String getName() {
    return "Random";
  }

  public float execute_float(ProgramChromosome c, int n, Object[] args) {
    return (float) Math.random();
  }

  public double execute_double(ProgramChromosome c, int n, Object[] args) {
    return Math.random();
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
      RandomGenerator result = new RandomGenerator(getGPConfiguration(),
          getReturnType());
      return result;
    } catch (Exception ex) {
      throw new CloneException(ex);
    }
  }
}
