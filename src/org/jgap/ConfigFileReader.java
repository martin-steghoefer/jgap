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
import java.io.*;

/**
 * This is a Singleton Helper class to read a JGAP config file and provide a
 * simple interface to the config properties.
 * @author Siddhartha Azad.
 * */
public class ConfigFileReader {

	private final static String CVS_REVISION = "$Revision: 1.1 $";
  
	// Name of the config file to read
	private String fileName;
	// Properties read from the config file
	private Properties props;
	// namespace of the property
	private String ns;
	/**
	 * Singleton Instance of ConfigFileReader
	 */
	private static ConfigFileReader cfReader;

	
	/**
	 * Method to create and access the Singleton ConfigFileReader instance.
	 * @author Siddhartha Azad.
	 * @param _fileName Name of the config file.
	 * */
	public static ConfigFileReader instance() {
	  if (cfReader == null)
	  	cfReader = new ConfigFileReader();
	  return cfReader;
	}

	/**
	 * Private Constructor.@param _fileName Name of the config file.
	 * @author Siddhartha Azad.
	 * */
	private ConfigFileReader() {
		props = new Properties();
	}
  
  /**
   * Retrieve the value for the property with the name as in param name.
   * @author Siddhartha Azad.
   * @param name Name of the property of which the value is required.
   * @return value for the property with the name as in param name, null if
   * property not found.
   * */
  public String getValue(String name) {
  	String tmpName = ns+"."+name;
  	String val = props.getProperty(tmpName);
  	return val;
  }
  
  /**
   * Retrieve the values for the property with the name as in param name.
   * @author Siddhartha Azad.
   * @param name Name of the property of which the value is required.
   * @return ArrayList of Strings with values for the property with the name as
   * in param name, null if property not found.
   * */
  public ArrayList getValues(String name) {
  	String val = "";
  	boolean done = false;
  	String tmpName = "";
  	int idx = 0;
  	ArrayList values = new ArrayList();
  	while(!done) {
  		tmpName = ns + "." + name + "[" + idx + "]";
  		val = props.getProperty(tmpName);
  		if(val == null)
  			done = true;
  		else {
  			values.add(val);
  			idx++;
  		}
  	}
  	if(idx == 0)
  		return null;
  	return values;
  }

  /**
   * Set the namespace for the properties that are being read from the
   * config file at this point.
   * @author Siddhartha Azad.
   * @param _ns Namespace for the properties being read.
   * */
  public void setNS(String _ns) {
  	ns = _ns;
  }

 /**
  * Set the config file to load from. Everytime this method is called,
  * properties are reloaded from the config file.
  * @author Siddhartha Azad.
  * @param _fileName Name of the config file.
  * */
  public void setFileName(String _fileName) throws ConfigException {
	fileName = _fileName;
	try {
		this.load();
	}
  	catch(Exception ex) {
        ex.printStackTrace();
        throw new ConfigException("Error reading Config file "+fileName);
    }
} 
  
  /**
   * Load the config properties file into a Properties instance.
   * @author Siddhartha Azad.
   * */
  private void load() throws ConfigException {
  	try {
  	  props.load(new FileInputStream(fileName));
  	}
  	catch(Exception ex) {
      ex.printStackTrace();
      throw new ConfigException("Error reading Config file "+fileName);
  	}
  }
  
}















