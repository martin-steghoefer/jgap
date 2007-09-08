/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.impl;

import java.util.*;
import org.jgap.*;
import junit.framework.*;
import java.util.concurrent.atomic.*;

/**
 * Tests the CauchyRandomGenerator class.
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class CauchyRandomGeneratorTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.11 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(CauchyRandomGeneratorTest.class);
    return suite;
  }

  /**
   * Check if construction and calculation possible in general.
   *
   * @author Klaus Meffert
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

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   */
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

  /**
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testNextInt_0() {
    final double stdDev = 0.04d;
    CauchyRandomGenerator calc = new CauchyRandomGenerator(0.0d, stdDev);
    int i = calc.nextInt(2);
    assertTrue(i < 2 && i >= 0);
    i = calc.nextInt(1);
    assertEquals(0, i);
  }

  /**
   * Tests serializability capabilities.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.01
   */
  public void testSerialize_0() throws Exception {
    /**@todo fix test as Java 5 uses java.util.concurrent.AtomicLong instead
     * of sun.misc.AtomicLong
     */
    CauchyRandomGenerator srg = new CauchyRandomGenerator();
    Random r1 = (Random)privateAccessor.getField(srg,"m_rn");
    AtomicLong seed1 = (AtomicLong)privateAccessor.getField(r1,"seed");
    long curr = System.currentTimeMillis();
    while (curr == System.currentTimeMillis());
    CauchyRandomGenerator srg2 = (CauchyRandomGenerator)doSerialize(srg);
    Random r2 = (Random)privateAccessor.getField(srg2,"m_rn");
    AtomicLong seed2 = (AtomicLong)privateAccessor.getField(r2,"seed");
    assertFalse(seed1.get() == seed2.get());
  }
}
