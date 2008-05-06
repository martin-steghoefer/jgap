/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.impl;

import java.util.*;
import org.jgap.*;
import junit.framework.*;

/**
 * Tests the CrossoverOperator class.
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class CrossoverOperatorTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.33 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(CrossoverOperatorTest.class);
    return suite;
  }

  public void setUp() {
    super.setUp();
    Configuration.reset();
  }

  /**
   * Following should be possible without error.
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testConstruct_0()
      throws Exception {
    new CrossoverOperator(conf, null);
    new CrossoverOperator(conf, new DefaultMutationRateCalculator(conf));
    new CrossoverOperator(conf, 2);
    new CrossoverOperator(new DefaultConfiguration(), 1);
    new CrossoverOperator(conf, 50);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testConstruct_1()
      throws Exception {
    try {
      new CrossoverOperator(conf, 0);
      fail();
    } catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testConstruct_2()
      throws Exception {
    try {
      new CrossoverOperator(new DefaultConfiguration(), -3);
      fail();
    } catch (IllegalArgumentException iex) {
      ; //this is OK
    }
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.3.2
   */
  public void testConstruct_4()
      throws Exception {
    CrossoverOperator op = new CrossoverOperator(conf, 5);
    assertSame(conf, op.getConfiguration());
    assertTrue(op.isAllowFullCrossOver());
    assertEquals(5, op.getCrossOverRate());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.3.2
   */
  public void testConstruct_5()
      throws Exception {
    CrossoverOperator op = new CrossoverOperator(conf, 3, false);
    assertSame(conf, op.getConfiguration());
    assertFalse(op.isAllowFullCrossOver());
    assertEquals(3, op.getCrossOverRate());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.3.2
   */
  public void testConstruct_6()
      throws Exception {
    CrossoverOperator op = new CrossoverOperator(conf, 1, true);
    assertSame(conf, op.getConfiguration());
    assertTrue(op.isAllowFullCrossOver());
    assertEquals(1, op.getCrossOverRate());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.3.2
   */
  public void testConstruct_7()
      throws Exception {
    CrossoverOperator op = new CrossoverOperator(conf, 1.2d);
    assertSame(conf, op.getConfiguration());
    assertTrue(op.isAllowFullCrossOver());
    assertEquals(1.2d, op.getCrossOverRatePercent(), DELTA);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.3.2
   */
  public void testConstruct_8()
      throws Exception {
    CrossoverOperator op = new CrossoverOperator(conf, 4.2d, true);
    assertSame(conf, op.getConfiguration());
    assertTrue(op.isAllowFullCrossOver());
    assertEquals(4.2d, op.getCrossOverRatePercent(), DELTA);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.3.2
   */
  public void testConstruct_9()
      throws Exception {
    CrossoverOperator op = new CrossoverOperator(conf, 18.0d, false);
    assertSame(conf, op.getConfiguration());
    assertFalse(op.isAllowFullCrossOver());
    assertEquals(18.0d, op.getCrossOverRatePercent(), DELTA);
  }

  /**
   * Use flat crossover rate and just exchange two alleles via crossover.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 1.1
   */
  public void testOperate_0()
      throws Exception {
    DefaultConfiguration conf = new DefaultConfiguration();
    // preset "random" values: index first chromosome, index second chromosome,
    // locus (index of gene on chromosome)
    RandomGeneratorForTesting rand = new RandomGeneratorForTesting();
    rand.setNextIntSequence(new int[] {
                            0, 1, 0});
    conf.setRandomGenerator(rand);
    conf.setFitnessFunction(new TestFitnessFunction());
    Gene sampleGene = new IntegerGene(conf, 1, 10);
    Chromosome chrom = new Chromosome(conf, sampleGene, 3);
    conf.setSampleChromosome(chrom);
    conf.setPopulationSize(6);
    Gene cgene1 = new IntegerGene(conf, 1, 100);
    cgene1.setAllele(new Integer(66));
    Gene[] genes1 = new Gene[] {
        cgene1};
    Chromosome chrom1 = new Chromosome(conf, genes1);
    Gene cgene2 = new IntegerGene(conf, 1, 100);
    cgene2.setAllele(new Integer(88));
    Gene[] genes2 = new Gene[] {
        cgene2};
    Chromosome chrom2 = new Chromosome(conf, genes2);
    // Age increase necessary to make x-over work.
    // -------------------------------------------
    chrom2.increaseAge();
    Chromosome[] population = new Chromosome[] {
        chrom1, chrom2};
    List chroms = new Vector();
    Gene gene1 = new IntegerGene(conf, 1, 10);
    gene1.setAllele(new Integer(5));
    chroms.add(gene1);
    Gene gene2 = new IntegerGene(conf, 1, 10);
    gene2.setAllele(new Integer(7));
    chroms.add(gene2);
    Gene gene3 = new IntegerGene(conf, 1, 10);
    gene3.setAllele(new Integer(4));
    chroms.add(gene3);
    CrossoverOperator op = new CrossoverOperator(conf,2);
    // following crossover-operation should exchange alleles of cgene1 and
    // cgene2.
    op.operate(new Population(conf, population), chroms);
    // chroms size = 3 + 2 genes
    //               3 = number of already existent genes
    //               2 = number of genes added from "population" (which contains
    //                   2 genes)
    assertEquals(5, chroms.size());
    // get Gene 3 = first new gene (0..2 = old genes, 3..4 = new genes)
    Chromosome target = (Chromosome) chroms.get(3);
    // 88 = allele of cgene2
    assertEquals(88, ( (Integer) target.getGene(0).getAllele()).intValue());
    // get Gene 4 = second new gene (0..2 = old genes, 3..4 = new genes)
    target = (Chromosome) chroms.get(4);
    // 66 = allele of cgene1
    assertEquals(66, ( (Integer) target.getGene(0).getAllele()).intValue());
  }

  /**
   * Consider crossover rate calculator.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testOperate_0_2()
      throws Exception {
    DefaultConfiguration conf = new DefaultConfiguration();
//    conf.addGeneticOperator(op);
    // preset "random" values: index first chromosome, index second chromosome,
    // locus (index of gene on chromosome)
    RandomGeneratorForTesting rand = new RandomGeneratorForTesting();
    rand.setNextIntSequence(new int[] {
                            0, 1, 0});
    conf.setRandomGenerator(rand);
    conf.setFitnessFunction(new TestFitnessFunction());
    Gene sampleGene = new IntegerGene(conf, 1, 10);
    Chromosome chrom = new Chromosome(conf, sampleGene, 2);
    conf.setSampleChromosome(chrom);
    conf.setPopulationSize(6);
    Gene cgene1 = new IntegerGene(conf, 1, 10);
    cgene1.setAllele(new Integer(6));
    Gene[] genes1 = new Gene[] {
        cgene1};
    Chromosome chrom1 = new Chromosome(conf, genes1);
    Gene cgene2 = new IntegerGene(conf, 1, 10);
    cgene2.setAllele(new Integer(8));
    Gene[] genes2 = new Gene[] {
        cgene2};
    Chromosome chrom2 = new Chromosome(conf, genes2);
    // Age increase necessary to make x-over work.
    // -------------------------------------------
    chrom2.increaseAge();
    Chromosome[] population = new Chromosome[] {
        chrom1, chrom2};
    List chroms = new Vector();
    Gene gene1 = new IntegerGene(conf, 1, 10);
    gene1.setAllele(new Integer(5));
    chroms.add(gene1);
    CrossoverOperator op = new CrossoverOperator(conf,
        new
        DefaultCrossoverRateCalculator(
            conf));
    op.operate(new Population(conf, population), chroms);
    // chroms size = 1 + 2 genes
    //               1 = number of already existent genes
    //               2 = number of genes added from "population" (which contains
    //                   2 genes)
    assertEquals(1 + 2, chroms.size());
    // get Gene 1 = first new gene (0..0 = old genes, 1..2 = new genes)
    Chromosome target = (Chromosome) chroms.get(1);
    assertEquals(8, ( (Integer) target.getGene(0).getAllele()).intValue());
    // get Gene 2 = second new gene (0..0 = old genes, 1..2 = new genes)
    target = (Chromosome) chroms.get(2);
    assertEquals(6, ( (Integer) target.getGene(0).getAllele()).intValue());
    // chroms size = 1 + 2 + 2 genes
    //               1 + 2 = number of already existent genes
    //               2     = number of genes added from "population" (which
    //                       contains 2 genes)
    op.operate(new Population(conf, population), chroms);
    assertEquals(1 + 2 + 2, chroms.size());
  }

  /**
   * Tests if crossing over produces same results for two operate-runs.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void testOperate_1()
      throws Exception {
    DefaultConfiguration conf = new DefaultConfiguration();
//    conf.addGeneticOperator(op);
    RandomGeneratorForTesting rand = new RandomGeneratorForTesting();
    rand.setNextIntSequence(new int[] {
                            0, 1, 0, 1, 2});
    conf.setRandomGenerator(rand);
    conf.setFitnessFunction(new TestFitnessFunction());
    Gene sampleGene = new IntegerGene(conf, 1, 10);
    Chromosome chrom = new Chromosome(conf, sampleGene, 3);
    conf.setSampleChromosome(chrom);
    conf.setPopulationSize(6);
    Gene cgene1 = new IntegerGene(conf, 1, 10);
    cgene1.setAllele(new Integer(6));
    Gene[] genes1 = new Gene[] {
        cgene1};
    Chromosome chrom1 = new Chromosome(conf, genes1);
    Gene cgene2 = new IntegerGene(conf, 1, 10);
    cgene2.setAllele(new Integer(8));
    Gene[] genes2 = new Gene[] {
        cgene2};
    Chromosome chrom2 = new Chromosome(conf, genes2);
    Chromosome[] population = new Chromosome[] {
        chrom1, chrom2};
    List chroms = new Vector();
    // add some genes to chroms that should not be overridden
    Gene gene1 = new IntegerGene(conf, 1, 10);
    gene1.setAllele(new Integer(5));
    chroms.add(gene1);
    Gene gene2 = new IntegerGene(conf, 1, 10);
    gene2.setAllele(new Integer(7));
    chroms.add(gene2);
    Chromosome[] population2 = (Chromosome[]) population.clone();
    GeneticOperator op = new CrossoverOperator(conf);
    op.operate(new Population(conf, population), chroms);
    op.operate(new Population(conf, population2), chroms);
    assertTrue(isChromosomesEqual(population, population2));
    // check that original genes have not been modified
    assertSame(gene1, chroms.get(0));
    assertSame(gene2, chroms.get(1));
  }

  /**
   * Test with CompositeGene.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.1
   */
  public void testOperate_2()
      throws Exception {
    DefaultConfiguration conf = new DefaultConfiguration();
    RandomGeneratorForTesting rand = new RandomGeneratorForTesting();
    rand.setNextIntSequence(new int[] {
                            0, 1, 0, 1, 2});
    conf.setRandomGenerator(rand);
    conf.setFitnessFunction(new TestFitnessFunction());
    Gene sampleGene = new IntegerGene(conf, 1, 10);
    Chromosome chrom = new Chromosome(conf, sampleGene, 3);
    conf.setSampleChromosome(chrom);
    conf.setPopulationSize(6);
    Gene cgene1 = new IntegerGene(conf, 1, 10);
    cgene1.setAllele(new Integer(6));
    CompositeGene compGene = new CompositeGene(conf);
    compGene.addGene(cgene1);
    Gene[] genes1 = new Gene[] {
        compGene};
    Chromosome chrom1 = new Chromosome(conf, genes1);
    Gene cgene2 = new IntegerGene(conf, 1, 10);
    cgene2.setAllele(new Integer(8));
    Gene[] genes2 = new Gene[] {
        cgene2};
    Chromosome chrom2 = new Chromosome(conf, genes2);
    // Age increase necessary to make x-over work.
    // -------------------------------------------
    chrom2.increaseAge();
    Chromosome[] population = new Chromosome[] {
        chrom1, chrom2};
    List chroms = new Vector();
    Gene gene1 = new IntegerGene(conf, 1, 10);
    gene1.setAllele(new Integer(5));
    chroms.add(gene1);
    Gene gene2 = new IntegerGene(conf, 1, 10);
    gene2.setAllele(new Integer(7));
    chroms.add(gene2);
    CrossoverOperator op = new CrossoverOperator(conf, 2);
    // Do the crossing over.
    // ---------------------
    op.operate(new Population(conf, population), chroms);
    // new size of chroms = 2 (original chromosomes) + 2 (from "population")
    assertEquals(2 + 2, chroms.size());
    Chromosome target = (Chromosome) chroms.get(2);
    CompositeGene result = (CompositeGene) target.getGene(0);
    assertEquals(8, ( (Integer) ( (Vector) result.getAllele())
                     .get(0)).intValue());
    target = (Chromosome) chroms.get(3);
    assertEquals(6, ( (Integer) target.getGene(0).getAllele()).intValue());
  }

  /**
   * Considers IGeneticOperatorConstraint. Here, the crossing over of a
   * BooleanGene is forbidden by that constraint.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testOperate_3()
      throws Exception {
    DefaultConfiguration conf = new DefaultConfiguration();
    IGeneticOperatorConstraint constraint = new
        GeneticOperatorConstraintForTesting();
    conf.getJGAPFactory().setGeneticOperatorConstraint(constraint);
    RandomGeneratorForTesting rand = new RandomGeneratorForTesting();
    rand.setNextIntSequence(new int[] {
                            0, 2, 0, 1, 2});
    conf.setRandomGenerator(rand);
    conf.setFitnessFunction(new TestFitnessFunction());
    Gene sampleGene = new IntegerGene(conf, 1, 10);
    Chromosome chrom = new Chromosome(conf, sampleGene, 3);
    conf.setSampleChromosome(chrom);
    conf.setPopulationSize(6);
    Gene cgene0 = new IntegerGene(conf, 1, 10);
    cgene0.setAllele(new Integer(8));
    Gene[] genes0 = new Gene[] {
        cgene0};
    Chromosome chrom0 = new Chromosome(conf, genes0);
    Gene cgene1 = new IntegerGene(conf, 1, 10);
    cgene1.setAllele(new Integer(5));
    Gene[] genes1 = new Gene[] {
        cgene1};
    ChromosomeForTesting chrom1 = new ChromosomeForTesting(conf, genes1);
    Gene cgene2 = new IntegerGene(conf, 1, 10);
    cgene2.setAllele(new Integer(6));
    Gene[] genes2 = new Gene[] {
        cgene2};
    Chromosome chrom2 = new Chromosome(conf, genes2);
    // Age increase necessary to make x-over work.
    // -------------------------------------------
    chrom2.increaseAge();
    Chromosome[] population = new Chromosome[] {
        chrom0, chrom1, chrom2};
    // Add some nonsense objects to results list (to see if they are kept).
    List chroms = new Vector();
    Gene gene1 = new IntegerGene(conf, 1, 10);
    gene1.setAllele(new Integer(5));
    chroms.add(gene1);
    Gene gene2 = new IntegerGene(conf, 1, 10);
    gene2.setAllele(new Integer(7));
    chroms.add(gene2);
    CrossoverOperator op = new CrossoverOperator(conf, 2);
    // Do the crossing over.
    // ---------------------
    op.operate(new Population(conf, population), chroms);
    assertEquals(2 + 2, chroms.size());
    assertSame(gene1, chroms.get(0));
    assertSame(gene2, chroms.get(1));
    Chromosome target = (Chromosome) chroms.get(2);
    assertEquals(6, ( (Integer) target.getGene(0).getAllele()).intValue());
    target = (Chromosome) chroms.get(3);
    assertEquals(8, ( (Integer) target.getGene(0).getAllele()).intValue());
  }

  /**
   * Ensures the operator is implementing Serializable.
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testIsSerializable_0()
      throws Exception {
    CrossoverOperator op = new CrossoverOperator(conf);
    assertTrue(isSerializable(op));
  }

  /**
   * Ensures that the operator and all objects contained implement Serializable.
   * Here, we use a null configuration.
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testDoSerialize_0()
      throws Exception {
    // construct object to be serialized
    CrossoverOperator op = new CrossoverOperator(conf,
        new
        DefaultCrossoverRateCalculator(
            conf));
    CrossoverOperator o = (CrossoverOperator) doSerialize(op);
    assertEquals(o, op);
  }

  /**
   * Ensures that the operator and all objects contained implement Serializable.
   * Here, we set a configuration.
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testDoSerialize_1()
      throws Exception {
    Configuration conf = new DefaultConfiguration();
    // construct object to be serialized
    CrossoverOperator op = new CrossoverOperator(conf,
        new
        DefaultCrossoverRateCalculator(
            conf));
    CrossoverOperator o = (CrossoverOperator) doSerialize(op);
    assertEquals(o, op);
  }

  public class GeneticOperatorConstraintForTesting
      implements IGeneticOperatorConstraint {
    public boolean isValid(Population a_pop, List a_chromosomes,
                           GeneticOperator a_caller) {
      Iterator it = a_chromosomes.iterator();
      while (it.hasNext()) {
        Chromosome chrom = (Chromosome) it.next();
        if (ChromosomeForTesting.class == chrom.getClass()) {
          return false;
        }
      }
      return true;
    }
  }
  /**
   * Test equals with classcast object.
   *
   * @throws Exception
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testEquals_0()
      throws Exception {
    GeneticOperator op = new CrossoverOperator(conf);
    assertFalse(op.equals(new Chromosome(conf)));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testCompareTo_0()
      throws Exception {
    CrossoverOperator op = new CrossoverOperator(conf);
    assertEquals(1, op.compareTo(null));
    CrossoverOperator op2 = new CrossoverOperator(conf);
    assertEquals(0, op.compareTo(op2));
    op = new CrossoverOperator(conf, 3);
    assertEquals(-1, op.compareTo(op2));
    assertEquals(1, op2.compareTo(op));
    op = new CrossoverOperator(conf, new DefaultCrossoverRateCalculator(conf));
    assertEquals(1, op.compareTo(op2));
    assertEquals( -1, op2.compareTo(op));
    op2 = new CrossoverOperator(conf, new DefaultCrossoverRateCalculator(conf));
    assertEquals(0, op.compareTo(op2));
  }
}
