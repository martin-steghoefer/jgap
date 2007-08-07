/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.supergenes;

import org.jgap.Gene;

/**
 * A class, deciding, if the supergene allele combination is valid.
 * Some classes Supergenes (like abstractSupergene) also implement
 * supergeneValidator, deciding themselfs about the gene validity.
 * In request to returs a validator, they return <i>this</i>.
 * Other classes may require always to set the external validator.
 *
 * @author Audrius Meskauskas
 * @since 2.0
 */
public interface SupergeneValidator {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.3 $";

  /**
   * Return true if this gene combination is valid for
   * the given supergene */
  boolean isValid(Gene[] a_genes, Supergene a_for_supergene);

  /**
   * @return persistent string representation (if needed) of this validator.
   * The method name is different allowing the same class to implement both
   * Supergene and supergeneValidator.
   *  */
  String getPersistent();

  /**
   * Set a persistend string representation (if needed) for this validator.
   * The method name is different allowing the same class to implement both
   * Supergene and supergeneValidator.
   */

  void setFromPersistent(String a_string);
}
