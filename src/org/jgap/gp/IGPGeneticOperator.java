/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp;

import org.jgap.*;

/**
 * Interface for genetic operators suitable for GP.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public interface IGPGeneticOperator
    extends GeneticOperator {
  /** String containing the CVS revision. Read out via reflection!*/
  static final String CVS_REVISION = "$Revision: 1.2 $";
}
