/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.grid.mathProblemDistributed;

import org.jgap.distr.grid.*;
import org.jgap.distr.grid.gp.*;
import org.jgap.*;
import org.jgap.gp.impl.*;

/**
 * Initializes the genotype on behalf of the workers in a grid.
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public class MyGenotypeInitializer
    implements IGenotypeInitializerGP {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  public GPGenotype setupGenotype(JGAPRequestGP a_req, GPPopulation a_initialPop)
      throws Exception {
    GPConfiguration conf = a_req.getConfiguration();
    GPPopulation pop;
    if (a_initialPop == null) {
      pop = new GPPopulation(conf, conf.getPopulationSize());
    }
    else {
      pop = a_initialPop;
    }
    int size = conf.getPopulationSize() - pop.size();
    IGridConfigurationGP gridConfig = a_req.getGridConfiguration();
    GPGenotype result = new GPGenotype(conf, pop, gridConfig.getTypes(),
                                       gridConfig.getArgTypes(),
                                       gridConfig.getNodeSets(),
                                       gridConfig.getMinDepths(),
                                       gridConfig.getMaxDepths(),
                                       gridConfig.getMaxNodes());
//    result.fillPopulation(size);/**@todo needed?*/
    pop.setGPProgram(0, new GPProgram(conf, 1));/**@todo just for quick walkthrough*/
    return result;
  }
}
