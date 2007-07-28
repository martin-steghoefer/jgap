/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.supergenes;

import junit.framework.*;

/**
 * Test suite for supergene. The last step is rather slow.
 *
 * @author Audrius Meskauskas
 * @since 2.0
 */
public class AllSupergenesTests
    extends TestSuite {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.9 $";

  public AllSupergenesTests(String a_name) {
    super(a_name);
  }

  public static Test suite() {
    TestSuite suite = new TestSuite("AllSupergenesTests");

    suite.addTest(SupergeneInternalParserTest.suite());
    suite.addTest(SupergenePersistentRepresentationTest.suite());
//    suite.addTest(SupergeneSampleApplicationTest.suite());
    return suite;
  }
}
