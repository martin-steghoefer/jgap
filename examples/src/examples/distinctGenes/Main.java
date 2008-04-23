/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.distinctGenes;

import org.jgap.*;
import org.jgap.impl.*;

/**
 * Simple class that demonstrates how to configure JGAP to use differently
 * composed genes.
 *
 * @author Klaus Meffert
 * @since 3.0
 */
public class Main {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.3 $";

  /**
   * Starts the example.
   * @param args ignored here
   */
  public static void main(String[] args) {
    int numEvolutions = 500;
    Configuration gaConf = new DefaultConfiguration();
    gaConf.reset();
    gaConf.setFitnessEvaluator(new DeltaFitnessEvaluator());
    gaConf.setPreservFittestIndividual(true);
    gaConf.setKeepPopulationSizeConstant(false);
    Genotype genotype = null;
    final int chromeSize = 41; //number of genes in each chromosome
    try {
      IChromosome sampleChromosome = new MyChromosome(gaConf,
          new BooleanGene(gaConf), chromeSize);
      gaConf.setSampleChromosome(sampleChromosome);
      gaConf.setPopulationSize(20);
      gaConf.setFitnessFunction(new SampleFitnessFunction());
      genotype = Genotype.randomInitialGenotype(gaConf);
    }
    catch (InvalidConfigurationException e) {
      e.printStackTrace();
      System.exit( -2);
    }
    int progress = 0;
    int percentEvolution = numEvolutions / 10;
    for (int i = 0; i < numEvolutions; i++) {
      genotype.evolve();
      // Print progress.
      // ---------------
      if (percentEvolution > 0 && i % percentEvolution == 0) {
        progress++;
        IChromosome fittest = genotype.getFittestChromosome();
        double fitness = fittest.getFitnessValue();
        System.out.println("Generation " + i +
                           ": Currently fittest Chromosome has fitness " +
                           fitness);
        if (fitness < 0.0001) {
          break;
        }
      }
    }
    // Print summary.
    // --------------
    IChromosome fittest = genotype.getFittestChromosome();
    System.out.println("Fittest Chromosome has fitness " +
                       fittest.getFitnessValue());
  }
}
