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
 * The mutation operator runs through the genes in each of the
 * Chromosomes in the population and mutates them in statistical
 * accordance to the given mutation rate. Mutated Chromosomes
 * are added to the list of candidate Chromosomes.
 */
public class MutationOperator implements GeneticOperator {
  protected int mutationRate;

  public MutationOperator(int desiredMutationRate) {
    mutationRate = desiredMutationRate;
  }

  public void operate(final Configuration gaConf, final Chromosome[] population,
                      List candidateChromosomes) {
    if (mutationRate == 0) {
      return;
    }

    RandomGenerator generator = gaConf.getRandomGenerator();

    // It would be inefficient to create copies of each Chromosome just
    // to decide whether to mutate them. Instead, we only make a copy
    // once we've positively decided to perform a mutation.

    for (int i = 0; i < population.length; i++) {
      BitSet genes = population[i].getGenes();
      int numberOfGenes = population[i].size();
      Chromosome copyOfChromosome = null;

      for(int j = 0 ; j < numberOfGenes; j++) {
        if(generator.nextInt(mutationRate) == 0) {
          // Now that we want to actually modify the Chromosome, let's
          // make a copy of it and modify the copy.
          if (copyOfChromosome == null) {
            copyOfChromosome = (Chromosome) population[i].clone(); 
            candidateChromosomes.add(copyOfChromosome); 
            genes = copyOfChromosome.getGenes();
          }

          // Java 1.4 introduced a flip() method, but then JGAP would
          // require 1.4 to operate, and slightly more compact code
          // doesn't seem to justify that.
          if (genes.get(j)) {
            genes.clear(j);
          }
          else {
            genes.set(j);
          }
        }
      }
    }
  }
}

