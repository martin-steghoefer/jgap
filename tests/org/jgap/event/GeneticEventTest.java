/*
 * This file is part of JGAP.
 *
 * JGAP is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * JGAP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser Public License
 * along with JGAP; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package org.jgap.event;

import junit.framework.*;

/**
 * Tests for GeneticEvent class
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class GeneticEventTest
    extends TestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  public GeneticEventTest() {

  }

  public static Test suite() {
    TestSuite suite = new TestSuite(GeneticEventTest.class);
    return suite;
  }

  public void testConstruct_0() {
    try
    {
      GeneticEvent event = new GeneticEvent("testEventName", null);
      fail();
    }
    catch (IllegalArgumentException illex)
    {
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
