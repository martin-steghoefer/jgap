/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.grid.fitnessDistributed;

import org.homedns.dade.jcgrid.*;
import org.homedns.dade.jcgrid.worker.*;
import org.jgap.distr.grid.*;

/**
 * Listener called on worker events.
 *
 * @author Klaus Meffert
 * @since 3.01
 */
public class MyWorkerFeedback
    implements GridWorkerFeedback {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.4 $";

  private String m_sessionName;

  /**
   * Called when listener is started.
   */
  public void start() {
  }

  public void beginWorkingFor(String a_sessionName, WorkRequest req) {
    m_sessionName = a_sessionName;
    System.out.println("Begin work for request " + req.getRID()
                       + ", session " + m_sessionName);
  }

  public void endWorkingFor(WorkResult res) {
    System.out.println("Result computed (req " + res.getRID() + ", session "
                       + m_sessionName + "): "
                       + ( (JGAPResult) res).getPopulation().getChromosome(0));
  }

  /**
   * Called when listener is stopped.
   */
  public void stop() {
    System.out.println("MyWorkerFeedback: listener stopped (session "
                       + m_sessionName + ")");
  }
}
