/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.distr.grid;

import java.io.*;
import org.jgap.*;

/**
 * Interface for defining a strategy that controls how a worker evolves a single
 * request.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public interface IEvolveStrategy
    extends Serializable {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.4 $";

  void evolve(Genotype a_genotype);
}
