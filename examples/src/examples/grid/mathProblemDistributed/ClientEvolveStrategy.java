/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.grid.mathProblemDistributed;

import org.homedns.dade.jcgrid.client.*;
import org.jgap.*;
import org.jgap.gp.*;
import org.jgap.gp.impl.*;
import org.jgap.distr.grid.*;
import org.jgap.distr.grid.gp.*;
import org.jgap.gp.function.Subtract;
import org.jgap.gp.function.Add3;
import org.jgap.gp.impl.GPConfiguration;
import org.jgap.gp.function.Divide;
import org.jgap.gp.function.Multiply3;
import org.jgap.gp.function.Pow;
import org.jgap.gp.function.Exp;
import org.jgap.gp.terminal.Variable;
import org.jgap.gp.function.Sine;
import org.jgap.gp.function.Multiply;
import org.jgap.gp.function.Add;
import org.jgap.gp.impl.*;
import org.jgap.gp.terminal.*;
import org.apache.log4j.*;

/**
 * Sample implementation of a strategy for evolving a generation on the client.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public class ClientEvolveStrategy
    extends GPProblem implements IClientEvolveStrategyGP {
  /** String containing the CVS revision. Read out via reflection!*/
  public final static String CVS_REVISION = "$Revision: 1.9 $";

  private static Logger log = Logger.getLogger(ClientEvolveStrategy.class);

//  private GPConfiguration m_config;

  private IClientFeedbackGP m_clientFeedback;

  private final int m_maxEvolutions = 3;

  private GPPopulation m_pop;

  private static Float[] x = new Float[20];

  private static float[] y = new float[20];

  /**@todo pass config*/
//  public ClientEvolveStrategy(GPConfiguration a_conf)
//      throws InvalidConfigurationException {
//    super(a_conf);
//  }

  /**
   * Default constructor is necessary here as it will be called dynamically!
   * Don't declare any other constructor as it will not be called!
   */
  public ClientEvolveStrategy() {
    super();
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
  public void initialize(GridClient a_gc, GPConfiguration a_config,
                         IClientFeedbackGP a_clientFeedback)
      throws Exception {
    log.info("Initializing...");
    super.setGPConfiguration(a_config);
    m_clientFeedback = a_clientFeedback;
    // Start with a randomly initialized population.
    // ---------------------------------------------
    GPGenotype gen = create();
    m_pop = gen.getGPPopulation();
  }

  public void afterWorkRequestsSent()
      throws Exception {
    // Don't clear population here!
    if (false) {
      // Important: clear population, otherwise it would grow
      // endlessly.
      // ----------------------------------------------------
      if (m_pop != null) {
        m_pop.clear();
        log.warn("Population cleared");
      }
    }
  }

  public boolean isEvolutionFinished(int a_evolutionsDone) {
    if (m_pop != null) {
      // Check if best solution is satisfying.
      // -------------------------------------
      IGPProgram fittest = m_pop.determineFittestProgram();
      if (fittest.getFitnessValue() < 20) {
        return true;
      }
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
    IGPProgram best = m_pop.determineFittestProgram();
    m_clientFeedback.info("Best solution evolved: " + best.getFitnessValue());
  }

  public void evolve()
      throws Exception {
    // Nothing to do here in this example as evolution takes
    // place on behalf of the workers .
    // -----------------------------------------------------
  }

  public JGAPRequestGP[] generateWorkRequests(JGAPRequestGP m_workReq,
      IRequestSplitStrategyGP m_splitStrategy, Object data)
      throws Exception {
    JGAPRequestGP[] workList;
    if (m_pop == null || m_pop.isFirstEmpty()) {
      log.warn("Initial population is empty!");
    }
    m_workReq.setPopulation(m_pop);
    m_workReq.setConfiguration(getGPConfiguration());
    if (getGPConfiguration().getJGAPFactory() == null) {
      throw new IllegalStateException("JGAPFactory must not be null!");
    }
    workList = m_splitStrategy.split(m_workReq);
    return workList;
  }

  /**
   * Merge the received results as a basis for the next evolution.
   *
   * @param a_result JGAPResult
   * @throws Exception
   * @since 3.2
   */
  public void resultReceived(JGAPResultGP a_result)
      throws Exception {
    // This is a very simplistic implementation!
    // -----------------------------------------
    GPPopulation pop = a_result.getPopulation();
    if (pop == null || pop.isFirstEmpty()) {
      IGPProgram best = a_result.getFittest();
      if (best == null) {
        log.error("Empty result received");
      }
      else {
        m_pop.addFittestProgram(best);
      }
    }
    else {
      m_pop = pop;
    }
  }

  public GPGenotype create()
      throws InvalidConfigurationException {
    GPConfiguration conf = getGPConfiguration();
    Class[] types = {
        CommandGene.FloatClass};
    Class[][] argTypes = { {}
    };
    Variable vx;
    CommandGene[][] nodeSets = { {
        vx = Variable.create(conf, "X", CommandGene.FloatClass),
        new Add(conf, CommandGene.FloatClass),
        new Add3(conf, CommandGene.FloatClass),
        new Subtract(conf, CommandGene.FloatClass),
        new Multiply(conf, CommandGene.FloatClass),
        new Multiply3(conf, CommandGene.FloatClass),
        new Divide(conf, CommandGene.FloatClass),
        new Sine(conf, CommandGene.FloatClass),
        new Exp(conf, CommandGene.FloatClass),
        new Pow(conf, CommandGene.FloatClass),
        new Terminal(conf, CommandGene.FloatClass, 2.0d, 10.0d, true),
    }
    };
    // Create genotype with initial population.
    // ----------------------------------------
    GPGenotype result = GPGenotype.randomInitialGenotype(conf, types, argTypes,
        nodeSets,
        20, true);
    result.putVariable(vx);
    conf.putVariable(vx);
    return result;
  }
}
