package org.jgap;

import junit.framework.*;

/**
 * <p>Title: Tests for the FitnessFunction class</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * @author Klaus Meffert
 */

public class FitnessFunctionTest extends TestCase {

  public FitnessFunctionTest() {
  }

  public static Test suite() {
    TestSuite suite = new TestSuite(FitnessFunctionTest.class);
    return suite;
  }

  public void testGetFitnessValue_0() {
    FitnessFunctionImpl fitfunc = new FitnessFunctionImpl(7);
    assertEquals(7, fitfunc.getFitnessValue(null));
  }

  public void testGetFitnessValue_1() {
      try
      {
          FitnessFunctionImpl fitfunc = new FitnessFunctionImpl(-7);
          fitfunc.getFitnessValue(null);
          fail( "Returning negative fitness value did not raise exception." );
      }
      catch( RuntimeException cause )
      {
          // This is expected since negative fitness values are illegal.
          // -----------------------------------------------------------
      }
  }

  public void testGetFitnessValue_2() {
        try
        {
            FitnessFunctionImpl fitfunc = new FitnessFunctionImpl(0);
            fitfunc.getFitnessValue(null);
            fail( "Returning fitness value of zero did not raise exception" );
        }
        catch( RuntimeException cause )
        {
            // This is expected since non-positive fitness values are illegal.
            // ---------------------------------------------------------------
        }
  }

  /**
   * Implementing class of abstract FitnessFunction class
   * @author Klaus Meffert
   * @version 1.0
   */
  private class FitnessFunctionImpl extends FitnessFunction {

    private int evaluationValue;

    public FitnessFunctionImpl(int evaluationValue) {
      this.evaluationValue = evaluationValue;
    }
    protected int evaluate( Chromosome a_subject ) {
      return evaluationValue;
    }

  }
}