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

/**
 * Swaps the genes instead of mutating them. This kind of operator is
 * required by Traveling Salesman Problem.
 *
 * @see J. Grefenstette, R. Gopal, R. Rosmaita, and D. Gucht.
 *  <i>Genetic algorithms for the traveling salesman problem</i>.
 * In Proceedings of the Second International Conference on Genetic Algorithms.
 *  Lawrence Eribaum Associates, Mahwah, NJ, 1985.
 * and also {@link http://ecsl.cs.unr.edu/docs/techreports/gong/node3.html
 * Sushil J. Louis & Gong Li  }
 *
 * @author Audrius Meskauskas
 * @author <font size=-1>Neil Rotstan, Klaus Meffert (reused code
 * from {@link org.jgap.impl.MutationOperator MutationOperator})</font>
 * @since 2.0
 */
public class SwappingMutationOperator extends MutationOperator {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

    /** {@inheritDoc} */
    public SwappingMutationOperator() {
    }

    /** {@inheritDoc} */
    public SwappingMutationOperator
    (IUniversalRateCalculator a_mutationRateCalculator) {
        super(a_mutationRateCalculator);
    }

    /** {@inheritDoc} */
    public SwappingMutationOperator(int a_desiredMutationRate) {
        super(a_desiredMutationRate);
    }

    /** {@inheritDoc} */
    public void operate(final Population a_population,
                        List a_candidateChromosomes) {

      // this was a private variable, now it is local reference.
      final IUniversalRateCalculator m_mutationRateCalc = getMutationRateCalc();

      // If the mutation rate is set to zero and dynamic mutation rate is
      // disabled, then we don't perform any mutation.
      // ----------------------------------------------------------------
      if (m_mutationRate == 0 && m_mutationRateCalc == null) {
        return;
      }
      // Determine the mutation rate. If dynamic rate is enabled, then
      // calculate it based upon the number of genes in the chromosome.
      // Otherwise, go with the mutation rate set upon construction.
      // --------------------------------------------------------------
      int currentRate;
      if (m_mutationRateCalc != null) {
        currentRate = m_mutationRateCalc.calculateCurrentRate();
      }
      else {
        currentRate = m_mutationRate;
      }
      RandomGenerator generator = Genotype.getConfiguration().
       getRandomGenerator();

      // It would be inefficient to create copies of each Chromosome just
      // to decide whether to mutate them. Instead, we only make a copy
      // once we've positively decided to perform a mutation.
      // ----------------------------------------------------------------
      for (int i = 0; i < a_population.size(); i++) {
        Chromosome x = (Chromosome) a_population.getChromosome(i);
        // This returns null if not mutated:
        Chromosome xm = operate(x, currentRate, generator);
        if (xm!=null) a_candidateChromosomes.add( x );
      }
  }

  private Chromosome operate(Chromosome a_x, int a_rate,
   RandomGenerator generator)
  {
      Chromosome chromosome = null;
      // ----------------------------------------
      for (int j = m_startOffset; j < a_x.size(); j++) {
        // Ensure probability of 1/currentRate for applying mutation
        // ---------------------------------------------------------
        if (generator.nextInt(a_rate) == 0) {
          if (chromosome == null)
            chromosome = (Chromosome) a_x.clone();
          Gene[] genes = chromosome.getGenes();
          // swap this gene with the other one now:
          //  mutateGene(genes[j], generator);
          // ------------------------------------
          int other = m_startOffset +
            generator.nextInt( genes.length-m_startOffset );
          Gene t = genes [j];
          genes [j] = genes [other];
          genes [other] = t;
          // Important! The array is modified expecting that the changes
          // will reflect to the chromosome for that getGene() was called.
          // In other words, the code above supposes that the refernce to
          // the original array m_genes in Chromosome was returned.
          //
        }
      }
      return chromosome;
  }

    private int m_startOffset = 1;

    /** Sets a number of genes at the start of chromosome, that are
     * excluded from the swapping. In the Salesman task, the first city
     * in the list should (where the salesman leaves from) probably should
     * not change as it is part of the list. The default value is 1.
     */
    public void setStartOffset (int a_offset)
    {
        m_startOffset = a_offset;
    }

    /** Gets a number of genes at the start of chromosome, that are
     * excluded from the swapping. In the Salesman task, the first city
     * in the list should (where the salesman leaves from) probably should
     * not change as it is part of the list. The default value is 1.
     */
    public int getStartOffset ()
    {
        return m_startOffset;
    }
}
