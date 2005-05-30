/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.event;

import junit.framework.*;


import org.jgap.*;

/**
 * Tests for GeneticEvent class
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class GeneticEventTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.6 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(GeneticEventTest.class);
    return suite;
  }

  /**
   * @author Klaus Meffert
   */
  public void testConstruct_0() {
    try {
      new GeneticEvent("testEventName", null);
      fail();
    }
    catch (IllegalArgumentException illex) {
      ; //this is OK
    }
  }

  /**
   * @author Klaus Meffert
   */
  public void testConstruct_1() {
    new GeneticEvent(null, this);
  }

  /**
   * @author Klaus Meffert
   */
  public void testGetEventName_0() {
    GeneticEvent event = new GeneticEvent("testEventName", this);
    assertEquals("testEventName", event.getEventName());
  }

  /**
   * @author Klaus Meffert
   */
  public void testGENOTYPE_EVOLVED_EVENT_0() {
    assertTrue(GeneticEvent.GENOTYPE_EVOLVED_EVENT != null);
    assertTrue(GeneticEvent.GENOTYPE_EVOLVED_EVENT.length() > 0);
  }
}
