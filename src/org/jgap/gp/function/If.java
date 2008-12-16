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
 * The if-then construct.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class If
    extends CommandGene
implements ICloneable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.6 $";

  public If(final GPConfiguration a_conf, Class a_type)
      throws InvalidConfigurationException {
    super(a_conf, 2, a_type);
  }

  public String toString() {
    return "if(&1) then (&2)";
  }

  public boolean execute_boolean(ProgramChromosome c, int n, Object[] args) {
    boolean x = c.execute_boolean(n, 0, args);
    boolean value = false;
    if (x) {
      value = c.execute_boolean(n, 1, args);
    }
    return value;
  }

  public int execute_int(ProgramChromosome c, int n, Object[] args) {
    int x = c.execute_int(n, 0, args);
    int value = 0;
    if (x >= 0) {
      value = c.execute_int(n, 1, args);
    }
    return value;
  }

  public long execute_long(ProgramChromosome c, int n, Object[] args) {
    long x = c.execute_long(n, 0, args);
    long value = 0;
    if (x >= 0) {
      value = c.execute_long(n, 1, args);
    }
    return value;
  }

  public float execute_float(ProgramChromosome c, int n, Object[] args) {
    float x = c.execute_float(n, 0, args);
    float value = 0;
    if (x >= 0) {
      value = c.execute_float(n, 1, args);
    }
    return value;
  }

  public double execute_double(ProgramChromosome c, int n, Object[] args) {
    double x = c.execute_double(n, 0, args);
    double value = 0;
    if (x >= 0) {
      value = c.execute_double(n, 1, args);
    }
    return value;
  }

  public void execute_void(ProgramChromosome c, int n, Object[] args) {
    int x = c.execute_int(n, 0, args);/**@todo add option for type of first child to constructor*/
    if (x >= 0) {
      c.execute_void(n, 1, args);
    }
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
      If result = new If(getGPConfiguration(), getReturnType());
      return result;
    } catch (Exception ex) {
      throw new CloneException(ex);
    }
  }
}
