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

import org.jgap.Gene;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junitx.util.PrivateAccessor;

/**
 * Tests for DoubleGene class
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class DoubleGeneTest
    extends TestCase
{
    private static final double DELTA = 0.0001d;

    public DoubleGeneTest ()
    {
    }

    public static Test suite ()
    {
        TestSuite suite = new TestSuite (DoubleGeneTest.class);
        return suite;
    }

    public void testConstruct_0 ()
    {
        Gene gene = new DoubleGene (1.1d, 100.1d);
        //following should be possible without exception
        gene.setAllele (new Double (101.1d));
    }

    public void testToString_0 ()
    {
        Gene gene = new DoubleGene (1.2d, 99.7d);
        gene.setAllele (new Double (47.3d));
        assertEquals ("47.3", gene.toString ());
    }

    public void testToString_1 ()
    {
        Gene gene = new DoubleGene (1.0d, 100.0d);
        gene.setAllele (new Double (102));
        double toString = Double.parseDouble (gene.toString ());
        assertTrue (toString >= 1 && toString <= 100);
    }

    public void testGetAllele_0 ()
    {
        Gene gene = new DoubleGene (1.9d, 100.4d);
        gene.setAllele (new Double (33.0d));
        assertEquals (new Double (33.0d), gene.getAllele ());
    }

    public void testGetAllele_1 ()
    {
        Gene gene = new DoubleGene (1.8d, 100.1d);
        gene.setAllele (new Double (1.9d));
        assertEquals (new Double (1.9d), gene.getAllele ());
    }

    public void testGetAllele_2 ()
    {
        Gene gene = new DoubleGene (1.0d, 100.0d);
        gene.setAllele (new Double (100.0d));
        assertEquals (new Double (100.0d), gene.getAllele ());
    }

    public void testEquals_0 ()
    {
        Gene gene1 = new DoubleGene (1.1d, 100.2d);
        Gene gene2 = new DoubleGene (1.1d, 100.2d);
        assertTrue (gene1.equals (gene2));
        assertTrue (gene2.equals (gene1));
    }

    public void testEquals_1 ()
    {
        Gene gene1 = new DoubleGene (1.9d, 100.4d);
        assertFalse (gene1.equals (null));
    }

    public void testEquals_2 ()
    {
        Gene gene1 = new DoubleGene (11.2d, 100.7d);
        assertFalse (gene1.equals (new BooleanGene ()));
    }

    public void testEquals_3 ()
    {
        Gene gene1 = new DoubleGene (1.0d, 100.7d);
        assertFalse (gene1.equals (new Vector ()));
    }

    public void testEquals_4 ()
    {
        Gene gene1 = new DoubleGene (1.2d, 100.3d);
        Gene gene2 = new DoubleGene (1.2d, 99.5d);
        assertTrue (gene1.equals (gene2));
        assertTrue (gene2.equals (gene1));
    }

    public void testDoubleValue_0 ()
    {
        DoubleGene gene1 = new DoubleGene (1.0d, 10000.0d);
        gene1.setAllele (new Double (4711.0d));
        assertEquals (4711.0d, gene1.doubleValue (), DELTA);
    }

    public void testDoubleValue_1 ()
    {
        DoubleGene gene1 = new DoubleGene (1.765d, 10000.0d);
        gene1.setAllele (null);
        try
        {
            assertEquals (0.0d, gene1.doubleValue (), DELTA);
            fail ();
        }
        catch (NullPointerException nullex)
        {
            ; //this is OK
        }
    }

    /**
     * Set Allele to null, no exception should occur
     */
    public void testSetAllele_0 ()
    {
        Gene gene1 = new DoubleGene (1.0d, 10000.0d);
        gene1.setAllele (null);
    }

    public void testSetAllele_1 ()
    {
        Gene gene1 = new DoubleGene (1.0d, 10000.0d);
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

    public void testNewGene_0 () throws Exception
    {
        Gene gene1 = new DoubleGene (1.0d, 10000.0d);
        gene1.setAllele (new Double (4711.0d));
        Double lower1 = (Double) PrivateAccessor.getField (gene1,
            "m_lowerBounds");
        Double upper1 = (Double) PrivateAccessor.getField (gene1,
            "m_upperBounds");
        Gene gene2 = gene1.newGene (new DefaultConfiguration ());
        Double lower2 = (Double) PrivateAccessor.getField (gene2,
            "m_lowerBounds");
        Double upper2 = (Double) PrivateAccessor.getField (gene2,
            "m_upperBounds");
        assertEquals (lower1, lower2);
        assertEquals (upper1, upper2);
    }

    public void testPersistentRepresentation_0() throws Exception {
        Gene gene1 = new DoubleGene (2.05d, 7.53d);
        gene1.setAllele(new Double(4.5d));
        String pres1 = gene1.getPersistentRepresentation();
        Gene gene2 = new DoubleGene ();
        gene2.setValueFromPersistentRepresentation(pres1);
        String pres2 = gene2.getPersistentRepresentation();
        assertEquals(pres1, pres2);
    }
}