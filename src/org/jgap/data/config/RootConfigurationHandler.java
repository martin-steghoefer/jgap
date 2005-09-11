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
 * The ConfigurationHandler for the Configuration class itself. This is the
 * entry point for a Configuration.
 * In other words this is for configuring a Configuration.
 *
 * @author Siddhartha Azad.
 * @since 2.3
 * */
public class RootConfigurationHandler
    implements ConfigurationHandler {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  // Namespace
  private final static String CONFIG_NAMESPACE = "org.jgap.Configuration";
  
  // constatns to indicate various properties
  
  private final static String GENETIC_OPS = "GeneticOperators";
  
  private final static String NATURAL_SELS = "NaturalSelectors";

  /**
   * @return Name of this Configuration Object (name of what you are
   * configuring) to be used in the properties file
   * @since 2.3
   * */
  public String getName() {
    return "Configuration";
  }

  /**
   * Return the information to generate the GUI for configuring this class.
   * @return A list of ConfigProperty objects.
   * @since 2.3
   * */
  public ArrayList getConfigProperties() {
   return null;
  }

  /**
   * Get the namespace to be used in the config file for the Configurable
   * this ConfigurationHandler belongs to.
   * @return The namepsace of the Configurable
   *
   * @author Siddhartha Azad.
   * @since 2.3
   * */
  public String getNS() {
    return CONFIG_NAMESPACE;
  }

  /**
   * Method that will populate an Configurable with the properties in the
   * config file.
   * @throws ConfigException
   * @throws InvalidConfigurationException
   *
   * @author Siddhartha Azad.
   * @since 2.3
   * */
  public void readConfig()
      throws ConfigException, InvalidConfigurationException {
  	// set the namespace to get the properties from
    ConfigFileReader.instance().setNS(CONFIG_NAMESPACE);
    String value = ConfigFileReader.instance().getValue("m_populationSize");
    if (value != null)
      configurable.setConfigProperty("m_populationSize", value);
    
    //  go through all genetic operators and configure them
    try {
    	ConfigurationHelper.configureClass(GENETIC_OPS);
    }
    catch(ConfigException conEx) {
    	conEx.printStackTrace();
    	System.err.println("Error while configuring " + GENETIC_OPS);
    }
    
    //  go through all natural selectors and configure them
    try {
    	ConfigurationHelper.configureClass(NATURAL_SELS);
    }
    catch(ConfigException conEx) {
    	conEx.printStackTrace();
    	System.err.println("Error while configuring " + NATURAL_SELS);
    }
    // go through all natural selectors and configure them
  }

  /**
   * Set the Configurable to which this ConfigurationHandler belongs.
   * @param _configurable The Configurable to which this ConfigurationHandler
   * belongs.
   *
   * @author Siddhartha Azad.
   * @since 2.3
   * */
  public void setConfigurable(Configurable _configurable) {
    configurable = _configurable;
  }
  
  Configurable configurable;
}
