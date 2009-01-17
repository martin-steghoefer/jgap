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

/**
 * Represents objects that process genetic events. Once subscribed to an
 * event type with the EventManager, an object implementing this interface will
 * be notified each time a genetic event of that type is fired (until it is
 * unsubscribed).
 *
 * @author Neil Rotstan
 * @author Klaus Meffert
 * @since 1.0
 */
public interface GeneticEventListener {

  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.6 $";

  /**
   * Notify this GeneticEventListener that an event has been fired of a type
   * to which this listener is subscribed.
   *
   * @param a_firedEvent the event object that was fired. The type of the event
   * can be determined by the GeneticEvent's name
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  void geneticEventFired(GeneticEvent a_firedEvent);
}
