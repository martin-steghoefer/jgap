/*
 * Copyright 2003 Klaus Meffert
 *
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

import java.util.List;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junitx.util.PrivateAccessor;


/**
 * Tests for Chromosome class
 */

public class EventManagerTest extends TestCase {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  public EventManagerTest() {

  }

  public static Test suite() {
    TestSuite suite = new TestSuite(EventManagerTest.class);
    return suite;
  }

  public void testAddEventListener_0() throws Exception {
    EventManager man = new EventManager();
    GeneticEventListener listener = new EventListener();
    Map listeners = (Map)PrivateAccessor.getField(man, "m_listeners");
    assertTrue(listeners.isEmpty());
    man.addEventListener("testeventname", listener);
    List listenersList =(List)listeners.get("testeventname");
    assertEquals(listener, listenersList.get(0));
  }

  public void testRemoveEventListener_0() throws Exception {
    EventManager man = new EventManager();
    GeneticEventListener listener = new EventListener();
    Map listeners = (Map)PrivateAccessor.getField(man, "m_listeners");
    man.addEventListener("testeventname", listener);
    List listenersList =(List)listeners.get("testeventname");
    man.removeEventListener("notfound",listener);
    assertEquals(listener, listenersList.get(0));
    man.removeEventListener("testeventname",null);
    assertEquals(listener, listenersList.get(0));
    man.removeEventListener("testeventname",listener);
    assertTrue(((List)listeners.get("testeventname")).size()==0);
  }

  private class EventListener implements  GeneticEventListener {

    public void geneticEventFired(GeneticEvent a_firedEvent) {
    }
  }

}