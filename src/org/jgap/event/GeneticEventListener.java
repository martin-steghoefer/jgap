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

/**
 * Represents objects that process genetic events. Once subscribed to an
 * event type with the EventManager, an object implementing this interface will
 * be notified each time a genetic event of that type is fired (until it is
 * unsubscribed).
 *
 * @author Neil Rotstan
 * @since 1.0
 */
public interface GeneticEventListener {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.2 $";

  /**
   * Notify this GeneticEventListener that an event has been fired of a type
   * to which this listener is subscribed.
   *
   * @param a_firedEvent The event object that was fired. The type of event
   *                     can be determined by the GeneticEvent's name.
   *
   * @author Neil Rotstan
   * @since 1.0
   */
  void geneticEventFired(GeneticEvent a_firedEvent);
}
