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
 * This interface must be implemented for any class to be Configurable.
 * @author Siddhartha Azad.
 * */
public interface Configurable {
	
	/**
	 * Return a ConfigurationHandler specific to the concrete class implementing
	 * this interface.
	 * @return A Concrete ConfigurationHandler.
	 * */
	ConfigurationHandler getConfigurationHandler();
}