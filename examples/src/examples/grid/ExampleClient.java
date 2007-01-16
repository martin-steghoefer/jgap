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
  private final static String CVS_REVISION = "$Revision: 1.6 $";

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
    jgapconfig.setPopulationSize(100);
    jgapconfig.setFitnessFunction(new SampleFitnessFunction());
    IChromosome sample = new Chromosome(jgapconfig,
                                        new BooleanGene(jgapconfig), 16);
    jgapconfig.setSampleChromosome(sample);
    // Setup work request.
    // -------------------
    MyRequest req = new MyRequest(m_gridconfig.getSessionName(), 0, jgapconfig);
    req.setWorkerReturnStrategy(new DefaultWorkerReturnStrategy());
    setWorkRequest(req);
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
}
