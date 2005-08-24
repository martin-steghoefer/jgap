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


/**
 * This class contains some Helper methods for the Configuration framework.
 * 
 * @author Siddhartha Azad
 * @since 2.4
 * */
public class ConfigurationHelper {
	
	/**
	 * Retrieve all instances of a certain property from the config file reader and configure
	 * each of these.
	 * @param className The name of the property to configure.
	 * 
	 * @author Siddhartha Azad.
	 * @since 2.4
	 * */
	public static void configureClass(String className) throws ConfigException {
		ArrayList values = ConfigFileReader.instance().getValues(className);
		if(values != null && values.size() > 0) {
	    	String cName = "";
	    	// iterate through all instances of this property and create Configurables for
	    	// them, then configure these
	    	for(Iterator iter = values.iterator(); iter.hasNext(); )  {
	    		try {
	    			cName = (String)iter.next();
	        		Class genClass = Class.forName(cName);
	        		Configurable conObj = (Configurable)genClass.newInstance();
	        		ConfigurationHandler cHandler = conObj.getConfigurationHandler();
	        		cHandler.readConfig();
	        	}
	    		catch(Exception ex) {
	    			ex.printStackTrace();
	    			throw new ConfigException("Error while configuring "+className+"."+cName);
	    		}
	    	}	
	    }
	}
}