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
 * Tests for StockRandomGenerator class
 *
 * @author Klaus Meffert
 * @since 2.2
 */
public class StockRandomGeneratorTest
    extends TestCase {

  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.1 $";

  //delta for distinguishing whether a value is to be interpreted as zero
  private static final double DELTA = 0.000001d;

  public StockRandomGeneratorTest() {
  }

  public static Test suite() {
    TestSuite suite = new TestSuite(StockRandomGeneratorTest.class);
    return suite;
  }

  /**
   * Check if construction and calculation in general possible
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

  public void testNextInt_0() throws Exception {
    StockRandomGenerator calc = new StockRandomGenerator();
    int res;
    for (int i=0;i<100;i++) {
      res = calc.nextInt(5);
      assertTrue(res < 5.00d);
      assertTrue(res >= 0.000d);
    }
  }

  public void testNextInt_1() throws Exception {
    StockRandomGenerator calc = new StockRandomGenerator();
    int res;
    int resOli = 0;
    for (int i=0;i<100;i++) {
      res = calc.nextInt();
      if (i > 0) {
        if (resOli == res) {
          fail("Two consecutive calls produced same value: "+res);
        }
        else {
          resOli = res;
        }
      }
      assertTrue(res >= 0.000d);
    }
  }

  public void testNextInt_2() throws Exception {
    StockRandomGenerator calc = new StockRandomGenerator();
    int res;
    int resOli = 0;
    for (int i=0;i<100;i++) {
      res = calc.nextInt(100000);
      if (i > 0) {
        if (resOli == res) {
          fail("Two consecutive calls produced same value: "+res);
        }
        else {
          resOli = res;
        }
      }
      assertTrue(res >= 0.000d);
    }
  }

  public void testNextLong_0() throws Exception {
    StockRandomGenerator calc = new StockRandomGenerator();
    long res;
    long resOll = 0;
    for (int i=0;i<100;i++) {
      res = calc.nextLong();
      if (i > 0) {
        if (resOll == res) {
          fail("Two consecutive calls produced same value: "+res);
        }
        else {
          resOll = res;
        }
      }
      assertTrue(res >= 0.000d);
    }
  }

  public void testNextDouble_0() throws Exception {
    StockRandomGenerator calc = new StockRandomGenerator();
    double res;
    double resOld = 0.0000d;
    for (int i=0;i<100;i++) {
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

  public void testNextFloat_0() throws Exception {
    StockRandomGenerator calc = new StockRandomGenerator();
    float res;
    float resOlf = 0.0000f;
    for (int i=0;i<100;i++) {
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

}
