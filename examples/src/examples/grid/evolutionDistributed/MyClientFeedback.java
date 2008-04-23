/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.grid.evolutionDistributed;

import org.jgap.*;
import org.jgap.distr.grid.*;
import org.apache.log4j.*;

/**
 * Listener for feedback sent to the client. This is a simple sample
 * implementation.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public class MyClientFeedback
    implements IClientFeedback {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  private final static String className = MyClientFeedback.class.getName();

  private static Logger log = Logger.getLogger(className);

  public MyClientFeedback() {
  }

  public void error(String msg, Exception ex) {
    log.error("Error catched on client side: " + msg, ex);
  }

  public void sendingFragmentRequest(JGAPRequest req) {
    log.info("Sending work request " + req.getRID());
  }

  public void receivedFragmentResult(JGAPRequest req, JGAPResult res,
                                     int idx) {
    log.warn("Receiving work (index " + idx + "). First solution: " +
             res.getPopulation().getChromosome(0));
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
