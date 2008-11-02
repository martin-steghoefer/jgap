/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.simpleBooleanThreaded;

import org.jgap.*;
import org.jgap.impl.*;
import org.jgap.event.*;

/**
 * Simple class that demonstrates the basic usage of JGAP together with
 * multithreaded processing.
 *
 * @author Klaus Meffert
 * @since 3.01
 */
public class SimpleExample {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.4 $";

  /**
   * Starts the example.
   * @param args ignored here
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.01
   */
  public static void main(String[] args)
      throws Exception {
    final int numEvolutions = 50;
    final int numThreads = 5;
    int chromeSize = 32;
    if (chromeSize > 32) {
      System.err.println("This example does not handle " +
                         "Chromosomes greater than 32 bits in length.");
      System.exit( -1);
    }
    for (int i = 0; i < numThreads; i++) {
      final int j = i;
      Configuration gaConf = new DefaultConfiguration(i + "", "no name");
      gaConf.setPreservFittestIndividual(i % 2 == 0);
      gaConf.setKeepPopulationSizeConstant(i % 2 != 0);
      IChromosome sampleChromosome = new Chromosome(gaConf,
          new BooleanGene(gaConf), chromeSize);
      gaConf.setSampleChromosome(sampleChromosome);
      gaConf.setPopulationSize(4);
      gaConf.setFitnessFunction(new MaxFunction());
      Genotype genotype = null;
      try {
        genotype = Genotype.randomInitialGenotype(gaConf);
      } catch (InvalidConfigurationException e) {
        e.printStackTrace();
        System.exit( -2);
      }
      final Thread t1 = new Thread(genotype);
      gaConf.getEventManager().addEventListener(GeneticEvent.
          GENOTYPE_EVOLVED_EVENT, new GeneticEventListener() {
        public void geneticEventFired(GeneticEvent a_firedEvent) {
          GABreeder genotype = (GABreeder) a_firedEvent.getSource();
          int evno = genotype.getLastConfiguration().getGenerationNr();
          if (evno % 10 == 0) {
            double bestFitness = genotype.getLastPopulation().
                determineFittestChromosome().getFitnessValue();
            System.out.println(t1.getName() + ": Evolving generation " + evno
                               + ", best fitness: " + bestFitness);
          }
          if (evno > numEvolutions) {
            t1.stop();
          }
          else {
            try {
              t1.sleep( (j + 1) * 3);
            } catch (InterruptedException iex) {
              iex.printStackTrace();
              System.exit(1);
            }
          }
        }
      });
      t1.setPriority( (i % 2) + 1);
      t1.start();
    }
  }
}
