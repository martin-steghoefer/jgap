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
 *
 * The Greedy Crossover is a specific type of crossover. It can only be is
 * applied if
 * <ul>
 * <li>
 * 1. All genes in the chromosome are different and
 * </li>
 * <li>
 * 2. The set of genes for both chromosomes is identical and only they order
 * in the chromosome can vary.
 * </li>
 * </ul>
 *
 * After the GreedyCrossover, these two conditions always remain true, so
 * it can be applied again and again.
 *
 * The algorithm throws an assertion error if the two initial chromosomes
 * does not satisfy these conditions.
 *
 *
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
  private static final String CVS_REVISION = "$Revision: 1.6 $";

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

   /**
    * Perfroms a greedy crossover for the two given chromosoms.
    * The explaining error message is written to System.err.
    *
    * @param a_firstMate the first chromosome to crossover on
    * @param a_secondMate the second chromosome to crossover on
    * @throws Error if the gene set in the chromosomes is not identical.
    *
    * @author Audrius Meskauskas
    * @since 2.1
    */
   public void operate (Chromosome a_firstMate, Chromosome a_secondMate) throws
        Error {

        Gene[] g1 = a_firstMate.getGenes();
        Gene[] g2 = a_secondMate.getGenes();

        Gene [] c1, c2;

        try {
         c1 = operate (g1, g2);
         c2 = operate (g2, g1);
         a_firstMate.setGenes(c1);
         a_secondMate.setGenes(c2);
        }
        catch ( Error err )
        {
          throw new Error("Error occured while operating on:"
                          + a_firstMate + " and "
                          + a_secondMate
                          + ". First " + m_startOffset + " genes were excluded " +
                          "from crossover. Error message: "
                          + err.getMessage());
        }

    }

   Gene [] operate(Gene[] g1, Gene[] g2)
   {
       int n = g1.length;

       LinkedList out = new LinkedList();
       TreeSet not_picked = new TreeSet();

       out.add(g1[m_startOffset]);
       for (int j = m_startOffset+1; j < n; j++) { // g[m_startOffset] picked
           if (ASSERTIONS && not_picked.contains( g1[j] ) )
            throw new Error("All genes must be different for "+
            getClass().getName()+". The gene "+g1[j]+"["+j+"] occurs more "+
            "than once in one of the chromosomes. ");
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
               throw new Error("Chromosome gene sets must be identical."
                               + " First Chrom: " + new Chromosome(g1)
                               + ". Second Chrom: " + new Chromosome(g2));
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

       if (ASSERTIONS)
       {
           if (out.size()!=g.length-m_startOffset)
            throw new Error("Unexpected internal error. "+
            "These two must be equal: "+out.size()+
            " and "+(g.length-m_startOffset)+", g.length "+
            g.length+", start offset "+m_startOffset);
       }

       for (int i = m_startOffset; i < g.length; i++) {
           g [i] = (Gene) gi.next();
       }

       return g;

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
