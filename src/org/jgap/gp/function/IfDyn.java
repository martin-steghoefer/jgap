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
 * The if-then construct with a dynamic number of children.
 *
 * @author Klaus Meffert
 * @since 3.4
 */
public class IfDyn
    extends CommandDynamicArity implements ICloneable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  public IfDyn(final GPConfiguration a_conf, Class a_returnType,
               int a_arityInitial, int a_arityMin, int a_arityMax)
      throws InvalidConfigurationException {
    super(a_conf, a_arityInitial + 1, a_arityMin + 1, a_arityMax + 1,
          a_returnType);
  }

  public String toString() {
    String s = "if(&1) then (";
    int size = size();
    for (int i = 0; i < size; i++) {
      s += "&" + (i + 2);
      if (i < size - 1) {
        s += ";";
      }
    }
    return s + ")";
  }

  public boolean execute_boolean(ProgramChromosome c, int n, Object[] args) {
    boolean x = c.execute_boolean(n, 0, args);
    boolean value = false;
    if (x) {
      int size = size();
      for (int i = 0; i < size; i++) {
        value = c.execute_boolean(n, i + 1, args);
        if (!value) {
          x = false;
        }
      }
    }
    return value;
  }

  public void execute_void(ProgramChromosome c, int n, Object[] args) {
//    System.out.println("Arity: " + getArity(null));
    int x = c.execute_int(n, 0, args);
    /**@todo add option for type of first child to constructor*/
    if (x >= 0) {
      int size = size();
      for (int i = 0; i < size; i++) {
        c.execute_void(n, i + 1, args);
      }
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
      IfDyn result = new IfDyn(getGPConfiguration(), getReturnType(), getArity(null),
                               getArityMin(), getArityMax());
      return result;
    } catch (Exception ex) {
      throw new CloneException(ex);
    }
  }
}
