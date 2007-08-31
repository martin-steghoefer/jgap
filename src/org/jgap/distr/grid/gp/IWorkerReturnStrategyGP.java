/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.distr.grid.gp;

import java.io.*;
import org.jgap.*;
import org.jgap.gp.impl.*;

/**
 * Defines which part of a result is returned by a worker.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public interface IWorkerReturnStrategyGP
    extends Serializable {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.2 $";

  JGAPResultGP assembleResult(JGAPRequestGP a_request, GPGenotype a_genotype)
      throws Exception;
}
