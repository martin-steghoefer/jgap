/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap;

import junit.framework.*;

/**
 * Test cases for class DeltaFitnessEvaluator.
 *
 * @author Klaus Meffert
 * @since 2.2
 */
public final class DeltaFitnessEvaluatorTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.10 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(DeltaFitnessEvaluatorTest.class);
    return suite;
  }

  public void testIsFitter_0() {
    FitnessEvaluator evaluator = new DeltaFitnessEvaluator();
    assertEquals(false, evaluator.isFitter(1, 0));
    assertEquals(true, evaluator.isFitter(0, 1));
  }

  public void testIsFitter_1() {
    FitnessEvaluator evaluator = new DeltaFitnessEvaluator();
    assertEquals(false, evaluator.isFitter(12, 11));
    assertEquals(true, evaluator.isFitter(11, 12));
  }

  public void testIsFitter_2() {
    FitnessEvaluator evaluator = new DeltaFitnessEvaluator();
    assertEquals(false, evaluator.isFitter( -1, 1));
    assertEquals(true, evaluator.isFitter(1, -1));
  }

  public void testIsFitter_3() {
    FitnessEvaluator evaluator = new DeltaFitnessEvaluator();
    assertEquals(false, evaluator.isFitter(0, 0));
  }

  public void testIsFitter_4() {
    FitnessEvaluator evaluator = new DeltaFitnessEvaluator();
    assertEquals(false, evaluator.isFitter( -4, -4));
  }

  public void testIsFitter_5() {
    FitnessEvaluator evaluator = new DeltaFitnessEvaluator();
    assertEquals(false, evaluator.isFitter( -3, -1));
    assertEquals(false, evaluator.isFitter( -1, -3));
  }

  public void testIsFitter_6()
      throws Exception {
    Configuration conf = new ConfigurationForTesting();
    FitnessEvaluator evaluator = new DeltaFitnessEvaluator();
    Chromosome chrom1 = new Chromosome(conf);
    chrom1.setFitnessValue(2);
    Chromosome chrom2 = new Chromosome(conf);
    chrom2.setFitnessValue(2);
    assertEquals(false, evaluator.isFitter(chrom1, chrom2));
    assertEquals(false, evaluator.isFitter(chrom2, chrom1));
    chrom2.setFitnessValue(3);
    assertEquals(true, evaluator.isFitter(chrom1, chrom2));
    assertEquals(false, evaluator.isFitter(chrom2, chrom1));
  }
}
