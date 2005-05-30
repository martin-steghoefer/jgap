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
 * The ConfigurationHandler for the Configuration class itself. This is the
 * entry point for a Configuration.
 * In other words this is for configuring a Configuration.
 *
 * @author Siddhartha Azad.
 * @since 2.3
 * */
public class RootConfigurationHandler
    implements ConfigurationHandler {
  private final static String CVS_REVISION = "$Revision: 1.7 $";

  // Namespae
  private final static String CONFIG_NAMESPACE = "Configuration";

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
    /**@todo This list could be cached after the first call.*/
    /**@todo we could scan all classes in the classpath for implementing
     * the INaturalSelector interface*/
    ArrayList cProps = new ArrayList();
    // NaturalSelectors available. This information will be renders as a JList.
    ConfigProperty cp;
    cp = new ConfigProperty();
    cp.setName("NaturalSelectors");
    cp.setType("Class");
    cp.setWidget("JList");
    cp.addValue("org.jgap.impl.BestChromosomesSelector");
    cp.addValue("org.jgap.impl.TournamentSelector");
    cProps.add(cp);
    // GeneticOperators available. This information will be renders as a JList.
    /**@todo we could scan all classes in the classpath for implementing
     * the GeneticOperator interface*/
    cp = new ConfigProperty();
    cp.setName("GeneticOperators");
    cp.setType("Class");
    cp.setWidget("JList");
    cp.addValue("org.jgap.impl.GaussianMutationOperator");
    cp.addValue("org.jgap.impl.MutationOperator");
    cProps.add(cp);
    // The population size
    cp = new ConfigProperty();
    cp.setName("m_populationSize");
    cp.setType("int");
    cp.setWidget("JTextField");
    cProps.add(cp);
    return cProps;
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
    ConfigFileReader.instance().setNS(CONFIG_NAMESPACE);
    String value = ConfigFileReader.instance().getValue("m_populationSize");
    if (value != null)
      configurable.setConfigProperty("m_populationSize", value);
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
