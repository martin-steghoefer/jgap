package org.jgap;

import junit.framework.*;
import org.jgap.impl.AllImplTests;
import org.jgap.event.AllEventTests;
import org.jgap.xml.AllXMLTests;


/**
 * <p>Title: Test suite for all test cases</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * @author Klaus Meffert
 */

public class AllTests extends TestSuite {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  public AllTests() {
  }

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTest(AllImplTests.suite());
    suite.addTest(AllBaseTests.suite());
    suite.addTest(AllEventTests.suite());
    suite.addTest(AllXMLTests.suite());
    return suite;
  }
}