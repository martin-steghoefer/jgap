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
import org.jgap.gp.*;
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
  private final static String CVS_REVISION = "$Revision: 1.7 $";

  public GPGenotype setupGenotype(JGAPRequestGP a_req,
                                  GPPopulation a_initialPop)
      throws Exception {
    GPConfiguration conf = a_req.getConfiguration();
    GridConfiguration gridConfig = (GridConfiguration) a_req.
        getGridConfiguration();
    GPPopulation pop;
    if (a_initialPop == null) {
      pop = new GPPopulation(conf, conf.getPopulationSize());
      // Randomly initialize the rest of the population.
      // ----------------------------------------------
      initPop(conf, gridConfig);
    }
    else {
      if (a_initialPop.isFirstEmpty()) {
        throw new RuntimeException("Initial population must either be null"
                                   + " or completely filled with gp programs!");
      }
      pop = a_initialPop;
    }
    int size = conf.getPopulationSize() - pop.size();
    GPPopulationInitializer popInit = null;
    if (size > 0) {
      // Randomly initialize the rest of the population.
      // ----------------------------------------------
      initPop(conf, gridConfig);
    }
    conf.putVariable(gridConfig.getVariable());
    GPGenotype result = new GPGenotype(conf, pop, gridConfig.getTypes(),
                                       gridConfig.getArgTypes(),
                                       gridConfig.getNodeSets(),
                                       gridConfig.getMinDepths(),
                                       gridConfig.getMaxDepths(),
                                       gridConfig.getMaxNodes(), popInit);
    result.putVariable(gridConfig.getVariable());
    return result;
  }

  /**
   * Initializes a partly or fully incomplete population on behalf of the
   * worker.
   *
   * @param conf the GP configuration
   * @param gridConfig the grid configuration
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.6
   */
  public void initPop(GPConfiguration conf, GridConfiguration gridConfig)
      throws Exception {
    GPPopulationInitializer popInit = new GPPopulationInitializer();
    Class[] types = {CommandGene.FloatClass};
    Class[][] argTypes = { {}
    };
    popInit.setUp(conf, types, argTypes,
                  gridConfig.getNodeSets(),
                  gridConfig.getMaxNodes(), true);
    popInit.setVariable(gridConfig.getVariable());
  }
}
