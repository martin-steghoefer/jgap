/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap;

import org.jgap.event.*;
import org.jgap.impl.*;

/**
 * Ready-to-go Implementation of org.jgap.Configuration with all important
 * parameters already set.
 *
 * @author Klaus Meffert
 * @since 2.2
 */
public class ConfigurationForTest
    extends Configuration {

  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  public ConfigurationForTest() throws InvalidConfigurationException {
    super();
    setPopulationSize(5);
    setFitnessFunction(new StaticFitnessFunction(2.3d));
    setEventManager(new EventManager());
    setFitnessEvaluator(new DefaultFitnessEvaluator());
    addNaturalSelector(new BestChromosomesSelector(), true);
    addGeneticOperator(new MutationOperator());
    setRandomGenerator(new StockRandomGenerator());
    Gene[] genes = new Gene[1];
    Gene gene = new BooleanGene();
    genes[0] = gene;
    Chromosome chrom = new Chromosome(genes);
    setSampleChromosome(chrom);
  }

}
