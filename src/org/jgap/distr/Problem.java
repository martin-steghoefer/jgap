/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
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
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  private FitnessFunction m_fitFunc;

  private int m_populationSize;

  private Object m_ID;

  private Chromosome[] m_initialChroms;

  public Problem() {
  }

  /**
   * @param a_fitFunc the fitness function to use
   * @param a_popSize the population size to use by/suggest to the worker
   * @param a_initialChroms initial chromosomes to use (e.g. from previous
   * evolutions), or empty
   * @throws IllegalArgumentException
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public Problem(FitnessFunction a_fitFunc, int a_popSize,
                 Chromosome[] a_initialChroms) throws IllegalArgumentException {
    if (a_fitFunc == null) {
      throw new IllegalArgumentException("Fitness function must not be null!");
    }
    if (a_popSize <= 0) {
      throw new IllegalArgumentException("Population size must be greater zero.");
    }
    m_fitFunc = a_fitFunc;
    m_populationSize = a_popSize;
    m_initialChroms = a_initialChroms;
  }

  /**
   * @param a_ID internal ID of the problem, should be unique
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void setID(Object a_ID) {
    m_ID = a_ID;
  }

  /**
   * @return internal ID of the problem, should be unique
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public Object getID() {
    return m_ID;
  }

  /**
   * @return population size to use
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public int getPopulationSize() {
    return m_populationSize;
  }

  /**
   * @return FitnessFunction to use
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public FitnessFunction getFitnessFunction() {
    return m_fitFunc;
  }

  /**
   * @return Chromosome[] initial chromosomes to use, may be empty
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public Chromosome[] getChromosomes() {
    return m_initialChroms;
  }
}
