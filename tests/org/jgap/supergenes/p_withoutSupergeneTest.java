/*
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
 *
 */

package org.jgap.supergenes;

import org.jgap.*;
import org.jgap.impl.*;
import java.io.*;

import org.jgap.supergenes.*;

/**
 * Computes the optimal change with the same condition as
 * SupergeneTest, but without using supergenes. Implemented
 * to compare the performance.
 * To test the Supergene, we created the "makechange" version with
 * additional condition: the number of nickels and pennies must be
 * both even or both odd. The supergene encloses two genes
 * (nickels and pennies) and is valid if the condition above is
 * satisfied.
 */

class p_withoutSupergeneTest
    extends p_SupergeneTest {

    /** String containing the CVS revision. Read out via reflection!*/
    private final static String CVS_REVISION = "0.0.0 alpha explosive";

    /**
     * Executes the genetic algorithm to determine the minimum number of
     * coins necessary to make up the given target amount of change. The
     * solution will then be written to System.out.
     *
     * @param a_targetChangeAmount The target amount of change for which this
     *                             method is attempting to produce the minimum
     *                             number of coins.
     *
     * @throws Exception
     */
    public int makeChangeForAmount (int a_targetChangeAmount) throws
        Exception {
        // Start with a DefaultConfiguration, which comes setup with the
        // most common settings.
        // -------------------------------------------------------------
        Configuration conf = new DefaultConfiguration ();
        // Set the fitness function we want to use, which is our

        // MinimizingMakeChangeFitnessFunction. We construct it with

        // the target amount of change passed in to this method.

        // ---------------------------------------------------------
        p_withoutSupergeneChangeFitnessFunction fitnessFunction =
            new p_withoutSupergeneChangeFitnessFunction(a_targetChangeAmount);
        conf.setFitnessFunction (fitnessFunction);
        // Now we need to tell the Configuration object how we want our
        // Chromosomes to be setup. We do that by actually creating a
        // sample Chromosome and then setting it on the Configuration
        // object. As mentioned earlier, we want our Chromosomes to each
        // have four genes, one for each of the coin types. We want the
        // values (alleles) of those genes to be integers, which represent
        // how many coins of that type we have. We therefore use the
        // IntegerGene class to represent each of the genes. That class
        // also lets us specify a lower and upper bound, which we set
        // to sensible values for each coin type.
        // --------------------------------------------------------------
        Gene[] sampleGenes = new Gene[4];
        sampleGenes[DIMES] = getDimesGene(); // Dimes
        sampleGenes[NICKELS] = getNickelsGene(); // Nickels
        sampleGenes[QUARTERS] = getQuartersGene(); // Quarters
        sampleGenes[PENNIES] = getPenniesGene(); // Pennies

        int s = solve
         (a_targetChangeAmount, conf, fitnessFunction, sampleGenes);
        return s;
    }

    public static void main (String[] args) {
        p_withoutSupergeneTest test = new p_withoutSupergeneTest();
        test.test ();
    };
}
