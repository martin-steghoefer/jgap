/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.supergenes;

import org.jgap.*;

/** Fitness function for a version where Supergene is used
 *
 * @author Neil Rotstan
 * @author Klaus Meffert
 * @author Audrius Meskauskas (subsequent adaptation)
 * @since 2.0
 */
class p_SupergeneChangeFitnessFunction
    extends p_abstractChangFitnessFunction {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

    public p_SupergeneChangeFitnessFunction(int a_targetAmount) {
        super(a_targetAmount);
    }

    /** Dimes and nickels are taken from the chromosome, and
     * quarters and pennies are taken from the supergene (gene number 2)
     */

    public Gene getResponsibleGene(Chromosome a_chromosome, int a_code)
     {
         switch (a_code) {
             case p_SupergeneTest.DIMES:
             case p_SupergeneTest.QUARTERS:
              return a_chromosome.getGene(a_code);

             case p_SupergeneTest.NICKELS:
              {
                  Supergene s = (Supergene) a_chromosome.getGene(2);
                  return s.getGene(0);
              }

             case p_SupergeneTest.PENNIES:
               {
                   Supergene s = (Supergene) a_chromosome.getGene(2);
                   return s.getGene(1);
               }
             default: throw new Error("Invalid coind code "+a_code);
         }
     }
}
