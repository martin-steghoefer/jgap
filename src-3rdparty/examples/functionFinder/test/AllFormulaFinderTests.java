package examples.functionFinder.test;

import junit.framework.*;

/**
 * Runs all tests for formula finder example
 *
 * @author Klaus Meffert
 * @since 2.2
 */
public class AllFormulaFinderTests extends TestSuite {

  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.1 $";

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
