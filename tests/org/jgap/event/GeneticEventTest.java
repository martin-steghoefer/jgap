package org.jgap.event;

import junit.framework.*;

/**
 * <p>Title: Tests for GeneticEvent class</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * @author Klaus Meffert
 */

public class GeneticEventTest extends TestCase {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  public GeneticEventTest() {
  }

  public static Test suite() {
    TestSuite suite = new TestSuite(GeneticEventTest.class);
    return suite;
  }

  public void testConstruct_0() {
    try {
      GeneticEvent event = new GeneticEvent("testEventName", null);
      fail();
    }
    catch (IllegalArgumentException illex) {
      ; //this is OK
    }
  }

  public void testConstruct_1() {
    GeneticEvent event = new GeneticEvent(null, this);
  }

  public void testGetEventName_0() {
    GeneticEvent event = new GeneticEvent("testEventName", this);
    assertEquals("testEventName", event.getEventName());
  }

  public void testGENOTYPE_EVOLVED_EVENT_0() {
    assertTrue(GeneticEvent.GENOTYPE_EVOLVED_EVENT != null);
    assertTrue(GeneticEvent.GENOTYPE_EVOLVED_EVENT.length() > 0);
  }
}