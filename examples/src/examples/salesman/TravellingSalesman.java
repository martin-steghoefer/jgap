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
package examples.salesman;

import org.jgap.*;
import org.jgap.impl.IntegerGene;
import org.jgap.impl.StockRandomGenerator;
import org.jgap.impl.salesman.*;
import org.jgap.impl.*;


/**
 * Explains how to use JGap extensions, needed to solve the task group,
 * known as the <i>Problem of the travelling salesman</i>. The extensions are
 * defined in {@link org.jgap.impl.salesman org.jgap.impl.salesman}
 *
 * <font size=-1><p>
 * The traveling salesman problem is the following: given a finite number of
 * 'cities' along with the
 * cost of travel between each pair of them, find the cheapest way of visiting
 * all the cities and returning to your starting point.
 * </font>
 * </p>
 *
 * @see
 *  <ul>
 *   <li>J. Grefenstette, R. Gopal, R. Rosmaita, and D. Gucht.
 *     <i>Genetic algorithms for the traveling salesman problem</i>.
 *     In Proceedings of the Second International Conference on Genetice
 *     Algorithms. Lawrence Eribaum Associates, Mahwah, NJ, 1985.
 *   </li>
 *   <li>
 *    <a href="http://ecsl.cs.unr.edu/docs/techreports/gong/node3.html">
 *      Sushil J. Louis & Gong Li</a> (very clear explanatory material)
 *   </li>
 *   <li>
 *     <a href="http://www.tsp.gatech.edu www.tsp.gatech.edu">
 *        <i>Travelling salesman</i> web site</a>
 *  </li>
 * </ul>
 *
 * This simple test and example shows how to use the Salesman class.
 * The distance between the cities is assumed to be equal
 * to the difference of the assigned numbers. So, the
 * optimal solution is obviously 1 2 3 4 ... n or reverse,
 * but the system does not know this. To get the useful application, you
 * need to override at least the distance function. Also, it is recommended
 * to define a new type of gene, corresponding the data about your "city".
 * For example, it can contain the city X and Y co-ordinates, used by
 * the distance function.
 *
 * @author Audrius Meskauskas
 * @version 1.0
 */

public class TravellingSalesman extends Salesman {

    /** The number of cities to visit, 7 by default. */
    public static final int CITIES = 7;

    /** Create an array of the given number of
     * integer genes. The first gene is always 0, this is
     * a city where the salesman starts the journey
     */
    public Chromosome createSampleChromosome(Object initial_data) {
        Gene [] genes = new Gene [CITIES];
        for (int i = 0; i < genes.length; i++) {
            genes [i] = new IntegerGene(0, CITIES-1);
            genes [i] .setAllele( new Integer (i));
        }

        Chromosome sample = new Chromosome (genes);

        System.out.println("Optimal way "+sample);
        System.out.println("Score "+
         (Integer.MAX_VALUE/2-m_conf.getFitnessFunction()
          .getFitnessValue(sample)));

        RandomGenerator g = new StockRandomGenerator();

        shuffle (genes);

        System.out.println("Sample chromosome "+sample);
        System.out.println("Score "+
         (Integer.MAX_VALUE/2-m_conf.getFitnessFunction()
          .getFitnessValue(sample)));

        return sample;
    }

    /** Distance is equal to the difference between city numbers,
     * except the distance between the last and first cities that
     * is equal to 1. In this way, we ensure that the optimal
     * soultion is 0 1 2 3 .. n - easy to check.
     */
    public double distance(Gene a_from, Gene a_to) {
        IntegerGene a = (IntegerGene) a_from;
        IntegerGene b = (IntegerGene) a_to;

        int A = a.intValue();
        int B = b.intValue();

        if ( A == 0 && B == CITIES-1) return 1;
        if ( B == 0 && A == CITIES-1) return 1;

        return Math.abs( A - B );
    }

    /** Solve a sample task with the number of cities, defined
     * in a CITIES constant. Print the known optimal way,
     * sample chromosome and found solution.
     */
    public static void main(String[] args) {

        try {
            TravellingSalesman t = new TravellingSalesman ();
            Chromosome optimal = t.findOptimalPath (null);
            System.out.println("Solution: ");
            System.out.println(optimal);
            System.out.println("Score "+
             (Integer.MAX_VALUE/2-optimal.getFitnessValue()));
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}