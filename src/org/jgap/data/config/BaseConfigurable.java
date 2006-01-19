/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.data.config;

import java.util.*;
import org.jgap.*;

/**
 * Abstract base implementation for Configurables.
 *
 * @author Klaus Meffert
 * @since 2.6
 * */
public abstract class BaseConfigurable
    implements Configurable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  public void setConfigProperty(String a_name, String a_value)
      throws ConfigException, InvalidConfigurationException {
    throw new ConfigException("Unknown multi property: " + a_name);
  }

  public void setConfigMultiProperty(String a_name, ArrayList a_values)
      throws ConfigException, InvalidConfigurationException {
    throw new ConfigException("Unknown multi property: " + a_name);
  }

  /**
   * Default implementation of this method. To be called only for derived
   * class objects.
   * @return a concrete ConfigurationHandler
   *
   * @author Siddhartha Azad
   * @since 2.4
   * */
  public ConfigurationHandler getConfigurationHandler() throws ConfigException {
    throw new ConfigException("No ConfigurationHandler present for this class");
  }

}
