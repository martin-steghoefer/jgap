/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.distr.grid.gp;

import org.homedns.dade.jcgrid.client.*;
import org.jgap.gp.impl.*;

/**
 * Abstract base class for the important GP grid configuration. It holds any
 * information necessary to describe a problem and the way it is solved
 * distributedly.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public abstract class GridConfigurationGPBase
    implements IGridConfigurationGP {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  private IClientFeedbackGP m_clientFeedback;

  private IRequestSplitStrategyGP m_splitStrategy;

  private GPConfiguration m_config;

  private IClientEvolveStrategyGP m_clientEvolveStrategy;

  private IWorkerEvolveStrategyGP m_workerEvolveStrategy;

  private IWorkerReturnStrategyGP m_workerReturnStrategy;

  private IGenotypeInitializerGP m_genotypeInitializer;
  private String m_packageName;

  public GridConfigurationGPBase() {
//    m_packageName = getClass().getPackage().getName();
  }

  public String getPackageName() {
    return m_packageName;
  }

  public IClientFeedbackGP getClientFeedback() {
    return m_clientFeedback;
  }

  public IClientEvolveStrategyGP getClientEvolveStrategy() {
    return m_clientEvolveStrategy;
  }

  public IRequestSplitStrategyGP getRequestSplitStrategy() {
    return m_splitStrategy;
  }

  public GPConfiguration getConfiguration() {
    return m_config;
  }

  public void setConfiguration(GPConfiguration a_config) {
    m_config = a_config;
  }

  public IWorkerEvolveStrategyGP getWorkerEvolveStrategy() {
    return m_workerEvolveStrategy;
  }

  public IWorkerReturnStrategyGP getWorkerReturnStrategy() {
    return m_workerReturnStrategy;
  }

  public IGenotypeInitializerGP getGenotypeInitializer() {
    return m_genotypeInitializer;
  }

  public void setGenotypeInitializer(IGenotypeInitializerGP a_initializer) {
    m_genotypeInitializer = a_initializer;
  }

  public void setWorkerReturnStrategy(IWorkerReturnStrategyGP a_strategy) {
    m_workerReturnStrategy = a_strategy;
  }

  public void setWorkerEvolveStrategy(IWorkerEvolveStrategyGP a_strategy) {
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

  public void setClientEvolveStrategy(IClientEvolveStrategyGP
                                      a_strategy) {
    m_clientEvolveStrategy = a_strategy;
  }

  public void setClientFeedback(IClientFeedbackGP a_clientFeedback) {
    m_clientFeedback = a_clientFeedback;
  }

  public void setRequestSplitStrategy(IRequestSplitStrategyGP a_splitStrategy) {
    m_splitStrategy = a_splitStrategy;
  }
}
