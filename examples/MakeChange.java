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
package examples;

import org.jgap.Allele;
import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.Genotype;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.IntegerAllele;


/**
 * This class provides an implementation of the classic "Make change" problem
 * using a genetic algorithm. The goal of the problem is to provide a
 * specified amount of change (from a cash purchase) in the fewest coins
 * possible. This example implementation uses American currency (quarters,
 * dimes, nickels, and pennies).
 * <p>
 * This example may be seen as somewhat significant because it demonstrates
 * the use of a genetic algorithm in a less-than-optimal problem space.
 * The genetic algorithm does best when there is a smooth slope of fitness
 * over the problem space towards the optimum solution. This problem exhibits
 * a more choppy space with more local optima. However, as can be seen from
 * running the example, the genetic algorith still does quite well with it
 * due to its relatively large coverage of the problem space (one of the
 * advantages of the genetic algorithm).
 */
public class MakeChange
{
    private static final int MAX_EVOLUTIONS = 50;

    /**
     * Calculates the amount of change represented by the given chromosome.
     *
     * @param a_chromosome The chromosome to evaluate
     * @return the amount of change (as an integer).
     */
    public static int calculateChangeAmount( Chromosome a_chromosome )
    {
        Allele[] genes = a_chromosome.getGenes();
        int quarters = ((Integer) genes[0].getValue() ).intValue();
        int dimes = ((Integer) genes[1].getValue() ).intValue();
        int nickels = ((Integer) genes[2].getValue() ).intValue();
        int pennies = ((Integer) genes[3].getValue() ).intValue();

        return ( quarters * 25 ) + ( dimes * 10 ) + ( nickels * 5 ) + pennies;
    }


    /**
     * Calculates the number of coins represented by the given chromosome.
     *
     * @param a_chromosome The chromosome to evaluate.
     * @return The number of coins represented by the given chromosome.
     */
    public static int calculateNumberOfCoins( Chromosome a_chromosome )
    {
        Allele[] genes = a_chromosome.getGenes();
        int quarters = ((Integer) genes[0].getValue() ).intValue();
        int dimes = ((Integer) genes[1].getValue() ).intValue();
        int nickels = ((Integer) genes[2].getValue() ).intValue();
        int pennies = ((Integer) genes[3].getValue() ).intValue();

        return quarters + dimes + nickels + pennies;
    }


    /**
     * Employs a genetic algorithm to try to determine the minimum number
     * of coins necessary to produce a given amount of change. Writes the
     * answer to System.out.
     *
     * @param a_changeValue The amount of change for which to determine the
     *                      minimum number of necessary coins.
     * @throws Exception if there is a problem.
     */
    private static void calculateMinimumCoinsForChange( int a_changeValue )
                        throws Exception
    {
        Configuration activeConfiguration = new DefaultConfiguration();
        activeConfiguration.setAutoExaggerationEnabled( true );
        activeConfiguration.setFitnessFunction(
            new MakeChangeFitnessFunction( a_changeValue ) );

        Allele[] sampleGenes = new Allele[4];
        sampleGenes[0] = new IntegerAllele( 0, 3 );
        sampleGenes[1] = new IntegerAllele( 0, 2 );
        sampleGenes[2] = new IntegerAllele( 0, 2 );
        sampleGenes[3] = new IntegerAllele( 0, 4 );

        activeConfiguration.setSampleChromosome(
            new Chromosome( sampleGenes ) );

        activeConfiguration.setPopulationSize( 500 );

        Genotype population =
            Genotype.randomInitialGenotype( activeConfiguration );

        for( int i = 0; i < MAX_EVOLUTIONS; i++ )
        {
            population.evolve();
        }

        Chromosome fittestChromosome = population.getFittestChromosome();
        System.out.println( "Best answer is " +
            calculateChangeAmount( fittestChromosome ) + " cents with " +
            calculateNumberOfCoins( fittestChromosome ) + " coins." );
        System.out.println( fittestChromosome.toString() );
    }


    /**
     * Main method. A single command-line argument is expected, which is
     * the amount of change to create (in other words, 75 would be 75
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
                if( amount >= 100 )
                {
                    System.out.println(
                        "The <amount> argument must be less than 100 ");
                }
                else
                {
                    try
                    {
                        calculateMinimumCoinsForChange( amount );
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
                    "The <amount> argument must be a valid integer value" );
            }
        }
    }
}
