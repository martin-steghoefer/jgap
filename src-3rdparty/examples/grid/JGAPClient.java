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

import org.homedns.dade.jcgrid.*;
import org.homedns.dade.jcgrid.client.*;
import org.jgap.*;
import org.jgap.event.*;
import org.jgap.impl.*;

public class JGAPClient {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  public JGAPClient() {
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
    MyGAClient rc = new MyGAClient(config, rfback, req);
    rc.start();
    rc.join();
  }

  //--------------------------------------------------------------------------

  public static void main(String[] args) {
    try {
      // Setup and start client
      JGAPClient client = new JGAPClient();
      client.doWork();
    } catch (Exception ex) {
      ex.printStackTrace();
      System.exit(1);
    }
  }

  public void doWork()
      throws Exception {
    GridNodeClientConfig config = new GridNodeClientConfig();
    config.setSessionName("JGAP_sample_problem");
    Configuration jgapconfig = new DefaultConfiguration();
    jgapconfig.setEventManager(new EventManager());
    jgapconfig.setPopulationSize(50);
    jgapconfig.setFitnessFunction(new SampleFitnessFunction());
    IChromosome sample = new Chromosome(jgapconfig,
                                        new BooleanGene(jgapconfig), 16);
    jgapconfig.setSampleChromosome(sample);
    // Creating work requests
    MyRequest req = new MyRequest(config.getSessionName(), 0, jgapconfig);
    doWork(config, req);
  }
}
