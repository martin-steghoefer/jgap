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

import org.jgap.impl.IntegerGene;
import org.jgap.supergenes.abstractSupergene;
import org.jgap.Gene;

/** Supergene to hold quarters and nickels. Valid if the number of
 * quarters and nickels is either both odd or both even.
 */

public class QuartersNickelsSupergene extends abstractSupergene {
    public QuartersNickelsSupergene() {
    }

    public QuartersNickelsSupergene( Gene[] a_genes )
     {
         super(a_genes);
     }

    public boolean isValid()
    {
         IntegerGene quarters = (IntegerGene) getGenes()[0];
         IntegerGene pennies  = (IntegerGene) getGenes()[1];
         boolean valid = quarters.intValue() % 2 == pennies.intValue() % 2;
         return valid;

    }

}