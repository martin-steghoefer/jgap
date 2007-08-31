/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.distr.grid;

import org.jgap.*;
import org.homedns.dade.jcgrid.client.*;

/**
 * Abstract base class for the important grid configuration. It holds any
 * information necessary to describe a problem and the way it is solved
 * distributedly.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public abstract class GridConfigurationBase
    implements IGridConfiguration {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  private IClientFeedback m_clientFeedback;

  private IRequestSplitStrategy m_splitStrategy;

  private Configuration m_config;

  private IClientEvolveStrategy m_clientEvolveStrategy;

  private IWorkerEvolveStrategy m_workerEvolveStrategy;

  private IWorkerReturnStrategy m_workerReturnStrategy;

  private IGenotypeInitializer m_genotypeInitializer;
  private String m_packageName;

  public GridConfigurationBase() {
//    m_packageName = getClass().getPackage().getName();
  }

  public String getPackageName() {
    return m_packageName;
  }

  public IClientFeedback getClientFeedback() {
    return m_clientFeedback;
  }

  public IClientEvolveStrategy getClientEvolveStrategy() {
    return m_clientEvolveStrategy;
  }

  public IRequestSplitStrategy getRequestSplitStrategy() {
    return m_splitStrategy;
  }

  public Configuration getConfiguration() {
    return m_config;
  }

  public void setConfiguration(Configuration a_config) {
    m_config = a_config;
  }

  public IWorkerEvolveStrategy getWorkerEvolveStrategy() {
    return m_workerEvolveStrategy;
  }

  public IWorkerReturnStrategy getWorkerReturnStrategy() {
    return m_workerReturnStrategy;
  }

  public IGenotypeInitializer getGenotypeInitializer() {
    return m_genotypeInitializer;
  }

  public void setGenotypeInitializer(IGenotypeInitializer a_initializer) {
    m_genotypeInitializer = a_initializer;
  }

  public void setWorkerReturnStrategy(IWorkerReturnStrategy a_strategy) {
    m_workerReturnStrategy = a_strategy;
  }

  public void setWorkerEvolveStrategy(IWorkerEvolveStrategy a_strategy) {
    m_workerEvolveStrategy = a_strategy;
  }

  /**
   * Write your initialization of the private attributes here!
   *
   * @param a_gridconfig current grid node client configuration (provided via
   * the command line at startup)
   * @throws Exception in case of any error
   */
  public abstract void initialize(GridNodeClientConfig a_gridconfig)
      throws Exception;

  /**
   * Called immediately before starting the grid computation. Verify here,
   * if your configuration is setup properly and all fields are initialized
   * correctly.
   *
   * @throws Exception
   */
  public abstract void validate()
      throws Exception;

  public void setClientEvolveStrategy(IClientEvolveStrategy
                                      a_strategy) {
    m_clientEvolveStrategy = a_strategy;
  }

  public void setClientFeedback(IClientFeedback a_clientFeedback) {
    m_clientFeedback = a_clientFeedback;
  }

  public void setRequestSplitStrategy(IRequestSplitStrategy a_splitStrategy) {
    m_splitStrategy = a_splitStrategy;
  }
}
