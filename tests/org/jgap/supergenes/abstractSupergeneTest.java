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

import org.jgap.Genotype;
import org.jgap.Chromosome;
import org.jgap.impl.IntegerGene;
import org.jgap.Gene;
import org.jgap.InvalidConfigurationException;
import org.jgap.Configuration;

/** Abstract class for testing Supergene performance.
 * @author Neil Rotstan, Klaus Meffert
 * @author Audrius Meskauskas (subsequent adaptation)
 * */
public abstract class abstractSupergeneTest {

    /** Gene index for the dimes gene */
    public static final int DIMES = 0;

    /** Gene index for the quarters gene. */
    public static final int QUARTERS = 1;

    /** Gene index for the nickels gene
     * Only used in the alternative presentation  */
    public static final int NICKELS = 2;

    /** Gene index for the pennies gene.
     * Only used in the alternative presentation  */
    public static final int PENNIES = 3;

    /** Create a Dimes gene instance. */
    protected Gene getDimesGene () {
        return new IntegerGene (0, 2); // 10?
    };

    /** Create a Nickels gene instance. */
    protected Gene getNickelsGene () {
        return new IntegerGene (0, 5);
    }

    /** Create a Pennies (1) gene instance. */
    protected Gene getPenniesGene () {
        return new IntegerGene (0, 7);
    }

    /** Create a Quarters gene instance. */
    protected Gene getQuartersGene () {
        return new IntegerGene (0, 3);
    }

    /** Compute the money value from the coin information. */
    public static int amountOfChange
     (int numQuarters, int numDimes, int numNickels, int numPennies) {
        return
         (numQuarters * 25) + (numDimes * 10) + (numNickels * 5) + numPennies;
     };

    /**
     * The total number of times we'll let the population evolve.
     */
    public static final int MAX_ALLOWED_EVOLUTIONS = 200;
    /** Chromosome size. */
    public static final int CHROMOSOME_SIZE = 2000;
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
     * @return absolute difference between the required and computed change
     * amount.
     */
    public abstract int makeChangeForAmount (int a_targetChangeAmount) throws
        Exception;

    public static boolean REPORT_ENABLED = true;

    /** Write report on eveluation to the given stream. */
    public Chromosome report (SupergeneChangeFitnessFunction fitnessFunction,
                               Genotype population) {

        Chromosome bestSolutionSoFar = population.getFittestChromosome ();

        if (!REPORT_ENABLED) return bestSolutionSoFar;

        System.out.println ("\nThe best solution has a fitness value of " +
                            bestSolutionSoFar.getFitnessValue ());
        System.out.println ("It contained the following: ");
        System.out.println ("\t" +
                            fitnessFunction.
                            getNumberOfCoinsAtGene (
            bestSolutionSoFar, QUARTERS) + " quarters.");
        System.out.println ("\t" +
                            fitnessFunction.
                            getNumberOfCoinsAtGene (
            bestSolutionSoFar, DIMES) + " dimes.");
        System.out.println ("\t" +
                            fitnessFunction.
                            getNumberOfCoinsAtGene (
            bestSolutionSoFar, NICKELS) + " nickels.");
        System.out.println ("\t" +
                            fitnessFunction.
                            getNumberOfCoinsAtGene (
            bestSolutionSoFar, PENNIES) + " pennies.");
        System.out.println ("For a total of " +
                            fitnessFunction.amountOfChange (
            bestSolutionSoFar) + " cents in " +
                            fitnessFunction.
                            getTotalNumberOfCoins (
            bestSolutionSoFar) + " coins.");
        return bestSolutionSoFar;
    }

    /** Test the method, returns the sum of all differences between
     * the required and obtained excange amount. One exception counts
     * as 1000 on the error score.
     */
    public int test () {
        int S = 0;
        int e;
        Test:
        for (int amount = 20; amount < 100; amount++) {
            try {
                System.out.println ("EXCANGING " + amount+" ");
                // do not solve cases without solutions
                if (!Force.solve(amount)) continue Test;
                //Force.solve(amount);

                e = makeChangeForAmount (amount);
                System.out.println(" err "+e);

                 if (REPORT_ENABLED) System.out.println("---------------");

                S = S + e;
            }
            catch (Exception ex) {
                ex.printStackTrace ();
                S+=1000;
            }
        }
        System.out.println("Sum of errors "+S);
        return S;
    }

    /**
     * Main method (however non-static!). A single command-line argument is expected, which is the
     * amount of change to create (in other words, 75 would be equal to 75
     * cents).
     *
     * @param args the command-line arguments.
     */
    public void _main (String[] args) {
        if (args.length != 1) {
            System.out.println ("Syntax: MakeChange <amount>");
        }
        else {
            try {
                int amount = Integer.parseInt (args[0]);
                if (amount < 1 || amount > 99) {
                    System.out.println (
                        "The <amount> argument must be between 1 and 99.");
                }
                else {
                    try {
                      makeChangeForAmount (amount);
                    }
                    catch (Exception e) {
                        e.printStackTrace ();
                    }
                }
            }
            catch (NumberFormatException e) {
                System.out.println (
                    "The <amount> argument must be a valid integer value");
            }
        }
    }

    /**
     * Find and print the solution, return the solution error.
     * @return absolute difference between the required and computed change
     */
    protected int solve (int a_targetChangeAmount, Configuration conf,
                       SupergeneChangeFitnessFunction fitnessFunction,
                       Gene[] sampleGenes)
                       throws InvalidConfigurationException {
        Chromosome sampleChromosome = new Chromosome (sampleGenes);
        conf.setSampleChromosome (sampleChromosome);
        // Finally, we need to tell the Configuration object how many
        // Chromosomes we want in our population. The more Chromosomes,
        // the larger number of potential solutions (which is good for
        // finding the answer), but the longer it will take to evolve
        // the population (which could be seen as bad). We'll just set
        // the population size to 500 here.
        // ------------------------------------------------------------
        conf.setPopulationSize (CHROMOSOME_SIZE);
        // Create random initial population of Chromosomes.
        // ------------------------------------------------
        Genotype population;
        population = Genotype.randomInitialGenotype (conf);

        int s;
        Evolution:
        // Evolve the population, break if the the change solution is found
        // ---------------------------------------------------------------
        for (int i = 0; i < MAX_ALLOWED_EVOLUTIONS; i++) {
            population.evolve ();
            s = Math.abs(fitnessFunction.amountOfChange(population.
             getFittestChromosome ())
             -a_targetChangeAmount);
             if (s==0) break Evolution;
             System.out.print(s+".");
        }
        // Display the best solution we found.
        // -----------------------------------
        Chromosome bestSolutionSoFar = report(fitnessFunction, population);

        s = Math.abs(fitnessFunction.amountOfChange(bestSolutionSoFar)
         -a_targetChangeAmount);
        return s;
    }


}
