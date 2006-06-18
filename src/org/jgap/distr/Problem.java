/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.distr;

import org.jgap.*;

/**
 * A problem to be solved by a worker.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class Problem {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  private FitnessFunction m_fitFunc;

  private int m_populationSize;

  private Object m_ID;

  private Chromosome[] m_initialChroms;

  public Problem() {
  }

  public Problem(FitnessFunction a_fitFunc, int a_popSize,
                 Chromosome[] a_initialChroms) {
    m_fitFunc = a_fitFunc;
    m_populationSize = a_popSize;
    m_initialChroms = a_initialChroms;
  }

  public void setID(Object a_ID) {
    m_ID = a_ID;
  }

  public Object getID() {
    return m_ID;
  }

  public int getPopulationSize() {
    return m_populationSize;
  }

  public FitnessFunction getFitnessFunction() {
    return m_fitFunc;
  }
}
