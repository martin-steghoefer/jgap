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
 * Pops a value from the stack after it has been pushed onto it (PushCommand).
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class Pop
    extends MathCommand implements ICloneable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.9 $";

  public Pop(final GPConfiguration a_conf, Class a_returnType)
      throws InvalidConfigurationException {
    this(a_conf, a_returnType, 0);
  }

  public Pop(final GPConfiguration a_conf, Class a_returnType,
             int a_subReturnType)
      throws InvalidConfigurationException {
    super(a_conf, 0, a_returnType, a_subReturnType, null);
  }

  public String toString() {
    return "pop &1";
  }

  /**
   * @return textual name of this command
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public String getName() {
    return "Pop";
  }

  public int execute_int(ProgramChromosome c, int n, Object[] args) {
    check(c);
    // Pop from stack.
    // ---------------
    if (getGPConfiguration().stackSize() < 1) {
      throw new IllegalStateException("pop without push");
    }
    return ( (Integer) getGPConfiguration().popFromStack()).
        intValue();
  }

  public long execute_long(ProgramChromosome c, int n, Object[] args) {
    check(c);
    if (getGPConfiguration().stackSize() < 1) {
      throw new IllegalStateException("pop without push");
    }
    return ( (Long) getGPConfiguration().popFromStack()).longValue();
  }

  public double execute_double(ProgramChromosome c, int n, Object[] args) {
    check(c);
    if (getGPConfiguration().stackSize() < 1) {
      throw new IllegalStateException("pop without push");
    }
    return ( (Double) getGPConfiguration().popFromStack()).doubleValue();
  }

  public float execute_float(ProgramChromosome c, int n, Object[] args) {
    check(c);
    if (getGPConfiguration().stackSize() < 1) {
      throw new IllegalStateException("pop without push");
    }
    return ( (Float) getGPConfiguration().popFromStack()).floatValue();
  }

  public Object execute_object(ProgramChromosome c, int n, Object[] args) {
    check(c);
    if (getGPConfiguration().stackSize() < 1) {
      throw new IllegalStateException("pop without push");
    }
    return getGPConfiguration().popFromStack();
  }

  public boolean isValid(ProgramChromosome a_program) {
    return a_program.getCommandOfClass(0, Push.class) >= 0;
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
      Pop result = new Pop(getGPConfiguration(), getReturnType(),
                           getSubReturnType());
      return result;
    } catch (Exception ex) {
      throw new CloneException(ex);
    }
  }
}
