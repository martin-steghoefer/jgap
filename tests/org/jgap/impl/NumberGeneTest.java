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

import java.util.Vector;

import org.jgap.Configuration;
import org.jgap.Gene;
import org.jgap.RandomGenerator;
import org.jgap.UnsupportedRepresentationException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junitx.util.PrivateAccessor;

/**
 * Tests for (abstract) NumberGene class
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class NumberGeneTest
    extends TestCase
{
    /** String containing the CVS revision. Read out via reflection!*/
    private final static String CVS_REVISION = "$Revision: 1.2 $";

    public NumberGeneTest ()
    {
    }

    public static Test suite ()
    {
        TestSuite suite = new TestSuite (NumberGeneTest.class);
        return suite;
    }

    public void testConstruct_0 ()
    {
        Gene gene = new NumberGeneImpl (1, 100);
        //following should be possible without exception
        gene.setAllele (new Integer (101));
    }

    public void testToString_0 ()
    {
        Gene gene = new NumberGeneImpl (1, 100);
        gene.setAllele (new Integer (47));
        assertEquals ("47", gene.toString ());
    }

    public void testToString_1 ()
    {
        Gene gene = new NumberGeneImpl (1, 100);
        gene.setAllele (new Integer (102));
        int toString = Integer.parseInt (gene.toString ());
        assertTrue (toString >= 1 && toString <= 100);
    }

    public void testGetAllele_0 ()
    {
        Gene gene = new NumberGeneImpl (1, 100);
        gene.setAllele (new Integer (33));
        assertEquals (new Integer (33), gene.getAllele ());
    }

    public void testGetAllele_1 ()
    {
        Gene gene = new NumberGeneImpl (1, 100);
        gene.setAllele (new Integer (1));
        assertEquals (new Integer (1), gene.getAllele ());
    }

    public void testGetAllele_2 ()
    {
        Gene gene = new NumberGeneImpl (1, 100);
        gene.setAllele (new Integer (100));
        assertEquals (new Integer (100), gene.getAllele ());

    }

    public void testEquals_0 ()
    {
        Gene gene1 = new NumberGeneImpl (1, 100);
        Gene gene2 = new NumberGeneImpl (1, 100);
        assertTrue (gene1.equals (gene2));
        assertTrue (gene2.equals (gene1));
    }

    public void testEquals_1 ()
    {
        Gene gene1 = new NumberGeneImpl (1, 100);
        assertFalse (gene1.equals (null));
    }

    public void testEquals_2 ()
    {
        Gene gene1 = new NumberGeneImpl (1, 100);
        assertFalse (gene1.equals (new BooleanGene ()));
    }

    public void testEquals_3 ()
    {
        Gene gene1 = new NumberGeneImpl (1, 100);
        assertFalse (gene1.equals (new Vector ()));
    }

    public void testEquals_4 ()
    {
        Gene gene1 = new NumberGeneImpl (1, 100);
        Gene gene2 = new NumberGeneImpl (1, 99);
        assertTrue (gene1.equals (gene2));
        assertTrue (gene2.equals (gene1));
    }

    /**
     * Set Allele to null, no exception should occur
     */
    public void testSetAllele_0 ()
    {
        Gene gene1 = new NumberGeneImpl (1, 10000);
        gene1.setAllele (null);
    }

    public void testSetAllele_1 ()
    {
        Gene gene1 = new NumberGeneImpl (1, 10000);
        try
        {
            gene1.setAllele ("22");
            fail ();
        }
        catch (ClassCastException classex)
        {
            ; //this is OK
        }
    }

    public void testNewGene_0 ()
        throws Exception
    {
        Gene gene1 = new NumberGeneImpl (1, 10000);
        gene1.setAllele (new Integer (4711));
        Integer lower1 = (Integer) PrivateAccessor.getField (gene1,
            "m_lowerBounds");
        Integer upper1 = (Integer) PrivateAccessor.getField (gene1,
            "m_upperBounds");
        Gene gene2 = gene1.newGene (new DefaultConfiguration ());
        Integer lower2 = (Integer) PrivateAccessor.getField (gene2,
            "m_lowerBounds");
        Integer upper2 = (Integer) PrivateAccessor.getField (gene2,
            "m_upperBounds");
        assertEquals (lower1, lower2);
        assertEquals (upper1, upper2);
    }

    /**
     * Test implementation of NumberGene, based on IntegerGene
     *
     * @author Klaus Meffert
     * @since 1.1
     */
    private class NumberGeneImpl
        extends NumberGene
    {
        protected final static long INTEGER_RANGE = (long) Integer.MAX_VALUE -
            (long) Integer.MIN_VALUE;

        protected int m_upperBounds;

        protected int m_lowerBounds;

        protected long m_boundsUnitsToIntegerUnits;

        public NumberGeneImpl (int a_lowerBounds, int a_upperBounds)
        {
            m_lowerBounds = a_lowerBounds;
            m_upperBounds = a_upperBounds;
            calculateBoundsUnitsToIntegerUnitsRatio ();
        }

        public Gene newGene (Configuration a_activeConfiguration)
        {
            return new NumberGeneImpl (m_lowerBounds, m_upperBounds);
        }

        public String getPersistentRepresentation ()
            throws UnsupportedOperationException
        {
            return toString () + PERSISTENT_FIELD_DELIMITER + m_lowerBounds +
                PERSISTENT_FIELD_DELIMITER + m_upperBounds;
        }

        public void setValueFromPersistentRepresentation (String
            a_representation)
            throws UnsupportedRepresentationException
        {
            // not implemented here!
            // ---------------------
        }

        public void setToRandomValue (RandomGenerator a_numberGenerator)
        {
            // not implemented here!
            // ---------------------
        }

        public void applyMutation (int index, double a_percentage)
        {
            // not implemented here!
            // ---------------------

        }

        protected void calculateBoundsUnitsToIntegerUnitsRatio ()
        {
            int divisor = m_upperBounds - m_lowerBounds + 1;

            if (divisor == 0)
            {
                m_boundsUnitsToIntegerUnits = INTEGER_RANGE;
            }
            else
            {
                m_boundsUnitsToIntegerUnits = INTEGER_RANGE / divisor;
            }
        }

        protected void mapValueToWithinBounds ()
        {
            if (m_value != null)
            {
                Integer i_value = ( (Integer) m_value);
                // If the value exceeds either the upper or lower bounds, then
                // map the value to within the legal range. To do this, we basically
                // calculate the distance between the value and the integer min,
                // determine how many bounds units that represents, and then add
                // that number of units to the upper bound.
                // -----------------------------------------------------------------
                if (i_value.intValue () > m_upperBounds ||
                    i_value.intValue () < m_lowerBounds)
                {
                    long differenceFromIntMin = (long) Integer.MIN_VALUE +
                        (long) i_value.intValue ();
                    int differenceFromBoundsMin =
                        (int) (differenceFromIntMin /
                        m_boundsUnitsToIntegerUnits);

                    m_value =
                        new Integer (m_upperBounds + differenceFromBoundsMin);
                }
            }
        }

        protected int compareToNative (Object o1, Object o2)
        {
            return ( (Integer) o1).compareTo (o2);
        }
    }
}