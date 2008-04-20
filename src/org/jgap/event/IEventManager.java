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

import java.io.*;

/**
 * Interface for event managers (e.g., see class EventManager).
 *
 * @author Klaus Meffert
 * @author Neil Rotstan
 * @since 2.6
 */
public interface IEventManager
    extends Serializable {
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
   * @since 2.6 (since 1.0 in EventManager)
   */
  void addEventListener(String a_eventName,
                        GeneticEventListener a_eventListenerToAdd);

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
   * @since 2.6 (since 1.0 in EventManager)
   */
  void removeEventListener(String a_eventName,
                           GeneticEventListener a_eventListenerToRemove);

  /**
   * Fires a genetic event. All subscribers of that particular event type
   * (as determined by the name of the event) will be notified of it
   * having been fired.
   *
   * @param a_eventToFire the representation of the genetic event to fire
   *
   * @author Neil Rotstan
   * @since 2.6 (since 1.0 in EventManager)
   */
  void fireGeneticEvent(GeneticEvent a_eventToFire);
}
