/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.distr.grid.gp;

import java.io.*;

/**
 * Interface for a strategy how to split a GP work request into parts. Each part
 * will be sent to a worker as an individual work request.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public interface IRequestSplitStrategyGP
    extends Serializable {

  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.2 $";

  /**
   * Creates single requests to be sent to workers. This is done by splitting
   * a single request into several parts. Each parts is sort of a modified
   * clone of the original (super) request
   *
   * @param a_request the request to split
   * @return single requests to be computed by workers
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public JGAPRequestGP[] split(JGAPRequestGP a_request)
      throws Exception;
}
