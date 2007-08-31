/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.data.config;

import java.util.*;
import org.jgap.*;

/**
 * The interface for the GUI to retrieve the information about what a
 * Configurable looks like and how it must be rendered.
 *
 * @author Siddhartha Azad
 * @since 2.3
 * */
public interface ConfigurationHandler {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.5 $";

  /**
   * Return the name of this Configuration Object to be used in the properties
   * file.
   * @return name of this config Object (name of what you are configuring)
   */
  String getName();

  /**
   * Return the information to generate the GUI for configuring this class.
   * @return a list of ConfigProperty objects
   */
  List getConfigProperties();

  /**
   * Method that will populate a Configurable with the properties in the
   * config file.
   *
   * @author Siddhartha Azad
   * @since 2.3
   */
  void readConfig()
      throws ConfigException, InvalidConfigurationException;

  /**
   * Get the namespace to be used in the config file for the Configurable
   * this ConfigurationHandler belongs to.
   * @return namepsace of the Configurable
   *
   * @author Siddhartha Azad
   * @since 2.3
   */
  String getNS();

  /**
   * Set the Configurable to which this ConfigurationHandler belongs.
   * @param a_configurable The Configurable to which this ConfigurationHandler
   * belongs.
   *
   * @author Siddhartha Azad
   * @since 2.3
   */
  void setConfigurable(Configurable a_configurable);
}
