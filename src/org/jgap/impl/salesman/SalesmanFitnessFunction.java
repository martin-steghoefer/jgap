/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.impl.salesman;

import org.jgap.*;

/**
 * The fitness function to solve the Travelling Salesman problem
 *
 * @author Audrius Meskauskas
 * @since 2.0
 */
public class SalesmanFitnessFunction extends FitnessFunction {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.4 $";

  public final Salesman salesman;

  public SalesmanFitnessFunction(Salesman a_salesman) {
    salesman = a_salesman;
  }

  /**
   * Computes the distance by calling salesman
   * {@link org.jgap.impl.salesman.distance
   * salesman.distance(Gene from, Gene to) }
   * @param a_subject chromosome representing cities
   * @return distance of the journey thru the cities represented in the
   * given chromosome
   *
   * @author Audrius Meskauskas
   * @since 2.0
   */
  protected double evaluate(Chromosome a_subject) {

    double s = 0;
    Gene[] genes = a_subject.getGenes();
    for (int i = 0; i < genes.length - 1; i++) {
      s += salesman.distance(genes[i], genes[i + 1]);
    }
    // add cost of coming back:
    s += salesman.distance(genes[genes.length - 1], genes[0]);

    return Integer.MAX_VALUE / 2 - s;
  }
}
