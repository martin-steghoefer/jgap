package org.jgap.supergenes;

import org.jgap.impl.IntegerGene;
import org.jgap.supergenes.Supergene;
import org.jgap.Chromosome;
import org.jgap.Gene;

/** Fitness function for a version where Supergene is used
 * @author Neil Rotstan, Klaus Meffert
 * @author Audrius Meskauskas (subsequent adaptation)
 */

public class SupergeneChangeFitnessFunction extends
 abstractChangFitnessFunction {

    public SupergeneChangeFitnessFunction(int a_targetAmount) {
        super(a_targetAmount);
    }

    /** Dimes and nickels are taken from the chromosome, and
     * quarters and pennies are taken from the supergene (gene number 2)
     */

    public Gene getResponsibleGene(Chromosome a_chromosome, int a_code)
     {
         switch (a_code) {
             case SupergeneTest.DIMES:
             case SupergeneTest.QUARTERS:
              return a_chromosome.getGene(a_code);

             case SupergeneTest.NICKELS:
              {
                  Supergene s = (Supergene) a_chromosome.getGene(2);
                  return s.getGene(0);
              }

             case SupergeneTest.PENNIES:
               {
                   Supergene s = (Supergene) a_chromosome.getGene(2);
                   return s.getGene(1);
               }
             default: throw new Error("Invalid coind code "+a_code);
         }
     }
}
