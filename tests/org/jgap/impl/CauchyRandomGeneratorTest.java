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

import org.jgap.*;
import junit.framework.*;

/**
 * Tests for CauchyRandomGenerator class
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class CauchyRandomGeneratorTest
    extends TestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.4 $";

  //delta for distinguishing whether a value is to be interpreted as zero
  private static final double DELTA = 0.000001d;

  public CauchyRandomGeneratorTest() {
  }

  public static Test suite() {
    TestSuite suite = new TestSuite(CauchyRandomGeneratorTest.class);
    return suite;
  }

  /**
   * Check if construction and calculation in general possible
   */
  public void testGeneral() {
    RandomGenerator calc = new CauchyRandomGenerator();
    calc.nextInt();
    calc.nextBoolean();
    calc.nextDouble();
    calc.nextFloat();
    calc.nextInt();
    calc.nextLong();
  }

  public void testNextCauchy_0()
      throws Exception {
    final double stdDev = 0.04d;
    CauchyRandomGenerator calc = new CauchyRandomGenerator(0.0d, stdDev);
    calc.nextCauchy();
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testGetCauchyStdDeviation_0() {
    final double stdDev = 0.04d;
    CauchyRandomGenerator calc = new CauchyRandomGenerator(0.0d, stdDev);
    assertEquals(stdDev, calc.getCauchyStandardDeviation(), DELTA);
  }
}
