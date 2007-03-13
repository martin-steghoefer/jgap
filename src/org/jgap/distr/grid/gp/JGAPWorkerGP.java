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

import org.homedns.dade.jcgrid.*;
import org.homedns.dade.jcgrid.worker.*;
import org.jgap.*;
import org.jgap.gp.impl.*;
import org.jgap.distr.grid.*;

/**
 * A GP worker receives work units from a JGAPServer and sends back computed
 * solutions to the same JGAPServer.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public class JGAPWorkerGP
    implements Worker {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

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
    JGAPRequestGP req = ( (JGAPRequestGP) work);
    /**@todo set gridworkerfeedback in class GridWorker*/

    // Setup configuration.
    // --------------------
    GPConfiguration conf = req.getConfiguration();
    conf = conf.newInstanceGP(conf.getId() + "_1", conf.getName() + "_1");
    // Important: Re-set the cloned configuration!
    // -------------------------------------------
    req.setConfiguration(conf);
    GPGenotype gen = null;
    // It is possible that no evolution happens at the worker.
    // -------------------------------------------------------
    if (req.getGenotypeInitializer() != null) {
      // Setup the genotype to evolve.
      // -----------------------------
      GPPopulation initialPop = req.getPopulation();
      gen = req.getGenotypeInitializer().setupGenotype(req, initialPop);
      if (req.getWorkerEvolveStrategy() != null) {
        // Execute evolution via registered strategy.
        // ------------------------------------------
        req.getWorkerEvolveStrategy().evolve(gen);
      }
    }
    // Assemble result according to registered strategy.
    // -------------------------------------------------
    WorkResult res = req.getWorkerReturnStrategy().assembleResult(req, gen);
    return res;
  }

  /**
   * Convenience method to start the worker.
   *
   * @param args command-line arguments, such as server address. See
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.01
   */
  public static void main(String[] args)
      throws Exception {
    // Start worker.
    // -------------
    new JGAPWorkers(args);
  }
}
