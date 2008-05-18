/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp;

import java.io.*;
import org.jgap.gp.impl.*;

/**
 * Abstract base class for GP-crossing over implementations.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public abstract class CrossMethod
    implements Serializable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.8 $";

  private /*transient*/ GPConfiguration m_configuration;

  public CrossMethod(GPConfiguration a_configuration) {
    m_configuration = a_configuration;
  }

  public GPConfiguration getConfiguration() {
    return m_configuration;
  }

  /**
   * Crosses two individuals.
   *
   * @param a_i1 the first individual to cross
   * @param a_i2 the second individual to cross
   * @return an array of the two resulting individuals
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public abstract IGPProgram[] operate(final IGPProgram a_i1,
                                       final IGPProgram a_i2);
}
