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
 * Tests the GaussianRandomGenerator class.
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class GaussianRandomGeneratorTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.18 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(GaussianRandomGeneratorTest.class);
    return suite;
  }

  /**
   * Check if construction and calculation in general possible
   * @author Klaus Meffert
   */
  public void testGeneral() {
    GaussianRandomGenerator calc = new GaussianRandomGenerator();
    calc.nextInt();
    calc.nextBoolean();
    calc.nextDouble();
    calc.nextFloat();
    calc.nextInt();
    calc.nextLong();
  }

  /**
   * @author Klaus Meffert
   */
  public void testConstruct_0() {
    try {
      new GaussianRandomGenerator(0.0d);
      fail();
    }
    catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   * @author Klaus Meffert
   */
  public void testConstruct_1() {
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

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testGetGaussianStdDeviation_0()
      throws Exception {
    final double stdDeriv = 0.04d;
    GaussianRandomGenerator calc = new GaussianRandomGenerator(stdDeriv);
    assertEquals(stdDeriv, calc.getGaussianStdDeviation(), DELTA);
//    Gene gene = new IntegerGene(1, 5);
//    Chromosome chrom = new Chromosome(gene, 50);
//    conf.setSampleChromosome(chrom);
    /**@todo finish*/
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testNextInt_0()
      throws Exception {
    GaussianRandomGenerator calc = new GaussianRandomGenerator(1.0d);
    int res;
    for (int i = 0; i < 100; i++) {
      res = calc.nextInt(5);
      assertTrue(res < 5.00d);
      assertTrue(res >= 0.000d);
    }
  }

  /**
   *
   * @throws Exception
   * @author Klaus Meffert
   */
  public void testNextInt_1()
      throws Exception {
    GaussianRandomGenerator calc = new GaussianRandomGenerator(1.0d);
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
      assertTrue(res >= 0.000d);
    }
  }

  /**
   *
   * @throws Exception
   * @author Klaus Meffert
   */
  public void testNextInt_2()
      throws Exception {
    GaussianRandomGenerator calc = new GaussianRandomGenerator(1.0d);
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
   * @author Klaus Meffert
   */
  public void testNextLong_0()
      throws Exception {
    GaussianRandomGenerator calc = new GaussianRandomGenerator(1.0d);
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
      assertTrue(res >= 0.000d);
    }
  }

  /**
   *
   * @throws Exception
   * @author Klaus Meffert
   */
  public void testNextDouble_0()
      throws Exception {
    GaussianRandomGenerator calc = new GaussianRandomGenerator(1.0d);
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
      if (res >= 1.0d) {
        System.err.println(res);
        assertTrue(res < 1.000d);
      }
    }
  }

  /**
   *
   * @throws Exception
   * @author Klaus Meffert
   */
  public void testNextFloat_0()
      throws Exception {
    GaussianRandomGenerator calc = new GaussianRandomGenerator(1.0d);
//    double min=-5.57979044;double max=5.7904768;
//    for (int i=0;i<100000000;i++) {
//      double g = calc.nextDouble();
//      if(g < min) {
//        min = g;
//      }
//      if (g > max) {
//        max = g;
//      }
//    }
//    System.err.println("MIN: "+min);
//    System.err.println("MAX: "+max);
//    GaussianRandomGenerator calc = new GaussianRandomGenerator(1.0d);
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
   * Tests serializability capabilities with default constructor.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.01
   */
  public void testSerialize_0()
      throws Exception {
    /**@todo fix test as Java 5 uses java.util.concurrent.AtomicLong instead
     * of sun.misc.AtomicLong
     */
    GaussianRandomGenerator srg = new GaussianRandomGenerator();
    Random r1 = (Random) privateAccessor.getField(srg, "m_rn");
    AtomicLong seed1 = (AtomicLong) privateAccessor.getField(r1, "seed");
    long curr = System.currentTimeMillis();
    while (curr == System.currentTimeMillis());
    GaussianRandomGenerator srg2 = (GaussianRandomGenerator) doSerialize(srg);
    Random r2 = (Random) privateAccessor.getField(srg2, "m_rn");
    AtomicLong seed2 = (AtomicLong) privateAccessor.getField(r2, "seed");
    assertFalse(seed1.get() == seed2.get());
  }
  /**
   * Tests serializability capabilities with non-default constructor.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.01
   */
  public void testSerialize_1() throws Exception {
    /**@todo fix test as Java 5 uses java.util.concurrent.AtomicLong instead
     * of sun.misc.AtomicLong
     */
    GaussianRandomGenerator srg = new GaussianRandomGenerator(0.5d);
    Random r1 = (Random) privateAccessor.getField(srg, "m_rn");
    AtomicLong seed1 = (AtomicLong) privateAccessor.getField(r1, "seed");
    long curr = System.currentTimeMillis();
    while (curr == System.currentTimeMillis());
    GaussianRandomGenerator srg2 = (GaussianRandomGenerator) doSerialize(srg);
    Random r2 = (Random) privateAccessor.getField(srg2, "m_rn");
    AtomicLong seed2 = (AtomicLong) privateAccessor.getField(r2, "seed");
    assertFalse(seed1.get() == seed2.get());
  }
}
