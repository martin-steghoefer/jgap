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

import org.jgap.distr.grid.*;
import org.jgap.*;
import org.jgap.impl.*;
import org.jgap.event.*;
import org.homedns.dade.jcgrid.client.*;

/**
 * Main configuration for defining the problem and the way it is solved in the
 * grid. Thus, the most important class in a JGAP Grid!
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public class GridConfiguration
    extends GridConfigurationBase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  public void initialize(GridNodeClientConfig gridconfig)
      throws Exception {
    // Create the problem to be solved.
    // --------------------------------
    gridconfig.setSessionName("JGAP_fitness_distributed");
    Configuration jgapconfig = new DefaultConfiguration();
    jgapconfig.setEventManager(new EventManager());
    jgapconfig.setPopulationSize(10);
    jgapconfig.setKeepPopulationSizeConstant(true);
    jgapconfig.setFitnessFunction(new SampleFitnessFunction());
    IChromosome sample = new Chromosome(jgapconfig,
                                        new BooleanGene(jgapconfig), 16);
    jgapconfig.setSampleChromosome(sample);
    // Setup parameters.
    // -----------------
    setWorkerReturnStrategy(new MyWorkerReturnStrategy());
    // No initialization of Genotype on behalf of workers.
    // ---------------------------------------------------
    setGenotypeInitializer(null);
    // Evolution takes place on client only!
    // -------------------------------------
    setWorkerEvolveStrategy(null);
    // Setup the client to produce a work request for each chromosome
    // to get its fitness value computed by a single worker.
    // --------------------------------------------------------------
    setRequestSplitStrategy(new MyRequestSplitStrategy(jgapconfig));
    setConfiguration(jgapconfig);
    // Evolution takes place on client.
    // --------------------------------
    setClientEvolveStrategy(new ClientEvolveStrategy());
    // Register client feedback listener.
    // ----------------------------------
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
}
