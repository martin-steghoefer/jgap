/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licencing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package org.jgap.impl;

import java.util.*;
import org.jgap.*;
import org.jgap.JGAPTestCase.*;
import junit.framework.*;

/**
 * Tests the SwappingMutationOperator class
 *
 * @author Klaus Meffert
 * @since 2.1
 */
public class SwappingMutationOperatorTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.14 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(SwappingMutationOperatorTest.class);
    return suite;
  }

  /**
   * @author Klaus Meffert
   * @since 2.1
   */
  public void testConstruct_0() {
    SwappingMutationOperator mutOp = new SwappingMutationOperator(234);
    assertEquals(234, mutOp.getMutationRate());
    assertNull(mutOp.getMutationRateCalc());
  }

  /**
   * @author Klaus Meffert
   * @since 2.1
   */
  public void testConstruct_1() {
    SwappingMutationOperator mutOp = new SwappingMutationOperator();
    assertEquals(0, mutOp.getMutationRate());
    assertNotNull(mutOp.getMutationRateCalc());
  }

  /**
   * @author Klaus Meffert
   * @since 2.1
   */
  public void testConstruct_2() {
    SwappingMutationOperator mutOp = new SwappingMutationOperator(null);
    assertEquals(0, mutOp.getMutationRate());
    assertNull(mutOp.getMutationRateCalc());
  }

  /**
   * @author Klaus Meffert
   * @since 2.1
   */
  public void testConstruct_3() {
    IUniversalRateCalculator calc = new DefaultMutationRateCalculator();
    MutationOperator mutOp = new MutationOperator(calc);
    assertEquals(0, mutOp.getMutationRate());
    assertEquals(calc, mutOp.getMutationRateCalc());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.1
   */
  public void testOperate_0()
      throws Exception {
    Configuration conf = new DefaultConfiguration();
    conf.setFitnessFunction(new TestFitnessFunction());
    Genotype.setConfiguration(conf);
    SwappingMutationOperator mutOp = new SwappingMutationOperator();
    List candChroms = new Vector();
    Chromosome[] population = new Chromosome[] {};
    mutOp.operate(new Population(population), candChroms);
    assertEquals(candChroms.size(), population.length);
    candChroms.clear();
    RandomGeneratorForTest gen = new RandomGeneratorForTest();
    gen.setNextInt(9);
    conf.setRandomGenerator(gen);
    Genotype.setConfiguration(conf);
    Chromosome c1 = new Chromosome(new BooleanGene(), 9);
    conf.setSampleChromosome(c1);
    conf.addNaturalSelector(new BestChromosomesSelector(), true);
    conf.setPopulationSize(5);
    for (int i = 0; i < c1.getGenes().length; i++) {
      c1.getGene(i).setAllele(Boolean.TRUE);
    }
    Chromosome c2 = new Chromosome(new IntegerGene(), 4);
    for (int i = 0; i < c2.getGenes().length; i++) {
      c2.getGene(i).setAllele(new Integer(27));
    }
    population = new Chromosome[] {
        c1, c2};
    mutOp.operate(new Population(population), candChroms);
    assertEquals(candChroms.size(), population.length);
    assertEquals(c1, candChroms.get(0));
    assertFalse(candChroms.get(0) == c1);
    assertEquals(c2, candChroms.get(1));
    assertFalse(candChroms.get(1) == c2);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.1
   */
  public void testOperate_1()
      throws Exception {
    SwappingMutationOperator mutOp = new SwappingMutationOperator();
    List candChroms = new Vector();
    Chromosome[] population = new Chromosome[] {
        new Chromosome(new BooleanGene(), 9),
        (new Chromosome(new IntegerGene(), 4))};
    Configuration conf = new Configuration();
    conf.setRandomGenerator(new StockRandomGenerator());
    Genotype.setConfiguration(conf);
    mutOp.operate(new Population(population), candChroms);
    /**@todo assert result is correct*/
  }

  /**
   * @author Klaus Meffert
   * @since 2.1
   */
  public void testOperate_2() {
    SwappingMutationOperator mutOp = new SwappingMutationOperator();
    List candChroms = new Vector();
    Chromosome[] population = new Chromosome[] {
        new Chromosome(new BooleanGene(), 9),
        (new Chromosome(new IntegerGene(), 4))};
    try {
      mutOp.operate(new Population(population), candChroms);
      fail();
    }
    catch (NullPointerException nex) {
      ; //this is OK
    }
  }

  /**
   * Tests if population size grows expectedly after two consecutive calls.
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.1
   */
  public void testOperate_3()
      throws Exception {
    DefaultConfiguration conf = new DefaultConfiguration();
    SwappingMutationOperator op = new SwappingMutationOperator();
    op.setStartOffset(0);
    conf.addGeneticOperator(op);
    Genotype.setConfiguration(conf);
    RandomGeneratorForTest rand = new RandomGeneratorForTest();
    rand.setNextDouble(0.45d);
    rand.setNextInt(0);
    conf.setRandomGenerator(rand);
    conf.setFitnessFunction(new TestFitnessFunction());
    Gene sampleGene = new IntegerGene(1, 10);
    Chromosome chrom = new Chromosome(sampleGene, 3);
    conf.setSampleChromosome(chrom);
    conf.setPopulationSize(6);
    Gene cgene1 = new IntegerGene(1, 10);
    cgene1.setAllele(new Integer(6));
    Gene[] genes1 = new Gene[] {
        cgene1};
    Chromosome chrom1 = new Chromosome(genes1);
    Gene cgene2 = new IntegerGene(1, 10);
    cgene2.setAllele(new Integer(9));
    Gene[] genes2 = new Gene[] {
        cgene2};
    Chromosome chrom2 = new Chromosome(genes2);
    Chromosome[] population = new Chromosome[] {
        chrom1, chrom2};
    List chroms = new Vector();
    Gene gene1 = new IntegerGene(1, 10);
    gene1.setAllele(new Integer(5));
    chroms.add(gene1);
    Gene gene2 = new IntegerGene(1, 10);
    gene2.setAllele(new Integer(7));
    chroms.add(gene2);
    Gene gene3 = new IntegerGene(1, 10);
    gene3.setAllele(new Integer(4));
    chroms.add(gene3);
    assertEquals(3, chroms.size());
    Population pop = new Population(population);
    op.operate(pop, chroms);
    assertEquals(2, pop.size());
    assertEquals(3 + 2, chroms.size());
    op.operate(pop, chroms);
    assertEquals(2, pop.size());
    assertEquals(3 + 2 + 2, chroms.size());
  }

  /**
   * Check if none of the genes is lost during swapping.
   * The checksum must stay the same. This step tests the swapping part,
   * not the the whole operator.
   * @throws Exception
   *
   * @author Audrius Meskauskas
   * @since 2.0
   */
  public void testOperate_4()
      throws Exception {
    Configuration conf = new DefaultConfiguration();
    conf.setFitnessFunction(new TestFitnessFunction());
    Genotype.setConfiguration(conf);
    int n_iterations = 20;
    RandomGenerator generator = new StockRandomGenerator();
    SwappingMutationOperator mutOp = new SwappingMutationOperator();
    mutOp.setStartOffset(0);
    for (int n_genes = 0; n_genes < 20; n_genes++) {
      Gene[] genes = new IntegerGene[n_genes];
      for (int i = 0; i < genes.length; i++) {
        genes[i] = new IntegerGene( -1000, 1000);
        genes[i].setToRandomValue(generator);
      }
      final long checksum = checksum(genes);
      Gene[] prev = new Gene[genes.length];
      for (int i = 0; i < n_iterations; i++) {
        for (int gene = 0; gene < genes.length; gene++) {
          System.arraycopy(genes, 0, prev, 0, genes.length);
          genes = mutOp.operate(generator, gene, genes);
          // checksum constant:
          assertEquals(checksum, checksum(genes));
        }
      }
    }
  }

  /**
   * Nothing to do. Test that nothing is done.
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testOperate_5() {
    Genotype.setConfiguration(new DefaultConfiguration());
    SwappingMutationOperator mutOp = new SwappingMutationOperator(0);
    mutOp.setMutationRateCalc(null);
    List candChroms = new Vector();
    BooleanGene gene1 = new BooleanGene();
    Chromosome chrom1 = new Chromosome(gene1, 1);
    chrom1.getGene(0).setAllele(Boolean.valueOf(false));
    IntegerGene gene2 = new IntegerGene(0, 10);
    Chromosome chrom2 = new Chromosome(gene2, 1);
    chrom2.getGene(0).setAllele(new Integer(3));
    candChroms.add(chrom1);
    candChroms.add(chrom2);
    mutOp.operate(null, candChroms);
    assertEquals(2, candChroms.size());
    assertEquals(chrom1, candChroms.get(0));
    assertEquals(chrom2, candChroms.get(1));
  }

  private long checksum(Gene[] a_genes) {
    long s = 0;
    for (int i = 0; i < a_genes.length; i++) {
      s += ( (IntegerGene) a_genes[i]).intValue();
    }
    return s;
  }

  /**
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testStartoffset_0() {
    SwappingMutationOperator op = new SwappingMutationOperator();
    assertEquals(1, op.getStartOffset());
    op.setStartOffset(2);
    assertEquals(2, op.getStartOffset());
    op.setStartOffset(1);
    assertEquals(1, op.getStartOffset());
    op.setStartOffset(0);
    assertEquals(0, op.getStartOffset());
  }

  /**
   * Ensures the operator is implementing Serializable
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testIsSerializable_0()
      throws Exception {
    SwappingMutationOperator op = new SwappingMutationOperator();
    assertTrue(isSerializable(op));
  }

  /**
   * Ensures that the operator and all objects contained implement Serializable
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testDoSerialize_0()
      throws Exception {
    // construct object to be serialized
    SwappingMutationOperator op = new SwappingMutationOperator(
        new DefaultCrossoverRateCalculator());
    SwappingMutationOperator o = (SwappingMutationOperator) doSerialize(op);
    assertEquals(o, op);
  }

  /**
   * Test equals with classcast object.
   *
   * @throws Exception
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testEquals_0() throws Exception {
    GeneticOperator op = new SwappingMutationOperator();
    assertFalse(op.equals(new Chromosome()));
  }
}
