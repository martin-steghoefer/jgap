package examples.functionFinder.test;

import junit.framework.*;

/**
 * Runs all tests for function builder example
 *
 * @author Klaus Meffert
 * @since 2.2
 */
public class TestAll extends TestSuite {

  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.1 $";

  public TestAll(String name) {
    super(name);
  }

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(FunctionBuilderTest.class);
    suite.addTestSuite(FitnessValueTest.class);
    suite.addTestSuite(GeneExtractorTest.class);
    return suite;
  }
}
