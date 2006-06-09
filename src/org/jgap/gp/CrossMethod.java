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

/**
 * Abstract base class for GP-crossing over implementations.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public abstract class CrossMethod
    implements java.io.Serializable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  private transient GPConfiguration m_configuration;

  public CrossMethod(GPConfiguration a_configuration) {
    m_configuration = a_configuration;
  }

  public GPConfiguration getConfiguration() {
    return m_configuration;
  }

  public abstract ProgramChromosome[] operate(final ProgramChromosome i1,
                                            final ProgramChromosome i2);
}
