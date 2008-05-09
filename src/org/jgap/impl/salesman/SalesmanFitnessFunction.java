/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.impl.salesman;

import org.jgap.*;

/**
 * The fitness function to solve the Travelling Salesman problem. The function
 * returned by this method calls {@link org.jgap.impl.salesman.Salesman#distance
 * distance(Object from, Object to) }
 *
 * @author Audrius Meskauskas
 * @since 2.0
 */
public class SalesmanFitnessFunction
    extends FitnessFunction {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.10 $";

  private final Salesman m_salesman;

  public SalesmanFitnessFunction(final Salesman a_salesman) {
    m_salesman = a_salesman;
  }

  /**
   * Computes the distance by calling salesman
   * {@link org.jgap.impl.salesman.Salesman#distance(org.jgap.Gene, org.jgap.Gene) }
   *
   * @param a_subject chromosome representing cities
   * @return distance of the journey thru the cities represented in the
   * given chromosome
   *
   * @author Audrius Meskauskas
   * @since 2.0
   */
  protected double evaluate(final IChromosome a_subject) {
    double s = 0;
    Gene[] genes = a_subject.getGenes();
    for (int i = 0; i < genes.length - 1; i++) {
      s += m_salesman.distance(genes[i], genes[i + 1]);
    }
    // add cost of coming back:
    s += m_salesman.distance(genes[genes.length - 1], genes[0]);
    return Integer.MAX_VALUE / 2 - s;
  }
}
