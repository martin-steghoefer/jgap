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
 * Exception throw when there is an error with configuring JGAP via the GUI.
 * @author Siddhartha Azad.
 * */
public class ConfigException extends Exception {

/**
* Constructs a new ConfigException instance with the
* given error message.
*
* @param a_message An error message describing the reason this exception
*                  is being thrown.
*/
public ConfigException(String a_message) {
super(a_message);
}
}