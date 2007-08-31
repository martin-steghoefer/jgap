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
import org.jgap.util.*;

/**
 * Bulk fitness functions are used to determine how optimal a group of
 * solutions are relative to each other. Bulk fitness functions can be
 * useful (vs. normal fitness functions) when fitness of a particular
 * solution cannot be easily computed in isolation, but instead is
 * dependent upon the fitness of its fellow solutions that are also
 * under consideration. This abstract class should be extended and the
 * evaluateChromosomes() method implemented to evaluate each of the
 * Chromosomes given in an array and set their fitness values prior
 * to returning.
 *
 * @author Neil Rotstan
 * @author Klaus Meffert
 * @since 1.0
 */
public abstract class BulkFitnessFunction
    implements Serializable, ICloneable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.9 $";

  /**
   * Calculates and sets the fitness values on each of the given
   * Chromosomes via their setFitnessValue() method.
   *
   * @param a_chromosomes list of Chromosomes for which the fitness values
   * must be computed and set
   *
   * @author Neil Rotstan
   * @author Klaus Meffert
   * @since 2.2 (prior versions used other input type)
   */
  public abstract void evaluate(Population a_chromosomes);

  /**
   * Override in your implementation if necessary.
   *
   * @return deep clone of the current instance
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public Object clone() {
    try {
      return super.clone();
    } catch (CloneNotSupportedException cex) {
      throw new CloneException(cex);
    }
  }
}
