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
 * The interface for the GUI to retrieve the information about what a Configurable
 * looks like and how it must be rendered.
 * @author Siddhartha Azad.
 * */
public interface ConfigurationHandler {

  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.4 $";

  /**
   * Return the name of this Configuration Object to be used in the properties
   * file.
   * @return Name of this Configuration Object (name of what you are configuring)
   * */
  String getName();

  /**
   * Return the information to generate the GUI for configuring this class.
   * @return A list of ConfigProperty objects.
   * */
  ArrayList getConfigProperties();
  
  /**
   * Method that will populate an Configurable with the properties in the
   * config file.
   * @author Siddhartha Azad.
   * */
  void readConfig()  throws ConfigException,
	InvalidConfigurationException ;
  
  /**
   * Get the namespace to be used in the config file for the Configurable
   * this ConfigurationHandler belongs to.
   * @author Siddhartha Azad.
   * @return The namepsace of the Configurable
   * */
  String getNS();
  
  /**
   * Set the Configurable to which this ConfigurationHandler belongs.
   * @author Siddhartha Azad.
   * @param _configurable The Configurable to which this ConfigurationHandler
   * belongs. 
   * */
  void setConfigurable(Configurable _configurable);
}
