/*
 * Copyright 2001, 2002 Neil Rotstan
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

import org.jgap.Configuration;
import org.jgap.InvalidConfigurationException;
import org.jgap.event.EventManager;


/**
 * The DefaultConfiguration class simplifies the GA configuration
 * process by providing default configuration values for many of the
 * configuration settings. The following values must still be provided:
 * chromosome size, population size, and desired fitness function.
 * All other settings may also be changed in the normal fashion for
 * those who wish to specify other custom values.
 */
public class DefaultConfiguration extends Configuration
{
    public DefaultConfiguration()
    {
        super();

        try
        {
            setNaturalSelector( new WeightedRouletteSelector() );
            setRandomGenerator( new StockRandomGenerator() );
            setEventManager( new EventManager() );
            addGeneticOperator( new ReproductionOperator() );
            addGeneticOperator( new CrossoverOperator() );
            addGeneticOperator( new MutationOperator() );
        }
        catch ( InvalidConfigurationException e )
        {
            throw new RuntimeException(
                "Fatal error: DefaultConfiguration class could not use its " +
                "own stock configuration values. This should never happen. " +
                "Please report this as a bug." );
        }
    }
}
