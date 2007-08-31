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

import org.jgap.*;
import org.jgap.util.*;

/**
 * Default and simple implementation of IWorkerEvolveStrategy.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public class DefaultEvolveStrategy
    implements IWorkerEvolveStrategy, ICloneable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.6 $";

  public void evolve(Genotype a_genotype) {
    a_genotype.evolve();
  }

  /**
   * @return deep clone of this instance
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public Object clone() {
    return new DefaultEvolveStrategy();
  }
}
