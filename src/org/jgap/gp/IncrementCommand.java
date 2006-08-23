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
 * The increment-by-one operation.
 *
 * @author Konrad Odell
 * @author Klaus Meffert
 * @since 3.0
 */
public class IncrementCommand
    extends MathCommand {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.5 $";

  private int m_increment;

  public IncrementCommand(final Configuration a_conf, Class a_type)
      throws InvalidConfigurationException {
    this(a_conf, a_type, 1);
  }

  public IncrementCommand(final Configuration a_conf, Class a_type,
                          int a_increment)
      throws InvalidConfigurationException {
    super(a_conf, 1, a_type);
    m_increment = a_increment;
  }

  protected Gene newGeneInternal() {
    try {
      Gene gene = new IncrementCommand(getConfiguration(), getReturnType(),
                                       m_increment);
      return gene;
    }
    catch (InvalidConfigurationException iex) {
      throw new IllegalStateException(iex.getMessage());
    }
  }

  public String toString() {
    return "INC(" + m_increment + ", &1)";
  }

  public int execute_int(ProgramChromosome c, int n, Object[] args) {
    return c.execute_int(n, 0, args) + m_increment;
  }

  public long execute_long(ProgramChromosome c, int n, Object[] args) {
    return c.execute_long(n, 0, args) + m_increment;
  }

  public float execute_float(ProgramChromosome c, int n, Object[] args) {
    return c.execute_float(n, 0, args) + m_increment;
  }

  public double execute_double(ProgramChromosome c, int n, Object[] args) {
    return c.execute_double(n, 0, args) + m_increment;
  }

  public Object execute_object(ProgramChromosome c, int n, Object[] args) {
    return ( (Compatible) c.execute_object(n, 0, args)).execute_increment();
  }

  public static interface Compatible {
    public Object execute_increment();
  }
}
