/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.impl;

import org.jgap.*;
import junit.framework.*;

/**
 * Tests the DefaultCloneHandler class.
 *
 * @author Klaus Meffert
 * @since 2.6
 */
public class DefaultCloneHandlerTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.3 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(DefaultCloneHandlerTest.class);
    return suite;
  }

  /**
   * Handler implementing Cloneable without clone() method.
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testIsHandlerFor_0()
      throws Exception {
    IHandler handler = new DefaultCloneHandler();
    assertFalse(handler.isHandlerFor(null, MyCloneHandlerForTest.class));
  }

  /**
   * Handler not implementing Cloneable.
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testIsHandlerFor_1()
      throws Exception {
    IHandler handler = new DefaultCloneHandler();
    assertFalse(handler.isHandlerFor(null, MyClassForTest.class));
  }

  /**
   * @throws Exception
   *
   * @author Klaus meffert
   * @since 2.6
   */
  public void testPerform_1()
      throws Exception {
    IHandler handler = new DefaultCloneHandler();
    FixedBinaryGene orig = new FixedBinaryGene(conf, 3);
    FixedBinaryGene clone = (FixedBinaryGene) handler.perform(orig,
        FixedBinaryGene.class, null);
    assertEquals(orig, clone);
  }

  /**
   * @throws Exception
   *
   * @author Klaus meffert
   * @since 2.6
   */
  public void testIsHandlerFor_2()
      throws Exception {
    IHandler handler = new DefaultCloneHandler();
    Object app = new MyAppDataForTest();
    assertFalse(handler.isHandlerFor(app,
        app.getClass()));
  }

  class MyAppDataForTest implements Cloneable {
    public int compareTo(Object o) {
      return 0;
    }

    public Object clone() throws CloneNotSupportedException {
      throw new CloneNotSupportedException();
    }

  }

  class MyCloneHandlerForTest
      implements Cloneable {
  }
  class MyClassForTest {
  }
}
