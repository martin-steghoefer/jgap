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

import java.util.*;

/**
 * This interface must be implemented for any class to be Configurable.
 *
 * @author Siddhartha Azad
 * @since 2.3
 * */
public interface Configurable {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.4 $";

  /**
   * Return a ConfigurationHandler specific to the concrete class implementing
   * this interface.
   * @return a concrete ConfigurationHandler
   *
   * @author Siddhartha Azad
   * @since 2.3
   * */
  ConfigurationHandler getConfigurationHandler();

  /**
   * Pass the name and value of a property to be set.
   * @author Siddhartha Azad.
   * @param name The name of the property.
   * @param value The value of the property.
   *
   * @author Siddhartha Azad
   * @since 2.3
   * */

  void setConfigProperty(String name, String value)
      throws ConfigException,
      InvalidConfigurationException;

  /**
   * Pass the name and values of a property to be set.
   * @author Siddhartha Azad.
   * @param name The name of the property.
   * @param values The different values of the property.
   *
   * @author Siddhartha Azad
   * @since 2.3
   * */
  void setConfigMultiProperty(String name, ArrayList values)
      throws
      ConfigException, InvalidConfigurationException;
}
