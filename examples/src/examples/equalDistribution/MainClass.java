/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.equalDistribution;

import java.util.*;
import org.jgap.*;
import org.jgap.impl.*;

/**
 * Given 64 vents with different weights. Try to make 8 group with each
 * groups having (nearly) the same weight as the other groups.<p>
 * Here, each vent has a similar weight with a small deviation. But the
 * deviation could also be significant (then, good solutions are harder to
 * find).<p>
 * The proposed way of solving this problem is quite easy in its structure.
 * There is potential for optimzations!
 *
 * @author Klaus Meffert
 * @since 3.2
 */
public class MainClass {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.2 $";

  /**
   * Holds the available vents, each with a specific weight
   */
  private Vent[] m_vents;

  private int m_numEvolutions;

  /**
   * Constructor.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public MainClass()
      throws Exception {
    makeVents();
    Genotype genotype = configureJGAP();
    doEvolution(genotype);
  }

  /**
   * Sets up the configuration for the problem.
   *
   * @return configured genotype
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  protected Genotype configureJGAP()
      throws Exception {
    m_numEvolutions = 50;
    Configuration gaConf = new DefaultConfiguration();
    gaConf.resetProperty(Configuration.PROPERTY_FITEVAL_INST);
    gaConf.setFitnessEvaluator(new DeltaFitnessEvaluator());
    // Just use a swapping operator instead of mutation and others.
    // ------------------------------------------------------------
    gaConf.getGeneticOperators().clear();
    SwappingMutationOperator swapper = new SwappingMutationOperator(gaConf);
    gaConf.addGeneticOperator(swapper);
    // Setup some other parameters.
    // ----------------------------
    gaConf.setPreservFittestIndividual(true);
    gaConf.setKeepPopulationSizeConstant(false);
    // Set number of individuals (=tries) per generation.
    // --------------------------------------------------
    gaConf.setPopulationSize(50);
    int chromeSize = m_vents.length;
    Genotype genotype = null;
    try {
      // Setup the structure with which to evolve the
      // solution of the problem.
      // --------------------------------------------
      IChromosome sampleChromosome = new Chromosome(gaConf,
          new IntegerGene(gaConf), chromeSize);
      gaConf.setSampleChromosome(sampleChromosome);
      // Setup the important fitness function!
      // -------------------------------------
      gaConf.setFitnessFunction(new SampleFitnessFunction(m_vents));
      //
      genotype = Genotype.randomInitialGenotype(gaConf);
      // Now ensure that each number from 1..64 (representing the
      // indices of the vents) is represented by exactly one gene.
      // --> Suboptimal here, as randomized initialization becomes
      //     obsolete (other solution would be more complicated).
      // ---------------------------------------------------------
      List chromosomes = genotype.getPopulation().getChromosomes();
      for (int i = 0; i < chromosomes.size(); i++) {
        IChromosome chrom = (IChromosome) chromosomes.get(i);
        for (int j = 0; j < chrom.size(); j++) {
          Gene gene = (Gene) chrom.getGene(j);
          gene.setAllele(new Integer(j));
        }
      }
    } catch (InvalidConfigurationException e) {
      e.printStackTrace();
      System.exit( -2);
    }
    return genotype;
  }

  /**
   * Does the evolution until finished.
   *
   * @param a_genotype the genotype to evolve
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void doEvolution(Genotype a_genotype) {
    int progress = 0;
    int percentEvolution = m_numEvolutions / 100;
    for (int i = 0; i < m_numEvolutions; i++) {
      a_genotype.evolve();
      // Print progress.
      // ---------------
      if (percentEvolution > 0 && i % percentEvolution == 0) {
        progress++;
        IChromosome fittest = a_genotype.getFittestChromosome();
        double fitness = fittest.getFitnessValue();
        System.out.println("Currently best solution has fitness " +
                           fitness);
        printSolution(fittest);
      }
    }
    // Print summary.
    // --------------
    IChromosome fittest = a_genotype.getFittestChromosome();
    System.out.println("Best solution has fitness " +
                       fittest.getFitnessValue());
    printSolution(fittest);
  }

  /**
   * @param a_solution a solution to print to the console
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void printSolution(IChromosome a_solution) {
    double groupWeights = 0.0d;
    for (int i = 0; i < 8; i++) {
      System.out.println("\nGroup " + i);
      System.out.println("-------");
      double groupWeight = 0.0d;
      for (int j = 0; j < 8; j++) {
        IntegerGene ventIndex = (IntegerGene) a_solution.getGene( (i * 8 + j));
        Vent vent = (Vent) m_vents[ventIndex.intValue()];
        double weight = vent.getWeight();
        groupWeight += weight;
        System.out.println("  Vent at index "
                           + ventIndex.intValue()
                           + " with weight "
                           + weight);
      }
      groupWeights += groupWeight;
      System.out.println("  --> Group weight: " + groupWeight);
    }
    System.out.println("\n Average group weight: " + groupWeights / 8);
  }

  /**
   * Create vents with different weights.
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public void makeVents() {
    m_vents = new Vent[64];
    for (int i = 0; i < m_vents.length; i++) {
      // Set a weight between 290 and 310
      double weight = 290 + Math.random() * 20;
      Vent vent = new Vent(weight);
      m_vents[i] = vent;
    }
  }

  /**
   * Start the example
   * @param args ignored
   *
   * @author Klaus Meffert
   * @since 3.2
   */
  public static void main(String[] args) {
    try {
      new MainClass();
    } catch (Throwable t) {
      t.printStackTrace();
      System.exit(1);
    }
  }
}
