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

package org.jgap.supergenes;

import org.jgap.*;


/**
 * The abstract supergeneValidator, hiding the getPersisten()
 * and setFromPersistent() methods that are not always required.
 * @author Audrius Meskauskas
 */

public abstract class Validator implements supergeneValidator {

    /** {@inheritDoc} */
    public abstract boolean isValid(Gene[] genes, Supergene for_supergene);

    /** {@inheritDoc}
     * The default implementation returns an empty string. */
    public String getPersistent() {
        return "";
    }

    /** {@inheritDoc}
     * The default implementation does nothing. */
    public void setFromPersistent(String a_from) {
    }

}