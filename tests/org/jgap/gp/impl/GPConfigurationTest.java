/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp.impl;

import java.util.*;
import junit.framework.*;
import org.jgap.*;
import org.jgap.gp.*;

/**
 * Tests the GPConfiguration class.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class GPConfigurationTest
    extends GPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.4 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(GPConfigurationTest.class);
    return suite;
  }

  public void setUp() {
    super.setUp();
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testSerialize_0()
      throws Exception {
    GPConfiguration.reset();
    GPConfiguration conf = new GPConfiguration();
    GPConfiguration conf2 = (GPConfiguration) doSerialize(conf);
    assertEquals(conf, conf2);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testMemory_0()
      throws Exception {
    GPConfiguration.reset();
    GPConfiguration conf = new GPConfiguration();
    conf.storeInMemory("name1", "test1");
    assertEquals("test1", conf.readFromMemory("name1"));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testMemory_1()
      throws Exception {
    GPConfiguration.reset();
    GPConfiguration conf = new GPConfiguration();
    try {
      conf.readFromMemory("name1");
      fail();
    } catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testMemory_2()
      throws Exception {
    GPConfiguration.reset();
    GPConfiguration conf = new GPConfiguration();
    conf.storeInMemory("name1", "test1");
    conf.storeInMemory("name2", "test2");
    conf.storeInMemory("name3", "test3");
    assertEquals("test2", conf.readFromMemory("name2"));
    // Read repeatedly.
    // ----------------
    assertEquals("test2", conf.readFromMemory("name2"));
    // Ensure vaues are not exchanged.
    // -------------------------------
    assertEquals("test1", conf.readFromMemory("name1"));
    assertEquals("test3", conf.readFromMemory("name3"));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testMemory_3()
      throws Exception {
    GPConfiguration.reset();
    GPConfiguration conf = new GPConfiguration();
    conf.storeInMemory("name1", "test1");
    conf.clearMemory();
    try {
      conf.readFromMemory("name1");
      fail();
    } catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testStack_0()
      throws Exception {
    GPConfiguration.reset();
    GPConfiguration conf = new GPConfiguration();
    assertEquals(0, conf.stackSize());
    try {
      assertNull(conf.peekStack());
      fail();
    } catch (EmptyStackException eex) {
      ; //this is OK
    }
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testStack_1()
      throws Exception {
    GPConfiguration.reset();
    GPConfiguration conf = new GPConfiguration();
    conf.pushToStack("test1");
    assertEquals(1, conf.stackSize());
    conf.pushToStack("test2");
    assertEquals(2, conf.stackSize());
    assertEquals("test2", conf.popFromStack());
    assertEquals(1, conf.stackSize());
    assertEquals("test1", conf.popFromStack());
    assertEquals(0, conf.stackSize());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testStack_2()
      throws Exception {
    GPConfiguration.reset();
    GPConfiguration conf = new GPConfiguration();
    Vector obj = new Vector();
    conf.pushToStack(obj);
    assertSame(obj, conf.peekStack());
    assertSame(obj, conf.popFromStack());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testStack_3()
      throws Exception {
    GPConfiguration.reset();
    GPConfiguration conf = new GPConfiguration();
    try {
      conf.popFromStack();
      fail();
    } catch (EmptyStackException eex) {
      ; //this is OK
    }
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public void testStack_4()
      throws Exception {
    GPConfiguration.reset();
    GPConfiguration conf = new GPConfiguration();
    conf.pushToStack("test1");
    conf.clearStack();
    assertEquals(0, conf.stackSize());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public void testSetSelectionMethod_0()
      throws Exception {
    GPConfiguration.reset();
    GPConfiguration conf = new GPConfiguration();
    try {
      conf.setSelectionMethod(null);
      fail();
    } catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public void testSetSelectionMethod_1()
      throws Exception {
    GPConfiguration.reset();
    GPConfiguration conf = new GPConfiguration();
    INaturalGPSelector sel = new FitnessProportionateSelection();
    conf.setSelectionMethod(sel);
    assertSame(sel, conf.getSelectionMethod());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public void testSetCrossoverMethod_0()
      throws Exception {
    GPConfiguration.reset();
    GPConfiguration conf = new GPConfiguration();
    try {
      conf.setCrossoverMethod(null);
      fail();
    } catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public void testSetCrossoverMethod_1()
      throws Exception {
    GPConfiguration.reset();
    GPConfiguration conf = new GPConfiguration();
    CrossMethod cross = new BranchTypingCross(conf);
    conf.setCrossoverMethod(cross);
    assertSame(cross, conf.getCrossMethod());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public void testConstructor_0()
      throws Exception {
    GPConfiguration.reset();
    GPConfiguration conf = new GPConfiguration();
    assertEquals(BranchTypingCross.class, conf.getCrossMethod().getClass());
    assertEquals(FitnessProportionateSelection.class, conf.getSelectionMethod().getClass());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public void testConstructor_1()
      throws Exception {
    GPConfiguration.reset();
    FitnessProportionateSelection sel = new FitnessProportionateSelection();
    GPConfiguration conf = new GPConfiguration(sel);
    assertSame(sel, conf.getSelectionMethod());
  }
}
