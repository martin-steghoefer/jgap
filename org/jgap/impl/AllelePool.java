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

import java.util.ArrayList;


/**
 * Manages a pool of Allele instances from which alleles may be acquired and
 * released, thus allowing them to be easily resused to save on the number
 * of transient objects that would otherwise be created. All of the methods
 * in this class are static, so it's unnecessary (and impossible) to create
 * an instance of this class.
 */
public class AllelePool
{
    /**
     * The pool of alleles managed by this class.
     */
    private static final ArrayList m_allelePool = new ArrayList();


    /**
     * Private constructor that should never be used since the methods in this
     * class are all static.
     */
    private AllelePool()
    {
    }


    /**
     * Attempts to acquire an Allele instance from the allele pool. It should
     * be noted that nothing is guaranteed about the value of the Allele and
     * it should be treated as undefined.
     *
     * @return An Allele instance from the pool (with an undefined value), or
     *         null if no allele instances are available in the pool.
     */
    public static synchronized Allele acquireAllele()
    {
        int poolSize = m_allelePool.size();

        if( poolSize > 0 )
        {
            // Fetch from the tail of the pool to avoid element
            // shifting within the ArrayList.
            // ------------------------------------------------
            return (Allele) m_allelePool.remove( poolSize - 1);
        }
        else
        {
            return null;
        }
     }

    /**
     * Releases an Allele to the pool. It's not required that the Allele
     * originated from the pool--any Allele can be released to it. This
     * method will invoke the cleanup() method on the Allele prior to
     * adding it back to the pool.
     */
    public static synchronized void releaseAllele( Allele a_alleleToRelease )
    {
        a_alleleToRelease.cleanup();
        m_allelePool.add( a_alleleToRelease );
    }

    public static int getPoolSize()
    {
        return m_allelePool.size();
    }
}
