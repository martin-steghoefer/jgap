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

import org.jgap.MutationRateCalculator;
import org.jgap.Configuration;

/**
 * Default implementation of a mutation rate calculcator
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class DefaultMutationRateCalculator
    implements MutationRateCalculator
{
    /** String containing the CVS revision. Read out via reflection!*/
    private final static String CVS_REVISION = "$Revision: 1.3 $";

    public DefaultMutationRateCalculator ()
    {

    }

    /**
     * Calculates the mutation rate
     * @param a_activeConfiguration current active configuration
     * @return calculated divisor of mutation rate probability (dividend is 1)
     *
     * @since 1.1 (same functionality since earlier, but not encapsulated)
     */
    public int calculateCurrentRate (Configuration a_activeConfiguration)
    {
        return a_activeConfiguration.getChromosomeSize() * 10;
    }

}