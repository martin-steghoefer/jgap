/*
 * Copyright 2003 Klaus Meffert
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
package org.jgap.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.jgap.InvalidConfigurationException;
import org.jgap.NaturalSelector;

/**
 * Ordered chain of NaturalSelectors. With this container you can plugin
 * NaturalSelector implementations which will be performed either before (pre-)
 * or after (post-selectors) registered genetic operations have been applied.
 * @see Genotype.evolve
 * @see Configuration.addNaturalSelector
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class ChainOfSelectors
{
    /** String containing the CVS revision. Read out via reflection!*/
    private final static String CVS_REVISION = "$Revision: 1.3 $";

    /**
     * Ordered list holding the NaturalSelector's.
     * Intentionally used as a decorator and not via inheritance!
     */
    private List selectors;

    public ChainOfSelectors ()
    {
        selectors = new Vector ();
    }

    /**
     * Adds a natural selector to the chain
     * @param a_selector the selector to be added
     * @throws InvalidConfigurationException
     *
     * @since 1.1 (previously part of class Configuration)
     */
    public void addNaturalSelector (NaturalSelector a_selector)
        throws InvalidConfigurationException
    {
        if (a_selector == null)
        {
            throw new InvalidConfigurationException (
                "This Configuration object is locked. Settings may not be " +
                "altered.");
        }
        selectors.add (a_selector);
    }

    public void addAll (Collection c)
        throws InvalidConfigurationException
    {
        Iterator it = c.iterator ();
        while (it.hasNext ())
        {
            NaturalSelector selector = (NaturalSelector) it.next ();
            addNaturalSelector (selector);
        }
    }

    public int size ()
    {
        return selectors.size ();
    }

    public boolean isEmpty ()
    {
        return size () == 0;
    }

    public int hashCode ()
    {
        return selectors.hashCode ();
    }

    public boolean equals (Object o)
    {
        return selectors.equals (o);
    }

    public NaturalSelector get (int index)
    {
        return (NaturalSelector) selectors.get (index);
    }

    public void clear() {
        selectors.clear();
    }

    public Iterator iterator() {
        return selectors.iterator();
    }
}