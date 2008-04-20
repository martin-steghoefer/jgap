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
import org.jgap.util.*;

/**
 * Manages event notification in the system. Observers that desire to be
 * notified of genetic events should subscribe to this class via the
 * addEventListener() method. To unsubscribe, use the removeEventListener()
 * method. To generate a genetic event, use the fireGeneticEvent() method,
 * which will take care of notifying the appropriate subscribers.
 *
 * @author Neil Rotstan
 * @author Klaus Meffert
 * @since 1.0
 */
public class EventManager
    implements IEventManager, ICloneable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.9 $";

  /**
   * References a Map of subscribed event listeners. Each key is an event
   * name, and each value is a List of listeners subscribed to that event.
   */
  private HashMap m_listeners = new HashMap();

  /**
   * Adds a new listener that will be notified when the event represented
   * by the given name is fired.
   *
   * @param a_eventName the name of the event to which the given listener
   * should be subscribed. Standard events are represented by constants in the
   * GeneticEvent class
   * @param a_eventListenerToAdd the genetic listener to subscribe to
   * notifications of the given event
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public synchronized void addEventListener(final String a_eventName,
      final GeneticEventListener a_eventListenerToAdd) {
    List eventListeners = (List) m_listeners.get(a_eventName);
    if (eventListeners == null) {
      eventListeners = new LinkedList();
      m_listeners.put(a_eventName, eventListeners);
    }
    eventListeners.add(a_eventListenerToAdd);
  }

  /**
   * Removes the given listener from subscription of the indicated event.
   * The listener will no longer be notified when the given event occurs.
   *
   * @param a_eventName the name of the event to which the given listener
   * should be removed. Standard events are represented by constants in the
   * GeneticEvent class
   * @param a_eventListenerToRemove the genetic listener to unsubscribe from
   * notifications of the given event
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public synchronized void removeEventListener(final String a_eventName,
      final GeneticEventListener a_eventListenerToRemove) {
    List eventListeners = (List) m_listeners.get(a_eventName);
    if (eventListeners != null) {
      eventListeners.remove(a_eventListenerToRemove);
    }
  }

  /**
   * Fires a genetic event. All subscribers of that particular event type
   * (as determined by the name of the event) will be notified.
   *
   * @param a_eventToFire the representation of the genetic event to fire
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  public synchronized void fireGeneticEvent(final GeneticEvent a_eventToFire) {
    List eventListeners =
        (List) m_listeners.get(a_eventToFire.getEventName());
    if (eventListeners != null) {
      // Iterate over the listeners and notify each one of the event.
      // ------------------------------------------------------------
      Iterator listenerIterator = eventListeners.iterator();
      while (listenerIterator.hasNext()) {
        ( (GeneticEventListener) listenerIterator.next()).
            geneticEventFired(a_eventToFire);
      }
    }
  }

  /**
   * @return hashcode
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public int hashCode() {
    int result = m_listeners.hashCode();
    return result;
  }

  /**
   * @return cloned instance
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public Object clone() {
    EventManager result = new EventManager();
    if (!m_listeners .isEmpty()) {
      result.m_listeners = (HashMap)m_listeners.clone();
    }
    return result;
  }
}
