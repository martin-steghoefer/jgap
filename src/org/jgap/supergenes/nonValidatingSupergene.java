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

import org.jgap.Gene;

/**
 * A supergene that is always valid. This is only needed for some
 * special cases.
 *
 * @author Audrius Meskauskas
 * @version 1.0
 */
public class nonValidatingSupergene extends abstractSupergene {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

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
