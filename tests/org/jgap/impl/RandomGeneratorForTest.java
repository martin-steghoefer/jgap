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

import org.jgap.RandomGenerator;

/**
 * A random generator only determined for testing purposes.
 * With this, you can specify the next value which will be returned
 *
 * @author Klaus Meffert
 * @since 1.1
 */

public class RandomGeneratorForTest
    implements RandomGenerator
{

    /** String containing the CVS revision. Read out via reflection!*/
    private static final String CVS_REVISION = "$Revision: 1.3 $";

    private int m_nextInt;
    private long m_nextLong;
    private double m_nextDouble;
    private float m_nextFloat;
    private boolean m_nextBoolean;

    public RandomGeneratorForTest ()
    {

    }

    public int nextInt ()
    {
        return m_nextInt;
    }

    public int nextInt (int ceiling)
    {
        return m_nextInt % ceiling;
    }

    public long nextLong ()
    {
        return m_nextLong;
    }

    public double nextDouble ()
    {
        return m_nextDouble;
    }

    public float nextFloat ()
    {
        return m_nextFloat;
    }

    public boolean nextBoolean ()
    {
        return m_nextBoolean;
    }

    public void setNextBoolean (boolean a_nextBoolean)
    {
        m_nextBoolean = a_nextBoolean;
    }

    public void setNextDouble (double a_nextDouble)
    {
        m_nextDouble = a_nextDouble;
    }

    public void setNextFloat (float a_nextFloat)
    {
        m_nextFloat = a_nextFloat;
    }

    public void setNextInt (int a_nextInt)
    {
        m_nextInt = a_nextInt;
    }

    public void setNextLong (long a_nextLong)
    {
        m_nextLong = a_nextLong;
    }

}