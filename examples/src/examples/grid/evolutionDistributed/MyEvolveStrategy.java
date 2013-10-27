/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.grid.evolutionDistributed;

import org.jgap.distr.grid.*;
import org.jgap.*;

/**
 * Sample implementation of IWorkerEvolveStrategy.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public class MyEvolveStrategy implements IWorkerEvolveStrategy {
  public void evolve(Genotype a_genotype) {
    a_genotype.evolve(40);
  }
}
