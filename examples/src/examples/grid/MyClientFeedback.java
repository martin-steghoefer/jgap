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

import org.jgap.*;
import org.jgap.distr.grid.*;

/**
 * Listener for feedback sent to the client. This is a simple sample
 * implementation.
 *
 * @author Klaus Meffert
 * @since 3.1
 */
public class MyClientFeedback
    implements IClientFeedback {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.4 $";

  public MyClientFeedback() {
  }

  public void error(String msg, Exception ex) {
    System.err.println(msg);
  }

  public void sendingFragmentRequest(JGAPRequest req) {
    System.out.println("Sending work");
  }

  public void receivedFragmentResult(JGAPRequest req, JGAPResult res,
                                     int idx) {
    System.out.println("Receiving work (index " + idx + "). Solution: " +
                       res.getFittest());
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
