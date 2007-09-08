/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp.impl;

import junit.framework.*;

/**
 * Test suite for all tests of package org.jgap.gp.impl
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class AllGPImplTests
    extends TestSuite {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.5 $";

  public static Test suite() {
    TestSuite suite = new TestSuite("AllGPImplTests");
    suite.addTest(BranchTypingCrossTest.suite());
    suite.addTest(DefaultGPFitnessEvaluatorTest.suite());
    suite.addTest(DeltaGPFitnessEvaluatorTest.suite());
    suite.addTest(GPConfigurationTest.suite());
    suite.addTest(GPGenotypeTest.suite());
    suite.addTest(GPPopulationTest.suite());
    suite.addTest(GPProgramTest.suite());
    suite.addTest(ProgramChromosomeTest.suite());
    return suite;
  }
}
