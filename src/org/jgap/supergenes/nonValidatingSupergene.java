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

import org.jgap.Gene;


/**
 * A supergene that is always valid. This is only needed for some
 * special cases.
 *
 * @author Audrius Meskauskas
 * @version 1.0
 */

public class nonValidatingSupergene extends abstractSupergene {

    public nonValidatingSupergene() {}
    public nonValidatingSupergene (Gene [] a_genes)
     {
         super (a_genes);
     }

    /** Always true */
    public final boolean isValid(Gene[] a, Supergene a_for) {
        return true;
    }

    /** Always true */
    public final boolean isValid() {
        return true;
    }


}