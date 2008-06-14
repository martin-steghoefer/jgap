/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.distr.grid.common;

import java.util.*;
import org.jgap.distr.*;

/**
 * Holds information about a single result obtained from a worker.
 *
 * @author Klaus Meffert
 * @since 3.3.4
 */
public class ResultData {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  private MasterInfo m_worker;

  private Date m_received;

  private double m_fitness;

  public ResultData() {
  }

  public double getFitness() {
    return m_fitness;
  }

  public Date getReceived() {
    return m_received;
  }

  public MasterInfo getWorker() {
    return m_worker;
  }

  public void setWorker(MasterInfo a_worker) {
    this.m_worker = a_worker;
  }

  public void setReceived(Date a_received) {
    this.m_received = a_received;
  }

  public void setFitness(double a_fitness) {
    this.m_fitness = a_fitness;
  }
}
