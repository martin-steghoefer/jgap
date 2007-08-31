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

/**
 * This interface must be implemented for any class to be Configurable.
 *
 * @author Siddhartha Azad
 * @author Klaus Meffert
 * @since 2.3
 * */
public interface Configurable {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.4 $";

  /**
   * @return name of the variable representing a configurable
   * @author Klaus Meffert
   * @since 2.6
   */
//  String getConfigVarName();
}
