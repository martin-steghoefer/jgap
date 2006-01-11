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
public class NonValidatingSupergene
    extends AbstractSupergene {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  public NonValidatingSupergene() {
  }

  public NonValidatingSupergene(final Gene[] a_genes) {
    super(a_genes);
  }

  /**
   * Always returns true
   * @param a_gene ignored
   * @param a_for Supergene ignored
   * @return always true
   */
  public final boolean isValid(final Gene[] a_gene, final Supergene a_for) {
    return true;
  }
}
