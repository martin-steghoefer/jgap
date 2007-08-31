/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.impl.job;

import org.jgap.*;

/**
 * Result of an evolution.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public class EvolveResult
    extends JobResult {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  private Population m_pop;

  public EvolveResult() {
  }

  public Population getPopulation() {
    return m_pop;
  }

  public void setPopulation(Population a_pop) {
    m_pop = a_pop;
  }
}
