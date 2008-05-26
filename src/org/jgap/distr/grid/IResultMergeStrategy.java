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

/**
 * Client side:
 * Can be used in implementations of IClientEvolveStrategyGP to merge previous
 * results with the current result in order to obtain a working set for future
 * evolutions.
 *
 * @author Klaus Meffert
 * @since 3.3.3
 */
public interface IResultMergeStrategy extends Serializable {

  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.2 $";

  /**
   *
   * @param a_set1 Collection
   * @param a_set2 Collection
   * @return Collection
   *
   * @author Klaus Meffert
   * @since 3.3.3
   */
  Object mergeResults(Object a_set1,  Object a_set2);
}
