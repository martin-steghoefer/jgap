package org.jgap.event;

import java.util.*;
import junit.framework.*;
import junitx.util.*;

import org.jgap.impl.*;

/**
 * <p>Title: Tests for Chromosome class</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * @author Klaus Meffert
 */

public class EventManagerTest extends TestCase {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

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