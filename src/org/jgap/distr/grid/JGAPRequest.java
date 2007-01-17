/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.distr.grid;

import org.homedns.dade.jcgrid.*;
import org.jgap.*;
import org.jgap.util.*;

/**
 * An instance that creates single requests to be sent to a worker.
 *
 * @author Klaus Meffert
 * @since 3.1
 */
public class JGAPRequest
    extends WorkRequest
    implements ICloneable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.6 $";

  private Configuration m_config;

  private Population m_pop;

  private IEvolveStrategy m_evolveStrategy;

  private IWorkerReturnStrategy m_returnStrategy;

  private IGenotypeInitializer m_genotypeInitializer;

  /**
   * Constructor.
   *
   * @param name String
   * @param id int
   * @param a_config Configuration
   * @param a_strategy the strategy to choose for evolution
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public JGAPRequest(String name, int id, Configuration a_config,
                     IEvolveStrategy a_strategy) {
    super(name, id);
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
  public JGAPRequest(String name, int id, Configuration a_config) {
    this(name, id, a_config, new DefaultEvolveStrategy());
  }

  /**
   * Constructor. Allows to specify a preset population with which the genotype
   * will be initialized.
   *
   * @param name String
   * @param id int
   * @param a_config Configuration
   * @param a_pop Population
   * @param a_strategy the strategy to choose for evolution
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public JGAPRequest(String name, int id, Configuration a_config,
                     Population a_pop, IEvolveStrategy a_strategy) {
    this(name, id, a_config, a_strategy);
    m_pop = a_pop;
  }

  /**
   * Constructor. Allows to specify a preset population with which the genotype
   * will be initialized.
   *
   * @param name String
   * @param id int
   * @param a_config Configuration
   * @param a_pop Population
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public JGAPRequest(String name, int id, Configuration a_config,
                     Population a_pop) {
    this(name, id, a_config, a_pop, new DefaultEvolveStrategy());
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
  public void setEvolveStrategy(IEvolveStrategy a_evolveStrategy) {
    m_evolveStrategy = a_evolveStrategy;
  }

  /**
   * @return the evolve strategy set
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public IEvolveStrategy getEvolveStrategy() {
    return m_evolveStrategy;
  }

  public void setWorkerReturnStrategy(IWorkerReturnStrategy a_strategy) {
    m_returnStrategy = a_strategy;
  }

  /**
   * @return the strategy which part of a result is returned by a worker
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public IWorkerReturnStrategy getWorkerReturnStrategy() {
    return m_returnStrategy;
  }

  /**
   * @param a_initializer the IGenotypeInitializer to use
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void setGenotypeInitializer(IGenotypeInitializer a_initializer) {
    m_genotypeInitializer = a_initializer;
  }


  /**
   * @return the IGenotypeInitializer set
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public IGenotypeInitializer getGenotypeInitializer() {
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
  public void setPopulation(Population a_pop) {
    m_pop = a_pop;
  }


  /**
   * @return the configuration set
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public Configuration getConfiguration() {
    return m_config;
  }

  /**
   * Set a modified configuration. Should only be used to re-set a configuration
   * because some parts have not been serialized.
   * @param a_conf the Configuration to set
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void setConfiguration(Configuration a_conf) {
    m_config = a_conf;
  }

  /**
   * @return the population used to initialize new requests. May be null or
   * empty
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public Population getPopulation() {
    return m_pop;
  }

  /**
   * @return deep clone of current instance
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public Object clone() {
    JGAPRequest result = newInstance(getSessionName(), getRID());
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
  public JGAPRequest newInstance(String a_name, int a_ID) {
    JGAPRequest result = new JGAPRequest(a_name, a_ID,
                                       getConfiguration(), getPopulation());
    result.setEvolveStrategy(getEvolveStrategy());
    result.setGenotypeInitializer(getGenotypeInitializer());
    result.setWorkerReturnStrategy(getWorkerReturnStrategy());
    return result;
  }
}
