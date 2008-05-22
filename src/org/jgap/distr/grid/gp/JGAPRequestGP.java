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

import java.util.*;

import org.homedns.dade.jcgrid.*;
import org.homedns.dade.jcgrid.worker.*;
import org.jgap.distr.*;
import org.jgap.gp.impl.*;
import org.jgap.util.*;

/**
 * An instance that defines a request from which work packages are generated
 * that are sent to workers in the grid.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public class JGAPRequestGP
    extends WorkRequest implements ICloneable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.8 $";

  private IGridConfigurationGP m_config;

  private GPPopulation m_pop;

  private IWorkerEvolveStrategyGP m_evolveStrategy;

  private IWorkerReturnStrategyGP m_returnStrategy;

  private IGenotypeInitializerGP m_genotypeInitializer;

  private GridWorkerFeedback m_workerFeedback;

  private int m_chunk;

  private MasterInfo m_requesterInfo;

  private String m_id;

  private Date m_requestDate;

  private String m_description;

  private double m_minFitness;

  private String m_title;

  private Object m_genericData;

  /**
   * Constructor.
   *
   * @param a_name textual description of request
   * @param a_id unique identification of request
   * @param a_chunk running index of request chunk, should be unique within an
   * identification
   * @param a_config Configuration
   * @param a_strategy the strategy to choose for evolution
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public JGAPRequestGP(String a_name, String a_id, int a_chunk,
                       IGridConfigurationGP a_config,
                       IWorkerEvolveStrategyGP a_strategy) {
    super(a_name, 0);
    m_config = a_config;
    m_evolveStrategy = a_strategy;
    m_chunk = a_chunk;
    m_id = a_id;
  }

  /**
   * Constructor.
   *
   * @param name String
   * @param a_id unique identification of request
   * @param a_chunk running index of request chunk, should be unique within an
   * identification
   * @param a_config Configuration
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public JGAPRequestGP(String name, String a_id, int a_chunk,
                       IGridConfigurationGP a_config) {
    this(name, a_id, a_chunk, a_config, new DefaultEvolveStrategyGP());
  }

  /**
   * Constructor. Allows to specify a preset population with which the genotype
   * will be initialized.
   *
   * @param a_name textual description of request
   * @param a_id unique identification of request
   * @param a_chunk running index of request chunk, should be unique within an
   * identification
   * @param a_config Configuration
   * @param a_pop Population
   * @param a_strategy the strategy to choose for evolution
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public JGAPRequestGP(String a_name, String a_id, int a_chunk,
                       IGridConfigurationGP a_config,
                       GPPopulation a_pop, IWorkerEvolveStrategyGP a_strategy) {
    this(a_name, a_id, a_chunk, a_config, a_strategy);
    m_pop = a_pop;
  }

  /**
   * Constructor. Allows to specify a preset population with which the genotype
   * will be initialized.
   *
   * @param a_name textual description of request
   * @param a_id unique identification of request
   * @param a_chunk running index of request chunk, should be unique within an
   * identification
   * @param a_config Configuration
   * @param a_pop Population
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public JGAPRequestGP(String a_name, String a_id, int a_chunk,
                       IGridConfigurationGP a_config,
                       GPPopulation a_pop) {
    this(a_name, a_id, a_chunk, a_config, a_pop, new DefaultEvolveStrategyGP());
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
   * @return the JGAP configuration set
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public GPConfiguration getConfiguration() {
    return m_config.getConfiguration();
  }

  /**
   * @return the grid configuration set
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public IGridConfigurationGP getGridConfiguration() {
    return m_config;
  }

  /**
   * Set a modified JGAP configuration. Should only be used to re-set a
   * configuration because some parts have not been serialized.
   *
   * @param a_conf the JGAP Configuration to set
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void setConfiguration(GPConfiguration a_conf) {
    m_config.setConfiguration(a_conf);
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
    JGAPRequestGP result = newInstance(getSessionName(), getID(), getChunk());
    return result;
  }

  /**
   * Creates a new instance using the given name and ID. Reason for this method:
   * ID cannot be set other than with construction!
   *
   * @param a_name textual description of request
   * @param a_id unique identification of request
   * @param a_chunk running index of request chunk, should be unique within an
   * identification
   * @return newly created JGAPRequest object
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public JGAPRequestGP newInstance(String a_name, String a_id, int a_chunk) {
    JGAPRequestGP result = new JGAPRequestGP(a_name, a_id, a_chunk,
        m_config, getPopulation());
    result.setEvolveStrategy(getWorkerEvolveStrategy());
    result.setGenotypeInitializer(getGenotypeInitializer());
    result.setWorkerReturnStrategy(getWorkerReturnStrategy());
    result.setRequesterInfo(getRequesterInfo());
    result.setDescription(getDescription());
    result.setRequestDate(getRequestDate());
    result.setMinFitness(getMinFitness());
    result.setTitle(getTitle());
    return result;
  }

  public int getChunk() {
    return m_chunk;
  }

  /**
   * @return information about the requester
   *
   * @author Klaus Meffert
   * @since 3.3.3
   */
  public MasterInfo getRequesterInfo() {
    return m_requesterInfo;
  }

  /**
   *
   * @param a_requesterInfo set information about the requester
   *
   * @author Klaus Meffert
   * @since 3.3.3
   */
  public void setRequesterInfo(MasterInfo a_requesterInfo) {
    m_requesterInfo = a_requesterInfo;
  }

  public String getID() {
    return m_id;
  }

  /**
   * @param a_date date the request was initialized
   *
   * @author Klaus Meffert
   * @since 3.3.3
   */
  public void setRequestDate(Date a_date) {
    m_requestDate = a_date;
  }

  /**
   * @return the date the request was initialized
   *
   * @author Klaus Meffert
   * @since 3.3.3
   */
  public Date getRequestDate() {
    return m_requestDate;
  }

  /**
   * @param a_descr arbitrary description of the request
   *
   * @author Klaus Meffert
   * @since 3.3.3
   */
  public void setDescription(String a_descr) {
    m_description = a_descr;
  }

  /**
   * @return arbitrary description of the request
   *
   * @author Klaus Meffert
   * @since 3.3.3
   */
  public String getDescription() {
    return m_description;
  }

  /**
   * @return the minimum fitness wanted for results to be returned as response
   * to this request
   *
   * @author Klaus Meffert
   * @since 3.3.3
   */
  public double getMinFitness() {
    return m_minFitness;
  }

  /**
   * @param a_minFitness the minimum fitness wanted for results to be returned as
   * response to this request
   *
   * @author Klaus Meffert
   * @since 3.3.3
   */
  public void setMinFitness(double a_minFitness) {
    m_minFitness = a_minFitness;
  }

  /**
   * @param a_title the title to set
   *
   * @author Klaus Meffert
   * @since 3.3.4
   */
  public void setTitle(String a_title) {
    m_title = a_title;
  }

  /**
   * @return the title set
   *
   * @author Klaus Meffert
   * @since 3.3.4
   */
  public String getTitle() {
    return m_title;
  }
}
