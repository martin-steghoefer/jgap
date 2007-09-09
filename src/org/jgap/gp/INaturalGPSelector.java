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

import java.io.*;
import org.jgap.gp.impl.*;

/**
 * Interface for algorithms selecting individuals for evolutionary operations.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public interface INaturalGPSelector
    extends Serializable {
  /** String containing the CVS revision. Read out via reflection!*/
  static final String CVS_REVISION = "$Revision: 1.6 $";

  /**
   * Select an individual based on an arbitrary algorithm.
   *
   * @param a_genotype the genotype used
   * @return the individual chosen from the genotype's population
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  IGPProgram select(GPGenotype a_genotype);
}
