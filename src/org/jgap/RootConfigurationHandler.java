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
 * @author Siddhartha Azad.
 * */
public class RootConfigurationHandler
    implements ConfigurationHandler {

  private final static String CVS_REVISION = "$Revision: 1.4 $";

  /**
   * Return the name of this Configuration Object to be used in the properties
   * file.
   * @return Name of this Configuration Object (name of what you are configuring)
   * */
  public String getName() {
    return "Configuration";
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
    // Just a test property for testing JTextFields on the GUI

    /**@todo Remove this later*/
    cp = new ConfigProperty();
    cp.setName("TestProps");
    cp.setType("int");
    cp.setWidget("JTextField");
    cProps.add(cp);
    return cProps;
  }
}
