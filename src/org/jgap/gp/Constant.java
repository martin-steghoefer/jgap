/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp;

import org.jgap.*;
import org.jgap.gp.*;

/**
 * A constant remaining the same value all time.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class Constant
    extends MathCommand {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  private Object m_value;

  public Constant(final Configuration a_conf, Class a_type, Object a_value)
      throws InvalidConfigurationException {
    super(a_conf, 0, a_type);
    m_value = a_value;
  }

  protected Gene newGeneInternal() {
    try {
      Gene gene = new Constant(getConfiguration(), getReturnType(), m_value);
      return gene;
    }
    catch (InvalidConfigurationException iex) {
      throw new IllegalStateException(iex.getMessage());
    }
  }

  public void applyMutation(int index, double a_percentage) {
  }

  public String toString() {
    return "const(" + m_value + ")";
  }

  public int execute_int(ProgramChromosome c, int n, Object[] args) {
    return ((Integer)m_value).intValue();
  }

  public long execute_long(ProgramChromosome c, int n, Object[] args) {
    return ((Long)m_value).intValue();
  }

  public float execute_float(ProgramChromosome c, int n, Object[] args) {
    return ((Float)m_value).floatValue();
  }

  public double execute_double(ProgramChromosome c, int n, Object[] args) {
    return ((Double)m_value).doubleValue();
  }

  public Object execute_object(ProgramChromosome c, int n, Object[] args) {
    return m_value;
  }

  public static interface Compatible {
    public Object execute_constant();
  }
}
