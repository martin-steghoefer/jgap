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
 * Tests the TwoWayMutationOperator class.
 *
 * @author Klaus Meffert
 * @since 3.1
 */
public class TwoWayMutationOperatorTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.4 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(TwoWayMutationOperatorTest.class);
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
   * @since 3.1
   */
  public void testConstruct_0()
      throws Exception {
    TwoWayMutationOperator mutOp = new TwoWayMutationOperator(conf, 234);
    assertEquals(234, mutOp.getMutationRate());
    assertNull(mutOp.getMutationRateCalc());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public void testConstruct_1()
      throws Exception {
    Genotype.setStaticConfiguration(conf);
    TwoWayMutationOperator mutOp = new TwoWayMutationOperator();
    assertEquals(0, mutOp.getMutationRate());
    assertNotNull(mutOp.getMutationRateCalc());
    assertSame(conf, mutOp.getConfiguration());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public void testConstruct_2()
      throws Exception {
    TwoWayMutationOperator mutOp = new TwoWayMutationOperator(conf, null);
    assertEquals(0, mutOp.getMutationRate());
    assertNull(mutOp.getMutationRateCalc());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public void testConstruct_3()
      throws Exception {
    IUniversalRateCalculator calc = new DefaultMutationRateCalculator(conf);
    TwoWayMutationOperator mutOp = new TwoWayMutationOperator(conf, calc);
    assertEquals(0, mutOp.getMutationRate());
    assertEquals(calc, mutOp.getMutationRateCalc());
  }

  /**
   * Ensure that size of mutated set of chromosomes equals the size of original
   * chromosomes.
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public void testOperate_0()
      throws Exception {
    Configuration conf = new DefaultConfiguration();
    conf.setFitnessFunction(new TestFitnessFunction());
    TwoWayMutationOperator mutOp = new TwoWayMutationOperator(conf);
    List candChroms = new Vector();
    Chromosome[] population = new Chromosome[] {};
    mutOp.operate(new Population(conf, population), candChroms);
    assertEquals(candChroms.size(), population.length);
  }

  /**
   * Ensure that size of mutated set of chromosomes equals the size of original
   * chromosomes.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public void testOperate_0_2()
      throws Exception {
    Configuration conf = new DefaultConfiguration();
    conf.setFitnessFunction(new TestFitnessFunction());
    TwoWayMutationOperator mutOp = new TwoWayMutationOperator(conf,
        new DefaultMutationRateCalculator(conf));
    List candChroms = new Vector();
    RandomGeneratorForTesting gen = new RandomGeneratorForTesting();
    gen.setNextInt(9);
    conf.setRandomGenerator(gen);
    Chromosome c1 = new Chromosome(conf, new BooleanGene(conf), 9);
    conf.setSampleChromosome(c1);
    conf.addNaturalSelector(new BestChromosomesSelector(conf), true);
    conf.setPopulationSize(5);
    for (int i = 0; i < c1.getGenes().length; i++) {
      c1.getGene(i).setAllele(Boolean.TRUE);
    }
    Chromosome c2 = new Chromosome(conf, new IntegerGene(conf), 4);
    for (int i = 0; i < c2.getGenes().length; i++) {
      c2.getGene(i).setAllele(new Integer(27));
    }
    Chromosome[] population = new Chromosome[] {
        c1, c2};
    mutOp.operate(new Population(conf, population), candChroms);
    assertEquals(candChroms.size(), population.length);
  }

  /**
   * Mutating with different types of genes contained in the population should
   * be possible without exception.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public void testOperate_1()
      throws Exception {
    List candChroms = new Vector();
    Configuration conf = new Configuration();
    conf.setPopulationSize(3);
    conf.setRandomGenerator(new StockRandomGenerator());
    TwoWayMutationOperator mutOp = new TwoWayMutationOperator(conf,
        new DefaultMutationRateCalculator(conf));
    Chromosome[] population = new Chromosome[] {
        new Chromosome(conf, new BooleanGene(conf), 9),
        (new Chromosome(conf, new IntegerGene(conf), 4))};
    mutOp.operate(new Population(conf, population), candChroms);
  }

  /**
   * NullpointerException because of null Configuration.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public void testOperate_2()
      throws Exception {
    TwoWayMutationOperator mutOp = new TwoWayMutationOperator(conf);
    List candChroms = new Vector();
    Chromosome[] population = new Chromosome[] {
        new Chromosome(conf, new BooleanGene(conf), 9),
        (new Chromosome(conf, new IntegerGene(conf), 4))};
    try {
      mutOp.operate(new Population(null, population), candChroms);
      fail();
    } catch (InvalidConfigurationException nex) {
      ; //this is OK
    }
  }

  /**
   * Tests if population size grows expectedly after two consecutive calls.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public void testOperate_3()
      throws Exception {
    DefaultConfiguration conf = new DefaultConfiguration();
    TwoWayMutationOperator op = new TwoWayMutationOperator(conf,
        new DefaultMutationRateCalculator(conf));
    conf.addGeneticOperator(op);
    RandomGeneratorForTesting rand = new RandomGeneratorForTesting();
    rand.setNextInt(0);
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
    cgene2.setAllele(new Integer(9));
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
    assertEquals(3, chroms.size());
    Population pop = new Population(conf, population);
    op.operate(pop, chroms);
    assertEquals(2, pop.size());
    assertEquals(3 + 2, chroms.size());
    op.operate(pop, chroms);
    assertEquals(2, pop.size());
    assertEquals(3 + 2 + 2, chroms.size());
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert (adapted from Dan Clark's test of MutationOperator)
   * @since 3.1
   */
  public void testOperate_3_1()
      throws Exception {
    DefaultConfiguration conf = new DefaultConfiguration();
    GeneticOperator op = new TwoWayMutationOperator(conf, 10);
    conf.addGeneticOperator(op);
    RandomGeneratorForTesting rand = new RandomGeneratorForTesting();
    // 0 in this sequence represents a gene to be mutated
    // thus, the middle gene of each chromosome should be mutated
    rand.setNextIntSequence(new int[] {0, 0, 1});
    rand.setNextDouble(0.7d);
    conf.setRandomGenerator(rand);
    conf.setFitnessFunction(new TestFitnessFunction());
    Gene sampleGene = new IntegerGene(conf, 0, 9);
    Chromosome chrom = new Chromosome(conf, sampleGene, 3);
    conf.setSampleChromosome(chrom);
    conf.setPopulationSize(6);
    Gene[] genes1 = new Gene[3];
    for (int i = 0; i < genes1.length; i++) {
      genes1[i] = new IntegerGene(conf, 0, 9);
      genes1[i].setAllele(new Integer(i));
    }
    Chromosome chrom1 = new Chromosome(conf, genes1);
    Gene[] genes2 = new Gene[3];
    for (int i = 0; i < genes2.length; i++) {
      genes2[i] = new IntegerGene(conf, 0, 9);
      genes2[i].setAllele(new Integer(i + 3));
    }
    Chromosome chrom2 = new Chromosome(conf, genes2);
    Chromosome[] population = new Chromosome[] {
        chrom1, chrom2};
    // this will cause an increase of 4 in mutated values
    // original + (0.7*2 - 1) * 10 (range of allele values)
    List chroms = new Vector();
    Population pop = new Population(conf, population);
    op.operate(pop, chroms);
    assertEquals(2, chroms.size());
    // test chromosome 1 - 3rd gene should be different
    Chromosome c1 = (Chromosome) chroms.get(0);
    assertEquals(new Integer(0), c1.getGene(0).getAllele());
    assertEquals(new Integer(1), c1.getGene(1).getAllele());
    assertEquals(new Integer(2 + 4), c1.getGene(2).getAllele());
    // test chromosome 2 - 2nd gene should be different
    Chromosome c2 = (Chromosome) chroms.get(1);
    assertEquals(new Integer(3), c2.getGene(0).getAllele());
    assertEquals(new Integer(4), c2.getGene(1).getAllele());
    assertEquals(new Integer(5 + 4), c2.getGene(2).getAllele());
    op.operate(pop, chroms);
    assertEquals(3, chroms.size());
  }

  /**
   * Ensure that nothing is done.
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public void testOperate_4()
      throws Exception {
    DefaultConfiguration conf = new DefaultConfiguration();
    TwoWayMutationOperator mutOp = new TwoWayMutationOperator(conf, 0);
    mutOp.setMutationRateCalc(null);
    List candChroms = new Vector();
    BooleanGene gene1 = new BooleanGene(conf);
    Chromosome chrom1 = new Chromosome(conf, gene1, 1);
    chrom1.getGene(0).setAllele(Boolean.valueOf(false));
    IntegerGene gene2 = new IntegerGene(conf, 0, 10);
    Chromosome chrom2 = new Chromosome(conf, gene2, 1);
    chrom2.getGene(0).setAllele(new Integer(3));
    candChroms.add(chrom1);
    candChroms.add(chrom2);
    mutOp.operate(null, candChroms);
    assertEquals(2, candChroms.size());
    assertEquals(chrom1, candChroms.get(0));
    assertEquals(chrom2, candChroms.get(1));
  }

  /**
   * Mutation, especially tested for an IntegerGene.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public void testOperate_5()
      throws Exception {
    Configuration conf = new Configuration();
    conf.setPopulationSize(5);
    RandomGeneratorForTesting rn = new RandomGeneratorForTesting();
    rn.setNextInt(0);
    rn.setNextDouble(0.8d); //C
    conf.setRandomGenerator(rn);
    BooleanGene gene1 = new BooleanGene(conf);
    Chromosome chrom1 = new Chromosome(conf, gene1, 1);
    chrom1.getGene(0).setAllele(Boolean.valueOf(false));
    IntegerGene gene2 = new IntegerGene(conf, 0, 10); //B: B1, B2
    Chromosome chrom2 = new Chromosome(conf, gene2, 1);
    chrom2.getGene(0).setAllele(new Integer(3)); //A
    Chromosome[] chroms = new Chromosome[] {
        chrom1, chrom2};
    TwoWayMutationOperator mutOp = new TwoWayMutationOperator(conf,
        new DefaultMutationRateCalculator(conf));
    Population pop = new Population(conf, chroms);
    mutOp.operate(pop, pop.getChromosomes());
    // now we should have the double number of chromosomes because the target
    // list is the same as the source list of chromosomes
    assertEquals(2 + 2, pop.getChromosomes().size());
    //old gene
    assertFalse( ( (BooleanGene) pop.getChromosome(0).getGene(0))
                .booleanValue());
    //mutated gene
    assertTrue( ( (BooleanGene) pop.getChromosome(2).getGene(0)).booleanValue());
    //old gene
    assertEquals(3, ( (IntegerGene) pop.getChromosome(1).getGene(0)).intValue());
    //mutated gene: A + (B2-B1) * (-1 + C * 2) --> see IntegerGene.applyMutation
    //        A, B1, B2, C: see comments above
    //        -1 + C * 2: see IntegerGene.applyMutation
    assertEquals( (int) Math.round(3 + (10 - 0) * ( -1 + 0.8d * 2)),
                 ( (IntegerGene) pop.getChromosome(3).getGene(0)).intValue());
  }

  /**
   * Mutation, especially tested for an IntegerGene. Uses a CompositeGene.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public void testOperate_5_2()
      throws Exception {
    Configuration conf = new Configuration();
    conf.setPopulationSize(5);
    BooleanGene gene1 = new BooleanGene(conf);
    CompositeGene comp1 = new CompositeGene(conf);
    comp1.addGene(gene1);
    Chromosome chrom1 = new Chromosome(conf, comp1, 1);
    ( (CompositeGene) chrom1.getGene(0)).geneAt(0).setAllele(
        Boolean.valueOf(false));
    IntegerGene gene2 = new IntegerGene(conf, 0, 10);
    CompositeGene comp2 = new CompositeGene(conf);
    comp2.addGene(gene2);
    Chromosome chrom2 = new Chromosome(conf, comp2, 1);
    ( (CompositeGene) chrom2.getGene(0)).geneAt(0).setAllele(new Integer(3));
    Chromosome[] chroms = new Chromosome[] {
        chrom1, chrom2};
    TwoWayMutationOperator mutOp = new TwoWayMutationOperator(conf,
        new DefaultMutationRateCalculator(conf));
    RandomGeneratorForTesting rn = new RandomGeneratorForTesting();
    rn.setNextInt(0);
    rn.setNextDouble(0.8d);
    conf.setRandomGenerator(rn);
    Population pop = new Population(conf, chroms);
    mutOp.operate(pop, pop.getChromosomes());
    assertEquals(2 + 2, pop.getChromosomes().size());
    //old gene
    assertFalse( ( (BooleanGene) ( (CompositeGene) pop.getChromosome(0).getGene(
        0)).geneAt(0)).booleanValue());
    //mutated gene
    assertTrue( ( (BooleanGene) ( (CompositeGene) pop.getChromosome(2).getGene(
        0)).geneAt(0)).booleanValue());
    //old gene
    assertEquals(3,
                 ( (IntegerGene) ( (CompositeGene) pop.getChromosome(1).
                                  getGene(0)).geneAt(0)).intValue());
    //mutated gene: A + (B2-B1) * (-1 + C * 2) --> see IntegerGene.applyMutation
    //        A, B1, B2, C: see comments above
    //        -1 + C * 2: see IntegerGene.applyMutation
    assertEquals( (int) Math.round(3 + (10 - 0) * ( -1 + 0.8d * 2)),
                 ( (
                     IntegerGene) ( (CompositeGene) pop.getChromosome(3).
                                   getGene(0)).
                  geneAt(0)).intValue());
  }

  /**
   * Following should be possible without exception.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public void testOperate_6()
      throws Exception {
    Configuration conf = new DefaultConfiguration();
    MutationOperator mutOp = new MutationOperator(conf, 0);
    mutOp.setMutationRateCalc(null);
    mutOp.operate(null, null);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public void testOperate_6_2()
      throws Exception {
    Configuration conf = new DefaultConfiguration();
    TwoWayMutationOperator mutOp = new TwoWayMutationOperator(conf, 0);
    mutOp.setMutationRateCalc(new DefaultMutationRateCalculator(conf));
    mutOp.operate(null, null);
  }

  /**
   * Considers IGeneticOperatorConstraint. Here, the mutation of a BooleanGene
   * is forbidden by that constraint.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public void testOperate_8()
      throws Exception {
    Configuration conf = new Configuration();
    conf.setPopulationSize(5);
    RandomGeneratorForTesting rn = new RandomGeneratorForTesting();
    rn.setNextInt(0);
    rn.setNextInt(0);
    rn.setNextDouble(0.8d);
    conf.setRandomGenerator(rn);
    BooleanGene gene1 = new BooleanGene(conf);
    Chromosome chrom1 = new Chromosome(conf, gene1, 1);
    chrom1.getGene(0).setAllele(Boolean.valueOf(false));
    IntegerGene gene2 = new IntegerGene(conf, 0, 10);
    Chromosome chrom2 = new Chromosome(conf, gene2, 1);
    chrom2.getGene(0).setAllele(new Integer(3));
    Chromosome[] chroms = new Chromosome[] {
        chrom1, chrom2};
    TwoWayMutationOperator mutOp = new TwoWayMutationOperator(conf,
        new DefaultMutationRateCalculator(conf));
    IGeneticOperatorConstraint constraint = new
        GeneticOperatorConstraintForTesting();
    conf.getJGAPFactory().setGeneticOperatorConstraint(
        constraint);
    Population pop = new Population(conf, chroms);
    mutOp.operate(pop, pop.getChromosomes());
    // +1 (not +2) because only IntegerGene should have been mutated.
    assertEquals(2 + 1, pop.getChromosomes().size());
    //old gene
    assertFalse( ( (BooleanGene) pop.getChromosome(0).getGene(0)).booleanValue());
    //old gene
    assertEquals(3, ( (IntegerGene) pop.getChromosome(1).
                     getGene(0)).intValue());
    //mutated gene
    assertEquals( (int) Math.round(3 + (10 - 0) * ( -1 + 0.8d * 2)),
                 ( (IntegerGene) pop.getChromosome(2).getGene(0)).intValue());
  }

  /**
   * Ensures operator is implementing Serializable.
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public void testIsSerializable_0()
      throws Exception {
    TwoWayMutationOperator op = new TwoWayMutationOperator(conf);
    assertTrue(isSerializable(op));
  }

  /**
   * Ensures that operator and all objects contained implement Serializable.
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public void testDoSerialize_0()
      throws Exception {
    // construct object to be serialized
    IUniversalRateCalculator calc = new DefaultCrossoverRateCalculator(conf);
    TwoWayMutationOperator op = new TwoWayMutationOperator(conf, calc);
    Object o = doSerialize(op);
    assertEquals(o, op);
  }

  public class GeneticOperatorConstraintForTesting
      implements IGeneticOperatorConstraint {
    public boolean isValid(Population a_pop, List a_chromosomes,
                           GeneticOperator a_caller) {
      Chromosome chrom = (Chromosome) a_chromosomes.get(0);
      Gene gene = chrom.getGene(0);
      return gene.getClass() != BooleanGene.class;
    }
  }
  /**
   * Test equals with classcast object.
   *
   * @throws Exception
   * @author Klaus Meffert
   * @since 3.1
   */
  public void testEquals_0()
      throws Exception {
    GeneticOperator op = new TwoWayMutationOperator(conf);
    assertFalse(op.equals(new Chromosome(conf)));
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.1
   */
  public void testCompareTo_0()
      throws Exception {
    TwoWayMutationOperator op = new TwoWayMutationOperator(conf);
    assertEquals(1, op.compareTo(null));
    TwoWayMutationOperator op2 = new TwoWayMutationOperator(conf);
    assertEquals(0, op.compareTo(op2));
    op = new TwoWayMutationOperator(conf, 3);
    assertEquals( -1, op.compareTo(op2));
    assertEquals(1, op2.compareTo(op));
    op = new TwoWayMutationOperator(conf,
                                    new DefaultMutationRateCalculator(conf));
    assertEquals(0, op.compareTo(op2));
    op = new TwoWayMutationOperator(conf, 3);
    op2 = new TwoWayMutationOperator(conf, 4);
    assertEquals( -1, op.compareTo(op2));
    assertEquals(1, op2.compareTo(op));
  }
}
