/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap;

import java.io.*;

/**
 * Interface for a calculator that determines a dynamic rate.
 *
 * @author Chris Knowles
 * @since 2.0
 */
public interface IUniversalRateCalculator extends Serializable {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.7 $";

  /**
   * Calculates the required dynamic rate.
   * @return the currently applying mutation rate.
   *
   * @author Chris Knowles
   * @since 2.0
   */
  int calculateCurrentRate();

  /**
   * Calculates whether a mutation should be carried out.
   * @return flag indicating whether mutation should be performed
   *
   * @author Chris Knowles
   * @since 2.0
   */
  boolean toBePermutated(IChromosome a_chrom, int a_geneIndex);
}
