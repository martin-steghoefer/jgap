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
 * This interface must be implemented for any class to be Configurable.
 *
 * @author Siddhartha Azad
 * @since 2.3
 * */
public interface Configurable {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.2 $";

  /**
   * Return a ConfigurationHandler specific to the concrete class implementing
   * this interface.
   * @return a concrete ConfigurationHandler
   * @throws ConfigException
   *
   * @author Siddhartha Azad
   * @since 2.3
   */
  ConfigurationHandler getConfigurationHandler()
      throws ConfigException;

  /**
   * Pass the name and value of a property to be set.
   * @param a_name the name of the property
   * @param a_value the value of the property
   * @throws ConfigException
   * @throws InvalidConfigurationException
   *
   * @author Siddhartha Azad
   * @since 2.3
   */
  void setConfigProperty(String a_name, String a_value)
      throws ConfigException, InvalidConfigurationException;

  /**
   * Pass the name and values of a property to be set.
   * @param a_name the name of the property
   * @param a_values the different values of the property
   * @throws ConfigException
   * @throws InvalidConfigurationException
   *
   * @author Siddhartha Azad
   * @since 2.3
   */
  void setConfigMultiProperty(String a_name, ArrayList a_values)
      throws ConfigException, InvalidConfigurationException;
}
