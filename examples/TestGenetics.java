/*
 * Copyright 2001, Neil Rotstan
 *
 * This file is part of JGAP.
 *
 * JGAP is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * JGAP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser Public License
 * along with JGAP; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

import org.jgap.*;
import org.jgap.impl.*;

/**
 * Simple test class that demonstrates basic usage of JGAP.
 */
public class TestGenetics {

  public static void main(String[] args) {
    if(args.length < 3) {
      System.err.println("Syntax: java TestGenetics <chromosome size> " +
                         "<population size> <num evolutions>");

      System.exit(-1);
    }

    int numEvolutions = Integer.parseInt(args[2]);
    Configuration gaConf = new Configuration();
    Genotype genotype = null;

    try {
      gaConf.setChromosomeSize(Integer.parseInt(args[0]));
      gaConf.setPopulationSize(Integer.parseInt(args[1]));
      gaConf.setFitnessFunction(new MaxFunction());
      gaConf.setNaturalSelector(new WeightedRouletteSelector());
      gaConf.setRandomGenerator(new StockRandomGenerator());

      gaConf.addGeneticOperator(new ReproductionOperator());
      gaConf.addGeneticOperator(new CrossoverOperator());
      gaConf.addGeneticOperator(new MutationOperator(1000));

      genotype = Genotype.randomInitialGenotype(gaConf); 
    }
    catch (InvalidConfigurationException e) {
      e.printStackTrace();
      System.exit(-2);
    }

    for(int i = 0; i < numEvolutions; i++) {
      genotype.evolve();
    }

    System.out.println(genotype.toString());

    Chromosome fittest = genotype.getFittestChromosome();
    System.out.println("Fittest Chromosome is " + fittest.toString() +
      " with a fitness value of " + 
      gaConf.getFitnessFunction().evaluate(fittest));
  }
} 
 
