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
import org.homedns.dade.jcgrid.worker.*;
import org.jgap.*;
import org.jgap.distr.grid.*;
import org.jgap.event.*;
import org.jgap.impl.*;

/**
 * A worker receives work units from a JGAPServer and sends back computed
 * solutions to a JGAPServer.
 *
 * @author Klaus Meffert
 * @since 3.2 (since 3.01 this class contained something different that is now
 * in class org.jgap.distr.grid.JGAPWorkers)
 */
public class JGAPWorker
    implements Worker {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.6 $";

  /**
   * Executes the evolution and returns the result.
   *
   * @param work WorkRequest
   * @param workDir String
   * @return WorkResult
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.01
   */
  public WorkResult doWork(WorkRequest work, String workDir)
      throws Exception {
    JGAPRequest req = ( (JGAPRequest) work);
    // Setup configuration.
    // --------------------
    Configuration conf = req.getConfiguration();
    req.setConfiguration(conf);
    conf.setEventManager(new EventManager()); //because it is not serialized!
    conf.setJGAPFactory(new JGAPFactory(false)); //because it is not serialized!
    // Setup the genotype to evolve.
    // -----------------------------
    Population initialPop = req.getPopulation();
    Genotype gen = req.getGenotypeInitializer().setupGenotype(req, initialPop);
    // Execute evolution via registered strategy.
    // ------------------------------------------
    req.getEvolveStrategy().evolve(gen);
    // Assemble result according to registered strategy.
    // -------------------------------------------------
    WorkResult res = req.getWorkerReturnStrategy().assembleResult(req, gen);
    return res;
  }
}
