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
 * The interface for the GUI to retrieve the information about what a Configurable
 * looks like and how it must be rendered.
 * @author Siddhartha Azad.
 * */
public interface ConfigurationHandler {
  /**
   * Return the name of this Configuration Object to be used in the properties
   * file.
   * @return Name of this Configuration Object (name of what you are configuring)
   * */
  String getName();

  /**
   * Return the information to generate the GUI for configuring this class.
   * @return A list of ConfigProperty objects.
   * */
  ArrayList getConfigProperties();
}
