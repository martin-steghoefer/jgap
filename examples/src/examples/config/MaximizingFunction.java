/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.config;

import java.io.*;

import org.jgap.*;
import org.jgap.impl.*;
import org.jgap.event.*;
import org.jgap.data.*;

/**
 * This class is to test the working of JGAP with a config file provided for
 * configuring JGAP.
 * @author Siddhartha Azad.
 * The problem statement is to maximize the value of the function 
 * f(a,b,c) = (a - b + c). This is trivial but works fine for the purpose of
 * demonstration of the working of JGAP with a config file. Reasonable bounds
 * have been set up for the values of a, b and c. 
 * */
public class MaximizingFunction {
	
	/** String containing the CVS revision. Read out via reflection!*/
	private final static String CVS_REVISION = "$Revision: 1.1 $";
	
	/**
	 * Constructor
	 * @author Siddhartha Azad.
	 * */
	public MaximizingFunction() {
		
	}
	
	public static void main(String args[]) {
		Configuration config;
		try {
			config = new CustomConfiguration("jgapTest.con");
		}
		catch(Exception ex) {
			ex.printStackTrace();
			return;
		}
		// set up a sample chromosome
		Gene[] sampleGenes = new Gene[3];
		sampleGenes[0] = new IntegerGene(60, 100);
		sampleGenes[1] = new IntegerGene(1, 50);
		sampleGenes[2] = new IntegerGene(100, 150);
		Chromosome sampleChromosome = new Chromosome(sampleGenes);
		Genotype population;
		FitnessFunction fitFunc = new MaximizingFunctionFitnessFunction();
		try {
			config.setFitnessFunction(fitFunc);
			// The higher the value, the better
			config.setFitnessEvaluator(new DefaultFitnessEvaluator());
			config.setSampleChromosome(sampleChromosome);
			BestChromosomesSelector bestChromsSelector = new 
				BestChromosomesSelector(1.0d);
			bestChromsSelector.setDoubletteChromosomesAllowed(false);
			config.addNaturalSelector(bestChromsSelector, true);
			config.setRandomGenerator(new StockRandomGenerator());
			config.setEventManager(new EventManager());
			config.addGeneticOperator(new CrossoverOperator());
			config.addGeneticOperator(new MutationOperator(15));
			population = Genotype.randomInitialGenotype(config);
		}
		catch(InvalidConfigurationException icEx) {
			icEx.printStackTrace();
			return;
		}
		// We expect the rest of the config parameter for example the population
		// size to be set via the config file
		
	    // Evolve the population
	    for (int i = 0; i < 10; i++) {
	    	population.evolve();
	    }
	    
	    Chromosome bestSolutionSoFar = population.getFittestChromosome();
	    System.out.println("The best solution has a fitness value of " +
	                       bestSolutionSoFar.getFitnessValue());
	    Integer aVal = (Integer)bestSolutionSoFar.getGene(0).getAllele();
	    Integer bVal = (Integer)bestSolutionSoFar.getGene(1).getAllele();
	    Integer cVal = (Integer)bestSolutionSoFar.getGene(2).getAllele();
	    System.out.println("a = "+aVal.intValue());
	    System.out.println("b = "+bVal.intValue());
	    System.out.println("c = "+cVal.intValue());
		
	}
}


















