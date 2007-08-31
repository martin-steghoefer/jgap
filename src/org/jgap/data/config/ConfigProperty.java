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

/**
 * Represents a property to be shown on the configuration screen.
 *
 * @author Siddhartha Azad
 * @since 2.3
 */
public class ConfigProperty {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.5 $";

  // name of the property
  private String m_name;

  // widget to use to get the value of this property
  private String m_widget;

  // label with which to display this property
  private String m_label;

  // allowed values for this property (if applicable)
  private List m_values;

  /**
   * Default Constructor for a ConfigProperty.
   *
   * @author Siddhartha Azad
   * @since 2.3
   */
  public ConfigProperty() {
    // defaults
    m_name = "";
    m_label = "";
    m_widget = "JTextField";
    m_values = Collections.synchronizedList(new ArrayList());
  }

  /**
   * @return name associated with this property
   *
   * @author Siddhartha Azad
   * @since 2.3
   */
  public String getName() {
    return m_name;
  }

  /**
   * Setter for the name of this property.
   * @param a_name the name associated with this property. This name will be
   * used as the key in the properties file for persisting
   * configuration information
   *
   * @author Siddhartha Azad
   * @since 2.3
   */
  public void setName(final String a_name) {
    m_name = a_name;
    // by default display label is same as the name
    if (m_label.equals("")) {
      m_label = a_name;
    }
  }


  /**
   * @return name of the widget associated with this property
   *
   * @author Siddhartha Azad
   * @since 2.3
   */
  public String getWidget() {
    return m_widget;
  }

  /**
   * Sets the widget.
   * @param a_widget either "JList" or "JTextField" for now
   *
   * @author Siddhartha Azad
   * @since 2.3
   */
  public void setWidget(final String a_widget) {
    m_widget = a_widget;
  }

  /**
   * @return label of the property
   *
   * @author Siddhartha Azad
   * @since 2.4
   */
  public String getLabel() {
    return m_label;
  }

  /**
   * Sets the label.
   * @param a_label the label of this property, by default the same as the
   * name of the property
   *
   * @author Siddhartha Azad
   * @since 2.4
   */
  public void setLabel(final String a_label) {
    m_label = a_label;
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
  public void addValue(final String a_value) {
    m_values.add(a_value);
  }

  /**
   * @return iterator on the values ArrayList for this property
   *
   * @author Siddhartha Azad
   * @since 2.3
   */
  public Iterator getValuesIter() {
    return m_values.iterator();
  }
}
