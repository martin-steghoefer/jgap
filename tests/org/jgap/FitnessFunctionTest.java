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

package org.jgap;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests for the FitnessFunction class
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class FitnessFunctionTest
    extends TestCase {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.7 $";

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
