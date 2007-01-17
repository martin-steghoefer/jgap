/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.grid;

import org.apache.commons.cli.*;
import org.apache.log4j.*;
import org.homedns.dade.jcgrid.client.*;
import org.homedns.dade.jcgrid.cmd.*;
import org.jgap.*;
import org.jgap.event.*;
import org.jgap.impl.*;
import org.jgap.distr.grid.*;
import org.homedns.dade.jcgrid.message.*;

/**
 * A Client defines a problem for the grid and sends it as a work request to
 * the JGAPServer.
 *
 * @author Klaus Meffert
 * @since 3.01
 */
public class ExampleClient
    extends JGAPClient {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  private final static String className = ExampleClient.class.getName();

  private static Logger log = Logger.getLogger(className);

  /**
   * Creates work requests to be computed by workers. Starts a completely setup
   * client to distribute work and receive solutions.
   *
   * @param a_gridconfig GridNodeClientConfig
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.01
   */
  public ExampleClient(GridNodeClientConfig a_gridconfig)
      throws Exception {
    super(a_gridconfig);
    // Create the problem to be solved.
    // --------------------------------
    m_gridconfig.setSessionName("JGAP_sample_problem");
    Configuration jgapconfig = new DefaultConfiguration();
    jgapconfig.setEventManager(new EventManager());
    jgapconfig.setPopulationSize(10);
    jgapconfig.setFitnessFunction(new SampleFitnessFunction());
    IChromosome sample = new Chromosome(jgapconfig,
                                        new BooleanGene(jgapconfig), 16);
    jgapconfig.setSampleChromosome(sample);
    // Setup work request.
    // -------------------
    MyRequest req = new MyRequest(m_gridconfig.getSessionName(), 0, jgapconfig);
    req.setWorkerReturnStrategy(new DefaultWorkerReturnStrategy());
    req.setGenotypeInitializer(new DefaultGenotypeInitializer());
    setWorkRequest(req);
    // Setup the client to produce a work request for each chromosome
    // to get its fitness value computed by a single worker.
    // --------------------------------------------------------------
    setRequestSplitStrategy(new FitnessSplitStrategy(jgapconfig));
    setConfiguration(jgapconfig);
    // Register client feedback listener.
    // ----------------------------------
    IClientFeedback rfback = new MyClientFeedback();
    setClientFeedback(rfback);
    // Start the threaded process.
    // ---------------------------
    start();
    join();
  }

  /**
   * Starts the client. Additional stuff: Sets up logger and parses command line
   * options.
   *
   * @param args String[]
   *
   * @author Klaus Meffert
   * @since 3.01
   */
  public static void main(String[] args) {
    try {
      // Setup logging.
      // --------------
      MainCmd.setUpLog4J("client", true);
      // Command line options.
      // ---------------------
      GridNodeClientConfig config = new GridNodeClientConfig();
      Options options = new Options();
      CommandLine cmd = MainCmd.parseCommonOptions(options, config, args);
      // Setup and start client.
      // -----------------------
      new ExampleClient(config);
    } catch (Exception ex) {
      ex.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * Threaded: Do distributed computation as follows:
   * 1) Randomly initialize population
   * 2)
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void run() {
    try {
      // Start Client.
      // -------------
      GridClient gc = new GridClient();
      gc.setNodeConfig(m_gridconfig);
      gc.start();
      // Initialize population randomly.
      // -------------------------------
      Genotype gen = Genotype.randomInitialGenotype(getConfiguration());
      Population pop = gen.getPopulation();
      try {
        // Do the complete evolution cycle n times.
        // ----------------------------------------
        final int maxEvolutions = 3;
        for (int a = 0; a < maxEvolutions; a++) {
          // Calculate fitness values of chromosomes.
          // Let each worker compute one fitness value.
          // ------------------------------------------
          JGAPRequest[] workList;
          m_workReq.setPopulation(pop);
          workList = m_splitStrategy.split(m_workReq);
          m_clientFeedback.setProgressMaximum(0);
          m_clientFeedback.setProgressMaximum(workList.length - 1);
          m_clientFeedback.beginWork();
          // Send work requests.
          // -------------------
          for (int i = 0; i < workList.length; i++) {
            JGAPRequest req = workList[i];
            m_clientFeedback.sendingFragmentRequest(req);
            gc.send(new GridMessageWorkRequest(req));
            if (this.isInterrupted())
              break;
          }
          // Receive work results.
          // ---------------------
          pop = new Population(getConfiguration(),
                               getConfiguration().getPopulationSize());
          int idx = -1;
          for (int i = 0; i < workList.length; i++) {
            m_clientFeedback.setProgressValue(i + workList.length);
            GridMessageWorkResult gmwr = (GridMessageWorkResult) gc.recv();
            JGAPResult workResult = (JGAPResult) gmwr.getWorkResult();
            idx = workResult.getRID();
            // Assemble results into a single population.
            // ------------------------------------------
            /**@todo consider request id to be able to receive individuals of
             * different populations
             */
            pop.addChromosomes(workResult.getPopulation());
            // Fire listener.
            // --------------
            m_clientFeedback.receivedFragmentResult(workList[idx], workResult,
                idx);
            if (this.isInterrupted()) {
              break;
            }
          }
          if (idx >= 0) {
            // Do the evolution locally.
            // Note: It would be easy to distribute this task, again.
            // ------------------------------------------------------
            gen = new Genotype(getConfiguration(), pop);
            gen.evolve();
            // Get back the evolved population for further evolutions
            // (see beginning of the iteration).
            // ------------------------------------------------------
            pop = gen.getPopulation();
            // Fire listener that one evolution cycle is complete.
            // ---------------------------------------------------
            m_clientFeedback.completeFrame(a);
          }
          else {
            m_clientFeedback.error("Worklist was empty", new RuntimeException(
                "Worklist was empty"));
          }
        }
        IChromosome best = pop.determineFittestChromosome();
        m_clientFeedback.info("Best solution evolved: " + best);
      } finally {
        try {
          gc.stop();
        } catch (Exception ex) {}
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      m_clientFeedback.error("Error while doing the work", ex);
    }
    m_clientFeedback.endWork();
  }

}
