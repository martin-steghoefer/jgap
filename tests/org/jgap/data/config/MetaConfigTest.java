/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.data.config;

import java.util.*;

import org.jgap.*;

import junit.framework.*;

/**
 * Tests the MetaConfig class.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class MetaConfigTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  // number of chromosomes used in test case
  private final static int NUM_CHROMS = 5;

  // number of genes used in test case
  private final static int NUM_GENES = 2;

  public static Test suite() {
    TestSuite suite = new TestSuite(MetaConfigTest.class);
    return suite;
  }

  public void testSingleton_0()
      throws Exception {
    MetaConfig mc = MetaConfig.getInstance();
    assertSame(MetaConfig.getInstance(), mc);
    assertSame(MetaConfig.getInstance(), mc);
  }

  public void testGetConfigProperty_0()
      throws Exception {
    MetaConfig mc = MetaConfig.getInstance();
    List props = mc.getConfigProperty("org.jgap.Configuration");
    assertEquals(4, props.size());
    props = mc.getConfigProperty("org.jgap.impl.TournamentSelector");
    assertEquals(1, props.size());
  }
}
