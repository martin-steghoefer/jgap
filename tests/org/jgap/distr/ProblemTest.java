/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.distr;

import org.jgap.*;
import org.jgap.impl.*;

import junit.framework.*;

/**
 * Tests the Problem class.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class ProblemTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.2 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(ProblemTest.class);
    return suite;
  }

  /**
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testConstruct_0() {
    try {
      new Problem(null, 3, null);
      fail();
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testConstruct_1() {
    try {
      new Problem(new StaticFitnessFunction(4.5d), 0, null);
      fail();
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testConstruct_2() {
    try {
      new Problem(new StaticFitnessFunction(4.5d), -1, null);
      fail();
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testConstruct_3() throws Exception {
    FitnessFunction ff = new StaticFitnessFunction(4.5d);
    Chromosome c = new Chromosome(conf);
    Chromosome[] chroms = new Chromosome[]{c};
    Problem p = new Problem(ff, 23, chroms);
    assertEquals(23, p.getPopulationSize());
    assertSame(ff, p.getFitnessFunction());
    assertSame(chroms, p.getChromosomes());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testGetID_0() throws Exception {
    Problem p = new Problem();
    String s = "2aXh-";
    p.setID(s);
    assertSame(s, p.getID());
  }
}
