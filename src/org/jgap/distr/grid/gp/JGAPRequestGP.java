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

import org.homedns.dade.jcgrid.*;
import org.jgap.*;
import org.jgap.gp.impl.*;
import org.jgap.util.*;
import org.homedns.dade.jcgrid.worker.*;
import org.jgap.distr.grid.*;

/**
 * An instance that creates single requests to be sent to a worker.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public class JGAPRequestGP
    extends WorkRequest
    implements ICloneable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  private GPConfiguration m_config;

  private GPPopulation m_pop;

  private IWorkerEvolveStrategyGP m_evolveStrategy;

  private IWorkerReturnStrategyGP m_returnStrategy;

  private IGenotypeInitializerGP m_genotypeInitializer;

  private GridWorkerFeedback m_workerFeedback;

  /**
   * Constructor.
   *
   * @param a_name String
   * @param a_id int
   * @param a_config Configuration
   * @param a_strategy the strategy to choose for evolution
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public JGAPRequestGP(String a_name, int a_id, GPConfiguration a_config,
                     IWorkerEvolveStrategyGP a_strategy) {
    super(a_name, a_id);
    m_config = a_config;
    m_evolveStrategy = a_strategy;
  }

  /**
   * Constructor.
   *
   * @param name String
   * @param id int
   * @param a_config Configuration
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public JGAPRequestGP(String name, int id, GPConfiguration a_config) {
    this(name, id, a_config, new DefaultEvolveStrategyGP());
  }

  /**
   * Constructor. Allows to specify a preset population with which the genotype
   * will be initialized.
   *
   * @param a_name String
   * @param a_id int
   * @param a_config Configuration
   * @param a_pop Population
   * @param a_strategy the strategy to choose for evolution
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public JGAPRequestGP(String a_name, int a_id, GPConfiguration a_config,
                     GPPopulation a_pop, IWorkerEvolveStrategyGP a_strategy) {
    this(a_name, a_id, a_config, a_strategy);
    m_pop = a_pop;
  }

  /**
   * Constructor. Allows to specify a preset population with which the genotype
   * will be initialized.
   *
   * @param a_name String
   * @param a_id int
   * @param a_config Configuration
   * @param a_pop Population
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public JGAPRequestGP(String a_name, int a_id, GPConfiguration a_config,
                     GPPopulation a_pop) {
    this(a_name, a_id, a_config, a_pop, new DefaultEvolveStrategyGP());
  }

  /**
   * Sets the strategy to use for executing the evolution with a worker for
   * a single request.
   *
   * @param a_evolveStrategy the evolve strategy to use
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void setEvolveStrategy(IWorkerEvolveStrategyGP a_evolveStrategy) {
    m_evolveStrategy = a_evolveStrategy;
  }

  /**
   * @return the evolve strategy set
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public IWorkerEvolveStrategyGP getWorkerEvolveStrategy() {
    return m_evolveStrategy;
  }

  public void setWorkerReturnStrategy(IWorkerReturnStrategyGP a_strategy) {
    m_returnStrategy = a_strategy;
  }

  /**
   * @return the strategy which part of a result is returned by a worker
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public IWorkerReturnStrategyGP getWorkerReturnStrategy() {
    return m_returnStrategy;
  }

  public GridWorkerFeedback getWorkerFeedback() {
   return m_workerFeedback;
  }

  public void setWorkerFeedback(GridWorkerFeedback a_feedback) {
   m_workerFeedback = a_feedback;
  }

  /**
   * @param a_initializer the IGenotypeInitializer to use
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void setGenotypeInitializer(IGenotypeInitializerGP a_initializer) {
    m_genotypeInitializer = a_initializer;
  }


  /**
   * @return the IGenotypeInitializer set
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public IGenotypeInitializerGP getGenotypeInitializer() {
    return m_genotypeInitializer;
  }

  /**
   * Sets the Population to store in this request so that it can be passed to
   * workers.
   *
   * @param a_pop the Population to store
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void setPopulation(GPPopulation a_pop) {
    m_pop = a_pop;
  }


  /**
   * @return the configuration set
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public GPConfiguration getConfiguration() {
    return m_config;
  }

  /**
   * Set a modified configuration. Should only be used to re-set a configuration
   * because some parts have not been serialized.
   *
   * @param a_conf the Configuration to set
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void setConfiguration(GPConfiguration a_conf) {
    m_config = a_conf;
  }

  /**
   * @return the population used to initialize new requests. May be null or
   * empty
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public GPPopulation getPopulation() {
    return m_pop;
  }

  /**
   * @return deep clone of current instance
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public Object clone() {
    JGAPRequestGP result = newInstance(getSessionName(), getRID());
    return result;
  }

  /**
   * Creates a new instance using the given name and ID. Reason for this method:
   * ID cannot be set other than with construction!
   *
   * @param a_name the name to set
   * @param a_ID unique ID to set
   * @return newly created JGAPRequest object
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public JGAPRequestGP newInstance(String a_name, int a_ID) {
    JGAPRequestGP result = new JGAPRequestGP(a_name, a_ID,
                                       getConfiguration(), getPopulation());
    result.setEvolveStrategy(getWorkerEvolveStrategy());
    result.setGenotypeInitializer(getGenotypeInitializer());
    result.setWorkerReturnStrategy(getWorkerReturnStrategy());
    return result;
  }
}
