/*
 * This file is part of JGAP.
 *
 * JGAP is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * JGAP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser Public License
 * along with JGAP; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package org.jgap.impl.salesman;

import org.jgap.IUniversalRateCalculator;
import org.jgap.Population;
import org.jgap.RandomGenerator;
import org.jgap.Chromosome;
import org.jgap.Genotype;
import org.jgap.Gene;

import org.jgap.impl.MutationOperator;
import org.jgap.impl.IntegerGene;
import org.jgap.impl.StockRandomGenerator;

import java.util.StringTokenizer;
import java.util.List;

/**
 * Swaps the genes instead of mutating them. This kind of operator is
 * required by Traveling Salesman Problem.
 * @author Audrius Meskauskas
 * @author <font size=-1>Neil Rotstan, Klaus Meffert (reused code
 * from {@link org.jgap.impl.MutationOperator MutationOperator})</font>
 *
 * @see J. Grefenstette, R. Gopal, R. Rosmaita, and D. Gucht.
 *  <i>Genetic algorithms for the traveling salesman problem</i>.
 * In Proceedings of the Second International Conference on Genetic Algorithms.
 *  Lawrence Eribaum Associates, Mahwah, NJ, 1985.
 * and also {@link http://ecsl.cs.unr.edu/docs/techreports/gong/node3.html
 * Sushil J. Louis & Gong Li  }
 *
 * @version 1.0
 */

public class SwappingMutationOperator extends MutationOperator {

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

    public static void main(String[] args) {
      test();
    }

    public static void test()
    {
        Chromosome a = new Chromosome(
         mga("1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20"));


        System.out.println(a);

        new SwappingMutationOperator().
         operate (a, 1, new StockRandomGenerator());

        System.out.println(a);

        System.out.println("-------");

    }

    static IntegerGene [] mga (String s)
    {
        StringTokenizer st = new StringTokenizer (s);
        IntegerGene [] g =
         new IntegerGene [ st.countTokens() ];

        for (int i = 0; i < g.length; i++) {
            IntegerGene ig = new IntegerGene();
            ig.setAllele( new Integer (
              Integer.parseInt( st.nextToken() ) ) );
            g [i] = ig;
        }

        return g;
    }




}