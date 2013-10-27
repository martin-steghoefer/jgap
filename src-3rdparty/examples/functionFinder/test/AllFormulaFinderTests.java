package examples.functionFinder.test;

import junit.framework.*;

/**
 * Runs all tests for formula finder example.<p>
 * Not included with other test suites (ALlTests) as this relies on a third-
 * party library.
 *
 * @author Klaus Meffert
 * @since 2.2
 */
public class AllFormulaFinderTests extends TestSuite {

  public AllFormulaFinderTests(String name) {
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
