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

import org.jgap.gp.*;
import org.jgap.*;
import org.jgap.gp.impl.*;

/**
 * An argument that will be used internally only by ADF's.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class Argument
    extends CommandGene {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.9 $";

  private int m_index;

  public Argument(final GPConfiguration a_conf, int a_index, Class type)
      throws InvalidConfigurationException {
    super(a_conf, 0, type);
    m_index = a_index;
  }

  public String toString() {
    return "Arg(" + m_index + ")";
  }

  /**
   * @return textual name of this command
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public String getName() {
    return "ADF Argument";
  }

  public boolean execute_boolean(ProgramChromosome c, int n, Object[] args) {
    return ( (Boolean) args[m_index]).booleanValue();
  }

  public int execute_int(ProgramChromosome c, int n, Object[] args) {
    return ( (Integer) args[m_index]).intValue();
  }

  public long execute_long(ProgramChromosome c, int n, Object[] args) {
    return ( (Long) args[m_index]).longValue();
  }

  public float execute_float(ProgramChromosome c, int n, Object[] args) {
    return ( (Float) args[m_index]).floatValue();
  }

  public double execute_double(ProgramChromosome c, int n, Object[] args) {
    return ( (Double) args[m_index]).doubleValue();
  }

  public Object execute_object(ProgramChromosome c, int n, Object[] args) {
    return args[m_index];
  }

  public Class getChildType(IGPProgram a_ind, int a_chromNum) {
    return null;
  }
}
