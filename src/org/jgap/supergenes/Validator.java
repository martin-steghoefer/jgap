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


/**
 * The abstract supergeneValidator, hiding the getPersisten()
 * and setFromPersistent() methods that are not always required.
 * @author Audrius Meskauskas
 */

public abstract class Validator implements supergeneValidator {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

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
