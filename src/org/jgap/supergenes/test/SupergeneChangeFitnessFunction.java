package org.jgap.supergenes.test;

import org.jgap.*;
import org.jgap.impl.IntegerGene;
import org.jgap.supergenes.Supergene;

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
             case SupergeneTest.NICKELS:
              return a_chromosome.getGene(a_code);

             case SupergeneTest.QUARTERS:
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