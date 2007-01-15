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

/**
 * An instance that creates single requests to be sent to a worker.
 *
 * @author Klaus Meffert
 * @since 3.1
 */
public abstract class JGAPRequest
    extends WorkRequest {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  private Configuration m_config;

  private Population m_pop;

  private IEvolveStrategy m_evolveStrategy;

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
   * is initialized.
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
   * is initialized.
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
   * Creates single requests to be sent to workers.
   *
   * @return single requests to be computed by workers
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public abstract JGAPRequest[] split()
      throws Exception;

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
}
