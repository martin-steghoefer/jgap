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

/**
 * Interface a ConfigWriter uses to get the information from a ConfigFrame.
 * @author Siddhartha Azad.
 * */
public interface IConfigInfo {
  final static String CVS_REVISION = "$Revision: 1.2 $";

  ConfigData getConfigData();

  /**
   * Get the config file to write to.
   * @return The config file name to write to.
   */
  String getFileName();
}
