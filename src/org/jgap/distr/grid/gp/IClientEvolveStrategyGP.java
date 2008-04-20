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
import org.jgap.distr.grid.*;
import org.jgap.gp.impl.*;

/**
 * Interface for defining a strategy that controls how a client evolves
 * generations of GP's.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public interface IClientEvolveStrategyGP
    extends Serializable {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.4 $";

  void initialize(IGridClientMediator a_gc, GPConfiguration a_config,
                  IClientFeedbackGP a_clientFeedback)
      throws Exception;

  JGAPRequestGP[] generateWorkRequests(JGAPRequestGP m_workReq,
                                       IRequestSplitStrategyGP m_splitStrategy,
                                       Object a_genericData)
      throws Exception;

  void resultReceived(JGAPResultGP a_result)
      throws Exception;

  boolean isEvolutionFinished(int a_evolutionsDone);

  void onFinished();

  void afterWorkRequestsSent()
      throws Exception;

  void evolve()
      throws Exception;
}
