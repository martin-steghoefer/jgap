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
 */
package org.jgap.impl.salesman;

import org.jgap.*;
import org.jgap.Gene;


/**
 * The fitness function to solve the Travelling Salesman problem
 * @author Audrius Meskauskas
 * @version 1.0
 */

public class SalesmanFitnessFunction extends FitnessFunction {
    public final Salesman salesman;
    public SalesmanFitnessFunction(Salesman a_salesman) {
        salesman = a_salesman;
    }

    /** Computes the distance by calling salesman
     * {@link org.jgap.impl.salesman.distance
     * salesman.distance(Gene from, Gene to) }
     */
    protected double evaluate(Chromosome a_subject) {

        double s = 0;
        Gene [] genes = a_subject.getGenes();
        for (int i = 0; i < genes.length-1; i++) {
            s += salesman.distance( genes [i], genes [i+1] );
        }
        // add cost of coming back:
        s+= salesman.distance( genes [genes.length-1], genes [0] );

        return Integer.MAX_VALUE/2-s;
    }

}