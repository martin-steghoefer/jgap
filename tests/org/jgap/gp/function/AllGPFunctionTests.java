/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp.function;

import junit.framework.*;

/**
 * Test suite for all tests of package org.jgap.gp.function.
 *
 * @author Klaus Meffert
 * @since 3.4
 */
public class AllGPFunctionTests
    extends TestSuite {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  public static Test suite() {
    TestSuite suite = new TestSuite("AllGPFunctionTests");
    suite.addTest(EqualsTest.suite());
    suite.addTest(ForLoopTest.suite());
    suite.addTest(GreaterThanTest.suite());
    suite.addTest(LesserThanTest.suite());
    return suite;
  }
}
