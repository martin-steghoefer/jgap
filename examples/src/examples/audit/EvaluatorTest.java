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
  private final static String CVS_REVISION = "$Revision: 1.1 $";

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
    chrom = new Chromosome(genes);
    chrom.setFitnessValue(5.8d);
    pop.addChromosome(chrom);
    chrom = new Chromosome(genes);
    chrom.setFitnessValue(12.4d);
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
  }

  public void testCalcPerformance_0() {

  }
}
