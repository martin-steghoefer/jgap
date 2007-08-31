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

import org.jgap.gp.*;
import org.jgap.gp.impl.*;
import org.jgap.util.*;

/**
 * Default and simple implementation of IWorkerEvolveStrategyGP.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public class DefaultEvolveStrategyGP
    implements IWorkerEvolveStrategyGP, ICloneable {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  public void evolve(GPGenotype a_genotype) {
    a_genotype.evolve();
  }

  /**
   * @return deep clone of this instance
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public Object clone() {
    return new DefaultEvolveStrategyGP();
  }
}
