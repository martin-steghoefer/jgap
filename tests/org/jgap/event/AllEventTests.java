package org.jgap.event;
import junit.framework.*;

/**
 * <p>Title: Test suite for all tests of package org.jgap.event</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * @author Klaus Meffert
 */

public class AllEventTests extends TestSuite {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  public AllEventTests() {
  }

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTest(EventManagerTest.suite());
    suite.addTest(GeneticEventTest.suite());
    return suite;
  }

}