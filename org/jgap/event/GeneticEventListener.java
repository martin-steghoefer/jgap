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


/**
 * Represents objects that process genetic events. Once subscribed to an
 * EventManager, an object implementing this interface will be notified
 * via the appropriate method each time a genetic event is fired.
 */
public interface GeneticEventListener
{
    /**
     * Notify this GeneticEventListener that a genotype has been evolved.
     *
     * @param a_evolutionEvent The event object representing the evolution
     *                         of a Genotype.
     */
    void genotypeEvolved( GenotypeEvent a_evolutionEvent );
}

