/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.impl.job;

import org.jgap.*;

import junit.framework.*;

/**
 * Tests the EvolveData class.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public class EvolveDataTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  public static Test suite() {
    return new TestSuite(EvolveDataTest.class);
  }

  public void setUp() {
    super.setUp();
    Configuration.reset();
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void testGetConfiguration_0()
      throws Exception {
    EvolveData data = new EvolveData(conf);
    assertSame(conf, data.getConfiguration());
  }

}
