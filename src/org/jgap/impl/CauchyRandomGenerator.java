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

import java.util.Random;

import org.jgap.RandomGenerator;

/**@todo not ready yet*/
/**@todo not ready yet*/
/**@todo not ready yet*/


/**
 * Cauchy probability density function
 * @see http://www.itl.nist.gov/div898/handbook/eda/section3/eda3663.htm
 *
 * @author Klaus Meffert
 * @since 1.1
 */

public class CauchyRandomGenerator implements RandomGenerator
{
    /** String containing the CVS revision. Read out via reflection!*/
    private final static String CVS_REVISION = "$Revision: 1.1 $";

    private static final double DELTA = 0.0000001;

    private Random rn = new Random ();

    public CauchyRandomGenerator ()
    {
    }

    /**
     * Calculates the density of the cauchy distribution for a given input
     * @param a_x input for computation
     * @return calculated density
     *
     * @see http://tracer.lcc.uma.es/tws/cEA/GMut.htm
     * @see http://hyperphysics.phy-astr.gsu.edu/hbase/math/gaufcn.html
     * @since 1.1 (same functionality since earlier, but not encapsulated)
     */
    private int calculateDensity (double a_x)
    {
        //compute (standard) cauchy distribution:
        //f(x) = (1/[pi(1+x²)])
        //----------------------------

        int result;
        double rate;

        rate = 1 / (Math.PI * (1+a_x*a_x));

        //invert the result as higher values indicate less probable mutation
        //------------------------------------------------------------------
        /**@todo implement*/

        result = (int) Math.round (rate);
        return result;
    }

    public int nextInt ()
    {
        return rn.nextInt ();
    }

    public int nextInt (int ceiling)
    {
        return nextInt () % ceiling;
    }

    public long nextLong ()
    {
        return rn.nextLong ();
    }

    public double nextDouble ()
    {
        return rn.nextDouble ();
    }

    public float nextFloat ()
    {
        return rn.nextFloat ();
    }

    public boolean nextBoolean ()
    {
        return rn.nextBoolean ();
    }

}