/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
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
import org.jgap.gp.impl.GPGenotype;
import java.util.Random;
import org.jgap.gp.terminal.Terminal;

/**
 * Sample implementation of a strategy for evolving a generation on the client.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public class ClientEvolveStrategy
    extends GPProblem
    implements IClientEvolveStrategyGP {
  /** String containing the CVS revision. Read out via reflection!*/
  public final static String CVS_REVISION = "$Revision: 1.1 $";

  private GPConfiguration m_config;

  private IClientFeedbackGP m_clientFeedback;

  private final int m_maxEvolutions = 3;

  private GPPopulation m_pop;

  private static Float[] x = new Float[20];

  private static float[] y = new float[20];

  public ClientEvolveStrategy(GPConfiguration a_conf)
      throws InvalidConfigurationException {
    super(a_conf);
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
    m_clientFeedback = a_clientFeedback;
    m_config = a_config;
    // Start with an empty population.
    // ------------------------------
    m_pop = new GPPopulation(m_config, m_config.getPopulationSize());
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
    IGPProgram fittest = m_pop.determineFittestProgram();
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
    IGPProgram best = m_pop.determineFittestProgram();
    m_clientFeedback.info("Best solution evolved: " + best);
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
  public void resultReceived(JGAPResultGP a_result)
      throws Exception {
/**@todo*/
//    m_pop.addChromosomes(a_result.getPopulation());
  }

  public GPGenotype create()
      throws InvalidConfigurationException {
    GPConfiguration conf = getGPConfiguration();
    Class[] types = {
        CommandGene.FloatClass};
    Class[][] argTypes = { {}
    };
    CommandGene[][] nodeSets = { {
        Variable.create(conf, "X", CommandGene.FloatClass),
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
    Random random = new Random();
    // Randomly initialize function data (X-Y table) for x^4+x^3+x^2-x
    // ---------------------------------------------------------------
    for (int i = 0; i < 20; i++) {
      float f = 8.0f * (random.nextFloat() - 0.3f);
      x[i] = new Float(f);
      y[i] = f * f * f * f + f * f * f + f * f - f;
      System.out.println(i + ") " + x[i] + "   " + y[i]);
    }
    // Create genotype with initial population.
    // ----------------------------------------
    return GPGenotype.randomInitialGenotype(conf, types, argTypes, nodeSets,
        20, true);
  }

}
