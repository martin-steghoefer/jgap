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
import org.jgap.impl.IntegerGene;


/** Fitness function for test implementations without using supergenes. */
public class withoutSupergeneChangeFitnessFunction extends
SupergeneChangeFitnessFunction {

    public withoutSupergeneChangeFitnessFunction(int a_targetAmount) {
        super(a_targetAmount);
    }
    public Gene getResponsibleGene(Chromosome a_chromosome, int a_code) {
        return a_chromosome.getGene(a_code);
    }

    /** Additionall check that the number of quarters and pennies should
     * be both even or odd.
     */
    public double evaluate(Chromosome a_subject) {
        IntegerGene quarters =
         (IntegerGene) a_subject.getGene(SupergeneTest.QUARTERS);
        IntegerGene pennies  =
         (IntegerGene) a_subject.getGene(SupergeneTest.PENNIES);

        boolean valid = quarters.intValue() % 2 == pennies.intValue() % 2;

        double r = super.evaluate(a_subject);
        if (!valid) r = 0;
        return r;
    }


}