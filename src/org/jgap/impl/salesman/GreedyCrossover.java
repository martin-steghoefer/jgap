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

import org.jgap.Gene;
import org.jgap.impl.NumberGene;
import org.jgap.impl.IntegerGene;
import org.jgap.Population;
import java.util.List;
import org.jgap.RandomGenerator;
import org.jgap.Genotype;
import org.jgap.Chromosome;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeSet;
import java.util.StringTokenizer;
import org.jgap.impl.StockRandomGenerator;
import java.util.Iterator;
import org.jgap.GeneticOperator;

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
 * @version 1.0
 */

public class GreedyCrossover implements GeneticOperator {

   /** Switches assertions on. Must be true during tests and debugging. */
   public static boolean ASSERTIONS = true;

    /** Compute the distance between "cities", indicated by these two
     * given genes. The default method expects the genes to be a
     * IntegerGenes's and returns they absolute difference, that
     * makes sense only for tests.
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

       // For each crossover, grab two random chromosomes and do that
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

   public static void main(String[] args) {

     test();
     test();
     test();
   }

   public static void test()
   {
       Chromosome a = new Chromosome(
        mga("1 9 5 3 4 2 0 6 8 7"));
       Chromosome b = new Chromosome(
        mga("1 4 3 2 0 5 6 9 7 8"));

       System.out.println(a);
       System.out.println(b);

       System.out.println();

       new GreedyCrossover(). operate (a, b);

       System.out.println(a);
       System.out.println(b);

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