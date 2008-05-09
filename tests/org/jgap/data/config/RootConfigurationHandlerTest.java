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

import org.jgap.*;
import junit.framework.*;

/**
 * Tests the RootConfigurationHandler class.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class RootConfigurationHandlerTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  // number of chromosomes used in test case
  private final static int NUM_CHROMS = 5;

  // number of genes used in test case
  private final static int NUM_GENES = 2;

  public static Test suite() {
    TestSuite suite = new TestSuite(RootConfigurationHandlerTest.class);
    return suite;
  }

  public void testConstruct_0() throws Exception {
    RootConfigurationHandler root = new RootConfigurationHandler();
    assertNull(root.getConfigProperties());
  }

  public void testGetNameSpace_0() throws Exception {
    RootConfigurationHandler root = new RootConfigurationHandler();
    assertEquals("org.jgap.Configuration",root.getNS());
  }

  public void testGetPrivateField_0() throws Exception {
    RootConfigurationHandler root = new RootConfigurationHandler();
    assertNull(root.getPrivateField(root, "nix"));
  }

  public void testGetPrivateField_1() throws Exception {
    RootConfigurationHandler root = new RootConfigurationHandler();
    assertNull(root.getPrivateField(root, "CONFIG_NAMESPACe"));
    java.lang.reflect.Field f = root.getPrivateField(root, "CONFIG_NAMESPACE");
    assertEquals(root.getClass(), f.getDeclaringClass());
    assertTrue(f.isAccessible());
  }

}
