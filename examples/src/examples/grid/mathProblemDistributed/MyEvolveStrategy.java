/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.grid.mathProblemDistributed;

import org.jgap.distr.grid.gp.*;
import org.jgap.gp.impl.*;

/**
 * Sample implementation of IWorkerEvolveStrategy.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public class MyEvolveStrategy
    implements IWorkerEvolveStrategyGP {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  public void evolve(GPGenotype a_genotype) {
    a_genotype.evolve(40);
  }
}
