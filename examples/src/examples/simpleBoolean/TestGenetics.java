/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
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

  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.3 $";

  public static void main(String[] args) {
    int numEvolutions = 500;
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
