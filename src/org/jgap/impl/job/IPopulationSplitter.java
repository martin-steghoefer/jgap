/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.impl.job;

import org.jgap.*;

/**
 * For splitting a single population into smaller chunks. Each chunk being meant
 * to be operated on a single node in a distributed calculation (e.g. a grid).
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public interface IPopulationSplitter {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.5 $";

  /**
   * Splits a single population into smaller sub-populations.
   *
   * @param a_pop input population
   * @return resulting chunks
   * @throws Exception in case of any error
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  Population[] split(Population a_pop) throws Exception ;
}
