/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.impl.job;

import org.jgap.*;
/**
 * Data needed by a IEvolveJob implementation to evolve.
 *
 * @author Klaus Meffert
 * @since 3.2
 */

public class EvolveData {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  private Population m_pop;
  private Configuration m_config;

  public EvolveData() {

  }

  public Configuration getConfiguration() {
    return m_config;
  }

  public Population getPopulation() {
    return m_pop;
  }

  public void setConfiguration(Configuration m_config) {
    this.m_config = m_config;
  }

  public void setPopulation(Population m_pop) {
    this.m_pop = m_pop;
  }
}
