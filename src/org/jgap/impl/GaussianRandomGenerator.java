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
import org.jgap.*;
import org.jgap.Configuration;

//
/**
 * Gaussian deviation serving as basis for randomly finding a number
 *
 * @author Klaus Meffert
 * @version 1.0
 */
public class GaussianRandomGenerator
    implements RandomGenerator
{
    /** String containing the CVS revision. Read out via reflection!*/
    private final static String CVS_REVISION = "$Revision: 1.1 $";

    private static final double DELTA = 0.0000001;

    private Random rn = new Random();

    /**
     * Mean of the gaussian deviation
     */
    private double m_mean;

    /**
     * Standard deviation of the gaussian deviation
     */
    private double m_standardDeviation;

    public GaussianRandomGenerator ()
    {
        this (0.0d);
    }

    /**
     * Constructor speicifying the (obliagtory) standard deviation
     * @param a_standardDeviation the standard deviation to use
     *
     * @since 1.1
     */
    public GaussianRandomGenerator (double a_standardDeviation)
    {
        super ();
        setGaussianStdDeviation (a_standardDeviation);
    }

    public void setGaussianMean (double a_mean)
    {
        m_mean = a_mean;
    }

    public void setGaussianStdDeviation (double a_standardDeviation)
    {
        m_standardDeviation = a_standardDeviation;
    }

    public double getGaussianStdDeviation ()
    {
        return m_standardDeviation;
    }

    /**
     * Calculates the mutation rate according to the gaussian derivation.
     * @param a_activeConfiguration current active configuration
     * @return calculated mutation rate
     *
     * @see http://tracer.lcc.uma.es/tws/cEA/GMut.htm
     * @see http://hyperphysics.phy-astr.gsu.edu/hbase/math/gaufcn.html
     * @since 1.1 (same functionality since earlier, but not encapsulated)
     */
    public int calculateDensity (double x)
    {
        if (Math.abs (m_standardDeviation) < DELTA)
        {
            throw new IllegalStateException (
                "Before using the Gaussian mutation"
                + " rate calculator, initialize it by setting the standard"
                + " deviation.");
        }

        //compute gaussian deviation:
        //f(x) = (1/sqrt(2pi*(stddev)²)*exp([-(x-stddev)²]/[2stddev²])
        //because x = a, the second term is eliminated
        //------------------------------------------------------------------

        int result;
        double rate, rate2;

        rate = (1 / Math.sqrt (2 * Math.PI * m_standardDeviation *
            m_standardDeviation));
        rate2 = Math.exp ( ( - (x -0.5d)*(x-0.5d)) / (2 * m_standardDeviation *
            m_standardDeviation));
        System.err.println ("rate: " + rate + ", rate2: " + rate2 + ", x=" + x);
        rate = rate * rate2;

        //width of the curve is approx. 6*m_standardDeviation

        //invert the result as higher values indicate less probable mutation
        //------------------------------------------------------------------
        /**@todo implement*/

        result = (int) Math.round (rate);
        return result;
    }

    public int nextInt ()
    {
        return rn.nextInt();
    }

    public int nextInt (int ceiling)
    {
        return nextInt() % ceiling;
    }

    public long nextLong ()
    {
        return rn.nextLong();
    }

    public double nextDouble ()
    {
        return rn.nextDouble();
    }

    public float nextFloat ()
    {
        return rn.nextFloat();
    }

    public boolean nextBoolean ()
    {
        return rn.nextBoolean();
    }


}