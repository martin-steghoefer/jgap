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

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Test suite for all tests of package org.jgap.impl
 */
public class AllImplTests
    extends TestSuite
{

    /** String containing the CVS revision. Read out via reflection!*/
    private final static String CVS_REVISION = "$Revision: 1.4 $";

    public AllImplTests ()
    {

    }

    public static Test suite ()
    {
        TestSuite suite = new TestSuite ();
        suite.addTest (IntegerGeneTest.suite ());
        suite.addTest (DoubleGeneTest.suite ());
        suite.addTest (StringGeneTest.suite ());
        suite.addTest (BooleanGeneTest.suite ());
        suite.addTest (MutationOperatorTest.suite ());
        suite.addTest (CrossoverOperatorTest.suite ());
        suite.addTest (ReproductionOperatorTest.suite ());
        suite.addTest (DefaultConfigurationTest.suite ());
        suite.addTest (PoolTest.suite ());
        return suite;
    }

}