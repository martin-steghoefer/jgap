/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.math.ga;

import org.jgap.*;
import org.jgap.event.*;
import org.jgap.impl.*;

import examples.math.*;

/**
 * Genetic configuration for the example.
 *
 * @author Michael Grove
 * @since 3.4.2
 */
public class MathRunnerConfiguration
    extends Configuration {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.1 $";

  public MathRunnerConfiguration()
      throws InvalidConfigurationException {
    PhenotypeExpresser<Double> aExpr = new MathGenePhenotypeExpresser();
//        PhenotypeExpresser<Double> aExpr = new IntegerGenePhenotypeExpresser();
//        PhenotypeExpresser<Double> aExpr = new MyIntegerGenePhenotypeExpresser();

    setPopulationSize(1000);
    setAlwaysCaculateFitness(!true);
    setFitnessFunction(new MathRunnerFitnessFunction(42, aExpr));
    Gene[] aGenes = new Gene[1];
    aGenes[0] = new MathGene(this);
//        aGenes[0] = new IntegerGene(this, -100000, 100000);
//        aGenes[0] = new MyIntegerGene(this, -100000, 100000);

    setSampleChromosome(new Chromosome(this, aGenes));
    setKeepPopulationSizeConstant(false);
    setPreservFittestIndividual(true);
    setEventManager(new EventManager());
    addGeneticOperator(new MutationOperator(this, 25));
    setFitnessEvaluator(new DeltaFitnessEvaluator());
    setRandomGenerator(new StockRandomGenerator());
    addNaturalSelector(new BestChromosomesSelector(this), false);
  }
}
