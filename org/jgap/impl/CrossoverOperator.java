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

package org.jgap.impl;

import org.jgap.*;
import java.util.*;

/**
 * The crossover operator randomly selects two Chromosomes from the
 * population and "mates" them by randomly picking a gene and then
 * swapping that gene and all subsequent genes between the two
 * Chromosomes. The two modified Chromosomes are then added to the
 * list of candidate Chromosomes. This operation is performed half
 * as many times as there are Chromosomes in the population.
 */
public class CrossoverOperator implements GeneticOperator {
  public void operate(Configuration gaConf, Chromosome[] population,
                      List candidateChromosomes) {
    int numCrossovers = population.length / 2;
    RandomGenerator generator = gaConf.getRandomGenerator();

    for (int i = 0; i < numCrossovers; i++) {
      Chromosome firstMate = (Chromosome)
        population[generator.nextInt(population.length)].clone();
  
      Chromosome secondMate = (Chromosome)
        population[generator.nextInt(population.length)].clone();

      BitSet firstGenes = firstMate.getGenes();
      BitSet secondGenes = secondMate.getGenes();
      int numberOfGenes = firstGenes.length();
      int locus = generator.nextInt(numberOfGenes);
      boolean currentAllele;
 
      for(int j = locus; j < numberOfGenes; j++) {
        // java 1.4 introduced some new BitSet methods that would clean
        // up the code below a bit (like a set() that accepts a boolean
        // value), but that would make JGAP dependent on Java 1.4, and
        // slightly cleaner code seems like a silly reason to require
        // that.
        currentAllele = firstGenes.get(j);
      
        if(secondGenes.get(j)) {
          firstGenes.set(j);
        }
        else {
          firstGenes.clear(j);
        }
  
        if(currentAllele) {
          secondGenes.set(j);
        }
  
        else {
          secondGenes.clear(j);
        }
      }

      candidateChromosomes.add(firstMate);
      candidateChromosomes.add(secondMate);
    }
  }
}

