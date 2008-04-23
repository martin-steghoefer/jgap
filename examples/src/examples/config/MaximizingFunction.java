/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.config;

import org.jgap.*;
import org.jgap.event.*;
import org.jgap.impl.*;

/**
 * This class is to test the working of JGAP with a config file provided for
 * configuring JGAP.
 * The problem statement is to maximize the value of the function
 * f(a,b,c) = (a - b + c). This is trivial but works fine for the purpose of
 * demonstration of the working of JGAP with a config file. Reasonable bounds
 * have been set up for the values of a, b and c.
 *
 * @author Siddhartha Azad
 * @since 2.3
 * */
public class MaximizingFunction {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.9 $";

  /**
   * Default Constructor
   * @author Siddhartha Azad
   * @since 2.3
   * */
  public MaximizingFunction() {
  }

  /**
   * Starting the example
   * @param args not used
   * @throws Exception
   *
   * @author Siddhartha Azad
   * @since 2.3
   */
  public static void main(String args[]) throws Exception{
    Configuration conf;
    try {
      conf = new Configuration("jgapTest.con", false);
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return;
    }
    // set up a sample chromosome
    Gene[] sampleGenes = new Gene[3];
    sampleGenes[0] = new IntegerGene(conf, 60, 100);
    sampleGenes[1] = new IntegerGene(conf, 1, 50);
    sampleGenes[2] = new IntegerGene(conf, 100, 150);
    IChromosome sampleChromosome = new Chromosome(conf, sampleGenes);
    Genotype population;
    FitnessFunction fitFunc = new MaximizingFunctionFitnessFunction();
    try {
      conf.setFitnessFunction(fitFunc);
      // The higher the value, the better
      conf.setFitnessEvaluator(new DefaultFitnessEvaluator());
      conf.setSampleChromosome(sampleChromosome);
      BestChromosomesSelector bestChromsSelector = new
          BestChromosomesSelector(conf, 1.0d);
      bestChromsSelector.setDoubletteChromosomesAllowed(false);
      conf.addNaturalSelector(bestChromsSelector, true);
      conf.setRandomGenerator(new StockRandomGenerator());
      conf.setEventManager(new EventManager());
      conf.addGeneticOperator(new CrossoverOperator(conf));
      conf.addGeneticOperator(new MutationOperator(conf, 15));
      population = Genotype.randomInitialGenotype(conf);
    }
    catch (InvalidConfigurationException icEx) {
      icEx.printStackTrace();
      return;
    }
    // We expect the rest of the config parameter, for example the population
    // size, to be set via the config file

    // Evolve the population
    for (int i = 0; i < 10; i++) {
      population.evolve();
    }
    IChromosome bestSolutionSoFar = population.getFittestChromosome();
    System.out.println("The best solution has a fitness value of " +
                       bestSolutionSoFar.getFitnessValue());
    Integer aVal = (Integer) bestSolutionSoFar.getGene(0).getAllele();
    Integer bVal = (Integer) bestSolutionSoFar.getGene(1).getAllele();
    Integer cVal = (Integer) bestSolutionSoFar.getGene(2).getAllele();
    System.out.println("a = " + aVal.intValue());
    System.out.println("b = " + bVal.intValue());
    System.out.println("c = " + cVal.intValue());
  }
}
