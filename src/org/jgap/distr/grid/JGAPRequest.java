/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.distr.grid;

import org.homedns.dade.jcgrid.*;
import org.jgap.*;

/**
 * An instance creating single requests to be sent to a worker.
 *
 * @author Klaus Meffert
 * @since 3.1
 */
public abstract class JGAPRequest
    extends WorkRequest {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  private Configuration m_config;

  public JGAPRequest(String name, int id, Configuration a_config) {
    super(name, id);
    m_config = a_config;
  }

  /**
   * @return the configuration set
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public Configuration getConfiguration() {
    return m_config;
  }

  /**
   * Creates single requests to be sent to workers.
   *
   * @return single requests to be computed by workers
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public abstract JGAPRequest[] split()
      throws Exception;
}
