package org.jgap.impl;

import org.jgap.*;
import java.util.*;

/**
 * This genetic operator performs Gaussian mutation across
 * all genes in a given Chromosome.
 * Taken from JOONEGAP
 *
 * @author Klaus Meffert (modified JOONEGAP source)
 * @since 2.0
 */
public class GaussianMutationOperator
    implements GeneticOperator {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.1 $";

  private final static Random RANDOM = new Random();

  private double m_dDeviation = 0.05;

  /**
   * Constructs a GaussianMutationOperator with a default
   * deviation of 0.05.
   */
  public GaussianMutationOperator() {
  }

  /**
   * Constructs a GaussianMutationOperator with the
   * given deviation.
   */
  public GaussianMutationOperator(double p_dDeviation) {
    m_dDeviation = p_dDeviation;
  }

  public void operate(final Configuration a_activeConfiguration,
                      final Population a_population,
                      List a_candidateChromosomes) {
    // For each Chromosome in the population...
    for (int i = 0; i < a_population.size(); i++) {
      Gene[] genes = a_population.getChromosome(i).getGenes();
      // ...take a copy of it...
      Chromosome copyOfChromosome = (Chromosome) a_population.getChromosome(i).
          clone();
      // ...add it to the candidate pool...
      a_candidateChromosomes.add(copyOfChromosome);
      // ...then Gaussian mutate all its genes...
      genes = copyOfChromosome.getGenes();
      for (int j = 0; j < genes.length; j++) {
        double dValue = ( (Double) genes[j].getAllele()).doubleValue();
        double dNextGaussian = RANDOM.nextGaussian();
        double dDifference = dNextGaussian * m_dDeviation;
        dValue += dDifference;
        genes[j].setAllele(new Double(dValue));
      }
    }
  }
}
