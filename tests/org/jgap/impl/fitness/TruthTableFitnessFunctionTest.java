/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.impl.fitness;

import java.util.*;

import org.jgap.*;

import junit.framework.*;

/**
 * Tests the TruthTableFitnessFunction class.
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class TruthTableFitnessFunctionTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.5 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(TruthTableFitnessFunctionTest.class);
    return suite;
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testCalcFitness_0()
      throws Exception {
    Map inout = new HashMap();
    inout.put(new Double(2), new Double(3));
    inout.put(new Double(9), new Double(11));
    TruthTableFitnessFunctionImpl fitfunc = new TruthTableFitnessFunctionImpl(
        conf, 7);
    Map truthTable = new HashMap();
    truthTable.put(new Double(2), new Double(4));
    truthTable.put(new Double(8), new Double(1));
    truthTable.put(new Double(9), new Double(11));
    fitfunc.setTruthTable(truthTable);
    assertEquals(2.0d, fitfunc.calcFitness(inout), DELTA);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testCalcFitness_1()
      throws Exception {
    Map inout = new HashMap();
    inout.put(new Double(2), new Double(3));
    inout.put(new Double(9), new Double(11));
    Map truthTable = new HashMap();
    truthTable.put(new Double(2), new Double(4));
    truthTable.put(new Double(8), new Double(1));
    truthTable.put(new Double(9), new Double(11));
    TruthTableFitnessFunctionImpl fitfunc = new TruthTableFitnessFunctionImpl(
        conf, 7, truthTable);
    assertEquals(2.0d, fitfunc.calcFitness(inout), DELTA);
  }

  /**
   * Calc fitness with one value being a NaN.
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testCalcFitness_2()
      throws Exception {
    Map inout = new HashMap();
    inout.put(new Double(2), new Double(Double.NaN));
    inout.put(new Double(9), new Double(11));
    Map truthTable = new HashMap();
    truthTable.put(new Double(2), new Double(4));
    truthTable.put(new Double(8), new Double(1));
    TruthTableFitnessFunctionImpl fitfunc = new TruthTableFitnessFunctionImpl(
        conf, 7, truthTable);
    assertTrue(Double.isNaN(fitfunc.calcFitness(inout)));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public void testgetConfiguration_0()
      throws Exception {
    Map truthTable = new HashMap();
    truthTable.put(new Double(9), new Double(11));
    TruthTableFitnessFunctionImpl fitfunc = new TruthTableFitnessFunctionImpl(
        conf, 7, truthTable);
    assertSame(conf, fitfunc.getConfiguration());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public void testConstruct_0()
      throws Exception {
    Genotype.setStaticConfiguration(conf);
    TruthTableFitnessFunctionImpl fitfunc = new TruthTableFitnessFunctionImpl();
    assertSame(conf, fitfunc.getConfiguration());
  }

  /**
   * Implementing class of abstract TruthTableFitnessFunction class.
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  private class TruthTableFitnessFunctionImpl
      extends TruthTableFitnessFunction {
    /**
     * @since 2.0 (until 1.1: type int)
     */
    private double m_evaluationValue;

    public TruthTableFitnessFunctionImpl() {
      super();
    }

    public TruthTableFitnessFunctionImpl(Configuration a_config,
        double a_evaluationValue, Map a_values) {
      super(a_config, a_values);
      m_evaluationValue = a_evaluationValue;
    }

    public TruthTableFitnessFunctionImpl(Configuration a_config,
        double a_evaluationValue) {
      super(a_config);
      m_evaluationValue = a_evaluationValue;
    }

    protected double evaluate(IChromosome a_subject) {
      return m_evaluationValue;
    }
  }
}
