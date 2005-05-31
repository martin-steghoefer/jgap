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
 * Represents an IMaster instance. Distributes work to IWorker instances.
 *
 * @author Klaus Meffert
 * @since 2.3
 */
public class Master {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  //information needed by workers
  private MasterInfo m_masterinfo;

  public Master() {
  }

  /**
   *
   * @return MasterInfo
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public MasterInfo getMasterInfo() {
    return m_masterinfo;
  }
}
