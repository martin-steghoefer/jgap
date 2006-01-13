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
import junit.framework.*;

/**
 * Tests the MutationOperator class
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class MutationOperatorTest
    extends JGAPTestCase {
  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.30 $";

  public static Test suite() {
    TestSuite suite = new TestSuite(MutationOperatorTest.class);
    return suite;
  }

  /**
   * @author Klaus Meffert
   */
  public void testConstruct_0() {
    MutationOperator mutOp = new MutationOperator(234);
    assertEquals(234, mutOp.getMutationRate());
    assertNull(mutOp.getMutationRateCalc());
  }

  /**
   * @author Klaus Meffert
   */
  public void testConstruct_1() {
    MutationOperator mutOp = new MutationOperator();
    assertEquals(0, mutOp.getMutationRate());
    assertNotNull(mutOp.getMutationRateCalc());
  }

  /**
   * @author Klaus Meffert
   */
  public void testConstruct_2() {
    MutationOperator mutOp = new MutationOperator(null);
    assertEquals(0, mutOp.getMutationRate());
    assertNull(mutOp.getMutationRateCalc());
  }

  /**
   * @author Klaus Meffert
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
   */
  public void testOperate_0()
      throws Exception {
    Configuration conf = new DefaultConfiguration();
    conf.setFitnessFunction(new TestFitnessFunction());
    Genotype.setConfiguration(conf);
    MutationOperator mutOp = new MutationOperator();
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
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   */
  public void testOperate_1()
      throws Exception {
    MutationOperator mutOp = new MutationOperator();
    List candChroms = new Vector();
    Chromosome[] population = new Chromosome[] {
        new Chromosome(new BooleanGene(), 9),
        (new Chromosome(new IntegerGene(), 4))};
    Configuration conf = new Configuration();
    conf.setRandomGenerator(new StockRandomGenerator());
    Genotype.setConfiguration(conf);
    mutOp.operate(new Population(population), candChroms);
  }

  /**
   * NullpointerException because of null Configuration
   *
   * @author Klaus Meffert
   */
  public void testOperate_2() {
    MutationOperator mutOp = new MutationOperator();
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
    GeneticOperator op = new MutationOperator();
    conf.addGeneticOperator(op);
    Genotype.setConfiguration(conf);
    RandomGeneratorForTest rand = new RandomGeneratorForTest();
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
   * Nothing to do. Test that nothing is done, then
   *
   * @author Klaus Meffert
   * @since 2.4
   */
  public void testOperate_4() {
    Genotype.setConfiguration(new DefaultConfiguration());
    MutationOperator mutOp = new MutationOperator(0);
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

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testOperate_5()
      throws Exception {
    MutationOperator mutOp = new MutationOperator();
    BooleanGene gene1 = new BooleanGene();
    Chromosome chrom1 = new Chromosome(gene1, 1);
    chrom1.getGene(0).setAllele(Boolean.valueOf(false));
    IntegerGene gene2 = new IntegerGene(0, 10);
    Chromosome chrom2 = new Chromosome(gene2, 1);
    chrom2.getGene(0).setAllele(new Integer(3));
    Chromosome[] chroms = new Chromosome[] {
        chrom1, chrom2};
    Configuration conf = new Configuration();
    conf.setPopulationSize(5);
    RandomGeneratorForTest rn = new RandomGeneratorForTest();
    rn.setNextInt(0);
    rn.setNextDouble(0.8d);
    conf.setRandomGenerator(rn);
    Genotype.setConfiguration(conf);
    Population pop = new Population(chroms);
    mutOp.operate(pop, pop.getChromosomes());
    assertEquals(2 + 2, pop.getChromosomes().size());
    //old gene
    assertFalse( ( (BooleanGene) pop.getChromosome(0).getGene(0))
                 .booleanValue());
    //mutated gene
    assertTrue( ( (BooleanGene) pop.getChromosome(2).getGene(0)).booleanValue());
    //old gene
    assertEquals(3, ( (IntegerGene) pop.getChromosome(1).getGene(0)).intValue());
    //mutated gene
    assertEquals( (int) Math.round(3 + (10 - 0) * ( -1 + 0.8d * 2)),
                 ( (IntegerGene) pop.getChromosome(3).getGene(0)).intValue());
  }

  /**
   * Following should be possible without exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testOperate_6() {
    Genotype.setConfiguration(new DefaultConfiguration());
    MutationOperator mutOp = new MutationOperator(0);
    mutOp.setMutationRateCalc(null);
    mutOp.operate(null, null);
  }

  /**
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testOperate_6_2() {
    Genotype.setConfiguration(new DefaultConfiguration());
    MutationOperator mutOp = new MutationOperator(0);
    mutOp.setMutationRateCalc(new DefaultMutationRateCalculator());
    mutOp.operate(null, null);
  }

  /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.2
   */
  public void testOperate_7()
      throws Exception {
    MutationOperator mutOp = new MutationOperator();
    BooleanGene gene1 = new BooleanGene();
    CompositeGene comp1 = new CompositeGene();
    comp1.addGene(gene1);
    Chromosome chrom1 = new Chromosome(comp1, 1);
    ( (CompositeGene) chrom1.getGene(0)).geneAt(0).setAllele(
        Boolean.valueOf(false));
    IntegerGene gene2 = new IntegerGene(0, 10);
    CompositeGene comp2 = new CompositeGene();
    comp2.addGene(gene2);
    Chromosome chrom2 = new Chromosome(comp2, 1);
    ( (CompositeGene) chrom2.getGene(0)).geneAt(0).setAllele(new Integer(3));
    Chromosome[] chroms = new Chromosome[] {
        chrom1, chrom2};
    Configuration conf = new Configuration();
    conf.setPopulationSize(5);
    RandomGeneratorForTest rn = new RandomGeneratorForTest();
    rn.setNextInt(0);
    rn.setNextDouble(0.8d);
    conf.setRandomGenerator(rn);
    Genotype.setConfiguration(conf);
    Population pop = new Population(chroms);
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
    //mutated gene
    assertEquals( (int) Math.round(3 + (10 - 0) * ( -1 + 0.8d * 2)),
                 ( (
        IntegerGene) ( (CompositeGene) pop.getChromosome(3).getGene(0)).
                  geneAt(0)).intValue());
  }

  /**
   * Considers IGeneticOperatorConstraint. Here, the mutation of a BooleanGene
   * is forbidden by that constraint.
   *
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testOperate_8()
      throws Exception {
    MutationOperator mutOp = new MutationOperator();
    BooleanGene gene1 = new BooleanGene();
    Chromosome chrom1 = new Chromosome(gene1, 1);
    chrom1.getGene(0).setAllele(Boolean.valueOf(false));

    IntegerGene gene2 = new IntegerGene(0, 10);
    Chromosome chrom2 = new Chromosome(gene2, 1);
    chrom2.getGene(0).setAllele(new Integer(3));
    Chromosome[] chroms = new Chromosome[] {
        chrom1, chrom2};
    Configuration conf = new Configuration();
    conf.setPopulationSize(5);
    RandomGeneratorForTest rn = new RandomGeneratorForTest();

    rn.setNextInt(0);
    rn.setNextInt(0);
    rn.setNextDouble(0.8d);
    conf.setRandomGenerator(rn);
    Genotype.setConfiguration(conf);

    IGeneticOperatorConstraint constraint = new
        GeneticOperatorConstraintForTest();
    Genotype.getConfiguration().getJGAPFactory().setGeneticOperatorConstraint(
        constraint);

    Population pop = new Population(chroms);
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
   * Ensures operator is implementing Serializable
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testIsSerializable_0()
      throws Exception {
    MutationOperator op = new MutationOperator();
    assertTrue(isSerializable(op));
  }

  /**
   * Ensures that operator and all objects contained implement Serializable
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testDoSerialize_0()
      throws Exception {
    // construct object to be serialized
    IUniversalRateCalculator calc = new DefaultCrossoverRateCalculator();
    MutationOperator op = new MutationOperator(calc);
    Object o = doSerialize(op);
    assertEquals(o, op);
  }

  public class GeneticOperatorConstraintForTest
      implements IGeneticOperatorConstraint {
    public boolean isValid(Population a_pop, List a_chromosomes,
                           GeneticOperator a_caller) {
      Chromosome chrom = (Chromosome)a_chromosomes.get(0);
      Gene gene = chrom.getGene(0);
      return gene.getClass() != BooleanGene.class;
    }
  }

  /**
   * Test equals with classcast object.
   *
   * @throws Exception
   * @author Klaus Meffert
   * @since 2.6
   */
  public void testEquals_0() throws Exception {
    GeneticOperator op = new MutationOperator();
    assertFalse(op.equals(new Chromosome()));
  }
}
