package org.jgap;

import junit.framework.*;

/**
 * <p>Title: Test suite for all tests of package org.jgap</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * @author Klaus Meffert
 */

public class AllBaseTests extends TestSuite {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  public AllBaseTests() {
  }

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTest(ChromosomeTest.suite());
    suite.addTest(ConfigurationTest.suite());
    suite.addTest(FitnessFunctionTest.suite());
    suite.addTest(GenotypeTest.suite());
    return suite;
  }

}