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
 * A random generator only determined for testing purposes
 * With this, you can specify the next value which will be returned
 */

public class RandomGeneratorForTest
    implements RandomGenerator
{

    /** String containing the CVS revision. Read out via reflection!*/
    private static final String CVS_REVISION = "$Revision: 1.2 $";

    private int _nextInt;
    private long _nextLong;
    private double _nextDouble;
    private float _nextFloat;
    private boolean _nextBoolean;

    public RandomGeneratorForTest ()
    {

    }

    public int nextInt ()
    {
        return _nextInt;
    }

    public int nextInt (int ceiling)
    {
        return _nextInt % ceiling;
    }

    public long nextLong ()
    {
        return _nextLong;
    }

    public double nextDouble ()
    {
        return _nextDouble;
    }

    public float nextFloat ()
    {
        return _nextFloat;
    }

    public boolean nextBoolean ()
    {
        return _nextBoolean;
    }

    public void set_nextBoolean (boolean _nextBoolean)
    {
        this._nextBoolean = _nextBoolean;
    }

    public void set_nextDouble (double _nextDouble)
    {
        this._nextDouble = _nextDouble;
    }

    public void set_nextFloat (float _nextFloat)
    {
        this._nextFloat = _nextFloat;
    }

    public void set_nextInt (int _nextInt)
    {
        this._nextInt = _nextInt;
    }

    public void set_nextLong (long _nextLong)
    {
        this._nextLong = _nextLong;
    }

}