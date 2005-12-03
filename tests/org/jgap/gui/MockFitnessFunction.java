/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gui;

import org.jgap.*;

/**
 * Sample Fitness function Mocking fitness functions.
 *
 * @author Siddhartha Azad
 * @since 2.3
 */
public class MockFitnessFunction
    extends FitnessFunction {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  /**
   * This is really a placeholder fitness function required only so that
   * we can tests certain parts of the system without having to specify
   * a real fitness function.
   * In the real world this would not be a good fitness function for any
   * application, since it simply returns a constant value.
   * @param a_subject The Chromosome instance to evaluate.
   * @return A positive integer reflecting the fitness rating of the given
   * Chromosome.
   *
   * @author Siddhartha Azad
   * @since 2.3
   */
  public double evaluate(Chromosome a_subject) {
    return 100;
  }
}
