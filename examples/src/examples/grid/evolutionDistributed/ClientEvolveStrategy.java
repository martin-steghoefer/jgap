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

import org.homedns.dade.jcgrid.client.*;
import org.jgap.*;
import org.jgap.distr.grid.*;

/**
 * Sample implementation of a strategy for evolving a generation on the client.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public class ClientEvolveStrategy
    implements IClientEvolveStrategy {
  /** String containing the CVS revision. Read out via reflection!*/
  public final static String CVS_REVISION = "$Revision: 1.3 $";

  private Configuration m_config;

  private IClientFeedback m_clientFeedback;

  private final int m_maxEvolutions = 3;

  private Population m_pop;

  /**
   * Default constructor is necessary here as it will be called dynamically!
   * Don't declare any other constructor as it will not be called!
   */
  public ClientEvolveStrategy() {
  }


  /**
   * Called at the very beginning and only once before distributed evolution
   * starts.
   *
   * @param a_gc GridClient
   * @param a_config Configuration
   * @param a_clientFeedback IClientFeedback
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void initialize(GridClient a_gc, Configuration a_config,
                         IClientFeedback a_clientFeedback)
      throws Exception {
    m_clientFeedback = a_clientFeedback;
    m_config = a_config;
    // Start with an empty population.
    // ------------------------------
    m_pop = new Population(m_config);
  }

  public void afterWorkRequestsSent()
      throws Exception {
    // Important: clear population, otherwise it would grow
    // endlessly.
    // ----------------------------------------------------
    m_pop.clear();
  }

  public boolean isEvolutionFinished(int a_evolutionsDone) {
    // Check if best solution is satisfying.
    // -------------------------------------
    IChromosome fittest = m_pop.determineFittestChromosome();
    if (fittest.getFitnessValue() > 50000) {
      return true;
    }
    // Do the complete evolution cycle 3 times.
    // ----------------------------------------
    if (a_evolutionsDone > m_maxEvolutions) {
      return true;
    }
    else {
      return false;
    }
  }

  /**
   * Called after evolution has finished.
   */
  public void onFinished() {
    IChromosome best = m_pop.determineFittestChromosome();
    m_clientFeedback.info("Best solution evolved: " + best);
  }

  public void evolve()
      throws Exception {
    // Nothing to do here in this example as evolution takes
    // place on behalf of the workers .
    // -----------------------------------------------------
  }

  public JGAPRequest[] generateWorkRequests(JGAPRequest m_workReq,
      IRequestSplitStrategy m_splitStrategy, Object data)
      throws Exception {
    JGAPRequest[] workList;
    m_workReq.setPopulation(m_pop);
    m_workReq.setConfiguration(m_config);
    workList = m_splitStrategy.split(m_workReq);
    return workList;
  }

  /**
   * Merge the received results as a basis for the next evolution.
   *
   * @param a_result JGAPResult
   * @throws Exception
   */
  public void resultReceived(JGAPResult a_result)
      throws Exception {
    m_pop.addChromosomes(a_result.getPopulation());
  }
}
