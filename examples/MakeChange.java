/*
 * Copyright 2001-2003 Neil Rotstan
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
package examples;

import org.jgap.Gene;
import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.Genotype;
import org.jgap.FitnessFunction;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.IntegerGene;


/**
 * This class attempts to create an amount of American change (using
 * quarters, dimes, nickels, and pennies) equal to a target amount provided
 * by the user. For a detailed discussion of this program, see the JGAP
 * Tutorial. For a more ambitious version of this program, which tries
 * to produce the target amount in the fewest possible coins, please see
 * the MinimizingMakeChange.java example.
 */
public class MakeChange
{
    private static final int MAX_ALLOWED_EVOLUTIONS = 500;

    public static void makeChangeForAmount( int a_targetChangeAmount )
                       throws Exception
    {
        // Start with a DefaultConfiguration, which comes setup with the
        // most common settings.
        // -------------------------------------------------------------
        Configuration conf = new DefaultConfiguration();

        // Set the fitness function we want to use, which is our
        // MakeChangeFitnessFunction that we created earlier. We'll
        // construct it with a target amount of 75 cents for now.
        // --------------------------------------------------------
        FitnessFunction myFunc =
            new MakeChangeFitnessFunction( a_targetChangeAmount );

        conf.setFitnessFunction( myFunc );

        // Now we need to tell the Configuration object how we want our
        // Chromosomes to be setup. We do that by actually creating a
        // sample Chromosome and then setting it on the Configuration
        // object. As mentioned earlier, we want our Chromosomes to
        // each have four genes, one for each of the coin types. We
        // want the values of those genes to be integers, which represent
        // how many coins of that type we have. We therefore use the
        // IntegerGene class to represent each of the genes. That class
        // also lets us specify a lower and upper bound, which we set
        // to sensible values for each coin type.
        // --------------------------------------------------------------
        Gene[] sampleGenes = new Gene[4];

        sampleGenes[0] = new IntegerGene( 0, 3 );  // Quarters
        sampleGenes[1] = new IntegerGene( 0, 2 );  // Dimes
        sampleGenes[2] = new IntegerGene( 0, 1 );  // Nickels
        sampleGenes[3] = new IntegerGene( 0, 4 );  // Pennies

        Chromosome sampleChromosome = new Chromosome( sampleGenes );

        conf.setSampleChromosome( sampleChromosome );

        // Finally, we need to tell the Configuration object how many
        // Chromosomes we want in our population. The more Chromosomes,
        // the larger number of potential solutions (which is good for
        // finding the answer), but the longer it will take to evolve
        // the population (which could be seen as bad). We'll just set
        // the population size to 1000 here.
        // ------------------------------------------------------------
        conf.setPopulationSize( 1000 );

        // Create random initial population of Chromosomes.
        // ------------------------------------------------
        Genotype population = Genotype.randomInitialGenotype( conf );

        // Evolve the population until we find the correct answer, or until
        // we reach the maximum number of allowed evolutions.
        // ----------------------------------------------------------------
        Chromosome bestSolutionSoFar = null;

        for( int i = 0; i < MAX_ALLOWED_EVOLUTIONS; i++ )
        {
            population.evolve();

            // Check the best solution so far and see if it totals up to
            // our target amount of change. If so, then we're done and
            // there's no need to continue evolving the population.
            // ---------------------------------------------------------
            bestSolutionSoFar = population.getFittestChromosome();

            if( MakeChangeFitnessFunction.amountOfChange( bestSolutionSoFar ) ==
                a_targetChangeAmount )
            {
                break;
            }
        }

        // Display the best solution we found.
        // -----------------------------------
        System.out.println( "The best solution contained the following: " );

        System.out.println(
            MakeChangeFitnessFunction.getNumberOfCoins( bestSolutionSoFar, 0 ) +
            " quarters." );

        System.out.println(
            MakeChangeFitnessFunction.getNumberOfCoins( bestSolutionSoFar, 1 ) +
            " dimes." );

        System.out.println(
            MakeChangeFitnessFunction.getNumberOfCoins( bestSolutionSoFar, 2 ) +
            " nickels." );

        System.out.println(
            MakeChangeFitnessFunction.getNumberOfCoins( bestSolutionSoFar, 3 ) +
            " pennies." );

        System.out.println( "For a total of " +
            MakeChangeFitnessFunction.amountOfChange( bestSolutionSoFar ) +
            " cents." );
    }


    /**
     * Main method. A single command-line argument is expected, which is
     * the amount of change to create (in other words, 75 would be equal to 75
     * cents).
     *
     * @param args the command-line arguments.
     */
    public static void main( String[] args )
    {
        if( args.length != 1 )
        {
            System.out.println( "Syntax: MakeChange <amount>" );
        }
        else
        {
            try
            {
                int amount = Integer.parseInt( args[0] );
                if( amount < 1 || amount > 99 )
                {
                    System.out.println(
                        "The <amount> argument must be between 1 and 99." );
                }
                else
                {
                    try
                    {
                        makeChangeForAmount( amount );
                    }
                    catch( Exception e )
                    {
                        e.printStackTrace();
                    }
                }
            }
            catch( NumberFormatException e )
            {
                System.out.println(
                    "The <amount> argument must be a valid integer value." );
            }
        }
    }
}
