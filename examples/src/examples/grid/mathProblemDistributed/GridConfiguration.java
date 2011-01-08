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
import org.jgap.distr.grid.gp.*;
import org.jgap.event.*;
import org.jgap.gp.impl.*;
import org.jgap.gp.terminal.*;
import org.jgap.impl.*;

/**
 * Main configuration for defining the problem and the way it is solved in the
 * grid. Thus, the most important class in a JGAP Grid!
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public class GridConfiguration
    extends GridConfigurationGPBase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.7 $";

  private Variable m_var;

  public GridConfiguration() {
    super();
  }

  public void initialize(GridNodeClientConfig a_gridconfig)
      throws Exception {
    // Create the problem to be solved.
    // --------------------------------
    if (a_gridconfig != null) {
      a_gridconfig.setSessionName("JGAP_mathprob_distributed");
    }
    GPConfiguration jgapconfig = new GPConfiguration();
    jgapconfig.setEventManager(new EventManager());
    jgapconfig.setPopulationSize(500);
    jgapconfig.setKeepPopulationSizeConstant(true);
    jgapconfig.setGPFitnessEvaluator(new DeltaGPFitnessEvaluator());
    IChromosome sample = new Chromosome(jgapconfig,
                                        new BooleanGene(jgapconfig), 16);
    jgapconfig.setSampleChromosome(sample);
    // Setup parameters.
    // -----------------
    setWorkerReturnStrategy(new MyWorkerReturnStrategy());
    // How to initialize the Genotype on behalf of the workers.
    // --------------------------------------------------------
    setGenotypeInitializer(new MyGenotypeInitializer());
    // How to evolve on the worker side.
    // ---------------------------------
    setWorkerEvolveStrategy(new MyEvolveStrategy());
    // Setup the client to produce a work request for each chromosome
    // to get its fitness value computed by a single worker.
    // --------------------------------------------------------------
    setRequestSplitStrategy(new MyRequestSplitStrategy(jgapconfig));
    setConfiguration(jgapconfig);
    // Set the evolution process (but evolve on workers).
    // --------------------------------------------------
    setClientEvolveStrategy(new ClientEvolveStrategy(this));
    jgapconfig.setFitnessFunction(new SampleFitnessFunction());
    // Optional: Register client feedback listener.
    // --------------------------------------------
    setClientFeedback(new MyClientFeedback());
  }

  public void validate()
      throws Exception {
    if (getRequestSplitStrategy() == null) {
      throw new RuntimeException("Please set the request split strategy first!");
    }
    if (getConfiguration() == null) {
      throw new RuntimeException("Please set the configuration first!");
    }
  }

  public Variable getVariable() {
    return m_var;
  }

  public void setVariable(Variable a_var) {
    m_var = a_var;
  }
}
