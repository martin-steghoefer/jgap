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

import org.jgap.Genotype;

import java.util.EventObject;

/**
 * Serves as events that are fired via an EventManager when various
 * events related to Genotypes occur. The specific kind of event is
 * conveyed through the specific EventManager method that is used to
 * fire the event.
 */
public class GenotypeEvent extends EventObject
{
    /**
     * Constructs a new GenotypeEvent that is related to the given
     * Genotype object.
     *
     * @param a_source The Genotype that acted as the source of the event.
     */
    public GenotypeEvent( Genotype a_source )
    {
        super( a_source );
    }
}
