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
import org.jgap.impl.IntegerGene;


/** Fitness function for test implementations without using supergenes.
 * @author Neil Rotstan, Klaus Meffert
 * @author Audrius Meskauskas (subsequent adaptation)
 *  */

class p_withoutSupergeneChangeFitnessFunction extends
p_SupergeneChangeFitnessFunction {

    public p_withoutSupergeneChangeFitnessFunction(int a_targetAmount) {
        super(a_targetAmount);
    }
    public Gene getResponsibleGene(Chromosome a_chromosome, int a_code) {
        return a_chromosome.getGene(a_code);
    }

    /** Additionall check that the number of nickels and pennies should
     * be both even or odd.
     */
    public double evaluate(Chromosome a_subject) {
        IntegerGene nickels =
         (IntegerGene) a_subject.getGene(p_SupergeneTest.NICKELS);
        IntegerGene pennies  =
         (IntegerGene) a_subject.getGene(p_SupergeneTest.PENNIES);

        boolean valid = nickels.intValue() % 2 == pennies.intValue() % 2;

        // valid = true; // uncomment for testing without the condition above

        double r;
        if (!valid) r = 0;
        else        r = super.evaluate(a_subject);
        return r;
    }


}
