/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap;

/**
 * Base class for any implementation of interface IChromosome.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public abstract class BaseChromosome
    implements IChromosome, IInitializer {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.7 $";

  /**
   * The configuration object to use
   */
  private /*transient*/ Configuration m_configuration;

  /**
   * The only constructor in this class. Sets the immutable configuration.
   *
   * @param a_configuration the configuration to set
   * @throws InvalidConfigurationException if configuration is null
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public BaseChromosome(Configuration a_configuration)
      throws InvalidConfigurationException {
    if (a_configuration == null) {
      throw new InvalidConfigurationException(
          "Configuration to be set must not"
          + " be null!");
    }
    m_configuration = a_configuration;
  }

  /**
   * @return the configuration used
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public Configuration getConfiguration() {
    return m_configuration;
  }

  /**
   * Creates and returns a copy of this object.
   *
   * @return a clone of this instance
   * @throws IllegalStateException instead of CloneNotSupportedException
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public abstract Object clone();
}
