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

package org.jgap.impl;

import junit.framework.*;

/**
 * Tests for ChromosomePool class
 *
 * @since 2.0
 * @author Klaus Meffert
 */
public class ChromosomePoolTest
    extends TestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";
  public ChromosomePoolTest() {
  }

  public static Test suite() {
    TestSuite suite = new TestSuite(ChromosomePoolTest.class);
    return suite;
  }

  /**
   * Test if construction possible without failure
   */
  public void testConstruct_0() {
    new ChromosomePool();
  }

}
