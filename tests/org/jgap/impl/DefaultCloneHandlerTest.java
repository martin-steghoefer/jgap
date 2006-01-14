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
 * Tests for DefaultCloneHandler class
 *
 * @author Klaus Meffert
 * @since 2.6
 */
public class DefaultCloneHandlerTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.1 $";

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
   * Try to clone object without initializing it properly.
   * @throws Exception
   *
   * @author Klaus meffert
   * @since 2.6
   */
  public void testPerform_0()
      throws Exception {
    IHandler handler = new DefaultCloneHandler();
    Chromosome orig = new Chromosome();
    try {
      handler.perform(orig, Chromosome.class, null);
      fail();
    }
    catch (IllegalStateException ix) {
      ; //this is OK as no config set for Chromosome
    }
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
    FixedBinaryGene orig = new FixedBinaryGene(3);
    FixedBinaryGene clone = (FixedBinaryGene) handler.perform(orig,
        FixedBinaryGene.class, null);
    assertEquals(orig, clone);
  }

  class MyCloneHandlerForTest
      implements Cloneable {
  }
  class MyClassForTest {
  }
}
