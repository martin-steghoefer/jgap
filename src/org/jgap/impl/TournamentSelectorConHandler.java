/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.impl;

import org.jgap.*;
import org.jgap.data.config.*;
import java.util.ArrayList;

public class TournamentSelectorConHandler
    implements ConfigurationHandler {
  private final static String CVS_REVISION = "$Revision: 1.5 $";

  // Must be the fully qualified class name
  private final static String CONFIG_NAMESPACE =
      "org.jgap.impl.TournamentSelector";

  private Configurable m_configurable;

  /**
   * Return the name of this Configuration Object to be used in the properties
   * file.
   * @return Name of this Configuration Object (name of what you are configuring)
   * */
  public String getName() {
    return CONFIG_NAMESPACE;
  }

  /**
   * Return the information to generate the GUI for configuring this class.
   * @return A list of ConfigProperty objects.
   * */
  public ArrayList getConfigProperties() {
    /**@todo This list could be cached after the first call.*/
    /**@todo we could scan all classes in the classpath for implementing
     * the INaturalSelector interface*/
    ArrayList cProps = new ArrayList();
    // NaturalSelectors available. This information will be renders as a JList.
    ConfigProperty cp;
    // The population size
    cp = new ConfigProperty();
    cp.setName("m_probability");
    cp.setWidget("JTextField");
    cProps.add(cp);
    return cProps;
  }

  /**
   * Get the namespace to be used in the config file for the Configurable
   * this ConfigurationHandler belongs to.
   * @return the namespace of the Configurable
   * @author Siddhartha Azad
   * */
  public String getNS() {
    return CONFIG_NAMESPACE;
  }

  /**
   * Method that will populate an Configurable with the properties in the
   * config file.
   * @author Siddhartha Azad
   * */
  public void readConfig()
      throws ConfigException, InvalidConfigurationException {
    ConfigFileReader.instance().setNS(CONFIG_NAMESPACE);
    String value = ConfigFileReader.instance().getValue("m_probability");
    if (value != null) {
      m_configurable.setConfigProperty("m_probability", value);
    }
  }

  /**
   * Set the Configurable to which this ConfigurationHandler belongs.
   * @param a_configurable the Configurable to which this ConfigurationHandler
   * belongs to
   *
   * @author Siddhartha Azad
   * */
  public void setConfigurable(Configurable a_configurable) {
    m_configurable = a_configurable;
  }

}
