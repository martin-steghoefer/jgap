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

import java.util.*;

/**
 * Interface for a constraint checker that determines whether a genetic
 * operation should be executed for a given list of chromosomes. it is possible
 * considering the caller. With that one could state that the two chromosome
 * with classes ChromosomeClass1 and ChromosomeClass2 should not be considered
 * for CrossoverOperator but for MutationOperator.
 *
 * @author Klaus Meffert
 * @since 2.6
 */
public interface IGeneticOperatorConstraint {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.2 $";

  boolean isValid(Population a_pop, List a_chromosomes,
                  GeneticOperator a_caller);
}
