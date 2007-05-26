package org.jgap.impl.job;

import org.jgap.*;

public class EvolveResult extends JobResult{
  private Population m_pop;

  public EvolveResult() {

  }

  public Population getPopulation() {
    return m_pop;
  }

  public void setPopulation(Population a_pop) {
    m_pop = a_pop;
  }
}
