/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.impl;

import org.jgap.*;

import junit.framework.*;

/**
 * Tests the DefaultCompareToHandler class.
 *
 * @author Klaus Meffert
 * @since 3.1
 */
public class DefaultCompareToHandlerTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.3 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(DefaultCompareToHandlerTest.class);
    return suite;
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public void testIsHandlerFor_0()
      throws Exception {
    IHandler handler = new DefaultCompareToHandler();
    assertFalse(handler.isHandlerFor(null, null));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public void testIsHandlerFor_1()
      throws Exception {
    IHandler handler = new DefaultCompareToHandler();
    assertTrue(handler.isHandlerFor(null, String.class));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public void testIsHandlerFor_3()
      throws Exception {
    IHandler handler = new DefaultCompareToHandler();
    String v = "Test";
    assertTrue(handler.isHandlerFor(v, null));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public void testPerform_1()
      throws Exception {
    IHandler handler = new DefaultCompareToHandler();
    FixedBinaryGene f1 = new FixedBinaryGene(conf, 3);
    Integer result = (Integer) handler.perform(f1, FixedBinaryGene.class, null);
    assertEquals(1, result.intValue());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public void testPerform_2()
      throws Exception {
    IHandler handler = new DefaultCompareToHandler();
    FixedBinaryGene f1 = new FixedBinaryGene(conf, 3);
    FixedBinaryGene f2 = new FixedBinaryGene(conf, 5);
    Integer result = (Integer) handler.perform(f1, FixedBinaryGene.class, f2);
    assertEquals(-1, result.intValue());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public void testIsHandlerFor_2()
      throws Exception {
    IHandler handler = new DefaultCompareToHandler();
    Object app = new MyAppDataForTesting();
    assertTrue(handler.isHandlerFor(app, null));
    assertTrue(handler.isHandlerFor(app, app.getClass()));
  }

  class MyAppDataForTesting
      implements Comparable {
    public int compareTo(Object o) {
      return 0;
    }
  }
}
