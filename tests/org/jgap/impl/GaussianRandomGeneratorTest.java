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

package org.jgap.impl;

import org.jgap.*;

import junit.framework.*;

/**
 * Tests for GaussianRandomGenerator class
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class GaussianRandomGeneratorTest
    extends TestCase {

  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.5 $";

  //delta for distinguishing whether a value is to be interpreted as zero
  private static final double DELTA = 0.000001d;

  public GaussianRandomGeneratorTest() {
  }

  public static Test suite() {
    TestSuite suite = new TestSuite(GaussianRandomGeneratorTest.class);
    return suite;
  }

  /**
   * Check if construction and calculation in general possible
   */
  public void testGeneral() {
    Configuration conf = new DefaultConfiguration();
    GaussianRandomGenerator calc = new GaussianRandomGenerator();
    calc.nextInt();
    calc.nextBoolean();
    calc.nextDouble();
    calc.nextFloat();
    calc.nextInt();
    calc.nextLong();
  }

  public void testConstruct_0() {
    Configuration conf = new DefaultConfiguration();
    try {
      GaussianRandomGenerator calc = new GaussianRandomGenerator(0.0d);
      fail();
    }
    catch (IllegalArgumentException iex) {
      ;//this is OK
    }
  }

  public void testConstruct_1() {
    Configuration conf = new DefaultConfiguration();
    GaussianRandomGenerator calc = new GaussianRandomGenerator(1.0d);
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

  public void testGetGaussianStdDeviation_0() throws Exception {
    Configuration conf = new DefaultConfiguration();
    final double stdDeriv = 0.04d;
    GaussianRandomGenerator calc = new GaussianRandomGenerator(
        stdDeriv);
    assertEquals(stdDeriv, calc.getGaussianStdDeviation(), DELTA);
//    Gene gene = new IntegerGene(1, 5);
//    Chromosome chrom = new Chromosome(gene, 50);
//    conf.setSampleChromosome(chrom);
    /**@todo finish*/
  }

  public void testNextInt_0() throws Exception {
    GaussianRandomGenerator calc = new GaussianRandomGenerator(1.0d);
    int res;
    for (int i=0;i<100;i++) {
      res = calc.nextInt(5);
      assertTrue(res < 5.00d);
      assertTrue(res >= 0.000d);
    }
  }

  public void testNextInt_1() throws Exception {
    GaussianRandomGenerator calc = new GaussianRandomGenerator(1.0d);
    int res;
    int resOld = 0;
    for (int i=0;i<100;i++) {
      res = calc.nextInt();
      if (i > 0) {
        if (resOld == res) {
          fail("Two consecutive calls produced same value: "+res);
        }
        else {
          resOld = res;
        }
      }
      assertTrue(res >= 0.000d);
    }
  }

  public void testNextLong_0() throws Exception {
    GaussianRandomGenerator calc = new GaussianRandomGenerator(1.0d);
    long res;
    long resOld = 0;
    for (int i=0;i<100;i++) {
      res = calc.nextLong();
      if (i > 0) {
        if (resOld == res) {
          fail("Two consecutive calls produced same value: "+res);
        }
        else {
          resOld = res;
        }
      }
      assertTrue(res >= 0.000d);
    }
  }

  public void testNextDouble_0() throws Exception {
    GaussianRandomGenerator calc = new GaussianRandomGenerator(1.0d);
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

  /**@todo add further tests*/
}
