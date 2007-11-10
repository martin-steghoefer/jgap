/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp;

/**
 * Marker interface for complex GP commands. A complex command is sort of a
 * macro.
 * Use case: As using macros too often may not be wanted, you could check for
 * this interface in your node validator and then decide whether to allow the
 * complex command to be used or not.
 *
 * @author Klaus Meffert
 * @since 3.3
 */
public interface IComplexCommand extends java.io.Serializable {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.2 $";

  public static enum Complexity {
  NANO, SMALL, MEDIUM, LARGE, VERY_LARGE} ;


  /**
   * @return the manually assigned complexity of the command
   *
   * @author Klaus Meffert
   * @since 3.3
   */
  Complexity getComplexity();
}
