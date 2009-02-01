/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.impl.salesman;

import org.jgap.*;
import org.jgap.impl.*;

import junit.framework.*;

/**
 * Test JGAP's travelling salesman implementation.
 *
 * @author Audrius Meskauskas
 * @author Klaus Meffert
 * @since 2.0
 */
public class TravellingSalesmanTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.17 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(TravellingSalesmanTest.class);
    return suite;
  }

  public void setUp() {
    super.setUp();
    Configuration.reset();
  }

  public void tearDown()
      throws Exception {
    super.tearDown();
  }

  /**
   * @throws Exception
   *
   * @author Audrius Meskauskas
   * @since 2.0
   */
  public void testSampleTravellingSalesmanApp()
      throws Exception {
    // With 7 cities, should find the best solution with score 7
    int oks = 0;
    for (int i = 0; i < 7; i++) {
      TravellingSalesmanForTesting t = new TravellingSalesmanForTesting();
      IChromosome optimal = t.findOptimalPath(null);
      if (Integer.MAX_VALUE / 2 - optimal.getFitnessValue() <= 7) {
        oks++;
      }
      Configuration.reset();
    }
    if (oks < 6) {
      fail("Less than 6 cities computed correctly!");
    }
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testSetStartOffset_0()
      throws Exception {
    TravellingSalesmanForTesting t = new TravellingSalesmanForTesting();
    assertEquals(1, t.getStartOffset());
    t.setStartOffset(47);
    assertEquals(47, t.getStartOffset());
  }

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
   * </p></font>
   *
   * See
   *  <ul>
   *   <li>J. Grefenstette, R. Gopal, R. Rosmaita, and D. Gucht.
   *     <i>Genetic algorithms for the traveling salesman problem</i>.
   *     In Proceedings of the Second International Conference on Genetic
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
  public class TravellingSalesmanForTesting
      extends Salesman {
    /** The number of cities to visit, 7 by default. */
    public static final int CITIES = 7;

    /**
     * Create an array of the given number of integer genes. The first gene is
     * always 0, this is a city where the salesman starts the journey.
     *
     * @param a_initial_data not needed here
     * @return new chromosome
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
      } catch (InvalidConfigurationException iex) {
        throw new IllegalStateException(iex.getMessage());
      }
    }

    /**
     * Distance is equal to the difference between city numbers,
     * except the distance between the last and first cities that
     * is equal to 1. In this way, we ensure that the optimal
     * soultion is 0 1 2 3 .. n - easy to check.
     * @param a_from Gene
     * @param a_to Gene
     * @return distance betwen cities
     */
    public double distance(Gene a_from, Gene a_to) {
      IntegerGene a = (IntegerGene) a_from;
      IntegerGene b = (IntegerGene) a_to;
      int A = a.intValue();
      int B = b.intValue();
      if (A == 0 && B == CITIES - 1) {
        return 1;
      }
      if (B == 0 && A == CITIES - 1) {
        return 1;
      }
      return Math.abs(A - B);
    }
  }
}
