/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.audit;

import junit.framework.*;

/**
 * Test suite for all tests of package org.jgap.distr
 *
 * @author Klaus Meffert
 * @since 1.0
 */
public class AllAuditTests
    extends TestSuite {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.5 $";

  public static Test suite() {
    TestSuite suite = new TestSuite("AllAuditTests");
    suite.addTest(EvaluatorTest.suite());
    suite.addTest(KeyedValueTest.suite());
    suite.addTest(KeyedValuesTest.suite());
    suite.addTest(KeyedValues2DTest.suite());
    suite.addTest(PermutingConfigurationTest.suite());
    return suite;
  }
}
