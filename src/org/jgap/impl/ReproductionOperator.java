/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.impl;

import java.util.*;
import org.jgap.*;

/**@todo this entity may be obsolete now!*/
/**
 * The reproduction operator loops through each of the Chromosomes in the
 * population and adds them to the list of candidate chromosomes. This
 * guarantees that each Chromosome in the current population remains a
 * candidate for selection for the next population. Note that if a
 * reproduction operator is not included in the list of configured genetic
 * operators, then Chromosomes in the genotype population may not become
 * candidates for natural selection.
 *
 * @author Neil Rotstan
 * @author Klaus Meffert
 * @since 1.0
 * @deprecated No need to use it anymore!
 */
public class ReproductionOperator
    implements GeneticOperator {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.8 $";

  /**
   * The operate method will be invoked on each of the genetic operators
   * referenced by the current Configuration object during the evolution
   * phase. Operators are given an opportunity to run in the order that
   * they are added to the Configuration. Implementations of this method
   * may reference the population of Chromosomes as it was at the beginning
   * of the evolutionary phase and/or they may instead reference the
   * candidate Chromosomes, which are the results of prior genetic operators.
   * In either case, only Chromosomes added to the list of candidate
   * chromosomes will be considered for natural selection. Implementations
   * should never modify the original population, but should first make copies
   * of the Chromosomes selected for modification and operate upon the copies.
   *
   * @param a_population The population of chromosomes from the current
   *                     evolution prior to exposure to any genetic operators.
   *                     Chromosomes in this array should never be modified.
   * @param a_candidateChromosomes The pool of chromosomes that are candidates
   *                               for the next evolved population. Only these
   *                               chromosomes will go to the natural
   *                               phase, so it's important to add any
   *                               modified copies of Chromosomes to this
   *                               list if it's desired for them to be
   *                               considered for natural selection.
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 1.0
   */
  public void operate(final Population a_population,
                      final List a_candidateChromosomes) {
    // Loop over the chromosomes in the population and add each one to
    // the candidate chromosomes pool so that they'll be considered for
    // the next phase of evolution. This is technically a no-no, but
    // refraining from making copies here of every Chromosome will save
    // significant time and memory.
    // ----------------------------------------------------------------
    a_candidateChromosomes.addAll(a_population.getChromosomes());
  }
}
