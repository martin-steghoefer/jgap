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

/**
 * Tests for StringGene class
 */
public class StringGeneTest
    extends TestCase
{
    public StringGeneTest ()
    {
    }

    public static Test suite ()
    {
        TestSuite suite = new TestSuite (StringGeneTest.class);
        return suite;
    }

    public void testConstruct_0 ()
    {
        Gene gene = new StringGene (1, 100);
        //following should be possible without exception
        gene.setAllele ("ABC");
    }

    public void testConstruct_1 ()
    {
        try
        {
            Gene gene = new StringGene (1, 0);
            fail ();
        }
        catch (IllegalArgumentException iex)
        {
            ; //this is OK
        }
    }

    public void testConstruct_2 ()
    {
        try
        {
            Gene gene = new StringGene ( -1, 3);
            fail ();
        }
        catch (IllegalArgumentException iex)
        {
            ; //this is OK
        }
    }

    public void testConstruct_3 ()
    {
        try
        {
            Gene gene = new StringGene (1, 3);
            gene.setAllele ("ABCD");
        }
        catch (IllegalArgumentException iex)
        {
            ; //this is OK
        }
    }

    public void testConstruct_4 ()
    {
        try
        {
            Gene gene = new StringGene (1, 3);
            gene.setAllele (new Double (2.3d));
        }
        catch (ClassCastException castex)
        {
            ; //this is OK
        }
    }

    public void testSetAlphabet_0 ()
    {
        StringGene gene = new StringGene (3, 5);
        try
        {
            gene.setAlphabet ("1" + Gene.PERSISTENT_FIELD_DELIMITER);
            fail ();
        }
        catch (IllegalArgumentException iex)
        {
            ; //this is OK
        }
    }

    public void testToString_0 ()
    {
        Gene gene = new StringGene (3, 7);
        gene.setAllele ("ABC");
        assertEquals ("ABC", gene.toString ());
    }

    public void testGetAllele_0 ()
    {
        Gene gene = new StringGene (3, 5);
        gene.setAllele ("BCD");
        assertEquals ("BCD", gene.getAllele ());
    }

    public void testEquals_0 ()
    {
        Gene gene1 = new StringGene (1, 100);
        Gene gene2 = new StringGene (1, 100);
        assertTrue (gene1.equals (gene2));
        assertTrue (gene2.equals (gene1));
    }

    public void testEquals_1 ()
    {
        Gene gene1 = new StringGene (3, 100);
        assertFalse (gene1.equals (null));
    }

    public void testEquals_2 ()
    {
        Gene gene1 = new StringGene (11, 77);
        assertTrue (gene1.equals (new StringGene ()));
    }

    public void testEquals_3 ()
    {
        Gene gene1 = new StringGene (11, 17);
        assertFalse (gene1.equals (new Vector ()));
    }

    public void testEquals_4 ()
    {
        Gene gene1 = new StringGene (12, 100);
        Gene gene2 = new StringGene (12, 99);
        assertTrue (gene1.equals (gene2));
        assertTrue (gene2.equals (gene1));
    }

    public void testEquals_5 ()
    {
        Gene gene1 = new StringGene (2, 5);
        Gene gene2 = new StringGene (1, 5);
        gene1.setAllele("ABC");
        gene2.setAllele("AB");
        assertFalse (gene1.equals (gene2));
        assertFalse (gene2.equals (gene1));
    }

    /**
     * Set Allele to null, no exception should occur
     */
    public void testSetAllele_0 ()
    {
        Gene gene1 = new StringGene (0, 10000);
        gene1.setAllele (null);
    }

    public void testSetAllele_1 ()
    {
        Gene gene1 = new StringGene (3, 4);
        try
        {
            gene1.setAllele ("AB");
            fail ();
        }
        catch (IllegalArgumentException iex)
        {
            ; //this is OK
        }
    }

    public void testNewGene_0 () throws Exception
    {
        StringGene gene1 = new StringGene (1, 4);
        gene1.setAllele ("XYZ");
        int minLength1 = gene1.getMinLength();
        int maxLength1 = gene1.getMaxLength();
        StringGene gene2 = (StringGene)gene1.newGene (new DefaultConfiguration ());
        int minLength2 = gene2.getMinLength();
        int maxLength2 = gene2.getMaxLength();
        assertEquals (minLength1, minLength2);
        assertEquals (maxLength1, maxLength2);
    }

}