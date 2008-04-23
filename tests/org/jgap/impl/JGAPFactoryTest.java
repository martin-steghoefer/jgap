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

import java.util.*;
import org.jgap.*;
import junit.framework.*;

/**
 * Tests the JGAPFactory class.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class JGAPFactoryTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.5 $";

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
    } catch (IndexOutOfBoundsException iex) {
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
    } catch (IndexOutOfBoundsException iex) {
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
    } catch (IndexOutOfBoundsException iex) {
      ; //this is OK
    }
  }

  /**
   * @author Klaus Meffert
   * @since 3.1
   */
  public void testCreateRandomGenerator_0() {
    JGAPFactory factory = new JGAPFactory(true);
    assertEquals(StockRandomGenerator.class,
                 factory.createRandomGenerator().getClass());
  }

  /**
   * Without caching.
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public void testGetCloneHandlerFor_0() {
    JGAPFactory factory = new JGAPFactory(false);
    ICloneHandler cloneHandler = new DefaultCloneHandler();
    factory.registerCloneHandler(cloneHandler);
    assertSame(cloneHandler, factory.getCloneHandlerFor(null, IApplicationData.class));
  }

  /**
   * With caching.
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public void testGetCloneHandlerFor_1() {
    JGAPFactory factory = new JGAPFactory(true);
    ICloneHandler cloneHandler = new DefaultCloneHandler();
    factory.registerCloneHandler(cloneHandler);
    assertSame(cloneHandler, factory.getCloneHandlerFor(null, IApplicationData.class));
    // Second request to verify caching returns correct results.
    // ---------------------------------------------------------
    assertSame(cloneHandler, factory.getCloneHandlerFor(null, IApplicationData.class));
  }

  /**
   * Type with no handler registered, caching active.
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public void testGetCloneHandlerFor_2() {
    JGAPFactory factory = new JGAPFactory(true);
    ICloneHandler cloneHandler = new DefaultCloneHandler();
    factory.registerCloneHandler(cloneHandler);
    assertNull(factory.getCloneHandlerFor(null, IntegerGene.class));
    assertNull(factory.getCloneHandlerFor(null, IntegerGene.class));
  }

  /**
   * Type with no handler registered, no caching
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public void testGetCloneHandlerFor_3() {
    JGAPFactory factory = new JGAPFactory(false);
    ICloneHandler cloneHandler = new DefaultCloneHandler();
    factory.registerCloneHandler(cloneHandler);
    assertNull(factory.getCloneHandlerFor(null, IntegerGene.class));
    assertNull(factory.getCloneHandlerFor(null, IntegerGene.class));
  }

  /**
   * Null type, no caching.
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public void testGetCloneHandlerFor_4() {
    JGAPFactory factory = new JGAPFactory(false);
    ICloneHandler cloneHandler = new DefaultCloneHandler();
    factory.registerCloneHandler(cloneHandler);
    assertNull(factory.getCloneHandlerFor(null, null));
  }

  /**
   * Null type, with caching.
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public void testGetCloneHandlerFor_5() {
    JGAPFactory factory = new JGAPFactory(true);
    ICloneHandler cloneHandler = new DefaultCloneHandler();
    factory.registerCloneHandler(cloneHandler);
    assertNull(factory.getCloneHandlerFor(null, null));
  }

  /**
   * Request for an unsupported object instance.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public void testGetCloneHandlerFor_6()
      throws Exception {
    JGAPFactory factory = new JGAPFactory(false);
    ICloneHandler cloneHandler = new DefaultCloneHandler();
    factory.registerCloneHandler(cloneHandler);
    Object inst = new IntegerGene(conf);
    assertNull(factory.getCloneHandlerFor(inst, null));
  }

  /**
   * Request for a supported object instance.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public void testGetCloneHandlerFor_7()
      throws Exception {
    JGAPFactory factory = new JGAPFactory(false);
    ICloneHandler cloneHandler = new DefaultCloneHandler();
    factory.registerCloneHandler(cloneHandler);
    Object inst = new Chromosome(conf);
    assertSame(cloneHandler, factory.getCloneHandlerFor(inst, null));
  }

  /**
   * Ensures JGAPFactory is implementing Serializable.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void testIsSerializable_0()
      throws Exception {
    JGAPFactory inst = new JGAPFactory(false);
    assertTrue(isSerializable(inst));
  }

  /**
   * Ensures that JGAPFactory and all objects contained implement Serializable
   * correctly.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void testDoSerialize_0()
      throws Exception {
    JGAPFactory inst = new JGAPFactory(false);
    Object o = doSerialize(inst);
    assertEquals(o, inst);
  }

}
