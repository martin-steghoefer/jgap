/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.grid.fitnessDistributed;

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

//  private GridClient m_gc;

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
//    m_gc = a_gc;
    // Initialize population randomly.
    // -------------------------------
    Genotype gen = Genotype.randomInitialGenotype(a_config);
    m_pop = gen.getPopulation();
  }

  public void afterWorkRequestsSent()
      throws Exception {
    m_pop = new Population(m_config, m_config.getPopulationSize());
  }

  public boolean isEvolutionFinished(int a_evolutionsDone) {
    // Do the complete evolution cycle 3 times.
    // ----------------------------------------
    if (a_evolutionsDone > m_maxEvolutions) {
      return true;
    }
    else {
      return false;
    }
  }

  public void onFinished() {
    IChromosome best = m_pop.determineFittestChromosome();
    m_clientFeedback.info("Best solution evolved: " + best);
  }

  public void evolve()
      throws Exception {
    // Do the evolution locally.
    // Note: It would be easy to also distribute this task.
    // ----------------------------------------------------
    Genotype gen = new Genotype(m_config, m_pop);
    gen.evolve();
    // Get back the evolved population for further evolutions
    // (see beginning of the iteration).
    // ------------------------------------------------------
    m_pop = gen.getPopulation();
  }

  public JGAPRequest[] generateWorkRequests(JGAPRequest m_workReq,
      IRequestSplitStrategy m_splitStrategy, Object data)
      throws Exception {
    // Calculate fitness values of chromosomes.
    // Let each worker compute one fitness value.
    // ------------------------------------------
    JGAPRequest[] workList;
    m_workReq.setPopulation(m_pop);
    workList = m_splitStrategy.split(m_workReq);
    return workList;
  }

  public void resultReceived(JGAPResult a_result)
      throws Exception {
    m_pop.addChromosomes(a_result.getPopulation());
  }
}
