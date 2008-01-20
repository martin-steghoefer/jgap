/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap;

import java.io.*;
import java.util.*;

/**
 * A GeneticOperator represents an operation that takes place on
 * a population of Chromosomes during the evolution process. Examples
 * of genetic operators include reproduction, crossover, and mutation.
 * This interface contains only one method - operate() - which is responsible
 * for performing the genetic operation on the current population of
 * Chromosomes.
 *
 * @author Neil Rotstan
 * @author Klaus Meffert
 * @since 1.0
 */
public interface GeneticOperator
    extends Serializable {

  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.16 $";

  /**
   * The operate method will be invoked on each of the genetic operators
   * referenced by the current Configuration object during the evolution
   * phase. Operators are given an opportunity to run in the order that
   * they are added to the Configuration. Implementations of this method
   * may reference the population of Chromosomes as it was at the beginning
   * of the evolutionary phase and/or they may instead reference the
   * candidate Chromosomes, which are the results of prior genetic operators.
   * In either case, only Chromosomes added to the list of candidate
   * chromosomes will be considered for natural selection.
   *
   * The parameters a_population and a_candidateChromosomes may refer to the same
   * list of chromosomes for performance issues. Thus would mean an in-place
   * modification. In ealier JGAP versions it was suggested never modifying the
   * input population. Please refer to implementations delivered with JGAP to
   * get a picture of the way non-susceptible in-place modifications are
   * possible. If wrongly done, ConcurrentModificationException could be risen
   * when accessing the population by an iterator in a GeneticOperator.
   * Or, if population.getChromosomes().size() was used inside a loop where
   * chromosomes were added to the input population this could lead to an
   * infinite loop in worst case.
   *
   * @param a_population the population of chromosomes from the current
   * evolution prior to exposure to any genetic operators. Chromosomes in this
   * array should not be modified. Please, notice, that the call in
   * Genotype.evolve() to the implementations of GeneticOperator overgoes this
   * due to performance issues
   * @param a_candidateChromosomes the pool of chromosomes that have been
   * selected for the next evolved population
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 2.0 (earlier versions referenced the Configuration object)
   */
  public void operate(final Population a_population,
                      final List a_candidateChromosomes);
}
