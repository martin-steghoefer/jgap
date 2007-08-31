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

/**
 * Base class for rate calculators.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public abstract class BaseRateCalculator
    implements IUniversalRateCalculator {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.3 $";

  private transient Configuration m_config;

  /**
   * @param a_config the configuration to use
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public BaseRateCalculator(Configuration a_config)
      throws InvalidConfigurationException {
    if (a_config == null) {
      throw new InvalidConfigurationException("Configuration must not be null!");
    }
    m_config = a_config;
  }

  /**
   * @return the configuration used
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public Configuration getConfiguration() {
    return m_config;
  }
}
