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
package org.jgap;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Title: Tests for the FitnessFunction class
 */
public class FitnessFunctionTest
    extends TestCase
{

    public FitnessFunctionTest ()
    {
    }

    public static Test suite ()
    {
        TestSuite suite = new TestSuite (FitnessFunctionTest.class);
        return suite;
    }

    public void testGetFitnessValue_0 ()
    {
        FitnessFunctionImpl fitfunc = new FitnessFunctionImpl (7);
        assertEquals (7, fitfunc.getFitnessValue (null));
    }

    public void testGetFitnessValue_1 ()
    {
        FitnessFunctionImpl fitfunc = new FitnessFunctionImpl ( -7);
        fitfunc.getFitnessValue (null);
        // No exception is expected for negative fitness value
    }

    public void testGetFitnessValue_2 ()
    {
        FitnessFunctionImpl fitfunc = new FitnessFunctionImpl (0);
        fitfunc.getFitnessValue (null);
        // No exception is expected for negative fitness value
    }

    /**
     * Implementing class of abstract FitnessFunction class
     * @author Klaus Meffert
     * @version 1.0
     */

    private class FitnessFunctionImpl
        extends FitnessFunction
    {
        private int evaluationValue;

        public FitnessFunctionImpl (int evaluationValue)
        {
            this.evaluationValue = evaluationValue;
        }

        protected int evaluate (Chromosome a_subject)
        {
            return evaluationValue;
        }
    }

}