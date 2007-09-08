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

import junit.framework.*;

/**
 * Test suite for all tests of package org.jgap.impl.fitness.
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class AllFitnessTests
    extends TestSuite {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  public static Test suite() {
    TestSuite suite = new TestSuite("AllFitnessTests");
    suite.addTest(TruthTableFitnessFunctionTest.suite());
    return suite;
  }
}
