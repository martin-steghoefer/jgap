package org.jgap.impl;

import junit.framework.*;

/**
 * <p>Title: Test suite for all tests of package org.jgap.impl</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * @author Klaus Meffert
 */

public class AllImplTests extends TestSuite {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  public AllImplTests() {
  }

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTest(IntegerGeneTest.suite());
    suite.addTest(BooleanGeneTest.suite());
    suite.addTest(MutationOperatorTest.suite());
    suite.addTest(CrossoverOperatorTest.suite());
    suite.addTest(ReproductionOperatorTest.suite());
    suite.addTest(DefaultConfigurationTest.suite());
    return suite;
  }

}