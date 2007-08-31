/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp.impl;

import java.util.Comparator;
import org.jgap.*;

import org.jgap.gp.*;

/**
 * Simple comparator to allow the sorting of GPProgram lists with the highest
 * fitness value in first place of the list.
 * Usage example:
 *   Arrays.sort(
 *     population.getGPPrograms(),
 *     new GPProgramFitnessComparator() );
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class GPProgramFitnessComparator
    implements Comparator {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.4 $";

  private IGPFitnessEvaluator m_fitnessEvaluator;

  /**
   * Constructs the comparator using the DefaultFitnessEvaluator
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public GPProgramFitnessComparator() {
    this(new DefaultGPFitnessEvaluator());
  }

  /**
   * @param a_evaluator the fitness evaluator to use
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public GPProgramFitnessComparator(IGPFitnessEvaluator a_evaluator) {
    if (a_evaluator == null) {
      throw new IllegalArgumentException("Evaluator must not be null");
    }
    m_fitnessEvaluator = a_evaluator;
  }

  /**
   * Compares two programs by using a FitnessEvaluator.
   *
   * @param a_program1 the first program to compare
   * @param a_program2 the second program to compare
   * @return -1 if a_program1 is fitter than a_program2, 1 if it is the other
   * way round and 0 if both are equal
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public int compare(final Object a_program1, final Object a_program2) {
    IGPProgram progOne = (IGPProgram) a_program1;
    IGPProgram progTwo = (IGPProgram) a_program2;
    if (m_fitnessEvaluator.isFitter(progOne, progTwo)) {
      return -1;
    }
    else if (m_fitnessEvaluator.isFitter(progTwo, progOne)) {
      return 1;
    }
    else {
      return 0;
    }
  }
}
