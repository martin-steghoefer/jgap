package org.jgap.xml;

import junit.framework.*;

/**
 * <p>Title: Test suite for all tests of package org.jgap.xml</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * @author Klaus Meffert
 */

public class AllXMLTests extends TestSuite {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  public AllXMLTests() {
  }

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTest(XMLManagerTest.suite());
    return suite;
  }


}