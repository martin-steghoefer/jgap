package org.jgap.supergenes;

import junit.framework.*;

/** Test suite for supergene. If this is ok, the supergene algorithm is
 * ok. The last step is rather slow, so it can run up till 4 seconds
 * on 2.7 Mhz PC.
 *
 * @author Audrius Meskauskas
 */


public class AllTests
    extends TestCase {

  public AllTests(String s) {
    super(s);
  }

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(org.jgap.supergenes.testSupergeneInternalParser.class);
    suite.addTestSuite(org.jgap.supergenes.
                      testSupergenePersistentRepresentation.class);
    suite.addTestSuite(org.jgap.supergenes.testSupergeneSampleApplication.class);
    return suite;
  }
}
