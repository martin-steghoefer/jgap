/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.distr;

import junit.framework.*;

/**
 * Test suite for all tests of package org.jgap.distr
 *
 * @author Klaus Meffert
 * @since 1.0
 */
public class AllDistrTests
    extends TestSuite {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  public AllDistrTests() {
  }

  public static Test suite() {
    TestSuite suite = new TestSuite();
//    suite.addTest(.suite());
    return suite;
  }
}
