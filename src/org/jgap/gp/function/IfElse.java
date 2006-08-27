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

/**
 * The if-then-else construct.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class IfElse
    extends CommandGene {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  public IfElse(final GPConfiguration a_conf, Class type)
      throws InvalidConfigurationException {
    super(a_conf, 3, type);
  }

  protected Gene newGeneInternal() {
    return null;
  }

  public String toString() {
    return "if(&1) then (&2) else(&3)";
  }

  public int execute_int(ProgramChromosome c, int n, Object[] args) {
    int x = c.execute_int(n, 0, args);
    int value = 0;
    if (x >= 0) {
      value = c.execute_int(n, 1, args);
    }
    else {
      value = c.execute_int(n, 2, args);
    }
    return value;
  }

  public long execute_long(ProgramChromosome c, int n, Object[] args) {
    long x = c.execute_long(n, 0, args);
    long value = 0;
    if (x >= 0) {
      value = c.execute_long(n, 1, args);
    }
    else {
      value = c.execute_long(n, 2, args);
    }
    return value;
  }

  public float execute_float(ProgramChromosome c, int n, Object[] args) {
    float x = c.execute_float(n, 0, args);
    float value = 0;
    if (x >= 0) {
      value = c.execute_float(n, 1, args);
    }
    else {
      value = c.execute_float(n, 2, args);
    }
    return value;
  }

  public double execute_double(ProgramChromosome c, int n, Object[] args) {
    double x = c.execute_double(n, 0, args);
    double value = 0;
    if (x >= 0) {
      value = c.execute_double(n, 1, args);
    }
    else {
      value = c.execute_double(n, 2, args);
    }
    return value;
  }
}
