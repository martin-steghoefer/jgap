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
 * Represents a propety to be shown on the Configuration Screen.
 * @author Siddhartha Azad.
 */
public class ConfigProperty {
	/**
	 * Default Constructor for a ConfigProperty.
	 * @author Siddhartha Azad.
	 */
	ConfigProperty() {
		// defaults
		name = "";
		type = "String";
		widget = "JTextField";
		values = new ArrayList();
	}
	
	/**
	 * Getter for the name of this property.
	 * @return name associated with this property.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Setter for the name of this property.
	 * @param _name The name associated with this property.
	 * This name will be used as the key in the properties file for persisting
	 * configuration information.
	 */
	public void setName(String _name) {
		name = _name;
	}
	
	/**
	 * Getter for the type of the values of this property.
	 * @return type of the value for this property.
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * Setter for the type of the values of this property.
	 * @param _type Example "int" or "Class" or "String".
	 */
	public void setType(String _type) {
		type = _type;
	}
	
	/**
	 * Getter for the name of the widget to be used to render this property.
	 * @return name of the widget associated with this property.
	 */
	public String getWidget() {
		return widget;
	}
	
	/**
	 * Setter for the name of the widget to be used to render this property.
	 * @param _widget Either "JList" or "JTextField" for now.
	 * @return name associated with this property.
	 */
	public void setWidget(String _widget) {
		widget = _widget;
	}
	
	/**
	 * Add a value into the values ArrayList. These values are added in case the
	 * display component is a ListBox or ComboBox or something that can have
	 * multiple values.
	 * @author Siddhartha Azad.
	 */
	public void addValue(String value) {
		values.add(value);
	}
	
	/**
	 * Get the iterator on the values associated with this property.
	 * @return Iterator on the values ArrayList for this property.
	 * @author Siddhartha Azad.
	 */
	public Iterator getValuesIter() {
		return values.iterator();
	}
	
	private String name;
	private String type;
	private String widget;
	ArrayList values;
}