/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap;

import junit.framework.*;

/**
 * Tests for the FitnessFunction class
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class FitnessFunctionTest
    extends TestCase {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.9 $";

  public FitnessFunctionTest() {
  }

  public static Test suite() {
    TestSuite suite = new TestSuite(FitnessFunctionTest.class);
    return suite;
  }

  public void testGetFitnessValue_0() {
    FitnessFunctionImpl fitfunc = new FitnessFunctionImpl(7);
    assertEquals(7.0d, fitfunc.getFitnessValue(null),0.00000001d);
  }

  public void testGetFitnessValue_1() {
    try {
      FitnessFunctionImpl fitfunc = new FitnessFunctionImpl( -7);
      fitfunc.getFitnessValue(null);
      fail();
    }
    catch (RuntimeException cause) {
      // This is expected since non-positive fitness values are illegal.
    }
  }

  public void testGetFitnessValue_2() {
    try {
      FitnessFunctionImpl fitfunc = new FitnessFunctionImpl(0);
      fitfunc.getFitnessValue(null);
    }
    catch (RuntimeException cause) {
      // This is expected since non-positive fitness values are illegal.
    }
  }

  /**
   * Implementing class of abstract FitnessFunction class
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  private class FitnessFunctionImpl
      extends FitnessFunction {

    /**
     * @since 2.0 (until 1.1: type int)
     */
    private double evaluationValue;
    public FitnessFunctionImpl(double evaluationValue) {
      this.evaluationValue = evaluationValue;
    }

    protected double evaluate(Chromosome a_subject) {
      return evaluationValue;
    }
  }
}
