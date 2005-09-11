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
import java.io.*;
import javax.swing.*;

/**
 * This class provides an interface to the configuration information to create
 * a JGAP Configuration GUI.
 * @author Siddhartha Azad
 * @since 2.4
 * */
public class MetaConfig {
	// file to read the GUI Configuration information from
	private static final String METACON_FILENAME = "jgap-meta.con";
	private static final String CN = "MetaConfig";
	// singleton instance
	private static MetaConfig instance = null;
	// ClassName-ConfigProperty mapping
	Hashtable metaMap = new Hashtable();
	// state for the parser
	int state;
	private static final int INIT=0;
	private static final int CLASS=1;
	private static final int PROPERTY=2;
	private static final int VALUES=3;
	// class name currently being handled
	String currName = null;
	ConfigProperty currProperty = null;
	
	public static MetaConfig instance() throws MetaConfigException, 
	IOException {
		if(null == instance)
			instance = new MetaConfig();
		return instance;
	}
	
	private MetaConfig() throws MetaConfigException, IOException {
		state = MetaConfig.INIT;
		init();
	}
	
	
	// public interface
	
	/**
	 * Read the meta-config file and load it in memory.
	 * @author Siddhartha Azad
	 * @since 2.4
	 * @param className The name of the class of which the properties are
	 * required
	 * @return The list of properties for this class, if class is registered,
	 * otherwise null.
	 * */
	public ArrayList getConfigProperty(String className) {
		return (ArrayList)(metaMap.get(className));
	}
	
	
	
	/**
	 *	Read the meta-config file and load it in memory.
	 *  @author Siddhartha Azad
	 * 	@since 2.4
	 *	Having to read my own property file without using the Java Property
	 *	class since I need to preserve the order of these properties, plus
	 *	I have duplicate labels.
	 */
	private void init() throws MetaConfigException, IOException {
		File metaFile = new File(METACON_FILENAME);
		FileReader fr = new FileReader(metaFile);
		LineNumberReader lr = new LineNumberReader(fr);
		String line = lr.readLine();
		while(line != null) {
			if(!this.isComment(line))
				parseLine(line);
			line = lr.readLine();
		}
		endState();
		lr.close();
	}
	
	/**
	 * Check whether a line is a comment.
	 * @author Siddhartha Azad
	 * @since 2.4
	 * @return true if a line is a comment, false if it is not.
	 * Any line starting with a '#' is a comment.
	 * */
	private boolean isComment(String line) {
		String tmpLine = line.trim();
		StringBuffer sb = new StringBuffer(tmpLine);
		if(sb.charAt(0) == '#')
			return true;
		return false;
	}
	
	/**
	 * Parse a line. This method is dispatches lines to other methods, hence 
	 * acting like a state machine.
	 * @author Siddhartha Azad
	 * @since 2.4
	 * */
	private void parseLine(String line) throws MetaConfigException {
		String [] tokens = line.split("=");
		if(tokens == null || tokens.length != 2)
			throw new MetaConfigException(CN+".parseLine():Exception while "+
					"parsing "+METACON_FILENAME+" line "+line + " is invalid");
		if(state == MetaConfig.INIT && tokens[0].equals("class"))
			handleClass(tokens[1]);
		else if(state == MetaConfig.CLASS && tokens[0].equals("property"))
			handleProperty(tokens[1]);
		else if(state == MetaConfig.PROPERTY && tokens[0].equals("values"))
			handleValues(tokens[1]);
		else if(state == MetaConfig.PROPERTY && tokens[0].equals("class"))
			handleClass(tokens[1]);
		else if(state == MetaConfig.VALUES && tokens[0].equals("class"))
			handleClass(tokens[1]);
		else if(state == MetaConfig.VALUES && tokens[0].equals("property"))
			handleProperty(tokens[1]);
		else
			throw new MetaConfigException(CN+".parseLine():Exception while "+
					"parsing "+METACON_FILENAME+" state "+state+
					" incompatible with line "+line);
	}
	
	/**
	 * Handle the state when a 'class' tag is found.
	 * @author Siddhartha Azad
	 * @since 2.4
	 * */	
	private void handleClass(String token) {
		int prevState = state;
		state = MetaConfig.CLASS;
		if(currProperty != null)
			add(currName, currProperty);
		currProperty = new ConfigProperty();
		currName = token;
	}
	
	/**
	 * Handle the state when a 'property' tag is found.
	 * @author Siddhartha Azad
	 * @since 2.4
	 * */	
	private void handleProperty(String token) throws MetaConfigException {
		int prevState = state;
		if(prevState == MetaConfig.VALUES) {
			if(currProperty != null) 
				add(currName, currProperty);
		}
		currProperty = new ConfigProperty();
		state = MetaConfig.PROPERTY;
		String [] tokens = token.split(",");
		if(tokens.length < 2 || tokens.length > 3)
			throw new MetaConfigException("Invalid format of property line: "+
					token);
		currProperty.setName(tokens[0].trim());
		currProperty.setWidget(tokens[1].trim());
		if(tokens.length == 3)
			currProperty.setLabel(tokens[2]);
	}
	
	/**
	 * Handle the state when a 'values' tag is found.
	 * @author Siddhartha Azad
	 * @since 2.4
	 * @param token The rhs of the values property.
	 * 
	 * */	
	private void handleValues(String token) throws MetaConfigException {
		int prevState = state;
		state = MetaConfig.VALUES;	
		String [] tokens = token.split(",");
		if(tokens.length == 0)
			throw new MetaConfigException("Invalid format of property line: "+
					token);
		for (int i=0; i < tokens.length; i++)
	         currProperty.addValue(tokens[i].trim());
	}
	
	/**
	 * Called once the EOF is encountered while parsing the file.
	 * @author Siddhartha Azad
	 * @since 2.4
	 * @throws MetaConfigException if parsing ends in an invalid state.
	 * */
	private void endState() throws MetaConfigException {
		if(state != MetaConfig.PROPERTY && state != MetaConfig.VALUES)
			throw new MetaConfigException("Invalid format of JGAP MetaConfig "+
					"file: "+ METACON_FILENAME+"Ending in Invalid state : "+
					state);
		if(currProperty != null)
			add(currName, currProperty);
	}
	
	/**
	 * Add a new ConfigProperty for a certain class to the hashtable of 
	 * properties.
	 * @author Siddhartha Azad
	 * @since 2.4
	 * @param currName Name of the class to which the property belongs.
	 * @param cp CofigProperty to be added to the class.
	 * */
	private void add(String currName, ConfigProperty cp) {
		ArrayList props = (ArrayList)metaMap.get(currName);
		if(null == props) {
			props = new ArrayList();
			metaMap.put(currName, props);
		}
		props.add(cp);
	}
}
