/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.chromInit;

import org.jgap.*;
import org.jgap.impl.*;

/**
 * Simple test class that demonstrates how to initialize chromosomes with
 * different numbers of Genes.
 *
 * @author Klaus Meffert
 * @since 2.4
 */
public class ChromosomeInit {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.7 $";

  public static void main(String[] args) {
    int numEvolutions = 500;
    // Create configuration by using DefaultConfiguration and removing
    // CrossoverOperator. Removal necessary because that operator does not
    // work with chromosomes of different gene size!
    Configuration gaConf = new DefaultConfiguration();
    // The next statement is dirty and to be avoided outside this example!
    gaConf.getGeneticOperators().remove(0);
    gaConf.setPreservFittestIndividual(true);
    gaConf.setKeepPopulationSizeConstant(false);
    try {
      int chromeSize;
      if (args.length == 0) {
        chromeSize = 7;
      }
      else {
        chromeSize = Integer.parseInt(args[0]);
      }
      if (chromeSize > 15) {
        System.err.println("This example does not handle " +
                           "Chromosomes greater than 15 bits in length.");
        System.exit( -1);
      }
      IChromosome sampleChromosome = new Chromosome(gaConf,
          new BooleanGene(gaConf), chromeSize);
      gaConf.setSampleChromosome(sampleChromosome);
      gaConf.setPopulationSize(20);
      gaConf.setFitnessFunction(new MaxFunction());
      // Completely initialize the population with custom code.
      // Notice that we assign the double number of Genes to
      // each other Chromosome.
      // ------------------------------------------------------
      int populationSize = gaConf.getPopulationSize();
      Population pop = new Population(gaConf, populationSize);
      for (int i = 0; i < populationSize; i++) {
        int mult;
        // Every second Chromosome has double the number of Genes.
        // -------------------------------------------------------
        if (i % 2 == 0) {
          mult = 1;
        }
        else {
          mult = 2;
        }
        Gene[] sampleGenes = sampleChromosome.getGenes();
        Gene[] newGenes = new Gene[sampleGenes.length * mult];
        RandomGenerator generator = gaConf.getRandomGenerator();
        for (int j = 0; j < newGenes.length; j = j + mult) {
          // We use the newGene() method on each of the genes in the
          // sample Chromosome to generate our new Gene instances for
          // the Chromosome we're returning. This guarantees that the
          // new Genes are setup with all of the correct internal state
          // for the respective gene position they're going to inhabit.
          // ----------------------------------------------------------
          newGenes[j] = sampleGenes[j / mult].newGene();
          // Set the gene's value (allele) to a random value.
          // ------------------------------------------------
          newGenes[j].setToRandomValue(generator);
          if (mult > 1) {
            newGenes[j + 1] = sampleGenes[j / 2].newGene();
            // Set the gene's value (allele) to a random value.
            // ------------------------------------------------
            newGenes[j + 1].setToRandomValue(generator);
          }
        }
        IChromosome chrom = Chromosome.randomInitialChromosome(gaConf);
        chrom.setGenes(newGenes);
        pop.addChromosome(chrom);
      }
      // Now we need to construct the Genotype. This could otherwise be
      // accomplished more easily by writing
      // "Genotype genotype = Genotype.randomInitialGenotype(...)"
      Genotype genotype = new Genotype(gaConf, pop);
      int progress = 0;
      int percentEvolution = numEvolutions / 100;
      for (int i = 0; i < numEvolutions; i++) {
        genotype.evolve();
        // Print progress.
        // ---------------
        if (percentEvolution > 0 && i % percentEvolution == 0) {
          progress++;
          IChromosome fittest = genotype.getFittestChromosome();
          double fitness = fittest.getFitnessValue();
          System.out.println("Fittest Chromosome has value " + fitness);
        }
      }
      IChromosome fittest = genotype.getFittestChromosome();
      System.out.println("Fittest Chromosome has value " +
                         fittest.getFitnessValue());
    }
    catch (InvalidConfigurationException e) {
      e.printStackTrace();
      System.exit( -2);
    }
  }
}
