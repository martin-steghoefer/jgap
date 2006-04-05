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

import java.util.*;
import org.jgap.*;
import junit.framework.*;

/**
 * Tests the JGAPFactory class
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class JGAPFactoryTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(JGAPFactoryTest.class);
    return suite;
  }

  /**
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testConstruct_0() {
    JGAPFactory factory = new JGAPFactory(false);
    assertFalse(factory.isUseCaching());
  }

  /**
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testConstruct_1() {
    JGAPFactory factory = new JGAPFactory(true);
    assertTrue(factory.isUseCaching());
  }

  /**
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testSetParameters_0() {
    JGAPFactory factory = new JGAPFactory(false);
    List params = new Vector();
    params.add("Param1");
    params.add("Param2");
    factory.setParameters(params);
    assertEquals(params, factory.getParameters());
  }

  /**
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testRegisterCloneHandler_0() {
    JGAPFactory factory = new JGAPFactory(false);
    ICloneHandler cloneHandler = new DefaultCloneHandler();
    int index = factory.registerCloneHandler(cloneHandler);
    assertEquals(0, index);
    assertSame(cloneHandler, factory.removeCloneHandler(index));
    try {
      assertNull(factory.removeCloneHandler(0));
      fail();
    }
    catch (IndexOutOfBoundsException iex) {
      ; //this is OK
    }
  }

  /**
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testRegisterCompareToHandler_0() {
    JGAPFactory factory = new JGAPFactory(false);
    ICompareToHandler cHandler = new DefaultCompareToHandler();
    int index = factory.registerCompareToHandler(cHandler);
    assertEquals(0, index);
    assertSame(cHandler, factory.removeCompareToHandler(index));
    try {
      assertNull(factory.removeCompareToHandler(0));
      fail();
    }
    catch (IndexOutOfBoundsException iex) {
      ; //this is OK
    }
  }

  /**
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testRegisterInitializer_0() {
    JGAPFactory factory = new JGAPFactory(false);
    IInitializer initer = new DefaultInitializer();
    int index = factory.registerInitializer(initer);
    assertEquals(0, index);
    assertSame(initer, factory.removeInitializer(index));
    try {
      assertNull(factory.removeInitializer(0));
      fail();
    }
    catch (IndexOutOfBoundsException iex) {
      ; //this is OK
    }
  }
}
