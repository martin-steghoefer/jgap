/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.distr.grid;

import org.jgap.*;

import junit.framework.*;

/**
 * Tests the DefaultGenotypeInitializer class.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public class DefaultGenotypeInitializerTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(DefaultGenotypeInitializerTest.class);
    return suite;
  }

  public void setUp() {
    super.setUp();
    // Important: Reset the configurational parameters set.
    // ----------------------------------------------------
    Configuration.reset();
  }

  /**
   * @author Klaus Meffert
   * @since 3.2
   */
  public void testCompareTo_0() {
    DefaultGenotypeInitializer o = new DefaultGenotypeInitializer();
    DefaultGenotypeInitializer p = new DefaultGenotypeInitializer();
    assertEquals(0, o.compareTo(p));
    assertEquals(0, p.compareTo(o));
  }
}
