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
 * Initializes the genotype on behalf of the workers in a grid.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public class MyGenotypeInitializer
    implements IGenotypeInitializer {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  public Genotype setupGenotype(JGAPRequest a_req, Population a_initialPop)
      throws Exception {
    Configuration conf = a_req.getConfiguration();
    Population pop;
    if (a_initialPop == null) {
      pop = new Population(conf);
    }
    else {
      pop = a_initialPop;
    }
    int size = conf.getPopulationSize() - pop.size();
    Genotype result = new Genotype(conf, pop);
    result.fillPopulation(size);
    return result;
  }
}
