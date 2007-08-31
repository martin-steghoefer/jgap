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

import org.homedns.dade.jcgrid.client.*;
import org.jgap.*;

/**
 * Interface for a grid configuration. It provides all information necessary
 * to define a problem and a strategy to distributedly solve the problem.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public interface IGridConfiguration
    extends Serializable {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.3 $";

  IClientFeedback getClientFeedback();

  IClientEvolveStrategy getClientEvolveStrategy();

  IRequestSplitStrategy getRequestSplitStrategy();

  Configuration getConfiguration();

  IWorkerEvolveStrategy getWorkerEvolveStrategy();

  void setWorkerEvolveStrategy(IWorkerEvolveStrategy a_strategy);

  void setClientEvolveStrategy(IClientEvolveStrategy a_strategy);

  void setClientFeedback(IClientFeedback a_clientFeedback);

  void setRequestSplitStrategy(IRequestSplitStrategy a_splitStrategy);

  IWorkerReturnStrategy getWorkerReturnStrategy();

  void setWorkerReturnStrategy(IWorkerReturnStrategy a_strategy);

  IGenotypeInitializer getGenotypeInitializer();

  void setGenotypeInitializer(IGenotypeInitializer a_initializer);

  void initialize(GridNodeClientConfig gridconfig)
      throws Exception;

  void validate()
      throws Exception;
}
