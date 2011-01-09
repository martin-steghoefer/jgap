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

import org.jgap.*;
import org.jgap.gp.*;
import org.jgap.gp.impl.*;
import org.jgap.distr.grid.gp.*;
import org.jgap.distr.grid.*;
import org.apache.log4j.*;

/**
 * Listener for feedback sent to the GP client. This is a simple sample
 * implementation.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public class MyClientFeedback
    implements IClientFeedbackGP {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.7 $";

  private static Logger log = Logger.getLogger(MyClientFeedback.class);

  public MyClientFeedback() {
  }

  public void error(String msg, Exception ex) {
    log.error("Error catched on client side: " + msg, ex);
  }

  public void sendingFragmentRequest(JGAPRequestGP req) {
    log.info("Sending work request " + req.getRID());
  }

  public void receivedFragmentResult(JGAPRequestGP req, JGAPResultGP res,
                                     int idx) {
    // This is just a quick and dirty solution.
    // Can you do it better?
    // ----------------------------------------
    GPPopulation pop = res.getPopulation();
    if (pop == null || pop.isFirstEmpty()) {
      IGPProgram best = res.getFittest();
      log.warn("Receiving work (index " + idx + "). Best solution: " +
               best.getFitnessValue());
      log.warn("Solution: "+best.toStringNorm(0));
      return;
    }
    if (pop == null) {
      log.error("Received empty result/population!");
    }
    else {
      log.warn("Receiving work (index " + idx + "). First solution: " +
               pop.getGPProgram(0));
    }
  }

  public void beginWork() {
    log.warn("Client starts sending work requests");
  }

  public void endWork() {
    log.warn("Your request was processed completely");
  }

  public void info(String a_msg) {
    log.warn(a_msg);
  }

  public void setProgressMaximum(int max) {
  }

  public void setProgressMinimum(int min) {
  }

  public void setProgressValue(int val) {
  }

  public void setRenderingTime(JGAPRequest req, long dt) {
  }

  public void completeFrame(int idx) {
    log.warn("Client notified that unit " + idx + " is finished.");
  }
}
