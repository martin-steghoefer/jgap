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
 * Tests for MutationOperator class
 *
 * @author Klaus Meffert
 * @since 1.1
 */
public class MutationOperatorTest
    extends TestCase {

  /** String containing the CVS revision. Read out via reflection!*/
  private static final String CVS_REVISION = "$Revision: 1.14 $";

  public MutationOperatorTest() {
  }

  public void setUp() {
    Genotype.setConfiguration(null);
  }

  public static Test suite() {
    TestSuite suite = new TestSuite(MutationOperatorTest.class);
    return suite;
  }

  public void testConstruct_0() {
    MutationOperator mutOp = new MutationOperator(234);
    assertEquals(234, mutOp.m_mutationRate);
    assertNull(mutOp.getMutationRateCalc());
  }

  public void testConstruct_1() {
    MutationOperator mutOp = new MutationOperator();
    assertEquals(0, mutOp.m_mutationRate);
    assertNotNull(mutOp.getMutationRateCalc());
  }

  public void testConstruct_2() {
    MutationOperator mutOp = new MutationOperator(null);
    assertEquals(0, mutOp.m_mutationRate);
    assertNull(mutOp.getMutationRateCalc());
  }

  public void testConstruct_3() {
    IUniversalRateCalculator calc = new DefaultMutationRateCalculator();
    MutationOperator mutOp = new MutationOperator(calc);
    assertEquals(0, mutOp.m_mutationRate);
    assertEquals(calc, mutOp.getMutationRateCalc());
  }

  public void testOperate_0() throws Exception {
    Configuration conf = new DefaultConfiguration();
    conf.setFitnessFunction(new TestFitnessFunction());
    Genotype.setConfiguration(conf);
    MutationOperator mutOp = new MutationOperator();
    List candChroms = new Vector();
    Chromosome[] population = new Chromosome[]{};
    mutOp.operate(new Population(population), candChroms);
    assertEquals(candChroms.size(), population.length);
    candChroms.clear();
    RandomGeneratorForTest gen = new RandomGeneratorForTest();
    gen.setNextInt(9);
    conf.setRandomGenerator(gen);
    Genotype.setConfiguration(conf);
    Chromosome c1 = new Chromosome(new BooleanGene(),9);
    conf.setSampleChromosome(c1);
    conf.addNaturalSelector(new BestChromosomesSelector(),true);
    conf.setPopulationSize(5);
    for (int i=0;i<c1.getGenes().length;i++) {
      c1.getGene(i).setAllele(Boolean.TRUE);
    }
    Chromosome c2 = new Chromosome(new IntegerGene(),4);
    for (int i=0;i<c2.getGenes().length;i++) {
      c2.getGene(i).setAllele(new Integer(27));
    }
    population = new Chromosome[]{c1,c2};
    mutOp.operate(new Population(population), candChroms);
    assertEquals(candChroms.size(), population.length);
  }

  public void testOperate_1() throws Exception {
    MutationOperator mutOp = new MutationOperator();
    List candChroms = new Vector();
    Chromosome[] population = new Chromosome[]{new Chromosome(new BooleanGene(),9),(new Chromosome(new IntegerGene(),4))};
    Configuration conf = new Configuration();
    conf.setRandomGenerator(new StockRandomGenerator());
    Genotype.setConfiguration(conf);
    mutOp.operate(new Population(population), candChroms);
  }

  public void testOperate_2() {
    MutationOperator mutOp = new MutationOperator();
    List candChroms = new Vector();
    Chromosome[] population = new Chromosome[]{new Chromosome(new BooleanGene(),9),(new Chromosome(new IntegerGene(),4))};
    try {
      mutOp.operate(new Population(population), candChroms);
      fail();
    }
    catch (NullPointerException nex) {
        ;//this is OK
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
    assertEquals(3+2, chroms.size());
    op.operate(pop, chroms);
    assertEquals(2, pop.size());
    assertEquals(3+2+2, chroms.size());
  }
}

class TestFitnessFunction
    extends FitnessFunction {

  /**
   * @param a_subject Chromosome
   * @return double
   * @since 2.0 (until 1.1: return type int)
   */
  protected double evaluate(Chromosome a_subject) {
    //result does not matter here
    return 1.000000d;
  }

    /**@todo test against CompositeGene*/
}
