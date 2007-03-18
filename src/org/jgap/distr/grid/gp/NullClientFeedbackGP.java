/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.distr.grid.gp;

/**
 * Null implementation of client feedback for GP. Does exactly nothing.
 * Will be used in case no client feedback is registered with the grid
 * configuration.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public class NullClientFeedbackGP
    implements IClientFeedbackGP {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  public void setProgressMinimum(int min) {
    // Do nothing.
  }

  public void setProgressMaximum(int max) {
    // Do nothing.
  }

  public void setProgressValue(int val) {
    // Do nothing.
  }

  public void beginWork() {
    // Do nothing.
  }

  public void sendingFragmentRequest(JGAPRequestGP req) {
    // Do nothing.
  }

  public void receivedFragmentResult(JGAPRequestGP req, JGAPResultGP res, int idx) {
    // Do nothing.
  }

  public void endWork() {
    // Do nothing.
  }

  public void completeFrame(int idx) {
    // Do nothing.
  }

  public void error(String msg, Exception ex) {
    // Do nothing.
  }

  public void info(String msg) {
    // Do nothing.
  }
}
