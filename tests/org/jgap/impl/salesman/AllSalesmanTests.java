/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.impl.salesman;

import junit.framework.*;

/**
 * Test the Travelling salesman package
 *
 * @author Audrius Meskauskas
 * @since 2.0
 */
public class AllSalesmanTests
    extends TestCase {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(org.jgap.impl.salesman.TravellingSalesmanTest.class);
    return suite;
  }
}
