/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap;

import org.jgap.audit.*;

/**
 * Base class for any implementation of interface GeneticOperator.
 * See MutationOperator or CrossoverOperator for examples.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public abstract class BaseGeneticOperator
    implements GeneticOperator, Comparable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.7 $";

  private Configuration m_configuration;

  protected IEvolutionMonitor m_monitor;

  protected boolean m_monitorActive;

  /**
   * The only constructor in this class. Sets the immutable configuration.
   * @param a_configuration the configuration to set (must not be null)
   * @throws InvalidConfigurationException
   * @author Klaus Meffert
   * @since 3.0
   */
  public BaseGeneticOperator(Configuration a_configuration)
      throws InvalidConfigurationException {
    if (a_configuration == null) {
      throw new InvalidConfigurationException("Configuration must not be null");
    }
    m_configuration = a_configuration;
    // Monitoring stuff:
    IEvolutionMonitor m_monitor = getConfiguration().getMonitor();
    boolean m_monitorActive = m_monitor != null;
  }

  /**
   * @todo make returned object immutable
   * @return the configuration set
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public Configuration getConfiguration() {
    return m_configuration;
  }

  /**
   * Compares this GeneticOperator against the specified object. The result is
   * true if the argument is an instance of this class and is equal with respect
   * to the data.
   *
   * @param a_other the object to compare against
   * @return true: if the objects are the same, false otherwise
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public boolean equals(final Object a_other) {
    try {
      /**@todo also compare Configuration?*/
      return compareTo(a_other) == 0;
    }
    catch (ClassCastException cex) {
      return false;
    }
  }

}
