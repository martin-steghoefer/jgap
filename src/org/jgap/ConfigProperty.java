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
 * Represents a property to be shown on the Configuration Screen.
 *
 * @author Siddhartha Azad
 * @since 2.3
 */
public class ConfigProperty {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  private String name;

  private String type;

  private String widget;

  private ArrayList values;

  /**
   * Default Constructor for a ConfigProperty.
   *
   * @author Siddhartha Azad
   * @since 2.3
   */
  ConfigProperty() {
    // defaults
    name = "";
    type = "String";
    widget = "JTextField";
    values = new ArrayList();
  }

  /**
   * @return name associated with this property.
   *
   * @author Siddhartha Azad
   * @since 2.3
   */
  public String getName() {
    return name;
  }

  /**
   * Setter for the name of this property.
   * @param a_name The name associated with this property.
   * This name will be used as the key in the properties file for persisting
   * configuration information.
   *
   * @author Siddhartha Azad
   * @since 2.3
   */
  public void setName(String a_name) {
    name = a_name;
  }

  /**
   * @return type of the value for this property.
   *
   * @author Siddhartha Azad
   * @since 2.3
   */
  public String getType() {
    return type;
  }

  /**
   * @param a_type Example "int" or "Class" or "String".
   *
   * @author Siddhartha Azad
   * @since 2.3
   */
  public void setType(String a_type) {
    type = a_type;
  }

  /**
   * @return name of the widget associated with this property.
   *
   * @author Siddhartha Azad
   * @since 2.3
   */
  public String getWidget() {
    return widget;
  }

  /**
   * Sets the widget
   * @param a_widget Either "JList" or "JTextField" for now.
   *
   * @author Siddhartha Azad
   * @since 2.3
   */
  public void setWidget(String a_widget) {
    widget = a_widget;
  }

  /**
   * Add a value into the values ArrayList. These values are added in case the
   * display component is a ListBox or ComboBox or something that can have
   * multiple values.
   * @param a_value the value to add
   *
   * @author Siddhartha Azad
   * @since 2.3
   */
  public void addValue(String a_value) {
    values.add(a_value);
  }

  /**
   * Get the iterator on the values associated with this property.
   * @return Iterator on the values ArrayList for this property.
   *
   * @author Siddhartha Azad
   * @since 2.3
   */
  public Iterator getValuesIter() {
    return values.iterator();
  }
}
