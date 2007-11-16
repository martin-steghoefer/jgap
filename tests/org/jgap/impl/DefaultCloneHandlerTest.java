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
import org.jgap.util.*;
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
  private static final String CVS_REVISION = "$Revision: 1.8 $";

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
    assertFalse(handler.isHandlerFor(null, MyCloneHandlerForTesting.class));
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
    assertFalse(handler.isHandlerFor(null, MyClassForTesting.class));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testIsHandlerFor_2()
      throws Exception {
    IHandler handler = new DefaultCloneHandler();
    Object app = new MyAppDataForTesting();
    assertTrue(handler.isHandlerFor(app, app.getClass()));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void testIsHandlerFor_3()
      throws Exception {
    IHandler handler = new DefaultCloneHandler();
    assertTrue(handler.isHandlerFor(null, ICloneable.class));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
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
   * @author Klaus Meffert
   * @since 3.2
   */
  public void testPerform_2()
      throws Exception {
    IHandler handler = new DefaultCloneHandler();
    FixedBinaryGene orig = new FixedBinaryGene(conf, 3);
    FixedBinaryGene clone = (FixedBinaryGene) handler.perform(orig,
        null, null);
    assertEquals(orig, clone);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void testPerform_3()
      throws Exception {
    IHandler handler = new DefaultCloneHandler();
    Chromosome orig = new Chromosome(conf);
    Chromosome clone = (Chromosome) handler.perform(orig, Chromosome.class, null);
    assertEquals(orig, clone);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void testPerform_4()
      throws Exception {
    IHandler handler = new DefaultCloneHandler();
    Chromosome orig = new Chromosome(conf);
    Chromosome clone = (Chromosome) handler.perform(orig, null, null);
    assertEquals(orig, clone);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void testPerform_5()
      throws Exception {
    IHandler handler = new DefaultCloneHandler();
    Chromosome orig = new Chromosome(conf);
    Chromosome clone = (Chromosome) handler.perform(orig, null, new Vector());
    assertEquals(orig, clone);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void testPerform_6()
      throws Exception {
    IHandler handler = new DefaultCloneHandler();
    Chromosome orig = new Chromosome(conf);
    try {
      Chromosome clone = (Chromosome) handler.perform(null, Chromosome.class, null);
      fail();
    } catch (NullPointerException nex) {
      ; //this is OK
    }
  }

  class MyAppDataForTesting
      implements Cloneable {
    public int compareTo(Object o) {
      return 0;
    }

    public Object clone()
        throws CloneNotSupportedException {
      throw new CloneNotSupportedException();
    }
  }
  class MyCloneHandlerForTesting
      implements Cloneable {
  }
  class MyClassForTesting {
  }
}
