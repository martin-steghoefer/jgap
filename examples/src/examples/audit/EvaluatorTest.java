/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package examples.audit;

import org.jgap.impl.*;
import org.jgap.*;
import junit.framework.*;

/**
 * Tests for Evaluator class
 *
 * @author Klaus Meffert
 * @since 2.2
 */
public class EvaluatorTest
    extends TestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private final static String CVS_REVISION = "$Revision: 1.3 $";

  private final static double DELTA = 0.00000001d;

  public EvaluatorTest() {
  }

  public void setUp() {
    Genotype.setConfiguration(null);
  }

  public static Test suite() {
    TestSuite suite = new TestSuite(EvaluatorTest.class);
    return suite;
  }

  public void testStoreGenotype_0()
      throws Exception {
    Configuration conf = new ConfigurationForTest();
    PermutingConfiguration pconf = new PermutingConfiguration(conf);
    Evaluator eval = new Evaluator(pconf);
    Population pop = new Population();
    Gene[] genes = new Gene[1];
    Gene gene = new BooleanGene();
    genes[0] = gene;
    Chromosome chrom = new Chromosome(genes);
    chrom.setFitnessValue(7.3d);
    pop.addChromosome(chrom);
    chrom = new Chromosome(genes);
    chrom.setFitnessValue(4.8d);
    pop.addChromosome(chrom);
    chrom = new Chromosome(genes);
    chrom.setFitnessValue(11.4d);
    pop.addChromosome(chrom);
    Genotype genotype = new Genotype(conf, pop);
    eval.storeGenotype(0, 0, genotype);
    Evaluator.GenotypeData genotypeData = eval.retrieveGenotype(0, 0);
    assertEquals(genotype.getPopulation().size(), genotypeData.size);
    assertEquals(genotype.getConfiguration().getGenerationNr(),
                 genotypeData.generation);
    Evaluator.ChromosomeData[] chromData = genotypeData.chromosomeData;
    for (int i = 0; i < chromData.length; i++) {
      assertEquals(genotype.getPopulation().getChromosome(i).getFitnessValue(),
                   chromData[i].fitnessValue, DELTA);
      assertEquals(genotype.getPopulation().getChromosome(i).size(),
                   chromData[i].size);
    }
    assertEquals(7.3d,
                 genotype.getPopulation().getChromosome(0).getFitnessValue(),
                 DELTA);
  }

  public void testCalcPerformance_0()
      throws Exception {
    Configuration conf = new ConfigurationForTest();
    PermutingConfiguration pconf = new PermutingConfiguration(conf);
    Evaluator eval = new Evaluator(pconf);
    Population pop = new Population();
    Gene[] genes = new Gene[1];
    Gene gene = new BooleanGene();
    genes[0] = gene;
    Chromosome chrom = new Chromosome(genes);
    chrom.setFitnessValue(7.3d);
    pop.addChromosome(chrom);
    chrom = new Chromosome(genes);
    chrom.setFitnessValue(4.8d);
    pop.addChromosome(chrom);
    chrom = new Chromosome(genes);
    chrom.setFitnessValue(11.4d);
    pop.addChromosome(chrom);
    Genotype genotype = new Genotype(conf, pop);
    eval.storeGenotype(0, 0, genotype);

    Evaluator.GenotypeDataAvg avg = eval.calcPerformance(0);
    assertEquals(pop.determineFittestChromosome().getFitnessValue(),
                 avg.bestFitnessValue, DELTA);
    assertEquals(0, avg.bestFitnessValueGeneration);
    assertEquals( (7.3 + 4.8 + 11.4) / 3, avg.avgFitnessValue, DELTA);
    assertEquals( (Math.abs(4.8 - 7.3) / 2 + Math.abs(11.4 - 4.8) / 2) / 1,
                 avg.avgDiversityFitnessValue, DELTA);
    assertEquals(0.0d, avg.avgBestDeltaFitnessValue, DELTA); //because only one run
  }

  public void testCalcPerformance_1()
      throws Exception {
    Configuration conf = new ConfigurationForTest();
    PermutingConfiguration pconf = new PermutingConfiguration(conf);
    Evaluator eval = new Evaluator(pconf);
    // run 0
    Population pop = new Population();
    Gene[] genes = new Gene[1];
    Gene gene = new BooleanGene();
    genes[0] = gene;
    Chromosome chrom = new Chromosome(genes);
    chrom.setFitnessValue(7.3d);
    pop.addChromosome(chrom);
    chrom = new Chromosome(genes);
    chrom.setFitnessValue(4.8d);
    pop.addChromosome(chrom);
    chrom = new Chromosome(genes);
    chrom.setFitnessValue(11.4d);
    pop.addChromosome(chrom);
    Genotype genotype = new Genotype(conf, pop);
    eval.storeGenotype(0, 0, genotype);

    // run 1
    pop = new Population();
    genes = new Gene[1];
    gene = new BooleanGene();
    genes[0] = gene;
    chrom = new Chromosome(genes);
    chrom.setFitnessValue(7);
    pop.addChromosome(chrom);
    chrom = new Chromosome(genes);
    chrom.setFitnessValue(17);
    pop.addChromosome(chrom);
    chrom = new Chromosome(genes);
    chrom.setFitnessValue(19);
    pop.addChromosome(chrom);
    genotype = new Genotype(conf, pop);
    eval.storeGenotype(0, 1, genotype);

    Evaluator.GenotypeDataAvg avg = eval.calcPerformance(0);
    assertEquals(pop.determineFittestChromosome().getFitnessValue(),
                 avg.bestFitnessValue, DELTA);
    assertEquals(0, avg.bestFitnessValueGeneration);
    assertEquals( ( (7.3 + 4.8 + 11.4) / 3) / 2 +
                 ( (7.0d + 17.0d + 19) / 3) / 2, avg.avgFitnessValue, DELTA);
    assertEquals( (Math.abs(4.8 - 7.3) / 2 + Math.abs(11.4 - 4.8) / 2) / 2
                 + (Math.abs(17.0 - 7) / 2 + Math.abs(19.0 - 17) / 2) / 2,
                 avg.avgDiversityFitnessValue, DELTA);
    assertEquals( (Math.abs(19 - 11.4d)) / 1, avg.avgBestDeltaFitnessValue,
                 DELTA);
    assertEquals(3 / 2 + 3 / 2, avg.sizeAvg, DELTA);
  }

  public void testGetNumberOfRuns_0()
      throws Exception {
    Configuration conf = new ConfigurationForTest();
    PermutingConfiguration pconf = new PermutingConfiguration(conf);
    Evaluator eval = new Evaluator(pconf);
    Population pop = new Population();
    Gene[] genes = new Gene[1];
    Gene gene = new BooleanGene();
    genes[0] = gene;
    Chromosome chrom = new Chromosome(genes);
    chrom.setFitnessValue(7.3d);
    pop.addChromosome(chrom);
    chrom = new Chromosome(genes);
    chrom.setFitnessValue(4.8d);
    pop.addChromosome(chrom);
    chrom = new Chromosome(genes);
    chrom.setFitnessValue(11.4d);
    pop.addChromosome(chrom);
    Genotype genotype = new Genotype(conf, pop);
    eval.storeGenotype(0, 0, genotype);
    assertEquals(1, eval.getNumberOfRuns(0));
    assertEquals(0, eval.getNumberOfRuns(1));
  }
  /**@todo do with 2 generations and 2 runs per generation*/
}
