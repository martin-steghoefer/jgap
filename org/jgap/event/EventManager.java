/*
 * Copyright 2001, Neil Rotstan
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

import org.jgap.*;
import java.util.*;

/**
 * Manages event notification in the system.
 */
public class EventManager
{
  private List listeners = new ArrayList();

  /**
   * Adds a new listener that will be notified when
   * Genetic Events happen.
   */
  public synchronized void addEventListener( GeneticEventListener e )
  {
    listeners.add( e );
  }

  /**
   * Removes a listener.
   */
  public synchronized void removeEventListener( GeneticEventListener e )
  {
    if ( listeners.contains( e ) )
    {
      listeners.remove( e );
    }
  }

  /**
   * Fires a Genotype Evolved Event.
   */
  public void fireGenotypeEvolvedEvent( GenotypeEvent event )
  {
    if ( listeners.size() < 1 ) return;
 
    List currentListeners = new ArrayList();
    synchronized( this )
    {
      currentListeners.addAll( listeners );
    }

    for ( int i = 0; i < listeners.size(); i++ )
    {
      ((GeneticEventListener)currentListeners.get(i)).genotypeEvolved( event );
    }
  }

}
