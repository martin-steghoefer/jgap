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
import org.homedns.dade.jcgrid.worker.*;

/**
 * Listener called on worker events.
 *
 * @author Klaus Meffert
 * @since 3.01
 */
public class MyWorkerFeedback
    implements GridWorkerFeedback {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  public void start() {
  }

  public void beginWorkingFor(String sessionName, WorkRequest req) {
    System.out.println("Begin work for session " + sessionName);
  }

  public void endWorkingFor(WorkResult res) {
    System.out.println("Result computed: " + ( (MyResult) res).getFittest());
  }

  public void stop() {
  }
}
