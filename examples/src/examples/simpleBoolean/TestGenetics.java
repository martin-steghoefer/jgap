/*
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
package examples.simpleBoolean;

import org.jgap.*;
import org.jgap.impl.*;

/**
 * Simple test class that demonstrates basic usage of JGAP.
 *
 * @author Neil Rotstan
 * @author Klaus Meffert
 * @since 2.0
 */
public class TestGenetics {
  public static void main(String[] args) {
    int numEvolutions = 500;
    ;
    Configuration gaConf = new DefaultConfiguration();
    gaConf.setFitnessEvaluator(new DeltaFitnessEvaluator());
    Genotype genotype = null;

    try {
      int chromeSize = 16;
      if (chromeSize > 32) {
        System.err.println("This example does not handle " +
                           "Chromosomes greater than 32 bits in length.");
        System.exit( -1);
      }

      gaConf.setSampleChromosome(
          new Chromosome(new BooleanGene(), chromeSize));
      gaConf.setPopulationSize(20);
      gaConf.setFitnessFunction(new MaxFunction());

      genotype = Genotype.randomInitialGenotype(gaConf);
    }
    catch (InvalidConfigurationException e) {
      e.printStackTrace();
      System.exit( -2);
    }

    int progress = 0;
    int percentEvolution = numEvolutions / 100;

    for (int i = 0; i < numEvolutions; i++) {
      genotype.evolve();

      // print progress
      if (percentEvolution > 0 && i % percentEvolution == 0) {
        progress++;
        Chromosome fittest = genotype.getFittestChromosome();
        System.out.println("Fittest Chromosome has value " +
                           fittest.getFitnessValue());
      }
    }

    Chromosome fittest = genotype.getFittestChromosome();
    System.out.println("Fittest Chromosome has value " +
                       fittest.getFitnessValue());
  }
}
