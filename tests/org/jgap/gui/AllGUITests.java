/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gui;

import junit.framework.*;

/**
 * Test suite for all tests of package org.jgap.gui
 * @author Siddhartha Azad
 */
public class AllGUITests
    extends TestSuite {

  public static Test suite() {
    TestSuite suite = new TestSuite("AllGUITests");
    suite.addTest(ConfigWriterTest.suite());
    return suite;
  }
}
