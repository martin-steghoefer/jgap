/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.gp;

/**
 * Abstract class representing a method of selecting individuals for evolutionary
 * operations. Classes extending this class must implement the select method.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public abstract class SelectionMethod
    implements java.io.Serializable {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.3 $";

  /**
   * Select an individual based on some method.
   *
   * @param a_world the World for the run
   * @return the individual chosen from the world's population
   *
   * @author Klaus Meffert
   * @since 3.0
   */
  public abstract ProgramChromosome select(GPGenotype a_world);
}
