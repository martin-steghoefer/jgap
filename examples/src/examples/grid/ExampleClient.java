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
 * Client defining work for the grid and sending it to the JGAPServer.
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
   * Create work requests to be computed by workers. Starts a completely setup
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
    m_gridconfig.setSessionName("JGAP_sample_problem");
    Configuration jgapconfig = new DefaultConfiguration();
    jgapconfig.setEventManager(new EventManager());
    jgapconfig.setPopulationSize(100);
    jgapconfig.setFitnessFunction(new SampleFitnessFunction());
    IChromosome sample = new Chromosome(jgapconfig,
                                        new BooleanGene(jgapconfig), 16);
    jgapconfig.setSampleChromosome(sample);
    // Creating work requests.
    // -----------------------
    MyRequest req = new MyRequest(m_gridconfig.getSessionName(), 0, jgapconfig);
    WorkerFeedback rfback = new WorkerFeedback();
    setClientFeedback(rfback);
    setWorkRequest(req);
    // Start the threaded process.
    // ---------------------------
    start();
    join();
  }

  //--------------------------------------------------------------------------

  public class WorkerFeedback
      implements IClientFeedback {
    public WorkerFeedback() {
    }

    public void error(String msg, Exception ex) {
      System.err.println(msg);
    }

    public void sendingFragmentRequest(JGAPRequest req) {
      System.err.println("Sending work");
    }

    public void receivedFragmentResult(JGAPRequest req, JGAPResult res,
                                       int idx) {
      System.err.println("Receiving work. Solution " + res.getFittest());
    }

    public void beginWork() {
    }

    public void endWork() {
    }

    public void setProgressMaximum(int max) {
    }

    public void setProgressMinimum(int min) {
    }

    public void setProgressValue(int val) {
    }

    public void setRenderingTime(MyRequest req, long dt) {
    }

    public void completeFrame(int idx) {
    }
  }
  //--------------------------------------------------------------------------

  public static void main(String[] args) {
    try {
      MainCmd.setUpLog4J("client", true);
      GridNodeClientConfig config = new GridNodeClientConfig();
      // Command line options.
      // ---------------------
      Options options = new Options();
      CommandLine cmd = MainCmd.parseCommonOptions(
          options, config, args);
      // Setup and start client.
      // -----------------------
      new ExampleClient(config);
    } catch (Exception ex) {
      ex.printStackTrace();
      System.exit(1);
    }
  }
}
