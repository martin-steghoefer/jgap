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

import java.util.concurrent.atomic.*;

import org.jgap.*;

import junit.framework.*;

/**
 * Tests the StockRandomGenerator class.
 *
 * @author Klaus Meffert
 * @since 2.2
 */
public class StockRandomGeneratorTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.9 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(StockRandomGeneratorTest.class);
    return suite;
  }

  /**
   * Check if construction and calculation in general possible
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testGeneral() {
    StockRandomGenerator calc = new StockRandomGenerator();
    calc.nextInt();
    calc.nextBoolean();
    calc.nextDouble();
    calc.nextFloat();
    calc.nextInt();
    calc.nextLong();
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testConstruct_0() {
    StockRandomGenerator calc = new StockRandomGenerator();
    int i = calc.nextInt();
    if (Math.abs(i) < DELTA) {
      i = calc.nextInt();
      if (Math.abs(i) < DELTA) {
        i = calc.nextInt();
        if (Math.abs(i) < DELTA) {
          fail();
        }
      }
    }
  }

  /**
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testNextInt_0()
      throws Exception {
    StockRandomGenerator calc = new StockRandomGenerator();
    int res;
    for (int i = 0; i < 100; i++) {
      res = calc.nextInt(5);
      assertTrue(res < 5);
      assertTrue(res >= 0);
    }
  }

  /**
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testNextInt_1()
      throws Exception {
    StockRandomGenerator calc = new StockRandomGenerator();
    int res;
    int resOli = 0;
    for (int i = 0; i < 100; i++) {
      res = calc.nextInt();
      if (i > 0) {
        if (resOli == res) {
          fail("Two consecutive calls produced same value: " + res);
        }
        else {
          resOli = res;
        }
      }
    }
  }

  /**
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testNextInt_2()
      throws Exception {
    StockRandomGenerator calc = new StockRandomGenerator();
    int res;
    int resOli = 0;
    for (int i = 0; i < 100; i++) {
      res = calc.nextInt(100000);
      if (i > 0) {
        if (resOli == res) {
          fail("Two consecutive calls produced same value: " + res);
        }
        else {
          resOli = res;
        }
      }
      assertTrue(res >= 0.000d);
    }
  }

  /**
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testNextLong_0()
      throws Exception {
    StockRandomGenerator calc = new StockRandomGenerator();
    long res;
    long resOll = 0;
    for (int i = 0; i < 100; i++) {
      res = calc.nextLong();
      if (i > 0) {
        if (resOll == res) {
          fail("Two consecutive calls produced same value: " + res);
        }
        else {
          resOll = res;
        }
      }
    }
  }

  /**
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testNextDouble_0()
      throws Exception {
    StockRandomGenerator calc = new StockRandomGenerator();
    double res;
    double resOld = 0.0000d;
    for (int i = 0; i < 100; i++) {
      res = calc.nextDouble();
      if (i > 0) {
        if (Math.abs(resOld - res) < DELTA) {
          fail("Two consecutive calls produced same value: " + res);
        }
        else {
          resOld = res;
        }
      }
      assertTrue(res >= 0.000d);
    }
  }

  /**
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testNextFloat_0()
      throws Exception {
    StockRandomGenerator calc = new StockRandomGenerator();
    float res;
    float resOlf = 0.0000f;
    for (int i = 0; i < 100; i++) {
      res = calc.nextFloat();
      if (i > 0) {
        if (Math.abs(resOlf - res) < DELTA) {
          fail("Two consecutive calls produced same value: " + res);
        }
        else {
          resOlf = res;
        }
      }
      assertTrue(res >= 0.000d);
    }
  }

  /**
   * Tests distribution of boolean random values. We should have more than 10%
   * of each true and false when running a significant number of tests
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.3
   */
  public void testNextBoolean_0()
      throws Exception {
    StockRandomGenerator calc = new StockRandomGenerator();
    boolean res;
    int trueCounter = 0;
    int falseCounter = 0;
    int total = 100;
    for (int i = 0; i < total; i++) {
      res = calc.nextBoolean();
      if (res) {
        trueCounter++;
      }
      else {
        falseCounter++;
      }
    }
    assertTrue(trueCounter > total * 0.1);
    assertTrue(falseCounter > total * 0.1);
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
    StockRandomGenerator srg = new StockRandomGenerator();
    Object o = privateAccessor.getField(srg, "seed");
    AtomicLong seed1 = (AtomicLong) privateAccessor.getField(srg, "seed");
    long curr = System.currentTimeMillis();
    while (curr == System.currentTimeMillis());
    StockRandomGenerator srg2 = (StockRandomGenerator) doSerialize(srg);
    AtomicLong seed2 = (AtomicLong) privateAccessor.getField(srg2, "seed");
    assertFalse(seed1.get() == seed2.get());
  }
}
