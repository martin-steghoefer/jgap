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

/**
 * Optional initializer for creating GP programs.
 * Set it via GPConfiguration.setInitStrategy.
 *
 * @author Klaus Meffert
 * @since 3.2.1
 */
public interface IGPInitStrategy
    extends Serializable {
  /** String containing the CVS revision. Read out via reflection!*/
  final static String CVS_REVISION = "$Revision: 1.5 $";

  /**
   * Initializes a chromosome within a GP program before a random creation of
   * the (rest of the) program is executed.
   *
   * @param a_chrom the chromosome within the GP program to create randomly
   * @param a_chromNum index of the chromosome with the GP program
   * @return the CommandGene to use as first node, or null if random selection
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.2.1
   */
  CommandGene init(IGPChromosome a_chrom, int a_chromNum)
      throws Exception;
}
