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
import java.util.List;


/**
 * Manages a pool of Allele instances from which alleles may be acquired and
 * released, thus allowing them to be easily resused to save on the number
 * of transient objects that would otherwise be created. Each instance of
 * this class is tied to a specific Configuration, which must be supplied
 * at time of initialization.
 */
public class AllelePool
{
    /**
     * An array of allele pools managed by this class. Each index of the
     * array corresponds to a locus (gene position) in the Chromosome setup
     * currently in use. Each List stores the pooled Alleles for that gene
     * position.
     */
    private List[] m_allelePools = null;

    /**
     * References the current active Configuration object.
     */
    private Configuration m_activeConfiguration;

    /**
     * Constructor.
     */
    public AllelePool()
    {
        // Nothing to do. All the action happens in the initialize() call.
        // We don't take in the Configuration object here because this
        // AllelePool itself will be created during the process of setting
        // up the Configuration, which means that when this constructor is
        // called, the Configuration may not be completely setup yet.
        // ---------------------------------------------------------------
    }


    /**
     * Initialize this pool with the given active Configuration.
     *
     * @param a_activeConfiguration the current active configuration
     */
    public void initialize( Configuration a_activeConfiguration )
    {
        m_activeConfiguration = a_activeConfiguration;
        m_allelePools = new List[ m_activeConfiguration.getChromosomeSize() ];
    }


    /**
     * Attempts to acquire an Allele instance from the allele pool that is
     * intended for the given gene position. It should be noted that nothing
     * is guaranteed about the value of the Allele and it should be treated as
     * undefined.
     *
     * @param a_locus The gene position for which the allele is destined.
     *
     * @return An Allele instance from the pool (with an undefined value), or
     *         null if no allele instances are available in the pool for the
     *         given gene position.
     */
    public synchronized Allele acquireAllele( int a_locus )
    {
        // Pull out all of the Alleles associated with the given gene
        // position.
        // ----------------------------------------------------------
        List locusPool = m_allelePools[ a_locus ];
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

        // Now add it to the pool appropriate for its locus. If no such
        // pool exists, we create it.
        // ------------------------------------------------------------
        List locusPool = m_allelePools[ a_locus ];
        if( locusPool == null )
        {
            locusPool = new ArrayList();
            m_allelePools[ a_locus ] = locusPool;
        }

        locusPool.add( a_alleleToRelease );
    }
}
