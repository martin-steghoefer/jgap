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
import org.jgap.xml.*;
import org.w3c.dom.*;

/**
 * Tests the XML capabilities of JGAP. Uses the simple boolean example for that
 *
 * @author Neil Rotstan
 * @author Klaus Meffert
 * @since 2.0
 */
public class TestXML {
  public static void main(String[] args) {
    try {
      Configuration activeConfiguration = new DefaultConfiguration();

      activeConfiguration.setSampleChromosome(
          new Chromosome(new BooleanGene(), 8));
      activeConfiguration.setPopulationSize(10);
      activeConfiguration.setFitnessFunction(new MaxFunction());
      Genotype.setConfiguration(activeConfiguration);

      // Test Chromsome manipulation methods
      Chromosome chromosome =
          Chromosome.randomInitialChromosome();

      Document chromosomeDoc =
          XMLManager.representChromosomeAsDocument(chromosome);

      Chromosome chromosomeFromXML =
          XMLManager.getChromosomeFromDocument(activeConfiguration,
                                               chromosomeDoc);

      if (! (chromosomeFromXML.equals(chromosome))) {
        System.out.println("Chromosome test failed.");
        System.exit( -1);
      }

      // Test Genotype manipulation methods
      Genotype genotype =
          Genotype.randomInitialGenotype(activeConfiguration);

      Document genotypeDoc =
          XMLManager.representGenotypeAsDocument(genotype);

      Genotype genotypeFromXML =
          XMLManager.getGenotypeFromDocument(activeConfiguration, genotypeDoc);

      if (! (genotypeFromXML.equals(genotype))) {
        System.out.println("Genotype test failed.");
        System.exit( -1);
      }
    }
    catch (Exception e) {
      System.out.println("Test failed due to error:");
      e.printStackTrace();
    }

    System.out.println("Tests successful.");
  }
}
