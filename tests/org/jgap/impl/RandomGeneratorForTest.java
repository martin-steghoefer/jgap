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

import java.util.List;

import org.jgap.RandomGenerator;

/**
 * A random generator only determined for testing purposes.
 * With this, you can specify the next value which will be returned.
 * It is also possible to specify a sequence to be appearing
 *
 * @author Klaus Meffert
 * @since 1.1
 */

public class RandomGeneratorForTest
    implements RandomGenerator
{

    /** String containing the CVS revision. Read out via reflection!*/
    private static final String CVS_REVISION = "$Revision: 1.4 $";

    private int m_nextInt;
    private long m_nextLong;
    private double m_nextDouble;
    private float m_nextFloat;
    private boolean m_nextBoolean;
    private int[] m_nextIntSequence;
    private int m_intIndex;

    public RandomGeneratorForTest ()
    {

    }

    public int nextInt ()
    {
        int result = m_nextIntSequence[m_intIndex++];
        if (m_intIndex >= m_nextIntSequence.length) {
            m_intIndex = 0;
        }
        return result;
    }

    public int nextInt (int ceiling)
    {
        return nextInt() % ceiling;
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
        setNextIntSequence(new int[]{a_nextInt});
    }

    public void setNextLong (long a_nextLong)
    {
        m_nextLong = a_nextLong;
    }

    public void setNextIntSequence(int[] a_sequence) {
        m_intIndex = 0;
        m_nextIntSequence = a_sequence;
    }

}