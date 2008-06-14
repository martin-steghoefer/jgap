/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.distr.grid;

import org.jgap.*;
import org.jgap.distr.grid.*;
import org.apache.log4j.*;

/**
 * Empty implementation.
 *
 * @author Klaus Meffert
 * @since 3.3.4
 */
public class DefaultClientFeedback
    implements IClientFeedback {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  private final static String className = DefaultClientFeedback.class.getName();

  private static Logger log = Logger.getLogger(className);

  public DefaultClientFeedback() {
  }

  public void error(String msg, Exception ex) {
  }

  public void sendingFragmentRequest(JGAPRequest req) {
  }

  public void receivedFragmentResult(JGAPRequest req, JGAPResult res,
                                     int idx) {
  }

  public void beginWork() {
  }

  public void endWork() {
  }

  public void info(String a_msg) {
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
  }
}
