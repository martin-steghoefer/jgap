/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap;

import org.jgap.audit.*;
import org.jgap.data.*;
import org.jgap.distr.*;
import org.jgap.eval.*;
import org.jgap.event.*;
import org.jgap.ext.*;
import org.jgap.gp.*;
import org.jgap.gui.*;
import org.jgap.impl.*;
import org.jgap.supergenes.*;
import org.jgap.util.*;
import org.jgap.xml.*;

import junit.framework.*;

/**
 * Test suite for all test cases.
 * Start this class to execute all Tests.
 * Required are junit.jar and junit-addons_1.4.jar.
 * In here, only test suites will be referenced (see method suite()).
 * Don't add any test cases to this class!
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class AllTests
    extends TestSuite {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.17 $";

  public static Test suite() {
    TestSuite suite = new TestSuite("AllTests");
    suite.addTest(AllAuditTests.suite());
    suite.addTest(AllBaseTests.suite());
    suite.addTest(AllDataTests.suite());
    suite.addTest(AllDistrTests.suite());
    suite.addTest(AllEvalTests.suite());
    suite.addTest(AllEventTests.suite());
    suite.addTest(AllExtTests.suite());
    suite.addTest(AllGPTests.suite());
    suite.addTest(AllGUITests.suite());
    suite.addTest(AllImplTests.suite());
    suite.addTest(AllSupergenesTests.suite());
    suite.addTest(AllUtilTests.suite());
    suite.addTest(AllXMLTests.suite());
    return suite;
  }
}
