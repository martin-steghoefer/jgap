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

import org.jgap.Gene;

/**
 * A class, deciding, if the supergene allele combination is valid.
 * Some classes Supergenes (like abstractSupergene) also implement
 * supergeneValidator, deciding themselfs about the gene validity.
 * In request to returs a validator, they return <i>this</i>.
 * Other classes may require always to set the external validator.
 * @author Audrius Meskauskas
 */

public interface supergeneValidator {

    /**
     * Return true if this gene combination is valid for
     * the given supergene */
    boolean isValid (Gene [] a_genes, Supergene a_for_supergene );

    /**
     * Get a persistent string representation (if needed) of this validator.
     * The name is different allowing the same class to implement both
     * Supergene and supergeneValidator.
     *  */
      String getPersistent();

     /**
      * Set a persistend string representation (if needed) for this validator.
      * The name is different allowing the same class to implement both
      * Supergene and supergeneValidator.
      */
      void setFromPersistent(String a_string);


}