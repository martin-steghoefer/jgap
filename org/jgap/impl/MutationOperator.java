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

public class MutationOperator implements GeneticOperator {
  protected int mutationRate;

  public MutationOperator(int desiredMutationRate) {
    mutationRate = desiredMutationRate;
  }

  public void operate(Configuration gaConf, Chromosome[] population,
                      List candidateChromosomes) {
    if (mutationRate == 0) {
      return;
    }

    RandomGenerator generator = gaConf.getRandomGenerator();

    for (int i = 0; i < population.length; i++) {
      BitSet genes = population[i].getGenes();
      int numberOfGenes = genes.length();

      for(int j = 0 ; j < numberOfGenes; j++) {
        if(generator.nextInt(mutationRate) == 0) {
          // Java 1.4 introduced a flip() method, but then JGAP would
          // require 1.4 to operate, and this seems like a silly reason
          // to do that.
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

