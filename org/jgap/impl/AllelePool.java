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
package org.jgap.impl;

import org.jgap.Allele;
import org.jgap.Configuration;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;


/**
 * Manages a pool of Allele instances from which alleles may be acquired and
 * released, thus allowing them to be easily resused to save on the number
 * of transient objects that would otherwise be created. Each instance of
 * this class is tied to a specific Configuration, which must be supplied
 * at construction time.
 */
public class AllelePool
{
    /**
     * A Map of allele pools managed by this class. Each key is the Class
     * of the pooled alleles, and the value is an ArrayList containing the
     * pool of those alleles.
     */
    private final Map m_allelePools;


    /**
     * References the current active Configuration object.
     */
    private final Configuration m_activeConfiguration;


    /**
     * Constructs a new AllelePool instance with the given active
     * Configuration.
     */
    public AllelePool( Configuration a_activeConfiguration )
    {
        m_allelePools = new HashMap();
        m_activeConfiguration = a_activeConfiguration;
    }


    /**
     * Attempts to acquire an Allele instance from the allele pool of the
     * given class and intended for the given gene position. It should
     * be noted that nothing is guaranteed about the value of the Allele and
     * it should be treated as undefined.
     *
     * @param a_alleleType The Class of the desired allele.
     * @param a_locus The gene position for which the allele is destined.
     *
     * @return An Allele instance from the pool (with an undefined value), or
     *         null if no allele instances are available in the pool.
     */
    public synchronized Allele acquireAllele( Class a_alleleType,
                                              int a_locus )
    {
        // First pull out all the Alleles of the given class type.
        // -------------------------------------------------------
        List[] classPool = (List[]) m_allelePools.get( a_alleleType );
        if( classPool == null )
        {
            return null;
        }
        else
        {
            // Now pull out all of the Alleles associated with the given
            // gene position.
            // ---------------------------------------------------------
            List locusPool = classPool[ a_locus ];
            if( locusPool == null || locusPool.isEmpty() )
            {
                return null;
            }
            else
            {
                // Remove the last allele in the pool and return it.
                // Note that removing the last allele (as opposed to the first
                // one) is an optimization because it prevents the ArrayList
                // from resizing itself.
                // -----------------------------------------------------------
                return (Allele) locusPool.remove( locusPool.size() - 1 );
            }
        }
     }


    /**
     * Releases an Allele to the pool. It's not required that the Allele
     * originated from the pool--any Allele can be released to it. This
     * method will invoke the cleanup() method on the Allele prior to
     * adding it back to the pool.
     *
     * @param a_alleleToRelease The Allele instance to be released back into
     *                          the pool.
     * @param a_locus The gene position at which the Allele was used.
     */
    public synchronized void releaseAllele( Allele a_alleleToRelease,
                                            int a_locus )
    {
        // First cleanup the allele before returning it back to the pool.
        // --------------------------------------------------------------
        a_alleleToRelease.cleanup();

        // Now add it to the pool appropriate for both its class and locus.
        // If no such pool exists, we create it.
        // ----------------------------------------------------------------
        Class alleleType = a_alleleToRelease.getClass();

        List[] classPool = (List[]) m_allelePools.get( alleleType );
        if( classPool == null )
        {
            classPool = new List[ m_activeConfiguration.getChromosomeSize() ];
            m_allelePools.put( alleleType, classPool );
        }

        List locusPool = classPool[ a_locus ];
        if( locusPool == null )
        {
            locusPool = new ArrayList();
            classPool[ a_locus ] = locusPool;
        }

        locusPool.add( a_alleleToRelease );
    }
}
