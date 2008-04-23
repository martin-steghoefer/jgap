/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.config;

import org.jgap.*;

/**
 * Sample Fitness function for the MaximizingFunction problem.
 */
public class MaximizingFunctionFitnessFunction
    extends FitnessFunction {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.5 $";

  /**
   * Determine the fitness of the given Chromosome instance. The higher the
   * return value, the more fit the instance. This method should always
   * return the same fitness value for two equivalent Chromosome instances.
   * @author Siddhartha Azad.
   * @param a_chromosome the Chromosome instance to evaluate
   * @return a positive integer reflecting the fitness rating of the given
   * Chromosome
   */
  public double evaluate(IChromosome a_chromosome) {
    int numGenes = a_chromosome.size();
    if (numGenes != 3) {
      throw new IllegalArgumentException("Chromosome for " +
                                         "MaximizingFunction must have "
                                         + "exactly 3 genes.");
    }
    Integer aVal = (Integer) a_chromosome.getGene(0).getAllele();
    Integer bVal = (Integer) a_chromosome.getGene(1).getAllele();
    Integer cVal = (Integer) a_chromosome.getGene(2).getAllele();
    return (aVal.intValue() - bVal.intValue() + cVal.intValue());
  }
}
