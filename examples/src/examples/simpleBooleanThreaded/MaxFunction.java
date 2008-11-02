/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.simpleBooleanThreaded;

import org.jgap.*;
import org.jgap.impl.*;

/**
 * Fitness function for our example. See evolve() method for details.
 *
 * @author Klaus Meffert
 * @since 3.01
 */
public class MaxFunction
    extends FitnessFunction {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  /**
   * This example implementation calculates the fitness value of Chromosomes
   * using BooleanAllele implementations. Each second bit is expected to be set,
   * each other bit to be not set.
   *
   * @param a_subject the Chromosome to be evaluated
   * @return defect rate of our problem
   *
   * @author Klaus Meffert
   * @since 3.01
   */
  public double evaluate(IChromosome a_subject) {
    int total = 0;
    for (int i = 0; i < a_subject.size(); i++) {
      BooleanGene value = (BooleanGene) a_subject.getGene(a_subject.size() -
          (i + 1));
      if (value.booleanValue()) {
        if (i % 2 == 0) {
          total += 2;
        }
      }
      else {
        if (i % 2 == 1) {
          total += 2;
        }
      }
      if (total < 0) {
        total = 0;
      }
    }
    return total;
  }
}
