/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.event;

import java.util.*;
import junit.framework.*;
import org.jgap.*;

/**
 * Tests for EventManager class
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class EventManagerTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.10 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(EventManagerTest.class);
    return suite;
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void testAddEventListener_0()
      throws Exception {
    IEventManager man = new EventManager();
    GeneticEventListener listener = new EventListener();
    Map listeners = (Map) privateAccessor.getField(man, "m_listeners");
    assertTrue(listeners.isEmpty());
    man.addEventListener("testeventname", listener);
    List listenersList = (List) listeners.get("testeventname");
    assertEquals(listener, listenersList.get(0));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testAddEventListener_1()
      throws Exception {
    IEventManager man = new EventManager();
    GeneticEventListener listener = new EventListener();
    Map listeners = (Map) privateAccessor.getField(man, "m_listeners");
    man.addEventListener("testeventname", listener);
    man.addEventListener("testeventname", listener);
    List listenersList = (List) listeners.get("testeventname");
    assertEquals(listener, listenersList.get(0));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void testRemoveEventListener_0()
      throws Exception {
    IEventManager man = new EventManager();
    GeneticEventListener listener = new EventListener();
    Map listeners = (Map) privateAccessor.getField(man, "m_listeners");
    man.addEventListener("testeventname", listener);
    List listenersList = (List) listeners.get("testeventname");
    man.removeEventListener("notfound", listener);
    assertEquals(listener, listenersList.get(0));
    man.removeEventListener("testeventname", null);
    assertEquals(listener, listenersList.get(0));
    man.removeEventListener("testeventname", listener);
    assertTrue( ( (List) listeners.get("testeventname")).size() == 0);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testFireGeneticeEvent_0()
      throws Exception {
    IEventManager man = new EventManager();
    GeneticEventListener listener = new EventListener();
    man.addEventListener("testeventname", listener);
    GeneticEvent genEvent = new GeneticEvent("wrong_name", this);
    man.fireGeneticEvent(genEvent);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testFireGeneticeEvent_1()
      throws Exception {
    IEventManager man = new EventManager();
    EventListener listener = new EventListener();
    man.addEventListener("testeventname", listener);
    GeneticEvent genEvent = new GeneticEvent("testeventname", this);
    man.fireGeneticEvent(genEvent);
    assertTrue(listener.m_fired);
    assertEquals(genEvent, listener.m_event);
  }

  /**
   *
   * @author Klaus Meffert
   */
  private class EventListener
      implements GeneticEventListener {
    private boolean m_fired;

    private GeneticEvent m_event;

    public void geneticEventFired(GeneticEvent a_firedEvent) {
      m_fired = true;
      m_event = a_firedEvent;
    }
  }
}
