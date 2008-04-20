/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.distr.grid.common;

/**
 * Signals that no work results are available up to now resp. that none could
 * be found.
 *
 * @author Klaus Meffert
 * @since 3.3.3
 */
public class NoWorkResultsFoundException extends Exception {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  public NoWorkResultsFoundException() {
  }
}
