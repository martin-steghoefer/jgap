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
 * Greedy crossover can be best explained in the terms of the
 * Traveling Salesman Problem:
 *
 * The algorithm selects the first city of one parent, compares the cities
 * leaving that city in both parents, and chooses the closer one to extend
 * the tour. If one city has already appeared in the tour, we choose the
 * other city. If both cities have already appeared, we randomly select a
 * non-selected city.
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
 * from {@link org.jgap.impl.CrossoverOperator CrossoverOperator})</font>
 * @since 2.0
 */
public class GreedyCrossover implements GeneticOperator {

  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.3 $";

   /** Switches assertions on. Must be true during tests and debugging. */
   public static boolean ASSERTIONS = true;

    /** Compute the distance between "cities", indicated by these two
     * given genes. The default method expects the genes to be a
     * IntegerGenes's and returns they absolute difference, that
     * makes sense only for tests.
     *
     * @param a_from Object
     * @param a_to Object
     * @return double
     */
    public double distance(Object a_from, Object a_to)
     {
         IntegerGene from = (IntegerGene) a_from;
         IntegerGene to   = (IntegerGene) a_to;

         return Math.abs( to.intValue() - from.intValue() );
     }

    public void operate(final Population a_population,
                         final List a_candidateChromosomes) {
       int numCrossovers = a_population.size() / 2;

       RandomGenerator generator
         = Genotype.getConfiguration().getRandomGenerator();

       // For each crossover, grab two random chromosomes and do what
       // Grefenstette et al says
       // --------------------------------------------------------------
       for (int i = 0; i < numCrossovers; i++) {

         Chromosome firstMate = (Chromosome)
             a_population.getChromosome(generator.
             nextInt(a_population.size())).clone();

         Chromosome secondMate = (Chromosome)
             a_population.getChromosome(generator.
             nextInt(a_population.size())).clone();

         operate(firstMate, secondMate);

         // Add the modified chromosomes to the candidate pool so that
         // they'll be considered for natural selection during the next
         // phase of evolution.
         // -----------------------------------------------------------
         a_candidateChromosomes.add(firstMate);
         a_candidateChromosomes.add(secondMate);
       }
     }

    private void operate (Chromosome firstMate, Chromosome secondMate) throws
        Error {
        Gene[] g1 = firstMate.getGenes();
        Gene[] g2 = secondMate.getGenes();

        operate (firstMate,  g1, g2);
        operate (secondMate, g2, g1);
    }

   void operate(Chromosome mate, Gene[] g1, Gene[] g2)
   {
       int n = g1.length;

       LinkedList out = new LinkedList();
       TreeSet not_picked = new TreeSet();

       out.add(g1[m_startOffset]);
       for (int j = m_startOffset+1; j < n; j++) { // g[m_startOffset] picked
           not_picked.add( g1 [j] );
       }

       if (ASSERTIONS)
       {
           if (g1.length!=g2.length)
            throw new Error ("Chromosome sizes must be equal");

           for (int j = m_startOffset; j < n; j++)
            if ( !not_picked.contains( g2 [j] ) )
             if ( ! g1[m_startOffset].equals( g2[j] ) )
             {
               System.err.println( new Chromosome (g1) );
               System.err.println( new Chromosome (g2) );
               throw new Error("Chromosome gene sets must be identical");
             }
       }

       while ( not_picked.size() > 1 )
       {
           Gene last = (Gene) out.getLast();
           Gene n1 = findNext (g1, last);
           Gene n2 = findNext (g2, last);

           Gene picked, other;

           boolean pick1;

           if ( n1 == null ) pick1 = false;
            else
           if ( n2 == null ) pick1 = true;
            else
           pick1 = distance ( last, n1 ) < distance ( last, n2 );

           if ( pick1 )
           {
               picked = n1;
               other  = n2;
           }
           else
           {
               picked = n2;
               other  = n1;
           }

           if (out.contains(picked)) picked = other;
           if (picked==null || out /* still */ .contains(picked) )
           {
              // select a non-selected // it is not random
              picked = (Gene) not_picked.first();
           };

            out.add(picked);
            not_picked.remove(picked);
       }

       if (ASSERTIONS && not_picked.size()!=1) throw new Error();

       out.add( not_picked.last());

       Gene [] g = new Gene [ n ];
       Iterator gi = out.iterator();

       for (int i = 0; i < m_startOffset; i++) {
           g [i] = g1 [i];
       }

       for (int i = m_startOffset; i < g.length; i++) {
           g [i] = (Gene) gi.next();
       }

       mate.setGenes( g );

   }

   Gene findNext ( Gene [] g, Gene x)
       {
           for (int i = m_startOffset; i < g.length-1; i++) {
               if ( g[i].equals(x) )
                return g[i+1];
           }
           return null;
       }

   private int m_startOffset = 1;

   /** Sets a number of genes at the start of chromosome, that are
    * excluded from the swapping. In the Salesman task, the first city
    * in the list should (where the salesman leaves from) probably should
    * not change as it is part of the list. The default value is 1.
    * @param a_offset int
    */
   public void setStartOffset (int a_offset)
   {
       m_startOffset = a_offset;
   }

   /** Gets a number of genes at the start of chromosome, that are
    * excluded from the swapping. In the Salesman task, the first city
    * in the list should (where the salesman leaves from) probably should
    * not change as it is part of the list. The default value is 1.
    * @return the offset
    */
   public int getStartOffset ()
   {
       return m_startOffset;
   }

}
