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

import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.Gene;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests for GaussianRandomGenerator class
 *
 * @author Klaus Meffert
 * @since 1.1
 */

public class GaussianRandomGeneratorTest
    extends TestCase
{

    /** String containing the CVS revision. Read out via reflection!*/
    private static final String CVS_REVISION = "$Revision: 1.1 $";

    private static final double DELTA = 0.000001;

    public GaussianRandomGeneratorTest ()
    {

    }

    public static Test suite ()
    {
        TestSuite suite = new TestSuite (GaussianRandomGeneratorTest.class);
        return suite;
    }

    /**@todo implement tests*/

    public void testCalculateCurrentRate_0() {
        Configuration conf = new DefaultConfiguration();
        GaussianRandomGenerator calc = new GaussianRandomGenerator();
        try {
            /**@todo finish*/
//            calc.calculateCurrentRate (conf);
//            fail();
        } catch (IllegalStateException iex) {
            ;//this is OK
        }
    }

    public void testCalculateCurrentRate_1() throws Exception {
        Configuration conf = new DefaultConfiguration();
        final double stdDeriv = 0.04d;
        GaussianRandomGenerator calc = new GaussianRandomGenerator (
            stdDeriv);
        assertEquals (stdDeriv, calc.getGaussianStdDeviation (), DELTA);
        Gene gene = new IntegerGene(1,5);
        Chromosome chrom = new Chromosome(gene, 50);
        conf.setSampleChromosome(chrom);
        /**@todo finish*/
    }

}