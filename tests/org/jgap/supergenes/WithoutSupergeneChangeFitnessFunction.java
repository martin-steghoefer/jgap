/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.supergenes;

import org.jgap.*;
import org.jgap.impl.IntegerGene;

/**
 * Fitness function for test implementations without using supergenes.
 *
 * @author Neil Rotstan, Klaus Meffert
 * @author Audrius Meskauskas (subsequent adaptation)
 */
class WithoutSupergeneChangeFitnessFunction
    extends SupergeneChangeFitnessFunction {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  public WithoutSupergeneChangeFitnessFunction(int a_targetAmount) {
    super(a_targetAmount);
  }

  public Gene getResponsibleGene(Chromosome a_chromosome, int a_code) {
    return a_chromosome.getGene(a_code);
  }

  /**
   * Additionall check that the number of nickels and pennies should
   * be both even or odd.
   */
  public double evaluate(Chromosome a_subject) {
    IntegerGene nickels =
        (IntegerGene) a_subject.getGene(SupergeneTest.NICKELS);
    IntegerGene pennies =
        (IntegerGene) a_subject.getGene(SupergeneTest.PENNIES);
    boolean valid = nickels.intValue() % 2 == pennies.intValue() % 2;
    // valid = true; // uncomment for testing without the condition above
    double r;
    if (!valid) {
      r = 0;
    }
    else {
      r = super.evaluate(a_subject);
    }
    return r;
  }
}
