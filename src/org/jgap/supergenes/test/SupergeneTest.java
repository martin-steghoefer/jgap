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

package org.jgap.supergenes.test;

import org.jgap.*;
import org.jgap.impl.*;
import java.io.*;

import org.jgap.supergenes.*;

/** To test the Supergene, we created the "makechange" version with
 * additional condition: the number of nickels and pennies must be
 * both even or both odd. The supergene encloses two genes
 * (nickels and pennies) and is valid if the condition above is
 * satisfied.
 */


public class SupergeneTest {

    /** Gene index for the dimes gene */
    public static final int DIMES    = 0;
    /** Gene index for the nickels gene */
    public static final int NICKELS  = 1;

    /** Gene index for the pennies gene.
     * Only used in the alternative presentation  */
    public static final int PENNIES  = 2;
    /** Gene index for the quarters gene.
     * Only used in the alternative presentation  */
    public static final int QUARTERS = 3;

    /** String containing the CVS revision. Read out via reflection!*/
    private final static String CVS_REVISION = "0.0.0 alpha explosive";

    /**
     * The total number of times we'll let the population evolve.
     */
    public static final int MAX_ALLOWED_EVOLUTIONS = 50;
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
    public static void makeChangeForAmount(int a_targetChangeAmount) throws
        Exception {
      // Start with a DefaultConfiguration, which comes setup with the
      // most common settings.
      // -------------------------------------------------------------
      Configuration conf = new DefaultConfiguration();
      // Set the fitness function we want to use, which is our

      // MinimizingMakeChangeFitnessFunction. We construct it with

      // the target amount of change passed in to this method.

      // ---------------------------------------------------------
      SupergeneChangeFitnessFunction fitnessFunction =
          new SupergeneChangeFitnessFunction(a_targetChangeAmount);
      conf.setFitnessFunction(fitnessFunction);
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
      Gene[] sampleGenes = new Gene[3];
      sampleGenes[DIMES] = new IntegerGene(0, 2); // Dimes
      sampleGenes[NICKELS] = new IntegerGene(0, 1); // Nickels
      /* Instead:
      sampleGenes[QUARTERS] = new IntegerGene(0, 3); // Quarters
      sampleGenes[PENNIES] = new IntegerGene(0, 4); // Pennies
      */
      sampleGenes[2] = new QuartersNickelsSupergene(
            new Gene[]
             { new IntegerGene(0,3), // Quarters
               new IntegerGene(0,4), // Pennies
             });

      Chromosome sampleChromosome = new Chromosome(sampleGenes);
      conf.setSampleChromosome(sampleChromosome);
      // Finally, we need to tell the Configuration object how many
      // Chromosomes we want in our population. The more Chromosomes,
      // the larger number of potential solutions (which is good for
      // finding the answer), but the longer it will take to evolve
      // the population (which could be seen as bad). We'll just set
      // the population size to 500 here.
      // ------------------------------------------------------------
      conf.setPopulationSize(500);
      // Create random initial population of Chromosomes.
      // Here we also could read in a previous run via XMLManager.readFile(..)
      // ------------------------------------------------
      Genotype population;
      population = Genotype.randomInitialGenotype(conf);

      // Evolve the population. Since we don't know what the best answer
      // is going to be, we just evolve the max number of times.
      // ---------------------------------------------------------------
      for (int i = 0; i < MAX_ALLOWED_EVOLUTIONS; i++) {
        population.evolve();
      }
      // Display the best solution we found.
      // -----------------------------------
      Chromosome bestSolutionSoFar = population.getFittestChromosome();
      System.out.println("The best solution has a fitness value of " +
                         bestSolutionSoFar.getFitnessValue());
      System.out.println("It contained the following: ");
      System.out.println("\t" +
                         fitnessFunction.
                         getNumberOfCoinsAtGene(
          bestSolutionSoFar, QUARTERS) + " quarters.");
      System.out.println("\t" +
                         fitnessFunction.
                         getNumberOfCoinsAtGene(
          bestSolutionSoFar, DIMES) + " dimes.");
      System.out.println("\t" +
                         fitnessFunction.
                         getNumberOfCoinsAtGene(
          bestSolutionSoFar, NICKELS) + " nickels.");
      System.out.println("\t" +
                         fitnessFunction.
                         getNumberOfCoinsAtGene(
          bestSolutionSoFar, PENNIES) + " pennies.");
      System.out.println("For a total of " +
                         fitnessFunction.amountOfChange(
          bestSolutionSoFar) + " cents in " +
                         fitnessFunction.
                         getTotalNumberOfCoins(
          bestSolutionSoFar) + " coins.");
    }

    /**
     * Main method. A single command-line argument is expected, which is the
     * amount of change to create (in other words, 75 would be equal to 75
     * cents).
     *
     * @param args the command-line arguments.
     */
    public static void _main(String[] args) {
      if (args.length != 1) {
        System.out.println("Syntax: MakeChange <amount>");
      }
      else {
        try {
          int amount = Integer.parseInt(args[0]);
          if (amount < 1 || amount > 99) {
            System.out.println(
                "The <amount> argument must be between 1 and 99.");
          }
          else {
            try {
              makeChangeForAmount(amount);
            }
            catch (Exception e) {
              e.printStackTrace();
            }
          }
        }
        catch (NumberFormatException e) {
          System.out.println(
              "The <amount> argument must be a valid integer value");
        }
      }
    }

  public static void main(String[] args) {
      for (int amount = 1; amount < 100; amount++)
          try {
            System.out.println("EXCANGING "+amount);
            makeChangeForAmount(amount);
          }
          catch (Exception e) {
            e.printStackTrace();
          }

       }
  }

