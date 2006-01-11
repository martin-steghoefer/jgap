/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.distr;

/**
 * Holds information about an IMaster instance and allows to communicate with
 * it.
 *
 * @author Klaus Meffert
 * @since 2.4
 */
public class MasterInfo {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  // The computer's address over it is reachable in the network
  public String m_IPAddress;

  // Only for display purposes
  public String m_name;
}
