/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp;

import junit.framework.*;
import org.jgap.*;

/**
 * Tests the GPConfiguration class.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class GPConfigurationTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  private GPConfiguration m_gpconf;

  public static Test suite() {
    TestSuite suite = new TestSuite(GPConfigurationTest.class);
    return suite;
  }

  public void setUp() {
    super.setUp();
    try {
      GPConfiguration.reset();
      m_gpconf = new GPConfiguration();
      m_gpconf.setPopulationSize(10);
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
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
    /**@todo implement equals and compareTo to make this test pass*/
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
    }
    catch (IllegalArgumentException iex) {
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

}
