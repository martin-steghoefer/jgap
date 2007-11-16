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
 * Tests the AveragingCrossoverOperator class.
 *
 * @author Klaus Meffert
 * @since 2.0
 */
public class AveragingCrossoverOperatorTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.23 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(AveragingCrossoverOperatorTest.class);
    return suite;
  }

  public void setUp() {
    super.setUp();
    Configuration.reset();
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void testOperate_0()
      throws Exception {
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
    Gene gene1 = new IntegerGene(conf, 1, 10);
    gene1.setAllele(new Integer(5));
    chroms.add(gene1);
    Gene gene2 = new IntegerGene(conf, 1, 10);
    gene2.setAllele(new Integer(7));
    chroms.add(gene2);
    Gene gene3 = new IntegerGene(conf, 1, 10);
    gene3.setAllele(new Integer(4));
    chroms.add(gene3);
    GeneticOperator op = new AveragingCrossoverOperator(conf);
    op.operate(new Population(conf, population), chroms);
    assertEquals(5, chroms.size());
    Chromosome target = (Chromosome) chroms.get(4);
    assertEquals(6, ( (Integer) target.getGene(0).getAllele()).intValue());
    target = (Chromosome) chroms.get(3);
    assertEquals(8, ( (Integer) target.getGene(0).getAllele()).intValue());
  }

  /**
   * Tests if crossing over produces same results for two operate-runs.
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
    Gene gene1 = new IntegerGene(conf, 1, 10);
    gene1.setAllele(new Integer(5));
    chroms.add(gene1);
    Gene gene2 = new IntegerGene(conf, 1, 10);
    gene2.setAllele(new Integer(7));
    chroms.add(gene2);
    Gene gene3 = new IntegerGene(conf, 1, 10);
    gene3.setAllele(new Integer(4));
    chroms.add(gene3);
    Chromosome[] population2 = (Chromosome[]) population.clone();
    GeneticOperator op = new AveragingCrossoverOperator(conf);
    op.operate(new Population(conf, population), chroms);
    op.operate(new Population(conf, population2), chroms);
    assertTrue(isChromosomesEqual(population, population2));
  }

  /**
   * Using CompositeGene as first crossover candidate.
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.0
   */
  public void testOperate_2()
      throws Exception {
    DefaultConfiguration conf = new DefaultConfiguration();
//    conf.addGeneticOperator(op);
    RandomGeneratorForTesting rand = new RandomGeneratorForTesting();
    rand.setNextIntSequence(new int[] {
                            0, 1, 0, 1, 2});
    conf.setRandomGenerator(rand);
    conf.setFitnessFunction(new TestFitnessFunction());
    Gene sampleGene = new IntegerGene(conf, 1, 10);
    CompositeGene compGene = new CompositeGene(conf);
    compGene.addGene(sampleGene);
    Chromosome chrom = new Chromosome(conf, compGene, 3);
    conf.setSampleChromosome(chrom);
    conf.setPopulationSize(6);
    Gene cgene1 = new IntegerGene(conf, 1, 10);
    cgene1.setAllele(new Integer(6));
    compGene = new CompositeGene(conf);
    compGene.addGene(cgene1);
    Gene[] genes1 = new Gene[] {
        compGene};
    Chromosome chrom1 = new Chromosome(conf, genes1);
    Gene cgene2 = new IntegerGene(conf, 1, 10);
    cgene2.setAllele(new Integer(8));
    Gene[] genes2 = new Gene[] {
        cgene2};
    Chromosome chrom2 = new Chromosome(conf, genes2);
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
    GeneticOperator op = new AveragingCrossoverOperator(conf);
    op.operate(new Population(conf, population), chroms);
    assertEquals(5, chroms.size());
    Chromosome target = (Chromosome) chroms.get(4);
    assertEquals(6, ( (Integer) target.getGene(0).getAllele()).intValue());
    target = (Chromosome) chroms.get(3);
    CompositeGene result = (CompositeGene) target.getGene(0);
    assertEquals(8, ( (Integer) ( (Vector) result.getAllele()).get(0))
                 .intValue());
  }

  /**
   * Considers crossover rate calculator
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testOperate_3()
      throws Exception {
    DefaultConfiguration conf = new DefaultConfiguration();
//    conf.addGeneticOperator(op);
    RandomGeneratorForTesting rand = new RandomGeneratorForTesting();
    rand.setNextIntSequence(new int[] {
                            0, 1, 0, 1, 2});
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
    GeneticOperator op = new AveragingCrossoverOperator(conf, new
        DefaultCrossoverRateCalculator(conf));
    op.operate(new Population(conf, population), chroms);
    assertEquals(3 + 2, chroms.size());
    Chromosome target = (Chromosome) chroms.get(4);
    assertEquals(6, ( (Integer) target.getGene(0).getAllele()).intValue());
    target = (Chromosome) chroms.get(3);
    assertEquals(8, ( (Integer) target.getGene(0).getAllele()).intValue());
    op.operate(new Population(conf, population), chroms);
    assertEquals(3 + 2 + 2, chroms.size());
  }

  /**
   * Using CompositeGene as second crossover candidate.
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testOperate_4()
      throws Exception {
//    conf.addGeneticOperator(op);
    RandomGeneratorForTesting rand = new RandomGeneratorForTesting();
    rand.setNextIntSequence(new int[] {
                            0, 1, 0, 1, 2});
    conf.setRandomGenerator(rand);
    conf.setFitnessFunction(new TestFitnessFunction());
    Gene sampleGene = new IntegerGene(conf, 1, 10);
    CompositeGene compGene = new CompositeGene(conf);
    compGene.addGene(sampleGene);
    Chromosome chrom = new Chromosome(conf, compGene, 3);
    conf.setSampleChromosome(chrom);
    conf.setPopulationSize(6);
    Gene cgene1 = new IntegerGene(conf, 1, 10);
    cgene1.setAllele(new Integer(6));
    compGene = new CompositeGene(conf);
    compGene.addGene(cgene1);
    Gene[] genes1 = new Gene[] {
        compGene};
    Chromosome chrom1 = new Chromosome(conf, genes1);
    Gene cgene2 = new IntegerGene(conf, 1, 10);
    cgene2.setAllele(new Integer(8));
    Gene[] genes2 = new Gene[] {
        cgene2};
    Chromosome chrom2 = new Chromosome(conf, genes2);
    Chromosome[] population = new Chromosome[] {
        chrom2, chrom1};
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
    GeneticOperator op = new AveragingCrossoverOperator(conf);
    op.operate(new Population(conf, population), chroms);
    assertEquals(5, chroms.size());
    Chromosome target = (Chromosome) chroms.get(3);
    assertEquals(6, ( (Integer) target.getGene(0).getAllele()).intValue());
    target = (Chromosome) chroms.get(4);
    CompositeGene result = (CompositeGene) target.getGene(0);
    assertEquals(8, ( (Integer) ( (Vector) result.getAllele()).get(0))
                 .intValue());
  }

  /**
   * Following should be possible without exception.
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testConstruct_0()
      throws Exception {
    DefaultConfiguration conf = new DefaultConfiguration();
    new AveragingCrossoverOperator(conf, (RandomGenerator)null);
  }

  /**
   * Following should be possible without exception.
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testConstruct_1()
      throws Exception {
    DefaultConfiguration conf = new DefaultConfiguration();
    new AveragingCrossoverOperator(conf, (IUniversalRateCalculator)null);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public void testConstruct_2()
      throws Exception {
    Genotype.setStaticConfiguration(conf);
    AveragingCrossoverOperator op = new AveragingCrossoverOperator();
    assertSame(conf, op.getConfiguration());
  }

  /**
   * Ensures Object is implementing Serializable.
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testIsSerializable_0()
      throws Exception {
    AveragingCrossoverOperator op = new AveragingCrossoverOperator(conf);
    assertTrue(isSerializable(op));
  }

  /**
   * Ensures that Object and all objects contained implement Serializable
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testDoSerialize_0()
      throws Exception {
    // construct object to be serialized
    IUniversalRateCalculator calc = new DefaultCrossoverRateCalculator(conf);
    AveragingCrossoverOperator op = new AveragingCrossoverOperator(conf, calc);
    Object o = doSerialize(op);
    assertEquals(o, op);
  }

  /**
   * Test equals with classcast object.
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testEquals_0()
      throws Exception {
    GeneticOperator op = new AveragingCrossoverOperator(conf);
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
    AveragingCrossoverOperator op = new AveragingCrossoverOperator(conf);
    assertEquals(1, op.compareTo(null));
    AveragingCrossoverOperator op2 = new AveragingCrossoverOperator(conf);
    assertEquals(0, op.compareTo(op2));
    op = new AveragingCrossoverOperator(conf, new StockRandomGenerator());
    assertEquals(0, op.compareTo(op2));
    assertEquals(0, op2.compareTo(op));
    op = new AveragingCrossoverOperator(conf,
                                        new DefaultCrossoverRateCalculator(conf));
    assertEquals(1, op.compareTo(op2));
    assertEquals( -1, op2.compareTo(op));
    op2 = new AveragingCrossoverOperator(conf,
                                         new
                                         DefaultCrossoverRateCalculator(conf));
    assertEquals(0, op.compareTo(op2));
  }

  /**
   * Crossover rate (setter introduced in 2.6).
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testCompareTo_1()
      throws Exception {
    AveragingCrossoverOperator op = new AveragingCrossoverOperator(conf);
    op.setCrossoverRate(3);
    assertEquals(1, op.compareTo(null));
    AveragingCrossoverOperator op2 = new AveragingCrossoverOperator(conf);
    assertEquals(1, op.compareTo(op2));
    assertEquals( -1, op2.compareTo(op));
    op2.setCrossoverRate(3);
    assertEquals(0, op.compareTo(op2));
    op = new AveragingCrossoverOperator(conf, new StockRandomGenerator());
    assertEquals( -1, op.compareTo(op2));
    assertEquals(1, op2.compareTo(op));
  }
}
