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
 * Test suite for all tests of package org.jgap.
*
 * @author Klaus Meffert
 * @since 1.1
 */
public class AllBaseTests
    extends TestSuite {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.12 $";

  public static Test suite() {
    TestSuite suite = new TestSuite("AllBaseTests");
    suite.addTest(BaseGeneTest.suite());
    suite.addTest(BaseRateCalculatorTest.suite());
    suite.addTest(ChromosomeTest.suite());
    suite.addTest(ConfigurationTest.suite());
    suite.addTest(DefaultFitnessEvaluatorTest.suite());
    suite.addTest(DeltaFitnessEvaluatorTest.suite());
    suite.addTest(FitnessFunctionTest.suite());
    suite.addTest(GenotypeTest.suite());
    suite.addTest(PopulationTest.suite());
    return suite;
  }
}
