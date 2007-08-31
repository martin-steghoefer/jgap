/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.util;

/**
 * Interface for commands (part of the Command pattern)
 *
 * @author Klaus Meffert
 * @since 2.3
 */
public interface ICommand {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.3 $";

  /**
   * Executes the command and returns the result of the operation
   * @param a_parameters parameters need for executing the command
   * @return result of operation
   * @throws Exception in case of any problem
   */
  CommandResult execute(Object a_parameters)
      throws Exception;
}
