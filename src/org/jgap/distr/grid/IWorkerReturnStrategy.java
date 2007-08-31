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
 * Defines which part of a result is returned by a worker.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public interface IWorkerReturnStrategy
    extends Serializable {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.3 $";

  JGAPResult assembleResult(JGAPRequest a_request, Genotype a_genotype)
      throws Exception;
}
