/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.distr.grid.gp;

import org.homedns.dade.jcgrid.client.*;
import org.jgap.gp.impl.*;
import org.jgap.gp.*;
import org.jgap.distr.grid.common.*;

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
  private final static String CVS_REVISION = "$Revision: 1.6 $";

  private IClientFeedbackGP m_clientFeedback;

  private IRequestSplitStrategyGP m_splitStrategy;

  private GPConfiguration m_config;

  private BasicContext m_context;

  private IClientEvolveStrategyGP m_clientEvolveStrategy;

  private IWorkerEvolveStrategyGP m_workerEvolveStrategy;

  private IWorkerReturnStrategyGP m_workerReturnStrategy;

  private IGenotypeInitializerGP m_genotypeInitializer;

  private String m_packageName;

  private Class[] m_types;

  private Class[][] m_argTypes;

  private CommandGene[][] m_nodeSets;

  private int[] m_minDepths;

  private int[] m_maxDepths;

  private int m_maxNodes;

  private double m_minFitnessToStore;

  public GridConfigurationGPBase() {
//    m_packageName = getClass().getPackage().getName();
    m_context = new BasicContext();
  }

  public void setContext(BasicContext a_context) {
    m_context = a_context;
  }

  public BasicContext getContext() {
    return m_context;
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

  public void setTypes(Class[] a_types) {
    m_types = a_types;
  }

  public void setArgTypes(Class[][] a_argTypes) {
    m_argTypes = a_argTypes;
  }

  public void setNodeSets(CommandGene[][] a_nodeSets) {
    m_nodeSets = a_nodeSets;
  }

  public void setMinDepths(int[] a_minDepths) {
    m_minDepths = a_minDepths;
  }

  public void setMaxDepths(int[] a_maxDepths) {
    m_maxDepths = a_maxDepths;
  }

  public void setMaxNodes(int a_maxNodes) {
    m_maxNodes = a_maxNodes;
  }

  public Class[] getTypes() {
    return m_types;
  }

  public Class[][] getArgTypes() {
    return m_argTypes;
  }

  public CommandGene[][] getNodeSets() {
    return m_nodeSets;
  }

  public int[] getMinDepths() {
    return m_minDepths;
  }

  public int[] getMaxDepths() {
    return m_maxDepths;
  }

  public int getMaxNodes() {
    return m_maxNodes;
  }

  public double getMinFitnessToStore() {
    return m_minFitnessToStore;
  }

  public void setMinFitnessToStore(double a_minFitnessToStore) {
    m_minFitnessToStore = a_minFitnessToStore;
  }
}
