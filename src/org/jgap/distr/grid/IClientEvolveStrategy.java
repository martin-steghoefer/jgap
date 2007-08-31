/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.distr.grid;

import java.io.*;
import org.jgap.*;
import org.homedns.dade.jcgrid.client.*;

/**
 * Interface for defining a strategy that controls how a client evolves
 * generations.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public interface IClientEvolveStrategy
    extends Serializable {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.3 $";

  void initialize(GridClient a_gc, Configuration a_config,
                  IClientFeedback a_clientFeedback)
      throws Exception;

  JGAPRequest[] generateWorkRequests(JGAPRequest m_workReq,
                                     IRequestSplitStrategy m_splitStrategy,
                                     Object a_genericData)
      throws Exception;

  void resultReceived(JGAPResult a_result)
      throws Exception;

  boolean isEvolutionFinished(int a_evolutionsDone);

  void onFinished();

  void afterWorkRequestsSent()
      throws Exception;

  void evolve()
      throws Exception;
}
