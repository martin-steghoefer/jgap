/*
 * Copyright 2001, 2002 Neil Rotstan
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

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;


/**
 * Manages event notification in the system. Observers that desire to be
 * notified of genetic events should subscribe to this class via the
 * addEventListener() method. To unsubscribe, use the removeEventListener()
 * method.
 * <p>
 * To generate a genetic event, one of the fire*() methods should be used.
 * These will take care of notifying all appropriate subscribers.
 */
public class EventManager
{
    private List listeners = new ArrayList();


    /**
     * Adds a new listener that will be notified when genetic events occur.
     *
     * @param a_eventListenerToAdd the genetic listener to subscribe to the
     *                             genetic event notifications.
     */
    public synchronized void addEventListener(
                                 GeneticEventListener a_eventListenerToAdd )
    {
        listeners.add( a_eventListenerToAdd );
    }


    /**
     * Removes the given listener from the event subscribers. The listener
     * will no longer be notified when genetic events occur.
     *
     * @param a_eventListenerToRemove the genetic listener to unsubscribe from
     *                                genetic event notifications.
     */
    public synchronized void removeEventListener(
                                 GeneticEventListener a_eventListenerToRemove )
    {
        if ( listeners.contains( a_eventListenerToRemove ) )
        {
            listeners.remove( a_eventListenerToRemove );
        }
    }


    /**
     * Fires a Genotype Evolved Event. All subscribers will be notified
     * of the event.
     *
     * @param a_eventToFire The representation of the GenotypeEvent to fire.
     */
    public void fireGenotypeEvolvedEvent( GenotypeEvent a_eventToFire )
    {
        // If there are no listeners, there's nothing to do.
        // -------------------------------------------------
        if ( listeners.isEmpty() )
        {
            return;
        }

        // Make a copy of the list of current subscribers. Otherwise, we have
        // to synchronize and lock everyone out while we perform notifications,
        // which could really slow things down since we have no idea what each
        // of the listeners is going to do. This way, worst case is that there
        // may be a bit of a delay before other subscribers are notified.
        // --------------------------------------------------------------------
        List currentListeners = new ArrayList();
        synchronized( this )
        {
            currentListeners.addAll( listeners );
        }

        // Iterate over the listeners and notify each one of the event.
        // ------------------------------------------------------------
        Iterator listenerIterator = currentListeners.iterator();
        while( listenerIterator.hasNext() )
        {
            ( (GeneticEventListener) listenerIterator.next() ).genotypeEvolved(
                                         a_eventToFire );
        }
    }
}
