/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.salesman;

import org.jgap.*;
import org.jgap.impl.*;
import org.jgap.impl.salesman.*;

/**
 * Explains how to use JGAP extensions, needed to solve the task group,
 * known as the <i>Problem of the travelling salesman</i>. The extensions are
 * defined in {@link org.jgap.impl.salesman org.jgap.impl.salesman}
 *
 * <font size=-1><p>
 * The traveling salesman problem is the following: given a finite number of
 * 'cities' along with the cost of travel between each pair of them, find the
 * cheapest way of visiting all the cities and returning to your starting point.
 * </p></font>
 *
 * Also see
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
 *   </li>
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
 * @since 2.0
 */
public class TravellingSalesman
    extends Salesman {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.14 $";

  /** The number of cities to visit*/
  public static final int CITIES = 7;

  public static final int[][] CITYARRAY = new int[][] { {2, 4}, {7, 5}, {7, 11},
      {8, 1}, {1, 6}, {5, 9}, {0, 11}
  };
  /**
   * Create an array of the given number of integer genes. The first gene is
   * always 0, this is the city where the salesman starts the journey.
   *
   * @param a_initial_data ignored
   * @return Chromosome
   *
   * @author Audrius Meskauskas
   * @since 2.0
   */
  public IChromosome createSampleChromosome(Object a_initial_data) {
    try {
      Gene[] genes = new Gene[CITIES];
      for (int i = 0; i < genes.length; i++) {
        genes[i] = new IntegerGene(getConfiguration(), 0, CITIES - 1);
        genes[i].setAllele(new Integer(i));
      }
      IChromosome sample = new Chromosome(getConfiguration(), genes);
      return sample;
    }
    catch (InvalidConfigurationException iex) {
      throw new IllegalStateException(iex.getMessage());
    }
  }

  /**
   * Distance is equal to the difference between city numbers, except the
   * distance between the last and first cities that is equal to 1. In this
   * way, we ensure that the optimal solution is 0 1 2 3 .. n - easy to check.
   *
   * @param a_from first gene, representing a city
   * @param a_to second gene, representing a city
   * @return the distance between two cities represented as genes

   * @author Audrius Meskauskas
   * @since 2.0
   */
  public double distance(Gene a_from, Gene a_to) {
    IntegerGene geneA = (IntegerGene) a_from;
    IntegerGene geneB = (IntegerGene) a_to;
    int a = geneA.intValue();
    int b = geneB.intValue();
    int x1 = CITYARRAY[a][0];
    int y1 = CITYARRAY[a][1];
    int x2 = CITYARRAY[b][0];
    int y2 = CITYARRAY[b][1];
//    if (A == 0 && B == CITIES - 1) {
//      return 1;
//    }
//    if (B == 0 && A == CITIES - 1) {
//      return 1;
//    }
    return Math.sqrt( (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
  }

  /**
   * Solve a sample task with the number of cities, defined in a CITIES
   * constant. Print the known optimal way, sample chromosome and found
   * solution.
   *
   * @param args not relevant here
   *
   * @author Audrius Meskauskas
   * @since 2.0
   */
  public static void main(String[] args) {
    try {
      TravellingSalesman t = new TravellingSalesman();
      IChromosome optimal = t.findOptimalPath(null);
      System.out.println("Solution: ");
      System.out.println(optimal);
      System.out.println("Score " +
                         (Integer.MAX_VALUE / 2 - optimal.getFitnessValue()));
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
