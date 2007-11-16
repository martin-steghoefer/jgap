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
 * Tests the DefaultInitializer class.
 *
 * @author Klaus Meffert
 * @since 2.6
 */
public class DefaultInitializerTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.4 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(DefaultInitializerTest.class);
    return suite;
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testIsHandlerFor_0()
      throws Exception {
    IHandler handler = new DefaultInitializer();
    assertFalse(handler.isHandlerFor(null, Object.class));
    assertTrue(handler.isHandlerFor(null, Chromosome.class));
    assertFalse(handler.isHandlerFor(null,DefaultInitializer.class));
    assertFalse(handler.isHandlerFor(new DefaultInitializer(),
                                     DefaultInitializer.class));
    assertTrue(handler.isHandlerFor(new MyInitializerForTesting(),
                                    MyInitializerForTesting.class));
  }

  /**
   * @throws Exception
   *
   * @author Klaus meffert
   * @since 2.6
   */
  public void testPerform_0()
      throws Exception {
    IHandler handler = new DefaultInitializer();
    Chromosome orig = new Chromosome(conf);
    try {
      handler.perform(orig, Chromosome.class, null);
      fail();
    }
    catch (InvalidConfigurationException iex) {
      ; //this is OK (Configuration is null)
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
    FixedBinaryGene orig = new FixedBinaryGene(conf, 3);
    FixedBinaryGene clone = (FixedBinaryGene) handler.perform(orig,
        FixedBinaryGene.class, null);
    assertEquals(orig, clone);
  }

  public class MyInitializerForTesting
      implements IInitializer {
    public boolean isHandlerFor(final Object a_obj, final Class a_class) {
      return true;
    }

    public Object perform(final Object a_obj, final Class a_class,
                          final Object a_params)
        throws Exception {
      return null;
    }
  }
}
