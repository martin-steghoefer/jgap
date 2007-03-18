/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.distr.grid.gp;

import java.io.*;

import org.homedns.dade.jcgrid.client.*;
import org.jgap.*;
import org.jgap.gp.impl.*;

/**
 * Interface for a GP-related grid configuration. It provides all information
 * necessary to define a problem and a strategy to distributedly solve the problem.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public interface IGridConfigurationGP
    extends Serializable {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.2 $";

  IClientFeedbackGP getClientFeedback();

  IClientEvolveStrategyGP getClientEvolveStrategy();

  IRequestSplitStrategyGP getRequestSplitStrategy();

  GPConfiguration getConfiguration();

  IWorkerEvolveStrategyGP getWorkerEvolveStrategy();

  void setWorkerEvolveStrategy(IWorkerEvolveStrategyGP a_strategy);

  void setClientEvolveStrategy(IClientEvolveStrategyGP a_strategy);

  void setClientFeedback(IClientFeedbackGP a_clientFeedback);

  void setRequestSplitStrategy(IRequestSplitStrategyGP a_splitStrategy);

  IWorkerReturnStrategyGP getWorkerReturnStrategy();

  void setWorkerReturnStrategy(IWorkerReturnStrategyGP a_strategy);

  IGenotypeInitializerGP getGenotypeInitializer();

  void setGenotypeInitializer(IGenotypeInitializerGP a_initializer);

  void initialize(GridNodeClientConfig gridconfig)
      throws Exception;

  void validate()
      throws Exception;
}
