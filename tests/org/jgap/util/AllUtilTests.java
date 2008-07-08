/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.util;

import junit.framework.*;

/**
 * Test suite for all tests of package org.jgap.util
*
 * @author Klaus Meffert
 * @since 2.5
 */
public class AllUtilTests
    extends TestSuite {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.4 $";

  public static Test suite() {
    TestSuite suite = new TestSuite("AllUtilTests");
    suite.addTest(FileKitTest.suite());
    suite.addTest(NumberKitTest.suite());
    suite.addTest(PluginDiscovererTest.suite());
    return suite;
  }
}
