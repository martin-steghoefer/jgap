/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp.terminal;

import org.jgap.*;
import org.jgap.gp.*;
import org.jgap.gp.impl.*;
import org.jgap.util.*;

/**
 * A constant remaining the same value all time.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class Constant
    extends MathCommand implements ICloneable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.8 $";

  private Object m_value;

  public Constant(final GPConfiguration a_conf, Class a_type, Object a_value)
      throws InvalidConfigurationException {
    this(a_conf, a_type, a_value, 0);
  }

  public Constant(final GPConfiguration a_conf, Class a_type, Object a_value,
                  int a_subReturnType)
      throws InvalidConfigurationException {
    super(a_conf, 0, a_type, a_subReturnType, null);
    m_value = a_value;
  }

  public String toString() {
    return m_value.toString();
  }

  /**
   * @return textual name of this command
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public String getName() {
    return "Constant";
  }

  public int execute_int(ProgramChromosome c, int n, Object[] args) {
    return ( (Integer) m_value).intValue();
  }

  public long execute_long(ProgramChromosome c, int n, Object[] args) {
    return ( (Long) m_value).intValue();
  }

  public float execute_float(ProgramChromosome c, int n, Object[] args) {
    return ( (Float) m_value).floatValue();
  }

  public double execute_double(ProgramChromosome c, int n, Object[] args) {
    return ( (Double) m_value).doubleValue();
  }

  public Object execute_object(ProgramChromosome c, int n, Object[] args) {
    return m_value;
  }

  /**
   * Clones the object. Simple and straight forward implementation here.
   *
   * @return cloned instance of this object
   *
   * @author Klaus Meffert
   * @since 3.6
   */
  public Object clone() {
    try {
      Constant result = new Constant(getGPConfiguration(), getReturnType(),
                                     m_value, getSubReturnType());
      return result;
    } catch (Throwable t) {
      throw new CloneException(t);
    }
  }
}
