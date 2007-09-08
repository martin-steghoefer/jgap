/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.eval;

import junit.framework.*;

/**
 * Test suite for all tests of package org.jgap.eval
 *
 * @author Klaus Meffert
 * @since 2.0
 */
public class AllEvalTests
    extends TestSuite {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.7 $";

  public static Test suite() {
    TestSuite suite = new TestSuite("AllEvalTests");
    suite.addTest(PopulationHistoryTest.suite());
    return suite;
  }
}
