/*
 * This file is part of JGAP.
 *
 * JGAP is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * JGAP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser Public License
 * along with JGAP; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package org.jgap;

import org.jgap.data.*;
import org.jgap.event.*;
import org.jgap.impl.*;
import org.jgap.xml.*;

import junit.framework.*;

/**
 * Test suite for all test cases.
 * Start this class to execute all Tests.
 * Required are junit.jar and junit-addons_1.4.jar
 * In here, only test suites will be coped with (see method suite()).
 * Don't add any test cases to this class.
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class AllTests
    extends TestSuite {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.5 $";

  public AllTests() {
  }

  public static Test suite() {
    TestSuite suite = new TestSuite();
    suite.addTest(AllBaseTests.suite());
    suite.addTest(AllDataTests.suite());
    suite.addTest(AllEventTests.suite());
    suite.addTest(AllImplTests.suite());
    suite.addTest(AllXMLTests.suite());
    return suite;
  }
}
