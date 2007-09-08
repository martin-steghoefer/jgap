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
 * Tests the ConfigProperty class
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class ConfigPropertyTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  // number of chromosomes used in test case
  private final static int NUM_CHROMS = 5;

  // number of genes used in test case
  private final static int NUM_GENES = 2;

  public static Test suite() {
    TestSuite suite = new TestSuite(ConfigPropertyTest.class);
    return suite;
  }

  public void testConstruct_0() throws Exception {
    ConfigProperty cp = new ConfigProperty();
    assertEquals(0, cp.getName().length());
    assertEquals(0, cp.getLabel().length());
    assertEquals("JTextField", cp.getWidget());
    assertFalse(cp.getValuesIter().hasNext());
  }

  public void testGetName_0() throws Exception {
    ConfigProperty cp = new ConfigProperty();
    final String name = "MysTRING";
    cp.setName(name);
    assertEquals(name, cp.getName());
    assertEquals(name, cp.getLabel());
    cp.setName("something");
    assertEquals(name, cp.getLabel());
  }

  public void testGetWidget_0() throws Exception {
    ConfigProperty cp = new ConfigProperty();
    final String name = "MysTRING";
    cp.setWidget(name);
    assertEquals(name, cp.getWidget());
  }

  public void testGetLabel_0() throws Exception {
    ConfigProperty cp = new ConfigProperty();
    final String name = "MysTRING";
    cp.setLabel(name);
    assertEquals(name, cp.getLabel());
  }

  public void testAddValue_0() throws Exception {
    ConfigProperty cp = new ConfigProperty();
    final String name = "MysTRING";
    cp.addValue(name);
    assertTrue(cp.getValuesIter().hasNext());
    String value = (String)cp.getValuesIter().next();
    assertEquals(name, value);
  }
}
