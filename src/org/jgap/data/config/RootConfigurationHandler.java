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

import java.lang.reflect.*;
import java.util.*;

import org.jgap.*;

/**
 * The ConfigurationHandler for the Configuration class itself. This is the
 * entry point for a Configuration.
 * In other words this is for dynamically building up a Configuration.
 *
 * @author Siddhartha Azad
 * @author Klaus Meffert
 * @since 2.3
 * */
public class RootConfigurationHandler
    implements ConfigurationHandler {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.12 $";

  // Namespace
  private final static String CONFIG_NAMESPACE = "org.jgap.Configuration";

  // constants to indicate various properties
  // ----------------------------------------
  private final static String GENETIC_OPS = "GeneticOperators";

  private final static String NATURAL_SELS = "NaturalSelectors";

  private Configurable m_configurable;

  /**
   * @return Name of this Configuration Object (name of what you are
   * configuring) to be used in the properties file.
   *
   * @since 2.3
   * */
  public String getName() {
    return "Configuration";
  }

  /**
   * Return the information to generate the GUI for configuring this class.
   * @return a list of ConfigProperty objects
   * @since 2.3
   * */
  public List getConfigProperties() {
    return null;
  }

  /**
   * Get the namespace to be used in the config file for the Configurable
   * this ConfigurationHandler belongs to.
   * @return the namespace of the Configurable
   *
   * @author Siddhartha Azad
   * @since 2.3
   * */
  public String getNS() {
    return CONFIG_NAMESPACE;
  }

  /**
   * Method that will populate an Configurable with the properties in the
   * config file.
   *
   * @throws ConfigException
   * @throws InvalidConfigurationException
   *
   * @author Siddhartha Azad
   * @since 2.3
   * */
  public void readConfig()
      throws ConfigException, InvalidConfigurationException {
    // TODO adapt to new concept of configuration via reflection and marker
    // interface
    // set the namespace to get the properties from
    ConfigFileReader.instance().setNS(CONFIG_NAMESPACE);
    String value = ConfigFileReader.instance().getValue("m_populationSize");
    try {
      if (value != null) {
        setConfigProperty(m_configurable, "m_populationSize", value);
      }
    } catch (IllegalAccessException ex) {
      ex.printStackTrace();
      throw new InvalidConfigurationException(ex.getMessage());
    }
    // go through all genetic operators and configure them
    configureClass(GENETIC_OPS);
    // go through all natural selectors and configure them
    configureClass(NATURAL_SELS);
  }

  /**
   * Set the Configurable to which this ConfigurationHandler belongs.
   * @param a_configurable the Configurable to which this ConfigurationHandler
   * belongs
   *
   * @author Siddhartha Azad
   * @since 2.3
   * */
  public void setConfigurable(Configurable a_configurable) {
    m_configurable = a_configurable;
  }

  /**
   * Sets the property of a configurable to a given value. Uses reflection to
   * do so. Queries the method getConfigVarName for the name of the field
   * hosting the configurable properties.
   *
   * @param a_configurable the configurable to use
   * @param a_propertyName the property to set
   * @param a_value the value to assign to the property
   * @throws IllegalAccessException
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void setConfigProperty(Object a_configurable, String a_propertyName,
                                String a_value)
      throws IllegalAccessException {
//    Use following in case variable name "m_config" should be determined
//    dynamically.
//    Method m = null;
//    try {
//      m = a_configurable.getClass().getDeclaredMethod("getConfigVarName",
//          new Class[0]);
//    } catch (NoSuchMethodException nex) {
//      // nothing to set here
//      return;
//    }
    String configVarName = "m_config"; //(String)m.invoke(a_configurable, new Object[0]);
    Field configVar = getPrivateField(a_configurable, configVarName);
    configVar.setAccessible(true);
    Object configObj = configVar.get(a_configurable);
    Field propertyVar = getPrivateField(configObj, a_propertyName);
    propertyVar.setAccessible(true);
    Class type = propertyVar.getType();
    if (type.equals(boolean.class)) {
      propertyVar.setBoolean(configObj, Boolean.valueOf(a_value).booleanValue());
    }
    else if (type.equals(byte.class)) {
      propertyVar.setByte(configObj, Byte.valueOf(a_value).byteValue());
    }
    else if (type.equals(char.class)) {
      propertyVar.setChar(configObj, a_value.charAt(0));
    }
    else if (type.equals(double.class)) {
      propertyVar.setDouble(configObj, Double.valueOf(a_value).doubleValue());
    }
    else if (type.equals(float.class)) {
      propertyVar.setFloat(configObj, Float.valueOf(a_value).floatValue());
    }
    else if (type.equals(int.class)) {
      propertyVar.setInt(configObj, Integer.valueOf(a_value).intValue());
    }
    else if (type.equals(long.class)) {
      propertyVar.setLong(configObj, Long.valueOf(a_value).longValue());
    }
    else if (type.equals(short.class)) {
      propertyVar.setShort(configObj, Short.valueOf(a_value).shortValue());
    }
    else if (type.equals(String.class)) {
      propertyVar.set(configObj, a_value);
    }
    else {
      throw new RuntimeException("Unknown field type: " + type.getName());
    }
  }

  /**
   * Helper method: Read a private field.
   * @param a_instance the instance the field is contained with
   * @param a_fieldName the name of the field to read
   * @return the Field object or null, if none found
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public static Field getPrivateField(Object a_instance, String a_fieldName) {
    final Field fields[] = a_instance.getClass().getDeclaredFields();
    for (int i = 0; i < fields.length; ++i) {
      if (a_fieldName.equals(fields[i].getName())) {
        fields[i].setAccessible(true);
        return fields[i];
      }
    }
    return null;
  }

  /**
   * Retrieve all instances of a certain property from the config file reader
   * and configure each of these.
   * @param className the name of the property to configure
   * @throws ConfigException
   *
   * @author Siddhartha Azad
   * @since 2.4
   * */
  public static void configureClass(String className)
      throws ConfigException {
    List values = ConfigFileReader.instance().getValues(className);
    if (values != null && values.size() > 0) {
      String cName = "";
      // iterate through all instances of this property and create Configurables
      // for them, then configure these
      for (Iterator iter = values.iterator(); iter.hasNext(); ) {
        try {
          cName = (String) iter.next();
          Class genClass = Class.forName(cName);
          Configurable conObj = (Configurable) genClass.newInstance();
//TODO          ConfigurationHandler cHandler = conObj.getConfigurationHandler();
//TODO          cHandler.readConfig();
        } catch (Exception ex) {
          throw new ConfigException("Error while configuring " + className +
                                    "." + cName);
        }
      }
    }
  }
}
