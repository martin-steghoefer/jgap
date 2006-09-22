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

/**
 * Client defining work for the grid and sending it to the JGAPServer.
 *
 * @author Klaus Meffert
 * @since 3.01
 */
public class JGAPClient {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  private final static String className = JGAPClient.class.getName();

  private static Logger log = Logger.getLogger(className);

  private GridNodeClientConfig m_config;

  public JGAPClient(GridNodeClientConfig a_config) {
    m_config = a_config;
  }

  //--------------------------------------------------------------------------

  public class RenderingFeedback
      implements MyClientFeedback {
    public RenderingFeedback() {
    }

    public void error(String msg, Exception ex) {
      System.err.println(msg);
    }

    public void sendingFragmentRequest(MyRequest req) {
      System.err.println("Sending work");
    }

    public void receivedFragmentResult(MyRequest req, MyResult res,
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
  public void doWork(GridNodeClientConfig config,
                     MyRequest req)
      throws Exception {
    RenderingFeedback rfback = new RenderingFeedback();
    MyGAClient rc = new MyGAClient(m_config, rfback, req);
    rc.start();
    rc.join();
  }

  //--------------------------------------------------------------------------

  public static void main(String[] args) {
    try {
      MainCmd.setUpLog4J("client", true);
      GridNodeClientConfig config = new GridNodeClientConfig();
      // Command line options
      Options options = new Options();
      CommandLine cmd = MainCmd.parseCommonOptions(
          options, config, args);
      // Setup and start client
      JGAPClient client = new JGAPClient(config);
      client.doWork();
    } catch (Exception ex) {
      ex.printStackTrace();
      System.exit(1);
    }
  }

  public void doWork()
      throws Exception {
    m_config.setSessionName("JGAP_sample_problem");
    Configuration jgapconfig = new DefaultConfiguration();
    jgapconfig.setEventManager(new EventManager());
    jgapconfig.setPopulationSize(100);
    jgapconfig.setFitnessFunction(new SampleFitnessFunction());
    IChromosome sample = new Chromosome(jgapconfig,
                                        new BooleanGene(jgapconfig), 16);
    jgapconfig.setSampleChromosome(sample);
    // Creating work requests
    MyRequest req = new MyRequest(m_config.getSessionName(), 0, jgapconfig);
    doWork(m_config, req);
  }
}
