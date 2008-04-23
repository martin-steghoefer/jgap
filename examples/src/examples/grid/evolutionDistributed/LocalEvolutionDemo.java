/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.grid.evolutionDistributed;

import org.jgap.distr.grid.*;
import org.jgap.*;
import examples.grid.fitnessDistributed.*;

/**
 * Demonstrates how the grid configuration can be used to do the whole
 * evolution locally (i.e. stand alone without server and workers).<p>
 * Our aim: We can recycle any code written for the grid. We don't have to
 * rewrite or add a single line of code!<p>
 * This class is still under development! It will evolve together with the
 * framework.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public class LocalEvolutionDemo {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  private GridConfiguration m_localconfig;

  private IClientEvolveStrategy m_clientEvolver;

  private MyRequest m_req;

  public LocalEvolutionDemo()
      throws Exception {
    m_localconfig = new GridConfiguration();
    m_localconfig.initialize(null);
    m_clientEvolver = m_localconfig.getClientEvolveStrategy();
    if (m_clientEvolver != null) {
      m_clientEvolver.initialize(null, m_localconfig.getConfiguration(),
                                 m_localconfig.getClientFeedback());
    }
    m_req = assembleWorkRequest();
    evolve();
  }

  protected MyRequest assembleWorkRequest() {
    MyRequest req = new MyRequest("Local session", 0,
                                  m_localconfig.getConfiguration());
    req.setWorkerReturnStrategy(m_localconfig.getWorkerReturnStrategy());
    req.setGenotypeInitializer(m_localconfig.getGenotypeInitializer());
    req.setEvolveStrategy(m_localconfig.getWorkerEvolveStrategy());
    req.setConfiguration(m_localconfig.getConfiguration());
    // Evolution takes place on client only!
    // -------------------------------------
    req.setEvolveStrategy(null);
    return req;
  }

  protected void evolve()
      throws Exception {
    // JGAPClient
//    m_localconfig.getClientFeedback().beginWork();
//    m_localconfig.getClientFeedback().endWork();
    m_clientEvolver.generateWorkRequests(m_req,
        m_localconfig.getRequestSplitStrategy(), null);
    m_clientEvolver.evolve();
    // JGAPWorker
    Genotype genotype = m_localconfig.getGenotypeInitializer().
        setupGenotype(m_req, null);
    if (m_localconfig.getWorkerEvolveStrategy() != null) {
      m_localconfig.getWorkerEvolveStrategy().evolve(genotype);
    }
    if (m_localconfig.getWorkerReturnStrategy() == null) {
      throw new IllegalStateException(
          "Worker return strategy expected, but was null!");
    }
    // Fetch the result and display it.
    // --------------------------------
    JGAPResult res = m_localconfig.getWorkerReturnStrategy().assembleResult(
        m_req, genotype);
    System.out.println(res.getPopulation().determineFittestChromosome());
  }

  public static void main(String[] args) {
    try {
      new LocalEvolutionDemo();
    } catch (Throwable t) {
      t.printStackTrace();
      System.exit(1);
    }
  }
}
